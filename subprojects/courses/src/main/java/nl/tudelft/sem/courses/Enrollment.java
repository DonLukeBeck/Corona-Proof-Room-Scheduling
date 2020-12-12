package nl.tudelft.sem.courses;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Enrollment")
public class Enrollment {
    @Column(name = "course_id")
    private String courseId;

    @Column(name = "student_id")
    private String studentId;

}
