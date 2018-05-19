package com.common.bean.exception;

public class LogicBusinessGroup {
    /***
     * 大的种类,比如注册短信和找回密码短信都属于短信类
     */
    private String group;
    private Object info;

    public String getGroup() {
        return group;
    }

    public LogicBusinessGroup setGroup(String group) {
        this.group = group;
        return this;
    }

    public Object getInfo() {
        return info;
    }

    public LogicBusinessGroup setInfo(Object info) {
        this.info = info;
        return this;
    }
}
