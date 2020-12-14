package nl.tudelft.sem.courses.repository;

import java.util.List;
import nl.tudelft.sem.courses.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, String>,
        JpaSpecificationExecutor<Enrollment> {

    List<Enrollment> findByCourseId(String courseId);


}
