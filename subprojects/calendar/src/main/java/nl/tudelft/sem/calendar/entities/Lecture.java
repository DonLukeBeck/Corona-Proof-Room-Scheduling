package nl.tudelft.sem.calendar.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Lecture", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "lecture_id")
    @Generated
    private Integer lectureId;

    @Column(name = "course_id")
    private String courseId;

    @Column(name = "room_id")
    private Integer roomId;

    @Column(name = "startTime")
    private LocalTime startTime;

    @Column(name = "endTime")
    private LocalTime endTime;

    @Column(name = "date")
    private LocalDate date;

    // What we get from the other microservice
    @Transient
    private int durationInMinutes;
    @Transient
    private Course course;
}
