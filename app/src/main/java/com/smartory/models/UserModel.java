package com.smartory.models;

import com.smartory.models.operations.Company;

public class UserModel {
    public String email;
    public String employee_name;
    public String first_name;
    public int id;
    public Boolean is_staff;
    public Boolean is_superuser;
    public Boolean is_tfa_active;
    public String last_name;
    public String username;
    public CompanyAuth company;

    public CompanyAuth getCompany() {
        return company;
    }

    public void setCompany(CompanyAuth company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getIs_staff() {
        return is_staff;
    }

    public void setIs_staff(Boolean is_staff) {
        this.is_staff = is_staff;
    }

    public Boolean getIs_superuser() {
        return is_superuser;
    }

    public void setIs_superuser(Boolean is_superuser) {
        this.is_superuser = is_superuser;
    }

    public Boolean getIs_tfa_active() {
        return is_tfa_active;
    }

    public void setIs_tfa_active(Boolean is_tfa_active) {
        this.is_tfa_active = is_tfa_active;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "email='" + email + '\'' +
                ", employee_name='" + employee_name + '\'' +
                ", first_name='" + first_name + '\'' +
                ", id=" + id +
                ", is_staff=" + is_staff +
                ", is_superuser=" + is_superuser +
                ", is_tfa_active=" + is_tfa_active +
                ", last_name='" + last_name + '\'' +
                ", username='" + username + '\'' +
                ", company=" + company +
                '}';
    }
}
