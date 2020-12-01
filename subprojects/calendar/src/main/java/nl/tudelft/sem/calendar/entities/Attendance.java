package nl.tudelft.sem.calendar.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    @Column(name = "student_id")
    private String studentId;

    @Column(name = "lecture_id")
    private Integer lectureId;

    @Column(name = "physical")
    private Boolean physical;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;
}
