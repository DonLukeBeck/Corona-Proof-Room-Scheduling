package nl.tudelft.sem.calendar.entities;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public class ScheduledLecture {
    private Course course;
    private Date date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Room room;
    private List<String> studentsOnCampus;

    public ScheduledLecture(Course course, Date date, LocalTime startTime, LocalTime endTime,
                            Room room, List<String> studentsOnCampus) {
        this.course = course;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.studentsOnCampus = studentsOnCampus;
    }

    public Course getCourse(){
        return this.course;
    }
    public Date getDate() { return this.date; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public Room getRoom() { return room; }
    public List<String> getStudentsOnCampus() { return studentsOnCampus; }
}