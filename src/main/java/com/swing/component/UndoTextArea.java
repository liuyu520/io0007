package com.swing.component;

import com.common.bean.FindTxtResultBean;
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
public class UndoTextArea extends JTextArea {
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
	private StringBuffer stringbuf = null;
    private FindTxtResultBean findTxtResultBean;
	public UndoTextArea(String text) {
		super(text);
		initlize();
	}
	public UndoTextArea() {
		super();
		initlize();

	}

	public UndoTextArea(int rows, int columns) {
		super(rows, columns);
		initlize();

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

	public UndoManager getUndo() {
		return undo;
	}

	public void setUndo(UndoManager undo) {
		this.undo = undo;
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}
	
	/**
	 * @autho : whuang dicate original content of the area
	 * @return
	 */
	public StringBuffer getStringbuf() {
		return stringbuf;
	}

	/***
	 * dicate original content of the area
	 *
	 * @param stringbuf
	 */
	public void setStringbuf(StringBuffer stringbuf) {
		this.stringbuf = stringbuf;
	}

	protected void initlize() {
		doc.addUndoableEditListener(new UndoableEditListener() {
			public void undoableEditHappened(UndoableEditEvent e) {
				undo.addEdit(e.getEdit());
			}
		});
//		addActionMap();
		TextCompUtil2.addActionMap(this, undo);
	}

	/***
	 * 可以被子类覆写
	 * @param textComponent
	 * @return
	 */
	protected JTextComponent dealSave(JTextComponent textComponent){
		return textComponent;
	}

}
