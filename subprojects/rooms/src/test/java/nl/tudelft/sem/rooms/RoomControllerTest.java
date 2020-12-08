package nl.tudelft.sem.rooms;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoomControllerTest {

    private transient Room room1;
    private transient Room room2;
    private transient RoomController roomController;

    @Autowired
    private transient RoomRepository roomRepository;

    /**
     * The initial setup before each test.
     */
    @BeforeEach
    void setUp() {
        //        rest1 = new Restriction();
        //        rest1.setValue(1.0f);
        //        rest1.setName("test");
        //        restrictionRepository.save(rest1);
        //
        //        rest2 = new Restriction();
        //        rest2.setValue(2.0f);
        //        rest2.setName("can");
        //        restrictionRepository.save(rest2);
        //
        //        restrictionController = new RestrictionController(restrictionRepository);
    }

    //    @Test
    //    public void constructorNotNull() {
    //        assertNotNull(roomController);
    //    }
}
