package nl.tudelft.sem.calendar.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CourseTest {
    private static Course course;
    private static String courseId;
    private static String courseName;
    private static String teacherId;
    private static List<String> participants;

    /**
     * Creates a course and its attributes used in all test cases.
     */
    @BeforeEach
    void setUp() {

        courseId = "CSE1305";
        courseName = "ADS";
        teacherId = "rkrebbers";

        participants = Arrays.asList("abobe", "mbjdegoede");

        course = Course.builder()
                .netIds(participants)
                .courseId(courseId)
                .courseName(courseName)
                .teacherId(teacherId).build();
    }

    /**
     * Tests the getter of the participant list.
     */
    @Test
    void testGetParticipantsList() {
        assertEquals(participants, course.getNetIds());
    }

    /**
     * Tests the setter of the participant list.
     */
    @Test
    void testSetParticipantsList() {
        List<String> participants2 = Arrays.asList("someNetId", "someNetId2");

        course.setNetIds(participants2);
        assertEquals(participants2, course.getNetIds());
    }

    /**
     * Tests the method that returns all the net ids associated with the course.
     * This method is used for testing purposes.
     */
    @Test
    void getNetIds() {
        assertEquals(participants, course.getNetIds());
    }

    /**
     * Tests the getter of the course id.
     */
    @Test
    void getCourseId() {
        assertEquals(courseId, course.getCourseId());
    }

    /**
     * Tests the getter of the course name.
     */
    @Test
    void getCourseName() {
        assertEquals(courseName, course.getCourseName());
    }

    /**
     * Tests the getter of the teacher id.
     */
    @Test
    void getTeacherId() {
        assertEquals(teacherId, course.getTeacherId());
    }

    /**
     * Tests the setter of the course id.
     */
    @Test
    void setCourseId() {
        course.setCourseId("CSE2115");
        assertEquals("CSE2115", course.getCourseId());
    }

    /**
     * Tests the setter of the course name.
     */
    @Test
    void setCourseName() {
        course.setCourseName("SEM");
        assertEquals("SEM", course.getCourseName());
    }

    /**
     * Tests the setter of the teacher id.
     */
    @Test
    void setTeacherId() {
        course.setTeacherId("apanichella");
        assertEquals("apanichella", course.getTeacherId());
    }
}