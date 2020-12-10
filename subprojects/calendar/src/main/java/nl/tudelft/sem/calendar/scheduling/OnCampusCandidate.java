package nl.tudelft.sem.calendar.scheduling;

import java.time.LocalDate;
import java.util.Objects;

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
     * hash function for the onCampusCandidate
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(netId, deadline);
    }

    /**
     * Returns the associated netId of this student.
     *
     * @return a string indicating the netid
     */
    public String getNetId() {
        return netId;
    }

    /**
     * Sets the associated netId of this student.
     *
     * @param netId a string indicating the netid to be set
     */
    public void setNetId(String netId) {
        this.netId = netId;
    }

    /**
     * Returns the deadline of this student.
     *
     * @return a localdate indicating indicating the latest date at which
     *                  a student should come to campus
     */
    public LocalDate getDeadline() {
        return this.deadline;
    }

    /**
     * Sets the deadline of this student.
     *
     * @param deadline a localdate indicating the latest date at which a student should come to
     *                 campus
     */
    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
}
