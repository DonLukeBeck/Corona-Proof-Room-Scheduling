package nl.tudelft.sem.courses.entity;

import java.sql.Date;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddLecture {
    private String courseId;
    private Date date;
    private int durationInMinutes;
}
