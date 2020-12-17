package nl.tudelft.sem.courses.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = EnrollmentController.class)
@AutoConfigureMockMvc
@WebMvcTest
// This class doesn't ever need to be serialized, so neither do it's members
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class EnrollmentControllerTest {
    //TODO: getAllEnrollments
    //TODO: getEnrollmentsByCourse
    //TODO: bareFromEnrollment
    //TODO: constructor
}
