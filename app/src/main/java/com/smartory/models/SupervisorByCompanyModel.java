package com.smartory.models;

public class SupervisorByCompanyModel {
    private int id_sql;
    public int pk_company;
    public int pk_supervisor;

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

    public int getPk_supervisor() {
        return pk_supervisor;
    }

    public void setPk_supervisor(int pk_supervisor) {
        this.pk_supervisor = pk_supervisor;
    }
}