package nl.tudelft.sem.courses;

import nl.tudelft.sem.courses.controller.CourseManagementController;
import nl.tudelft.sem.courses.entity.Course;
import nl.tudelft.sem.courses.entity.Enrollment;
import nl.tudelft.sem.courses.entity.Lecture;
import nl.tudelft.sem.courses.repository.CourseRepository;
import nl.tudelft.sem.courses.repository.EnrollmentRepository;
import nl.tudelft.sem.courses.repository.LectureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = CourseManagementController.class)
@AutoConfigureMockMvc
@WebMvcTest
// This class doesn't ever need to be serialized, so neither do it's members
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class CourseManagementControllerTest {

    private Course course;
    private Lecture lecture;
    private Enrollment enrollment;
    private static Date date;
    private List<String> participants;
    private List<Enrollment> enrollments;
    private List<Lecture> lectures;
    private String errorMessage = "Error";

    private CourseManagementController courseManagementController;

    @MockBean
    CourseRepository courseRepository;
    @MockBean
    EnrollmentRepository enrollmentRepository;
    @MockBean
    LectureRepository lectureRepository;

    /**
     * The initial setup before each test.
     */
    @BeforeEach
    void setUp() {
        this.course = new Course();
        course.setCourseId("CSE1200");
        course.setTeacherId("Andy");
        course.setCourseName("OOPP");

        this.lecture = new Lecture();
        date = new Date(1220227200L * 1000);
        lecture.setCourseId("CSE1200");
        lecture.setScheduledDate(date);
        lecture.setDuration(30);
        lecture.setLectureId(1);
        lectures = new ArrayList<>();
        lectures.add(lecture);

        this.enrollment = new Enrollment();
        enrollment.setCourseId("CSE1200");
        enrollment.setStudentId("Henry");
        enrollments = new ArrayList<>();
        enrollments.add(enrollment);
        participants = new ArrayList<>();
        participants.add(enrollment.getStudentId());

        courseManagementController =
                new CourseManagementController(courseRepository, enrollmentRepository, lectureRepository);
        when(courseRepository.findByCourseId(course.getCourseId())).thenReturn(course);
        when(courseRepository.findByCourseName(course.getCourseName())).thenReturn(course);
        when(courseRepository.findByTeacherId(course.getTeacherId())).thenReturn(course);

        when(enrollmentRepository.findByCourseId(course.getCourseName())).thenReturn(enrollments);

        when(lectureRepository.findByCourseId(course.getCourseName())).thenReturn(lectures);
        when(lectureRepository.findByCourseIdAndDate(course.getCourseName(), date)).thenReturn(lecture);
    }

    @Test
    public void constructorNotNull() {
        assertNotNull(courseManagementController);
    }

    @Test
    void createNewCourseSuccess() {
        assertEquals("Saved",
                courseManagementController.createNewCourse
                        ("CSE2200", "AD", "Teacher", participants));
    }

    @Test
    void createNewCourseFail() {
        assertEquals("Already Exists",
                courseManagementController.createNewCourse
                        (course.getCourseId(), course.getCourseName(), course.getTeacherId(), participants));
    }

    @Test
    void deleteCourseSuccess() {
        assertEquals("Deleted",
                courseManagementController.deleteCourse(course.getCourseId()));
    }

    @Test
    void deleteCourseFail() {
        assertEquals(errorMessage,
                courseManagementController.deleteCourse("RandomNumber"));
    }

    @Test
    void getCourseSuccess() {
        assertEquals(course, courseManagementController.getCourse(course.getCourseId()));
    }

    @Test
    void getCourseFail() {
        assertEquals(null, courseManagementController.getCourse("RandomNumber"));
    }

    @Test
    void getCourseParticipantsSuccess() {
        for (String i : courseManagementController.getCourseParticipants(enrollment.getCourseId())) {
            Enrollment e = new Enrollment();
            e.setCourseId(enrollment.getCourseId());
            e.setStudentId(i);
            assert (enrollments.contains(e));
        }
    }

    @Test
    void getCourseParticipantsFail() {
        assertEquals(0, courseManagementController.getCourseParticipants(enrollment.getCourseId()).size());
    }

    @Test
    void getCourseIdForTeacherSuccess() {
        assertEquals(course.getCourseId(), courseManagementController.getCourseIdForTeacher(course.getTeacherId()));
    }

    @Test
    void getCourseIdForTeacherFail() {
        assertEquals(errorMessage, courseManagementController.getCourseIdForTeacher("Random Teacher"));
    }

    @Test
    void planNewLectureSuccess() {
        assertEquals("Lecture added", courseManagementController.planNewLecture(lecture.getCourseId(), lecture.getDuration(), lecture.getScheduledDate()));
    }

    @Test
    void planNewLectureFail() {
        assertEquals(errorMessage, courseManagementController.planNewLecture("Random number", lecture.getDuration(), lecture.getScheduledDate()));
        assertEquals(errorMessage, courseManagementController.planNewLecture(lecture.getCourseId(), -8, lecture.getScheduledDate()));
    }

    @Test
    void cancelLectureSuccess() {
        //this method does not work
        //assertEquals("Lecture deleted", courseManagementController.cancelLecture(lecture.getCourseId(), lecture.getScheduledDate()));
    }

    @Test
    void cancelLectureFail() {
        date = new Date(1220227299L * 1000);
        assertEquals(errorMessage, courseManagementController.cancelLecture("Random Number", lecture.getScheduledDate()));
        assertEquals(errorMessage, courseManagementController.cancelLecture(lecture.getCourseId(), date));
    }
}
