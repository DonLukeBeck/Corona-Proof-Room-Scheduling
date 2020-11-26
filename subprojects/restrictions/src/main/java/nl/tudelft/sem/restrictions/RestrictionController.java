package nl.tudelft.sem.restrictions;

import org.springframework.beans.factory.annotation.Autowired;
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
        return -10000;
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
     * Fetches restrictions value with given name form the database.
     */
    @PostMapping(path = "/restrictionByName")
    public @ResponseBody
    float getRestrictionl(@RequestParam String name) {
        for (Restriction r : restrictionRepository.findAll()) {
            if (r.getName().equals(name)) {
                return r.getValue();
            }
        }
        return -10000;
    }

    //    List<Room> getAllRoomsWithAdjustedCapacity()
    //    setMinSeatsBig(int numberOfSeats)
    //    setTimeGapLength(int gapTimeInMinutes)
    //    int getTimeGapLength()
    //    setStartTime(LocalTime startTime)
    //    setEndTime(LocalTime endTime)
    //    int getStartTime()
    //    int getEndTime()


}
