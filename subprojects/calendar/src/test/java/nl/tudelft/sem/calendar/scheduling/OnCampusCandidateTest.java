package nl.tudelft.sem.calendar.scheduling;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OnCampusCandidateTest {
    private static String netId;
    private static LocalDate deadline;
    private static OnCampusCandidate candidate;

    /**
     * Creates a test OnCampusCandidate and attributes used for verification used in all the
     * tests cases.
     */
    @BeforeEach
    void setUp() {
        netId = "abobe";
        deadline = LocalDate.now();
        candidate = new OnCampusCandidate(netId, deadline);
    }

    /**
     * Tests the equals method in case a different type of object is
     * given.
     */
    @Test
    void testNotEqualsDifferentType() {
        Object object = new Object();
        assertNotEquals(object, candidate);
        assertNotEquals(object.hashCode(), candidate.hashCode());
    }

    /**
     * Tests the equals method in case the same candidate is
     * given.
     */
    @Test
    void testSameObject() {
        assertEquals(candidate, candidate);
        assertEquals(candidate.hashCode(), candidate.hashCode());
    }


    /**
     * Tests the equals method in case a campus candidate with the same netId and deadline is
     * given.
     */
    @Test
    void testEquals() {
        OnCampusCandidate candidate2 = new OnCampusCandidate(netId, deadline);
        assertEquals(candidate2, candidate);
        assertEquals(candidate.hashCode(), candidate2.hashCode());
    }

    /**
     * Tests the equals method in case a candidate with a different netId is given.
     */
    @Test
    void testNotAnEqualNetId() {
        OnCampusCandidate candidate2 = new OnCampusCandidate("mbjdegoede", deadline);
        assertNotEquals(candidate2, candidate);
        assertNotEquals(candidate2.hashCode(), candidate.hashCode());
    }

    /**
     * Tests the equals method in case a candidate with a different deadline is given.
     */
    @Test
    void testNotAnEqualDeadline() {
        OnCampusCandidate candidate2 = new OnCampusCandidate(netId, LocalDate.now().plusDays(1));
        assertNotEquals(candidate2, candidate);
        assertNotEquals(candidate2.hashCode(), candidate.hashCode());
    }

    /**
     * Tests the getter and setter of the netId attribute.
     */
    @Test
    void testGetSetNetId() {
        candidate.setNetId("mbjdegoede");
        assertEquals("mbjdegoede", candidate.getNetId());
    }

    /**
     * Tests the getter and setter of the deadline attribute.
     */
    @Test
    void testGetSetDeadline() {
        candidate.setDeadline(LocalDate.now().plusDays(2));
        assertEquals(LocalDate.now().plusDays(2), candidate.getDeadline());
    }
}