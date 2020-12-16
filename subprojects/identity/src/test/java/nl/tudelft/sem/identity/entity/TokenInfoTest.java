package nl.tudelft.sem.identity.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TokenInfoTest {
    private static TokenInfo token;

    /**
     * Method to setup all the parameters before each test.
     */
    @BeforeEach
    public void setup() {
        token = new TokenInfo();
        token.setNetid("lbecheanu");
        token.setRole("student");
    }

    @Test
    public void constructorTest() {
        assertNotNull(token);
    }

    @Test
    public void getNetIdTest() {
        assertEquals("lbecheanu", token.getNetid());
    }

    @Test
    public void setNetIdTest() {
        token.setNetid("testId");
        assertEquals("testId", token.getNetid());
    }

    @Test
    public void getRoleTest() {
        assertEquals("student", token.getRole());
    }

    @Test
    public void setRoleTest() {
        token.setRole("teacher");
        assertEquals("teacher", token.getRole());
    }
}
