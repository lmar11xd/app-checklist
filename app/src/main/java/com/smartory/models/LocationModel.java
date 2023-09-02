package com.smartory.models;

import com.google.gson.annotations.SerializedName;

public class LocationModel {

    private int id_Sql;
    @SerializedName("pk")
    private int id;
    @SerializedName("name")
    private String name;
    public Double latitude;
    public Double longitude;
    public Double altitude;
    public OperacionModel operation;
    public Integer pk_operation;

    public LocationModel(){}
    public LocationModel(int id, String name, Double latitude, Double longitude, Double altitude, Integer pk_operation) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.pk_operation = pk_operation;
    }

    public int getId_Sql() {
        return id_Sql;
    }

    public void setId_Sql(int id_Sql) {
        this.id_Sql = id_Sql;
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public OperacionModel getOperation() {
        return operation;
    }

    public void setOperation(OperacionModel operation) {
        this.operation = operation;
    }

    public Integer getPk_operation() {
        return pk_operation;
    }

    public void setPk_operation(Integer pk_operation) {
        this.pk_operation = pk_operation;
    }
}