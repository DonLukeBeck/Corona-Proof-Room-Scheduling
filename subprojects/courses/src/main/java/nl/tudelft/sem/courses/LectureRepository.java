package nl.tudelft.sem.courses;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LectureRepository extends JpaRepository<Lecture, Integer>,
        JpaSpecificationExecutor<Lecture> {

    List<Lecture> findByCourseId(String courseId);

    List<Lecture> findByCourseName(String courseName);

}
