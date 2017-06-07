package com.swing.component;

import com.common.bean.FindTxtResultBean;
import com.common.util.SystemHWUtil;
import com.string.widget.util.ValueWidget;
import com.swing.callback.ActionCallback;
import com.swing.component.inf.IPlaceHolder;
import com.swing.dialog.GenericDialog;
import com.swing.listener.DoubleKeyAdapter;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Map;

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
    /***
     * 父组件
     */
    private JComponent parentPanelOrFrame;
    /***
     * key:"Command_enter","Ctrl_enter","alt_enter"
     */
    private Map<String, ActionCallback> actionCallbackMap;
    private boolean hasSetTFCaretPosition = false;
    /***
     * 0:原始状态;<br />
     * 1:最大化状态;<br />
     * 2:删除之后
     */
    protected Integer maxStatus = 0;
    protected JDialog maxJDialog;

	public UndoTextField(String text, boolean needSearch) {
		super(text);
		initlize(needSearch);
	}

    public UndoTextField(boolean needSearch, Map<String, ActionCallback> actionCallbackMap) {
        super();
        initlize(needSearch, actionCallbackMap);

	}

    public UndoTextField(boolean needSearch) {
        this(needSearch, (Map<String, ActionCallback>) null);
    }

    public UndoTextField() {
        this(true, (Map<String, ActionCallback>) null);
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

    protected void initlize(boolean needSearch, Map<String, ActionCallback> actionCallbackMap) {
        doc.addUndoableEditListener(new UndoableEditListener() {
			public void undoableEditHappened(UndoableEditEvent e) {
				undo.addEdit(e.getEdit());
			}
		});
        TextCompUtil2.addActionMap(this, undo, needSearch, actionCallbackMap);

        //在文本框聚焦的情况下,通过按下方向键(仅支持左,右,不支持上下),可以使光标定位到句首或句尾
        final JTextComponent textField = this;
        this.addKeyListener(new DoubleKeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyTyped(e);
                TextCompUtil2.setTFCaretPosition(e, textField, this);
            }
        });
    }

    protected void initlize(boolean needSearch) {
        this.initlize(needSearch, (Map<String, ActionCallback>) null);
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
        if (!ValueWidget.isNullOrEmpty(placeHolder)) {
            setPlaceHolder(placeHolder);
            TextCompUtil2.placeHolderFocus(this, placeHolder);
        }
        setHasSetTFCaretPosition(true);
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

    public void setText(String input, boolean regular) {
        super.setText(input);
        /***
         * 防止程序中setText 方法的参数正好和 placeholder 完全相同<br>
         *     手动输入是没有问题的
         */
        if (regular) {
            setForeground(Color.black);
        }
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

    public boolean isHasSetTFCaretPosition() {
        return hasSetTFCaretPosition;
    }

    public void setHasSetTFCaretPosition(boolean hasSetTFCaretPosition) {
        this.hasSetTFCaretPosition = hasSetTFCaretPosition;
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
