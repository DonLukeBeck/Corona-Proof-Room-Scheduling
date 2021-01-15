package nl.tudelft.sem.courses.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import nl.tudelft.sem.courses.entity.Course;
import nl.tudelft.sem.courses.entity.Enrollment;
import nl.tudelft.sem.courses.repository.CourseRepository;
import nl.tudelft.sem.courses.repository.EnrollmentRepository;
import nl.tudelft.sem.courses.util.Validate;
import nl.tudelft.sem.shared.entity.AddCourse;
import nl.tudelft.sem.shared.entity.BareCourse;
import nl.tudelft.sem.shared.entity.StringMessage;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    protected transient StringMessage noAccessMessage =
        new StringMessage("You are not allowed to view this page. Please contact administrator.");

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
     * Adds a new course with provided parameters.
     *
     * @param request an {@link HttpServletRequest} containing validation information
     * @param addCourse the course to add
     * @return an indication of whether the operation succeeded or not.
     */
    @PostMapping(path = "/createNewCourse") // Map ONLY POST Requests
    public ResponseEntity<?> createNewCourse(
            HttpServletRequest request, @RequestBody AddCourse addCourse)
            throws IOException, InterruptedException, JSONException {

        String validation = validate.validateRole(request, teacherRole);
        if (validation.equals(noAccessMessage.getMessage())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(noAccessMessage);
        }

        Course r = courseRepository.findByCourseId(addCourse.getCourseId());
        try {
            if (r.getCourseId().equals(addCourse.getCourseId())) {
                return ResponseEntity.ok(new StringMessage("Course already exists."));
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
            return ResponseEntity.ok(new StringMessage("Course created."));
        }
    }

    /**
     * Deletes a new course with provided parameters.
     *
     * @param courseId the Id of the course to delete
     * @param request an {@link HttpServletRequest} containing validation information
     * @return an indication of whether the operation succeeded or not.
     */
    @DeleteMapping(path = "/deleteCourse") // Map ONLY POST Requests
    public ResponseEntity<?> deleteCourse(HttpServletRequest request,
                                          @RequestParam String courseId)
            throws JSONException, IOException, InterruptedException {

        String validation = validate.validateRole(request, teacherRole);
        if (validation.equals(noAccessMessage.getMessage())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(noAccessMessage);
        }

        Course r = courseRepository.findByCourseId(courseId);
        if (r == null) {
            return ResponseEntity.notFound().build();
        }
        courseRepository.delete(r);
        return ResponseEntity.ok(new StringMessage("Course deleted."));
    }
}