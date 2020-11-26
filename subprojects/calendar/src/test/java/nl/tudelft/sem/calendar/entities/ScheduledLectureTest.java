package nl.tudelft.sem.calendar.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScheduledLectureTest {
    private Course course;
    private Date date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Room room;
    private List<String> studentsOnCampus;
    private ScheduledLecture scheduledLecture;

    @BeforeEach
    void setUp() {
        endTime = LocalTime.NOON;
        startTime = LocalTime.MIDNIGHT;
        course = new Course(Arrays.asList("Matthijs", "Alex"));
        date = new Date(System.currentTimeMillis());
        room = new Room(10, 200, "Drebbelweg IZ-2");

        scheduledLecture = new ScheduledLecture(course,date);
        scheduledLecture.setStartTime(startTime);
        scheduledLecture.setEndTime(endTime);
        scheduledLecture.setRoom(room);
    }

    @Test
    void testGetCourse() {
        assertEquals(course, scheduledLecture.getCourse());
    }

    @Test
    void testGetDate() {
        assertEquals(date, scheduledLecture.getDate());
    }

    @Test
    void testGetStartTime() {
        assertEquals(startTime, scheduledLecture.getStartTime());
    }

    @Test
    void testGetEndTime() {
        assertEquals(endTime, scheduledLecture.getEndTime());
    }

    @Test
    void testGetRoom() {
        assertEquals(room, scheduledLecture.getRoom());
    }

    @Test
    void testGetStudentsOnCampus() {

    }

    @Test
    void testSetRoom() {
        scheduledLecture.setRoom(room);
        assertEquals(room, scheduledLecture.getRoom());
    }

    @Test
    void testSetStartTime() {
        scheduledLecture.setStartTime(LocalTime.MIDNIGHT);
        assertEquals(LocalTime.MIDNIGHT, scheduledLecture.getStartTime());
    }

    @Test
    void testSetEndTime() {
        scheduledLecture.setEndTime(LocalTime.NOON);
        assertEquals(LocalTime.NOON, scheduledLecture.getEndTime());
    }

    @Test
    void testAddStudentsOnCampus() {

    }
}