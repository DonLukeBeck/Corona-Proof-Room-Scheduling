package nl.tudelft.sem.courses.controller;

import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import nl.tudelft.sem.courses.entity.Lecture;
import nl.tudelft.sem.courses.repository.CourseRepository;
import nl.tudelft.sem.courses.repository.LectureRepository;
import nl.tudelft.sem.courses.util.Validate;
import nl.tudelft.sem.shared.entity.AddLecture;
import nl.tudelft.sem.shared.entity.BareLecture;
import nl.tudelft.sem.shared.entity.StringMessage;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/lecture")
public class LectureController {

    @Autowired
    private transient CourseRepository courseRepository;
    @Autowired
    private transient LectureRepository lectureRepository;
    @Autowired
    private transient Validate validate;

    private transient String teacherRole = "teacher";

    protected transient StringMessage noAccessMessage =
        new StringMessage("You are not allowed to view this page. Please contact administrator.");

    /**
     * Instantiates repository needed.
     */
    public LectureController(CourseRepository courseRepository,
                             LectureRepository lectureRepository, Validate validate) {
        this.courseRepository = courseRepository;
        this.lectureRepository = lectureRepository;
        this.validate = validate;
    }

    /**
     * Getter for the course repository.
     *
     * @return the course repository
     */
    public CourseRepository getCourseRepository() {
        return courseRepository;
    }

    /**
     * Getter for the lecture repository.
     *
     * @return the lecture repository
     */
    public LectureRepository getLectureRepository() {
        return lectureRepository;
    }

    /**
     * Getter for the validate object.
     *
     * @return the validate object
     */
    public Validate getValidate() {
        return validate;
    }

    /**
     * Cancels a lecture with provided properties.
     *
     * @param courseId the Id of the associated course
     * @param date the date on which to cancel the lecture
     * @return an indication of whether the operation succeeded or not
     */
    @DeleteMapping(path = "/cancelLecture") // Map ONLY POST Requests
    public ResponseEntity<?> cancelLecture(HttpServletRequest request,
                                           @RequestParam String courseId,
                                           @RequestParam @DateTimeFormat(
            iso = DateTimeFormat.ISO.DATE) LocalDate date) throws JSONException, IOException,
            InterruptedException {

        String validation = validate.validateRole(request, teacherRole);
        if (validation.equals(noAccessMessage.getMessage())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(noAccessMessage);
        }

        // We need to add one day since Spring of MariaDB or something matches against one day off
        Date sqlDate = Date.valueOf(date.plusDays(1));
        List<Lecture> lectures =
                lectureRepository.findByCourseIdAndScheduledDate(courseId, sqlDate);
        if (lectures.size() == 0) {
            return ResponseEntity.notFound().build();
        }
        for (Lecture l : lectures) {
            lectureRepository.delete(l);
        }
        return ResponseEntity.ok(new StringMessage("Lecture(s) cancelled."));
    }
}
