package nl.tudelft.sem.courses.entity;

import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Lecture")
public class Lecture {
    @Id
    @Column(name = "lecture_id")
    private int lectureId;

    @Column(name = "course_id")
    private String courseId;

    @Column(name = "duration")
    private int duration;

    @Column(name = "scheduled_date")
    private Date scheduledDate;

}
