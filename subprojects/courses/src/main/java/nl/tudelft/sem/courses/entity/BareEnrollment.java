package nl.tudelft.sem.courses.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BareEnrollment {
    private static final long serialVersionUID = 1231234123412341244L;
    private String courseId;
    private String studentId;
}
