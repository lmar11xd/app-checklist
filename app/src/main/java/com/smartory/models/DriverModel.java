package com.smartory.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DriverModel {
    private int id_sql;
    public int id;// es el ide dentro de party_driver

    @SerializedName("name")
    public String name_driver;

    public String pk_operation;
    public String pk_company;
    public Party party_driver;

    public int getId_sql() {
        return id_sql;
    }

    public void setId_sql(int id_sql) {
        this.id_sql = id_sql;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName_driver() {
        return name_driver;
    }

    public void setName_driver(String name_driver) {
        this.name_driver = name_driver;
    }

    public String getPk_operation() {
        return pk_operation;
    }

    public void setPk_operation(String pk_operation) {
        this.pk_operation = pk_operation;
    }

    public String getPk_company() {
        return pk_company;
    }

    public void setPk_company(String pk_company) {
        this.pk_company = pk_company;
    }

    public Party getParty_driver() {
        return party_driver;
    }

    public void setParty_driver(Party party_driver) {
        this.party_driver = party_driver;
    }

}
