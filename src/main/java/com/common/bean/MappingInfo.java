package com.common.bean;

public class MappingInfo {
    private int type;
    private String displayName;

    public int getType() {
        return type;
    }

    public MappingInfo setType(int type) {
        this.type = type;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public MappingInfo setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
