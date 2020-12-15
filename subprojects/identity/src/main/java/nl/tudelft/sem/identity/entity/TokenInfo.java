package nl.tudelft.sem.identity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenInfo {
    private String role;
    private String netid;
}
