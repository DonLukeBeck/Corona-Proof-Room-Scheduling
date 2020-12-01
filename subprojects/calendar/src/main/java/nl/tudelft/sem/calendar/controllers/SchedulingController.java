package nl.tudelft.sem.calendar.controllers;

import java.time.LocalTime;
import java.util.List;
import nl.tudelft.sem.calendar.communication.RestrictionManagementCommunicator;
import nl.tudelft.sem.calendar.entities.Lecture;
import nl.tudelft.sem.calendar.entities.Room;
import nl.tudelft.sem.calendar.scheduling.LectureScheduler;


public class SchedulingController {

    /**
     * This method will form the main API endpoint for the Scheduling functionality, once the
     * connection face with the other services is determined, it will be implemented to match up.
     *
     * @param lecturesToSchedule the list of lectures to be scheduled
     * @return a string indicating success or failure
     */
    public String schedulePlannedLectures(List<Lecture> lecturesToSchedule) {

        // Make API call to retrieve the start time
        LocalTime startTime = RestrictionManagementCommunicator.getStartTime();
        // Make API call to retrieve the end time
        LocalTime endTime = RestrictionManagementCommunicator.getEndTime();
        // Make API call to retrieve time gap length
        int timeGapLength = RestrictionManagementCommunicator.getTimeGapLength();
        // Make API call to retrieve rooms with restricted capacity
        List<Room> rooms = RestrictionManagementCommunicator.getAllRoomsWithAdjustedCapacity();
        // Create the scheduler that does the scheduling
        LectureScheduler scheduler = new LectureScheduler(rooms, lecturesToSchedule, startTime,
                endTime, timeGapLength);
        // Schedule the lecture
        scheduler.scheduleAllLectures();
        return null;
    }
}