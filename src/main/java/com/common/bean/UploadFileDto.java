package com.common.bean;

/**
 * Created by whuanghkl on 17/7/17.
 */
public class UploadFileDto {
    private String fileName;
    private String remoteAbsolutePath;
    private String url;
    private String fullUrl;
    private String relativePath;
    private String imgTag;
    private String folder;
    /***
     * 是否上传成功
     */
    private boolean isSuccess;
    /***
     * 上传失败的原因
     */
    private String errorMessage;

    public String getFileName() {
        return fileName;
    }

    public UploadFileDto setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getRemoteAbsolutePath() {
        return remoteAbsolutePath;
    }

    public UploadFileDto setRemoteAbsolutePath(String remoteAbsolutePath) {
        this.remoteAbsolutePath = remoteAbsolutePath;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public UploadFileDto setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public UploadFileDto setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
        return this;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public UploadFileDto setRelativePath(String relativePath) {
        this.relativePath = relativePath;
        return this;
    }

    public String getImgTag() {
        return imgTag;
    }

    public UploadFileDto setImgTag(String imgTag) {
        this.imgTag = imgTag;
        return this;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
