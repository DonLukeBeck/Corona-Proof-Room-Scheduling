package nl.tudelft.sem.calendar.entities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {
    private Course course;
    private List<String> participants;

    @BeforeEach
    void setUp() {
        List<String> participants = Arrays.asList("Matthijs", "Alex");
        course = new Course(participants);
    }

    @Test
    void testGetParticipants() {
        assertEquals(participants, course.getParticipants());
    }
}