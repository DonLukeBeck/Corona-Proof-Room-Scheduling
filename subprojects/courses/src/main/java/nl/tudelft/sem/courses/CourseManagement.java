package nl.tudelft.sem.courses;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // This means that this class is a RestController
@RequestMapping(path = "/course") // URL's start with /course (after Application path)
public class CourseManagement {

    @Autowired
    private transient CourseRepository courseRepository;

    @Autowired
    private transient EnrollmentRepository enrollmentRepository;

    @Autowired
    private transient LectureRepository lectureRepository;

    /**
     * Instantiates repository needed.
     */
    public CourseManagement(CourseRepository courseRepository,
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
    public String createNewCourse(String courseId, String courseName, String teacherId,
                                  List<String> participants) {
        for (Course r : courseRepository.findAll()) {
            if (r.getCourseId().equals(courseId)) {
                if (r.getCourseName() == courseName) {
                    return "Already Exists";
                }
            }
        }

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

    /**
     * Deletes a new course with provided parameters.
     */
    @PostMapping(path = "/deleteCourse") // Map ONLY POST Requests
    public String deleteCourse(String courseId) {
        for (Course r : courseRepository.findAll()) {
            if (r.getCourseId().equals(courseId)) {
                courseRepository.delete(r);
                return "Deleted";
            }
        }
        return "Error";
    }

    /**
     * Returns a course with provided parameters.
     */
    @GetMapping(path = "/getCourse") // Map ONLY POST Requests
    public Course getCourse(String courseId) {
        for (Course r : courseRepository.findAll()) {
            if (r.getCourseId().equals(courseId)) {
                return r;
            }
        }
        return null;
    }

    /**
     * Returns a list of participants with provided parameters.
     */
    @GetMapping(path = "/getCourseParticipants") // Map ONLY POST Requests
    public List<String> getCourseParticipants(String courseId) {
        ArrayList<String> result = new ArrayList<>();
        for (Enrollment r : enrollmentRepository.findAll()) {
            if (r.getCourseId().equals(courseId)) {
                result.add(r.getStudentId());
            }
        }
        return result;
    }

    /**
     * Plans a lecture with provided arguments.
     */
    @PostMapping(path = "/planNewLecture") // Map ONLY POST Requests
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    // Found 'DD'-anomaly for variable 'lectureId' (lines '96'-'98').
    // -> correct since we need to count
    public String planNewLecture(String courseId, int durationInMinutes, Date date) {
        int lectureId = 1;
        for (Lecture lecture : lectureRepository.findAll()) {
            lectureId++;
        }

        Lecture lecture = new Lecture();
        lecture.setCourseId(courseId);
        lecture.setDuration(durationInMinutes);
        lecture.setLectureId(lectureId);
        lecture.setScheduledDate(date);
        lectureRepository.save(lecture);
        return "Lecture added";
    }

    /**
     * Cancels a lecture with provided arguments.
     */
    @PostMapping(path = "/cancelLecture") // Map ONLY POST Requests
    public String cancelLecture(String courseId, Date date) {
        for (Lecture lecture : lectureRepository.findAll()) {
            if (lecture.getCourseId().equals(courseId)) {
                if (lecture.getScheduledDate().equals(date)) {
                    lectureRepository.delete(lecture);
                    return "Lecture deleted";
                }
            }
        }

        return "Error";
    }


}
