package com.common.bean;

import java.util.List;

/**
 * Created by 黄威 on 02/11/2016.<br >
 * QRCodepanel的配置
 */
public class QRCodeInfoBean {
    private String input;
    /***
     * 下拉框的内容
     */
    private List<String> list;

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
