package nl.tudelft.sem.rooms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import nl.tudelft.sem.shared.entity.StringMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = Room.class)
@AutoConfigureMockMvc
@WebMvcTest
// This class doesn't ever need to be serialized, so neither do it's members
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
class RoomControllerTest {

    private Room room1;
    private Room room2;

    private RoomController roomController;
    private List<Room> allRooms;

    @MockBean
    RoomRepository roomRepository;

    /**
     * The initial setup before each test.
     */
    @BeforeEach
    void setUp() {
        this.room1 = new Room(1, "DW-1", 10);
        this.room2 = new Room(2, "DW-2", 20);
        allRooms = Arrays.asList(room1, room2);

        roomController = new RoomController(roomRepository);
        when(roomRepository.findAll())
                .thenReturn(allRooms);
        when(roomRepository.findByName(room1.getName()))
                .thenReturn(java.util.Optional.ofNullable(room1));
        when(roomRepository.findById(room2.getRoomId()))
                .thenReturn(java.util.Optional.ofNullable(room2));

    }

    @Test
    public void constructorNotNull() {
        assertNotNull(roomController);
    }

    @Test
    void addNewRoom() {
        assertEquals(ResponseEntity.ok(new StringMessage("Already Exists")),
                roomController.addNewRoom(room1.getName(), room1.getCapacity()));
    }

    @Test
    void addNewRoom1() {
        assertEquals(ResponseEntity.ok(new StringMessage("Invalid capacity.")),
                roomController.addNewRoom("hey", -4));
    }

    @Test
    void addNewRoom2() {
        assertEquals(ResponseEntity.ok(new StringMessage("Room added.")),
                roomController.addNewRoom("hey", 4));
    }

    @Test
    void deleteRoom() {
        assertEquals(ResponseEntity.ok(new StringMessage("Room could not be found.")),
                roomController.deleteRoom("Test"));
    }

    @Test
    void deleteRoom1() {
        assertEquals(ResponseEntity.ok(new StringMessage("Deleted")),
                roomController.deleteRoom("DW-1"));
    }

    @Test
    void getAllRooms() {
        assertEquals(ResponseEntity.ok(allRooms),
                roomController.getAllRooms());
    }

    @Test
    void getRoomName() {
        assertEquals(ResponseEntity.ok(new StringMessage("DW-2")),
                roomController.getRoomName(room2.getRoomId()));
    }

    @Test
    void getRoomName1() {
        assertEquals(ResponseEntity.notFound().build(),
                roomController.getRoomName(7));
    }
}
