package nl.tudelft.sem.calendar.entities;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BareLecture {
    private static final long serialVersionUID = 1233465464564564564L;
    private String courseId;
    private Date date;
    private int durationInMinutes;
}
