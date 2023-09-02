package com.smartory.models;

import java.util.Date;

public class APKVersionControl {
    private int id;
    private boolean is_active;
    private Date created;
    private Date updated;
    private String version;
    private String file;
    private Object description;
    private int created_by;
    private int updated_by;

    public APKVersionControl() {
    }

    public APKVersionControl(int id, boolean is_active, Date created, Date updated, String version, String file, Object description, int created_by, int updated_by) {
        this.id = id;
        this.is_active = is_active;
        this.created = created;
        this.updated = updated;
        this.version = version;
        this.file = file;
        this.description = description;
        this.created_by = created_by;
        this.updated_by = updated_by;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    public int getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(int updated_by) {
        this.updated_by = updated_by;
    }
}
