package nl.tudelft.sem.rooms;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
     * This function is used to create a new room.
     *
     * @param name of the new room
     * @param capacity of the new room
     * @return success or error message
     */
    public String addNewRoom(String name, int capacity) {
        Optional<Room> r1 = roomRepository.findByName(name);
        if (r1.isPresent()) {
            return "Already Exists";
        }
        final int invCap = 1;
        if (capacity < invCap) {
            return "Invalid Capacity";
        }
        Room r = new Room();
        r.setCapacity(capacity);
        r.setName(name);
        roomRepository.save(r);
        return "Saved";
    }

    /**
     * This function deletes a room by its name.
     *
     * @param name of the room
     * @return success or error message
     */
    public String deleteRoom(String name) {
        Optional<Room> ro = roomRepository.findByName(name);
        if (ro.isPresent()) {
            roomRepository.deleteById(ro.get().getRoomId());
            return "Deleted";
        }
        return null;
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
    @PostMapping(path = "/getRoomName") // Map ONLY POST Requests
    @ResponseBody
    public ResponseEntity<?>  getRoomName(@RequestParam int roomId) {
        Room r = roomRepository.findById(roomId).orElse(null);
        if (r != null) {
            return ResponseEntity.ok(r.getName());
        }
        return ResponseEntity.notFound().build();
    }

}