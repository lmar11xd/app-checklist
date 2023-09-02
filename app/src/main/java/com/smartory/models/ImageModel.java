package com.smartory.models;
public class ImageModel {

    private int id;
    private String name;
    private String documentIdApi;
    private String state;
    private String type;
    private String owner;
    private String gps_longitude;
    private String gps_latitude;
    private String datetime;
    private String label;
    private String image_url;
    private String comment;

    public ImageModel(){}

    public ImageModel(
            int id,
            String name,
            String documentIdApi,
            String state,
            String type,
            String owner,
            String gps_longitude,
            String gps_latitude,
            String datetime,
            String label,
            String image_url,
            String comment
    ) {
        this.id = id;
        this.name = name;
        this.documentIdApi = documentIdApi;
        this.state = state;
        this.type = type;
        this.owner = owner;
        this.gps_longitude = gps_longitude;
        this.gps_latitude = gps_latitude;
        this.datetime = datetime;
        this.label = label;
        this.image_url = image_url;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocumentIdApi() {
        return documentIdApi;
    }

    public void setDocumentIdApi(String documentIdApi) {
        this.documentIdApi = documentIdApi;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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
}
