package nl.tudelft.sem.restrictions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

class RestrictionTest {

    private static Restriction rest;

    /**
     * The initial setup before each test.
     */
    @BeforeEach
    void setUp() {
        rest = new Restriction();
        rest.setValue(1.0f);
        rest.setName("test");
    }

    @Test
    public void constructorNotNull() {
        assertNotNull(rest);
    }

    @Test
    void testGetName() {
        assertEquals("test", rest.getName());
    }

    @Test
    void setName() {
        rest.setName("can");
        assertEquals("can", rest.getName());
    }

    @Test
    void getValue() {
        assertEquals(1.0f, rest.getValue());
    }

    @Test
    void setValue() {
        rest.setValue(2.0f);
        assertEquals(2.0f, rest.getValue());
    }
}