package nl.tudelft.sem.calendar.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoomTest {
    private Room room;
    private int roomId;
    private int capacity;
    private String name;


    @BeforeEach
    void setUp() {
        roomId = 10;
        capacity = 200;
        name = "Drebbelweg IZ-2";
        room = new Room(roomId, capacity, name);
    }

    @Test
    void testGetRoomId() {
        assertEquals(roomId, room.getRoomId());
    }

    @Test
    void testGetName() {
        assertEquals(name, room.getName());
    }

    @Test
    void testGetCapacity() {
        assertEquals(capacity, room.getCapacity());
    }

    @Test
    void testSetRoomId() {
        int roomId2 = 120;
        room.setRoomId(roomId2);
        assertEquals(roomId2, room.getRoomId());
    }

    @Test
    void testSetName() {
        String name = "IZ_3";
        room.setName(name);
        assertEquals(name, room.getName());
    }

    @Test
    void testSetCapacity() {
        int capacity2 = 560;
        room.setCapacity(capacity2);
        assertEquals(capacity2, room.getCapacity());
    }
}