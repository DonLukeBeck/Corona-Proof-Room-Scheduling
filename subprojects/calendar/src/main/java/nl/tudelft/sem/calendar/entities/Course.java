package nl.tudelft.sem.calendar.entities;

import java.util.List;

public class Course {
    private List<String> participants;

    /**
     * Creates a course with a given list of participants.
     *
     * @param participants a list of strings representing
     */
    public Course(List<String> participants) {
        this.participants = participants;
    }

    /**
     * Returns the list of course participants.
     *
     * @return the list of course participants for this course
     */
    public List<String> getParticipants() {
        return participants;
    }

    /**
     * Sets the list of course participants.
     *
     * @param participants the list of course participants for this course
     */
    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }
}