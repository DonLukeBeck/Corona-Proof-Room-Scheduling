package nl.tudelft.sem.calendar.entities;

import java.io.Serializable;
import javax.persistence.Embeddable;
import lombok.Data;


@Embeddable
@Data
public class AttendanceId implements Serializable {
    private static long serialVersionUID = 1234324213L;
    private String studentid;
    private int lectureid;

    public AttendanceId(int lectureid, String studentid) {
        this.lectureid = lectureid;
        this.studentid = studentid;
    }
}