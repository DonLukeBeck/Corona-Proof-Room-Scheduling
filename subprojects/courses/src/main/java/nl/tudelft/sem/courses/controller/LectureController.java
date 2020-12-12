package nl.tudelft.sem.courses.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import nl.tudelft.sem.courses.entity.BareLecture;
import nl.tudelft.sem.courses.entity.Lecture;
import nl.tudelft.sem.courses.repository.CourseRepository;
import nl.tudelft.sem.courses.repository.EnrollmentRepository;
import nl.tudelft.sem.courses.repository.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/lecture")
public class LectureController {
    @Autowired
    private transient CourseRepository courseRepository;

    @Autowired
    private transient EnrollmentRepository enrollmentRepository;

    @Autowired
    private transient LectureRepository lectureRepository;

    /**
     * Get endpoint to retrieve all lectures
     *
     * @return A list of {@link BareLecture}s
     */
    @GetMapping("/lectures")
    @ResponseBody
    public ResponseEntity<?> listLectures() {
        return ResponseEntity.ok(
            bareFromLecture(lectureRepository.findAll().stream()).collect(Collectors.toList()));
    }

    /**
     * Get endpoint to retrieve all lectures after a certain date
     *
     * @return A list of {@link BareLecture}s
     */
    @GetMapping("/date/{date}")
    @ResponseBody
    public ResponseEntity<?> listLecturesAfterDate(@PathVariable("date") LocalDate date) {
        return ResponseEntity.ok(bareFromLecture(lectureRepository.findAll().stream()
            .filter(l -> date.isAfter(l.getScheduledDate().toLocalDate())))
            .collect(Collectors.toList()));
    }

    private Stream<BareLecture> bareFromLecture(Stream<Lecture> s) {
        return s.map(l -> new BareLecture(l.getCourseId(),
            Instant.ofEpochMilli(l.getScheduledDate().getTime()).atZone(ZoneId.systemDefault())
            .toLocalDate(), l.getDuration()));
    }
}
