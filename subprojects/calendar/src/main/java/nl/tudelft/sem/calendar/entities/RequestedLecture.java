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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }
}