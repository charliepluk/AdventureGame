import java.util.ArrayList;

/**
 *
 */
public class Room {
    // ID and DESC should not be changed
    private final int id;
    private final int winItem;
    private final String desc;
    private final String winMsg;

    // Connections in a room
    // Set to room id, default to 0
    private int southConnection = 0;
    private int northConnection = 0;
    private int westConnection  = 0;
    private int eastConnection  = 0;

    // List to hold items in room
    private ArrayList<Integer> itemsInRoom = new ArrayList<>();

    public Room(int id, String desc, int winItem, String winMsg) {
        this.id = id;
        this.desc = desc;
        this.winItem = winItem;
        this.winMsg = winMsg;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getDescription() {
        return desc;
    }

    public int getWinItem() {
        return winItem;
    }

    public String getWinMessage() {
        return winMsg;
    }

    // Returns id of room south of current room
    public int getSouthConnection() {
        return southConnection;
    }

    // Returns id of room north of current room
    public int getNorthConnection() {
        return northConnection;
    }

    // Returns id of room west of current room
    public int getWestConnection() {
        return westConnection;
    }

    // Returns id of room east of current room
    public int getEastConnection() {
        return eastConnection;
    }

    public ArrayList<Integer> getItemsInRoom() {
        return itemsInRoom;
    }

    // Setters
    // Sets id of room south of current room
    public void setSouthConnection(int newSouthConnect) {
        this.southConnection = newSouthConnect;
    }

    // Sets id of room north of current room
    public void setNorthConnection(int newNorthConnect) {
        this.northConnection = newNorthConnect;
    }

    // Sets id of room west of current room
    public void setWestConnection(int newWestConnect) {
        this.westConnection = newWestConnect;
    }

    // Sets id of room east of current room
    public void setEastConnection(int newEastConnect) {
        this.eastConnection = newEastConnect;
    }

    // Helper methods
    public void addItem(int itemToAdd) {
        this.itemsInRoom.add(itemToAdd);
    }

    public void removeItem(int itemToRemove) {
        for (int i = 0; i < this.itemsInRoom.size(); i++) {
            if ( this.itemsInRoom.get(i) == itemToRemove) {
                this.itemsInRoom.remove(i);
                return;
            }
        }
    }

    public void printRoom() {
        if (northConnection == 0) {
            System.out.println("+------------+");
        }
        else {
            System.out.println("+----    ----+");
        }

        System.out.println("|            |");

        if (westConnection != 0 && eastConnection != 0) {
            System.out.println("              ");
        }
        else if (westConnection == 0 && eastConnection != 0) {
            System.out.println("|             ");
        }
        else if (westConnection != 0 && eastConnection == 0) {
            System.out.println("             |");
        }
        else {
            System.out.println("|            |");
        }

        System.out.println("|            |");

        if (southConnection == 0) {
            System.out.println("+------------+");
        }
        else {
            System.out.println("+----    ----+");
        }
    }
}
