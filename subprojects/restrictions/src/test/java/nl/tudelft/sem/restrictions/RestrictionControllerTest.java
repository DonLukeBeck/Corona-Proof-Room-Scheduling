package nl.tudelft.sem.restrictions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RestrictionControllerTest {

    private transient Restriction rest1;
    private transient Restriction rest2;
    private transient RestrictionController restrictionController;

    @Autowired
    private transient RestrictionRepository restrictionRepository;

    /**
     * The initial setup before each test.
     */
    @BeforeEach
    void setUp() {
        rest1 = new Restriction();
        rest1.setValue(1.0f);
        rest1.setName("test");
        restrictionRepository.save(rest1);

        rest2 = new Restriction();
        rest2.setValue(2.0f);
        rest2.setName("can");
        restrictionRepository.save(rest2);

        restrictionController = new RestrictionController(restrictionRepository);
    }

    @Test
    public void constructorNotNull() {
        assertNotNull(restrictionController);
    }

    @Test
    public void addNewRestrictionSuccess() {
        assertEquals("Saved", restrictionController.addNewRestriction("name", 3.0f));
    }

    @Test
    public void addNewRestrictionSuccess1() {
        assertEquals("Updated", restrictionController.addNewRestriction("test", 4.0f));
    }

    @Test
    public void addNewRestrictionSuccess2() {
        assertEquals("Already Exists", restrictionController.addNewRestriction("test2", 2.0f));
    }

}