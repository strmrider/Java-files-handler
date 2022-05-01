package com.company.tests.filehandler.Models;

import org.json.simple.JSONObject;
import java.nio.file.attribute.FileTime;

public class SysItem {
    private String name;
    private String path;
    private FileTime creation;
    private FileTime modified;
    private FileTime accessed;
    private final boolean hidden;
    protected FileItemType type;

    public SysItem(String name, String path, FileTime creation, FileTime modified, FileTime accessed, boolean hidden){
        this.name = name;
        this.path = path;
        this.creation = creation;
        this.modified = modified;
        this.accessed = accessed;
        this.hidden = hidden;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public boolean isHidden(){
        return hidden;
    }

    public FileTime getAccessed() {
        return accessed;
    }

    public FileTime getCreation() {
        return creation;
    }

    public FileTime getModified() {
        return modified;
    }

    public FileItemType getType() {
        return type;
    }

    public void setAccessed(FileTime accessed) {
        this.accessed = accessed;
    }

    public void setModified(FileTime modified) {
        this.modified = modified;
    }

    public void setCreation(FileTime creation) {
        this.creation = creation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public JSONObject toJson(){
        JSONObject obj = new JSONObject();
        obj.put("name", name);
        obj.put("path", path);
        obj.put("creation", creation.toString());
        obj.put("modified", modified.toString());
        obj.put("accessed", accessed.toString());
        obj.put("type", type.toString());
        obj.put("hidden", hidden);
        return obj;
    }
}
