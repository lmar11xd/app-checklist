package com.smartory.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TransportUnitModel {
    private int id_Sql;
    public int id;
    public ArrayList<CisternaModel> cisterns;
    public ArrayList<TractoModel> tractos;
    public String plate_cistern;
    public String plate_tracto;
    public OperacionModel operation;
    @SerializedName("pk")
    public String pk_operation;
    public String pk_company;
    public boolean is_active;
    public String created;
    public String updated;
    public CompanyTransportModel company;

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

    public ArrayList<CisternaModel> getCisterns() {
        return cisterns;
    }

    public void setCisterns(ArrayList<CisternaModel> cisterns) {
        this.cisterns = cisterns;
    }

    public ArrayList<TractoModel> getTractos() {
        return tractos;
    }

    public void setTractos(ArrayList<TractoModel> tractos) {
        this.tractos = tractos;
    }

    public String getPlate_cistern() {
        return plate_cistern;
    }

    public void setPlate_cistern(String plate_cistern) {
        this.plate_cistern = plate_cistern;
    }

    public String getPlate_tracto() {
        return plate_tracto;
    }

    public void setPlate_tracto(String plate_tracto) {
        this.plate_tracto = plate_tracto;
    }

    public OperacionModel getOperation() {
        return operation;
    }

    public void setOperation(OperacionModel operation) {
        this.operation = operation;
    }

    public String getPk_operation() {
        return pk_operation;
    }

    public void setPk_operation(String pk_operation) {
        this.pk_operation = pk_operation;
    }

    public String getPk_company() {
        return pk_company;
    }

    public void setPk_company(String pk_company) {
        this.pk_company = pk_company;
    }

    public CompanyTransportModel getCompany() {
        return company;
    }

    public void setCompany(CompanyTransportModel company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "TransportUnitModel{" +
                "id_Sql=" + id_Sql +
                ", id=" + id +
                ", cisterns=" + cisterns +
                ", tractos=" + tractos +
                ", plate_cistern='" + plate_cistern + '\'' +
                ", plate_tracto='" + plate_tracto + '\'' +
                ", operation=" + operation +
                ", pk_operation='" + pk_operation + '\'' +
                ", pk_company='" + pk_company + '\'' +
                ", is_active=" + is_active +
                ", created='" + created + '\'' +
                ", updated='" + updated + '\'' +
                ", company=" + company +
                '}';
    }
}
