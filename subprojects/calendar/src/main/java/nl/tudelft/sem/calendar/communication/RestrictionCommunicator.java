package nl.tudelft.sem.calendar.communication;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.util.List;
import nl.tudelft.sem.calendar.entities.Room;
import nl.tudelft.sem.calendar.exceptions.ServerErrorException;
import nl.tudelft.sem.calendar.util.Constants;
import org.springframework.stereotype.Service;


@Service
public class RestrictionCommunicator extends Communicator {

    /**
     * Returns all rooms with adjusted capacity.
     *
     * @return list of rooms
     * @throws IOException an input/output exception
     * @throws InterruptedException an interrupted exception
     * @throws ServerErrorException a server error exception
     */
    public List<Room> getAllRoomsWithAdjustedCapacity() throws

            InterruptedException, ServerErrorException, IOException {

        var response = getResponse("/restrictions/getAllRoomsWithAdjustedCapacity",
                Constants.RESTRICTION_SERVER_URL);

        return objectMapper.readValue(response.body(),
                new TypeReference<>() {});
    }

    /**
     * Returns the time gap length set.
     *
     * @return the time gap length in minutes
     * @throws IOException an input/output exception
     * @throws InterruptedException an interrupted exception
     * @throws ServerErrorException a server error exception
     */
    public int getTimeGapLength() throws
            InterruptedException, ServerErrorException, IOException {

        var response = getResponse("/restrictions/getTimeGapLength",
                Constants.RESTRICTION_SERVER_URL);
        return objectMapper.readValue(response.body(), new TypeReference<>() {
        });
    }

    /**
     * Returns the start time of university.
     *
     * @return the local start time
     * @throws IOException an input/output exception
     * @throws InterruptedException an interrupted exception
     * @throws ServerErrorException a server error exception
     */
    public int getStartTime() throws

            InterruptedException, ServerErrorException, IOException {

        var response = getResponse("/restrictions/getStartTime",
                Constants.RESTRICTION_SERVER_URL);
        return objectMapper.readValue(response.body(), new TypeReference<>() {
        });
    }

    /**
     * Returns the end time of university.
     *
     * @return the local end time
     * @throws IOException an input/output exception
     * @throws InterruptedException an interrupted exception
     * @throws ServerErrorException a server error exception
     */

    public int getEndTime() throws

            InterruptedException, ServerErrorException, IOException {

        var response = getResponse("/restrictions/getEndTime",
                Constants.RESTRICTION_SERVER_URL);
        return objectMapper.readValue(response.body(), new TypeReference<>() {
        });
    }
}