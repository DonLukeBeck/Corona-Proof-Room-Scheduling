package nl.tudelft.sem.calendar.entities;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
public class Attendance {
    @EmbeddedId
    private AttendanceId attendanceId;

    @Column(name = "student_id", insertable=false, updatable = false)
    private String studentId;

    @Column(name = "lecture_id", insertable=false, updatable = false)
    private Integer lectureId;

    @Column(name = "physical")
    private Boolean physical;
}
