package nl.tudelft.sem.courses.repository;


import nl.tudelft.sem.courses.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, String>,
        JpaSpecificationExecutor<Course> {

    Course findByCourseId(String courseId);

    Course findByCourseName(String courseName);

    @Query(value = "SELECT * FROM Course WHERE teacher_id = ?1 LIMIT 1", nativeQuery = true)
    Course findByCourseIdForTeacher(String teacherId);

}
