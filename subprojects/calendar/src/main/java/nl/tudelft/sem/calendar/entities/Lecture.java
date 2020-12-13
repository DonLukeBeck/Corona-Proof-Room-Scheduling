package nl.tudelft.sem.calendar.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
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
public class Lecture implements Serializable  {
    
    private static final long serialVersionUID = 1233464399341123464L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    // Attributes of the lecture, before it gets scheduled
    @Transient
    @JsonIgnore
    private int durationInMinutes;

    @Transient
    @JsonIgnore
    private Course course;

    @Transient
    boolean selectedForOnCampus;
}
