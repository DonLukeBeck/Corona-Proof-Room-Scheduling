package nl.tudelft.sem.courses.repository;

import java.sql.Date;
import java.util.List;
import nl.tudelft.sem.courses.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Integer>,
        JpaSpecificationExecutor<Lecture> {

    List<Lecture> findByCourseId(String courseId);

    // @Query(value = "SELECT * FROM lecture WHERE course_id = ?1 AND scheduled_date = ?2 LIMIT 1",
    //         nativeQuery = true)
    List<Lecture> findByCourseIdAndScheduledDate(String courseId, Date date);

}
