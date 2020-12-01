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
@Table(name = "Enrollment")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Enrollment {
    @Column(name = "student_id")
    private String studentId;

    @Column(name = "course_id")
    private String courseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    /**
     * Custom constructor using only the studentId and courseId.
     * @param netId the netId of the associated student
     * @param courseId the courseId of the associated course
     */
    public Enrollment(String netId, String courseId) {
        this.studentId = netId;
        this.courseId = courseId;
    }
}
