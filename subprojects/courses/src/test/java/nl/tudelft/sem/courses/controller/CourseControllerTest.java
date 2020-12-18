package nl.tudelft.sem.courses.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
import nl.tudelft.sem.courses.util.Validate;
import nl.tudelft.sem.shared.entity.AddCourse;
import nl.tudelft.sem.shared.entity.AddLecture;
import nl.tudelft.sem.courses.entity.Course;
import nl.tudelft.sem.courses.entity.Enrollment;
import nl.tudelft.sem.courses.entity.Lecture;
import nl.tudelft.sem.courses.repository.CourseRepository;
import nl.tudelft.sem.courses.repository.EnrollmentRepository;
import nl.tudelft.sem.courses.repository.LectureRepository;
import nl.tudelft.sem.courses.util.JwtValidate;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
    private transient String noAccessMessage = "You are not allowed to view this page. Please contact administrator.";

    private AddCourse addcourse;
    private Course course;
    private Enrollment enrollment;
    private List<String> participants;
    private List<Enrollment> enrollments;
    private List<Course> courses;

    private CourseController courseController;

    private HttpServletRequest request;
    private HttpServletRequest wrongRequest;

    @MockBean
    CourseRepository courseRepository;
    @MockBean
    EnrollmentRepository enrollmentRepository;
    @MockBean
    LectureRepository lectureRepository;
    @MockBean
    Validate validate;

    /**
     * The initial setup before each test.
     */
    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        String courseName = "OOPP";
        String teacherId = "Andy";
        String courseId = "CSE1200";

        this.enrollment = new Enrollment();
        enrollment.setCourseId(courseId);
        enrollment.setStudentId("Henry");
        enrollments = new ArrayList<>();
        enrollments.add(enrollment);
        participants = new ArrayList<>();
        participants.add(enrollment.getStudentId());


        this.addcourse = new AddCourse(courseId, courseName, teacherId, participants);
        this.course = new Course();
        courses = new ArrayList<>();
        courses.add(course);
        course.setCourseName(courseName);
        course.setTeacherId(teacherId);
        course.setCourseId(courseId);

        LocalDate localDate = LocalDate.of(2020, 1, 8);
        Date sqlDate = Date.valueOf(localDate);
        Lecture lecture = new Lecture();
        lecture.setDuration(30);
        lecture.setScheduledDate(sqlDate);
        lecture.setCourseId(courseId);
        List<Lecture> lectures = new ArrayList<>();
        lectures.add(lecture);
        lectureRepository.save(lecture);

        request = Mockito.mock(HttpServletRequest.class);
        wrongRequest = Mockito.mock(HttpServletRequest.class);


        when(courseRepository.findByCourseId(course.getCourseId())).thenReturn(course);
        when(courseRepository.findByCourseName(course.getCourseName())).thenReturn(course);
        when(courseRepository.findAllByTeacherId(course.getTeacherId())).thenReturn(List.of(course));

        when(enrollmentRepository.findByCourseId(course.getCourseId())).thenReturn(enrollments);

        when(lectureRepository.findByCourseId(course.getCourseId())).thenReturn(lectures);
        when(lectureRepository.findByCourseIdAndScheduledDate(course.getCourseId(), Date.valueOf(localDate.plusDays(1)))).thenReturn(List.of(lecture));

        when(validate.validateRole(request, "teacher")).thenReturn("netid");
        when(validate.validateRole(wrongRequest, "teacher")).thenReturn(noAccessMessage);

        courseController = new CourseController(courseRepository, enrollmentRepository, validate);
    }

    @Test
    public void constructorNotNull() {
        assertNotNull(courseController);
    }

    @Test
    void createNewCourseSuccess() throws IOException, InterruptedException {
        AddCourse newOne = new AddCourse("CSE1299", "OOPP", "Andy", participants);
        assertEquals(ResponseEntity.ok(new Message("Course created.")), courseController.createNewCourse(request, newOne));
    }

    @Test
    void createNewCourseAccessDenied() throws IOException, InterruptedException {
        AddCourse newOne = new AddCourse("CSE1299", "OOPP", "Andy", participants);
        assertEquals(ResponseEntity.ok(new Message(noAccessMessage)), courseController.createNewCourse(wrongRequest, newOne));
    }

    @Test
    void createNewCourseAlreadyExists() throws IOException, InterruptedException {
        assertEquals(ResponseEntity.ok(new Message("Course already exists.")),courseController.createNewCourse(request, addcourse));
    }

    @Test
    void getAllCoursesSuccess() {
        assertEquals(courseRepository.findAll(), courseController.getAllCourses().getBody());
    }

    @Test
    void getCoursesForTeacherSuccess() {
        assertEquals(courseRepository.findAllByTeacherId(course.getTeacherId()),
                courseController.getCoursesForTeacher(course.getTeacherId()).getBody());
    }

    @Test
    void getCoursesForTeacherFail() {
        assertNotEquals(courseRepository.findAllByTeacherId(course.getTeacherId()),
                courseController.getCoursesForTeacher("RandomTeacherId").getBody());
    }

    @Test
    void constructorTest() {
        CourseController courseController = new CourseController(courseRepository,
                enrollmentRepository, validate);
        assertEquals(courseRepository, courseController.getCourseRepository());
        assertEquals(enrollmentRepository, courseController.getEnrollmentRepository());
        assertEquals(validate, courseController.getValidate());
    }

    @Test
    void deleteCourseSuccess() throws JSONException, IOException, InterruptedException {
        assertEquals(ResponseEntity.ok(new Message("Course deleted.")),
                courseController.deleteCourse(request, course.getCourseId()));
    }

    @Test
    void deleteCourseAccessDenied() throws JSONException, IOException, InterruptedException {
        assertEquals(ResponseEntity.ok(new Message(noAccessMessage)),
                courseController.deleteCourse(wrongRequest,course.getCourseId()));
    }

    @Test
    void getCourseSuccess() throws IOException, InterruptedException {
        assertEquals(course, courseController.getCourse(request, course.getCourseId()).getBody());
    }

    @Test
    void getCourseFail() throws IOException, InterruptedException {
        assertEquals(ResponseEntity.notFound().build(), courseController
                .getCourse(request, "RandomNumber"));
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
