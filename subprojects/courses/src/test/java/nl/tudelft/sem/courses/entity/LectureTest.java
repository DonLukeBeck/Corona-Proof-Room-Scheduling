package nl.tudelft.sem.courses.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LectureTest {

    private static Lecture lecture;
    private static Date date;

    /**
     * The initial setup before each test.
     */
    @BeforeEach
    void createCourse() {
        date = new Date(1220227200L * 1000);
        lecture = new Lecture();
        lecture.setCourseId("CSE1230");
        lecture.setDuration(50);
        lecture.setLectureId(9);
        lecture.setScheduledDate(date);
    }

    @Test
    public void constructorNotNull() {
        assertNotNull(lecture);
    }

    @Test
    void getCourseIdTest() {
        assertEquals("CSE1230", lecture.getCourseId());
    }

    @Test
    void setCourseIdTest() {
        lecture.setCourseId("CSE3012");
        assertEquals("CSE3012", lecture.getCourseId());
    }

    @Test
    void getDurationTest() {
        assertEquals(50, lecture.getDuration());
    }

    @Test
    void setDurationTest() {
        lecture.setDuration(30);
        assertEquals(30, lecture.getDuration());
    }

    @Test
    void getLectureIdTest() {
        assertEquals(9, lecture.getLectureId());
    }

    @Test
    void setLectureIdTest() {
        lecture.setLectureId(4);
        assertEquals(4, lecture.getLectureId());
    }

    @Test
    void getDateTest() {
        assertEquals(date, lecture.getScheduledDate());
    }

    @Test
    void setDateTest() {
        date = new Date(1220227799L * 1000);
        lecture.setScheduledDate(date);
        assertEquals(date, lecture.getScheduledDate());
    }
}
