package nl.tudelft.sem.courses.controller;

import nl.tudelft.sem.shared.entity.BareLecture;
import nl.tudelft.sem.courses.entity.Course;
import nl.tudelft.sem.courses.entity.Enrollment;
import nl.tudelft.sem.courses.entity.Lecture;
import nl.tudelft.sem.courses.repository.CourseRepository;
import nl.tudelft.sem.courses.repository.EnrollmentRepository;
import nl.tudelft.sem.courses.repository.LectureRepository;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = EnrollmentController.class)
@AutoConfigureMockMvc
@WebMvcTest
// This class doesn't ever need to be serialized, so neither do it's members
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class EnrollmentControllerTest {

    private Course course;
    private EnrollmentController enrollmentController;

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
    void setUp() throws JSONException {
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

        this.course = new Course();
        course.setCourseName(courseName);
        course.setTeacherId(teacherId);
        course.setCourseId(courseId);
        courseRepository.save(course);

        LocalDate localDate = LocalDate.of(2020, 1, 8);
        Date sqlDate = Date.valueOf(localDate);
        BareLecture bareLecture = new BareLecture();
        Lecture lecture = new Lecture();
        lecture.setDuration(30);
        lecture.setScheduledDate(sqlDate);
        lecture.setCourseId(courseId);
        bareLecture.setDurationInMinutes(30);
        bareLecture.setDate(localDate);
        bareLecture.setCourseId(courseId);
        List<Lecture> lectures = new ArrayList<>();
        List<BareLecture> bareLectures = new ArrayList<>();
        lectures.add(lecture);
        bareLectures.add(bareLecture);
        lectureRepository.save(lecture);
        LocalDate dt = localDate.minusDays(5);
        Date sqldt = Date.valueOf(dt);

        enrollmentController =
                new EnrollmentController(enrollmentRepository);

        when(courseRepository.findByCourseId(course.getCourseId())).thenReturn(course);
        when(courseRepository.findByCourseName(course.getCourseName())).thenReturn(course);
        when(courseRepository.findAllByTeacherId(course.getTeacherId()))
                .thenReturn(List.of(course));
        when(lectureRepository.findByCourseId(course.getCourseId())).thenReturn(lectures);
        when(lectureRepository.findByCourseIdAndScheduledDate(course.getCourseId(),
                Date.valueOf(localDate.plusDays(1)))).thenReturn(List.of(lecture));
    }

    @Test
    public void constructorNotNull() {
        assertNotNull(enrollmentController);
    }

    @Test
    void getAllEnrollmentsTest() {
        assertEquals(enrollmentRepository.findAll(),
                enrollmentController.getAllEnrollments().getBody());
    }


        @Test
        void getEnrollmentsByCourseTest () {
            assertEquals(enrollmentRepository.findByCourseId(course.getCourseId()),
                    enrollmentController.getEnrollmentsByCourse(course.getCourseId()).getBody());
        }
    }
