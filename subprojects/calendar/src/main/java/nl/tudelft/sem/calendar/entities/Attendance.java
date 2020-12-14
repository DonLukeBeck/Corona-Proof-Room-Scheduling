package nl.tudelft.sem.calendar.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Attendance", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@IdClass(Attendance.class)
public class Attendance implements Serializable {

    private static final long serialVersionUID = 1233464392341123464L;

    @Id
    @Column(name = "student_id", insertable = false, updatable = false)
    private String studentId;

    @Id
    @Column(name = "lecture_id", insertable = false, updatable = false)
    private Integer lectureId;

    @Column(name = "physical")
    private Boolean physical;
}
