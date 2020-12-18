package nl.tudelft.sem.courses.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import nl.tudelft.sem.courses.util.Validate;
import nl.tudelft.sem.shared.entity.AddCourse;
import nl.tudelft.sem.shared.entity.BareCourse;
import nl.tudelft.sem.courses.entity.Course;
import nl.tudelft.sem.courses.entity.Enrollment;
import nl.tudelft.sem.courses.entity.Message;
import nl.tudelft.sem.courses.repository.CourseRepository;
import nl.tudelft.sem.courses.repository.EnrollmentRepository;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController // This means that this class is a RestController
@RequestMapping(path = "/course") // URL's start with /course (after Application path)
public class CourseController {

    @Autowired
    private transient CourseRepository courseRepository;

    @Autowired
    private transient EnrollmentRepository enrollmentRepository;

    @Autowired
    private transient Validate validate;

    private transient String teacherRole = "teacher";
    protected transient String noAccessMessage = "You are not allowed to view this page. Please contact administrator.";


    /**
     * Instantiates repository needed.
     */
    public CourseController(CourseRepository courseRepository,
                            EnrollmentRepository enrollmentRepository, Validate validate) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.validate = validate;
    }

    public Validate getValidate() {
        return validate;
    }

    public CourseRepository getCourseRepository() {
        return courseRepository;
    }

    public EnrollmentRepository getEnrollmentRepository() {
        return enrollmentRepository;
    }

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
     *
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
     * Adds a new course with provided parameters.
     *
     * @param addCourse the course to add
     *
     * @return an indication of whether the operation succeeded or not.
     */
    @PostMapping(path = "/createNewCourse") // Map ONLY POST Requests
    public ResponseEntity<?> createNewCourse(
            HttpServletRequest request, @RequestBody AddCourse addCourse)
            throws IOException, InterruptedException, JSONException {

        String validation = validate.validateRole(request, teacherRole);
        if (validation.equals(noAccessMessage)) {
            return ResponseEntity.ok(new Message(noAccessMessage));
        }

        Course r = courseRepository.findByCourseId(addCourse.getCourseId());
        try {
            if (r.getCourseId().equals(addCourse.getCourseId())) {
                return ResponseEntity.ok(new Message("Course already exists."));
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {

            for (String id : addCourse.getParticipants()) {
                Enrollment enrollment = new Enrollment();
                enrollment.setCourseId(addCourse.getCourseId());
                enrollment.setStudentId(id);
                enrollmentRepository.save(enrollment);
            }
            Course course = new Course();
            course.setCourseName(addCourse.getCourseName());
            course.setCourseId(addCourse.getCourseId());
            course.setTeacherId(addCourse.getTeacherId());
            courseRepository.save(course);
            return ResponseEntity.ok(new Message("Course created."));
        }
    }

    /**
     * Deletes a new course with provided parameters.
     */
    @DeleteMapping(path = "/deleteCourse") // Map ONLY POST Requests
    public ResponseEntity<?> deleteCourse(HttpServletRequest request, @RequestParam String courseId) throws JSONException, IOException, InterruptedException {
        String validation = validate.validateRole(request, teacherRole);
        if (validation.equals(noAccessMessage)) {
            return ResponseEntity.ok(new Message(noAccessMessage));
        }

            Course r = courseRepository.findByCourseId(courseId);
            if (r == null) {
                return ResponseEntity.notFound().build();
            }
            courseRepository.delete(r);
            return ResponseEntity.ok(new Message("Course deleted."));
    }

    /**
     * Returns a course with provided parameters.
     *
     *
     * @param courseId the Id of the course to delete
     */
    @GetMapping(path = "/getCourse") // Map ONLY POST Requests
    public ResponseEntity<?> getCourse(HttpServletRequest request, @RequestParam String courseId)
            throws JSONException, IOException, InterruptedException {

        String validation = validate.validateRole(request, teacherRole);
        if (validation.equals(noAccessMessage)) {
            return ResponseEntity.ok(new Message(noAccessMessage));
        }
        Course r = courseRepository.findByCourseId(courseId);
        if (r == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(r);
    }

    /**
     * Returns a list of participants with provided parameters.
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
