package com.swing.dialog.toast;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.string.widget.util.ValueWidget;
/***
 * Android like Toast in Swing <br>
 * 参考:http://stackoverflow.com/questions/10161149/android-like-toast-in-swing
 * @author huangweii
 * 2015年8月7日
 */
public class ToastMessage extends JDialog {
	private static final long serialVersionUID = 5556830959614382641L;
	int miliseconds;

	public ToastMessage(String toastString, int time,ToastCallback toastCallback)
	{
		this(toastString,time, null,toastCallback);
	}
	public ToastMessage(String toastString, int time, Color bgColor,final ToastCallback toastCallback) {
		this.miliseconds = time;
		setUndecorated(true);
		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		if (ValueWidget.isNullOrEmpty(bgColor)) {
			bgColor = Color.blue;
		}
		panel.setBackground(bgColor);
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
		getContentPane().add(panel, BorderLayout.CENTER);

		JLabel toastLabel = new JLabel();
		toastLabel.setText(toastString);
		toastLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		toastLabel.setForeground(Color.WHITE);

		setSize(toastLabel.getPreferredSize().width + 20, 31);

		setAlwaysOnTop(true);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int y = dim.height / 2 - getSize().height / 2;
		int half = y / 2;
		setLocation(dim.width / 2 - getSize().width / 2, y + half);
		panel.add(toastLabel);
		setVisible(false);

		new Thread() {
			public void run() {
				try {
					Thread.sleep(miliseconds);
					dispose();
					//回调
					if(!ValueWidget.isNullOrEmpty(toastCallback)){
						toastCallback.callback();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	/***
	 * 弹框
	 * @param toastString
	 * @param time
	 * @param bgColor
	 */
	public static void toast(String toastString, int time, Color bgColor,ToastCallback toastCallback){
		ToastMessage toastMessage = new ToastMessage(toastString,time,bgColor,toastCallback);
        toastMessage.setVisible(true);
	}
	public static void toast(String toastString, int time, Color bgColor){
		toast(toastString, time, bgColor, (ToastCallback)null);
	}
	/***
	 * 弹框
	 * @param toastString
	 * @param time
	 */
	public static void toast(String toastString, int time){
		toast(toastString, time, (Color)null,(ToastCallback)null);
	}
	public static void toast(String toastString, int time,ToastCallback toastCallback){
		toast(toastString, time, (Color)null,toastCallback);
	}
}
