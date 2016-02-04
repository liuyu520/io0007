package com.swing.table;

import javax.swing.*;
/***
 * 还需要增加如下功能:<br>
 * (1)增加一行
 * (2)获取选中行的数据
 * @author Administrator
 * @date 2015年11月4日
 */
public class MyTable extends JTable{

	private static final long serialVersionUID = -257468425432013131L;
	/**
	 * 清空表格数据
	 */
	public void cleanUp(){
		TableUtil3.cleanTableData(this);  
        repaint();  
	}
	/***
	 * 获取表格数据
	 * @return
	 */
	public Object[] getTableData2(){
		Object[][] data2 = TableUtil3.getParameter4Table(this/*,isTextComponent*/).getOnlyStringValue();
		return data2;
	}
}
