package nl.tudelft.sem.calendar.communication;

import java.time.LocalTime;
import java.util.List;
import nl.tudelft.sem.calendar.entities.Room;

public class RestrictionManagementCommunicator {

    /**
     * This method mocks the getAllRoomsWithAdjustedCapacity endpoint from the restriction
     * management service.
     *
     * @return the rooms used for scheduling, after adjusting their capacity
     */
    public static List<Room> getAllRoomsWithAdjustedCapacity() {
        return null;
    }

    /**
     * This method mocks the getTimeGapLength endpoint from the restriction management service.
     *
     * @return the time gap that is to be placed between each two lectures
     */
    public static int getTimeGapLength() {
        return 0;
    }

    /**
     * This method mocks the getStartTime endpoint from the restriction management service.
     *
     * @return the time at which the scheduling can start
     */
    public static LocalTime getStartTime() {
        return LocalTime.of(8, 45);
    }

    /**
     * This method mocks the getEndTime endpoint from the restriction management service.
     *
     * @return the time at which the scheduling should stop
     */
    public static LocalTime getEndTime() {
        return LocalTime.of(17, 45);
    }
}


