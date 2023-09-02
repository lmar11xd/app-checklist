package com.smartory.models;

import com.google.gson.annotations.SerializedName;

public class OperacionModel {
    public int id_sql;
    @SerializedName("pk")
    public int pk;
    public String state;
    public String name;
    public String operation_id;

    public OperacionModel(){}

    public int getId_sql() {
        return id_sql;
    }

    public void setId_sql(int id_sql) {
        this.id_sql = id_sql;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getState() { return state; }

    public void setState(String state) { this.state = state; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "OperacionModel{" +
                "id_sql=" + id_sql +
                ", pk=" + pk +
                ", state='" + state + '\'' +
                ", name='" + name + '\'' +
                ", operation_id='" + operation_id + '\'' +
                '}';
    }
}
