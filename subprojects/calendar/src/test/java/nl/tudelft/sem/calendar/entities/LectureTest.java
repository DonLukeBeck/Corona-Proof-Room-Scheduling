package nl.tudelft.sem.calendar.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LectureTest {
    private transient Integer lectureId;
    private transient String courseId;
    private transient Integer roomId;
    private transient LocalTime startTime;
    private transient LocalTime endTime;
    private transient LocalDate date;
    private transient List<Attendance> attendances;
    private transient int durationInMinutes;
    private transient Course course;
    private transient Lecture lecture;

    private transient String studentId;
    private transient Boolean physical;
    private transient Attendance attendance;
    private transient String courseName;
    private transient String teacherId;
    private transient List<Enrollment> participants;

    /**
     * Creates a lecture and attributes that are used for verification.
     */
    @BeforeEach
    void setUp() {
        lectureId = 1;
        courseId = "CSE1305";
        roomId = 10;
        startTime = LocalTime.NOON;
        endTime = LocalTime.MIDNIGHT;
        date = LocalDate.now();
        studentId = "mbjdegoede";
        physical = false;
        attendance = Attendance.builder()
                .lectureId(lectureId)
                .physical(physical)
                .studentId(studentId).build();
        attendances = new ArrayList<>();
        attendances.add(attendance);
        durationInMinutes = 90;
        courseId = "CSE1305";
        courseName = "ADS";
        teacherId = "rkrebbers";

        participants = Arrays.asList(
                Enrollment.builder().studentId("abobe").courseId("CSE2100").build(), 
                Enrollment.builder().studentId("mbjdegoede").courseId("CSE2100").build());

        course = Course.builder()
                .participantsList(participants)
                .courseId(courseId)
                .courseName(courseName)
                .teacherId(teacherId).build();
        lecture = new Lecture(lectureId, courseId, roomId, startTime, 
                endTime, date, durationInMinutes, course, false);

    }

    /**
     * Tests the getter of the lecture id.
     */
    @Test
    void testGetLectureId() {
        assertEquals(1, lecture.getLectureId());
    }

    /**
     * Tests the getter of the course id.
     */
    @Test
    void testGetCourseId() {
        assertEquals("CSE1305", lecture.getCourseId());
    }

    /**
     * Tests the getter of the room id.
     */
    @Test
    void testGetRoomId() {
        assertEquals(10, lecture.getRoomId());
    }

    /**
     * Tests the getter of the scheduling start time.
     */
    @Test
    void testGetStartTime() {
        assertEquals(LocalTime.NOON, lecture.getStartTime());
    }

    /**
     * Tests the getter of the scheduling ending time.
     */
    @Test
    void testGetEndTime() {
        assertEquals(LocalTime.MIDNIGHT, lecture.getEndTime());
    }

    /**
     * Tests the getter of the scheduling date.
     */
    @Test
    void testGetDate() {
        assertEquals(LocalDate.now(), lecture.getDate());
    }

    /**
     * Tests the getter of the declared duration in minutes of the to be scheduled lecture.
     */
    @Test
    void testGetDurationInMinutes() {
        assertEquals(90, lecture.getDurationInMinutes());
    }

    /**
     * Tests the getter of the associated course.
     */
    @Test
    void testGetCourse() {
        assertEquals(course, lecture.getCourse());
    }

    /**
     * Tests the setter of the lecture id.
     */
    @Test
    void testSetLectureId() {
        lecture.setLectureId(5);
        assertEquals(5, lecture.getLectureId());
    }

    /**
     * Tests the setter of the course id.
     */
    @Test
    void testSetCourseId() {
        lecture.setCourseId("CSE2305");
        assertEquals("CSE2305", lecture.getCourseId());
    }

    /**
     * Tests the setter of the room id.
     */
    @Test
    void testSetRoomId() {
        lecture.setRoomId(20);
        assertEquals(20, lecture.getRoomId());
    }

    /**
     * Tests the setter of the scheduling start time.
     */
    @Test
    void testSetStartTime() {
        lecture.setStartTime(LocalTime.of(9, 35));
        assertEquals(LocalTime.of(9, 35), lecture.getStartTime());
    }

    /**
     * Tests the setter of the scheduling ending time.
     */
    @Test
    void testSetEndTime() {
        lecture.setEndTime(LocalTime.of(10, 50));
        assertEquals(LocalTime.of(10, 50), lecture.getEndTime());
    }

    /**
     * Tests the setter of the scheduling date.
     */
    @Test
    void testSetDate() {
        lecture.setDate(LocalDate.of(2020, 10, 28));
        assertEquals(LocalDate.of(2020, 10, 28), lecture.getDate());
    }

    /**
     * Tests the setter of the declared duration in minutes of the requested lecture.
     */
    @Test
    void testSetDurationInMinutes() {
        lecture.setDurationInMinutes(100);
        assertEquals(100, lecture.getDurationInMinutes());
    }

    /**
     * Tests the setter of the associated course.
     */
    @Test
    void testSetCourse() {
        Course course1 = Course.builder()
                .participantsList(participants)
                .courseId(courseId)
                .courseName("CSE1400")
                .teacherId(teacherId).build();
        lecture.setCourse(course1);
        assertEquals(course1, lecture.getCourse());
    }

    /**
     * Tests the getter and setter of the attribute indicating whether a student
     *      is selected to attend the lecture on-campus or not,
     *      used when retrieving the lecture for the schedule.
     */
    @Test
    void testGetSetSelectedForOnCampus() {
        assertFalse(lecture.isSelectedForOnCampus());
        lecture.setSelectedForOnCampus(true);
        assertTrue(lecture.isSelectedForOnCampus());
    }
}