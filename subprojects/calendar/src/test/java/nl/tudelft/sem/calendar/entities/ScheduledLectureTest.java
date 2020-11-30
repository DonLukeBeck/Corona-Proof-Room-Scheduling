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

    /**
     * Creates a new scheduled lecture and attributes used for verification in all test cases.
     */
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

    /**
     * Tests the getter of the course attribute.
     */
    @Test
    void testGetCourse() {
        assertEquals(course, scheduledLecture.getCourse());
    }

    /**
     * Tests the getter of the date attribute.
     */
    @Test
    void testGetLocalDate() {
        assertEquals(date, scheduledLecture.getDate());
    }

    /**
     * Tests the getter of the start time attribute.
     */
    @Test
    void testGetStartTime() {
        assertEquals(startTime, scheduledLecture.getStartTime());
    }

    /**
     * Tests the getter of the end time attribute.
     */
    @Test
    void testGetEndTime() {
        assertEquals(endTime, scheduledLecture.getEndTime());
    }

    /**
     * Tests the getter of the room attribute.
     */
    @Test
    void testGetRoom() {
        assertEquals(room, scheduledLecture.getRoom());
    }

    /**
     * Tests the getter of the list of students on campus attribute.
     */
    @Test
    void testGetStudentsOnCampus() {
        assertTrue(scheduledLecture.getStudentsOnCampus().isEmpty());
    }

    /**
     * Tests the setter of the room attribute.
     */
    @Test
    void testSetRoom() {
        scheduledLecture.setRoom(room);
        assertEquals(room, scheduledLecture.getRoom());
    }

    /**
     * Tests the setter of the start time attribute.
     */
    @Test
    void testSetStartTime() {
        scheduledLecture.setStartTime(LocalTime.MIDNIGHT);
        assertEquals(LocalTime.MIDNIGHT, scheduledLecture.getStartTime());
    }

    /**
     * Tests the setter of the end time attribute.
     */
    @Test
    void testSetEndTime() {
        scheduledLecture.setEndTime(LocalTime.NOON);
        assertEquals(LocalTime.NOON, scheduledLecture.getEndTime());
    }

    /**
     * Tests whether adding a list of netIds to the selected list of students works as expected.
     */
    @Test
    void testAddStudentsOnCampus() {
        assertTrue(scheduledLecture.getStudentsOnCampus().isEmpty());
        List<String> students2 = Arrays.asList("mbjdegoede", "abobe");
        scheduledLecture.addStudentsOnCampus(students2);
        assertEquals(students2, scheduledLecture.getStudentsOnCampus());
    }

    /**
     * Tests whether adding a single netId to the selected list of students works as expected.
     */
    @Test
    void testAddStudentOnCampus() {
        assertTrue(scheduledLecture.getStudentsOnCampus().isEmpty());
        String student2 = "abobe";
        scheduledLecture.addStudentOnCampus(student2);
        assertTrue(scheduledLecture.getStudentsOnCampus().contains(student2));
    }

    /**
     * Tests the setter of the course attribute.
     */
    @Test
    void testSetCourse() {
        Course course2 = new Course(Arrays.asList("abobe"));
        scheduledLecture.setCourse(course2);
        assertEquals(course2, scheduledLecture.getCourse());
    }

    /**
     * Tests the setter of the date attribute.
     */
    @Test
    void testSetDate() {
        LocalDate date2 = LocalDate.now();
        scheduledLecture.setDate(date2);
        assertEquals(date2, scheduledLecture.getDate());
    }

    /**
     * Tests the setter of the list of selected students on campus attribute.
     */
    @Test
    void testSetStudentsOnCampus() {
        assertTrue(scheduledLecture.getStudentsOnCampus().isEmpty());
        List<String> studentsOnCampus = Arrays.asList("someNetId, someNetId2");
        scheduledLecture.setStudentsOnCampus(studentsOnCampus);
        assertEquals(studentsOnCampus, scheduledLecture.getStudentsOnCampus());
    }
}