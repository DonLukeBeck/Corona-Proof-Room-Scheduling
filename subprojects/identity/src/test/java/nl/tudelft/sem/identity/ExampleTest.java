package nl.tudelft.sem.identity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ExampleTest {

    @Test
    public void testGettersSetters() {
        Example exampleTest = new Example("name");
        assertEquals("nice", exampleTest.getName());
        exampleTest.setName("newName");
        assertEquals("nice", exampleTest.getName());
    }
}