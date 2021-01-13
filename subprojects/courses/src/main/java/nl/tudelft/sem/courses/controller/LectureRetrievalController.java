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

public class LectureRetrievalController {

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
    public LectureRetrievalController(CourseRepository courseRepository,
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
     * Get endpoint to retrieve all lectures.
     *
     * @return A list of {@link BareLecture}s
     */
    @GetMapping("/getAllLectures")
    @ResponseBody
    public ResponseEntity<?> getAllLectures() {
        Stream<BareLecture> tt = lectureRepository.findAll().stream().map(l -> new BareLecture(l.getCourseId(),
                        Instant.ofEpochMilli(l.getScheduledDate().getTime()).atZone(ZoneId.systemDefault())
                                .toLocalDate(), l.getDuration()));

        return ResponseEntity.ok(
               tt.collect(Collectors.toList()));
    }

    /**
     * Get endpoint to retrieve all lectures after a certain date.
     *
     * @param date the date for which to retrieve the lectures
     * @return A list of {@link BareLecture}s
     */
    @GetMapping("/getLecturesAfterDate")
    @ResponseBody
    public ResponseEntity<?> getLecturesAfterDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Date sqlDate = Date.valueOf(date);
        Stream<BareLecture> tt = lectureRepository
                .findByScheduledDateAfter(sqlDate).stream().map(l -> new BareLecture(l.getCourseId(),
                Instant.ofEpochMilli(l.getScheduledDate().getTime()).atZone(ZoneId.systemDefault())
                        .toLocalDate(), l.getDuration()));
        return ResponseEntity.ok(tt.collect(Collectors.toList()));
    }
}
