package nl.tudelft.sem.calendar.entities;

public class OnCampusCandidate {

    private String netId;
    private int numParticipations;

    public OnCampusCandidate(String netId, int numParticipations){

        this.netId = netId;
        this.numParticipations = numParticipations;
    }

    public String getNetId() {
        return this.netId;
    }

    public int getNumParticipations(){
        return this.numParticipations;
    }
}
