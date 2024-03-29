package nl.tudelft.sem.shared.entity;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddLecture {
    private String courseId;
    private Date date;
    private int durationInMinutes;
}
