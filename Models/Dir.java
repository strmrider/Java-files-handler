package com.company.tests.filehandler.Models;

import java.nio.file.attribute.FileTime;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Dir extends SysItem {
    private final ArrayList<Dir> dirs;
    private final ArrayList<FileItem> files;

    public Dir(String name, String path, FileTime creation, FileTime modified, FileTime accessed, boolean hidden){
        super(name, path, creation, modified, accessed, hidden);
        this.type = FileItemType.Dir;
        dirs = new ArrayList<>();
        files = new ArrayList<>();
    }

    public void addDir(Dir dir){
        dirs.add(dir);
    }

    public void addFile(FileItem file){
        files.add(file);
    }

    public boolean contains(String name){
        return true;
    }

    public int size(){
        return dirs.size() + files.size();
    }

    public ArrayList<Dir> getDirs() {
        return dirs;
    }

    public ArrayList<FileItem> getFiles() {
        return files;
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj =  super.toJson();
        JSONArray dirsArray = new JSONArray();
        JSONArray filesArray = new JSONArray();
        if (dirs.size() > 0) {
            for (Dir dir : dirs) {
                dirsArray.add(dir.toJson());
            }
        }
        if (files.size() > 0) {
            for (FileItem file : files) {
                filesArray.add(file.toJson());
            }
        }
        obj.put("dirs", dirsArray);
        obj.put("files", filesArray);
        return obj;
    }
}
