package nl.tudelft.sem.calendar.entities;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Course {
    private List<String> netIds;
    private String courseId;
    private String courseName;
    private String teacherId;
}
