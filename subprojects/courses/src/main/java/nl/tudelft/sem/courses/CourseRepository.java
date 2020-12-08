package nl.tudelft.sem.courses;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query(value = "SELECT * FROM Course WHERE course_id = ?1 LIMIT 1", nativeQuery = true)
    Course findByCourseId(String courseId);
}
