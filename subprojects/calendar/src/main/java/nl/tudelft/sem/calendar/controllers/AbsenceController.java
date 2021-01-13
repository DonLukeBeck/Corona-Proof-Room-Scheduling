package nl.tudelft.sem.calendar.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import nl.tudelft.sem.calendar.entities.Attendance;
import nl.tudelft.sem.calendar.entities.Lecture;
import nl.tudelft.sem.calendar.repositories.AttendanceRepository;
import nl.tudelft.sem.calendar.repositories.LectureRepository;
import nl.tudelft.sem.calendar.util.Validate;
import nl.tudelft.sem.shared.Constants;
import nl.tudelft.sem.shared.entity.StringMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/calendar")
public class AbsenceController {
    @Autowired
    private transient AttendanceRepository attendanceRepository;

    @Autowired
    private transient LectureRepository lectureRepository;

    @Autowired
    private transient Validate validate;


    /**
     * Constructor used for testing to inject mockable dependencies.
     *
     * @param attendanceRepository the attendance repository
     * @param lectureRepository the lecture repository
     * @param validate the validation unit for role validation
     */
    public AbsenceController(
            AttendanceRepository attendanceRepository, LectureRepository lectureRepository,
            Validate validate) {
        this.attendanceRepository = attendanceRepository;
        this.lectureRepository = lectureRepository;
        this.validate = validate;
    }

    /**
     * This method is used by a user to indicate that
     * the user will be absent during a fysical lecture.
     *
     * @param context the {@link AbsenceContext} of the request
     *
     * @return a string with 'success' if done.
     */
    @PostMapping(path = "/indicateAbsence")
    @ResponseBody
    @SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.AvoidDuplicateLiterals"})
    // we need to add specific values to lectureIds
    // we need to suppress this warning for every method
    public ResponseEntity<?> indicateAbsence(HttpServletRequest request,
                                             @RequestBody AbsenceContext context) {
        String userId = context.getUserId();
        String courseId = context.getCourseId();
        LocalDate date = context.getDate();

        if (!validate.validateRole(request, Constants.studentRole).equals(userId)) {
            return ResponseEntity.ok(Constants.noAccessMessage);
        }
        try {
            List<Lecture> list = lectureRepository
                    .findByDateAndCourseId(date.plusDays(1), courseId);

            for (Lecture l : list) {
                int lectureId = l.getRoomId();
                for (Attendance a :
                        attendanceRepository.findByLectureIdAndStudentId(lectureId, userId)) {
                    attendanceRepository.delete(a);
                    a.setPhysical(false);
                    attendanceRepository.save(a);
                }
            }
            return ResponseEntity.ok(new StringMessage("Indicated absence."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new StringMessage("Could not indicated absence."));
        }
    }

    @Data
    @AllArgsConstructor
    public static class AbsenceContext {
        String userId;
        String courseId;
        LocalDate date;
    }

    /**
     * This method is used by a lecture to retrieve a list of netIds belonging to students that
     * were selected to attend the lecture on-campus.
     *
     * @param request an object containing authorization properties.
     * @param courseId the course for which the user will be absent.
     * @param date the date on which the lecture would have took place
     *
     * @return a list of netIds of selected students
     */
    @GetMapping(path = "/getPhysicalAttendantsForLecture")
    @ResponseBody
    public ResponseEntity<?> getPhysicalAttendantsForLecture(
            HttpServletRequest request, String courseId,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        String validation = validate.validateRole(request, Constants.teacherRole);

        if (validation.equals(Constants.noAccessMessage.getMessage())) {
            return ResponseEntity.ok(Constants.noAccessMessage);
        }

        List<Lecture> list = lectureRepository.findByDateAndCourseId(date.plusDays(1),
                courseId);
        List<String> netIds = new ArrayList<>();

        for (Lecture l : list) {
            int lectureId = l.getRoomId();
            List<String> temp = attendanceRepository
                    .findByLectureId(lectureId).stream()
                    .filter(Attendance::getPhysical)
                    .map(Attendance::getStudentId)
                    .collect(Collectors.toList());
            netIds.addAll(temp);
        }
        return ResponseEntity.ok(netIds);
    }
}
