package com.test.bean;

public class Student2 extends Person2 {
    /***
     * 学号
     */
    private String schoolNumber;
    private String classroom;

    public String getSchoolNumber() {
        return schoolNumber;
    }

    public void setSchoolNumber(String schoolNumber) {
        this.schoolNumber = schoolNumber;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

}
