package nl.tudelft.sem.calendar.entities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OnCampusCandidateTest {
    private String netId;
    private int numParticipants;
    private OnCampusCandidate candidate;

    @BeforeEach
    void setUp() {
        netId = "abobe";
        numParticipants = 24;
        candidate = new OnCampusCandidate(netId, numParticipants);
    }
    @Test
    void testGetNetId() {
        assertEquals(netId,candidate.getNetId());
    }

    @Test
    void testConstructorParticipations() {
        assertEquals(numParticipants, candidate.getNumParticipations());
    }
    
    @Test 
    void testGetSetParticipations() {
        candidate.setNumParticipations(10);
        assertEquals(10, candidate.getNumParticipations());
    }
}