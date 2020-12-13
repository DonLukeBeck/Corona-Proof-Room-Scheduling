package nl.tudelft.sem.calendar.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BareEnrollment {
    private static final long serialVersionUID = 1231234123412341244L;
    private String courseId;
    private String studentId;
}
