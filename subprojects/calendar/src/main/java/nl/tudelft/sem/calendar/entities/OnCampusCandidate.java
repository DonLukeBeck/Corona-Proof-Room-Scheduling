package nl.tudelft.sem.calendar.entities;

public class OnCampusCandidate {

    private String netId;
    private int numParticipations;

    public OnCampusCandidate(String netId, int numParticipations){
        this.netId = netId;
        this.numParticipations = numParticipations;
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
