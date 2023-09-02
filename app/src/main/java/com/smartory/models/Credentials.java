package com.smartory.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Credentials {
    public String token;
    private String app_version;

    public String username;

    public String password;

    public String type;
    public UserModel user;

    public ArrayList<String> user_group;
    public ArrayList<String> user_permissions;
    public int user_id;
    public boolean is_tfa_active;

    public Credentials(String username, String password, String app_version) {
        this.username = username;
        this.password = password;
        this.app_version = app_version;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public boolean isIs_tfa_active() {
        return is_tfa_active;
    }

    public void setIs_tfa_active(boolean is_tfa_active) {
        this.is_tfa_active = is_tfa_active;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public ArrayList<String> getUser_group() {
        return user_group;
    }

    public void setUser_group(ArrayList<String> user_group) {
        this.user_group = user_group;
    }

    public ArrayList<String> getUser_permissions() {
        return user_permissions;
    }

    public void setUser_permissions(ArrayList<String> user_permissions) {
        this.user_permissions = user_permissions;
    }

    @Override
    public String toString() {
        return "Credentials{" +
                "token='" + token + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", type='" + type + '\'' +
                ", user=" + user +
                ", user_group=" + user_group +
                ", user_permissions=" + user_permissions +
                ", user_id=" + user_id +
                ", is_tfa_active=" + is_tfa_active +
                '}';
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }
}
