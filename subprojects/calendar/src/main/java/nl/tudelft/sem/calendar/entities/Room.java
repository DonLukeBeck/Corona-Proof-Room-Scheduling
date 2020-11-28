package nl.tudelft.sem.calendar.entities;
public class Room {
    private int roomId;
    private int capacity;
    private String name;

    public Room(int roomId, int capacity, String name) {
        this.roomId = roomId;
        this.capacity = capacity;
        this.name = name;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}