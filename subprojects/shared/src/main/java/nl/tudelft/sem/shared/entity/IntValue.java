package nl.tudelft.sem.shared.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IntValue {
    private static final long serialVersionUID = 1233465464564562096L;
    private int value;
}
