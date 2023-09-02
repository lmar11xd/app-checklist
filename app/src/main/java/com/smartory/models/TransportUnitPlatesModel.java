package com.smartory.models;

import java.util.ArrayList;

public class TransportUnitPlatesModel {

    private int id_Sql;
    public int id;
    public ArrayList<String> cisterns;
    public ArrayList<String> tractos;

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

    public ArrayList<String> getCisterns() {
        return cisterns;
    }

    public void setCisterns(ArrayList<String> cisterns) {
        this.cisterns = cisterns;
    }

    public ArrayList<String> getTractos() {
        return tractos;
    }

    public void setTractos(ArrayList<String> tractos) {
        this.tractos = tractos;
    }
}
