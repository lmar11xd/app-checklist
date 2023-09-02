package com.smartory.models;

public class ResponseImageItemChecklist {
    private String label;
    private int checklist_section_item;
    private String image;
    private String comment;
    private int pk;
    private int non_compliance;
    private int non_compliance_lifting;
    private String state_lifting;

    public ResponseImageItemChecklist() {
    }

    public ResponseImageItemChecklist(String label, int checklist_section_item, String image, String comment) {
        this.label = label;
        this.checklist_section_item = checklist_section_item;
        this.image = image;
        this.comment = comment;
    }

    public ResponseImageItemChecklist(String label, int checklist_section_item, String image, String comment, int pk, int non_compliance, int non_compliance_lifting, String state_lifting) {
        this.label = label;
        this.checklist_section_item = checklist_section_item;
        this.image = image;
        this.comment = comment;
        this.pk = pk;
        this.non_compliance = non_compliance;
        this.non_compliance_lifting = non_compliance_lifting;
        this.state_lifting = state_lifting;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public int getNon_compliance() {
        return non_compliance;
    }

    public void setNon_compliance(int non_compliance) {
        this.non_compliance = non_compliance;
    }

    public int getNon_compliance_lifting() {
        return non_compliance_lifting;
    }

    public void setNon_compliance_lifting(int non_compliance_lifting) {
        this.non_compliance_lifting = non_compliance_lifting;
    }

    public String getState_lifting() {
        return state_lifting;
    }

    public void setState_lifting(String state_lifting) {
        this.state_lifting = state_lifting;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getChecklist_section_item() {
        return checklist_section_item;
    }

    public void setChecklist_section_item(int checklist_section_item) {
        this.checklist_section_item = checklist_section_item;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
