package com.smartory.models;

public class NonComplianceLifting {
    private String state;
    private String observation_lifting;
    private String rejection_comment;
    private String non_compliance;

    public NonComplianceLifting() {
    }

    public NonComplianceLifting(String state, String observation_lifting, String rejection_comment, String non_compliance) {
        this.state = state;
        this.observation_lifting = observation_lifting;
        this.rejection_comment = rejection_comment;
        this.non_compliance = non_compliance;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getObservation_lifting() {
        return observation_lifting;
    }

    public void setObservation_lifting(String observation_lifting) {
        this.observation_lifting = observation_lifting;
    }

    public String getRejection_comment() {
        return rejection_comment;
    }

    public void setRejection_comment(String rejection_comment) {
        this.rejection_comment = rejection_comment;
    }

    public String getNon_compliance() {
        return non_compliance;
    }

    public void setNon_compliance(String non_compliance) {
        this.non_compliance = non_compliance;
    }
}
