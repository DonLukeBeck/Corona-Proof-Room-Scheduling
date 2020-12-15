package nl.tudelft.sem.courses.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import nl.tudelft.sem.courses.entity.BareCourse;
import nl.tudelft.sem.courses.entity.Course;
import nl.tudelft.sem.courses.repository.CourseRepository;
import nl.tudelft.sem.courses.repository.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/course")
public class CourseController {
    @Autowired
    private transient CourseRepository courseRepository;

    @Autowired
    private transient LectureRepository lectureRepository;

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
}
