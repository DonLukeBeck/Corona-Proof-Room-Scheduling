package nl.tudelft.sem.calendar.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



class CourseTest {
    private static Course course;
    private static List<String> participants;

    /**
     * Creates a course and a list of associated participants used in all the test cases.
     */
    @BeforeEach
    void setUp() {
        participants = Arrays.asList("abobe", "mbjdegoede");
        course = new Course(participants);
    }

    /**
     * Tests the getter of the participant list.
     */
    @Test
    void testGetParticipants() {
        assertEquals(participants, course.getParticipants());
    }

    /**
     * Tests the setter of the participant list.
     */
    @Test
    void testSetParticipants() {
        List<String> participants2 = Arrays.asList("someNetId", "someNetId2");
        course.setParticipants(participants2);
        assertEquals(participants2, course.getParticipants());
    }
}