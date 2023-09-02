package com.smartory.models;

public class PatchValidarModel {

    public String supervisor_signature;
    public String state;
    public String getSupervisor_signature() {
        return supervisor_signature;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setSupervisor_signature(String supervisor_signature) {
        this.supervisor_signature = supervisor_signature;
    }

    @Override
    public String toString() {
        return "PatchValidarModel{" +
                "supervisor_signature='" + supervisor_signature + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
