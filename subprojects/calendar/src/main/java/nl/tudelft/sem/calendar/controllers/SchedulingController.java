package nl.tudelft.sem.calendar.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import nl.tudelft.sem.calendar.communication.CourseAdapter;
import nl.tudelft.sem.calendar.communication.RestrictionCommunicator;
import nl.tudelft.sem.calendar.entities.Lecture;
import nl.tudelft.sem.calendar.entities.Room;
import nl.tudelft.sem.calendar.scheduling.LectureScheduler;
import nl.tudelft.sem.calendar.util.Validate;
import nl.tudelft.sem.shared.Constants;
import nl.tudelft.sem.shared.entity.StringMessage;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/calendar")
public class SchedulingController {

    @Autowired
    private transient LectureScheduler lectureScheduler;

    @Autowired
    private transient RestrictionCommunicator restrictionCommunicator;

    @Autowired
    private transient CourseAdapter courseAdapter;

    @Autowired
    private transient Validate validate;

    /**
     * Constructor used for testing to inject mockable dependencies.
     *
     * @param lectureScheduler the lecture scheduler
     * @param restrictionCommunicator the communicator for the restrictions service
     * @param courseAdapter the communicator for the course service
     */
    public SchedulingController(
        LectureScheduler lectureScheduler, RestrictionCommunicator restrictionCommunicator,
        CourseAdapter courseAdapter, Validate validate) {

        this.lectureScheduler = lectureScheduler;
        this.restrictionCommunicator = restrictionCommunicator;
        this.courseAdapter = courseAdapter;
        this.validate = validate;
    }

    /**
     * This method will form the main API endpoint for the Scheduling functionality, once the
     * connection face with the other services is determined, it will be implemented to match up.
     *
     * @return a response entity indicating success or failure.
     */
    @PostMapping(path = "/scheduleLectures")
    @ResponseBody
    public ResponseEntity<?> scheduleLectures(HttpServletRequest request)
            throws JSONException {

        String validation = validate.validateRole(request, Constants.teacherRole);
        if (validation.equals(Constants.noAccessMessage.getMessage())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Constants.noAccessMessage);
        }

        try {
            // Make API call to retrieve the start time
            LocalTime startTime = LocalTime
                    .ofSecondOfDay(restrictionCommunicator.getStartTime());

            // Make API call to retrieve the end time
            LocalTime endTime = LocalTime
                    .ofSecondOfDay(restrictionCommunicator.getEndTime());
            // Make API call to retrieve time gap length
            int timeGapLength = restrictionCommunicator.getTimeGapLength();
            // Make API call to retrieve rooms with restricted capacity
            List<Room> rooms =
                    restrictionCommunicator.getAllRoomsWithAdjustedCapacity();
            // Get all the lectures to be scheduled
            List<Lecture> lecturesToSchedule = courseAdapter
                    .getToBeScheduledLectures(LocalDate.now());
            // Create the scheduler that does the scheduling
            lectureScheduler.setFields(rooms, lecturesToSchedule, startTime,
                    endTime, timeGapLength);
            // Schedule the lecture
            lectureScheduler.scheduleAllLectures();

            return ResponseEntity.ok(new StringMessage("Successfully scheduled lectures."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new StringMessage("Internal server error."));
        }
    }
}