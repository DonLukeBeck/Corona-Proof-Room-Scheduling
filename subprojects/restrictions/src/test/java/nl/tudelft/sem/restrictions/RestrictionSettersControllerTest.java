package nl.tudelft.sem.restrictions;

import static nl.tudelft.sem.shared.Constants.noAccessMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import nl.tudelft.sem.restrictions.communication.Validate;
import nl.tudelft.sem.restrictions.controllers.RestrictionSettersController;
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
    void setUp() {
        ae = ResponseEntity.ok(new StringMessage("Already Exists"));
        fb = ResponseEntity.status(HttpStatus.FORBIDDEN).body(noAccessMessage);
        this.rest1 = new Restriction();
        this.rest1.setValue(1.0f);
        this.rest1.setName("test");

        this.rest2 = new Restriction("test2", 2.0f);

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
        Restriction rest3 = new Restriction("test3", 2000.0f);
        Restriction rest4 = new Restriction("test4", 4000.0f);
        when(restrictionRepository.findByName("startTime"))
                .thenReturn(java.util.Optional.of(rest3));
        when(restrictionRepository.findByName("endTime"))
                .thenReturn(java.util.Optional.of(rest4));
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
    void setMinSeatsBig() {
        assertEquals(ae, restrictionController.setMinSeatsBig(request, 2.0f));
        assertEquals(fb, restrictionController.setMinSeatsBig(wrongRequest, 2.0f));
    }

    @Test
    void setTimeGapLength() {
        assertEquals(ae, restrictionController.setTimeGapLength(request, 1.0f));
        assertEquals(fb, restrictionController.setTimeGapLength(wrongRequest, 1.0f));
    }

    @Test
    void setStartTime() {
        assertEquals(ResponseEntity.ok(new StringMessage("Saved")),
                restrictionController.setStartTime(request, 1000));
        assertEquals(fb,
                restrictionController.setStartTime(wrongRequest, 1000));
    }

    @Test
    void setEndTime() {
        assertEquals(ResponseEntity.ok(new StringMessage("Saved")),
                restrictionController.setEndTime(request, 3000));
        assertEquals(fb,
                restrictionController.setEndTime(wrongRequest, 3000));
    }
}