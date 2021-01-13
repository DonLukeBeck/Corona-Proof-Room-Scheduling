package nl.tudelft.sem.courses.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LecturePlannerController {

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
    public LecturePlannerController(CourseRepository courseRepository,
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
     * Plans a lecture with provided properties.
     *
     * @param addLecture the object containing the lecture details
     * @return an indication of whether the operation succeeded or not
     */
    @PostMapping(path = "/planNewLecture") // Map ONLY POST Requests
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    // Found 'DD'-anomaly for variable 'lectureId' (lines '96'-'98').
    // -> correct since we need to count
    public ResponseEntity<?> planNewLecture(HttpServletRequest request,
                                            @RequestBody AddLecture addLecture)
            throws JSONException, IOException, InterruptedException {

        String validation = validate.validateRole(request, teacherRole);
        if (validation.equals(noAccessMessage.getMessage())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(noAccessMessage);
        }

        if (courseRepository.findByCourseId(addLecture.getCourseId()) != null) {
            Lecture lecture = new Lecture();
            lecture.setCourseId(addLecture.getCourseId());
            lecture.setDuration(addLecture.getDurationInMinutes());
            lecture.setScheduledDate(addLecture.getDate());
            lectureRepository.save(lecture);
            return ResponseEntity.ok(new StringMessage("Lecture planned."));
        } else {
            return ResponseEntity.ok(new StringMessage("The course with id "
                    + addLecture.getCourseId() + " does not exist."));
        }
    }
}
