package com.smartory.models.operations;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverByOperation {
    @SerializedName("pk")
    @Expose
    private Integer pk;
    @SerializedName("driver")
    @Expose
    private Driver driver;
    @SerializedName("carrier")
    @Expose
    private Company carrier;
    @SerializedName("operation")
    @Expose
    private com.smartory.models.operations.operation operation;

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Company getCompany() {
        return carrier;
    }

    public void setCompany(Company company) {
        this.carrier = company;
    }

    public com.smartory.models.operations.operation getOperation() {
        return operation;
    }

    public void setOperation(com.smartory.models.operations.operation operation) {
        this.operation = operation;
    }
}
