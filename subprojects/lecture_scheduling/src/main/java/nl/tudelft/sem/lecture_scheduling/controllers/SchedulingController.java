package nl.tudelft.sem.lecture_scheduling.controllers;
import nl.tudelft.sem.lecture_scheduling.entities.Lecture;
import nl.tudelft.sem.lecture_scheduling.entities.Room;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import nl.tudelft.sem.lecture_scheduling.repositories;
//@EnableJpaRepositories("nl.tudelft.sem.lecture_scheduling.repositories")

import java.util.*;
import nl.tudelft.sem.lecture_scheduling.entities.Student;
import org.springframework.stereotype.Controller;

//@RestController
public class SchedulingController{

    //@PostMapping("schedule-lectures")
    public String  schedulePlannedLectures(List<Lecture> lecturesTo){
        int percentOfStudents = 1;

        Map<String,Integer> allParticipants = new HashMap<String, Integer>();   // make API call to course to ask for number of participants

        List<Room> rooms = new ArrayList<Room>(); // make API call to rooms to retrieve rooms

        Collections.sort(lecturesTo, Comparator.comparing(l -> l.getDate()).thenComparing(l -> l.getCourseParticipants().size()));
        Collections.sort(rooms, Comparator.comparing(r -> r.getCapacity()));

        for(Lecture lecture: lecturesTo) {
            lecture.getCourse().getParticpants()
            //courseParticipants = lecture.getCourse().getParticipants();
            for(String student: courseParticipants) {
                allParticipants.put(student.getNetId(), 0);
            }

        }


        return null;
    }

}