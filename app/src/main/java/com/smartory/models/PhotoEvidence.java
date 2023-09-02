package com.smartory.models;

import android.graphics.Bitmap;

public class PhotoEvidence {
    private Bitmap image;
    private String label;
    private String image_url;
    private String comment;
    private String document;
    private String gps_longitude;
    private String gps_latitude;
    private String datetime;
    private String typePhoto;
    private String state;
    private String idSql;

    public PhotoEvidence(Bitmap image, String label) {
        this.image = image;
        this.label = label;
    }

    public PhotoEvidence(Bitmap image, String label, String image_url, String comment, String document, String gps_longitude, String gps_latitude, String datetime, String typePhoto) {
        this.image = image;
        this.label = label;
        this.image_url = image_url;
        this.comment = comment;
        this.typePhoto = typePhoto;
        this.document = document;
        this.gps_longitude = gps_longitude;
        this.gps_latitude = gps_latitude;
        this.datetime = datetime;
        this.state = "local";
    }

    public String getIdSql() {
        return idSql;
    }

    public void setIdSql(String idSql) {
        this.idSql = idSql;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getGps_longitude() {
        return gps_longitude;
    }

    public void setGps_longitude(String gps_longitude) {
        this.gps_longitude = gps_longitude;
    }

    public String getGps_latitude() {
        return gps_latitude;
    }

    public void setGps_latitude(String gps_latitude) {
        this.gps_latitude = gps_latitude;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getTypePhoto() {
        return typePhoto;
    }

    public void setTypePhoto(String typePhoto) {
        this.typePhoto = typePhoto;
    }

    @Override
    public String toString() {
        return "PhotoEvidence{" +
                "image=" + image +
                ", label='" + label + '\'' +
                ", image_url='" + image_url + '\'' +
                ", comment='" + comment + '\'' +
                ", document='" + document + '\'' +
                ", gps_longitude='" + gps_longitude + '\'' +
                ", gps_latitude='" + gps_latitude + '\'' +
                ", datetime='" + datetime + '\'' +
                ", typePhoto='" + typePhoto + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
