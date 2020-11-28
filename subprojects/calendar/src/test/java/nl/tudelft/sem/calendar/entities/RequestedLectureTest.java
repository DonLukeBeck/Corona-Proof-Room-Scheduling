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
       course = new Course(Arrays.asList("mbjdegoede", "abobe"));
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

    @Test
    void testSetCourse() {
        Course course2 = new Course(Arrays.asList("mbjdegoede", "abobe"));
        lecture.setCourse(course2);
        assertEquals(course2, lecture.getCourse());
    }

    @Test
    void testSetDate() {
        Date date2 = new Date(System.currentTimeMillis()+1);
        lecture.setDate(date2);
        assertEquals(date2, lecture.getDate());
    }

    @Test
    void testSetDurationInMinutes() {
        int duration2 = 100;
        lecture.setDurationInMinutes(duration2);
        assertEquals(duration2, lecture.getDurationInMinutes());
    }
}