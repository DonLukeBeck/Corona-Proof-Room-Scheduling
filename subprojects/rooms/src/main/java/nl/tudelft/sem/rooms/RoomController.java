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

    /**
     * This function is used to create a new room.
     *
     * @param name of the new room
     * @param capacity of the new room
     * @return success or error message
     */
    public String addNewRoom(String name, int capacity) {
        for (Room ro : roomRepository.findAll()) {
            if (ro.getName().equals(name)) {
                return "Already Exists";
            }
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
        for (Room ro : roomRepository.findAll()) {
            if (ro.getName().equals(name)) {
                roomRepository.deleteById(ro.getRoomId());
                return "Deleted";
            }
        }
        return "DNE";
    }

    /**
     * This function gets all rooms.
     *
     * @return iterable containing all rooms
     */
    @PostMapping(path = "/getAllRooms") // Map ONLY POST Requests
    public Iterable<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    /**
     * Returns the room by its id.
     *
     * @param roomId of the room
     * @return room entity
     */
    @PostMapping(path = "/getRoom") // Map ONLY POST Requests
    public Room getRoom(@RequestParam int roomId) {
        return roomRepository.findById(roomId).orElse(null);
    }

}