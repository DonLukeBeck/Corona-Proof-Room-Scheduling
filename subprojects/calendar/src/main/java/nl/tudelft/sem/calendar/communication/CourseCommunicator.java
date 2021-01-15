package nl.tudelft.sem.calendar.communication;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import nl.tudelft.sem.calendar.exceptions.ServerErrorException;
import nl.tudelft.sem.calendar.util.Constants;
import nl.tudelft.sem.shared.entity.BareCourse;
import nl.tudelft.sem.shared.entity.BareEnrollment;
import nl.tudelft.sem.shared.entity.BareLecture;
import org.springframework.stereotype.Service;

@Service
public class CourseCommunicator extends Communicator {

    /**
     * Retrieves the list of courses based on the id of a teacher.
     *
     * @param teacherId - the id of the teacher
     * @return a list of courses
     * @throws IOException          - an input/output exception
     * @throws InterruptedException - an interrupted exception
     * @throws ServerErrorException - a server error exception
     */
    public List<BareCourse> getCoursesByTeacher(String teacherId)
        throws IOException, InterruptedException, ServerErrorException {
        return objectMapper.readValue(getResponse(
            "/course/getCoursesForTeacher?teacherId=" + encode(teacherId),
            Constants.COURSE_SERVER_URL).body(), new TypeReference<>() {});
    }

    /**
     * Gets lectures after specified date.
     *
     * @param date the date after which you want the lectures
     * @return a list of lectures scheduled after the date
     * @throws InterruptedException the interrupted exception
     * @throws ServerErrorException the server error exception
     * @throws IOException          the io exception
     */
    public List<BareLecture> getLecturesAfterDate(LocalDate date)
        throws InterruptedException, ServerErrorException, IOException {
        return objectMapper.readValue(getResponse("/lecture/getLecturesAfterDate?date="
                + encode(date.toString()), Constants.COURSE_SERVER_URL).body(),
            new TypeReference<>() {});
    }

    /**
     * Gets course from id.
     *
     * @param courseId the course id
     * @return the bare course
     * @throws InterruptedException the interrupted exception
     * @throws ServerErrorException the server error exception
     * @throws IOException          the io exception
     */
    public BareCourse getCourseFromId(String courseId)
        throws InterruptedException, ServerErrorException, IOException {
        return objectMapper.readValue(getResponse("/course/getCourse?courseId="
                + courseId, Constants.COURSE_SERVER_URL).body(),
            new TypeReference<>() {});
    }

    /**
     * Gets enrollments for course.
     *
     * @param courseId the course id
     * @return the enrollments for course
     * @throws InterruptedException the interrupted exception
     * @throws ServerErrorException the server error exception
     * @throws IOException          the io exception
     */
    public List<BareEnrollment> getEnrollmentsForCourse(String courseId)
        throws InterruptedException, ServerErrorException, IOException {
        return objectMapper.readValue(getResponse("/enrollment/getEnrollmentsByCourse?courseId="
                + courseId, Constants.COURSE_SERVER_URL).body(),
            new TypeReference<>() {});
    }

}

