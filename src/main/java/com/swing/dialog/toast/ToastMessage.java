package com.swing.dialog.toast;

import com.string.widget.util.ValueWidget;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
/***
 * Android like Toast in Swing <br>
 * 参考:http://stackoverflow.com/questions/10161149/android-like-toast-in-swing
 * @author huangweii
 * 2015年8月7日
 */
public class ToastMessage extends JDialog {
	private static final long serialVersionUID = 5556830959614382641L;
	int miliseconds;

    public ToastMessage(String toastString, int time, ToastCallback toastCallback, Integer heightDelta) {
        this(toastString, time, null, toastCallback, heightDelta);
    }

    public ToastMessage(String toastString, int time, Color bgColor, final ToastCallback toastCallback, Integer heightDelta) {
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
        int x1 = dim.width / 2 - getSize().width / 2;
        if (null != heightDelta && heightDelta > 0) {
            x1 += heightDelta;
        }
        setLocation(x1, y + half);
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
    public static void toast(String toastString, int time, Color bgColor, ToastCallback toastCallback, Integer heightDelta) {
        ToastMessage toastMessage = new ToastMessage(toastString, time, bgColor, toastCallback, heightDelta);
        toastMessage.setVisible(true);
	}

	public static void toast(String toastString, int time, Color bgColor){
        toast(toastString, time, bgColor, (Integer) null);
    }

    public static void toast(String toastString, int time, Color bgColor, Integer heightDelta) {
        toast(toastString, time, bgColor, (ToastCallback) null, heightDelta);
    }

    public static void toast(String toastString, int time) {
        toast(toastString, time, (Integer) null);
    }

    /**
     * 绿色
     *
     * @param toastString
     * @param miliseconds
     */
    public static void toastRight(String toastString, int miliseconds) {
        toast(toastString, miliseconds, Color.GREEN, (ToastCallback) null, (Integer) null);
    }
	/***
	 * 弹框
	 * @param toastString
	 * @param time
	 */
    public static void toast(String toastString, int time, Integer heightDelta) {
        toast(toastString, time, (Color) null, (ToastCallback) null, heightDelta);
    }

    public static void toast(String toastString, int time, ToastCallback toastCallback, Integer heightDelta) {
        toast(toastString, time, (Color) null, toastCallback, heightDelta);
    }
}
