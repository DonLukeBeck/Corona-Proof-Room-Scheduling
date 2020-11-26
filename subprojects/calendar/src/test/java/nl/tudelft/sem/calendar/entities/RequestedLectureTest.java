package nl.tudelft.sem.calendar.entities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class RequestedLectureTest {
    private Course course;
    private RequestedLecture lecture;
    private Date date;
    private int durationInMinutes;

    @BeforeEach
    void setUp() {
       date = new Date(System.currentTimeMillis());
       course = new Course(Arrays.asList("Matthijs", "Alex"));
       durationInMinutes = 90;
       lecture = new RequestedLecture(course, date, durationInMinutes);
    }

    @Test
    void testGetCourse() {
        assertEquals(course, lecture.getCourse());
    }

    @Test
    void testGetDate() {
        assertEquals(date, lecture.getDate());
    }

    @Test
    void testGetDurationInMinutes() {
        assertEquals(durationInMinutes, lecture.getDurationInMinutes());
    }
}