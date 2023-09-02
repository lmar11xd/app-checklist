package com.smartory.models;

public class CredentialsPinCode {
    public String pin;

    public CredentialsPinCode(String pin) {
        this.pin = pin;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    @Override
    public String toString() {
        return "CredentialsPinCode{" +
                "pin='" + pin + '\'' +
                '}';
    }
}
