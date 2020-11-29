package nl.tudelft.sem.calendar.communication;

import java.time.LocalTime;
import java.util.List;
import nl.tudelft.sem.calendar.entities.Room;

public class RestrictionManagementCommunicator {
    public static List<Room> getAllRoomsWithAdjustedCapacity() {
        return null;
    }

    public static int getTimeGapLength() {
        return 0;
    }

    public static LocalTime getStartTime() {
        return LocalTime.of(8, 45);
    }

    public static LocalTime getEndTime() {
        return LocalTime.of(17, 45);
    }
}


