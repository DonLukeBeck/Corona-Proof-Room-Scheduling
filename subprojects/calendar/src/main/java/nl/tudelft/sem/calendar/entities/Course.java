package nl.tudelft.sem.calendar.entities;
import java.util.List;

public class Course {
    private List<String> participants;

    public Course(List<String> participants) {
        this.participants = participants;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }
}