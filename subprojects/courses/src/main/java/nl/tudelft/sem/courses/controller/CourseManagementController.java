package nl.tudelft.sem.courses.controller;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import nl.tudelft.sem.courses.entity.Course;
import nl.tudelft.sem.courses.entity.Enrollment;
import nl.tudelft.sem.courses.entity.Lecture;
import nl.tudelft.sem.courses.repository.CourseRepository;
import nl.tudelft.sem.courses.repository.EnrollmentRepository;
import nl.tudelft.sem.courses.repository.LectureRepository;
import nl.tudelft.sem.courses.util.JwtValidate;
import nl.tudelft.sem.shared.entity.AddCourse;
import nl.tudelft.sem.shared.entity.AddLecture;
import nl.tudelft.sem.shared.entity.BareCourse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController // This means that this class is a RestController
@RequestMapping(path = "/course") // URL's start with /course (after Application path)
public class CourseManagementController {

    @Autowired
    private transient CourseRepository courseRepository;

    @Autowired
    private transient EnrollmentRepository enrollmentRepository;

    @Autowired
    private transient LectureRepository lectureRepository;

    private transient String errorMessage = "{\"message\": \"Error\"}";
    private transient JwtValidate jwtValidate = new JwtValidate();

    /**
     * Instantiates repository needed.
     */
    public CourseManagementController(CourseRepository courseRepository,
                                      EnrollmentRepository enrollmentRepository,
                                      LectureRepository lectureRepository) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.lectureRepository = lectureRepository;
    }

    public JSONObject validate(HttpServletRequest request)
            throws IOException, InterruptedException, JSONException {
        JSONObject jwtInfo = jwtValidate.jwtValidate(request);
        return jwtInfo;
    }

    /**
     * Get endpoint to retrieve all courses.
     *
     * @return A list of {@link BareCourse}s
     */
    @GetMapping("/courses")
    @ResponseBody
    public ResponseEntity<?> listCourses() {
        return ResponseEntity.ok(courseRepository.findAll());
    }

    /**
     * Get endpoint to a course using an id.
     *
     * @return the {@link BareCourse} with courseId as id
     */
    @GetMapping("/id/{id}")
    @ResponseBody
    public ResponseEntity<?> listCourses(@PathVariable("id") String id) {
        var res = courseRepository.findById(id);
        if (res.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(res.get());
    }

    /**
     * Retrieves a course list based on teacher id.
     *
     * @param request request information
     * @param id id of the teacher
     * @return a list of courses based on teacher id
     */
    @GetMapping("/teacher/{id}")
    @ResponseBody
    public ResponseEntity<?> getCoursesTeacher(HttpServletRequest request,
                                               @PathVariable("id") String id) {
        List<Course> courseList = courseRepository.findAllByTeacherId(id);
        if (courseList == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(courseList);
    }

    /**
     * Adds a new course with provided parameters.
     */
    @PostMapping(path = "/createNewCourse") // Map ONLY POST Requests
    public ResponseEntity<?> createNewCourse(HttpServletRequest request, @RequestBody AddCourse addCourse)
            throws IOException, InterruptedException, JSONException {

        JSONObject jwtInfo = validate(request);
        try {
            if (!jwtInfo.getString("role").equals("teacher")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    stringToJsonMessage("You are not allowed to create a course. Please contact administrator."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                stringToJsonMessage("You are not allowed to create a course. Please contact administrator."));
        }

        Course r = courseRepository.findByCourseId(addCourse.getCourseId());
        try {
            if (r.getCourseId().equals(addCourse.getCourseId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    stringToJsonMessage("Already Exists"));
            }
            return null;
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
            return ResponseEntity.ok(stringToJsonMessage("Saved"));
        }
    }

    /**
     * Deletes a new course with provided parameters.
     */
    @DeleteMapping(path = "/deleteCourse") // Map ONLY POST Requests
    public String deleteCourse(@RequestParam String courseId) {
        Course r = courseRepository.findByCourseId(courseId);
        if (r == null) {
            return errorMessage;
        }

        if (r.getCourseId().equals(courseId)) {
            courseRepository.delete(r);
            return stringToJsonMessage("Course Deleted");
        }

        return errorMessage;
    }

    /**
     * Returns a course with provided parameters.
     */
    @GetMapping(path = "/getCourse") // Map ONLY POST Requests
    public Course getCourse(@RequestParam String courseId) {
        Course r = courseRepository.findByCourseId(courseId);
        if (r == null) {
            return null;
        }
        return r;
    }

    /**
     * Returns a list of participants with provided parameters.
     */
    @GetMapping(path = "/getCourseParticipants") // Map ONLY Get Requests
    public List<String> getCourseParticipants(@RequestParam String courseId) {
        ArrayList<String> result = new ArrayList<>();
        for (Enrollment r : enrollmentRepository.findByCourseId(courseId)) {
            result.add(r.getStudentId());
        }
        return result;
    }

    /**
     * Returns the courseId given the teacher's ID.
     */
    @GetMapping(path = "/getCourseIdForTeacher") // Map ONLY Get Requests
    public String getCourseIdForTeacher(@RequestParam String teacherId) {
        Course course = courseRepository.findByTeacherId(teacherId);
        if (course == null) {
            return errorMessage;
        }
        return stringToJsonMessage(course.getCourseId());
    }

    /**
     * Plans a lecture with provided arguments.
     */
    @PostMapping(path = "/planNewLecture") // Map ONLY POST Requests
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    // Found 'DD'-anomaly for variable 'lectureId' (lines '96'-'98').
    // -> correct since we need to count
    public String planNewLecture(@RequestBody AddLecture addLecture) {
        if (courseRepository.findByCourseId(addLecture.getCourseId()) != null) {
            Lecture lecture = new Lecture();
            lecture.setCourseId(addLecture.getCourseId());
            lecture.setDuration(addLecture.getDurationInMinutes());
            lecture.setScheduledDate(addLecture.getDate());
            lectureRepository.save(lecture);
            return stringToJsonMessage("nl.tudelft.sem.shared.entity.Lecture added");
        } else {
            return stringToJsonMessage("The course with id " + addLecture.getCourseId() +
                " does not exist.");
        }
    }

    /**
     * Cancels a lecture with provided arguments.
     */
    @DeleteMapping(path = "/cancelLecture") // Map ONLY POST Requests
    public String cancelLecture(@RequestParam String courseId, @RequestParam @DateTimeFormat(
            iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        // We need to add one day since Spring of MariaDB or something matches against one day off
        Date sqlDate = Date.valueOf(date.plusDays(1));
        Lecture lecture = lectureRepository.findByCourseIdAndScheduledDate(courseId, sqlDate);
        if (lecture == null) {
            return errorMessage;
        }
        lectureRepository.delete(lecture);
        return stringToJsonMessage("nl.tudelft.sem.shared.entity.Lecture deleted");
    }

    private String stringToJsonMessage(String s) {
        return "{\"message\": \"" + s + "\"}";
    }

}
