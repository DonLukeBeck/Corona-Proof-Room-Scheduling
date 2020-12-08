package nl.tudelft.sem.courses;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
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
@RequestMapping(path = "/enrollment")
public class EnrollmentController {
    @Autowired
    private transient CourseRepository courseRepository;

    @Autowired
    private transient EnrollmentRepository enrollmentRepository;

    /**
     * Get endpoint to retrieve all enrollments
     *
     * @return A list of {@link BareEnrollment}s
     */
    @GetMapping("/enrollments")
    @ResponseBody
    public ResponseEntity<?> listEnrollments() {
        return ResponseEntity.ok(bareFromEnrollment(
            enrollmentRepository.findAll().stream()).collect(Collectors.toList()));
    }

    /**
     * Get endpoint to retrieve all enrollments from a specific course
     *
     * @return A list of {@link BareEnrollment}s
     */
    @GetMapping("/course/{courseId}")
    @ResponseBody
    public ResponseEntity<?> listEnrollmentsByCourse(@PathVariable("courseId") String courseId) {
        return ResponseEntity.ok(bareFromEnrollment(enrollmentRepository.findAll().stream()
            .filter(e -> e.getCourseId().equals(courseId))).collect(Collectors.toList()));
    }

    private Stream<BareEnrollment> bareFromEnrollment(Stream<Enrollment> s) {
        return s.map(e -> new BareEnrollment(e.getCourseId(), e.getStudentId()));
    }
}
