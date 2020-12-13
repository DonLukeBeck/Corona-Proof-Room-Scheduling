package nl.tudelft.sem.calendar.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Enrollment {
    private String studentId;
    private String courseId;
}
