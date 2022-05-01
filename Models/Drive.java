package com.company.tests.filehandler.Models;

public class Drive {
    private final String mount;
    private final long volume;
    private final long freeSpace;
    private final long usedSpace;
    private final String type;

    public Drive(String mount, long volume, long freeSpace, String type){
        this.mount = mount;
        this.volume = volume;
        this.freeSpace = freeSpace;
        usedSpace = volume - freeSpace;
        this.type = type;
    }

    public String getMount() {
        return mount;
    }

    public long getVolume() {
        return volume;
    }

    public long getFreeSpace() {
        return freeSpace;
    }

    public long getUsedSpace() {
        return usedSpace;
    }

    public String getType() {
        return type;
    }
}
