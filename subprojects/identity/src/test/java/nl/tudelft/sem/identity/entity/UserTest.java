package nl.tudelft.sem.identity.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class UserTest {
    private static User user;

    /**
     * Method to setup all the paramaters before each test.
     */
    @BeforeEach
    public void setup() {
        user = new User();
        user.setNetid("lbecheanu");
        user.setRole("Student");
        user.setPassword("Password");
        user.setQuarantine(false);
    }

    @Test
    public void constructorTest() {
        assertNotNull(user);
    }

    @Test
    public void getNetIdTest() {
        assertEquals("lbecheanu", user.getNetid());
    }

    @Test
    public void setNetIdTest() {
        user.setNetid("testId");
        assertEquals("testId", user.getNetid());
    }

    @Test
    public void getRoleTest() {
        assertEquals("Student", user.getRole());
    }

    @Test
    public void setRoleTest() {
        user.setRole("Teacher");
        assertEquals("Teacher", user.getRole());
    }

    @Test
    public void getPasswordTest() {
        assertEquals("Password", user.getPassword());
    }

    @Test
    public void setPasswordTest() {
        user.setPassword("123");
        assertEquals("123", user.getPassword());
    }

    @Test
    public void getQuarantineTest() {
        assertEquals(false, user.getQuarantine());
    }

    @Test
    public void setQuarantineTest() {
        user.setQuarantine(true);
        assertEquals(true, user.getQuarantine());
    }

}
