package nl.tudelft.sem.calendar.controllers;

import nl.tudelft.sem.calendar.communication.RestrictionManagementCommunicator;
import nl.tudelft.sem.calendar.entities.RequestedLecture;
import nl.tudelft.sem.calendar.entities.Room;
import nl.tudelft.sem.calendar.entities.ScheduledLecture;

import java.time.LocalTime;
import java.util.List;

//import nl.tudelft.sem.lecture_scheduling.repositories;
//@EnableJpaRepositories("nl.tudelft.sem.lecture_scheduling.repositories")


//@RestController
public class SchedulingController {

    //@PostMapping("schedule-lectures")
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
                endTime, timeGapLength, 0.50);
        List<ScheduledLecture> scheduledLectures = scheduler.scheduledAllLectures();
        return null;
    }
}