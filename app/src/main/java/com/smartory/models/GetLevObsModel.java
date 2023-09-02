package com.smartory.models;

public class GetLevObsModel {
    private int idSql;
    public int pk;
    public ItemLevObsModel item;
    public String state;


    public int getIdSql() {
        return idSql;
    }

    public void setIdSql(int idSql) {
        this.idSql = idSql;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public ItemLevObsModel getItem() {
        return item;
    }

    public void setItem(ItemLevObsModel item) {
        this.item = item;
    }

    public String getState() { return state; }

    public void setState(String state) { this.state = state; }
}
