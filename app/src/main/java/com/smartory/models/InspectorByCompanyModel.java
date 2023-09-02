package com.smartory.models;

public class InspectorByCompanyModel {
    private int id_sql;
    public int pk_company;
    public int pk_inspector;

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

    public int getPk_inspector() {
        return pk_inspector;
    }

    public void setPk_inspector(int pk_inspector) {
        this.pk_inspector = pk_inspector;
    }
}
