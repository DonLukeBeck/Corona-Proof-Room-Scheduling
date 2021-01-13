package nl.tudelft.sem.calendar.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import nl.tudelft.sem.calendar.communication.CourseAdapter;
import nl.tudelft.sem.calendar.communication.RoomCommunicator;
import nl.tudelft.sem.calendar.entities.Attendance;
import nl.tudelft.sem.calendar.entities.Lecture;
import nl.tudelft.sem.calendar.exceptions.ServerErrorException;
import nl.tudelft.sem.calendar.repositories.AttendanceRepository;
import nl.tudelft.sem.calendar.repositories.LectureRepository;
import nl.tudelft.sem.calendar.util.Validate;
import nl.tudelft.sem.shared.Constants;
import nl.tudelft.sem.shared.entity.BareCourse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/calendar")
public class RetrievalController {
    @Autowired
    private transient RoomCommunicator roomCommunicator;

    @Autowired
    private transient AttendanceRepository attendanceRepository;

    @Autowired
    private transient LectureRepository lectureRepository;

    @Autowired
    private transient CourseAdapter courseAdapter;

    @Autowired
    private transient Validate validate;


    /**
     * Constructor used for testing to inject mockable dependencies.
     *
     * @param roomCommunicator the communicator to get room information
     * @param attendanceRepository the repository dealing with attendances
     * @param lectureRepository the repository dealing with lectures
     * @param courseAdapter the communicator for the course service
     * @param validate the validation unit
     */
    public RetrievalController(
            RoomCommunicator roomCommunicator, AttendanceRepository attendanceRepository,
            LectureRepository lectureRepository, CourseAdapter courseAdapter, Validate validate) {

        this.roomCommunicator = roomCommunicator;
        this.attendanceRepository = attendanceRepository;
        this.lectureRepository = lectureRepository;
        this.courseAdapter = courseAdapter;
        this.validate = validate;
    }

    /**
     * This method will return the personal schedule of a student.
     *
     * @param request an object containing authorization properties.
     *
     * @return returns a list with the lectures for a student if granted access.
     */
    @GetMapping(path = "/getMyPersonalScheduleStudent") // Map ONLY GET Requests
    @ResponseBody
    @SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.AvoidDuplicateLiterals"})
    // we need to add specific values to lectureIds
    // we need to suppress this warning for every method
    public ResponseEntity<?> getMyPersonalScheduleStudent(HttpServletRequest request)
            throws IOException, InterruptedException, ServerErrorException {

        String validation = validate.validateRole(request, Constants.studentRole);
        if (validation.equals(Constants.noAccessMessage.getMessage())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Constants.noAccessMessage);
        }

        List<Lecture> lectures = new ArrayList<>();
        Map<Integer, Boolean> lectureIdPhysical = createLectureIdPhysicalMap(
                validation);

        for (Map.Entry<Integer, Boolean> idPhysical : lectureIdPhysical.entrySet()) {
            Lecture l = lectureRepository.findByLectureId(idPhysical.getKey());
            l.setSelectedForOnCampus(idPhysical.getValue());
            l.setRoomName(roomCommunicator.getRoomName(l.getRoomId()));
            lectures.add(l);
        }
        return ResponseEntity.ok(lectures);
    }

    /**
     * This method will return the personal schedule of a teacher.
     *
     * @param request an object containing authorization properties.
     *
     * @return returns a list with the lectures for a teacher if granted access.
     */
    @GetMapping(path = "/getMyPersonalScheduleTeacher") // Map ONLY GET Requests
    @ResponseBody
    @SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.AvoidDuplicateLiterals"})
    // we need to add specific values to lectureIds
    // we need to suppress this warning for every method
    public ResponseEntity<?> getMyPersonalScheduleTeacher(HttpServletRequest request)
            throws IOException, InterruptedException, ServerErrorException {

        String validation = validate.validateRole(request, Constants.teacherRole);
        if (validation.equals(Constants.noAccessMessage.getMessage())) {
            return ResponseEntity.ok(Constants.noAccessMessage);
        }

        List<BareCourse> courseList =
                courseAdapter.coursesFromTeacher(validation);

        List<Lecture> lectureList = new ArrayList<>();
        for (BareCourse bareCourse : courseList) {
            List<Lecture> lectures = lectureRepository.findByCourseId(bareCourse.getCourseId());
            for (Lecture l : lectures) {
                l.setSelectedForOnCampus(true);
                l.setRoomName(roomCommunicator.getRoomName(l.getRoomId()));
                lectureList.add(l);
            }
        }
        return ResponseEntity.ok(lectureList);
    }

    /**
     * This method will return the personal schedule of a user for a given day.
     *
     * @param request an object containing authorization properties.
     * @param date the date for which the schedule must be given.
     *
     * @return returns a list with the lectures for a the user for a given day if granted access.
     */
    @GetMapping(path = "/getMyPersonalScheduleForDayStudent") // Map ONLY GET Requests
    @ResponseBody
    @SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.AvoidDuplicateLiterals"})
    // we need to add specific values to lectureIds
    // we need to suppress this warning for every method
    public ResponseEntity<?> getMyPersonalScheduleForDayStudent(
            HttpServletRequest request,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
            throws IOException, InterruptedException, ServerErrorException {

        String validation = validate.validateRole(request, Constants.studentRole);
        if (validation.equals(Constants.noAccessMessage.getMessage())) {
            return ResponseEntity.ok(Constants.noAccessMessage);
        }

        ArrayList<Lecture> lectures = new ArrayList<>();
        Map<Integer, Boolean> lectureIdPhysical = createLectureIdPhysicalMap(validation);

        for (Lecture l : lectureRepository.findByDate(date.plusDays(1))) {
            if (lectureIdPhysical.containsKey(l.getLectureId())) {
                l.setSelectedForOnCampus(lectureIdPhysical.get(l.getLectureId()));
                l.setRoomName(roomCommunicator.getRoomName(l.getRoomId()));
                lectures.add(l);
            }
        }
        return ResponseEntity.ok(lectures);
    }

    /**
     * This method will return the personal schedule of a user for a given day.
     *
     * @param request an object containing authorization properties.
     * @param date the date for which the schedule must be given.
     *
     * @return returns a list with the lectures for a the user for a given day if granted access.
     */
    @GetMapping(path = "/getMyPersonalScheduleForDayTeacher") // Map ONLY GET Requests
    @ResponseBody
    @SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.AvoidDuplicateLiterals"})
    // we need to add specific values to lectureIds
    // we need to suppress this warning for every method
    public ResponseEntity<?> getMyPersonalScheduleForDayTeacher(
            HttpServletRequest request,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
            throws IOException, InterruptedException, ServerErrorException {

        String validation = validate.validateRole(request, Constants.teacherRole);
        if (validation.equals(Constants.noAccessMessage.getMessage())) {
            return ResponseEntity.ok(Constants.noAccessMessage);
        }

        List<BareCourse> courseList =
                courseAdapter.coursesFromTeacher(validation);
        List<Lecture> lectureList = new ArrayList<>();

        for (BareCourse bareCourse : courseList) {
            List<Lecture> lectures =
                    lectureRepository.findByCourseIdAndDate(bareCourse.getCourseId(),
                            date.plusDays(1));
            for (Lecture l : lectures) {
                l.setSelectedForOnCampus(true);
                l.setRoomName(roomCommunicator.getRoomName(l.getRoomId()));
                lectureList.add(l);
            }
        }
        return ResponseEntity.ok(lectureList);
    }

    /**
     * This method will return the personal schedule of a user for a given course.
     *
     * @param request an object containing authorization properties.
     * @param courseId the course for which the schedule must be given.
     *
     * @return returns a list with the lectures for a the user for a given course if granted access.
     */
    @GetMapping(path = "/getMyPersonalScheduleForCourseStudent") // Map ONLY GET Requests
    @ResponseBody
    @SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.AvoidDuplicateLiterals"})
    // we need to add specific values to lectureIds
    // we need to suppress this warning for every method
    public ResponseEntity<?> getMyPersonalScheduleForCourseStudent(
            HttpServletRequest request, String courseId)
            throws IOException, InterruptedException, ServerErrorException {

        String validation = validate.validateRole(request, Constants.studentRole);
        if (validation.equals(Constants.noAccessMessage.getMessage())) {
            return ResponseEntity.ok(Constants.noAccessMessage);
        }

        ArrayList<Lecture> lectures = new ArrayList<>();
        Map<Integer, Boolean> lectureIdPhysical = createLectureIdPhysicalMap(validation);

        for (Lecture l : lectureRepository.findByCourseId(courseId)) {
            if (lectureIdPhysical.containsKey(l.getLectureId())) {
                l.setSelectedForOnCampus(lectureIdPhysical.get(l.getLectureId()));
                l.setRoomName(roomCommunicator.getRoomName(l.getRoomId()));
                lectures.add(l);
            }
        }
        return ResponseEntity.ok(lectures);
    }

    /**
     * This method will return the personal schedule of a user for a given course.
     *
     * @param request an object containing authorization properties.
     * @param courseId the course for which the schedule must be given.
     *
     * @return returns a list with the lectures for a the user for a given course if granted access.
     */
    @GetMapping(path = "/getMyPersonalScheduleForCourseTeacher") // Map ONLY GET Requests
    @ResponseBody
    @SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.AvoidDuplicateLiterals"})
    // we need to add specific values to lectureIds
    // we need to suppress this warning for every method
    public ResponseEntity<?> getMyPersonalScheduleForCourseTeacher(
            HttpServletRequest request, String courseId) {

        String validation = validate.validateRole(request, Constants.teacherRole);

        if (validation.equals(Constants.noAccessMessage.getMessage())) {
            return ResponseEntity.ok(Constants.noAccessMessage);
        }

        // Should we check if the teacher actually teaches the course?

        List<Lecture> result = lectureRepository.findByCourseId(courseId);
        return ResponseEntity.ok(result);
    }

    /**
     * Helper method to create a map in which the lectureId and a boolean
     * indicating physical presence are stored.
     *
     * @param userId the userId of the user to look for
     *
     * @return a map containing a lectureId and a boolean indicating physical presence
     */
    public Map<Integer, Boolean> createLectureIdPhysicalMap(String userId) {
        Map<Integer, Boolean> result = new HashMap<>();
        for (Attendance a : attendanceRepository.findByStudentId(userId)) {
            result.put(a.getLectureId(), a.getPhysical());
        }
        return result;
    }
}
