package nl.tudelft.sem.courses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LectureRepository extends JpaRepository<Lecture, Integer> {

    @Query(value = "SELECT * FROM Lecture WHERE course_id = ?1 LIMIT 1", nativeQuery = true)
    Course findByCourseId(String courseId);
}
