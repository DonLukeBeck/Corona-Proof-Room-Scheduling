package nl.tudelft.sem.calendar.scheduling;

import java.time.LocalDate;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnCampusCandidate {

    private String netId;
    private LocalDate deadline;

    /**
     * Creates a candidate for the scheduling with and id and deadline. The latter is increased by
     * 14 days each time a candidate is scheduled for a lecture.
     *
     * @param netId    a string representing the netid of the candidate
     * @param deadline a localdate representing the date before which a students should have come to
     *                 campus students
     */
    public OnCampusCandidate(String netId, LocalDate deadline) {
        this.netId = netId;
        this.deadline = deadline;
    }

    /**
     * Checks for equality of a candidate.
     *
     * @param o the candidate to compare this with
     * @return a boolean indicating equality
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OnCampusCandidate)) {
            return false;
        }
        OnCampusCandidate that = (OnCampusCandidate) o;
        return this.getDeadline() == that.getDeadline()
                && getNetId().equals(that.getNetId());
    }

    /**
     * Hash function for the onCampusCandidate, used in the equals method.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(netId, deadline);
    }

}
