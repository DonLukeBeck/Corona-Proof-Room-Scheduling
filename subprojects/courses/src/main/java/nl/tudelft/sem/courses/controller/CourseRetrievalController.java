package nl.tudelft.sem.courses.controller;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.tudelft.sem.courses.entity.Course;
import nl.tudelft.sem.courses.entity.Enrollment;
import nl.tudelft.sem.courses.repository.CourseRepository;
import nl.tudelft.sem.courses.repository.EnrollmentRepository;
import nl.tudelft.sem.shared.entity.BareCourse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Getter
@AllArgsConstructor
public class CourseRetrievalController {

    @Autowired
    private transient CourseRepository courseRepository;
    @Autowired
    private transient EnrollmentRepository enrollmentRepository;

    /**
     * Get endpoint to retrieve all courses.
     *
     * @return A list of {@link BareCourse}s
     */
    @GetMapping("/getAllCourses")
    @ResponseBody
    public ResponseEntity<?> getAllCourses() {
        return ResponseEntity.ok(courseRepository.findAll());
    }

    /**
     * Retrieves a course list based on teacher id.
     *
     * @param teacherId id of the teacher
     * @return A list of {@link Course}s
     */
    @GetMapping("/getCoursesForTeacher")
    @ResponseBody
    public ResponseEntity<?> getCoursesForTeacher(@RequestParam String teacherId) {
        List<Course> courseList = courseRepository.findAllByTeacherId(teacherId);
        if (courseList == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(courseList);
    }

    /**
     * Returns a course with provided parameters.
     *
     * @param courseId the Id of the course to delete
     * @return the course if found, else a 404
     */
    @GetMapping(path = "/getCourse") // Map ONLY POST Requests
    public ResponseEntity<?> getCourse(@RequestParam String courseId) {
        Course r = courseRepository.findByCourseId(courseId);
        if (r == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(r);
    }

    /**
     * Returns a list of participants with provided parameters.
     *
     * @param courseId the Id of the course to retrieve the participants for
     * @return a list of course participants
     */
    @GetMapping(path = "/getCourseParticipants") // Map ONLY Get Requests
    public ResponseEntity<?> getCourseParticipants(@RequestParam String courseId) {
        ArrayList<String> result = new ArrayList<>();
        for (Enrollment r : enrollmentRepository.findByCourseId(courseId)) {
            result.add(r.getStudentId());
        }
        return ResponseEntity.ok(result);
    }

}
