package com.common.bean;

import java.io.Serializable;

/**
 * Created by 黄威 on 02/11/2016.<br >
 * QRCodepanel的配置
 */
public class QRCodeInfoBean implements Serializable {
    private String input;
    /***
     * 下拉框的内容
     */
    private String list;

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }
}
