package nl.tudelft.sem.calendar.entities;
import java.time.LocalDate;

public class OnCampusCandidate {

    private String netId;
    private LocalDate deadline;

    public OnCampusCandidate(String netId, LocalDate deadline){
        this.netId = netId;
        this.deadline = deadline;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof OnCampusCandidate)) {
            return false;
        }
        OnCampusCandidate that = (OnCampusCandidate) o;
        return getDeadline() == that.getDeadline() &&
                getNetId().equals(that.getNetId());
    }

    public String getNetId() {
        return netId;
    }

    public void setNetId(String netId) {
        this.netId = netId;
    }

    public LocalDate getDeadline() {
        return this.deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
}
