package nl.tudelft.sem.courses.controller;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import nl.tudelft.sem.courses.entity.Enrollment;
import nl.tudelft.sem.courses.repository.EnrollmentRepository;
import nl.tudelft.sem.shared.entity.BareEnrollment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/enrollment")
public class EnrollmentController extends Controller {

    @Autowired
    private transient EnrollmentRepository enrollmentRepository;

    /**
     * Instantiates repository needed.
     */
    public EnrollmentController(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    /**
     * Get endpoint to retrieve all enrollments.
     *
     * @return A list of {@link BareEnrollment}s
     */
    @GetMapping("/getAllEnrollments")
    @ResponseBody
    public ResponseEntity<?> getAllEnrollments() {
        return ResponseEntity.ok(bareFromEnrollment(
            enrollmentRepository.findAll().stream()).collect(Collectors.toList()));
    }

    /**
     * Get endpoint to retrieve all enrollments from a specific course.
     *
     * @return A list of {@link BareEnrollment}s
     */
    @GetMapping("/getEnrollmentsByCourse")
    @ResponseBody
    public ResponseEntity<?> getEnrollmentsByCourse(@RequestParam("courseId") String courseId) {
        return ResponseEntity.ok(bareFromEnrollment(enrollmentRepository.findAll().stream()
            .filter(e -> e.getCourseId().equals(courseId))).collect(Collectors.toList()));
    }

    private Stream<BareEnrollment> bareFromEnrollment(Stream<Enrollment> s) {
        return s.map(e -> new BareEnrollment(e.getCourseId(), e.getStudentId()));
    }
}
