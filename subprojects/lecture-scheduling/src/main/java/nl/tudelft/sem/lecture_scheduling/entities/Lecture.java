package nl.tudelft.sem.lecture_scheduling.entities;

import java.util.Date;

public class Lecture {
    private Course course;
    private Date date;

    public Course getCourse(){
        return this.course;
    }
    public Date getDate() { return this.date; }

}