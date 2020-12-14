package nl.tudelft.sem.calendar.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(Enrollment.class)
public class Enrollment implements Serializable {

    private static final long serialVersionUID = 1233454322341123464L;

    @Id
    @Column(name = "student_id")
    private String studentId;

    @Id
    @Column(name = "course_id")
    private String courseId;
}
