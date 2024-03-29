package nl.tudelft.sem.courses.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import nl.tudelft.sem.shared.entity.BareLecture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class BareLectureTest {

    private static BareLecture lecture;
    private static LocalDate date;

    /**
     * The initial setup before each test.
     */
    @BeforeEach
    void createCourse() {
        date = LocalDate.of(2020, 1, 8);
        lecture = new BareLecture("CSE1230", date, 50);
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
        assertEquals(50, lecture.getDurationInMinutes());
    }

    @Test
    void setDurationTest() {
        lecture.setDurationInMinutes(30);
        assertEquals(30, lecture.getDurationInMinutes());
    }

    @Test
    void getDateTest() {
        assertEquals(date, lecture.getDate());
    }

    @Test
    void setDateTest() {
        date = LocalDate.of(2019, 1, 8);
        lecture.setDate(date);
        assertEquals(date, lecture.getDate());
    }
}
