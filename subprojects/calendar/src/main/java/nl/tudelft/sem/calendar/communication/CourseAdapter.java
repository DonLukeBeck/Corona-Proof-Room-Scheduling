package nl.tudelft.sem.calendar.communication;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import nl.tudelft.sem.calendar.entities.Course;
import nl.tudelft.sem.calendar.entities.Enrollment;
import nl.tudelft.sem.calendar.entities.Lecture;
import nl.tudelft.sem.calendar.exceptions.ServerErrorException;
import nl.tudelft.sem.calendar.util.Constants;
import nl.tudelft.sem.shared.entity.BareCourse;
import nl.tudelft.sem.shared.entity.BareEnrollment;
import nl.tudelft.sem.shared.entity.BareLecture;
import org.springframework.stereotype.Service;

@Service
public class CourseAdapter {

    private final transient CourseCommunicator courseCommunicator = new CourseCommunicator();

    /**
     * Retrieves the lectures that need to be scheduled from the
     * Course Management Service.
     *
     * @param date the (current) date after which the lectures should be considered for scheduling.
     * @return a list of {@link Lecture} objects, used in the scheduling process
     * @throws IOException          an input/output exception
     * @throws InterruptedException an interrupted exception
     * @throws ServerErrorException a server error exception
     */
    public List<Lecture> getToBeScheduledLectures(LocalDate date)
        throws IOException, InterruptedException, ServerErrorException {

        var bareLectures = courseCommunicator.getLecturesAfterDate(date);
        // this isn't a DU-anomaly since we use it to store and retrieve courses inside the for loop
        @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
        var courseMap = new HashMap<String, Course>();
        var lectureList = new ArrayList<Lecture>();
        for (var l : bareLectures) {
            if (!courseMap.containsKey(l.getCourseId())) {
                courseMap.put(l.getCourseId(), courseFromId(l.getCourseId()));
            }
            lectureList.add(Lecture.builder().course(courseMap.get(l.getCourseId()))
                .courseId(l.getCourseId()).durationInMinutes(l.getDurationInMinutes())
                .date(l.getDate()).build());
        }
        return lectureList;
    }

    /**
     * Retrieves the associated course and participants for a lecture.
     *
     * @param courseId the id of the course that needs to be retrieved
     * @return a {@link Course} object, containing information about the course
     * @throws IOException          an input/output exception
     * @throws InterruptedException an interrupted exception
     * @throws ServerErrorException a server error exception
     */
    public Course courseFromId(String courseId)
        throws IOException, InterruptedException, ServerErrorException {
        var resp = courseCommunicator.getCourseFromId(courseId);
        var enrollments = courseCommunicator.getEnrollmentsForCourse(courseId);

        return new Course(enrollments.stream().map(BareEnrollment::getStudentId)
                .collect(Collectors.toList()), resp.getCourseId(), resp.getCourseName(),
                resp.getTeacherId());
    }

    /**
     * Retrieves the list of courses based on the id of a teacher.
     *
     * @param teacherId - the id of the teacher
     * @return a list of courses
     * @throws IOException          - an input/output exception
     * @throws InterruptedException - an interrupted exception
     * @throws ServerErrorException - a server error exception
     */
    public List<BareCourse> coursesFromTeacher(String teacherId)
        throws IOException, InterruptedException, ServerErrorException {
        return courseCommunicator.getCoursesByTeacher(teacherId);
    }
}

