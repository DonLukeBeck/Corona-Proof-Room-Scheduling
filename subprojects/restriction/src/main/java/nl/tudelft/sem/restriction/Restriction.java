package nl.tudelft.sem.restriction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Restriction")
public class Restriction {
    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private float value;

    /**
     * Returns the name variable
     * @return name of the restriction
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name variable
     * @param name of the restriction entity
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the value variable
     * @return value of the restriction
     */
    public float getValue() {
        return value;
    }

    /**
     * Sets the value variable
     * @param value of the restriction entity
     */
    public void setValue(float value) {
        this.value = value;
    }
}
