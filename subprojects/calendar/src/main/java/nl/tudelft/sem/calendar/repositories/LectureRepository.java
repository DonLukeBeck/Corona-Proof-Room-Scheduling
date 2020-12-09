package nl.tudelft.sem.calendar.repositories;

import java.util.List;
import nl.tudelft.sem.calendar.entities.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {

    List<Lecture> findAll();

    List<Lecture> findByLectureId(long lectureId);
}
