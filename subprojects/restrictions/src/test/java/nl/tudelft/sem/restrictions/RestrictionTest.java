package nl.tudelft.sem.restrictions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RestrictionTest {

    private transient Restriction rest;

    @Autowired
    private transient RestrictionRepository restrictionRepository;

    /**
     * The initial setup before each test.
     */
    @BeforeEach
    void setUp() {
        rest = new Restriction();
        rest.setValue(1.0f);
        rest.setName("test");
        restrictionRepository.save(rest);
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