package com.swing.component;

import com.common.bean.FindTxtResultBean;
import com.swing.callback.ActionCallback;
import com.swing.dialog.GenericDialog;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.util.Map;

/***
 * 按Ctrl+R 使文本框只读
 * <br>按Ctrl+E 使文本框可编辑
 * <br>按Ctrl+D 清空文本框
 * <br>Command+enter
 * <br>Ctrl+enter
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
    /***
     * 父组件
     */
    private JComponent parentPanelOrFrame;
    /***
     * key:"Command_enter","Ctrl_enter","alt_enter"
     */
    private Map<String, ActionCallback> actionCallbackMap;
    /***
     * 0:原始状态;<br />
     * 1:最大化状态;<br />
     * 2:删除之后
     */
    protected Integer maxStatus = 0;
    protected JDialog maxJDialog;

    public UndoTextArea(String text) {
        super(text);
        initlize();
    }

    public UndoTextArea(String text, Map<String, ActionCallback> actionCallbackMap) {
        super(text);
        initlize(actionCallbackMap);
    }

    public UndoTextArea(Map<String, ActionCallback> actionCallbackMap) {
        super();
        initlize(actionCallbackMap);

	}

    public UndoTextArea() {
        this((Map<String, ActionCallback>) null);
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

    protected void initlize(Map<String, ActionCallback> actionCallbackMap) {
        doc.addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undo.addEdit(e.getEdit());
            }
		});
//		addActionMap();
        TextCompUtil2.addActionMap(this, undo, actionCallbackMap);
        final JTextComponent textField = this;
        TextCompUtil2.bindKeyEvent(textField);
        //增加鼠标悬浮时的提示
        this.setToolTipText("Command+Shift+E :使文本框可编辑");
       /* public void keyPressed(KeyEvent e) {
                super.keyTyped(e);
                TextCompUtil2.toggleUpperLowerCase(e, textField);
            }
        });*/
    }

    protected void initlize() {
        /*doc.addUndoableEditListener(new UndoableEditListener() {
            public void undoableEditHappened(UndoableEditEvent e) {
                undo.addEdit(e.getEdit());
            }
        });
        TextCompUtil2.addActionMap(this, undo, null);*/
        this.initlize(null);
    }

	/***
	 * 可以被子类覆写
	 * @param textComponent
	 * @return
	 */
	protected JTextComponent dealSave(JTextComponent textComponent){
		return textComponent;
	}

    public JComponent getParentPanelOrFrame() {
        return parentPanelOrFrame;
    }

    public void setParentPanelOrFrame(JComponent parentPanelOrFrame) {
        this.parentPanelOrFrame = parentPanelOrFrame;
    }

    public Map<String, ActionCallback> getActionCallbackMap() {
        return actionCallbackMap;
    }

    public void setActionCallbackMap(Map<String, ActionCallback> actionCallbackMap) {
        this.actionCallbackMap = actionCallbackMap;
    }

    public Integer getMaxStatus() {
        return maxStatus;
    }

    public void setMaxStatus(Integer maxStatus) {
        this.maxStatus = maxStatus;
    }

    public JDialog getMaxJDialog() {
        return maxJDialog;
    }

    public void setMaxJDialog(JDialog maxJDialog) {
        this.maxJDialog = maxJDialog;
    }
}
