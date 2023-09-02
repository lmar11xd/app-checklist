package com.smartory.models;

public class CarrierModel {
    public int id_sql;
    public int pk;
    public String party;
    public OperacionModel operation;
    public String pkCompany;
    public String pkOperation;
    public CompanyTransportModel company;

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

    public OperacionModel getOperation() {
        return operation;
    }

    public void setOperation(OperacionModel operation) {
        this.operation = operation;
    }

    public String getPkCompany() {
        return pkCompany;
    }

    public void setPkCompany(String pkCompany) {
        this.pkCompany = pkCompany;
    }

    public String getPkOperation() {
        return pkOperation;
    }

    public void setPkOperation(String pkOperation) {
        this.pkOperation = pkOperation;
    }

    public CompanyTransportModel getCompany() {
        return company;
    }

    public void setCompany(CompanyTransportModel company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "CarrierModel{" +
                "id_sql=" + id_sql +
                ", pk=" + pk +
                ", party='" + party + '\'' +
                ", operation=" + operation +
                ", pkCompany='" + pkCompany + '\'' +
                ", pkOperation='" + pkOperation + '\'' +
                ", company=" + company +
                '}';
    }
}
