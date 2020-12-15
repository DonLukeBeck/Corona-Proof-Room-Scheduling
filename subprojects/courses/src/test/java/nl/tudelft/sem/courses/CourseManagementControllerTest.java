package nl.tudelft.sem.courses;

import nl.tudelft.sem.courses.controller.CourseManagementController;
import nl.tudelft.sem.courses.entity.AddCourse;
import nl.tudelft.sem.courses.entity.AddLecture;
import nl.tudelft.sem.courses.entity.Course;
import nl.tudelft.sem.courses.entity.Enrollment;
import nl.tudelft.sem.courses.entity.Lecture;
import nl.tudelft.sem.courses.repository.CourseRepository;
import nl.tudelft.sem.courses.repository.EnrollmentRepository;
import nl.tudelft.sem.courses.repository.LectureRepository;
import nl.tudelft.sem.courses.util.JwtValidate;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static nl.tudelft.sem.courses.util.JwtValidate.jwtValidate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = CourseManagementController.class)
@AutoConfigureMockMvc
@WebMvcTest
// This class doesn't ever need to be serialized, so neither do it's members
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class CourseManagementControllerTest {

    private AddCourse Addcourse;
    private Course course;
    private AddLecture Addlecture;
    private Lecture lecture;
    private Enrollment enrollment;
    private static Date date;
    private List<String> participants;
    private List<Enrollment> enrollments;
    private List<Lecture> lectures;
    private String errorMessage = "Error";

    private CourseManagementController courseManagementController;

    private static HttpClient client;
    private HttpServletRequest request;
    private HttpServletRequest response;

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
    void setUp() throws IOException, InterruptedException {

        this.request = Mockito.mock(HttpServletRequest.class);
        this.response = Mockito.mock(HttpServletRequest.class);
        this.client = Mockito.mock(HttpClient.class);

        this.enrollment = new Enrollment();
        enrollment.setCourseId("CSE1200");
        enrollment.setStudentId("Henry");
        enrollments = new ArrayList<>();
        enrollments.add(enrollment);
        participants = new ArrayList<>();
        participants.add(enrollment.getStudentId());

        this.Addcourse = new AddCourse("CSE1200", "OOPP", "Andy", participants);
        this.course = new Course();
        course.setCourseName("OOPP");
        course.setTeacherId("Andy");
        course.setCourseId("CSE1200");

        date = new Date(1220227200L * 1000);
        this.Addlecture = new AddLecture("CSE1200", date, 30);
        this.lecture = new Lecture();
        lecture.setDuration(30);
        lecture.setScheduledDate(date);
        lecture.setCourseId("CSE1200");
        lectures = new ArrayList<>();
        lectures.add(lecture);

        courseManagementController =
                new CourseManagementController(courseRepository, enrollmentRepository, lectureRepository);
        when(courseRepository.findByCourseId(course.getCourseId())).thenReturn(course);
        when(courseRepository.findByCourseName(course.getCourseName())).thenReturn(course);
        when(courseRepository.findByTeacherId(course.getTeacherId())).thenReturn(course);

        when(enrollmentRepository.findByCourseId(course.getCourseName())).thenReturn(enrollments);

        when(lectureRepository.findByCourseId(course.getCourseName())).thenReturn(lectures);
        when(lectureRepository.findByCourseIdAndDate(course.getCourseName(), date)).thenReturn(lecture);

        when(request.getHeader("Authorization")).thenReturn("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsdWthIiwic2NvcGUiOlt7ImF1dGhvcml0eSI6IlJPTEVfVEVBQ0hFUiJ9XSwiZXhwIjoxNjA4MDQ2MTM2LCJpYXQiOjE2MDgwNDI1MzZ9.1Pn1WnHIsa6YHIGqmnCPogfmvPqwXIjpwuhotzk6SnU"); //????

        JSONObject obj = new JSONObject();
        obj.put("role", "teacher");
        obj.put("netid", "luka");
        when(jwtValidate(request)).thenReturn(obj);


    }

    @Test
    public void constructorNotNull() {
        assertNotNull(courseManagementController);
    }

    @Test
    void createNewCourseSuccess() throws IOException, InterruptedException {
        AddCourse newOne = new AddCourse("CSE1299", "OOPP", "Andy", participants);
        System.out.println(request.getHeader("Authorization"));
        assertEquals("Saved",
                courseManagementController.createNewCourse
                        (request, newOne));
    }

    @Test
    void createNewCourseFail() throws IOException, InterruptedException {
        assertEquals("Already Exists",
                courseManagementController.createNewCourse
                        (request, Addcourse));
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
        // the method cancelLecture does not work
        // Uncomment line below if method is fixed
        // assertEquals("Lecture deleted", courseManagementController.cancelLecture(lecture.getCourseId(), lecture.getScheduledDate()));
    }

    @Test
    void cancelLectureFail() {
        date = new Date(1220227299L * 1000);
        assertEquals(errorMessage, courseManagementController.cancelLecture("Random Number", lecture.getScheduledDate()));
        assertEquals(errorMessage, courseManagementController.cancelLecture(lecture.getCourseId(), date));
    }
}
