package com.smartory.models.operations;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Route {
    @SerializedName("pk")
    @Expose
    private Integer pk;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("operation")
    @Expose
    private com.smartory.models.operations.operation operation;

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public com.smartory.models.operations.operation getOperation() {
        return operation;
    }

    public void setOperation(com.smartory.models.operations.operation operation) {
        this.operation = operation;
    }
}