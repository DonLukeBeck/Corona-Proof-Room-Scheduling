package nl.tudelft.sem.calendar.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



class CourseTest {
    private Course course;
    private List<String> participants;

    @BeforeEach
    void setUp() {
        participants = Arrays.asList("abobe", "mbjdegoede");
        course = new Course(participants);
    }

    @Test
    void testGetParticipants() {
        assertEquals(participants, course.getParticipants());
    }

    @Test
    void testSetParticipants() {
        List<String> participants2 = Arrays.asList("someNetId", "someNetId2");
        course.setParticipants(participants2);
        assertEquals(participants2, course.getParticipants());
    }
}