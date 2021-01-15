package nl.tudelft.sem.identity.entity;

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
@Table(name = "user")
public class User {
    @Id
    @Column(name = "netid")
    private String netid;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "quarantine")
    private Boolean quarantine;

}
