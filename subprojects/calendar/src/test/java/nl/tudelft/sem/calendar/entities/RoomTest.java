package nl.tudelft.sem.calendar.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoomTest {
    private Room room;
    private int roomId;
    private int capacity;
    private String name;

    /**
     * Creates a room and attributes that are used for verification.
     */
    @BeforeEach
    void setUp() {
        roomId = 10;
        capacity = 200;
        name = "Drebbelweg IZ-2";
        room = new Room(roomId, capacity, name);
    }

    /**
     * Tests the getter for the roomId.
     */
    @Test
    void testGetRoomId() {
        assertEquals(roomId, room.getRoomId());
    }

    /**
     * Tests the getter for the room name.
     */
    @Test
    void testGetName() {
        assertEquals(name, room.getName());
    }

    /**
     * Tests the getter for the room capacity.
     */
    @Test
    void testGetCapacity() {
        assertEquals(capacity, room.getCapacity());
    }

    /**
     * Tests the setter for the room id.
     */
    @Test
    void testSetRoomId() {
        int roomId2 = 120;
        room.setRoomId(roomId2);
        assertEquals(roomId2, room.getRoomId());
    }

    /**
     * Tests the setter for the room name.
     */
    @Test
    void testSetName() {
        String name = "IZ_3";
        room.setName(name);
        assertEquals(name, room.getName());
    }

    /**
     * Tests the setter for the room capacity.
     */
    @Test
    void testSetCapacity() {
        int capacity2 = 560;
        room.setCapacity(capacity2);
        assertEquals(capacity2, room.getCapacity());
    }
}