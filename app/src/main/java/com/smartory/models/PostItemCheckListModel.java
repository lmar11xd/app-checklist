package com.smartory.models;

public class PostItemCheckListModel {
    public int id;
    public int id_sql;
    private String due_date;
    public String observation_description;
    private String observation_lifting;
    private String observation_lifting_date;
    private String compliance_image;
    private String non_compliance_image;
    private String observation_lifting_image;
    private String state;
    private int check_list;
    private int check_list_item;

    public PostItemCheckListModel(){};

    public PostItemCheckListModel(String due_date, String observation_description, String observation_lifting, String observation_lifting_date, String compliance_image, String non_compliance_image, String observation_lifting_image, String state, int check_list, int check_list_item) {
        this.due_date = due_date;
        this.observation_description = observation_description;
        this.observation_lifting = observation_lifting;
        this.observation_lifting_date = observation_lifting_date;
        this.compliance_image = compliance_image;
        this.non_compliance_image = non_compliance_image;
        this.observation_lifting_image = observation_lifting_image;
        this.state = state;
        this.check_list = check_list;
        this.check_list_item = check_list_item;
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

    @Override
    public String toString() {
        return "PostItemCheckListModel{" +
                "due_date='" + due_date + '\'' +
                ", observation_description='" + observation_description + '\'' +
                ", observation_lifting='" + observation_lifting + '\'' +
                ", observation_lifting_date='" + observation_lifting_date + '\'' +
                ", compliance_image='" + compliance_image + '\'' +
                ", non_compliance_image='" + non_compliance_image + '\'' +
                ", observation_lifting_image='" + observation_lifting_image + '\'' +
                ", state='" + state + '\'' +
                ", check_list=" + check_list +
                ", check_list_item=" + check_list_item +
                '}';
    }
}
