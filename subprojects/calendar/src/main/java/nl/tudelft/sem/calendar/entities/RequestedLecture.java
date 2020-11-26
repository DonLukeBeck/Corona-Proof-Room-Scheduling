package nl.tudelft.sem.calendar.entities;

import java.util.Date;

public class RequestedLecture {
    private Course course;
    private Date date;
    private int durationInMinutes;

    public RequestedLecture(Course course, Date date, int durationInMinutes){
        this.course = course;
        this.date = date;
        this.durationInMinutes = durationInMinutes;
    }

    public Course getCourse(){ return this.course; }
    public Date getDate() { return this.date; }
    public int getDurationInMinutes() { return this.durationInMinutes; }
}