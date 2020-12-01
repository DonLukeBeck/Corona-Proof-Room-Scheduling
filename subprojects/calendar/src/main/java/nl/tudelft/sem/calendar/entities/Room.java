package nl.tudelft.sem.calendar.entities;

import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Room {
    private Integer roomId;
    private String name;
    private Integer capacity;
}
