package com.smartory.models;

public class Party {
    public Party(){}
    public int id;
    public String name;
    public CompanyTransportModel company;
    public OperacionModel operation;

    public OperacionModel getOperation() { return operation; }

    public void setOperation(OperacionModel operation) { this.operation = operation; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CompanyTransportModel getCompany() {
        return company;
    }

    public void setCompany(CompanyTransportModel company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
