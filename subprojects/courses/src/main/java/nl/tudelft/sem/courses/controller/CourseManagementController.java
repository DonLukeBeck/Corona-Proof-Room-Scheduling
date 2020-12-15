package nl.tudelft.sem.courses.controller;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import nl.tudelft.sem.courses.entity.AddCourse;
import nl.tudelft.sem.courses.entity.AddLecture;
import nl.tudelft.sem.courses.entity.Course;
import nl.tudelft.sem.courses.entity.Enrollment;
import nl.tudelft.sem.courses.entity.Lecture;
import nl.tudelft.sem.courses.repository.CourseRepository;
import nl.tudelft.sem.courses.repository.EnrollmentRepository;
import nl.tudelft.sem.courses.repository.LectureRepository;
import nl.tudelft.sem.courses.util.JwtValidate;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    private transient String errorMessage = "Error";
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
            throws IOException, InterruptedException {
        JSONObject jwtInfo = jwtValidate.jwtValidate(request);
        return jwtInfo;
    }

    /**
     * Adds a new course with provided parameters.
     */
    @PostMapping(path = "/createNewCourse") // Map ONLY POST Requests
    public String createNewCourse(HttpServletRequest request, @RequestBody AddCourse addCourse)
            throws IOException, InterruptedException, JSONException {

        JSONObject jwtInfo = validate(request);
        try {
            if (!jwtInfo.getString("role").equals("teacher")) {
                return "You are not allowed to create a course. Please contact administrator.";
            }
        } catch (Exception e) {
            return "You are not allowed to create a course. Please contact administrator.";
        }

        Course r = courseRepository.findByCourseId(addCourse.getCourseId());
        try {
            if (r.getCourseId().equals(addCourse.getCourseId())) {
                return "Already Exists";
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
            return "Saved";
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
            return "Deleted";
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
        return course.getCourseId();
    }

    /**
     * Plans a lecture with provided arguments.
     */
    @PostMapping(path = "/planNewLecture") // Map ONLY POST Requests
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    // Found 'DD'-anomaly for variable 'lectureId' (lines '96'-'98').
    // -> correct since we need to count
    public String planNewLecture(@RequestParam String courseId, @RequestParam int durationInMinutes,
                                 @RequestParam  Date date) {
        if (courseId == null || durationInMinutes < 0) {
            return errorMessage;
        }
        if (courseRepository.findByCourseId(courseId) != null) {
            int lectureId = 1 + lectureRepository.findAll().size();
            Lecture lecture = new Lecture();
            lecture.setCourseId(courseId);
            lecture.setDuration(durationInMinutes);
            //lecture.setLectureId(lectureId);
            lecture.setScheduledDate(date);
            lectureRepository.save(lecture);
            return "Lecture added";
        }
        return errorMessage;
    }

    /**
     * Cancels a lecture with provided arguments.
     */
    @DeleteMapping(path = "/cancelLecture") // Map ONLY POST Requests
    public String cancelLecture(@RequestParam String courseId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Date sqlDate = Date.valueOf(date);
        Lecture lecture = lectureRepository.findByCourseIdAndDate(courseId, sqlDate);
        if (lecture == null) {
            return errorMessage;
        }
        lectureRepository.delete(lecture);
        return "Lecture deleted";
    }

}
