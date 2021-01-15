package nl.tudelft.sem.restrictions.controllers;

import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import nl.tudelft.sem.restrictions.Restriction;
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
public class RestrictionCapacityController {

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
    public RestrictionCapacityController(RestrictionRepository restrictionRepository,
                                        Validate validate) {
        this.restrictionRepository = restrictionRepository;
        this.validate = validate;
    }

    /**
     * Add the restriction to database.
     *
     * @param name of restriction
     * @param value of restriction
     * @param restrictionRepository the database repository
     * @return string message
     */
    public static StringMessage addRestriction(String name, float value,
                                               RestrictionRepository restrictionRepository) {
        Restriction r = restrictionRepository.findByName(name).orElse(null);
        if (r != null && r.getValue() == value) {
            return new StringMessage("Already Exists");
        }
        if (r != null) {
            restrictionRepository.delete(r);
        }
        restrictionRepository.save(new Restriction(name, value));
        return new StringMessage("Saved");
    }

    @Data
    @AllArgsConstructor
    public static class SetCapacityRestrictionContext {
        boolean isBigRoom;
        float maxPercentageAllowed;
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
                                                    @RequestBody
                                                            SetCapacityRestrictionContext context) {
        String validation = validate.validateRole(request,
                Constants.teacherRole);
        if (validation.equals(Constants.noAccessMessage.getMessage())) {
            return forbidden;
        }
        if (context.isBigRoom()) {
            return ResponseEntity.ok(
                    addRestriction("bigRoomMaxPercentage", context.getMaxPercentageAllowed(),
                            restrictionRepository));
        } else {
            return ResponseEntity.ok(
                    addRestriction("smallRoomMaxPercentage", context.getMaxPercentageAllowed(),
                            restrictionRepository));
        }
    }
}
