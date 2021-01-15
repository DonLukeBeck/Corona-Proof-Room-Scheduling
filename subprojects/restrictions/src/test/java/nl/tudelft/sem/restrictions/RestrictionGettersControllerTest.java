package nl.tudelft.sem.restrictions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import javax.servlet.http.HttpServletRequest;
import nl.tudelft.sem.restrictions.communication.RoomsCommunicator;
import nl.tudelft.sem.restrictions.communication.ServerErrorException;
import nl.tudelft.sem.restrictions.communication.Validate;
import nl.tudelft.sem.restrictions.controllers.RestrictionGettersController;
import nl.tudelft.sem.shared.entity.IntValue;
import nl.tudelft.sem.shared.entity.StringMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = Restriction.class)
@AutoConfigureMockMvc
@WebMvcTest(RestrictionGettersController.class)
// This class doesn't ever need to be serialized, so neither do it's members
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
class RestrictionGettersControllerTest {

    private Restriction rest1;
    private Restriction rest2;
    private Restriction rest3;
    private Restriction rest4;
    private Room room1;
    private Room room2;
    private Room room3;
    private Room room4;
    private List<Room> allRooms;
    private ResponseEntity<?> ae;
    private ResponseEntity<?> fb;
    private HttpServletRequest request;
    private HttpServletRequest wrongRequest;

    @MockBean
    RoomsCommunicator roomsCommunicator;

    @MockBean
    RestrictionRepository restrictionRepository;

    @MockBean
    Validate validate;

    @InjectMocks
    private RestrictionGettersController restrictionController;

    /**
     * The initial setup before each test.
     */
    @BeforeEach
    void setUp() {
        ae = ResponseEntity.ok(new StringMessage("Already Exists"));
        this.rest1 = new Restriction();
        this.rest1.setValue(1.0f);
        this.rest1.setName("test");

        this.room1 = new Room(1, "DW-1", 100);
        this.room2 = new Room(2, "DW-2", 200);
        this.room3 = new Room(3, "DW-3", 300);
        this.room4 = new Room(4, "DW-4", 400);
        allRooms = Arrays.asList(room1, room2, room3, room4);

        this.rest2 = new Restriction("test2", 2.0f);
        this.rest3 = new Restriction("test3", 2000.0f);
        this.rest4 = new Restriction("test4", 4000.0f);

        restrictionController = new RestrictionGettersController(restrictionRepository,
                roomsCommunicator);
        when(restrictionRepository.findByName(rest1.getName()))
                .thenReturn(java.util.Optional.ofNullable(rest1));
        when(restrictionRepository.findByName(rest2.getName()))
                .thenReturn(java.util.Optional.ofNullable(rest2));
        when(restrictionRepository.findByName("bigRoomMaxPercentage"))
                .thenReturn(java.util.Optional.ofNullable(rest1));
        when(restrictionRepository.findByName("smallRoomMaxPercentage"))
                .thenReturn(java.util.Optional.ofNullable(rest2));
        when(restrictionRepository.findByName("minSeatsBig"))
                .thenReturn(java.util.Optional.ofNullable(rest2));
        when(restrictionRepository.findByName("gapTimeInMinutes"))
                .thenReturn(java.util.Optional.ofNullable(rest1));
        when(restrictionRepository.findByName("startTime"))
                .thenReturn(java.util.Optional.ofNullable(rest3));
        when(restrictionRepository.findByName("endTime"))
                .thenReturn(java.util.Optional.ofNullable(rest4));
    }

    @Test
    public void constructorNotNull() {
        assertNotNull(restrictionController);
    }

    @Test
    public void equalsTest() {
        assertEquals(rest2, rest2);
    }

    @Test
    public void notEqualsTest() {
        assertNotEquals(restrictionController, rest1);
    }

    @Test
    public void notEquals2() {
        assertNotEquals(rest2, rest1);
    }

    @Test
    public void toStringTest() {
        assertEquals(rest1.toString(), "Restriction(name=test, value=1.0)");
    }

    @Test
    void getRestrictionVal1() {
        assertEquals(restrictionController.getRestrictionVal("test"), 1.0f);
    }

    @Test
    void getRestrictionVal2() {
        assertThrows(NoSuchElementException.class,
                () -> restrictionController.getRestrictionVal("aaa"));
    }

    @Test
    void getTimeGapLength() {
        assertEquals(ResponseEntity.ok(new IntValue(1)), restrictionController.getTimeGapLength());
    }

    @Test
    void getStartTime() {
        assertEquals(ResponseEntity.ok(new IntValue(2000)), restrictionController.getStartTime());
    }

    @Test
    void getEndTime() {
        assertEquals(ResponseEntity.ok(new IntValue(4000)), restrictionController.getEndTime());
    }

    @Test
    void getRoomsAdjusted() throws InterruptedException, ServerErrorException, IOException {
        when(roomsCommunicator.getAllRooms()).thenReturn(allRooms);
        when(restrictionRepository.findByName("minSeatsBig"))
                .thenReturn(java.util.Optional.of(new Restriction("", 200.0f)));
        when(restrictionRepository.findByName("bigRoomMaxPercentage"))
                .thenReturn(java.util.Optional.of(new Restriction("", 30.0f)));
        when(restrictionRepository.findByName("smallRoomMaxPercentage"))
                .thenReturn(java.util.Optional.of(new Restriction("", 20.0f)));
        Room room6 = new Room(1, "DW-1", 20);
        Room room7 = new Room(2, "DW-2", 60);
        Room room8 = new Room(3, "DW-3", 90);
        Room room9 = new Room(4, "DW-4", 120);
        List<Room> it = Arrays.asList(room6, room7, room8, room9);

        assertEquals(ResponseEntity.ok(it), restrictionController
                .getAllRoomsWithAdjustedCapacity());
    }
}