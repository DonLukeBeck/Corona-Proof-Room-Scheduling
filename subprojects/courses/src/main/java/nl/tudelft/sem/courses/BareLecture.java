package nl.tudelft.sem.courses;

import java.time.LocalDate;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BareLecture {
    private static final long serialVersionUID = 1233465464564564564L;
    private String courseId;
    private LocalDate date;
    private int durationInMinutes;
}
