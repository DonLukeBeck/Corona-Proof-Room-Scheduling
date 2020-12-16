package nl.tudelft.sem.calendar.communication;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import nl.tudelft.sem.calendar.exceptions.ServerErrorException;
import nl.tudelft.sem.calendar.util.Constants;
import org.springframework.stereotype.Service;

@Service
public class RoomManagementCommunicator extends Communicator {

    /**
     * Returns the name of the room with given id.
     *
     * @param roomId of the room
     * @return name of the room
     * @throws IOException an input/output exception
     * @throws InterruptedException an interrupted exception
     * @throws ServerErrorException a server error exception
     */
    public String getRoomName(int roomId) throws
            InterruptedException, ServerErrorException, IOException {

        var response = getResponse("/rooms/getRoomName" + roomId,
                Constants.ROOMS_SERVER_URL);
        return objectMapper.readValue(response.body(), new TypeReference<String>() {
        });
    }


}