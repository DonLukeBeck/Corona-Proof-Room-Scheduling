package nl.tudelft.sem.courses;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EnrollmentRepository extends JpaRepository<Enrollment, String>,
        JpaSpecificationExecutor<Enrollment> {

    List<Enrollment> findByCourseId(String courseId);

    List<Enrollment> findByCourseName(String courseName);

}
