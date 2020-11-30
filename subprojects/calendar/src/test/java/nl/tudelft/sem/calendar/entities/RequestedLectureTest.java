package nl.tudelft.sem.calendar.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RequestedLectureTest {
    private Course course;
    private RequestedLecture lecture;
    private LocalDate date;
    private int durationInMinutes;

    /**
     * Creates a new requested lecture and attributes used for verification, used in all test
     * cases.
     */
    @BeforeEach
    void setUp() {
        date = LocalDate.now();
        course = new Course(Arrays.asList("mbjdegoede", "abobe"));
        durationInMinutes = 90;
        lecture = new RequestedLecture(course, date, durationInMinutes);
    }

    /**
     * Tests the getter of the course attribute.
     */
    @Test
    void testGetCourse() {
        assertEquals(course, lecture.getCourse());
    }

    /**
     * Tests the getter of the date attribute.
     */
    @Test
    void testGetDate() {
        assertEquals(date, lecture.getDate());
    }

    /**
     * Tests the getter of the duration attribute.
     */
    @Test
    void testGetDurationInMinutes() {
        assertEquals(durationInMinutes, lecture.getDurationInMinutes());
    }

    /**
     * Tests the setter of the course attribute.
     */
    @Test
    void testSetCourse() {
        Course course2 = new Course(Arrays.asList("mbjdegoede", "abobe"));
        lecture.setCourse(course2);
        assertEquals(course2, lecture.getCourse());
    }

    /**
     * Tests the setter of the date attribute.
     */
    @Test
    void testSetDate() {
        LocalDate date2 = LocalDate.now();
        lecture.setDate(date2);
        assertEquals(date2, lecture.getDate());
    }

    /**
     * Tests the setter of the duration in minutes attribute.
     */
    @Test
    void testSetDurationInMinutes() {
        int duration2 = 100;
        lecture.setDurationInMinutes(duration2);
        assertEquals(duration2, lecture.getDurationInMinutes());
    }
}