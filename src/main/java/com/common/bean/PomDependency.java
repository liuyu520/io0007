package com.common.bean;

/**
 * Created by 黄威 on 8/24/16.<br >
 */
public class PomDependency {
    private String groupId;
    private String artifactId;
    private String version;
    private String type = "jar";
    private String packaging = "jar";

    public String getGroupId() {
        return groupId;
    }

    public PomDependency setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public PomDependency setArtifactId(String artifactId) {
        this.artifactId = artifactId;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public PomDependency setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getType() {
        return type;
    }

    public PomDependency setType(String type) {
        this.type = type;
        return this;
    }

    public String getPackaging() {
        return packaging;
    }

    public PomDependency setPackaging(String packaging) {
        this.packaging = packaging;
        return this;
    }

    @Override
    public String toString() {
        return "PomDependency{" +
                "groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
