package nl.tudelft.sem.courses.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import nl.tudelft.sem.courses.entity.Course;
import nl.tudelft.sem.courses.entity.Enrollment;
import nl.tudelft.sem.courses.entity.Lecture;
import nl.tudelft.sem.courses.repository.CourseRepository;
import nl.tudelft.sem.courses.repository.EnrollmentRepository;
import nl.tudelft.sem.courses.repository.LectureRepository;
import nl.tudelft.sem.courses.util.Validate;
import nl.tudelft.sem.shared.entity.AddCourse;
import nl.tudelft.sem.shared.entity.StringMessage;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = CourseController.class)
@AutoConfigureMockMvc
@WebMvcTest
// This class doesn't ever need to be serialized, so neither do it's members
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class CourseControllerTest {
    private transient StringMessage noAccessMessage =
            new StringMessage("You are not allowed to view this page. Please contact administrator.");

    private AddCourse addcourse;
    private Course course;
    private Enrollment enrollment;
    private List<String> participants;
    private List<Enrollment> enrollments;

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
    void setUp() throws IOException, InterruptedException, JSONException {
        String courseId = "CSE1200";
        String courseName = "OOPP";
        String teacherId = "Andy";

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
        when(courseRepository.findAllByTeacherId(
                course.getTeacherId())).thenReturn(List.of(course));

        when(enrollmentRepository.findByCourseId(course.getCourseId())).thenReturn(enrollments);

        when(lectureRepository.findByCourseId(course.getCourseId())).thenReturn(lectures);
        when(lectureRepository.findByCourseIdAndScheduledDate(
                course.getCourseId(), Date.valueOf(
                        localDate.plusDays(1)))).thenReturn(List.of(lecture));

        when(validate.validateRole(request, "teacher"))
                .thenReturn("netid");
        when(validate.validateRole(wrongRequest, "teacher"))
                .thenReturn(noAccessMessage.getMessage());

        courseController =
                new CourseController(courseRepository, enrollmentRepository, validate);
    }

    @Test
    public void constructorNotNull() {
        assertNotNull(courseController);
    }

    @Test
    void createNewCourseSuccess() throws IOException, InterruptedException {
        AddCourse newOne = new AddCourse(
                "CSE1299", "OOPP", "Andy", participants);

        assertEquals(ResponseEntity.ok(new StringMessage("Course created.")),
                courseController.createNewCourse
                        (request, newOne));
    }

    @Test
    void createNewCourseAccessDenied() throws IOException, InterruptedException {
        AddCourse newOne = new AddCourse(
                "CSE1299", "OOPP", "Andy", participants);

        assertEquals(ResponseEntity.status(HttpStatus.FORBIDDEN).body(noAccessMessage),
                courseController.createNewCourse(wrongRequest, newOne));
    }

    @Test
    void createNewCourseAlreadyExists() throws IOException, InterruptedException {
        assertEquals(ResponseEntity.ok(new StringMessage("Course already exists.")),
                courseController.createNewCourse(request, addcourse));
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
        assertEquals(ResponseEntity.ok(new StringMessage("Course deleted.")),
                courseController.deleteCourse(request, course.getCourseId()));
    }

    @Test
    void deleteCourseAccessDenied() throws JSONException, IOException, InterruptedException {
        assertEquals(ResponseEntity.status(HttpStatus.FORBIDDEN).body(noAccessMessage),
                courseController.deleteCourse(wrongRequest,course.getCourseId()));
    }

    @Test
    void deleteCourseNotExistent() throws JSONException, IOException, InterruptedException {
        assertEquals(ResponseEntity.notFound().build(),
                courseController.deleteCourse(request,"RandomNumber"));
    }

    @Test
    void getCourseParticipants() {
        ResponseEntity<ArrayList<String>> response = (ResponseEntity<ArrayList<String>>)
                courseController.getCourseParticipants(enrollment.getCourseId());

        for (String i : response.getBody()) {
            Enrollment e = new Enrollment();
            e.setCourseId(enrollment.getCourseId());
            e.setStudentId(i);
            assert (enrollments.contains(e));
        }
    }

    @Test
    void getCourseParticipantsFail() {
        enrollment.setCourseId("randomCourseIdFail");
        ResponseEntity<ArrayList<String>> response = (ResponseEntity<ArrayList<String>>)
                courseController.getCourseParticipants(enrollment.getCourseId());
        assertEquals(0, response.getBody().size());
    }
}
