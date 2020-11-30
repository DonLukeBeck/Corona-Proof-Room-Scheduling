package nl.tudelft.sem.calendar.controllers;

import java.time.LocalTime;
import java.util.List;
import nl.tudelft.sem.calendar.communication.RestrictionManagementCommunicator;
import nl.tudelft.sem.calendar.entities.RequestedLecture;
import nl.tudelft.sem.calendar.entities.Room;

public class SchedulingController {

    /**
     * This method will form the main API endpoint for the Scheduling functionality, once the
     * connection face with the other services is determined, it will be implemented to match up.
     *
     * @param lecturesToSchedule the list of lectures to be scheduled
     * @return a string indicating success or failure
     */
    public String schedulePlannedLectures(List<RequestedLecture> lecturesToSchedule) {

        LocalTime startTime = RestrictionManagementCommunicator.getStartTime(); // Make API call
        // to retrieve the start time
        LocalTime endTime = RestrictionManagementCommunicator.getEndTime(); // Make API call to
        // retrieve the end time
        int timeGapLength = RestrictionManagementCommunicator.getTimeGapLength(); // Make API
        // call to retrieve time gap length
        List<Room> rooms = RestrictionManagementCommunicator.getAllRoomsWithAdjustedCapacity();
        // Make API call to retrieve rooms with restricted capacity

        LectureScheduler scheduler = new LectureScheduler(rooms, lecturesToSchedule, startTime,
                endTime, timeGapLength); // Create the scheduler that does the scheduling

        scheduler.scheduleAllLectures();
        return null;
    }
}