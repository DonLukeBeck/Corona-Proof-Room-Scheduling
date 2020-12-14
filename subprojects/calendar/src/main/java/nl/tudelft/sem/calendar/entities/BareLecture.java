package nl.tudelft.sem.calendar.entities;

import java.time.LocalDate;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BareLecture {
    private static final long serialVersionUID = 1233465464564564564L;
    private String courseId;
    private LocalDate date;
    private int durationInMinutes;
}
