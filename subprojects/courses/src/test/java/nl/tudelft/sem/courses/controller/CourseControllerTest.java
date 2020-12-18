package nl.tudelft.sem.courses.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.http.HttpClient;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.tudelft.sem.courses.entity.Message;
import nl.tudelft.sem.shared.entity.AddCourse;
import nl.tudelft.sem.shared.entity.AddLecture;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = CourseController.class)
@AutoConfigureMockMvc
@WebMvcTest
// This class doesn't ever need to be serialized, so neither do it's members
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class CourseControllerTest {

    private AddCourse addcourse;
    private Course course;
    private AddLecture addlecture;
    private Lecture lecture;
    private Enrollment enrollment;
    private LocalDate localDate;
    private Date sqlDate;
    private List<String> participants;
    private List<Enrollment> enrollments;
    private List<Lecture> lectures;
    private String errorMessage = "Error";
    private List<Course> courses;

    private CourseController courseController;

    private static HttpClient client;
    private HttpServletRequest request;
    private HttpServletResponse response;

    private JwtValidate jwt;

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
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.client = mock(HttpClient.class);
        //this.jwt = mock(JwtValidate.class);
        //this.courseManagementController = mock(CourseManagementController.class);

        String courseId = "CSE1200";

        this.enrollment = new Enrollment();
        enrollment.setCourseId(courseId);
        enrollment.setStudentId("Henry");
        enrollments = new ArrayList<>();
        enrollments.add(enrollment);
        participants = new ArrayList<>();
        participants.add(enrollment.getStudentId());

        String courseName = "OOPP";
        String teacherId = "Andy";

        this.addcourse = new AddCourse(courseId, courseName, teacherId, participants);
        this.course = new Course();
        courses = new ArrayList<>();
        courses.add(course);
        course.setCourseName(courseName);
        course.setTeacherId(teacherId);
        course.setCourseId(courseId);

        localDate = LocalDate.of(2020, 1, 8);
        this.sqlDate = Date.valueOf(localDate);
        this.addlecture = new AddLecture(courseId, sqlDate, 30);
        this.lecture = new Lecture();
        lecture.setDuration(30);
        lecture.setScheduledDate(sqlDate);
        lecture.setCourseId(courseId);
        lectures = new ArrayList<>();
        lectures.add(lecture);

        courseController =
                new CourseController(courseRepository,
                        enrollmentRepository);
        when(courseRepository.findByCourseId(course.getCourseId())).thenReturn(course);
        when(courseRepository.findByCourseName(course.getCourseName())).thenReturn(course);
        when(courseRepository.findAllByTeacherId(course.getTeacherId())).thenReturn(courses);

        when(enrollmentRepository.findByCourseId(course.getCourseId())).thenReturn(enrollments);

        when(lectureRepository.findByCourseId(course.getCourseId())).thenReturn(lectures);
        when(lectureRepository.findByCourseIdAndScheduledDate(course.getCourseId(),
                Date.valueOf(localDate.plusDays(1)))).thenReturn(lectures);

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer"
                        + " eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9."
                        + "eyJzdWIiOiJsdWthIiwic2NvcGUiOlt7ImF1dGhvcml0"
                        + "eSI6IlJPTEVfVEVBQ0hFUiJ9XSwiZXhwIjoxNjA4MDQ2MTM2LCJ"
                        + "pYXQiOjE2MDgwNDI1MzZ9.1Pn1WnHIsa6YHIGqmnCPogfmvPqwXI"
                        + "jpwuhotzk6SnU"); //????

        JSONObject obj = new JSONObject();
        obj.put("role", "teacher");
        obj.put("netid", "luka");

        //when(jwt.jwtValidate(request)).thenReturn(obj);
        //when(courseManagementController.validate(request)).thenReturn(obj);
    }

    @Test
    public void constructorNotNull() {
        assertNotNull(courseController);
    }

    //    @Test
    //    void createNewCourseSuccess() throws IOException, InterruptedException {
    //        AddCourse newOne = new AddCourse("CSE1299", "OOPP", "Andy", participants);
    //        System.out.println(request.getHeader("Authorization"));
    //        assertEquals("Saved",
    //                courseManagementController.createNewCourse
    //                        (request, newOne));
    //    }
    //
    //    @Test
    //    void createNewCourseFail() throws IOException, InterruptedException {
    //        assertEquals("Already Exists",
    //                courseManagementController.createNewCourse
    //                        (request, addcourse));
    //    }


    @Test
    void deleteCourseSuccess() {
        assertEquals(ResponseEntity.ok(new Message("Course deleted.")),
                courseController.deleteCourse(course.getCourseId()));
    }

    @Test
    void deleteCourseFail() {
        assertEquals(ResponseEntity.notFound().build(),
                courseController.deleteCourse("RandomNumber"));
    }


    @Test
    void getCourseSuccess() {
        assertEquals(course, courseController.getCourse(course.getCourseId()).getBody());
    }

    @Test
    void getCourseFail() {
        assertEquals(ResponseEntity.notFound().build(), courseController
                .getCourse("RandomNumber"));
    }

    @Test
    void getCourseParticipantsSuccess() {
        List<String> part = (List) courseController
                .getCourseParticipants(enrollment.getCourseId()).getBody();

        for (String i : part) {
            Enrollment e = new Enrollment();
            e.setCourseId(enrollment.getCourseId());
            e.setStudentId(i);
            assert (enrollments.contains(e));
        }
    }

    @Test
    void getCourseParticipantsFail() {
        enrollment.setCourseId("randomCourseIdFail");
        List<String> part = (List) courseController
                .getCourseParticipants(enrollment.getCourseId()).getBody();
        assertEquals(0, part.size());
    }

    @Test
    void getCourseIdForTeacherSuccess() {
        List<Course> t = (List) courseController
                .getCoursesForTeacher(course.getTeacherId()).getBody();
        for (Course c : courses) {
            assert(t.contains(c));
        }
    }

    @Test
    void getCourseIdForTeacherFail() {
        assertEquals(ResponseEntity.ok(new ArrayList()), courseController
                .getCoursesForTeacher("Random Teacher"));
    }
}
