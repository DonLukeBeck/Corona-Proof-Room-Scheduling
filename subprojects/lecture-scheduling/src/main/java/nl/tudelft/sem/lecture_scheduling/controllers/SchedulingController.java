package nl.tudelft.sem.lecture_scheduling.controllers;
import nl.tudelft.sem.lecture_scheduling.communication.RestrictionManagementCommunicator;
import nl.tudelft.sem.lecture_scheduling.communication.CalendarCommunicator;
import nl.tudelft.sem.lecture_scheduling.entities.OnCampusCandidate;
import nl.tudelft.sem.lecture_scheduling.entities.RequestedLecture;
import nl.tudelft.sem.lecture_scheduling.entities.Room;
import nl.tudelft.sem.lecture_scheduling.entities.ScheduledLecture;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import nl.tudelft.sem.lecture_scheduling.repositories;
//@EnableJpaRepositories("nl.tudelft.sem.lecture_scheduling.repositories")
import java.util.*;

import static java.util.stream.Collectors.groupingBy;


//@RestController
public class SchedulingController {

    //@PostMapping("schedule-lectures")
    public String  schedulePlannedLectures(List<RequestedLecture> lecturesToSchedule) {

        LocalTime startTime = RestrictionManagementCommunicator.getStartTime(); // Make API call to retrieve the start time
        LocalTime endTime = RestrictionManagementCommunicator.getEndTime(); // Make API call to retrieve the end time
        int timeGapLength = RestrictionManagementCommunicator.getTimeGapLength(); // Make API call to retrieve time gap length
        List<Room> rooms = RestrictionManagementCommunicator.getAllRoomsWithAdjustedCapacity(); // Make API call to retrieve rooms with restricted capacity

        LectureScheduler scheduler = new LectureScheduler(rooms, lecturesToSchedule, startTime, endTime, timeGapLength, 0.50);
        List<ScheduledLecture> scheduledLectures = scheduler.scheduledAllLectures();

        if(CalendarCommunicator.sendSchedulerLecturesToCalendar(scheduledLectures)){
            //success;
        }
        else{
            //something went wrong with passing the lectures to the calendar.
        }
        return null;
    }
}