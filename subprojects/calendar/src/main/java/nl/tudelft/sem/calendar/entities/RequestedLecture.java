package nl.tudelft.sem.calendar.entities;

import java.time.LocalDate;

public class RequestedLecture {
    private Course course;
    private LocalDate date;
    private int durationInMinutes;

    /**
     * Creates a Requested Lecture based on a course, date and duration.
     *
     * @param course            the course where the lecture is associated with
     * @param date              the date at which the lecture should be scheduled
     * @param durationInMinutes the duration of the lecture in minutes
     */
    public RequestedLecture(Course course, LocalDate date, int durationInMinutes) {
        this.course = course;
        this.date = date;
        this.durationInMinutes = durationInMinutes;
    }

    /**
     * Returns the associated course of this lecture.
     *
     * @return the associated course
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Sets the associated course of this lecture.
     *
     * @param course the associated course
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Returns the associated date of this lecture.
     *
     * @return the associated date
     */
    public LocalDate getDate() {
        return date;
    }


    /**
     * Sets the associated date of this lecture.
     *
     * @param date the associated date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Gets the associated duration of this lecture.
     *
     * @return the duration in minutes
     */
    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    /**
     * Sets the associated duration of this lecture.
     *
     * @param durationInMinutes the duration in minutes
     */
    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }
}