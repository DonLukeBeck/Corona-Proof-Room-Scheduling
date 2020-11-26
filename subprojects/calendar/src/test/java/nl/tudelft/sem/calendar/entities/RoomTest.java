package nl.tudelft.sem.calendar.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
}