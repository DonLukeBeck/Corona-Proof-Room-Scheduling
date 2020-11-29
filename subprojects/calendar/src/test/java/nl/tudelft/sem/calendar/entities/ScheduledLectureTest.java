package nl.tudelft.sem.calendar.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScheduledLectureTest {
    private Course course;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Room room;
    private ScheduledLecture scheduledLecture;

    @BeforeEach
    void setUp() {
        endTime = LocalTime.NOON;
        startTime = LocalTime.MIDNIGHT;
        course = new Course(Arrays.asList("mbjdegoede", "abobe"));
        date = LocalDate.now();
        room = new Room(10, 200, "Drebbelweg IZ-2");

        scheduledLecture = new ScheduledLecture(course, date);
        scheduledLecture.setStartTime(startTime);
        scheduledLecture.setEndTime(endTime);
        scheduledLecture.setRoom(room);
    }

    @Test
    void testGetCourse() {
        assertEquals(course, scheduledLecture.getCourse());
    }

    @Test
    void testGetLocalDate() {
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
        assertTrue(scheduledLecture.getStudentsOnCampus().isEmpty());
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
        assertTrue(scheduledLecture.getStudentsOnCampus().isEmpty());
        List<String> students2 = Arrays.asList("mbjdegoede", "abobe");
        scheduledLecture.addStudentsOnCampus(students2);
        assertEquals(students2, scheduledLecture.getStudentsOnCampus());
    }

    @Test
    void testAddStudentOnCampus() {
        assertTrue(scheduledLecture.getStudentsOnCampus().isEmpty());
        String student2 = "abobe";
        scheduledLecture.addStudentOnCampus(student2);
        assertTrue(scheduledLecture.getStudentsOnCampus().contains(student2));
    }

    @Test
    void testSetCourse() {
        Course course2 = new Course(Arrays.asList("abobe"));
        scheduledLecture.setCourse(course2);
        assertEquals(course2, scheduledLecture.getCourse());
    }

    @Test
    void testSetDate() {
        LocalDate date2 = LocalDate.now();
        scheduledLecture.setDate(date2);
        assertEquals(date2, scheduledLecture.getDate());
    }

    @Test
    void testSetStudentsOnCampus() {
        assertTrue(scheduledLecture.getStudentsOnCampus().isEmpty());
        List<String> studentsOnCampus = Arrays.asList("someNetId, someNetId2");
        scheduledLecture.setStudentsOnCampus(studentsOnCampus);
        assertEquals(studentsOnCampus, scheduledLecture.getStudentsOnCampus());
    }
}