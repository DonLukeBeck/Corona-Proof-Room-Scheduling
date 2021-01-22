package nl.tudelft.sem.courses.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem.courses.entity.Course;
import nl.tudelft.sem.courses.entity.Enrollment;
import nl.tudelft.sem.courses.repository.CourseRepository;
import nl.tudelft.sem.courses.repository.EnrollmentRepository;
import nl.tudelft.sem.shared.entity.BareEnrollment;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = EnrollmentController.class)
@AutoConfigureMockMvc
@WebMvcTest
// This class doesn't ever need to be serialized, so neither do it's members
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class EnrollmentControllerTest {

    private Course course;
    private Course course2;
    private EnrollmentController enrollmentController;
    private List<BareEnrollment> bareEnrollments;

    @MockBean
    CourseRepository courseRepository;
    @MockBean
    EnrollmentRepository enrollmentRepository;

    /**
     * The initial setup before each test.
     */
    @BeforeEach
    void setUp() throws JSONException {

        String courseId = "CSE1200";
        String courseId2 = "anothercourse";

        Enrollment enrollment = new Enrollment();
        enrollment.setCourseId(courseId);
        enrollment.setStudentId("Henry");
        Enrollment enrollment2 = new Enrollment();
        enrollment.setCourseId(courseId2);
        enrollment.setStudentId("OtherStudent");
        List<Enrollment> enrollments = new ArrayList<>();
        enrollments.add(enrollment);
        enrollments.add(enrollment2);
        enrollmentRepository.save(enrollment);
        enrollmentRepository.save(enrollment2);
        bareEnrollments = new ArrayList<>();
        bareEnrollments.add(new BareEnrollment(enrollment.getCourseId(), enrollment.getStudentId()));
        bareEnrollments.add(new BareEnrollment(enrollment2.getCourseId(), enrollment2.getStudentId()));


        String courseName = "OOPP";
        String teacherId = "Andy";

        String courseName2 = "NOTOOPP";
        String teacherId2 = "NotAndy";

        this.course = new Course(courseId, courseName, teacherId);
        courseRepository.save(course);

        this.course2 = new Course(courseId2, courseName2, teacherId2);
        courseRepository.save(course2);

        enrollmentController =
                new EnrollmentController(enrollmentRepository);
        when(enrollmentRepository.findAll()).thenReturn(enrollments);
    }

    @Test
    public void constructorNotNull() {
        assertNotNull(enrollmentController);
    }

    @Test
    void getAllEnrollmentsTest() {
        assertEquals(bareEnrollments,
                enrollmentController.getAllEnrollments().getBody());
    }

    @Test
    void getEnrollmentsByCourseTest() {
        assertEquals(enrollmentRepository.findByCourseId(course.getCourseId()),
                enrollmentController.getEnrollmentsByCourse(course.getCourseId()).getBody());
    }

    @Test
    void GetEnrollmentsByCourseTest2() {
        assertEquals(null, enrollmentController.getEnrollmentsByCourse(null).getBody());
    }

    @Test
    void GetEnrollmentsByCourseTest3() {
        assertEquals(enrollmentRepository.findByCourseId("randomCourseId"), enrollmentController.getEnrollmentsByCourse("randomCourseId").getBody());
    }

    @Test
    void getEnrollmentsByCourseTest4() {
        assertNotEquals(enrollmentRepository.findByCourseId(course.getCourseId()),
                enrollmentController.getEnrollmentsByCourse(course2.getCourseId()).getBody());
    }

}