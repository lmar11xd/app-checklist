package com.smartory.models;

public class SendStaterRejected extends SendState{
    //private String state;
    private String rejection_comment;

    public SendStaterRejected() {
        super();
    }

    public SendStaterRejected(String state, String rejection_comment) {
        super(state);
        //this.state = state;
        this.rejection_comment = rejection_comment;
    }

    /*public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }*/

    public String getRejection_comment() {
        return rejection_comment;
    }

    public void setRejection_comment(String rejection_comment) {
        this.rejection_comment = rejection_comment;
    }
}
