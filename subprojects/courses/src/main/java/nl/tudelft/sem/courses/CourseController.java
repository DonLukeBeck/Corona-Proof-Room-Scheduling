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
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CourseController {
    @Autowired
    private transient CourseRepository courseRepository;

    @Autowired
    private transient LectureRepository lectureRepository;

    /**
     * Get endpoint to retrieve all courses
     *
     * @return A list of {@link Course}s
     */
    @GetMapping("courses")
    @ResponseBody
    public ResponseEntity<?> listCourses() {
        return ResponseEntity.ok(courseRepository.findAll());
    }

}
