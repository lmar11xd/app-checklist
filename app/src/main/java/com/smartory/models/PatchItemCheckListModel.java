package com.smartory.models;

public class PatchItemCheckListModel {
    public String observation_description;
    private String observation_lifting; //LEV.O
    private String compliance_image; // foto cumplimiento
    private String non_compliance_image; //imagen de incumplimiento
    private String observation_lifting_image; // imagen de levantamiento de observaciones
    private String state;// C NC NA
    private boolean reviewed;

    public PatchItemCheckListModel(){};

    public PatchItemCheckListModel(String observation_description, String observation_lifting, String compliance_image, String non_compliance_image, String observation_lifting_image, String state, boolean reviewed) {
        this.observation_description = observation_description;
        this.observation_lifting = observation_lifting;
        this.compliance_image = compliance_image;
        this.non_compliance_image = non_compliance_image;
        this.observation_lifting_image = observation_lifting_image;
        this.state = state;
        this.reviewed = reviewed;
    }

    public String getObservation_description() {
        return observation_description;
    }

    public void setObservation_description(String observation_description) {
        this.observation_description = observation_description;
    }

    public String getObservation_lifting() {
        return observation_lifting;
    }

    public void setObservation_lifting(String observation_lifting) {
        this.observation_lifting = observation_lifting;
    }


    public String getCompliance_image() {
        return compliance_image;
    }

    public void setCompliance_image(String compliance_image) {
        this.compliance_image = compliance_image;
    }

    public String getNon_compliance_image() {
        return non_compliance_image;
    }

    public void setNon_compliance_image(String non_compliance_image) {
        this.non_compliance_image = non_compliance_image;
    }

    public String getObservation_lifting_image() {
        return observation_lifting_image;
    }

    public void setObservation_lifting_image(String observation_lifting_image) {
        this.observation_lifting_image = observation_lifting_image;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean getReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

    @Override
    public String toString() {
        return "PatchItemCheckListModel{" +
                ", observation_description='" + observation_description + '\'' +
                ", observation_lifting='" + observation_lifting + '\'' +
                ", compliance_image='" + compliance_image + '\'' +
                ", non_compliance_image='" + non_compliance_image + '\'' +
                ", observation_lifting_image='" + observation_lifting_image + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
