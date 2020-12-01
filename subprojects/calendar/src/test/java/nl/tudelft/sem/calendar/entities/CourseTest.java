package nl.tudelft.sem.calendar.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



class CourseTest {
    private static Course course;
    private static List<Enrollment> participants;

    /**
     * Creates a course and a list of associated participants used in all the test cases.
     */
    @BeforeEach
    void setUp() {
        participants = Arrays.asList(
                Enrollment.builder().studentId("abobe").courseId("CSE2100").build(),
                Enrollment.builder().studentId("mbjdegoede").courseId("CSE2100").build());

        course = Course.builder().participantsList(participants).build();
    }

    /**
     * Tests the getter of the participant list.
     */
    @Test
    void testGetParticipantsList() {
        assertEquals(participants, course.getParticipantsList());
    }

    /**
     * Tests the setter of the participant list.
     */
    @Test
    void testSetParticipantsList() {
        List<Enrollment> participants2 = Arrays.asList(
                Enrollment.builder().studentId("someNetId").courseId("CSE2100").build(),
                Enrollment.builder().studentId("someNetId2").courseId("CSE2100").build());

        course.setParticipantsList(participants2);
        assertEquals(participants2, course.getParticipantsList());
    }

    @Test
    void getNetIds() {
    }

    @Test
    void getCourseId() {
    }

    @Test
    void getCourseName() {
    }

    @Test
    void getTeacherId() {
    }

    @Test
    void setCourseId() {
    }

    @Test
    void setCourseName() {
    }

    @Test
    void setTeacherId() {

    }
}