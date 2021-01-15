package nl.tudelft.sem.restrictions.controllers;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import nl.tudelft.sem.restrictions.Restriction;
import nl.tudelft.sem.restrictions.RestrictionRepository;
import nl.tudelft.sem.restrictions.Room;
import nl.tudelft.sem.restrictions.communication.RoomsCommunicator;
import nl.tudelft.sem.restrictions.communication.ServerErrorException;
import nl.tudelft.sem.shared.entity.IntValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController // This means that this class is a RestController
@RequestMapping(path = "/restrictions") // URL's start with /restrictions (after Application path)
public class RestrictionGettersController {

    @Autowired
    private transient RestrictionRepository restrictionRepository;

    @Autowired
    private transient RoomsCommunicator roomsCommunicator;

    private transient int cap;
    private transient int minSeatsBig;
    private transient int bigPercentage;
    private transient int smallPercentage;

    /**
     * Initializes the repository that is being used.
     *
     * @param restrictionRepository the repository that will be used
     */
    public RestrictionGettersController(RestrictionRepository restrictionRepository,
                                        RoomsCommunicator roomsCommunicator) {
        this.restrictionRepository = restrictionRepository;
        this.roomsCommunicator = roomsCommunicator;
    }

    /**
     * This function returns the value of a given restriction.
     *
     * @param name of the restriction
     * @return the value of the restriction or -999.9 if does not exists
     */
    public float getRestrictionVal(String name) {
        Optional<Restriction> r = restrictionRepository.findByName(name);
        if (r.isPresent()) {
            return r.get().getValue();
        }
        throw new NoSuchElementException("No Restriction Found, Need to Create One!");
    }

    /**
     * Returns the time gap between two lectures restriction.
     *
     * @return time gap as  a float
     */
    @GetMapping(path = "/getTimeGapLength") // Map ONLY POST Requests
    @ResponseBody
    public ResponseEntity<?> getTimeGapLength() {
        return ResponseEntity.ok(new IntValue((int) getRestrictionVal("gapTimeInMinutes")));
    }

    /**
     * Returns the start time of the university.
     *
     * @return start time as a localtime
     */
    @GetMapping(path = "/getStartTime") // Map ONLY POST Requests
    @ResponseBody
    public ResponseEntity<?> getStartTime() {
        return ResponseEntity.ok(new IntValue((int) getRestrictionVal("startTime")));
    }

    /**
     * Returns the end time of the university.
     *
     * @return end time as a localtime
     */
    @GetMapping(path = "/getEndTime") // Map ONLY POST Requests
    @ResponseBody
    public ResponseEntity<?> getEndTime() {
        return ResponseEntity.ok(new IntValue((int) getRestrictionVal("endTime")));
    }

    /**
     * Returns list of rooms with adjusted capacity.
     *
     * @return list of rooms
     */
    @GetMapping(path = "/getAllRoomsWithAdjustedCapacity") // Map ONLY POST Requests
    @ResponseBody
    public ResponseEntity<?> getAllRoomsWithAdjustedCapacity()
            throws InterruptedException, ServerErrorException, IOException {
        List<Room> it = roomsCommunicator.getAllRooms();
        return ResponseEntity.ok(adjustCapacitiesOfRooms(it));
    }

    List<Room> adjustCapacitiesOfRooms(List<Room> it) {
        updateConstants();
        for (Room r : it) {
            cap = r.getCapacity();
            if (cap >= minSeatsBig) {
                cap = ((cap / 100)
                        * bigPercentage);
            } else {
                cap = ((cap / 100)
                        * smallPercentage);
            }
            r.setCapacity(cap);
        }
        return it;
    }

    void updateConstants() {
        minSeatsBig = ((int) getRestrictionVal("minSeatsBig"));
        smallPercentage = ((int) getRestrictionVal("smallRoomMaxPercentage"));
        bigPercentage = ((int) getRestrictionVal("bigRoomMaxPercentage"));
    }
}

