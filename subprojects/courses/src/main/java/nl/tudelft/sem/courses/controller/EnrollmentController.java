package nl.tudelft.sem.courses.controller;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;
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

@Getter
@AllArgsConstructor
@RestController
@RequestMapping(path = "/enrollment")
public class EnrollmentController {

    @Autowired
    private transient EnrollmentRepository enrollmentRepository;

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
     * @param courseId the id of the course to get the enrollments for
     * @return A list of {@link BareEnrollment}s
     */
    @GetMapping("/getEnrollmentsByCourse")
    @ResponseBody
    public ResponseEntity<?> getEnrollmentsByCourse(@RequestParam("courseId") String courseId) {
        if (courseId == null) {
            return ResponseEntity.notFound().build();
        }
        Collection<Enrollment> temp = enrollmentRepository.findAll();
        return ResponseEntity.ok(bareFromEnrollment(
                temp.stream()
                .filter(e -> courseId
                        .equals(e.getCourseId())))
                .collect(Collectors
                        .toList()));
    }

    /**
     * Helper method to prepare a stream of Enrollments to be sent over the network.
     *
     * @param s the stream of enrollments
     * @return a stream of bare enrollments
     */
    private Stream<BareEnrollment> bareFromEnrollment(Stream<Enrollment> s) {
        return s.map(e -> new BareEnrollment(e.getCourseId(), e.getStudentId()));
    }
}