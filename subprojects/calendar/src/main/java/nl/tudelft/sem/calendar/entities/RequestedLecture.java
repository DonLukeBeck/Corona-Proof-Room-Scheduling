package nl.tudelft.sem.calendar.entities;

import java.util.Date;

public class RequestedLecture {
    private Course course;
    private Date date;
    private int durationInMinutes;

    public Course getCourse(){
        return this.course;
    }
    public Date getDate() { return this.date; }
    public int getDurationInMinutes() { return this.durationInMinutes; }
}