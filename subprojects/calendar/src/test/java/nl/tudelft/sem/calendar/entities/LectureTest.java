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
    void getDate() {
    }

    @Test
    void getAttendances() {
    }

    @Test
    void getDurationInMinutes() {
    }

    @Test
    void getCourse() {
    }

    @Test
    void setLectureId() {
    }

    @Test
    void setCourseId() {
    }

    @Test
    void setRoomId() {
    }

    @Test
    void setStartTime() {
    }

    @Test
    void setEndTime() {
    }

    @Test
    void setDate() {
    }

    @Test
    void setAttendances() {
    }

    @Test
    void setDurationInMinutes() {
    }

    @Test
    void setCourse() {
    }
}