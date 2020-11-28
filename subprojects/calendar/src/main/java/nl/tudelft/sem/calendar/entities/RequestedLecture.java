package nl.tudelft.sem.calendar.entities;
import java.time.LocalDate;

public class RequestedLecture {
    private Course course;
    private LocalDate date;
    private int durationInMinutes;

    public RequestedLecture(Course course, LocalDate date, int durationInMinutes){
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }
}