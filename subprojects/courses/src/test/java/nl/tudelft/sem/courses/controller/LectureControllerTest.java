
package nl.tudelft.sem.courses.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import nl.tudelft.sem.courses.entity.Message;
import nl.tudelft.sem.courses.repository.CourseRepository;
import nl.tudelft.sem.courses.repository.EnrollmentRepository;
import nl.tudelft.sem.courses.repository.LectureRepository;
import nl.tudelft.sem.courses.util.Validate;
import nl.tudelft.sem.shared.entity.AddLecture;
import nl.tudelft.sem.shared.entity.BareLecture;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
    private transient String noAccessMessage =
            "You are not allowed to view this page. Please contact administrator.";

    private AddLecture addlecture;
    private Lecture lecture;
    private LocalDate localDate;
    private List<BareLecture> bareLectures;
    private LocalDate dt;
    private HttpServletRequest request;
    private HttpServletRequest wrongRequest;

    private LectureController lectureController;

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
    void setUp() throws JSONException, IOException, InterruptedException {
        String courseId = "CSE1200";
        String courseName = "OOPP";
        String teacherId = "Andy";

        Enrollment enrollment = new Enrollment();
        enrollment.setCourseId(courseId);
        enrollment.setStudentId("Henry");
        List<Enrollment> enrollments = new ArrayList<>();
        enrollments.add(enrollment);
        List<String> participants = new ArrayList<>();
        participants.add(enrollment.getStudentId());
        enrollmentRepository.save(enrollment);

        Course course = new Course();
        course.setCourseName(courseName);
        course.setTeacherId(teacherId);
        course.setCourseId(courseId);
        courseRepository.save(course);

        request = Mockito.mock(HttpServletRequest.class);
        wrongRequest = Mockito.mock(HttpServletRequest.class);

        localDate = LocalDate.of(2020, 1, 8);
        Date sqlDate = Date.valueOf(localDate);
        this.addlecture = new AddLecture(courseId, sqlDate, 30);
        BareLecture bareLecture = new BareLecture();
        this.lecture = new Lecture();
        lecture.setDuration(30);
        lecture.setScheduledDate(sqlDate);
        lecture.setCourseId(courseId);
        bareLecture.setDurationInMinutes(30);
        bareLecture.setDate(localDate);
        bareLecture.setCourseId(courseId);
        List<Lecture> lectures = new ArrayList<>();
        bareLectures = new ArrayList<>();
        lectures.add(lecture);
        bareLectures.add(bareLecture);
        lectureRepository.save(lecture);
        dt = localDate.minusDays(5);
        Date sqldt = Date.valueOf(dt);

        lectureController =
                new LectureController(courseRepository, lectureRepository, validate);

        when(courseRepository.findByCourseId(course.getCourseId())).thenReturn(course);
        when(courseRepository.findByCourseName(course.getCourseName())).thenReturn(course);
        when(courseRepository.findAllByTeacherId(course.getTeacherId())).
                thenReturn(List.of(course));
        when(lectureRepository.findByCourseId(course.getCourseId())).thenReturn(lectures);
        when(lectureRepository.findByCourseIdAndScheduledDate(course.getCourseId(),
                Date.valueOf(localDate.plusDays(1)))).thenReturn(List.of(lecture));
        when(lectureRepository.findByScheduledDateAfter(sqldt)).thenReturn(lectures);

        when(validate.validateRole(request, "teacher"))
                .thenReturn("netid");
        when(validate.validateRole(wrongRequest, "teacher"))
                .thenReturn(noAccessMessage);
    }

    @Test
    public void constructor() {
        assertNotNull(lectureController);
        assertEquals(courseRepository, lectureController.getCourseRepository());
        assertEquals(lectureRepository, lectureController.getLectureRepository());
        assertEquals(validate, lectureController.getValidate());
    }

    @Test
    void getAllLectures() {
        assertEquals(lectureRepository.findAll(), lectureController.getAllLectures().getBody());
    }

    @Test
    void getLecturesAfterDate() {
        List<BareLecture> list =
                (List<BareLecture>) lectureController.getLecturesAfterDate(dt).getBody();
        for (BareLecture l : list) {
            System.out.println(l);
            assert(bareLectures.contains(l));
        }
    }

    @Test
    void planNewLectureSuccess() throws JSONException, IOException, InterruptedException {
        assertEquals(ResponseEntity.ok(new Message("Lecture planned.")),
                lectureController.planNewLecture(request, addlecture));
    }

    @Test
    void planNewLectureAccessDenied() throws JSONException, IOException, InterruptedException {
        assertEquals(ResponseEntity.ok(new Message(noAccessMessage)),
                lectureController.planNewLecture(wrongRequest, addlecture));
    }


    @Test
    void planNewLectureFail() throws JSONException, IOException, InterruptedException {
        addlecture.setCourseId("randomCourseIdFail");
        assertEquals(ResponseEntity.ok(new Message(
                "The course with id " + addlecture.getCourseId()
                        + " does not exist.")), lectureController.planNewLecture(request, addlecture));
    }

    @Test
    void cancelLectureSuccess() throws JSONException, IOException, InterruptedException {
        assertEquals(ResponseEntity.ok(new Message("Lecture(s) cancelled.")),
                lectureController.cancelLecture(request, lecture.getCourseId(), localDate));
    }

    @Test
    void cancelLectureAccessDenied() throws JSONException, IOException, InterruptedException {
        assertEquals(ResponseEntity.ok(new Message(noAccessMessage)),
                lectureController.cancelLecture(wrongRequest, lecture.getCourseId(), localDate));
    }

    @Test
    void cancelLectureFail() throws JSONException, IOException, InterruptedException {
        assertEquals(ResponseEntity.notFound().build(),
                lectureController.cancelLecture(request,"randomCourseIdFail", localDate));
        localDate = LocalDate.of(1985, 1, 8);
        assertEquals(ResponseEntity.notFound().build(),
                lectureController.cancelLecture(request, lecture.getCourseId(), localDate));
    }
}
