package com.common.bean;

/**
 * Created by Administrator on 2016/1/9.
 */
public class HeightWidthBean {
    private int height;
    private int width;
    /***
     * false 表示取消
     */
    private boolean isSuccess;

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

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public boolean isValid() {
        return this.getHeight() > 0 && this.getWidth() > 0;
    }
}
