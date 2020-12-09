package nl.tudelft.sem.courses;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, String>,
        JpaSpecificationExecutor<Course> {

    Course findByCourseId(String courseId);

    Course findByCourseName(String courseName);

}
