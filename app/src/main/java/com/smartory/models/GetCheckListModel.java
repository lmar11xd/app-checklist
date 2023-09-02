package com.smartory.models;

import com.smartory.models.operations.Route;

public class GetCheckListModel {
    private int idSql;
    public int id;
    public String number;
    public String type_check;
    public String datetime;
    public Route route;
    public DriverModel driver;
    public InspectorModel inspector;
    public SupervisorModel supervisor;
    public CompanyTransportModel company_shipment;
    public TransportUnitPlatesModel transport_unit;
    public OperacionModel operation;
    public LocationModel location;
    public String tracto_image;
    public String cistern_image;
    public String mileage;
    public String state;
    public String name_location;
    public String last_inspection;
    public String year_fabrication;
    public int progress;

    public String getName_location() {
        return name_location;
    }

    public void setName_location(String name_location) {
        this.name_location = name_location;
    }

    public LocationModel getLocation() {
        return location;
    }

    public void setLocation(LocationModel location) {
        this.location = location;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getState() { return state; }

    public void setState(String state) { this.state = state; }

    public CompanyTransportModel getCompany_shipment() {
        return company_shipment;
    }

    public void setCompany_shipment(CompanyTransportModel company_shipment) {
        this.company_shipment = company_shipment;
    }

    public TransportUnitPlatesModel getTransport_unit() {
        return transport_unit;
    }

    public void setTransport_unit(TransportUnitPlatesModel transport_unit) {
        this.transport_unit = transport_unit;
    }

    public DriverModel getDriver() {
        return driver;
    }

    public void setDriver(DriverModel driver) {
        this.driver = driver;
    }

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType_check() {
        return type_check;
    }

    public void setType_check(String type_check) {
        this.type_check = type_check;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public InspectorModel getInspector() {
        return inspector;
    }

    public void setInspector(InspectorModel inspector) {
        this.inspector = inspector;
    }

    public SupervisorModel getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(SupervisorModel supervisor) {
        this.supervisor = supervisor;
    }

    public OperacionModel getOperation() {
        return operation;
    }

    public void setOperation(OperacionModel operation) {
        this.operation = operation;
    }

    public String getTracto_image() { return tracto_image; }

    public void setTracto_image(String tracto_image) { this.tracto_image = tracto_image; }

    public String getCistern_image() { return cistern_image; }

    public void setCistern_image(String cistern_image) { this.cistern_image = cistern_image; }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public String getLastInspection() {
        return last_inspection;
    }

    public void setLastInspection(String last_inspection) {
        this.last_inspection = last_inspection;
    }

    public String getYearFabrication() {
        return year_fabrication;
    }

    public void setYearFabrication(String manufactoringYear) {
        this.year_fabrication = manufactoringYear;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
