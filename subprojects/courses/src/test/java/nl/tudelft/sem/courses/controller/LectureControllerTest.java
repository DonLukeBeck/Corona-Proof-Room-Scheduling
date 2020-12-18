package nl.tudelft.sem.courses.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem.shared.entity.AddCourse;
import nl.tudelft.sem.shared.entity.AddLecture;
import nl.tudelft.sem.shared.entity.BareLecture;
import nl.tudelft.sem.courses.entity.Course;
import nl.tudelft.sem.courses.entity.Enrollment;
import nl.tudelft.sem.courses.entity.Lecture;
import nl.tudelft.sem.courses.entity.Message;
import nl.tudelft.sem.courses.repository.CourseRepository;
import nl.tudelft.sem.courses.repository.EnrollmentRepository;
import nl.tudelft.sem.courses.repository.LectureRepository;
import org.json.JSONException;
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
    private BareLecture bareLecture;
    private Course course;
    private AddLecture addlecture;
    private Lecture lecture;
    private Enrollment enrollment;
    private LocalDate localDate;
    private Date sqlDate;
    private List<String> participants;
    private List<Enrollment> enrollments;
    private List<Lecture> lectures;
    private List<BareLecture> bareLectures;
    private LocalDate dt;
    private Date sqldt;

    private LectureController lectureController;

    /**
    private static HttpClient client;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private JwtValidate jwt;
    */

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

        /**
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.client = mock(HttpClient.class);
        this.jwt = mock(JwtValidate.class);
        this.courseManagementController = mock(CourseManagementController.class);
         */

        this.enrollment = new Enrollment();
        enrollment.setCourseId(courseId);
        enrollment.setStudentId("Henry");
        enrollments = new ArrayList<>();
        enrollments.add(enrollment);
        participants = new ArrayList<>();
        participants.add(enrollment.getStudentId());
        enrollmentRepository.save(enrollment);

        this.addcourse = new AddCourse(courseId, courseName, teacherId, participants);
        this.course = new Course();
        course.setCourseName(courseName);
        course.setTeacherId(teacherId);
        course.setCourseId(courseId);
        courseRepository.save(course);

        localDate = LocalDate.of(2020, 1, 8);
        this.sqlDate = Date.valueOf(localDate);
        this.addlecture = new AddLecture(courseId, sqlDate, 30);
        this.bareLecture = new BareLecture();
        this.lecture = new Lecture();
        lecture.setDuration(30);
        lecture.setScheduledDate(sqlDate);
        lecture.setCourseId(courseId);
        bareLecture.setDurationInMinutes(30);
        bareLecture.setDate(localDate);
        bareLecture.setCourseId(courseId);
        lectures = new ArrayList<>();
        bareLectures = new ArrayList<>();
        lectures.add(lecture);
        bareLectures.add(bareLecture);
        lectureRepository.save(lecture);
        dt = localDate.minusDays(5);
        sqldt = Date.valueOf(dt);

        lectureController =
                new LectureController(courseRepository, lectureRepository);

        when(courseRepository.findByCourseId(course.getCourseId())).thenReturn(course);
        when(courseRepository.findByCourseName(course.getCourseName())).thenReturn(course);
        when(courseRepository.findAllByTeacherId(course.getTeacherId())).thenReturn(List.of(course));
        when(lectureRepository.findByCourseId(course.getCourseId())).thenReturn(lectures);
        when(lectureRepository.findByCourseIdAndScheduledDate(course.getCourseId(), Date.valueOf(localDate.plusDays(1)))).thenReturn(List.of(lecture));
        when(lectureRepository.findByScheduledDateAfter(sqldt)).thenReturn(lectures);

        /**
        when(request.getHeader("Authorization")).thenReturn("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsdWthIiwic2NvcGUiOlt7ImF1dGhvcml0eSI6IlJPTEVfVEVBQ0hFUiJ9XSwiZXhwIjoxNjA4MDQ2MTM2LCJpYXQiOjE2MDgwNDI1MzZ9.1Pn1WnHIsa6YHIGqmnCPogfmvPqwXIjpwuhotzk6SnU"); //????

        JSONObject obj = new JSONObject();
        obj.put("role", "teacher");
        obj.put("netid", "luka");

        when(jwt.jwtValidate(request)).thenReturn(obj);
        when(courseManagementController.validate(request)).thenReturn(obj);
        */
    }

    @Test
    public void constructorNotNull() {
        assertNotNull(lectureController);
    }

    @Test
    void getAllLectures() {
        assertEquals(lectureRepository.findAll(), lectureController.getAllLectures().getBody());
    }

    @Test
    void getLecturesAfterDate() {
        List<BareLecture> list = (List<BareLecture>) lectureController.getLecturesAfterDate(dt).getBody();
        for (BareLecture l : list) {
            System.out.println(l);
            assert(bareLectures.contains(l));
        }
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
