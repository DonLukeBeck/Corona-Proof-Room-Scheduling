package nl.tudelft.sem.calendar.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OnCampusCandidateTest {
    private String netId;
    private LocalDate deadline;
    private OnCampusCandidate candidate;

    @BeforeEach
    void setUp() {
        netId = "abobe";
        deadline = LocalDate.now();
        candidate = new OnCampusCandidate(netId, deadline);
    }

    @Test
    void testEquals() {
        OnCampusCandidate candidate2 = new OnCampusCandidate(netId, deadline);
        assertEquals(candidate2, candidate);
    }

    @Test
    void testNotAnEqualNetId() {
        OnCampusCandidate candidate2 = new OnCampusCandidate("mbjdegoede", deadline);
        assertNotEquals(candidate2, candidate);
    }

    @Test
    void testNotAnEqualDeadline() {
        OnCampusCandidate candidate2 = new OnCampusCandidate(netId, LocalDate.now().plusDays(1));
        assertNotEquals(candidate2, candidate);
    }

    @Test
    void testGetSetNetId() {
        candidate.setNetId("mbjdegoede");
        assertEquals("mbjdegoede", candidate.getNetId());
    }

    @Test
    void testGetSetDeadline() {
        candidate.setDeadline(LocalDate.now().plusDays(2));
        assertEquals(LocalDate.now().plusDays(2), candidate.getDeadline());
    }
}