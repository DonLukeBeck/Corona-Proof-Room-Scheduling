package nl.tudelft.sem.calendar.repositories;

import java.time.LocalDate;
import java.util.List;
import nl.tudelft.sem.calendar.entities.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Integer> {

    List<Lecture> findAll();
    List<Lecture> findByLectureId(int lectureId);
    List<Lecture> findByCourseId(String courseId);
    List<Lecture> findByDate(LocalDate date);
}