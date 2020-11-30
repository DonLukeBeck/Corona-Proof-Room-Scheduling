package nl.tudelft.sem.calendar.entities;

public class Room {
    private int roomId;
    private int capacity;
    private String name;

    /**
     * Creates a room object with a given id, capacity and name.
     *
     * @param roomId   and integer representing the roomId
     * @param capacity an integer expressing the capacity of the room
     * @param name     a string representing the name of the room
     */
    public Room(int roomId, int capacity, String name) {
        this.roomId = roomId;
        this.capacity = capacity;
        this.name = name;
    }

    /**
     * Returns the roomId of this room.
     *
     * @return the room
     */
    public int getRoomId() {
        return roomId;
    }

    /**
     * Sets the roomId of this room.
     *
     * @param roomId the roomId of this room
     */
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    /**
     * Returns the capacity of this room.
     *
     * @return the capacity of this room
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the capacity of this room.
     *
     * @param capacity the capacity of this room
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Returns the name of this room.
     *
     * @return the name of the room
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this course.
     *
     * @param name the name of the course
     */
    public void setName(String name) {
        this.name = name;
    }
}