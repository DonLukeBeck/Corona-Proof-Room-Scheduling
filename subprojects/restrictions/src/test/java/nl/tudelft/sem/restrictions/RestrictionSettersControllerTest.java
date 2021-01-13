package nl.tudelft.sem.restrictions;

import nl.tudelft.sem.restrictions.communication.Validate;
import nl.tudelft.sem.shared.Constants;
import nl.tudelft.sem.shared.entity.StringMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static nl.tudelft.sem.shared.Constants.noAccessMessage;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = Restriction.class)
@AutoConfigureMockMvc
@WebMvcTest(RestrictionSettersController.class)
// This class doesn't ever need to be serialized, so neither do it's members
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
class RestrictionSettersControllerTest {

    private Restriction rest1;
    private Restriction rest2;
    private ResponseEntity<?> ae;
    private ResponseEntity<?> fb;
    private HttpServletRequest request;
    private HttpServletRequest wrongRequest;

    @MockBean
    RestrictionRepository restrictionRepository;

    @MockBean
    Validate validate;

    @InjectMocks
    private RestrictionSettersController restrictionController;

    /**
     * The initial setup before each test.
     */
    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        ae = ResponseEntity.ok(new StringMessage("Already Exists"));
        fb = ResponseEntity.status(HttpStatus.FORBIDDEN).body(Constants.noAccessMessage);
        this.rest1 = new Restriction();
        this.rest1.setValue(1.0f);
        this.rest1.setName("test");

        this.rest2 = new Restriction("test2", 2.0f);
        Restriction rest3 = new Restriction("test3", 2000.0f);
        Restriction rest4 = new Restriction("test4", 4000.0f);

        request = Mockito.mock(HttpServletRequest.class);
        wrongRequest = Mockito.mock(HttpServletRequest.class);

        restrictionController = new RestrictionSettersController(restrictionRepository,
                validate);
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
        when(validate.validateRole(request, "teacher"))
                .thenReturn("netid");
        when(validate.validateRole(wrongRequest, "teacher"))
                .thenReturn(noAccessMessage.getMessage());
    }

    @Test
    public void constructorNotNull() {
        assertNotNull(restrictionController);
    }

    @Test
    public void addNewRestrictionSuccess() {
        assertEquals(new StringMessage("Saved"), restrictionController
                .addNewRestriction("name", 3.0f));
    }

    @Test
    public void addNewRestrictionSuccess1() {
        assertEquals(new StringMessage("Updated"), restrictionController
                .addNewRestriction("test", 4.0f));
    }

    @Test
    public void addNewRestrictionSuccess2() {
        assertEquals(new StringMessage("Already Exists"), restrictionController
                .addNewRestriction("test2", 2.0f));
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
    void setCapacityRestriction() throws IOException, InterruptedException {
        RestrictionSettersController.SetCapacityRestrictionContext context =
            new RestrictionSettersController.SetCapacityRestrictionContext(true, 1.0f);
        assertEquals(ae, restrictionController.setCapacityRestriction(request, context));
        assertEquals(fb, restrictionController.setCapacityRestriction(wrongRequest, context));
    }

    @Test
    void setCapacityRestriction2() throws IOException, InterruptedException {
        RestrictionSettersController.SetCapacityRestrictionContext context =
            new RestrictionSettersController.SetCapacityRestrictionContext(false, 2.0f);
        assertEquals(ae, restrictionController.setCapacityRestriction(request, context));
        assertEquals(fb, restrictionController.setCapacityRestriction(wrongRequest, context));
    }

    @Test
    void setMinSeatsBig() throws IOException, InterruptedException {
        assertEquals(ae, restrictionController.setMinSeatsBig(request, 2.0f));
        assertEquals(fb, restrictionController.setMinSeatsBig(wrongRequest, 2.0f));
    }

    @Test
    void setTimeGapLength() throws IOException, InterruptedException {
        assertEquals(ae, restrictionController.setTimeGapLength(request, 1.0f));
        assertEquals(fb, restrictionController.setTimeGapLength(wrongRequest, 1.0f));
    }

    @Test
    void setStartTime() throws IOException, InterruptedException {
        assertEquals(ResponseEntity.ok(new StringMessage("Updated")),
                restrictionController.setStartTime(request, 1000));
        assertEquals(fb,
                restrictionController.setStartTime(wrongRequest, 1000));
    }

    @Test
    void setEndTime() throws IOException, InterruptedException {
        assertEquals(ResponseEntity.ok(new StringMessage("Updated")),
                restrictionController.setEndTime(request, 3000));
        assertEquals(fb,
                restrictionController.setEndTime(wrongRequest, 3000));
    }
}