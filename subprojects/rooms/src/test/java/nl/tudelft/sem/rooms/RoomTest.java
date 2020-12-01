package nl.tudelft.sem.rooms;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoomTest {

    private transient Room room;

    @Autowired
    private transient RoomRepository roomRepository;

    /**
     * The initial setup before each test.
     */
    @BeforeEach
    void setUp() {
    //        rest = new Restriction();
    //        rest.setValue(1.0f);
    //        rest.setName("test");
    //        restrictionRepository.save(rest);
    }

    //    @Test
    //    public void constructorNotNull() {
    //        assertNotNull(room);
    //    }

}