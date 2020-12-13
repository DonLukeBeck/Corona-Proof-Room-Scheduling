package nl.tudelft.sem.calendar.communication;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import nl.tudelft.sem.calendar.entities.Room;

public class RestrictionManagementCommunicator extends Communicator {

    //    public static List<Room> getAllRoomsWithAdjustedCapacity() throws
    //            InterruptedException, ServerErrorException, IOException {
    //
    //            var response = getResponse("/getAllRoomsWithAdjustedCapacity",
    //                    Constants.RESTRICTION_SERVER_URL);
    //            return objectMapper.readValue(response.body(), new TypeReference<>() {});
    //    }
    //
    //    // int, long or float??
    //    public static int getTimeGapLength() throws
    //            InterruptedException, ServerErrorException, IOException {
    //
    //        var response = getResponse("/getTimeGapLength",
    //                Constants.RESTRICTION_SERVER_URL);
    //        return objectMapper.readValue(response.body(), new TypeReference<>() {});
    //    }
    //
    //    public static LocalTime getStartTime() throws
    //            InterruptedException, ServerErrorException, IOException {
    //
    //        var response = getResponse("/getStartTime",
    //                Constants.RESTRICTION_SERVER_URL);
    //        return objectMapper.readValue(response.body(), new TypeReference<>() {});
    //    }
    //
    //    public static LocalTime getEndTime() throws
    //            InterruptedException, ServerErrorException, IOException {
    //
    //        var response = getResponse("/getEndTime",
    //                Constants.RESTRICTION_SERVER_URL);
    //        return objectMapper.readValue(response.body(), new TypeReference<>() {});
    //    }

    /**
     * This method mocks the getAllRoomsWithAdjustedCapacity endpoint from the restriction
     * management service.
     *
     * @return the rooms used for scheduling, after adjusting their capacity
     */
    public static List<Room> getAllRoomsWithAdjustedCapacity() {
        return Arrays.asList(new Room(1, "Class", 2),
        new Room(2, "IZ - 2", 20),
        new Room(3, "W - 2", 30),
        new Room(4, "IZ - 4", 15),
        new Room(5, "TS - 3", 25));
    }

    /**
     * This method mocks the getTimeGapLength endpoint from the restriction management service.
     *
     * @return the time gap that is to be placed between each two lectures
     */
    public static int getTimeGapLength() {
        return 45;
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


