package com.common.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 黄威 on 30/11/2016.<br >
 */
public class Json2BeanClassInfo {
    /***
     * 类名,例如:/Users/whuanghkl/work/mygit/io0007_new/src/test/java/com/chanjet/gov/service/dto/ordernew/Value.java
     /Users/whuanghkl/work/mygit/io0007_new/src/test/java/com/chanjet/gov/service/dto/ordernew/TestDto.java
     <br />
     */
    private List<String> classNameList;
    /***
     * 类体
     */
    private List<String> classBodyList;

    public List<String> getClassNameList() {
        return classNameList;
    }

    public void setClassNameList(List<String> classNameList) {
        this.classNameList = classNameList;
    }

    public List<String> getClassBodyList() {
        return classBodyList;
    }

    public void setClassBodyList(List<String> classBodyList) {
        this.classBodyList = classBodyList;
    }

    public void addClassBody(String classBody) {
        if (null == classBodyList) {
            classBodyList = new ArrayList<String>();
        }
        classBodyList.add(classBody);
    }
}
