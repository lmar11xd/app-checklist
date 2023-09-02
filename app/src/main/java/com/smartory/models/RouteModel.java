package com.smartory.models;

public class RouteModel {

    private int id_sql;
    public int id_api;
    public String name_route;
    public int pk_operation;

    public int getId_sql() {
        return id_sql;
    }

    public void setId_sql(int id_sql) {
        this.id_sql = id_sql;
    }

    public int getId_api() {
        return id_api;
    }

    public void setId_api(int id_api) {
        this.id_api = id_api;
    }

    public String getName_route() {
        return name_route;
    }

    public void setName_route(String name_route) {
        this.name_route = name_route;
    }

    public int getPk_operation() {
        return pk_operation;
    }

    public void setPk_operation(int pk_operation) {
        this.pk_operation = pk_operation;
    }
}
