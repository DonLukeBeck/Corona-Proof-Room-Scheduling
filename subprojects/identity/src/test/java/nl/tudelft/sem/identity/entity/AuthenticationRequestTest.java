package nl.tudelft.sem.identity.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthenticationRequestTest {
    private static AuthenticationRequest request;

    /**
     * Method to setup all the parameters before each test.
     */
    @BeforeEach
    public void setup() {
        request = new AuthenticationRequest();
        request.setNetid("lbecheanu");
        request.setPassword("Password");
    }

    @Test
    public void constructorTest() {
        assertNotNull(request);
    }

    @Test
    public void getNetIdTest() {
        assertEquals("lbecheanu", request.getNetid());
    }

    @Test
    public void setNetIdTest() {
        request.setNetid("testId");
        assertEquals("testId", request.getNetid());
    }

    @Test
    public void getPasswordTest() {
        assertEquals("Password", request.getPassword());
    }

    @Test
    public void setPasswordTest() {
        request.setPassword("123");
        assertEquals("123", request.getPassword());
    }
}
