package nl.tudelft.sem.identity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    private String netid;

    private String password;

    public String getNetid() {
        return this.netid;
    }

    public void setNetid(String netid) {
        this.netid = netid;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
