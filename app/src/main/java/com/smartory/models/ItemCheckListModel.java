package com.smartory.models;

import java.util.ArrayList;

public class ItemCheckListModel{
    private int idSql;
    private String type; //local-cloud
    //GET
    private int id; // mismo valor que check_list_item
    private SectionModel check_list_section; // mismo valor que check_list
    private String id_section;
    private String name;
    private String id_check_list;
    //POST
    public SectionModel section; // mismo valor que check_list

    private String due_date;
    private String observation_description;
    private String observation_lifting; //LEV.O
    private String observation_lifting_date; // F.LEV.O
    private String compliance_image; // foto cumplimiento
    private String non_compliance_image; //imagen de incumplimiento
    private String observation_lifting_image; // imagen de levantamiento de observaciones
    private String state;// C NC NA
    private int check_list;
    private int check_list_item;
    private ArrayList<String> operations;
    private String operations_text;
    private String editado; // si - no
    private boolean reviewed; //0:No revisado - 1:Revisado

    public String getEditado() {
        return editado;
    }

    public void setEditado(String editado) {
        this.editado = editado;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SectionModel getSection() {
        return section;
    }

    public void setSection(SectionModel section) {
        this.section = section;
    }

    public String getId_check_list() {
        return id_check_list;
    }

    public void setId_check_list(String id_check_list) {
        this.id_check_list = id_check_list;
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

    public SectionModel getCheck_list_section() {
        return check_list_section;
    }

    public void setCheck_list_section(SectionModel check_list_section) {
        this.check_list_section = check_list_section;
    }

    public String getId_section() {
        return id_section;
    }

    public void setId_section(String id_section) {
        this.id_section = id_section;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
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

    public String getObservation_lifting_date() {
        return observation_lifting_date;
    }

    public void setObservation_lifting_date(String observation_lifting_date) {
        this.observation_lifting_date = observation_lifting_date;
    }

    public String getCompliance_image() {
        return compliance_image;
    }

    public void setCompliance_image(String compliance_image) {
        this.compliance_image = compliance_image;
    }

    public String getNon_compliance_image() {
        return non_compliance_image;
    }

    public void setNon_compliance_image(String non_compliance_image) {
        this.non_compliance_image = non_compliance_image;
    }

    public String getObservation_lifting_image() {
        return observation_lifting_image;
    }

    public void setObservation_lifting_image(String observation_lifting_image) {
        this.observation_lifting_image = observation_lifting_image;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getCheck_list() {
        return check_list;
    }

    public void setCheck_list(int check_list) {
        this.check_list = check_list;
    }

    public int getCheck_list_item() {
        return check_list_item;
    }

    public void setCheck_list_item(int check_list_item) {
        this.check_list_item = check_list_item;
    }

    public ArrayList<String> getOperations() {
        return operations;
    }

    public void setOperations(ArrayList<String> operations) {
        this.operations = operations;
    }

    public String getOperations_text() {
        return operations_text;
    }

    public void setOperations_text(String operations_text) {
        this.operations_text = operations_text;
    }

    public boolean getReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

    @Override
    public String toString() {
        return "ItemCheckListModel{" +
                "idSql=" + idSql +
                ", type='" + type + '\'' +
                ", id=" + id +
                ", check_list_section=" + check_list_section +
                ", id_section='" + id_section + '\'' +
                ", name='" + name + '\'' +
                ", id_check_list='" + id_check_list + '\'' +
                ", section=" + section +
                ", due_date='" + due_date + '\'' +
                ", observation_description='" + observation_description + '\'' +
                ", observation_lifting='" + observation_lifting + '\'' +
                ", observation_lifting_date='" + observation_lifting_date + '\'' +
                ", compliance_image='" + compliance_image + '\'' +
                ", non_compliance_image='" + non_compliance_image + '\'' +
                ", observation_lifting_image='" + observation_lifting_image + '\'' +
                ", state='" + state + '\'' +
                ", check_list=" + check_list +
                ", check_list_item=" + check_list_item +
                ", editado='" + editado + '\'' +
                '}';
    }
}