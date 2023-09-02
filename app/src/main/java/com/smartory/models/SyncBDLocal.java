package com.smartory.models;

import android.content.Context;

import com.smartory.network.InterfaceAPI;


public class SyncBDLocal{
    private String token;
    private Context context;
    private InterfaceAPI api;
    private String[] params;

    public SyncBDLocal(InterfaceAPI api, String token, Context context, String... params) {
        this.token = token;
        this.context = context;
        this.api = api;
        this.params = params;
    }

    public InterfaceAPI getApi() {
        return api;
    }

    public void setApi(InterfaceAPI api) {
        this.api = api;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String[] getParams() {
        return params;
    }
    public void setParams(String... params) {
        this.params = params;
    }
}
