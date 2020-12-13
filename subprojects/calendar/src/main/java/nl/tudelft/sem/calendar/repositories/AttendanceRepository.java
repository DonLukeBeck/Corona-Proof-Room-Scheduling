package nl.tudelft.sem.calendar.repositories;

import java.util.List;
import nl.tudelft.sem.calendar.entities.Attendance;
import nl.tudelft.sem.calendar.entities.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    List<Attendance> findAll();

    List<Attendance> findByLectureId(int lectureId);

    List<Attendance> findByStudentId(String studentId);

    List<Attendance> findByLectureIdAndStudentId(int lectureId, String studentId);
}
