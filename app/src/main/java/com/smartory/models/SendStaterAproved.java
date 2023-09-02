package com.smartory.models;

public class SendStaterAproved extends SendState{
    //private String state;
    private String observation_lifting;

    public SendStaterAproved() {
        super();
    }

    public SendStaterAproved(String state, String rejection_comment) {
        super(state);
        //this.state = state;
        this.observation_lifting = rejection_comment;
    }

    /*public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }*/

    public String getObservation_lifting() {
        return observation_lifting;
    }

    public void setObservation_lifting(String rejection_comment) {
        this.observation_lifting = rejection_comment;
    }
}
