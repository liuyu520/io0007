package com.swing.dialog;

import com.common.dict.Constant2;
import com.string.widget.util.ValueWidget;
import com.swing.dialog.inf.DialogInterface;
import com.swing.dialog.toast.ToastMessage;
import com.swing.event.EventHWUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

/***
 * 
 * @author huangwei
 * 
 */
public abstract class AbstractDialog extends JDialog implements DialogInterface {
	private static final long serialVersionUID = -882549032390513157L;

	public AbstractDialog(Frame owner, boolean modal) {
		super(owner, modal);
	}

	public AbstractDialog() {
		super();
	}

	public void launchFrame() {
		launchFrame(null);
	}
	public void launchFrame(String title) {
		if(!ValueWidget.isNullOrEmpty(title)){
			this.setTitle(title);
		}
		
		// ImageIcon icon = null;
		// try {
		// URL url = new URL(
		// "file:///usr/share/icons/hicolor/16x16/apps/gnotravex.png");
		// icon = new ImageIcon(url);
		// } catch (MalformedURLException e) {
		// e.printStackTrace();
		// }

//		setLoc(this, 800, 600);
		layout3(this.getContentPane());
		this.setVisible(true);
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/***
	 * 必须执行launchFrame ,layout3才会被调用
	 * @param contentPane
     */
	public abstract void layout3(Container contentPane);

	/***
	 * 
	 * 
	 * @param resourcePath : "/com/pass/img/posterous_uploader.png"
	 * @throws IOException
	 */
    /*public void setIcon(String resourcePath,Class clz) {
        if (resourcePath.startsWith("/")) {//eg "/com/pass/img/posterous_uploader.png"
			URL url = clz.getResource(resourcePath);
			ImageIcon icon = new ImageIcon(url);
			if (null != icon) {
				this.setIconImage(icon.getImage());
			}
		}
	}*/

	/***
	 * Set the position of the window
	 * 
	 * @param dialog
	 * @param width
	 * @param height
	 */
	private void setLoc(JDialog dialog, int width, int height) {
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setSize(width, height);

		Dimension framesize = dialog.getSize();
		int x = (int) screensize.getWidth() / 2 - (int) framesize.getWidth()
				/ 2;
		int y = (int) screensize.getHeight() / 2 - (int) framesize.getHeight()
				/ 2;
		dialog.setLocation(x, y);
	}

	public void setLoc(int width, int height) {
		setLoc(this, width, height);
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
	 * 全屏
	 */
	public void fullScreen(){
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screensize.width , screensize.height-Constant2.SWING_TASK_BAR_HEIGHT);
		setLocation(0, 0);
	}
	public void fullScreenMinus30(){
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		setLoc(this, screensize.width, screensize.height-30);
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

    public void show2() {
        this.setVisible(true);
    }

    public void close2() {
        this.dispose();
    }

    /***
     * 增加dialog快捷键.<Br>
     * Ctrl+S,触发保存配置文件<br />
     * GenericDialog 的layout3()方法中会调用
     */
    protected void setShortCuts(JComponent component) {
//        Toolkit toolkit = Toolkit.getDefaultToolkit();
        // 注册应用程序全局键盘事件, 所有的键盘事件都会被此事件监听器处理.
        component.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                System.out.println("kE.getKeyCode():" + event.getKeyCode());
                super.keyPressed(event);
                if (event.getClass() == KeyEvent.class) {
                    KeyEvent kE = ((KeyEvent) event);
                    // 处理按键事件 Ctrl+S
                    if (kE.getKeyCode() == KeyEvent.VK_S
                            && EventHWUtil.isControlDown(kE) && !kE.isAltDown() && !kE.isShiftDown()
                            && kE.getID() == KeyEvent.KEY_PRESSED) {
                        saveConfig();
                        ToastMessage.toast("保存配置文件成功", 3000);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println("kE.getKeyCode()22:" + e.getKeyCode());
                super.keyReleased(e);
            }
        });
    }

    /***
     * Ctrl+S 保存配置文件
     * @throws IOException
     */
    protected abstract void saveConfig();
}
