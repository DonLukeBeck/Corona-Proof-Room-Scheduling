package nl.tudelft.sem.courses.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.tudelft.sem.courses.entity.Lecture;
import nl.tudelft.sem.courses.repository.CourseRepository;
import nl.tudelft.sem.courses.repository.LectureRepository;
import nl.tudelft.sem.courses.util.Validate;
import nl.tudelft.sem.shared.Constants;
import nl.tudelft.sem.shared.entity.AddLecture;
import nl.tudelft.sem.shared.entity.StringMessage;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Getter
@AllArgsConstructor
public class LecturePlannerController {

    @Autowired
    private transient CourseRepository courseRepository;
    @Autowired
    private transient LectureRepository lectureRepository;
    @Autowired
    private transient Validate validate;

    /**
     * Plans a lecture with provided properties.
     *
     * @param addLecture the object containing the lecture details
     * @return an indication of whether the operation succeeded or not
     */
    @PostMapping(path = "/planNewLecture") // Map ONLY POST Requests
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    // Found 'DD'-anomaly for variable 'lectureId' (lines '96'-'98').
    // -> correct since we need to count
    public ResponseEntity<?> planNewLecture(HttpServletRequest request,
                                            @RequestBody AddLecture addLecture)
            throws JSONException, IOException, InterruptedException {

        String validation = validate.validateRole(request, Constants.teacherRole);
        if (validation.equals(Constants.noAccessMessage.getMessage())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Constants.noAccessMessage);
        }

        if (courseRepository.findByCourseId(addLecture.getCourseId()) != null) {
            Lecture lecture = new Lecture();
            lecture.setCourseId(addLecture.getCourseId());
            lecture.setDuration(addLecture.getDurationInMinutes());
            lecture.setScheduledDate(addLecture.getDate());
            lectureRepository.save(lecture);
            return ResponseEntity.ok(new StringMessage("Lecture planned."));
        } else {
            return ResponseEntity.ok(new StringMessage("The course with id "
                    + addLecture.getCourseId() + " does not exist."));
        }
    }
}
