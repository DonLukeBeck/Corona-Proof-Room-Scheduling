package nl.tudelft.sem.calendar.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import nl.tudelft.sem.calendar.communication.CourseManagementCommunicator;
import nl.tudelft.sem.calendar.communication.RestrictionManagementCommunicator;
import nl.tudelft.sem.calendar.entities.*;
import nl.tudelft.sem.calendar.exceptions.ServerErrorException;
import nl.tudelft.sem.calendar.repositories.AttendanceRepository;
import nl.tudelft.sem.calendar.repositories.LectureRepository;
import nl.tudelft.sem.calendar.scheduling.LectureScheduler;
import nl.tudelft.sem.calendar.util.JwtValidate;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    transient LectureScheduler lectureScheduler;

    @Autowired
    private transient AttendanceRepository attendanceRepository;

    @Autowired
    private transient LectureRepository lectureRepository;

    public CalendarController(AttendanceRepository attendanceRepository,
                              LectureRepository lectureRepository) {
        this.attendanceRepository = attendanceRepository;
        this.lectureRepository = lectureRepository;
    }

    /**
     * This method will form the main API endpoint for the Scheduling functionality, once the
     * connection face with the other services is determined, it will be implemented to match up.
     *
     * @return a string indicating success or failure
     */
    @PostMapping(path = "/scheduleLectures")
    @ResponseBody
    public ResponseEntity<?> schedulePlannedLectures() {

        try {
            // Make API call to retrieve the start time
            LocalTime startTime = RestrictionManagementCommunicator.getStartTime();
            // Make API call to retrieve the end time
            LocalTime endTime = RestrictionManagementCommunicator.getEndTime();
            // Make API call to retrieve time gap length
            int timeGapLength = RestrictionManagementCommunicator.getTimeGapLength();
            // Make API call to retrieve rooms with restricted capacity
            List<Room> rooms = RestrictionManagementCommunicator.getAllRoomsWithAdjustedCapacity();
            // Get all the lectures to be scheduled
            List<Lecture> lecturesToSchedule = CourseManagementCommunicator
                    .getToBeScheduledLecturesTest();
            // Create the scheduler that does the scheduling
            lectureScheduler.setFields(rooms, lecturesToSchedule, startTime,
                    endTime, timeGapLength);
            // Schedule the lecture
            lectureScheduler.scheduleAllLectures();

            return ResponseEntity.ok(HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error.");
        }
    }

    /**
     * This method will return the personal schedule of a student.
     *
     * @param studentId the userId of the student.
     *
     * @return returns a list with the lectures for a student
     */
    @GetMapping(path = "/getMyPersonalScheduleStudent") // Map ONLY GET Requests
    @ResponseBody
    @SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.AvoidDuplicateLiterals"})
    // we need to add specific values to lectureIds
    // we need to suppress this warning for every method
    public Object getMyPersonalScheduleStudent(HttpServletRequest request, String studentId) throws IOException, InterruptedException {
/*
        String role = RoleValidation.getRole(request);
        try {
            if (!role.equals("student")) {
                return "You are not allowed to view this page. Please contact administrator.";
            }
        } catch (Exception e) {
            return "You are not allowed to view this page. Please contact administrator.";
        }
*/
        List<Lecture> lectures = new ArrayList<>();
        Map<Integer, Boolean> lectureIdPhysical = createLectureIdPhysicalMap(studentId);

        for (Map.Entry<Integer, Boolean> idPhysical : lectureIdPhysical.entrySet()) {
            Lecture l = lectureRepository.findByLectureId(idPhysical.getKey());
            l.setSelectedForOnCampus(idPhysical.getValue());
            lectures.add(l);
        }
        return ResponseEntity.ok(lectures);
    }

    /**
     * This method will return the personal schedule of a teacher.
     *
     * @return returns a list with the lectures for a teacher
     */
    @GetMapping(path = "/getMyPersonalScheduleTeacher") // Map ONLY GET Requests
    @ResponseBody
    @SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.AvoidDuplicateLiterals"})
    // we need to add specific values to lectureIds
    // we need to suppress this warning for every method
    public ResponseEntity<?> getMyPersonalScheduleTeacher(HttpServletRequest request) throws IOException, InterruptedException, ServerErrorException {

        JSONObject jwtInfo = JwtValidate.jwtValidate(request);
        try {
            if (!jwtInfo.getString("role").equals("teacher")) {
                ResponseEntity.ok("You are not allowed to view this page. Please contact administrator.");
            }
        } catch (Exception e) {
            ResponseEntity.ok("You are not allowed to view this page. Please contact administrator.");
        }

        List<BareCourse> courseList = CourseManagementCommunicator.coursesFromTeacher(jwtInfo.getString("netid"));
        List<Lecture> lectureList = new ArrayList<Lecture>();
        for(BareCourse bareCourse : courseList) {
            List<Lecture> lectures = lectureRepository.findByCourseId(bareCourse.getCourseId());
            lectureList.addAll(lectures);
        }
        return ResponseEntity.ok(lectureList);
    }

    /**
     * This method will return the personal schedule of a user for a given day.
     *
     * @param studentId the userId of the user.
     * @param date the date for which the schedule must be given.
     *
     * @return returns a list with the lectures for a the user for a given day
     */
    @GetMapping(path = "/getMyPersonalScheduleForDayStudent") // Map ONLY GET Requests
    @ResponseBody
    @SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.AvoidDuplicateLiterals"})
    // we need to add specific values to lectureIds
    // we need to suppress this warning for every method
    public Object getMyPersonalScheduleForDayStudent(HttpServletRequest request, String studentId, LocalDate date) throws IOException, InterruptedException {
        /*
        String role = RoleValidation.getRole(request);
        try {
            if (!role.equals("student")) {
                return "You are not allowed to view this page. Please contact administrator.";
            }
        } catch (Exception e) {
            return "You are not allowed to view this page. Please contact administrator.";
        }
*/
        ArrayList<Lecture> lectures = new ArrayList<>();
        Map<Integer, Boolean> lectureIdPhysical = createLectureIdPhysicalMap(studentId);

        for (Lecture l : lectureRepository.findByDate(date)) {
            if (lectureIdPhysical.containsKey(l.getLectureId())) {
                l.setSelectedForOnCampus(lectureIdPhysical.get(l.getLectureId()));
                lectures.add(l);
            }
        }
        return ResponseEntity.ok(lectures);
    }

    /**
     * This method will return the personal schedule of a user for a given day.
     *
     * @param teacherId the userId of the user.
     * @param date the date for which the schedule must be given.
     *
     * @return returns a list with the lectures for a the user for a given day
     */
    @GetMapping(path = "/getMyPersonalScheduleForDayTeacher") // Map ONLY GET Requests
    @ResponseBody
    @SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.AvoidDuplicateLiterals"})
    // we need to add specific values to lectureIds
    // we need to suppress this warning for every method
    public Object getMyPersonalScheduleForDayTeacher(HttpServletRequest request, String teacherId, LocalDate date) throws IOException, InterruptedException, ServerErrorException {
      /*  String role = RoleValidation.getRole(request);
        try {
            if (!role.equals("teacher")) {
                return "You are not allowed to view this page. Please contact administrator.";
            }
        } catch (Exception e) {
            return "You are not allowed to view this page. Please contact administrator.";
        }
*/
       // String courseId = CourseManagementCommunicator.coursesFromTeacher(teacherId);
        //List<Lecture> result = lectureRepository.findByCourseId(courseId);
      //  for (Lecture i : result) {
      //      if (i.getDate() != date) {
     //           result.remove(i);
     //       }
     //   }
        return ResponseEntity.ok(request);
    }

    /**
     * This method will return the personal schedule of a user for a given course.
     *
     * @param userId the userId of the user.
     * @param courseId the course for which the schedule must be given.
     *
     * @return returns a list with the lectures for a the user for a given course
     */
    @GetMapping(path = "/getMyPersonalScheduleForCourseStudent") // Map ONLY GET Requests
    @ResponseBody
    @SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.AvoidDuplicateLiterals"})
    // we need to add specific values to lectureIds
    // we need to suppress this warning for every method
    public Object getMyPersonalScheduleForCourseStudent(HttpServletRequest request,String userId, String courseId) throws IOException, InterruptedException {
       /* String role = RoleValidation.getRole(request);
        try {
            if (!role.equals("student")) {
                return "You are not allowed to view this page. Please contact administrator.";
            }
        } catch (Exception e) {
            return "You are not allowed to view this page. Please contact administrator.";
        }
*/
        ArrayList<Lecture> lectures = new ArrayList<>();
        Map<Integer, Boolean> lectureIdPhysical = createLectureIdPhysicalMap(userId);

        for (Lecture l : lectureRepository.findByCourseId(courseId)) {
            if (lectureIdPhysical.containsKey(l.getLectureId())) {
                l.setSelectedForOnCampus(lectureIdPhysical.get(l.getLectureId()));
                lectures.add(l);
            }
        }
        return ResponseEntity.ok(lectures);
    }

    /**
     * This method will return the personal schedule of a user for a given course.
     *
     * @param teacherId the userId of the user.
     * @param courseId the course for which the schedule must be given.
     *
     * @return returns a list with the lectures for a the user for a given course
     */
    @GetMapping(path = "/getMyPersonalScheduleForCourseTeacher") // Map ONLY GET Requests
    @ResponseBody
    @SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.AvoidDuplicateLiterals"})
    // we need to add specific values to lectureIds
    // we need to suppress this warning for every method
    public Object getMyPersonalScheduleForCourseTeacher(HttpServletRequest request, String teacherId, String courseId) throws IOException, InterruptedException, ServerErrorException {
       /* String role = RoleValidation.getRole(request);
        try {
            if (!role.equals("teacher")) {
                return "You are not allowed to view this page. Please contact administrator.";
            }
        } catch (Exception e) {
            return "You are not allowed to view this page. Please contact administrator.";
        }*/
        List<Lecture> result = lectureRepository.findByCourseId(courseId);
        for (Lecture i : result) {
            if (i.getCourseId() != courseId) {
                result.remove(i);
            }
        }
        return ResponseEntity.ok(result);
    }

    /**
     * Helper method to create a map in which the lectureId and a boolean
     * indicating physical presence are stored.
     *
     * @param userId - the userId of the user to look for
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
     * This method is used by a user to indicate that
     * the user will be absent during a fysical lecture.
     *
     * @param userId the userId of the user.
     * @param courseId the course for which the user will be absent.
     * @param date the date on which the lecture would have took place
     *
     * @return a string with 'success' if done
     */
    @PostMapping(path = "/indicateAbsence")
    @ResponseBody
    @SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.AvoidDuplicateLiterals"})
    // we need to add specific values to lectureIds
    // we need to suppress this warning for every method
    public ResponseEntity<?> indicateAbsence(String userId, String courseId, LocalDate date) {
        try {
            int lectureId = 0;
            for (Lecture l : lectureRepository.findByDate(date)) {
                if (l.getCourseId().equals(courseId)) {
                    lectureId = l.getLectureId();
                }
            }

            for (Attendance a :
                    attendanceRepository.findByLectureIdAndStudentId(lectureId, userId)) {
                attendanceRepository.delete(a);
                a.setPhysical(false);
                attendanceRepository.save(a);
            }
            return ResponseEntity.ok(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error.");
        }
    }
}