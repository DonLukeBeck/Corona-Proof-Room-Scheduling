package nl.tudelft.sem.shared.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddCourse {
    private String courseId;
    private String courseName;
    private String teacherId;
    private List<String> participants;
}

