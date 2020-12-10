package nl.tudelft.sem.calendar.communication;

import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem.calendar.entities.Lecture;

public class CourseManagementCommunicator {

    /**
     * This method is a mock for the connection with the Course management service.
     *
     * @return the lectures pushed by the course service that need to be scheduled
     */
    public static List<Lecture> getToBeScheduledLectures() {
        return new ArrayList<Lecture>();
    }
}


