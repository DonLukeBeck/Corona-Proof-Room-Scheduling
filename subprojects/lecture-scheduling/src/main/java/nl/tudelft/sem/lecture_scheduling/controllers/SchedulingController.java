package nl.tudelft.sem.lecture_scheduling.controllers;
import nl.tudelft.sem.lecture_scheduling.communication.RestrictionManagementCommunicator;
import nl.tudelft.sem.lecture_scheduling.entities.Lecture;
import nl.tudelft.sem.lecture_scheduling.entities.Room;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import nl.tudelft.sem.lecture_scheduling.repositories;
//@EnableJpaRepositories("nl.tudelft.sem.lecture_scheduling.repositories")
import org.springframework.stereotype.Controller;
import java.util.*;


//@RestController
public class SchedulingController{

    //@PostMapping("schedule-lectures")
    public String  schedulePlannedLectures(List<Lecture> lecturesToSchedule){

        Map<String,Integer> allParticipants = new HashMap<>(); // Global hash map to keep track of participation in any lecture within 2 weeks

        int timeGapLength = RestrictionManagementCommunicator.getTimeGapLength(); // Make API call to retrieve time gap length
        List<Room> rooms = RestrictionManagementCommunicator.getAllRoomsWithAdjustedCapacity(); // Make API call to retrieve rooms with restricted capacity

        lecturesToSchedule.sort(Comparator.comparing(Lecture::getDate)
                .thenComparing((Lecture l) -> l.getCourse().getParticipants().size()));

        if(rooms == null) throw new NullPointerException("No rooms available");
        else rooms.sort(Comparator.comparing(Room::getCapacity));

        for(Lecture lecture: lecturesToSchedule) {
            List<String> courseParticipants = lecture.getCourse().getParticipants();
            for(String student: courseParticipants) {
                allParticipants.put(student, 0); // If already in there it shouldn't replace it.
            }
        }
        return null;
    }
}