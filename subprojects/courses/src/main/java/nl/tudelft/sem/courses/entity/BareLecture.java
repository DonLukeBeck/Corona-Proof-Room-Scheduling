package nl.tudelft.sem.courses.entity;

import java.time.LocalDate;
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
