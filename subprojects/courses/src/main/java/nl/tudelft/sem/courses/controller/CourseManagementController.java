package nl.tudelft.sem.courses.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem.courses.entity.Course;
import nl.tudelft.sem.courses.entity.Enrollment;
import nl.tudelft.sem.courses.entity.Lecture;
import nl.tudelft.sem.courses.repository.CourseRepository;
import nl.tudelft.sem.courses.repository.EnrollmentRepository;
import nl.tudelft.sem.courses.repository.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    /**
     * Adds a new course with provided parameters.
     */
    @PostMapping(path = "/createNewCourse") // Map ONLY POST Requests
    public String createNewCourse(@RequestParam String courseId, @RequestParam String courseName,
                                  @RequestParam String teacherId,
                                  @RequestParam List<String> participants) {
        Course r = courseRepository.findByCourseId(courseId);
        try {
            if (r.getCourseId().equals(courseId)) {
                return "Already Exists";
            }
            return null;
        } catch (Exception e) {

            for (String id : participants) {
                Enrollment enrollment = new Enrollment();
                enrollment.setCourseId(courseId);
                enrollment.setStudentId(id);
                enrollmentRepository.save(enrollment);
            }
            Course course = new Course();
            course.setCourseName(courseName);
            course.setCourseId(courseId);
            course.setTeacherId(teacherId);
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
            lecture.setLectureId(lectureId);
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
    public String cancelLecture(@RequestParam String courseId, @RequestParam Date date) {
        Lecture lecture = lectureRepository.findByCourseIdAndDate(courseId, date);
        if (lecture == null) {
            return errorMessage;
        }
        lectureRepository.delete(lecture);
        return "Lecture deleted";
    }


}
