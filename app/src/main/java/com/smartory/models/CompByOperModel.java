package com.smartory.models;

public class CompByOperModel {
    private int id_sql;
    public int pk_company;
    public int pk_operation;

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

    public int getPk_operation() {
        return pk_operation;
    }

    public void setPk_operation(int pk_operation) {
        this.pk_operation = pk_operation;
    }
}
