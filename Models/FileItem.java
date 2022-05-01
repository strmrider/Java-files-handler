package com.company.tests.filehandler.Models;

import org.json.simple.JSONObject;

import java.nio.file.attribute.FileTime;

public class FileItem extends SysItem {
    private final long size;

    public FileItem(String name, String path, FileTime creation, FileTime modified, FileTime accessed,
                    long size, boolean hidden){
        super(name, path, creation, modified, accessed, hidden);
        this.size = size;
        this.type = FileItemType.File;
    }

    public long getSize() {
        return size;
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = super.toJson();
        obj.put("size", size);
        return obj;
    }
}
