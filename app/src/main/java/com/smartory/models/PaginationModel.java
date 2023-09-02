package com.smartory.models;

import java.util.ArrayList;

public class PaginationModel {
    private int count;
    private String url;
    private ArrayList<GetCheckListModel> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<GetCheckListModel> getResults() {
        return results;
    }

    public void setResults(ArrayList<GetCheckListModel> results) {
        this.results = results;
    }
}
