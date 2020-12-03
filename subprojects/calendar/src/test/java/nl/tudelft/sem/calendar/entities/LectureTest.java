package nl.tudelft.sem.calendar.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class LectureTest {
    private Integer lectureId;
    private String courseId;
    private Integer roomId;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    List<Attendance> attendances;
    private int durationInMinutes;
    private Course course;
    private Lecture lecture;

    String studentId;
    Boolean physical;
    Attendance attendance;
    String courseName;
    String teacherId;
    List<Enrollment> participants;
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
        attendances = new ArrayList<Attendance>();
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
        lecture = new Lecture(lectureId, courseId, roomId, startTime, endTime, date, attendances, durationInMinutes, course);

    }
    @Test
    void testGetLectureId() {
        assertEquals(1, lecture.getLectureId());
    }

    @Test
    void testGetCourseId() {
        assertEquals("CSE1305", lecture.getCourseId());
    }

    @Test
    void testGetRoomId() {
        assertEquals(10, lecture.getRoomId());
    }

    @Test
    void testGetStartTime() {
        assertEquals(LocalTime.NOON, lecture.getStartTime());
    }

    @Test
    void testGetEndTime() {
        assertEquals(LocalTime.MIDNIGHT, lecture.getEndTime());
    }

    @Test
    void testGetDate() {
        assertEquals(LocalDate.now(),lecture.getDate());
    }

    @Test
    void testGetAttendances() {
        assertEquals(attendances, lecture.getAttendances());
    }

    @Test
    void testGetDurationInMinutes() {
        assertEquals(90, lecture.getDurationInMinutes());
    }

    @Test
    void testGetCourse() {
        assertEquals(course, lecture.getCourse());
    }

    @Test
    void testSetLectureId() {
        lecture.setLectureId(5);
        assertEquals(5, lecture.getLectureId());
    }

    @Test
    void testSetCourseId() {
        lecture.setCourseId("CSE2305");
        assertEquals("CSE2305",lecture.getCourseId());
    }

    @Test
    void testSetRoomId() {
        lecture.setRoomId(20);
        assertEquals(20, lecture.getRoomId());
    }

    @Test
    void testSetStartTime() {
        lecture.setStartTime(LocalTime.of(9,35));
        assertEquals(LocalTime.of(9,35), lecture.getStartTime());
    }

    @Test
    void testSetEndTime() {
        lecture.setEndTime(LocalTime.of(10,50));
        assertEquals(LocalTime.of(10,50), lecture.getEndTime());
    }

    @Test
    void testSetDate() {
        lecture.setDate(LocalDate.of(2020,10,28));
        assertEquals(LocalDate.of(2020,10,28), lecture.getDate());
    }

    @Test
    void testSetAttendances() {
        Attendance attendance1 = Attendance.builder()
                .lectureId(15)
                .physical(true)
                .studentId("abobe").build();
        List<Attendance> att = new ArrayList<Attendance>();
        att.add(attendance1);
        lecture.setAttendances(att);
        assertEquals(att, lecture.getAttendances());
    }

    @Test
    void testSetDurationInMinutes() {
        lecture.setDurationInMinutes(100);
        assertEquals(100, lecture.getDurationInMinutes());
    }

    @Test
    void setCourse() {
    }
}