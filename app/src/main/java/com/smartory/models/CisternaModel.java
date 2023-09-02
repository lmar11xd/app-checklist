package com.smartory.models;

import com.google.gson.annotations.SerializedName;

public class CisternaModel {
    public int id_Sql;
    @SerializedName("pk")
    public int id;
    @SerializedName("name")
    public String plate_cisterns;

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

    public String getPlate_cisterns() {
        return plate_cisterns;
    }

    public void setPlate_cisterns(String plate_cisterns) {
        this.plate_cisterns = plate_cisterns;
    }
}
