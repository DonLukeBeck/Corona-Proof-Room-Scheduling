package nl.tudelft.sem.courses;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
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
     * @return A list of {@link Lecture}s
     */
    @GetMapping("/lectures")
    @ResponseBody
    public ResponseEntity<?> listLectures() {
        return ResponseEntity.ok(lectureRepository.findAll());
    }

    /**
     * Get endpoint to retrieve all lectures after a certain date
     *
     * @return A list of {@link RequestedLecture}s
     */
    @GetMapping("/date/{date}")
    @ResponseBody
    public ResponseEntity<?> listLecturesAfterDate(@PathVariable("date") Date date) {
        return ResponseEntity.ok(lectureRepository.findAll().stream().filter(l -> l.getScheduledDate().after(date))
            .map(lecture -> new RequestedLecture(
                courseRepository.findById(lecture.getCourseId()).get(),
                lecture.getScheduledDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                lecture.getDuration())).collect(Collectors.toList()));

    }

}
