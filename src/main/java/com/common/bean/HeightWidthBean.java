package com.common.bean;

/**
 * Created by Administrator on 2016/1/9.
 */
public class HeightWidthBean {
    private int height;
    private int width;
    /***
     * 分辨率倍数
     */
    private int multiple = 2;
    /***
     * false 表示取消
     */
    private boolean beSuccess;
    /**
     * 是否保存到文件
     */
    private boolean saveToFile;
    /***
     * 是否上传截图到远程服务器
     */
    private boolean upload2Server;
    /****
     * 截完图之后进行编辑
     */
    private boolean editScreenshots;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }



    public boolean isValid() {
        return this.getHeight() > 0 && this.getWidth() > 0;
    }

    public int getMultiple() {
        return multiple;
    }

    public void setMultiple(int multiple) {
        this.multiple = multiple;
    }

    public boolean isSaveToFile() {
        return saveToFile;
    }

    public void setSaveToFile(boolean saveToFile) {
        this.saveToFile = saveToFile;
    }

    public boolean isUpload2Server() {
        return upload2Server;
    }

    public void setUpload2Server(boolean upload2Server) {
        this.upload2Server = upload2Server;
    }

    public boolean isBeSuccess() {
        return beSuccess;
    }

    public void setBeSuccess(boolean beSuccess) {
        this.beSuccess = beSuccess;
    }

    public boolean isEditScreenshots() {
        return editScreenshots;
    }

    public void setEditScreenshots(boolean editScreenshots) {
        this.editScreenshots = editScreenshots;
    }
}
