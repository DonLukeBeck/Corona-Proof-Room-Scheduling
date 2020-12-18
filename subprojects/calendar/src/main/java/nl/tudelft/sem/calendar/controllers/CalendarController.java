package nl.tudelft.sem.calendar.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import nl.tudelft.sem.calendar.communication.CourseManagementCommunicator;
import nl.tudelft.sem.calendar.communication.RestrictionManagementCommunicator;
import nl.tudelft.sem.calendar.communication.RoomManagementCommunicator;
import nl.tudelft.sem.calendar.entities.Attendance;
import nl.tudelft.sem.calendar.entities.Lecture;
import nl.tudelft.sem.calendar.entities.Room;
import nl.tudelft.sem.calendar.exceptions.ServerErrorException;
import nl.tudelft.sem.calendar.repositories.AttendanceRepository;
import nl.tudelft.sem.calendar.repositories.LectureRepository;
import nl.tudelft.sem.calendar.scheduling.LectureScheduler;
import nl.tudelft.sem.calendar.util.JwtValidate;
import nl.tudelft.sem.shared.entity.BareCourse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/calendar")

public class CalendarController {

    private transient String teacherRole = "teacher";
    private transient String studentRole = "student";
    private transient String noAccessMessage =
            "You are not allowed to view this page. Please contact administrator.";

    @Autowired
    private transient LectureScheduler lectureScheduler;

    @Autowired
    private transient AttendanceRepository attendanceRepository;

    @Autowired
    private transient LectureRepository lectureRepository;

    @Autowired
    private transient RestrictionManagementCommunicator restrictionManagementCommunicator;

    @Autowired
    private transient CourseManagementCommunicator courseManagementCommunicator;

    @Autowired
    private transient RoomManagementCommunicator roomManagementCommunicator;

    /**
     * This method will form the main API endpoint for the Scheduling functionality, once the
     * connection face with the other services is determined, it will be implemented to match up.
     *
     * @return a response nl.tudelft.sem.shared.entity indicating success or failure.
     */
    @PostMapping(path = "/scheduleLectures")
    @ResponseBody
    public ResponseEntity<?> scheduleLectures(HttpServletRequest request)
            throws InterruptedException, IOException, JSONException {

        String validation = validateRole(request, teacherRole);
        if (validation.equals(noAccessMessage)) {
            return ResponseEntity.ok(noAccessMessage);
        }

        try {
            // Make API call to retrieve the start time
            LocalTime startTime = LocalTime
                    .ofSecondOfDay(restrictionManagementCommunicator.getStartTime());
            // Make API call to retrieve the end time
            LocalTime endTime = LocalTime
                    .ofSecondOfDay(restrictionManagementCommunicator.getEndTime());
            // Make API call to retrieve time gap length
            int timeGapLength = restrictionManagementCommunicator.getTimeGapLength();
            // Make API call to retrieve rooms with restricted capacity
            List<Room> rooms =
                    restrictionManagementCommunicator.getAllRoomsWithAdjustedCapacity();
            // Get all the lectures to be scheduled
            List<Lecture> lecturesToSchedule = courseManagementCommunicator
                    .getToBeScheduledLectures(LocalDate.of(2020, 03, 27));
            // Create the scheduler that does the scheduling
            lectureScheduler.setFields(rooms, lecturesToSchedule, startTime,
                    endTime, timeGapLength);
            // Schedule the lecture
            lectureScheduler.scheduleAllLectures();


            return ResponseEntity.ok("Successfully scheduled lectures.");
        } catch (Exception e) {
            System.out.println(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error.");
        }
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
            throws IOException, InterruptedException, JSONException, ServerErrorException {

        String validation = validateRole(request, studentRole);
        if (validation.equals(noAccessMessage)) {
            return ResponseEntity.ok(noAccessMessage);
        }

        List<Lecture> lectures = new ArrayList<>();
        Map<Integer, Boolean> lectureIdPhysical = createLectureIdPhysicalMap(
                validation);

        for (Map.Entry<Integer, Boolean> idPhysical : lectureIdPhysical.entrySet()) {
            Lecture l = lectureRepository.findByLectureId(idPhysical.getKey());
            l.setSelectedForOnCampus(idPhysical.getValue());
            l.setRoomName(roomManagementCommunicator.getRoomName(l.getRoomId()));
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
            throws IOException, InterruptedException, ServerErrorException, JSONException {

        String validation = validateRole(request, teacherRole);
        if (validation.equals(noAccessMessage)) {
            return ResponseEntity.ok(noAccessMessage);
        }

        List<BareCourse> courseList =
                courseManagementCommunicator.coursesFromTeacher(validation);

        List<Lecture> lectureList = new ArrayList<>();
        for (BareCourse bareCourse : courseList) {
            List<Lecture> lectures = lectureRepository.findByCourseId(bareCourse.getCourseId());
            for (Lecture l : lectures) {
                l.setSelectedForOnCampus(true);
                l.setRoomName(roomManagementCommunicator.getRoomName(l.getRoomId()));
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
            throws IOException, InterruptedException, JSONException, ServerErrorException {

        String validation = validateRole(request, studentRole);
        if (validation.equals(noAccessMessage)) {
            return ResponseEntity.ok(noAccessMessage);
        }

        ArrayList<Lecture> lectures = new ArrayList<>();
        Map<Integer, Boolean> lectureIdPhysical = createLectureIdPhysicalMap(validation);

        for (Lecture l : lectureRepository.findByDate(date.plusDays(1))) {
            if (lectureIdPhysical.containsKey(l.getLectureId())) {
                l.setSelectedForOnCampus(lectureIdPhysical.get(l.getLectureId()));
                l.setRoomName(roomManagementCommunicator.getRoomName(l.getRoomId()));
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
            throws IOException, InterruptedException, ServerErrorException, JSONException {

        String validation = validateRole(request, teacherRole);
        if (validation.equals(noAccessMessage)) {
            return ResponseEntity.ok(noAccessMessage);
        }

        List<BareCourse> courseList =
               courseManagementCommunicator.coursesFromTeacher(validation);
        List<Lecture> lectureList = new ArrayList<>();

        for (BareCourse bareCourse : courseList) {
            List<Lecture> lectures =
                    lectureRepository.findByCourseIdAndDate(bareCourse.getCourseId(),
                            date.plusDays(1));
            for (Lecture l : lectures) {
                l.setSelectedForOnCampus(true);
                l.setRoomName(roomManagementCommunicator.getRoomName(l.getRoomId()));
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
            throws IOException, InterruptedException, JSONException, ServerErrorException {

        String validation = validateRole(request, studentRole);
        if (validation.equals(noAccessMessage)) {
            return ResponseEntity.ok(noAccessMessage);
        }

        ArrayList<Lecture> lectures = new ArrayList<>();
        Map<Integer, Boolean> lectureIdPhysical = createLectureIdPhysicalMap(validation);

        for (Lecture l : lectureRepository.findByCourseId(courseId)) {
            if (lectureIdPhysical.containsKey(l.getLectureId())) {
                l.setSelectedForOnCampus(lectureIdPhysical.get(l.getLectureId()));
                l.setRoomName(roomManagementCommunicator.getRoomName(l.getRoomId()));
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
            HttpServletRequest request, String courseId)
            throws IOException, InterruptedException, JSONException {

        String validation = validateRole(request, teacherRole);

        if (validation.equals(noAccessMessage)) {
            return ResponseEntity.ok(noAccessMessage);
        }

        // Should we check if the teacher actually teaches the course?

        List<Lecture> result = lectureRepository.findByCourseId(courseId);
        return ResponseEntity.ok(result);
    }

    /**
     * This method is used by a user to indicate that
     * the user will be absent during a fysical lecture.
     *
     * @param userId the userId of the user.
     * @param courseId the course for which the user will be absent.
     * @param date the date on which the lecture would have took place
     *
     * @return a string with 'success' if done.
     */
    @PostMapping(path = "/indicateAbsence")
    @ResponseBody
    @SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.AvoidDuplicateLiterals"})
    // we need to add specific values to lectureIds
    // we need to suppress this warning for every method
    public ResponseEntity<?> indicateAbsence(HttpServletRequest request,
                                             String userId, String courseId,
                                             @DateTimeFormat(iso = DateTimeFormat
                                             .ISO.DATE) LocalDate date)
            throws IOException, InterruptedException, JSONException {

        if (!validateRole(request, studentRole).equals(userId)) {
            return ResponseEntity.ok(noAccessMessage);
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
            return ResponseEntity.ok("Indicated absence.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not indicated absence.");
        }
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
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
            throws InterruptedException, IOException, JSONException {

        String validation = validateRole(request, teacherRole);

        if (validation.equals(noAccessMessage)) {
            return ResponseEntity.ok(noAccessMessage);
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

    /**
     * Helper method to validate the role of a user.
     *
     * @param request the request containing jwt token information
     * @param role the desired role
     *
     * @return an error message if the user hasn't got the desired role, else its netId.
     */
    public String validateRole(HttpServletRequest request, String role)
            throws JSONException, IOException, InterruptedException {

        JSONObject jwtInfo = JwtValidate.jwtValidate(request);
        try {
            if (!jwtInfo.getString("role").equals(role)) {
                return noAccessMessage;
            }
        } catch (Exception e) {
            return noAccessMessage;
        }
        return jwtInfo.getString("netid");
    }
}