package com.smartory.models;

public class ItemLevObsModel {
    private int idSql;
    public int id;
    public String check_list;
    public String check_list_date;
    public String transport_company;
    public String transport_unit;
    public String section;
    public String check_list_item;
    public String observation_description;
    public String observation_lifting;
    public String severity;
    public String due_date;

    public int getIdSql() {
        return idSql;
    }

    public void setIdSql(int idSql) {
        this.idSql = idSql;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCheck_list() {
        return check_list;
    }

    public void setCheck_list(String check_list) {
        this.check_list = check_list;
    }

    public String getCheck_list_date() {
        return check_list_date;
    }

    public void setCheck_list_date(String check_list_date) {
        this.check_list_date = check_list_date;
    }

    public String getTransport_company() {
        return transport_company;
    }

    public void setTransport_company(String transport_company) {
        this.transport_company = transport_company;
    }

    public String getTransport_unit() {
        return transport_unit;
    }

    public void setTransport_unit(String transport_unit) {
        this.transport_unit = transport_unit;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getCheck_list_item() {
        return check_list_item;
    }

    public void setCheck_list_item(String check_list_item) {
        this.check_list_item = check_list_item;
    }

    public String getObservation_description() {
        return observation_description;
    }

    public void setObservation_description(String observation_description) {
        this.observation_description = observation_description;
    }

    public String getObservation_lifting() {
        return observation_lifting;
    }

    public void setObservation_lifting(String observation_lifting) {
        this.observation_lifting = observation_lifting;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }
}