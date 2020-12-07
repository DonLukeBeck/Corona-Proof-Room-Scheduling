package nl.tudelft.sem.calendar.entities;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class AttendanceId implements Serializable {
    private String student_id;
    private int lecture_id;

    public AttendanceId(int lecture_id, String student_id) {
        this.lecture_id = lecture_id;
        this.student_id = student_id;
    }
}