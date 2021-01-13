package nl.tudelft.sem.calendar.entities;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
     * Custom constructor to create courses with a list of netIds. Used to test the scheduling
     * algorithm.
     *
     * @param netIds a list of netids representing course participants
     * @param courseId the id of the course to create
     * @param courseName teh name of the course to create
     * @param teacherId the id of the teacher teaching the course to create
     */
    public Course(List<String> netIds, String courseId, String courseName, String teacherId) {
        participantsList = new ArrayList<>();
        netIds.stream().forEach(netId -> this.participantsList.add(new Enrollment(netId, courseId)));
        this.courseId = courseId;
        this.courseName = courseName;
        this.teacherId = teacherId;
    }

    /**
     * Returns the netIds for each enrollment.
     *
     * @return the list of netIds associated with the course.
     */
    public List<String> getNetIds() {
        List<String> netIds = new ArrayList<>();
        participantsList.stream().forEach(enrolment -> netIds.add(enrolment.getStudentId()));
        return netIds;
    }
}
