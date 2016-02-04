package com.common.bean;

import java.io.Serializable;

public class Woman implements Serializable {
	private static final long serialVersionUID = 868039609587700700L;
	private String name;
	private int age;
	/***
	 * 爱好，兴趣
	 */
	private String hobby;
	/***
	 * 是否高兴
	 */
	private boolean isCheerful;
	/***
	 * 身份证号码
	 */
	private String ID;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getHobby() {
		return hobby;
	}
	public void setHobby(String hobby) {
		this.hobby = hobby;
	}
	public boolean isCheerful() {
		return isCheerful;
	}
	public void setCheerful(boolean isCheerful) {
		this.isCheerful = isCheerful;
	}
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	@Override
	public String toString() {
		return "Woman [name=" + name + "]";
	}
	
}
