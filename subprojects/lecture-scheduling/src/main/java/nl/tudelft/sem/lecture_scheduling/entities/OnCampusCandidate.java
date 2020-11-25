package nl.tudelft.sem.lecture_scheduling.entities;

public class OnCampusCandidate {

    private String netID;
    private int numParticipations;

    public OnCampusCandidate(String netID, int numParticipations){

        this.netID = netID;
        this.numParticipations = numParticipations;
    }

    public String getNetID() {
        return this.netID;
    }

    public int getNumParticipations(){
        return this.numParticipations;
    }
}
