package nl.tudelft.sem.rooms;

import nl.tudelft.sem.shared.entity.StringMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // This means that this class is a RestController
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

    /**
     * This function gets all rooms.
     *
     * @return iterable containing all rooms
     */
    @GetMapping(path = "/getAllRooms") // Map ONLY POST Requests
    @ResponseBody
    public ResponseEntity<?> getAllRooms() {
        return ResponseEntity.ok(roomRepository.findAll());
    }

    /**
     * Returns the room name by its id.
     *
     * @param roomId of the room
     * @return room name
     */
    @GetMapping(path = "/getRoomName") // Map ONLY POST Requests
    @ResponseBody
    public ResponseEntity<?> getRoomName(@RequestParam int roomId) {
        Room r = roomRepository.findById(roomId).orElse(null);
        if (r == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new StringMessage(r.getName()));
    }

}