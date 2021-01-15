package nl.tudelft.sem.courses.controller;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.tudelft.sem.courses.entity.Lecture;
import nl.tudelft.sem.courses.repository.LectureRepository;
import nl.tudelft.sem.courses.util.Validate;
import nl.tudelft.sem.shared.Constants;
import nl.tudelft.sem.shared.entity.StringMessage;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Getter
@AllArgsConstructor
@RestController
@RequestMapping(path = "/lecture")
public class LectureController {

    @Autowired
    private transient LectureRepository lectureRepository;
    @Autowired
    private transient Validate validate;

    /**
     * Cancels a lecture with provided properties.
     *
     * @param courseId the Id of the associated course
     * @param date the date on which to cancel the lecture
     * @return an indication of whether the operation succeeded or not
     */
    @DeleteMapping(path = "/cancelLecture") // Map ONLY POST Requests
    public ResponseEntity<?> cancelLecture(HttpServletRequest request,
                                           @RequestParam String courseId,
                                           @RequestParam @DateTimeFormat(
            iso = DateTimeFormat.ISO.DATE) LocalDate date) throws JSONException, IOException,
            InterruptedException {

        String validation = validate.validateRole(request, Constants.teacherRole);
        if (validation.equals(Constants.noAccessMessage.getMessage())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Constants.noAccessMessage);
        }

        // We need to add one day since Spring of MariaDB or something matches against one day off
        Date sqlDate = Date.valueOf(date.plusDays(1));
        List<Lecture> lectures =
                lectureRepository.findByCourseIdAndScheduledDate(courseId, sqlDate);
        if (lectures.size() == 0) {
            return ResponseEntity.notFound().build();
        }
        for (Lecture l : lectures) {
            lectureRepository.delete(l);
        }
        return ResponseEntity.ok(new StringMessage("Lecture(s) cancelled."));
    }
}
