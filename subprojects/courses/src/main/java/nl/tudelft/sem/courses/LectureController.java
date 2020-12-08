package nl.tudelft.sem.courses;

import java.time.ZoneId;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
    public ResponseEntity<?> listLecturesAfterDate(@PathVariable("date") Date date) {
        return ResponseEntity.ok(bareFromLecture(lectureRepository.findAll().stream()
            .filter(l -> l.getScheduledDate().after(date))).collect(Collectors.toList()));
    }

    private Stream<BareLecture> bareFromLecture(Stream<Lecture> s) {
        return s.map(l -> new BareLecture(l.getCourseId(), l.getScheduledDate(), l.getDuration()));
    }
}
