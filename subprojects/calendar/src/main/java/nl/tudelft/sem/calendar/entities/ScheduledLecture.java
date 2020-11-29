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

    public ScheduledLecture(Course course, LocalDate date) {
        this.course = course;
        this.date = date;
        this.studentsOnCampus = new ArrayList<>();
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

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public List<String> getStudentsOnCampus() {
        return studentsOnCampus;
    }

    public void setStudentsOnCampus(List<String> studentsOnCampus) {
        this.studentsOnCampus = studentsOnCampus;
    }

    public void addStudentOnCampus(String netId) {
        this.studentsOnCampus.add(netId);
    }

    public void addStudentsOnCampus(List<String> netIds) {
        this.studentsOnCampus.addAll(netIds);
    }
}