
package nl.tudelft.sem.courses.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
import nl.tudelft.sem.shared.Constants;
import nl.tudelft.sem.shared.entity.AddLecture;
import nl.tudelft.sem.shared.entity.BareLecture;
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

@ContextConfiguration(classes = LectureController.class)
@AutoConfigureMockMvc
@WebMvcTest
// This class doesn't ever need to be serialized, so neither do it's members
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class LectureRetrievalControllerTest {

    private AddLecture addlecture;
    private Lecture lecture;
    private LocalDate localDate;
    private List<BareLecture> bareLectures;
    private LocalDate dt;
    private LectureRetrievalController lectureController;

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

        localDate = LocalDate.of(2020, 1, 8);
        Date sqlDate = Date.valueOf(localDate);
        this.addlecture = new AddLecture(courseId, sqlDate, 30);
        this.lecture = new Lecture();
        lecture.setDuration(30);
        lecture.setScheduledDate(sqlDate);
        lecture.setCourseId(courseId);
        BareLecture bareLecture = new BareLecture();
        bareLecture.setDurationInMinutes(30);
        bareLecture.setDate(localDate);
        bareLecture.setCourseId(courseId);
        List<Lecture> lectures = new ArrayList<>();
        bareLectures = new ArrayList<>();
        lectures.add(lecture);
        bareLectures.add(bareLecture);
        lectureRepository.save(lecture);

        lectureController =
                new LectureRetrievalController(lectureRepository);


        dt = localDate.minusDays(5);
        Date sqldt = Date.valueOf(dt);
        when(lectureRepository.findByScheduledDateAfter(sqldt)).thenReturn(lectures);
        when(lectureRepository.findByScheduledDateAfter(null)).thenReturn(null);
        when(lectureRepository.findAll()).thenReturn(lectures);
    }

    @Test
    public void constructor() {
        assertNotNull(lectureController);
        assertEquals(lectureRepository, lectureController.getLectureRepository());
    }

    @Test
    void getAllLectures() {
        assertEquals(bareLectures
                , lectureController.getAllLectures().getBody());
    }

    @Test
    void getLecturesAfterDate() {
        List<BareLecture> list =
                (List<BareLecture>) lectureController.getLecturesAfterDate(dt).getBody();
        for (BareLecture l : list) {
            assert (bareLectures.contains(l));
        }
    }

    @Test
    void getLecturesAfterDateNoDate() {
        assertEquals(null, lectureController.getLecturesAfterDate(null).getBody());
    }

    @Test
    void getLecturesAfterDateWrongDate() {
        dt = localDate.plusYears(5);
        List<BareLecture> list =
                (List<BareLecture>) lectureController.getLecturesAfterDate(dt).getBody();
        assertEquals(0, list.size());
    }
}
