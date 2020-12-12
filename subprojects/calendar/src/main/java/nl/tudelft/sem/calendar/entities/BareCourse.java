package nl.tudelft.sem.calendar.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BareCourse {
    private static final long serialVersionUID = 1233464312341123464L;
    private String courseId;
    private String courseName;
    private String teacherId;
}
