package nl.tudelft.sem.calendar.entities;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Course {
    private String courseId;
    private String courseName;
    private String teacherId;
    private List<Enrollment> participantsList;

    /**
     * Custom constructor to create courses with just a list of netIds. Used to test the scheduling
     * algorithm.
     *
     * @param netIds a list of netids representing course participants
     */
    public Course(List<String> netIds) {
        participantsList = new ArrayList<>();
        netIds.stream().forEach(netId -> participantsList.add(new Enrollment(netId, courseId)));
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
