package nl.tudelft.sem.restrictions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restriction")
public class Restriction {
    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private float value;
}
