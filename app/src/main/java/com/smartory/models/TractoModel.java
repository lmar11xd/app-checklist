package com.smartory.models;

import com.google.gson.annotations.SerializedName;

public class TractoModel {
    public int id_Sql;
    @SerializedName("pk")
    public int id;
    @SerializedName("name")
    public String plate_tractos;
    private String last_inspection;
    private String year_fabrication;

    public TractoModel(){}
    public TractoModel(int id_Sql, int id, String plate_tracto, String year_fabrication) {
        this.id_Sql = id_Sql;
        this.id = id;
        this.plate_tractos = plate_tracto;
        this.year_fabrication = year_fabrication;
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

    public String getPlate_tractos() {
        return plate_tractos;
    }

    public void setPlate_tractos(String plate_tractos) {
        this.plate_tractos = plate_tractos;
    }

    public String getLastChecklist() {
        return last_inspection;
    }

    public void setLastChecklist(String last_inspection) {
        this.last_inspection = last_inspection;
    }

    public String getYearFabrication() {
        return year_fabrication;
    }

    public void setYearFabrication(String year_fabrication) {
        this.year_fabrication = year_fabrication;
    }
}
