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
import nl.tudelft.sem.courses.entity.AddCourse;
import nl.tudelft.sem.courses.entity.AddLecture;
import nl.tudelft.sem.courses.entity.Course;
import nl.tudelft.sem.courses.entity.Enrollment;
import nl.tudelft.sem.courses.entity.Lecture;
import nl.tudelft.sem.courses.entity.Message;
import nl.tudelft.sem.courses.repository.CourseRepository;
import nl.tudelft.sem.courses.repository.EnrollmentRepository;
import nl.tudelft.sem.courses.repository.LectureRepository;
import nl.tudelft.sem.courses.util.JwtValidate;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = LectureController.class)
@AutoConfigureMockMvc
@WebMvcTest
// This class doesn't ever need to be serialized, so neither do it's members
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class LectureControllerTest {

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

    private LectureController lectureController;

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
    void setUp() throws IOException, InterruptedException, JSONException {
        String courseId = "CSE1200";
        String courseName = "OOPP";
        String teacherId = "Andy";

        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.client = mock(HttpClient.class);
        //this.jwt = mock(JwtValidate.class);
        //this.courseManagementController = mock(CourseManagementController.class);

        this.enrollment = new Enrollment();
        enrollment.setCourseId(courseId);
        enrollment.setStudentId("Henry");
        enrollments = new ArrayList<>();
        enrollments.add(enrollment);
        participants = new ArrayList<>();
        participants.add(enrollment.getStudentId());

        this.addcourse = new AddCourse(courseId, courseName, teacherId, participants);
        this.course = new Course();
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

        lectureController =
                new LectureController(courseRepository, lectureRepository);

        when(courseRepository.findByCourseId(course.getCourseId())).thenReturn(course);
        when(courseRepository.findByCourseName(course.getCourseName())).thenReturn(course);
        when(courseRepository.findAllByTeacherId(course.getTeacherId())).thenReturn(List.of(course));
        when(lectureRepository.findByCourseId(course.getCourseId())).thenReturn(lectures);
        when(lectureRepository.findByCourseIdAndScheduledDate(course.getCourseId(), Date.valueOf(localDate.plusDays(1)))).thenReturn(List.of(lecture));
        when(request.getHeader("Authorization")).thenReturn("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsdWthIiwic2NvcGUiOlt7ImF1dGhvcml0eSI6IlJPTEVfVEVBQ0hFUiJ9XSwiZXhwIjoxNjA4MDQ2MTM2LCJpYXQiOjE2MDgwNDI1MzZ9.1Pn1WnHIsa6YHIGqmnCPogfmvPqwXIjpwuhotzk6SnU"); //????

        JSONObject obj = new JSONObject();
        obj.put("role", "teacher");
        obj.put("netid", "luka");

        //when(jwt.jwtValidate(request)).thenReturn(obj);
        //when(courseManagementController.validate(request)).thenReturn(obj);
    }

    //TODO: cleanup of setup code above, I dont think everything is necessary there.
    //TODO: getAllLectures
    //TODO: getLecturesAfterDate
    //TODO: bareFromLecture

    @Test
    public void constructorNotNull() {
        assertNotNull(lectureController);
    }

    @Test
    void planNewLectureSuccess() throws JSONException {
        assertEquals(ResponseEntity.ok(new Message("Lecture planned.")),
                lectureController.planNewLecture(addlecture));
    }

    @Test
    void planNewLectureFail() throws JSONException {
        addlecture.setCourseId("randomCourseIdFail");
        assertEquals(ResponseEntity.ok(new Message(
                "The course with id " + addlecture.getCourseId()
                + " does not exist.")), lectureController.planNewLecture(addlecture));
    }

    @Test
    void cancelLectureSuccess() throws JSONException {
        assertEquals(ResponseEntity.ok(new Message("Lecture(s) cancelled.")),
                lectureController.cancelLecture(lecture.getCourseId(), localDate));
    }

    @Test
    void cancelLectureFail() throws JSONException {
        assertEquals(ResponseEntity.notFound().build(),
                lectureController.cancelLecture("randomCourseIdFail", localDate));
        localDate = LocalDate.of(1985, 1, 8);
        assertEquals(ResponseEntity.notFound().build(),
                lectureController.cancelLecture(lecture.getCourseId(), localDate));
    }
}
