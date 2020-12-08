package nl.tudelft.sem.courses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    @Query(value = "SELECT * FROM Enrollment WHERE course_id = ?1 LIMIT 1", nativeQuery = true)
    Course findByCourseId(String courseId);
}
