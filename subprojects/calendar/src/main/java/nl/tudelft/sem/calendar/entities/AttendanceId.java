package nl.tudelft.sem.calendar.entities;
import javax.persistence.Embeddable;
import java.io.Serializable;
import lombok.Data;
import lombok.Generated;


@Embeddable
@Data
public class AttendanceId implements Serializable {
    private static long serialVersionUID = 1234324213L;
    private String studentId;
    private int lectureId;

    public AttendanceId(int lectureId, String studentId) {
        this.lectureId = lectureId;
        this.studentId = studentId;
    }
}