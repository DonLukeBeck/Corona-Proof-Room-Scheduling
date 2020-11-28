package nl.tudelft.sem.calendar.entities;

import java.util.Objects;

public class OnCampusCandidate {

    private String netId;
    private int numParticipations;

    public OnCampusCandidate(String netId, int numParticipations){
        this.netId = netId;
        this.numParticipations = numParticipations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof OnCampusCandidate)) {
            return false;
        }
        OnCampusCandidate that = (OnCampusCandidate) o;
        return getNumParticipations() == that.getNumParticipations() &&
                getNetId().equals(that.getNetId());
    }

    public String getNetId() {
        return netId;
    }

    public void setNetId(String netId) {
        this.netId = netId;
    }

    public int getNumParticipations() {
        return numParticipations;
    }

    public void setNumParticipations(int numParticipations) {
        this.numParticipations = numParticipations;
    }
}
