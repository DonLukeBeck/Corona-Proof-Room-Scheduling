package nl.tudelft.sem.calendar.controllers;

import java.time.LocalTime;
import java.util.List;
import nl.tudelft.sem.calendar.communication.RestrictionManagementCommunicator;
import nl.tudelft.sem.calendar.entities.Lecture;
import nl.tudelft.sem.calendar.entities.Room;
import nl.tudelft.sem.calendar.scheduling.LectureScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SchedulingController {
    @Autowired
    transient LectureScheduler lectureScheduler;
    /**
     * This method will form the main API endpoint for the Scheduling functionality, once the
     * connection face with the other services is determined, it will be implemented to match up.
     *
     * @param lecturesToSchedule the list of lectures to be scheduled
     * @return a string indicating success or failure
     */
    @PostMapping(path = "/scheduleLectures")
    public String schedulePlannedLectures(List<Lecture> lecturesToSchedule) {

        try {
            // Make API call to retrieve the start time
            LocalTime startTime = RestrictionManagementCommunicator.getStartTime();
            // Make API call to retrieve the end time
            LocalTime endTime = RestrictionManagementCommunicator.getEndTime();
            // Make API call to retrieve time gap length
            int timeGapLength = RestrictionManagementCommunicator.getTimeGapLength();
            // Make API call to retrieve rooms with restricted capacity
            List<Room> rooms = RestrictionManagementCommunicator.getAllRoomsWithAdjustedCapacity();
            // Create the scheduler that does the scheduling
            lectureScheduler.setFields(rooms, lecturesToSchedule, startTime,
                    endTime, timeGapLength);
            // Schedule the lecture
            lectureScheduler.scheduleAllLectures();

            return "Success!";
        }
        catch (Exception e){
            return "Failure!";
        }
    }

}