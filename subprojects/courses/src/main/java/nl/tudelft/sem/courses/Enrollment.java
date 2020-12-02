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

    /**
     * returns the course ID.
     *
     * @return the course ID
     */
    public String getCourseId() {
        return courseId;
    }

    /**
     * sets the course ID.
     *
     * @param courseId the new course ID
     */
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    /**
     * returns the student ID.
     *
     * @return the student ID
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * sets the student ID.
     *
     * @param studentId the new student ID
     */
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
