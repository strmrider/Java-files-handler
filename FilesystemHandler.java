package com.company.tests.filehandler;

import com.company.tests.filehandler.Models.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;


public class FilesystemHandler {
    public static byte[] readFile(String path) throws IOException {
        File file = new File(path);
        byte[] buffer = new byte[(int)file.length()];
        FileInputStream inputStream = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int total = 0;
        int read;
        while (total < file.length()){
            read = inputStream.read(buffer, 0, buffer.length);
            if (read > 0){
                total += read;
                bos.write(buffer, 0, read);
            }
        }
        return bos.toByteArray();
    }

    public static String readTextFile(String path) throws IOException {
        byte[] file = readFile(path);
        return new String(file);
    }

    public static void writeToFile(String path, byte[] data) throws IOException {
        File file = new File(path);
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(data);
    }

    public static boolean removeFile(String path){
        File file = new File(path);
        return file.delete();
    }

    public static boolean renameFile(String path, String name) throws FileNotFoundException {
        boolean success = false;
        File file = new File(path);
        if (file.exists()) {
            File destFile = new File(file.getParent() + "\\" + name);
            if (!destFile.exists()) {
                success = file.renameTo(destFile);
            }
            return success;
        }
        else
            throw new FileNotFoundException();
    }

    private static boolean cloneFile(String source, String target, boolean copy) throws IOException {
        boolean success = false;
        File sourceFile = new File (source);
        File targetFile = new File(target);
        if (!targetFile.exists()){
            success = targetFile.mkdirs();
        }
        else
            success = true;
        targetFile = new File(target + "\\" + sourceFile.getName());
        InputStream in = new FileInputStream(sourceFile);
        OutputStream out = new FileOutputStream(targetFile);
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        in.close();
        out.flush();
        out.close();

        if (!copy){
            return sourceFile.delete();
        }
        return success;
    }

    public static boolean moveFile(String source, String target) throws IOException {
        return cloneFile(source, target, false);
    }

    public static boolean copyFile(String source, String target) throws IOException {
        return cloneFile(source, target, true);
    }

    public static boolean createDir(String path){
        boolean success = false;
        File newDir = new File(path);
        if (!newDir.exists()){
            success = newDir.mkdirs();
        }
        return success;
    }

    public static Dir readDir(String path) throws IOException {
        File dirFile = new File(path);
        if (!dirFile.exists())
            return null;
        File[] items = dirFile.listFiles();
        BasicFileAttributes attr = Files.readAttributes(dirFile.toPath(), BasicFileAttributes.class);
        Dir dir = new Dir(dirFile.getName(), dirFile.getParent(), attr.creationTime(),
                attr.lastModifiedTime(), attr.lastAccessTime(), dirFile.isHidden());
        if (items != null) {
            for (File item : items) {
                attr = Files.readAttributes(item.toPath(), BasicFileAttributes.class);
                if (item.isFile()){
                    FileItem file = new FileItem(item.getName(), item.getParent(), attr.creationTime(),
                            attr.lastModifiedTime(), attr.lastAccessTime(), item.length(), item.isHidden());
                    dir.addFile(file);
                }
                else if(item.isDirectory()){
                    Dir subDir = new Dir(item.getName(), item.getParent(), attr.creationTime(),
                            attr.lastModifiedTime(), attr.lastAccessTime(), item.isHidden());
                    dir.addDir(subDir);
                }
            }
        }
        return dir;
    }

    private static FileTime timeFromString(String time) throws java.text.ParseException {
        long millis =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(time).getTime();
        return FileTime.fromMillis(millis);
    }

    public static SysItem parseSysItem(String jsonFormat) throws ParseException, java.text.ParseException {
        Object obj = new JSONParser().parse(jsonFormat);
        JSONObject jo = (JSONObject) obj;
        FileItemType type = FileItemType.valueOf((String)jo.get("type"));
        FileTime creation = timeFromString((String)jo.get("creation"));
        FileTime modified = timeFromString((String)jo.get("modified"));
        FileTime accessed = timeFromString((String)jo.get("accessed"));
        if (type == FileItemType.Dir){
            Dir dir = new Dir((String) jo.get("name"), (String) jo.get("path"), creation,
                    modified, accessed, (boolean)jo.get("hidden"));
            JSONArray dirs = (JSONArray)jo.get("dirs");
            JSONArray files = (JSONArray)jo.get("files");
            for (Object value : dirs) {
                String dirFormat = value.toString();
                dir.addDir((Dir) parseSysItem(dirFormat));
            }
            for (Object value : files) {
                String dirFormat = value.toString();
                dir.addFile((FileItem) parseSysItem(dirFormat));
            }
            return dir;
        }
        else if(type == FileItemType.File){
            return new FileItem((String) jo.get("name"), (String) jo.get("path"), creation,
                    modified, accessed, (long)jo.get("size"), (boolean)jo.get("hidden"));
        }
        return null;
    }

    public static Dir parseDir(String json) throws ParseException, java.text.ParseException {
        SysItem sysItem = parseSysItem(json);
        if (sysItem != null)
            return (Dir)sysItem;
        else
            return null;
    }

    public static FileItem parseFile(String json) throws ParseException, java.text.ParseException {
        SysItem sysItem = parseSysItem(json);
        if (sysItem != null)
            return (FileItem)sysItem;
        else
            return null;
    }

    public static Drive[] getPartitions(){
        File[] drivesList = File.listRoots();
        FileSystemView fsv = FileSystemView.getFileSystemView();
        Drive[] drives = new Drive[drivesList.length];
        for (int i=0; i < drivesList.length; i++){
            File drive = drivesList[i];
            drives[i] = new Drive(drive.toString(), drive.getTotalSpace(),
                    drive.getFreeSpace(), fsv.getSystemTypeDescription(drive));
        }
        return drives;
    }
}
