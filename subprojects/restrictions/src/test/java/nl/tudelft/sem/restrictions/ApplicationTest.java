package nl.tudelft.sem.restrictions;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ApplicationTest {

    @Test
    public void applicationStartTest() {
        Application.main(new String[]{});
    }
}
