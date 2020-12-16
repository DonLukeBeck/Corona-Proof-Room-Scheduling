package nl.tudelft.sem.rooms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoomTest {

    private transient Room room;
    private transient Room room1;

    /**
     * The initial setup before each test.
     */
    @BeforeEach
    void setUp() {
        room = new Room();
        room.setName("DW-1");
        room.setCapacity(6);
        room.setRoomId(1);
        room1 = new Room(2, "DW-3", 10);
    }

    @Test
    public void constructorNotNull() {
        assertNotNull(room);
        assertNotNull(room1);
    }


    @Test
    void getRoomId() {
        assertEquals(1, room.getRoomId());
    }

    @Test
    void setRoomId() {
        room.setRoomId(2);
        assertEquals(2, room.getRoomId());
    }

    @Test
    void getName() {
        assertEquals("DW-1", room.getName());
    }

    @Test
    void setName() {
        room.setName("EWI-1");
        assertEquals("EWI-1", room.getName());
    }

    @Test
    void getCapacity() {
        assertEquals(6, room.getCapacity());
    }

    @Test
    void setCapacity() {
        room.setCapacity(10);
        assertEquals(10, room.getCapacity());
    }

    @Test
    void canEqualTest() {
        assertTrue(room.canEqual(room1));
    }

    @Test
    void toStringTest() {
        assertEquals("Room(roomId=1, name=DW-1, capacity=6)", room.toString());
    }

    @Test
    void equalsTest() {
        assertNotEquals(room, room1);
    }
}