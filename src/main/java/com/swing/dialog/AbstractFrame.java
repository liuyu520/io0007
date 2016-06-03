package com.swing.dialog;

import com.common.util.WindowUtil;
import com.string.widget.util.ValueWidget;
import com.swing.dialog.inf.DialogInterface;
import com.swing.dialog.toast.ToastMessage;
import com.swing.menu.MenuUtil2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

/***
 * 
 * @author huangwei
 * 
 */
public abstract class AbstractFrame extends JFrame implements DialogInterface{
	private static final long serialVersionUID = -882549032390513157L;

	
	public void launchFrame() {
		// ImageIcon icon = null;
		// try {
		// URL url = new URL(
		// "file:///usr/share/icons/hicolor/16x16/apps/gnotravex.png");
		// icon = new ImageIcon(url);
		// } catch (MalformedURLException e) {
		// e.printStackTrace();
		// }
		DialogUtil.lookAndFeel2();

		// setLoc(this, 800, 600);
		layout3(this.getContentPane());
//		readProp();
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/***
	 * 必须执行launchFrame ,layout3才会被调用
	 * @param contentPane
     */
	public abstract void layout3(Container contentPane);


    public void setIcon(String resourcePath, Class<?> clazz) throws IOException {
        DialogUtil.setIcon(this, resourcePath, clazz);
    }

	/***
	 * 全屏
	 * @param frame
	 */
	protected void fullScreen(JFrame frame){
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		setLoc(frame,screensize.width , screensize.height);
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

	/***
	 * Set the position of the window
	 * 
	 * @param frame
	 * @param width
	 * @param height
	 */
	protected void setLoc(JFrame frame, int width, int height) {
		int taskHeight=20;
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		double heightScreen=screensize.getHeight();
		if(height>heightScreen){
			height=(int) heightScreen-taskHeight*2;
		}
		frame.setSize(width, height);

		Dimension framesize = frame.getSize();
		int x = (int) screensize.getWidth() / 2 - (int) framesize.getWidth()
				/ 2;
		int y = (int) heightScreen / 2 - (int) framesize.getHeight()
				/ 2;
		frame.setLocation(x, y);
	}

	public void setLoc(int width, int height) {
		setLoc(this, width, height);
	}
	public void fullScreen(){
		fullScreen(this);
	}
	/***
	 * 全屏,底部到任务栏
	 * @param frame
	 */
	public void fullScreen2(JFrame frame){
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(screensize.width, screensize.height-40);
		frame.setLocation(0, 0);
	}
	/***
	 * generate a button for paste
	 * 
	 * @param tf
	 * @return
	 */
	protected JButton getPasteButton(final JTextField tf) {
		JButton pasteButton = new JButton(MenuUtil2.ACTION_STR_PASTE);
		pasteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = WindowUtil.getSysClipboardText();
				if (ValueWidget.isHasValue(input)) {
					tf.setText(input);
				}
			}
		});
		return pasteButton;
	}

	/***
	 * generate a button for copy
	 * 
	 * @param tf
	 * @return
	 */
	protected JButton getCopyButton(final JTextField tf) {
		JButton copyButton = new JButton(MenuUtil2.ACTION_STR_COPY);
		copyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = tf.getText();
				if (ValueWidget.isHasValue(input)) {
					WindowUtil.setSysClipboardText(input);
				}
			}
		});
		return copyButton;
	}
	/***
	 * shutdown all window and frame
	 */
	public void closeAllWindow() {
		this.dispose();
		System.exit(0);
	}
	public void drag(final Component component,final Component tf)
	{
		DialogUtil.drag(component, tf, this);
	}
	public void drag(final Component component)// 定义的拖拽方法
	{
		DialogUtil.drag(component, null,this);
	}
	
	 /***
	 * 增加全局快捷键.<Br>
	 * Ctrl+S,触发保存配置文件
	 */
	protected void setGlobalShortCuts() {
		// Add global shortcuts
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		// 注册应用程序全局键盘事件, 所有的键盘事件都会被此事件监听器处理.
		toolkit.addAWTEventListener(new java.awt.event.AWTEventListener() {
			public void eventDispatched(AWTEvent event) {
				if (event.getClass() == KeyEvent.class) {
					KeyEvent kE = ((KeyEvent) event);
					// 处理按键事件 Ctrl+S
					if (kE.getKeyCode() == KeyEvent.VK_S
							&& kE.isControlDown()&&!kE.isAltDown()&&!kE.isShiftDown()
							&& kE.getID() == KeyEvent.KEY_PRESSED) {
						saveConfig();
                        ToastMessage.toast("保存配置文件成功", 3000);
                    } else if(kE.getKeyCode() == KeyEvent.VK_E
							&& kE.isControlDown()&&!kE.isAltDown()&&!kE.isShiftDown()
							&& kE.getID() == KeyEvent.KEY_PRESSED){//最近访问的文件或窗口
						showRecentList();//Ctrl+E或者Alt+Shift+C  最近更改的代码
					}
				}
			}
		}, java.awt.AWTEvent.KEY_EVENT_MASK);

	}
	/***
	 * Ctrl+E 最近打开的文件<br>
	 * 子类可以覆写
	 */
	public void showRecentList(){
		
	}
		/***
		 * Ctrl+S 保存配置文件
		 * @throws IOException
		 */
		protected abstract void saveConfig();
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
}
