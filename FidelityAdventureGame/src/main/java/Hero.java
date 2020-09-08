public class Hero {
    private int location;
    private int item;

    // Hero constructor
    // default to first room and no item
    public Hero() {
        location = 1;
        item = 0;
    }

    // Getters
    public int getLocation() {
        return location;
    }

    public int getItem() {
        return item;
    }

    // Setters
    public void setLocation(int room) {
        location = room;
    }

    public void setItem(int item) {
        this.item = item;
    }
}
