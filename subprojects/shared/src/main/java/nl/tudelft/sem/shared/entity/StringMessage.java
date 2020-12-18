package nl.tudelft.sem.shared.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StringMessage {
    private static final long serialVersionUID = 1233465464564563330L;
    private String message;
}
