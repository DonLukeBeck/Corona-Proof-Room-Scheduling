package nl.tudelft.sem.calendar.entities;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScheduledLecture {
    private Course course;
    private Date date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Room room;
    private List<String> studentsOnCampus;

    public ScheduledLecture(Course course, Date date) {
        this.course = course;
        this.date = date;
        this.startTime = null;
        this.endTime = null;
        this.room = null;
        this.studentsOnCampus = new ArrayList<String>();
    }

    public Course getCourse(){
        return this.course;
    }
    public Date getDate() { return this.date; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public Room getRoom() { return room; }
    public List<String> getStudentsOnCampus() { return studentsOnCampus; }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void addStudentsOnCampus(String netId) {
        this.studentsOnCampus.add(netId);
    }
}