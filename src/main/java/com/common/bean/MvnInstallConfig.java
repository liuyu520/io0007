package com.common.bean;

import java.io.Serializable;

/**
 * Created by 黄威 on 04/01/2017.<br >
 * 被 com/swing/dialog/MavenTookitDialog.java 使用
 */
public class MvnInstallConfig implements Serializable {
    private String groupId;
    private String artifactId;
    private String version;
    /***
     * jar 或者classic
     */
    private String packaging;
    /***
     * jar包的绝对路径
     */
    private String jarFilePath;
    private String pomDepency;

    public String getGroupId() {
        return groupId;
    }

    public MvnInstallConfig setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public MvnInstallConfig setArtifactId(String artifactId) {
        this.artifactId = artifactId;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public MvnInstallConfig setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getPackaging() {
        return packaging;
    }

    public MvnInstallConfig setPackaging(String packaging) {
        this.packaging = packaging;
        return this;
    }

    public String getJarFilePath() {
        return jarFilePath;
    }

    public MvnInstallConfig setJarFilePath(String jarFilePath) {
        this.jarFilePath = jarFilePath;
        return this;
    }

    public String getPomDepency() {
        return pomDepency;
    }

    public MvnInstallConfig setPomDepency(String pomDepency) {
        this.pomDepency = pomDepency;
        return this;
    }
}
