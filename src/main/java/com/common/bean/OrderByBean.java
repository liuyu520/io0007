package com.common.bean;
/***
 * 用于数据库查询的order by
 * @author huangwei
 * @since 2015年1月24日
 */
public class OrderByBean {
	private String orderMode;
	private String orderedColumn;
	
	/***
	 * 构造方法
	 */
	public OrderByBean() {
		super();
	}
	/***
	 * 有参数的构造方法
	 * @param orderMode
	 * @param orderedColumn
	 */
	public OrderByBean(String orderMode, String orderedColumn) {
		super();
		this.orderMode = orderMode;
		this.orderedColumn = orderedColumn;
	}
	public String getOrderMode() {
		return orderMode;
	}
	public void setOrderMode(String orderMode) {
		this.orderMode = orderMode;
	}
	public String getOrderedColumn() {
		return orderedColumn;
	}
	public void setOrderedColumn(String orderedColumn) {
		this.orderedColumn = orderedColumn;
	}
	
}
