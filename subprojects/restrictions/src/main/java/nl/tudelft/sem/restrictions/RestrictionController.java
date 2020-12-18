package nl.tudelft.sem.restrictions;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import nl.tudelft.sem.restrictions.communication.RoomsCommunicator;
import nl.tudelft.sem.restrictions.communication.ServerErrorException;
import nl.tudelft.sem.restrictions.communication.Validate;
import nl.tudelft.sem.shared.entity.IntValue;
import nl.tudelft.sem.shared.entity.StringMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController // This means that this class is a RestController
@RequestMapping(path = "/restrictions") // URL's start with /restrictions (after Application path)
public class RestrictionController {

    private transient String teacherRole = "teacher";
    private transient String studentRole = "student";
    private transient String noAccessMessage =
            "You are not allowed to view this page. Please contact administrator.";

    @Autowired
    private transient RestrictionRepository restrictionRepository;

    @Autowired
    private transient RoomsCommunicator roomsCommunicator;

    @Autowired
    private transient Validate validate;

    /**
     * Initializes the repository that is being used.
     *
     * @param restrictionRepository the repository that will be used
     */
    public RestrictionController(RestrictionRepository restrictionRepository,
                                 RoomsCommunicator roomsCommunicator,
                                 Validate validate) {
        this.restrictionRepository = restrictionRepository;
        this.roomsCommunicator = roomsCommunicator;
        this.validate = validate;
    }

    /**
     * Creates a new restriction or updates it.
     *
     * @param name of the restriction
     * @param value of the restriction (float)
     * @return message containing the action that has been done
     */
    public StringMessage addNewRestriction(String name, float value) {
        Optional<Restriction> r = restrictionRepository.findByName(name);
        if (r.isPresent()) {
            if (r.get().getValue() == value) {
                return new StringMessage("Already Exists");
            } else {
                restrictionRepository.delete(r.get());
                Restriction rn = new Restriction();
                rn.setName(name);
                rn.setValue(value);
                restrictionRepository.save(rn);
                return new StringMessage("Updated");
            }
        }
        Restriction rn = new Restriction();
        rn.setName(name);
        rn.setValue(value);
        restrictionRepository.save(rn);
        return new StringMessage("Saved");
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
     * This function sets the capacity restrictions for big and small rooms.
     *
     * @param context the {@link SetCapacityRestrictionContext} to create the restriction
     * @return a string containing the success or error message
     */
    @PostMapping(path = "/setCapacityRestriction") // Map ONLY POST Requests
    @ResponseBody
    public ResponseEntity<?> setCapacityRestriction(HttpServletRequest request,
                                                @RequestBody SetCapacityRestrictionContext context)
            throws IOException, InterruptedException {
        String validation = validate.validateRole(request, teacherRole);
        if (validation.equals(noAccessMessage)) {
            return ResponseEntity.ok(new StringMessage(noAccessMessage));
        }
        if (context.isBigRoom()) {
            return ResponseEntity.ok(
                    addNewRestriction("bigRoomMaxPercentage", context.getMaxPercentageAllowed()));
        } else {
            return ResponseEntity.ok(
                    addNewRestriction("smallRoomMaxPercentage", context.getMaxPercentageAllowed()));
        }
    }

    @Data
    @AllArgsConstructor
    public static class SetCapacityRestrictionContext {
        boolean isBigRoom;
        float maxPercentageAllowed;
    }

    /**
     * Sets the minumum number of seats of a room to be considered big.
     *
     * @param numberOfSeats as a float
     * @return a string containing the success or error message
     */
    @PostMapping(path = "/setMinSeatsBig") // Map ONLY POST Requests
    @ResponseBody
    public ResponseEntity<?> setMinSeatsBig(HttpServletRequest request,
                                            @RequestBody float numberOfSeats) throws IOException,
                                            InterruptedException {
        String validation = validate.validateRole(request, teacherRole);
        if (validation.equals(noAccessMessage)) {
            return ResponseEntity.ok(new StringMessage(noAccessMessage));
        }
        return ResponseEntity.ok(addNewRestriction("minSeatsBig", numberOfSeats));
    }

    /**
     * Sets the gap time between two lectures restriction.
     *
     * @param gapTimeInMinutes between two lectures
     * @return a string containing the success or error message
     */
    @PostMapping(path = "/setTimeGapLength") // Map ONLY POST Requests
    @ResponseBody
    public ResponseEntity<?> setTimeGapLength(HttpServletRequest request,
                                              @RequestBody float gapTimeInMinutes) throws IOException,
                                              InterruptedException {
        String validation = validate.validateRole(request, teacherRole);
        if (validation.equals(noAccessMessage)) {
            return ResponseEntity.ok(new StringMessage(noAccessMessage));
        }

        return ResponseEntity.ok(addNewRestriction("gapTimeInMinutes", gapTimeInMinutes));
    }

    /**
     * Returns the big or small room max percentage.
     *
     * @param bigOrSmallRoom boolean representing if the parameter is for big (1) or small (0) rooms
     * @return a string containing the success or error message
     */
    public IntValue getCapacityRestriction(boolean bigOrSmallRoom) {
        if (bigOrSmallRoom) {
            return new IntValue((int) getRestrictionVal("bigRoomMaxPercentage"));
        } else {
            return new IntValue((int) getRestrictionVal("smallRoomMaxPercentage"));
        }
    }

    /**
     * Returns the minimum seats to be considered a big room.
     *
     * @return float representing minimum seat number
     */
    public int getMinSeatsBig() {
        return (int) getRestrictionVal("minSeatsBig");
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
     * Sets the start time of the university.
     *
     * @param startTime the amount of seconds since midnight
     * @return a string containing the success or error message
     */
    @PostMapping(path = "/setStartTime") // Map ONLY POST Requests
    @ResponseBody
    public ResponseEntity<?> setStartTime(HttpServletRequest request,
                                          @RequestBody int startTime)
            throws IOException, InterruptedException {
        String validation = validate.validateRole(request, teacherRole);
        if (validation.equals(noAccessMessage)) {
            return ResponseEntity.ok(new StringMessage(noAccessMessage));
        }
        return ResponseEntity.ok(addNewRestriction("startTime", (float) startTime));
    }

    /**
     * Sets the end time of the university.
     *
     * @param endTime the amount of seconds since midnight
     * @return a string containing the success or error message
     */
    @PostMapping(path = "/setEndTime") // Map ONLY POST Requests
    @ResponseBody
    public ResponseEntity<?> setEndTime(HttpServletRequest request,
                                        @RequestBody int endTime) throws IOException,
                                        InterruptedException {
        String validation = validate.validateRole(request, teacherRole);
        if (validation.equals(noAccessMessage)) {
            return ResponseEntity.ok(new StringMessage(noAccessMessage));
        }

        return ResponseEntity.ok(addNewRestriction("endTime", (float) endTime));
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
        for (Room r : it) {
            int cap = r.getCapacity();
            if (cap >= getMinSeatsBig()) {
                r.setCapacity((int) (cap * (getCapacityRestriction(true).getValue() / 100.0)));
            } else {
                r.setCapacity((int) (cap * (getCapacityRestriction(false).getValue() / 100.0)));
            }
        }
        return ResponseEntity.ok(it);
    }
}

