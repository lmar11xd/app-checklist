package com.smartory.models;

import androidx.annotation.NonNull;

public class CheckListModel {
    private int idSql;
    public int id;
    public String type;
    public String number;
    private String type_check;
    private String datetime;
    private String mileage;
    private String driver_evaluation_1;
    private String driver_evaluation_2;
    private String driver_signature;
    private String supervisor_signature;
    private String state;
    private String enviado;
    private String plate_tracto;
    private String plate_cistern;
    private String tracto_image;
    private String cistern_image;
    private String latitude;
    private String longitude;
    private String unit_configuration;
    public int route;
    public String name_route;

    public int driver;
    public String name_driver;

    public int inspector;
    public String name_inspector;


    public int supervisor;
    public String name_supervisor;

    private int company_shipment;
    private String name_company_shipment;


    public int transport_unit;
    public String name_transport_unit;

    public Integer location;
    public String name_location;
    public String id_location_sql;

    public int operation;
    public String name_operation;
    public int operation_id_check;

    public String last_inspection;
    public String year_fabrication;
    public int progress;

    public String getId_location_sql() {
        return id_location_sql;
    }

    public void setId_location_sql(String id_location_sql) {
        this.id_location_sql = id_location_sql;
    }

    public String getEnviado() { return enviado; }

    public void setEnviado(String enviado) { this.enviado = enviado; }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName_transport_unit() {
        return name_transport_unit;
    }

    public int getCompany_shipment() {
        return company_shipment;
    }

    public void setCompany_shipment(int company_shipment) {
        this.company_shipment = company_shipment;
    }

    public String getName_company_shipment() {
        return name_company_shipment;
    }

    public void setName_company_shipment(String name_company_shipment) {
        this.name_company_shipment = name_company_shipment;
    }

    public void setName_transport_unit(String name_transport_unit) {
        this.name_transport_unit = name_transport_unit;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public String getName_operation() {
        return name_operation;
    }

    public void setName_operation(String name_operation) {
        this.name_operation = name_operation;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getDriver_evaluation_1() {
        return driver_evaluation_1;
    }

    public void setDriver_evaluation_1(String driver_evaluation_1) {
        this.driver_evaluation_1 = driver_evaluation_1;
    }

    public String getDriver_evaluation_2() {
        return driver_evaluation_2;
    }

    public void setDriver_evaluation_2(String driver_evaluation_2) {
        this.driver_evaluation_2 = driver_evaluation_2;
    }

    public String getDriver_signature() {
        return driver_signature;
    }

    public void setDriver_signature(String driver_signature) {
        this.driver_signature = driver_signature;
    }

    public String getSupervisor_signature() {
        return supervisor_signature;
    }

    public void setSupervisor_signature(String supervisor_signature) {
        this.supervisor_signature = supervisor_signature;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getDriver() {
        return driver;
    }

    public void setDriver(int driver) {
        this.driver = driver;
    }

    public int getInspector() {
        return inspector;
    }

    public void setInspector(int inspector) {
        this.inspector = inspector;
    }

    public int getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(int supervisor) {
        this.supervisor = supervisor;
    }

    public int getTransport_unit() {
        return transport_unit;
    }

    public void setTransport_unit(int transport_unit) {
        this.transport_unit = transport_unit;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public String getName_driver() {
        return name_driver;
    }

    public void setName_driver(String name_driver) {
        this.name_driver = name_driver;
    }

    public String getName_inspector() {
        return name_inspector;
    }

    public void setName_inspector(String name_inspector) {
        this.name_inspector = name_inspector;
    }

    public String getName_supervisor() {
        return name_supervisor;
    }

    public void setName_supervisor(String name_supervisor) { this.name_supervisor = name_supervisor; }

    public String getName_location() { return name_location; }

    public void setName_location(String name_location) { this.name_location = name_location; }

    public String getPlate_tracto() { return plate_tracto; }

    public void setPlate_tracto(String plate_tracto) { this.plate_tracto = plate_tracto;   }

    public String getPlate_cistern() { return plate_cistern; }

    public void setPlate_cistern(String plate_cistern) { this.plate_cistern = plate_cistern; }

    public String getTracto_image() {
        return tracto_image;
    }

    public void setTracto_image(String tracto_image) {
        this.tracto_image = tracto_image;
    }

    public String getCistern_image() {
        return cistern_image;
    }

    public void setCistern_image(String cistern_image) {
        this.cistern_image = cistern_image;
    }

    public String getLatitude() { return latitude; }

    public void setLatitude(String latitude) { this.latitude = latitude; }

    public String getLongitude() { return longitude; }

    public int getRoute() {
        return route;
    }

    public void setRoute(int route) {
        this.route = route;
    }

    public String getName_route() {
        return name_route;
    }

    public void setName_route(String name_route) {
        this.name_route = name_route;
    }

    public String getUnit_configuration() {
        return unit_configuration;
    }

    public void setUnit_configuration(String unit_configuration) {
        this.unit_configuration = unit_configuration;
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

    public void setYearFabrication(String year_fabrication) {
        this.year_fabrication = year_fabrication;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @NonNull
    @Override
    public String toString() {
        return "CheckListModel{" +
                "idSql=" + idSql +
                ", id=" + id +
                ", type='" + type + '\'' +
                ", number='" + number + '\'' +
                ", type_check='" + type_check + '\'' +
                ", datetime='" + datetime + '\'' +
                ", mileage='" + mileage + '\'' +
                ", driver_evaluation_1='" + driver_evaluation_1 + '\'' +
                ", driver_evaluation_2='" + driver_evaluation_2 + '\'' +
                // ", driver_signature='" + driver_signature + '\'' +
                // ", supervisor_signature='" + supervisor_signature + '\'' +
                ", state='" + state + '\'' +
                ", enviado='" + enviado + '\'' +
                ", plate_tracto='" + plate_tracto + '\'' +
                ", plate_cistern='" + plate_cistern + '\'' +
                // ", tracto_image='" + tracto_image + '\'' +
                // ", cistern_image='" + cistern_image + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", route=" + route +
                ", name_route='" + name_route + '\'' +
                ", driver=" + driver +
                ", name_driver='" + name_driver + '\'' +
                ", inspector=" + inspector +
                ", name_inspector='" + name_inspector + '\'' +
                ", supervisor=" + supervisor +
                ", name_supervisor='" + name_supervisor + '\'' +
                ", company_shipment=" + company_shipment +
                ", name_company_shipment='" + name_company_shipment + '\'' +
                ", transport_unit=" + transport_unit +
                ", name_transport_unit='" + name_transport_unit + '\'' +
                ", location=" + location +
                ", name_location='" + name_location + '\'' +
                ", id_location_sql='" + id_location_sql + '\'' +
                ", operation=" + operation +
                ", name_operation='" + name_operation + '\'' +
                '}';
    }

    public void setLongitude(String longitude) { this.longitude = longitude; }
}
