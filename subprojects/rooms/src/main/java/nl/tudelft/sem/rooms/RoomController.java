package nl.tudelft.sem.rooms;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import nl.tudelft.sem.shared.entity.StringMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
     * This function is used to create a new room.
     *
     * @param context the {@link RoomController.NewRoomContext} to create a new room
     * @return success or error message
     */
    @PostMapping(path = "/addNewRoom") // Map ONLY POST Requests
    @ResponseBody
    public ResponseEntity<?> addNewRoom(@RequestBody NewRoomContext context) {
        String name = context.getName();
        // `capacity` is being read in multiple places, and is not undefined after not being used
        @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
        int capacity = context.getCapacity();
        Optional<Room> r1 = roomRepository.findByName(name);
        if (r1.isPresent()) {
            return ResponseEntity.ok(new StringMessage("Already Exists"));
        }
        final int invCap = 1;
        if (capacity < invCap) {
            return ResponseEntity.ok(new StringMessage("Invalid capacity."));
        }
        Room r = new Room();
        r.setCapacity(capacity);
        r.setName(name);
        roomRepository.save(r);
        return ResponseEntity.ok(new StringMessage("Room added."));
    }

    @Data
    @AllArgsConstructor
    public static class NewRoomContext {
        String name;
        int capacity;
    }

    /**
     * This function deletes a room by its name.
     *
     * @param name of the room
     * @return success or error message
     */

    @DeleteMapping(path = "/deleteRoom") // Map ONLY POST Requests
    @ResponseBody
    public ResponseEntity<?> deleteRoom(@RequestParam String name) {
        Optional<Room> ro = roomRepository.findByName(name);
        if (ro.isPresent()) {
            roomRepository.deleteById(ro.get().getRoomId());
            return ResponseEntity.ok(new StringMessage("Deleted"));
        }
        return ResponseEntity.ok(new StringMessage("Room could not be found."));
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
        if (r != null) {
            return ResponseEntity.ok(new StringMessage(r.getName()));
        }
        return ResponseEntity.notFound().build();
    }

}