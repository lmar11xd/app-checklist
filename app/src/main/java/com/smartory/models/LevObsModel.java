package com.smartory.models;

public class LevObsModel {
    private int idSql;
    public int pk;
    public int id_item;
    public String check_list;
    public String check_list_date;
    public String transport_company;
    public String transport_unit;
    public String section;
    public String check_list_item;
    public String severity;
    public String observation_description;
    public String Observation_lifting;
    public String non_compliance_image; //imagen de incumplimiento
    public String due_date;
    public String state;

    public int getIdSql() {
        return idSql;
    }

    public void setIdSql(int idSql) {
        this.idSql = idSql;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public int getId_item() {
        return id_item;
    }

    public void setId_item(int id_item) {
        this.id_item = id_item;
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

    public String getTransport_unit() {
        return transport_unit;
    }

    public void setTransport_unit(String transport_unit) {
        this.transport_unit = transport_unit;
    }

    public String getTransport_company() {
        return transport_company;
    }

    public void setTransport_company(String transport_company) {
        this.transport_company = transport_company;
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

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getObservation_description() {
        return observation_description;
    }

    public void setObservation_description(String observation_description) {
        this.observation_description = observation_description;
    }

    public String getObservation_lifting() {
        return Observation_lifting;
    }

    public void setObservation_lifting(String observation_lifting) {
        Observation_lifting = observation_lifting;
    }

    public String getNon_compliance_image() {
        return non_compliance_image;
    }

    public void setNon_compliance_image(String non_compliance_image) {
        this.non_compliance_image = non_compliance_image;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getState() { return state; }

    public void setState(String state) { this.state = state; }

    @Override
    public String toString() {
        return "LevObsModel{" +
                "idSql=" + idSql +
                ", pk=" + pk +
                ", id_item=" + id_item +
                ", check_list='" + check_list + '\'' +
                ", check_list_date='" + check_list_date + '\'' +
                ", transport_company='" + transport_company + '\'' +
                ", transport_unit='" + transport_unit + '\'' +
                ", section='" + section + '\'' +
                ", check_list_item='" + check_list_item + '\'' +
                ", severity='" + severity + '\'' +
                ", observation_description='" + observation_description + '\'' +
                ", Observation_lifting='" + Observation_lifting + '\'' +
                //", non_compliance_image='" + non_compliance_image + '\'' +
                ", due_date='" + due_date + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
