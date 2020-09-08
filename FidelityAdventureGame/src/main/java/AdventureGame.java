import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import javax.swing.plaf.synth.SynthScrollBarUI;

public class AdventureGame {

    public static void main(String[] args) throws Exception {
        // Get game info from JSON file
        JSONObject gameInfo = (JSONObject) new JSONParser().parse(new FileReader("src/main/charliegame.json"));

        // Get items and rooms
        Map<Integer, String> items = getItems(gameInfo);
        Map<Integer, Room> rooms = getRooms(gameInfo);

        // Create Hero
        Hero hero = new Hero();

        // Create Scanner to read user input
        Scanner read = new Scanner(System.in);

        boolean gameRunning = true;
        while (gameRunning) {
            int heroLocation = hero.getLocation();
            int itemInHand = hero.getItem();

            // Get current room info
            Room currentRoom = rooms.get(heroLocation);
            String currentRoomDesc = currentRoom.getDescription();
            ArrayList<Integer> currentRoomItems = currentRoom.getItemsInRoom();

            // Print info out
            System.out.println(currentRoomDesc);
            for (int item : currentRoomItems) {
                String itemInfo = items.get(item);
                System.out.println("You see a " + itemInfo + ".");
            }

            if (itemInHand != 0) {
                System.out.println("You are carrying a " + items.get(itemInHand) + ".");
            }
            currentRoom.printRoom();

            System.out.print("What would you like to do? ");
            String instructions = read.nextLine().toUpperCase();

            if (instructions.contains("MOVE")) {
                moveHero(hero, currentRoom, instructions);
            }
            else if (instructions.contains("GET")) {
                pickUpItem(hero, currentRoom, items, instructions);
            }
            else if (instructions.contains("DROP")) {
                dropItem(hero, currentRoom, items, instructions);
            }
            else {
                System.out.println("Invalid command, try 'Move [direction]', 'Get [item name]', or 'Drop [item name]'");
            }

            if (currentRoom.getItemsInRoom().contains(currentRoom.getWinItem())) {
                System.out.println(currentRoom.getWinMessage());
                gameRunning = false;
            }

            System.out.println();
        }
    }

    /**
     *
     * @param gameInfo JSONObject containing Game Info
     * @return HashMap of Item objects
     */
    private static Map<Integer, String> getItems(JSONObject gameInfo) {
        Map<Integer, String> listOfItems = new HashMap<>();
        JSONArray items = ((JSONArray) gameInfo.get("Items"));

        for (Object item : items) {
            // Get JSONObject of current item
            JSONObject itemInfo = (JSONObject) item;

            // Get item id and name
            int itemId = (int) ((long) itemInfo.get("id"));
            String itemName = (String) itemInfo.get("name");

            listOfItems.put(itemId, itemName);
        }

        return listOfItems;
    }

    /**
     * Parses JSONObject to create Room objects and returns a hash map of created Room objects
     *
     * @param gameInfo JSONObject containing Game Info
     * @return HashMap of Room objects
     */
    private static Map<Integer, Room> getRooms(JSONObject gameInfo) {
        Map<Integer, Room> listOfRooms = new HashMap<>();
        JSONArray rooms = ((JSONArray) gameInfo.get("Rooms"));

        for (Object room : rooms) {
            // Get JSONObject of current room
            JSONObject roomInfo = (JSONObject) room;

            // Get the id and description of current room
            int roomId = (int) ((long) roomInfo.get("id"));
            String roomDesc = (String) roomInfo.get("description");

            // Get win item id if exists
            int winItemId = 0;
            if (roomInfo.get("winningItemId") != null) {
                winItemId = (int) ((long) roomInfo.get("winningItemId"));
            }
            String winMsg = (String) roomInfo.get("winningMessage");

            // Create a new Room
            Room createdRoom = new Room(roomId, roomDesc, winItemId, winMsg);

            // Get and set room connections
            JSONArray connections = (JSONArray) roomInfo.get("Connections");
            setRoomConnections(createdRoom, connections);

            // Get and set room items
            JSONArray roomItems = (JSONArray) roomInfo.get("itemsinRoom");
            setRoomItems(createdRoom, roomItems);

            // Add Room to list
            listOfRooms.put(roomId, createdRoom);
        }

        return listOfRooms;
    }

    /**
     * Helper method to set room connections
     *
     * @param room the room to set connections for
     * @param connections JSONArray of room connections
     */
    private static void setRoomConnections(Room room, JSONArray connections) {
        if (connections.size() == 0) {
            return;
        }

        for (Object connection : connections) {
            JSONObject roomConnection = (JSONObject) connection;

            if (roomConnection.get("South") != null) {
                room.setSouthConnection((int) ((long)roomConnection.get("South")));
            }

            if (roomConnection.get("North") != null) {
                room.setNorthConnection((int) ((long)roomConnection.get("North")));
            }

            if (roomConnection.get("West") != null) {
                room.setWestConnection((int) ((long)roomConnection.get("West")));
            }

            if (roomConnection.get("East") != null) {
                room.setEastConnection((int) ((long)roomConnection.get("East")));
            }
        }
    }

    /**
     * Helper method to set room items for a given room
     *
     * @param room Room to set items for
     * @param roomItems JSONArray of items to be set
     */
    private static void setRoomItems(Room room, JSONArray roomItems) {
        if (roomItems == null || roomItems.size() == 0) {
            return;
        }

        for (Object roomItem : roomItems) {
            JSONObject item = (JSONObject) roomItem;
            int itemToAdd = (int) ((long) item.get("id"));

            // add to list of items in room
            room.addItem(itemToAdd);
        }
    }

    /**
     * Helper function to move hero towards a direction (SOUTH, NORTH, WEST, EAST)
     *
     * @param hero the hero to move
     * @param room the current room the hero is in
     * @param instruction move instructions
     */
    private static void moveHero(Hero hero, Room room, String instruction) {
        String[] instructionSplit = instruction.split(" ");

        if (!instructionSplit[0].equals("MOVE")) {
            System.out.println("Move command must start with 'Move', try again...");
            return;
        }

        if (instructionSplit.length > 2) {
            System.out.println("Move command is too long, try 'Move [direction to move]");
            return;
        }

        if (instructionSplit[1].equals("SOUTH")) {
            if (room.getSouthConnection() != 0) {
                hero.setLocation(room.getSouthConnection());
            }
            else {
                System.out.println("Can not move south, try again...");
            }
        }
        else if (instructionSplit[1].equals("NORTH")) {
            if (room.getNorthConnection() != 0) {
                hero.setLocation(room.getNorthConnection());
            }
            else {
                System.out.println("Can not move north, try again...");
            }
        }
        else if (instructionSplit[1].equals("WEST")) {
            if (room.getWestConnection() != 0) {
                hero.setLocation(room.getWestConnection());
            }
            else {
                System.out.println("Can not move west, try again...");
            }
        }
        else if (instructionSplit[1].equals("EAST")) {
            if (room.getEastConnection() != 0) {
                hero.setLocation(room.getEastConnection());
            }
            else {
                System.out.println("Can not move east, try again...");
            }
        }
        else {
            System.out.println("Invalid direction, try 'south', 'north', 'west' or 'east'");
        }
    }

    private static void pickUpItem(Hero hero, Room room, Map<Integer, String> items, String instruction) {
        String[] instructionSplit = instruction.split(" ");
        String item = instruction.split("GET ")[1];

        if (!instructionSplit[0].equals("GET")) {
            System.out.println("Get command must start with 'get', try again...");
            return;
        }

        int itemToPickUpId = 0;
        for (Map.Entry<Integer, String> entry : items.entrySet()) {
            if (entry.getValue().toUpperCase().equals(item)) {
                itemToPickUpId = entry.getKey();
            }
        }

        if (!room.getItemsInRoom().contains(itemToPickUpId)) {
            System.out.println("Item does not exist in this room, try again...");
            return;
        }

        if (hero.getItem() != 0) {
            // Drop current item
            room.addItem(hero.getItem());
        }
        room.removeItem(itemToPickUpId);
        hero.setItem(itemToPickUpId);
    }

    private static void dropItem(Hero hero, Room room, Map<Integer, String> items, String instruction) {
        String[] instructionSplit = instruction.split(" ");
        String item = instruction.split("DROP ")[1];

        if (!instructionSplit[0].equals("DROP")) {
            System.out.println("Drop command must start with 'Drop', try again...");
            return;
        }

        if (hero.getItem() == 0) {
            System.out.println("The hero is not holding an item, try again...");
            return;
        }

        int itemToDrop = 0;
        for (Map.Entry<Integer, String> entry : items.entrySet()) {
            if (entry.getValue().toUpperCase().equals(item)) {
                itemToDrop = entry.getKey();
            }
        }

        if (hero.getItem() == itemToDrop) {
            hero.setItem(0);
            room.addItem(itemToDrop);
        }
        else {
            System.out.println("The hero is not holding the specified item, try again...");
        }
    }
}
