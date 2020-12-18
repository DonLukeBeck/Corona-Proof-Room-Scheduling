package nl.tudelft.sem.courses.controller;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import nl.tudelft.sem.shared.entity.AddLecture;
import nl.tudelft.sem.shared.entity.BareLecture;
import nl.tudelft.sem.courses.entity.Lecture;
import nl.tudelft.sem.courses.entity.Message;
import nl.tudelft.sem.courses.repository.CourseRepository;
import nl.tudelft.sem.courses.repository.LectureRepository;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
public class LectureController extends Controller {
    @Autowired
    private transient CourseRepository courseRepository;

    @Autowired
    private transient LectureRepository lectureRepository;

    /**
     * Instantiates repository needed.
     */
    public LectureController(CourseRepository courseRepository,
                                      LectureRepository lectureRepository) {
        this.courseRepository = courseRepository;
        this.lectureRepository = lectureRepository;
    }

    /**
     * Get endpoint to retrieve all lectures.
     *
     * @return A list of {@link BareLecture}s
     */
    @GetMapping("/getAllLectures")
    @ResponseBody
    public ResponseEntity<?> getAllLectures() {
        return ResponseEntity.ok(
            bareFromLecture(lectureRepository.findAll().stream()).collect(Collectors.toList()));
    }

    /**
     * Get endpoint to retrieve all lectures after a certain date.
     *
     * @return A list of {@link BareLecture}s
     */
    @GetMapping("/getLecturesAfterDate")
    @ResponseBody
    public ResponseEntity<?> getLecturesAfterDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(bareFromLecture(lectureRepository.findAll().stream()
            .filter(l -> date.isBefore(l.getScheduledDate().toLocalDate())))
            .collect(Collectors.toList()));
    }

    /**
     * Helper method to convert a {@link BareLecture} stream into a {@link Lecture} stream.
     *
     * @param lectures a stream of lectures
     *
     * @return a stream of bare lectures
     */
    private Stream<BareLecture> bareFromLecture(Stream<Lecture> lectures) {
        return lectures.map(l -> new BareLecture(l.getCourseId(),
            Instant.ofEpochMilli(l.getScheduledDate().getTime()).atZone(ZoneId.systemDefault())
            .toLocalDate(), l.getDuration()));
    }

    /**
     * Plans a lecture with provided arguments.
     */
    @PostMapping(path = "/planNewLecture") // Map ONLY POST Requests
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    // Found 'DD'-anomaly for variable 'lectureId' (lines '96'-'98').
    // -> correct since we need to count
    public ResponseEntity<?> planNewLecture(@RequestBody AddLecture addLecture)
            throws JSONException {
        System.out.println("1");
        if (courseRepository.findByCourseId(addLecture.getCourseId()) != null) {
            Lecture lecture = new Lecture();
            lecture.setCourseId(addLecture.getCourseId());
            lecture.setDuration(addLecture.getDurationInMinutes());
            lecture.setScheduledDate(addLecture.getDate());
            System.out.println("2");
            lectureRepository.save(lecture);
            return ResponseEntity.ok(new Message("Lecture planned."));
        } else {
            System.out.println("3");
            return ResponseEntity.ok(new Message("The course with id "
                    + addLecture.getCourseId() + " does not exist."));
        }
    }

    /**
     * Cancels a lecture with provided arguments.
     */
    @DeleteMapping(path = "/cancelLecture") // Map ONLY POST Requests
    public ResponseEntity<?> cancelLecture(@RequestParam String courseId, @RequestParam @DateTimeFormat(
            iso = DateTimeFormat.ISO.DATE) LocalDate date) throws JSONException {
        // We need to add one day since Spring of MariaDB or something matches against one day off
        Date sqlDate = Date.valueOf(date.plusDays(1));
        List<Lecture> lectures = lectureRepository.findByCourseIdAndScheduledDate(courseId, sqlDate);
        if (lectures.size() == 0) {
            return ResponseEntity.notFound().build();
        }
        for(Lecture l : lectures){
            lectureRepository.delete(l);
        }
        return ResponseEntity.ok(new Message("Lecture(s) cancelled."));
    }
}
