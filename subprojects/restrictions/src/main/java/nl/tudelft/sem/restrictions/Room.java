package nl.tudelft.sem.restrictions;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Room implements Serializable {
    private static final long serialVersionUID = 1233454322345527774L;
    private Integer roomId;
    private String name;
    private Integer capacity;
}
