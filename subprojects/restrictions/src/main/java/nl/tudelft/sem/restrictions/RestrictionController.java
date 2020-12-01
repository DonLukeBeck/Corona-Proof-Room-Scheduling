package nl.tudelft.sem.restrictions;

import java.time.LocalTime;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController // This means that this class is a RestController
@RequestMapping(path = "/restrictions") // URL's start with /restrictions (after Application path)
public class RestrictionController {

    @Autowired
    private transient RestrictionRepository restrictionRepository;

    /**
     * Instantiates repository needed.
     */
    public RestrictionController(RestrictionRepository restrictionRepository) {
        this.restrictionRepository = restrictionRepository;
    }

    /**
     * Adds a new restrictions with provided parameters.
     */
    public String addNewRestriction(String name, float value) {
        for (Restriction r : restrictionRepository.findAll()) {
            if (r.getName().equals(name)) {
                if (r.getValue() == value) {
                    return "Already Exists";
                } else {
                    r.setValue(value);
                    restrictionRepository.save(r);
                    return "Updated";
                }
            }
        }
        Restriction rn = new Restriction();
        rn.setName(name);
        rn.setValue(value);
        restrictionRepository.save(rn);
        return "Saved";
    }

    /**
     * Fetches restrictions value with given name form the database.
     */
    public float getRestrictionVal(String name) {
        for (Restriction r : restrictionRepository.findAll()) {
            if (r.getName().equals(name)) {
                return r.getValue();
            }
        }
        throw new NoSuchElementException("No Restriction Found, Need to Create One!");
    }

    /**
     * This function sets the capacity restrictions for big and small rooms.
     *
     * @param bigOrSmallRoom boolean representing if the parameter is for big (1) or small (0) rooms
     * @param maxPercentageAllowed max percentage allowed to be used in a room
     * @return a string containing the success or error message
     */
    @PostMapping(path = "/setCapacityRestriction") // Map ONLY POST Requests
    public String setCapacityRestriction(@RequestParam boolean bigOrSmallRoom,
                                         @RequestParam float maxPercentageAllowed) {
        if (bigOrSmallRoom) {
            return addNewRestriction("bigRoomMaxPercentage", maxPercentageAllowed);
        } else {
            return addNewRestriction("smallRoomMaxPercentage", maxPercentageAllowed);
        }
    }

    /**
     * Sets the minumum number of seats of a room to be considered big.
     *
     * @param numberOfSeats as a float
     * @return a string containing the success or error message
     */
    @PostMapping(path = "/setMinSeatsBig") // Map ONLY POST Requests
    public String setMinSeatsBig(@RequestParam float numberOfSeats) {
        return addNewRestriction("minSeatsBig", numberOfSeats);
    }

    /**
     * Sets the gap time between two lectures restriction.
     *
     * @param gapTimeInMinutes between two lectures
     * @return a string containing the success or error message
     */
    @PostMapping(path = "/setTimeGapLength") // Map ONLY POST Requests
    public String setTimeGapLength(@RequestParam float gapTimeInMinutes) {
        return addNewRestriction("gapTimeInMinutes", gapTimeInMinutes);
    }

    /**
     * Returns the big or small room max percentage.
     *
     * @param bigOrSmallRoom boolean representing if the parameter is for big (1) or small (0) rooms
     * @return a string containing the success or error message
     */
    @PostMapping(path = "/getCapacityRestriction") // Map ONLY POST Requests
    public float getCapacityRestriction(@RequestParam boolean bigOrSmallRoom) {
        if (bigOrSmallRoom) {
            return getRestrictionVal("bigRoomMaxPercentage");
        } else {
            return getRestrictionVal("smallRoomMaxPercentage");
        }
    }

    /**
     * Returns the minimum seats to be considered a big room.
     *
     * @return float representing minimum seat number
     */
    @GetMapping(path = "/getMinSeatsBig") // Map ONLY POST Requests
    public float getMinSeatsBig() {
        return getRestrictionVal("minSeatsBig");
    }

    /**
     * Returns the time gap between two lectures restriction.
     *
     * @return time gap as  a float
     */
    @GetMapping(path = "/getTimeGapLength") // Map ONLY POST Requests
    public float getTimeGapLength() {
        return getRestrictionVal("gapTimeInMinutes");
    }

    /**
     * Sets the start time of the university.
     *
     * @param startTime as a localtime
     * @return a string containing the success or error message
     */
    @PostMapping(path = "/setStartTime") // Map ONLY POST Requests
    public String setStartTime(@RequestParam LocalTime startTime) {
        return addNewRestriction("startTime", (float) startTime.toSecondOfDay());
    }

    /**
     * Sets the end time of the university.
     *
     * @param endTime as a localtime
     * @return a string containing the success or error message
     */
    @PostMapping(path = "/setEndTime") // Map ONLY POST Requests
    public String setEndTime(@RequestParam LocalTime endTime) {
        return addNewRestriction("endTime", (float) endTime.toSecondOfDay());
    }

    /**
     * Returns the start time of the university.
     *
     * @return start time as a localtime
     */
    @GetMapping(path = "/getStartTime") // Map ONLY POST Requests
    public LocalTime getStartTime() {
        return LocalTime.ofSecondOfDay((int) getRestrictionVal("startTime"));
    }

    /**
     * Returns the end time of the university.
     *
     * @return end time as a localtime
     */
    @GetMapping(path = "/getEndTime") // Map ONLY POST Requests
    public LocalTime getEndTime() {
        return LocalTime.ofSecondOfDay((int) getRestrictionVal("endTime"));
    }

    /*
    @GetMapping(path = "/getAllRoomsWithAdjustedCapacity") // Map ONLY POST Requests
    public Iterable<Room> getAllRoomsWithAdjustedCapacity() {
        Iterable<Room> it = RoomController.getAllRooms();
        for (Room r : it) {
            int cap = r.getCapacity();
            if (cap >= getMinSeatsBig()) {
                r.setCapacity(cap*(getCapacityRestriction(true)/100));
            } else {
                r.setCapacity(cap*(getCapacityRestriction(false)/100));
            }
        }
        return -10000;
    }

    @GetMapping(path = "/getRoomWithAdjustedCapacity") // Map ONLY POST Requests
    Room getRoomWithAdjustedCapacity(int roomId) {
        Room r = RoomController.getRoom(roomId);
        int cap = r.getCapacity();
        if (cap >= getMinSeatsBig()) {
            r.setCapacity(cap*(getCapacityRestriction(true)/100));
        } else {
            r.setCapacity(cap*(getCapacityRestriction(false)/100));
        }
        return r;
    }
    */


}
