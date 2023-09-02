package com.smartory.models.operations;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SupervisorByOperation {
    @SerializedName("pk")
    @Expose
    private Integer pk;
    @SerializedName("supervisor")
    @Expose
    private Supervisor supervisor;
    @SerializedName("company")
    @Expose
    private Company company;
    @SerializedName("operation")
    @Expose
    private com.smartory.models.operations.operation operation;

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public Supervisor getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public com.smartory.models.operations.operation getOperation() {
        return operation;
    }

    public void setOperation(com.smartory.models.operations.operation operation) {
        this.operation = operation;
    }
}
