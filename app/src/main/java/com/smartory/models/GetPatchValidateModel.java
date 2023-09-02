package com.smartory.models;

public class GetPatchValidateModel {
    public int id;
    public String is_active;
    public String created;
    public String number;
    public String type_check;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
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

    @Override
    public String toString() {
        return "GetPatchValidateModel{" +
                "id=" + id +
                ", is_active='" + is_active + '\'' +
                ", created='" + created + '\'' +
                ", number='" + number + '\'' +
                ", type_check='" + type_check + '\'' +
                '}';
    }
}
