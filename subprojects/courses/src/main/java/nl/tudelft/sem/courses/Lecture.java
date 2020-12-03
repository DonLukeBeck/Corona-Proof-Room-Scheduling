package nl.tudelft.sem.courses;

import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
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

    /**
     * returns the lecture ID.
     *
     * @return the lecture ID
     */
    public int getLectureId() {
        return lectureId;
    }

    /**
     * sets the lecture ID.
     *
     * @param lectureId the new lecure ID
     */
    public void setLectureId(int lectureId) {
        this.lectureId = lectureId;
    }

    /**
     * returns the course ID.
     *
     * @return the course ID.
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
     * returns the duration.
     *
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * sets the duration.
     *
     * @param duration the new duration.
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * returns the scheduled date.
     *
     * @return the scheduled date.
     */
    public Date getScheduledDate() {
        return scheduledDate;
    }

    /**
     * Sets the scheduled date.
     *
     * @param scheduledDate the scheduled date.
     */
    public void setScheduledDate(Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }
}
