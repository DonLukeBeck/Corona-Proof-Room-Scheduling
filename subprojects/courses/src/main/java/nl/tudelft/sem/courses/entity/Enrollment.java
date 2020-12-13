package nl.tudelft.sem.courses.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Enrollment")
@IdClass(Enrollment.class)
public class Enrollment implements Serializable {
    @Id
    @Column(name = "course_id")
    private String courseId;

    @Id
    @Column(name = "student_id")
    private String studentId;

}
