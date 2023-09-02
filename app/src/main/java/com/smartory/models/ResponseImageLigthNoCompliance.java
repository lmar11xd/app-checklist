package com.smartory.models;

public class ResponseImageLigthNoCompliance {
    private String image;
    private String pk;

    public ResponseImageLigthNoCompliance() {
    }

    public ResponseImageLigthNoCompliance(String image, String pk) {
        this.image = image;
        this.pk = pk;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }
}
