package com.smartory.models;

import android.graphics.Bitmap;

import okhttp3.MultipartBody;
import retrofit2.http.Multipart;

public class PostItemImageCheckList {
    private int idSql;
    private String label;
    private String checklist_section_item;
    private Bitmap image;
    private String comment;
    private String gps_longitude;
    private String gps_latitude;
    private String datetime;

    public PostItemImageCheckList() {
    }

    public PostItemImageCheckList(int idSql, String label, String checklist_section_item, Bitmap image, String comment, String gps_longitude, String gps_latitude, String datetime) {
        this.idSql = idSql;
        this.label = label;
        this.checklist_section_item = checklist_section_item;
        this.image = image;
        this.comment = comment;
        this.gps_longitude = gps_longitude;
        this.gps_latitude = gps_latitude;
        this.datetime = datetime;
    }

    public int getId() {
        return idSql;
    }

    public void setId(int idSql) {
        this.idSql = idSql;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCheck_list_item() {
        return checklist_section_item;
    }

    public void setCheck_list_item(String checklist_section_item) {
        this.checklist_section_item = checklist_section_item;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
}
