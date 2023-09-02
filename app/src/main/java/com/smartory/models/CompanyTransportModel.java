package com.smartory.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CompanyTransportModel {

    private int id_sql;
    public int pk;
    @SerializedName("party")
    public String party;
    public ArrayList<OperacionModel> operation;

    public ArrayList<OperacionModel> getOperation() {
        return operation;
    }

    public void setOperation(ArrayList<OperacionModel> operation) {
        this.operation = operation;
    }

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

    public String getName() {
        return party;
    }

    public void setName(String party) {
        this.party = party;
    }
}
