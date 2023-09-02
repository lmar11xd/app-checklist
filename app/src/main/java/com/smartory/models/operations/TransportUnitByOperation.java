package com.smartory.models.operations;

import com.google.gson.annotations.SerializedName;
import com.smartory.models.CisternaModel;
import com.smartory.models.CompanyTransportModel;
import com.smartory.models.OperacionModel;
import com.smartory.models.TractoModel;
import com.smartory.models.TransportUnitModel;

import java.util.ArrayList;

public class TransportUnitByOperation {
    public int pk;
    public OperationInTransporUnitModel operation;
    public TransportUnitModel transport_unit;
    public String transport_unit_company_name;
    public int transport_unit_company_id;

    @Override
    public String toString() {
        return "TransportUnitByOperation{" +
                "pk=" + pk +
                ", operation=" + operation +
                ", transport_unit=" + transport_unit +
                ", transport_unit_company_name='" + transport_unit_company_name + '\'' +
                ", transport_unit_company_id=" + transport_unit_company_id +
                '}';
    }
}
