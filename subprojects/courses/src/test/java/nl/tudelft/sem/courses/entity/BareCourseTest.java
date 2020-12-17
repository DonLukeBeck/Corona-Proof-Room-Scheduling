package nl.tudelft.sem.courses.entity;

import nl.tudelft.sem.courses.entity.BareCourse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BareCourseTest {

    private static BareCourse course;

    /**
     * The initial setup before each test.
     */
    @BeforeEach
    void createCourse() {
        course = new BareCourse("CSE1230", "SEM", "TE22");
    }

    @Test
    public void constructorNotNull() {
        assertNotNull(course);
    }

    @Test
    void getCourseIdTest() {
        assertEquals("CSE1230", course.getCourseId());
    }

    @Test
    void setCourseIdTest() {
        course.setCourseId("CSE3012");
        assertEquals("CSE3012", course.getCourseId());
    }

    @Test
    void getCourseNameTest() {
        assertEquals("SEM", course.getCourseName());
    }

    @Test
    void setCourseNameTest() {
        course.setCourseName("AD");
        assertEquals("AD", course.getCourseName());
    }

    @Test
    void getTeacherIdTest() {
        assertEquals("TE22", course.getTeacherId());
    }

    @Test
    void setTeacherIdTest() {
        course.setTeacherId("Teacher");
        assertEquals("Teacher", course.getTeacherId());
    }

}
