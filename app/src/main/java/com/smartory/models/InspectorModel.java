package com.smartory.models;

import java.util.ArrayList;

public class InspectorModel {
    public int id_sql;
    public int pk;
    public String party;
    public OperacionModel operation;
    public CompanyTransportModel company;
    public String pkCompany;
    public String pkOperation;
    public String name_company;

    public OperacionModel getOperation() {
        return operation;
    }

    public void setOperation(OperacionModel operation) {
        this.operation = operation;
    }

    public String getPkCompany() { return pkCompany; }

    public void setPkCompany(String pkCompany) { this.pkCompany = pkCompany; }

    public String getPkOperation() {
        return pkOperation;
    }

    public void setPkOperation(String pkOperation) {
        this.pkOperation = pkOperation;
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


    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public CompanyTransportModel getCompany() {
        return company;
    }

    public void setCompany(CompanyTransportModel company) {
        this.company = company;
    }

    public String getName_company() {
        return name_company;
    }

    public void setName_company(String name_company) {
        this.name_company = name_company;
    }

    @Override
    public String toString() {
        return "InspectorModel{" +
                "id_sql=" + id_sql +
                ", pk=" + pk +
                ", party='" + party + '\'' +
                ", operation=" + operation +
                ", company=" + company +
                ", pkCompany='" + pkCompany + '\'' +
                ", pkOperation='" + pkOperation + '\'' +
                ", name_company='" + name_company + '\'' +
                '}';
    }
}
