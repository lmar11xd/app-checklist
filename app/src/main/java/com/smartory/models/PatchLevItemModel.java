package com.smartory.models;

public class PatchLevItemModel {
    public String observation_lifting;
    public String observation_lifting_image;
    public String state;

    public String getObservation_lifting() {
        return observation_lifting;
    }

    public void setObservation_lifting(String observation_lifting) {
        this.observation_lifting = observation_lifting;
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

    @Override
    public String toString() {
        return "PatchLevItemModel{" +
                "observation_lifting='" + observation_lifting + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
