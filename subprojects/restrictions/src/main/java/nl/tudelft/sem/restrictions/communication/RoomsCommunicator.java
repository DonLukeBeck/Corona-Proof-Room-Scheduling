package nl.tudelft.sem.restrictions.communication;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import nl.tudelft.sem.restrictions.Room;

public class RoomsCommunicator extends Communicator {

    /**
     * Returns all rooms.
     *
     * @return list of rooms.
     * @throws IOException an input/output exception
     * @throws InterruptedException an interrupted exception
     * @throws ServerErrorException a server error exception
     */
    public static Iterable<Room> getAllRooms() throws
            InterruptedException, ServerErrorException, IOException {

        var response = getResponse("/getAllRooms",
                Constants.ROOMS_SERVER_URL);
        return objectMapper.readValue(response.body(), new TypeReference<Iterable<Room>>() {
        });
    }
}
