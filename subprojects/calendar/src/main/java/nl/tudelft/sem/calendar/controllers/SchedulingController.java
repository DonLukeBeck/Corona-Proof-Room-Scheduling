package nl.tudelft.sem.calendar.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem.calendar.communication.RestrictionManagementCommunicator;
import nl.tudelft.sem.calendar.communication.CourseManagementCommunicator;
import nl.tudelft.sem.calendar.entities.Attendance;
import nl.tudelft.sem.calendar.entities.Lecture;
import nl.tudelft.sem.calendar.entities.Room;
import nl.tudelft.sem.calendar.repositories.AttendanceRepository;
import nl.tudelft.sem.calendar.repositories.LectureRepository;
import nl.tudelft.sem.calendar.scheduling.LectureScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SchedulingController {

    @Autowired
    transient LectureScheduler lectureScheduler;

    @Autowired
    private transient AttendanceRepository attendanceRepository;

    @Autowired
    private transient LectureRepository lectureRepository;

    public SchedulingController(AttendanceRepository attendanceRepository,
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
    public String schedulePlannedLectures() {

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
            List<Lecture> lecturesToSchedule = CourseManagementCommunicator.getToBeScheduledLectures();
            // Create the scheduler that does the scheduling
            lectureScheduler.setFields(rooms, lecturesToSchedule, startTime,
                    endTime, timeGapLength);
            // Schedule the lecture
            lectureScheduler.scheduleAllLectures();

            return "Success!";
        } catch (Exception e) {
            return "Failure!";
        }
    }

    /**
     * This method will return the personal schedule of a user.
     *
     * @param userId the userId of the user.
     *
     * @return returns a list with the lectures for a the user
     */
    @GetMapping(path = "/getMyPersonalSchedule") // Map ONLY GET Requests
    @SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.AvoidDuplicateLiterals"})
    // we need to add specific values to lectureIds
    // we need to suppress this warning for every method
    public List getMyPersonalSchedule(String userId) {
        List<Lecture> lectures = new ArrayList<>();
        ArrayList<Integer> lectureIds = new ArrayList<>();
        for (Attendance a : attendanceRepository.findByStudentId(userId)) {
            lectureIds.add(a.getLectureId());
        }
        for (Integer i : lectureIds) {
            lectures.addAll(lectureRepository.findByLectureId(i));
        }
        return lectures;
    }

    /**
     * This method will return the personal schedule of a user for a given day.
     *
     * @param userId the userId of the user.
     * @param date the date for which the schedule must be given.
     *
     * @return returns a list with the lectures for a the user for a given day
     */
    @GetMapping(path = "/getMyPersonalScheduleForDay") // Map ONLY GET Requests
    @SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.AvoidDuplicateLiterals"})
    // we need to add specific values to lectureIds
    // we need to suppress this warning for every method
    public List getMyPersonalScheduleForDay(String userId, LocalDate date) {
        ArrayList<Lecture> lectures = new ArrayList<>();
        ArrayList<Integer> lectureIds = new ArrayList<>();
        for (Attendance a : attendanceRepository.findByStudentId(userId)) {
            lectureIds.add(a.getLectureId());
        }

        for (Lecture l : lectureRepository.findByDate(date)) {
            if (lectureIds.contains(l.getLectureId())) {
                lectures.add(l);
            }
        }
        return lectures;
    }

    /**
     * This method will return the personal schedule of a user for a given course.
     *
     * @param userId the userId of the user.
     * @param courseId the course for which the schedule must be given.
     *
     * @return returns a list with the lectures for a the user for a given course
     */
    @GetMapping(path = "/getMyPersonalScheduleForCourse") // Map ONLY GET Requests
    @SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.AvoidDuplicateLiterals"})
    // we need to add specific values to lectureIds
    // we need to suppress this warning for every method
    public List getMyPersonalScheduleForCourse(String userId, String courseId) {
        ArrayList<Lecture> lectures = new ArrayList<>();
        ArrayList<Integer> lectureIds = new ArrayList<>();
        for (Attendance a : attendanceRepository.findByStudentId(userId)) {
            lectureIds.add(a.getLectureId());
        }

        for (Lecture l : lectureRepository.findByCourseId(courseId)) {
            if (lectureIds.contains(l.getLectureId())) {
                lectures.add(l);
            }
        }
        return lectures;
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
    @SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.AvoidDuplicateLiterals"})
    // we need to add specific values to lectureIds
    // we need to suppress this warning for every method
    public String indicateAbsence(String userId, String courseId, LocalDate date) {
        int lectureId = 0;
        for (Lecture l : lectureRepository.findByDate(date)) {
            if (l.getCourseId().equals(courseId)) {
                lectureId = l.getLectureId();
            }
        }

        for (Attendance a : attendanceRepository.findByLectureIdAndStudentId(lectureId, userId)) {
            attendanceRepository.delete(a);
            a.setPhysical(false);
            attendanceRepository.save(a);
        }

        return "Succes";
    }

}