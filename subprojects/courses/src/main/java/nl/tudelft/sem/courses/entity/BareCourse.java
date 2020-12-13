package nl.tudelft.sem.courses.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BareCourse {
    private static final long serialVersionUID = 1233464312341123464L;
    private String courseId;
    private String courseName;
    private String teacherId;
}
