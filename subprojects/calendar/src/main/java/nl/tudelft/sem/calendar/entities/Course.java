package nl.tudelft.sem.calendar.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Course", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Course {
    @Column(name = "course_id")
    private String courseId;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "teacher_id")
    private String teacherId;

    @OneToMany(mappedBy = "course_id", fetch = FetchType.LAZY)
    private List<Enrollment> participantsList;


    /**
     * Custom constructor to create courses with just a list of netIds. Used to test the scheduling
     * algorithm.
     *
     * @param netIds a list of netids representing course participants
     */
    public Course(List<String> netIds) {
        participantsList = new ArrayList<>();
        netIds.stream().forEach(netId -> participantsList
                .add(new Enrollment(netId, courseId)));
    }

    /**
     * Returns the netIds for each enrollment. Used to test the scheduling.
     *
     * @return the list of netIds associated with the course.
     */
    public List<String> getNetIds() {
        List<String> netIds = new ArrayList<>();
        participantsList.stream().forEach(enrolment -> netIds.add(enrolment.getStudentId()));
        return netIds;
    }
}
