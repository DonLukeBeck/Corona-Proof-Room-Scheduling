package nl.tudelft.sem.restrictions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;



@ContextConfiguration(classes = Restriction.class)
@AutoConfigureMockMvc
@WebMvcTest
// This class doesn't ever need to be serialized, so neither do it's members
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
class RestrictionControllerTest {

    private Restriction rest1;
    private Restriction rest2;
    private String ae;

    private RestrictionController restrictionController;

    @MockBean
    RestrictionRepository restrictionRepository;

    /**
     * The initial setup before each test.
     */
    @BeforeEach
    void setUp() {
        ae = "Already Exists";
        this.rest1 = new Restriction();
        this.rest1.setValue(1.0f);
        this.rest1.setName("test");

        this.rest2 = new Restriction("test2", 2.0f);
        
        restrictionController = new RestrictionController(restrictionRepository);
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
    }

    @Test
    public void constructorNotNull() {
        assertNotNull(restrictionController);
    }

    @Test
    public void addNewRestrictionSuccess() {
        assertEquals("Saved", restrictionController.addNewRestriction("name", 3.0f));
    }

    @Test
    public void addNewRestrictionSuccess1() {
        assertEquals("Updated", restrictionController.addNewRestriction("test", 4.0f));
    }

    @Test
    public void addNewRestrictionSuccess2() {
        assertEquals(ae, restrictionController.addNewRestriction("test2", 2.0f));
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
        assertThrows(NoSuchElementException.class, () -> {
            restrictionController.getRestrictionVal("aaa");
        });
    }

    @Test
    void setCapacityRestriction() {
        assertEquals(ae, restrictionController.setCapacityRestriction(true, 1.0f));
    }

    @Test
    void setCapacityRestriction2() {
        assertEquals(ae, restrictionController.setCapacityRestriction(false, 2.0f));
    }

    @Test
    void setMinSeatsBig() {
        assertEquals(ae, restrictionController.setMinSeatsBig(2.0f));
    }

    @Test
    void setTimeGapLength() {
        assertEquals(ae, restrictionController.setTimeGapLength(1.0f));
    }

    @Test
    void getCapacityRestriction() {
        assertEquals(1.0f, restrictionController.getCapacityRestriction(true));
    }

    @Test
    void getCapacityRestriction2() {
        assertEquals(2.0f, restrictionController.getCapacityRestriction(false));
    }

    @Test
    void getMinSeatsBig() {
        assertEquals(2.0f, restrictionController.getMinSeatsBig());
    }

    @Test
    void getTimeGapLength() {
        assertEquals(1.0f, restrictionController.getTimeGapLength());
    }

    @Test
    void setStartTime() {
    }

    @Test
    void setEndTime() {
    }

    @Test
    void getStartTime() {
    }

    @Test
    void getEndTime() {
    }
}