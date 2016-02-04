package com.swing.dialog;

import com.swing.dialog.inf.DialogInterface;

import javax.swing.*;
import java.awt.*;
/***
 * 
 * @author huangwei
 * @since 2014年11月15日
 */
public abstract class AbstractPanel  extends JPanel implements DialogInterface{
	private static final long serialVersionUID = -5170152630907540309L;
	
	public void drag(final Component component,final Component tf)
	{
		DialogUtil.drag(component, tf, this);
	}
	public void drag(final Component component)// 定义的拖拽方法
	{
		DialogUtil.drag(component, null,this);
	}
	
	/***
	 * 获取下拉框中选中的项
	 * @param encodingComboBox
	 * @return
	 */
	protected String getSelectedItem4ComboBox(JComboBox<String> encodingComboBox){
		Object selectedItem=encodingComboBox.getSelectedItem();
		String selectItemStr=null;
		if(selectedItem instanceof String){
			selectItemStr=(String)selectedItem;
		}else{
			selectItemStr=selectedItem.toString();
		}
		return selectItemStr;
	}
	/***
	 * 获取屏幕的高度
	 * @return
	 */
	public int getScreenHeight(){
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		return screensize.height;
	}
	public int getFullHeight(){
		return getScreenHeight();
	}
	/***
	 * 获取屏幕的宽度
	 * @return
	 */
	public int getScreenWidth(){
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		return screensize.width;
	}

}
