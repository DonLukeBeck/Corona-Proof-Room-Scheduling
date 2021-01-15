package nl.tudelft.sem.restrictions.controllers;

import javax.servlet.http.HttpServletRequest;
import nl.tudelft.sem.restrictions.RestrictionRepository;
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

    private static ResponseEntity<?> forbidden = ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(Constants.noAccessMessage);

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
     * Add the restriction to database.
     *
     * @param name of restriction
     * @param value of restriction
     * @return string message
     */
    private StringMessage addRestriction(String name, float value) {
        return RestrictionCapacityController.addRestriction(name, value, restrictionRepository);
    }

    /**
     * Checks if the validation is the teacher.
     *
     * @param request of the user
     * @return boolean indication of teacher
     */
    private boolean teacherValidate(HttpServletRequest request) {
        String validation = validate.validateRole(request, Constants.teacherRole);
        return validation.equals(Constants.noAccessMessage.getMessage());
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
            return forbidden;
        }
        return ResponseEntity.ok(addRestriction("minSeatsBig", numberOfSeats));
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
            return forbidden;
        }

        return ResponseEntity.ok(addRestriction("gapTimeInMinutes", gapTimeInMinutes));
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
            return forbidden;
        }
        return ResponseEntity.ok(addRestriction("startTime", (float) startTime));
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
            return forbidden;
        }

        return ResponseEntity.ok(addRestriction("endTime", (float) endTime));
    }
}

