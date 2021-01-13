package nl.tudelft.sem.restrictions;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import nl.tudelft.sem.restrictions.communication.Validate;
import nl.tudelft.sem.shared.Constants;
import nl.tudelft.sem.shared.entity.StringMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController // This means that this class is a RestController
@RequestMapping(path = "/restrictions") // URL's start with /restrictions (after Application path)
public class RestrictionSettersController {

    @Autowired
    private transient RestrictionRepository restrictionRepository;

    @Autowired
    private transient Validate validate;

    /**
     * Initializes the repository that is being used.
     *
     * @param restrictionRepository the repository that will be used
     */
    public RestrictionSettersController(RestrictionRepository restrictionRepository,
                                         Validate validate) {
        this.restrictionRepository = restrictionRepository;
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
                restrictionRepository.save(new Restriction(name, value));
                return new StringMessage("Updated");
            }
        }
        restrictionRepository.save(new Restriction(name, value));
        return new StringMessage("Saved");
    }

    /**
     * Checks if the validation is the teacher.
     *
     * @param request of the user
     * @return boolean indication of teacher
     */
    public boolean teacherValidate(HttpServletRequest request) {
        String validation = validate.validateRole(request, Constants.teacherRole);
        return validation.equals(Constants.noAccessMessage.getMessage());
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
                                            @RequestBody SetCapacityRestrictionContext context) {
        if (teacherValidate(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Constants.noAccessMessage);
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
                                            @RequestBody float numberOfSeats) {
        if (teacherValidate(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Constants.noAccessMessage);
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
                                              @RequestBody float gapTimeInMinutes) {
        if (teacherValidate(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Constants.noAccessMessage);
        }

        return ResponseEntity.ok(addNewRestriction("gapTimeInMinutes", gapTimeInMinutes));
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
                                          @RequestBody int startTime) {
        if (teacherValidate(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Constants.noAccessMessage);
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
                                        @RequestBody int endTime) {
        if (teacherValidate(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Constants.noAccessMessage);
        }

        return ResponseEntity.ok(addNewRestriction("endTime", (float) endTime));
    }
}

