package nl.tudelft.sem.rooms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController // This means that this class is a RestController
@RequestMapping(path = "/rooms") // URL's start with /restrictions (after Application path)
public class RoomController {

    @Autowired
    private transient RoomRepository roomRepository;

    /**
     * Initializes the repository that is being used.
     *
     * @param roomRepository the repository that will be used
     */
    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }
}

    //CRUD
    //List<Room> getAllRooms()
    //Room getRoom(int roomId)