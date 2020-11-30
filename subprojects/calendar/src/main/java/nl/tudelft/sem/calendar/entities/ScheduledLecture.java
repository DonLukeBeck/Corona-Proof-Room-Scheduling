package nl.tudelft.sem.calendar.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ScheduledLecture {
    private Course course;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Room room;
    private List<String> studentsOnCampus;

    /**
     * Creates a ScheduledLecture with a given course and date.
     *
     * @param course the course associated with this lecture
     * @param date   the date at which the lecture is scheduled
     */
    public ScheduledLecture(Course course, LocalDate date) {
        this.course = course;
        this.date = date;
        this.studentsOnCampus = new ArrayList<>();
    }

    /**
     * Returns the course of this scheduled lecture.
     *
     * @return course of this scheduled lecture
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Sets the course of this scheduled lecture.
     *
     * @param course the course of this scheduled lecture
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Returns the date of this scheduled lecture.
     *
     * @return the date of this scheduled lecture
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date of this scheduled lecture.
     *
     * @param date the date of this scheduled lecture
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Returns the stating time of this scheduled lecture.
     *
     * @return the starting time of this scheduled lecture
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the starting time of this scheduled lecture.
     *
     * @param startTime the starting time of this scheduled lecture
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Returns the ending time of this scheduled lecture.
     *
     * @return the ending time of this scheduled lecture
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the ending time of this scheduled lecture.
     *
     * @param endTime the ending time of this scheduled lecture
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the room of this scheduled lecture.
     *
     * @return the room of this scheduled lecture
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Sets the room of this scheduled lecture.
     *
     * @param room the room of this scheduled lecture
     */
    public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * Returns the list of students that are selected to attend this lecture on campus.
     *
     * @return the list of students that are selected to attend this lecture on campus
     */
    public List<String> getStudentsOnCampus() {
        return studentsOnCampus;
    }

    /**
     * Sets the list of students that are selected to attend this lecture on campus.
     *
     * @param studentsOnCampus the list netIds of the students that are selected to attend this
     *                         lecture on campus
     */
    public void setStudentsOnCampus(List<String> studentsOnCampus) {
        this.studentsOnCampus = studentsOnCampus;
    }

    /**
     * Adds a list of students to the list of students selected to attend this lecture on campus.
     *
     * @param netId the netId of the student selected to attend this lecture on campus
     */
    public void addStudentOnCampus(String netId) {
        this.studentsOnCampus.add(netId);
    }

    /**
     * Adds a list of students to the list of students that are selected to attend this lecture on
     * campus.
     *
     * @param netIds the list of netIds of the students that are to be added
     */
    public void addStudentsOnCampus(List<String> netIds) {
        this.studentsOnCampus.addAll(netIds);
    }
}