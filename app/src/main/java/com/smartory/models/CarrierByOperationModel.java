package com.smartory.models;

public class CarrierByOperationModel {
    private int id_sql;
    public int pk_company;
    public int pk_carrier;

    public int getId_sql() {
        return id_sql;
    }

    public void setId_sql(int id_sql) {
        this.id_sql = id_sql;
    }

    public int getPk_company() {
        return pk_company;
    }

    public void setPk_company(int pk_company) {
        this.pk_company = pk_company;
    }

    public int getPk_carrier() {
        return pk_carrier;
    }

    public void setPk_carrier(int pk_carrier) {
        this.pk_carrier = pk_carrier;
    }
}
