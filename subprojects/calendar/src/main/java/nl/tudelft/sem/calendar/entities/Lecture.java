package nl.tudelft.sem.calendar.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Lecture", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Lecture {
    @Id
    @Column(name = "lecture_id")
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

    @OneToMany(mappedBy = "lecture_id", fetch = FetchType.LAZY)
    private List<Attendance> participantsList;
}
