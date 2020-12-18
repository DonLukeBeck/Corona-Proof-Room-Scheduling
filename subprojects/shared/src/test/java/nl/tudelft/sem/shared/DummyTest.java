package nl.tudelft.sem.shared;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

public class DummyTest {

    @Test
    // This test causes Jacoco to generate the test reports for us, which allows the pipeline to
    // assume that every subproject has a test report.
    public void dummyTest() {
        assert(true);
    }
}
