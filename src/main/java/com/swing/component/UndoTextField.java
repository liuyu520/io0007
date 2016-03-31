package com.swing.component;

import com.common.bean.FindTxtResultBean;
import com.common.util.SystemHWUtil;
import com.swing.component.inf.IPlaceHolder;
import com.swing.dialog.GenericDialog;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;
import java.awt.*;

/***
 * 按Ctrl+R 使文本框只读
 * <br>按Ctrl+E 使文本框可编辑
 * <br>按Ctrl+D 清空文本框
 * @author huangweii
 * 2015年11月4日
 */
public class UndoTextField extends JTextField  implements IPlaceHolder{
	private static final long serialVersionUID = 2622113838910292609L;
	/***
	 * 用于截图的对话框
	 */
	private static GenericDialog screenshotDialog = TextCompUtil2.screenshotDialog;
	/***
	 * 自定义成员变量,默认提示语
	 */
	protected String placeHolderText;
	private UndoManager undo = new UndoManager();
	private Document doc = getDocument();
	private FindTxtResultBean findTxtResultBean;

	public UndoTextField(String text, boolean needSearch) {
		super(text);
		initlize(needSearch);
	}

	public UndoTextField(boolean needSearch) {
		super();
		initlize(needSearch);

	}

	public UndoTextField() {
		this(true);
	}

	public UndoTextField(int size, boolean needSearch) {
		super(size);
		initlize(needSearch);
	}

	public UndoTextField(int size) {
		this(size, true);
	}
	public static GenericDialog getScreenshotDialog() {
		return screenshotDialog;
	}

	public GenericDialog showScreenshotDialog(){
		screenshotDialog.launchFrame();
		return screenshotDialog;
	}

	public GenericDialog showScreenshotDialog(int width,int height){
		Point point= this.getLocationOnScreen();
		screenshotDialog.setBounds(point.x,point.y,width,height);
		return showScreenshotDialog();

	}

	public FindTxtResultBean getFindTxtResultBean() {
		return findTxtResultBean;
	}

	public void setFindTxtResultBean(FindTxtResultBean findTxtResultBean) {
		this.findTxtResultBean = findTxtResultBean;
	}

	public void stopUndo() {
		// undo.die();
		undo.discardAllEdits();
	}

	protected void initlize(boolean needSearch) {
		doc.addUndoableEditListener(new UndoableEditListener() {
			public void undoableEditHappened(UndoableEditEvent e) {
				undo.addEdit(e.getEdit());
			}
		});
//		addActionMap();
		TextCompUtil2.addActionMap(this, undo, needSearch);
	}

	/***
	 * 可以被子类覆写
	 * @param textComponent
	 * @return
	 */
	protected JTextComponent dealSave(JTextComponent textComponent){
		return textComponent;
	}

	/***
	 * 设置默认提示语
	 * @param placeHolder
	 */
	public void setPlaceHolder(String placeHolder){
		TextCompUtil2.setPlaceHolder(this, placeHolder);
	}

	/***
	 * 判断当前文本是否是默认提示语
	 * @param placeHolder
	 * @return
	 */
	public boolean isPlaceHolder(String placeHolder){
		return TextCompUtil2.isPlaceHolder(this, placeHolder);
	}

	/***
	 * 设置默认提示语,包括监听focus事件
	 * @param placeHolder
	 */
	public UndoTextField placeHolder(String placeHolder){
		setPlaceHolder(placeHolder);
		TextCompUtil2.placeHolderFocus(this, placeHolder);
		return this;
	}

	/***
	 * 排除了placeholder
	 * @return
	 */
	public String getText2() {
		String text=super.getText();
		if(TextCompUtil2.isPlaceHolder(this,text, placeHolderText)){
			return SystemHWUtil.EMPTY;
		}else{
			return text;
		}
	}
}
