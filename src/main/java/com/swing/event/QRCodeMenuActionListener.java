package com.swing.event;

import com.string.widget.util.ValueWidget;
import com.swing.component.QRCodePanel;
import com.swing.menu.MenuUtil2;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/***
 * 设置新的密码
 * 
 * @author huangwei
 * 
 */
public class QRCodeMenuActionListener implements ActionListener {
	private QRCodePanel qRCodePanel;

	public QRCodeMenuActionListener(QRCodePanel qRCodePanel) {
		super();
		this.qRCodePanel = qRCodePanel;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		if (command.equals(MenuUtil2.ACTION_IMAGE_COPY)) {
			this.qRCodePanel.clipboardImageAction();
			System.out.println("copy");
//			GUIUtil23.infoDialog("联系邮箱:huangwei@yunmasoft.com");
		}else if (command.equals(MenuUtil2.ACTION_IMAGE_PASTE)) {
			this.qRCodePanel.pasteClipboardImageAction();
		}else if (command.equals(MenuUtil2.ACTION_STR_EXPORT)) {// 退出应用程序
			qRCodePanel.saveQRCodeDialog();
		}else if (command.equals(MenuUtil2.ACTION_STR_CLEANUP)) {// 退出应用程序
			qRCodePanel.cleanUpQRLabel();
		}else if (command.equals(MenuUtil2.ACTION_ENLARGE)) {// 退出应用程序
			qRCodePanel.enlarge();
		}else if (command.equals(MenuUtil2.ACTION_REDUCE)) {// 退出应用程序
			qRCodePanel.reduce();
		}else if (command.startsWith(MenuUtil2.ACTION_READ_QR_CODE)) {// 读取二维码
			qRCodePanel.deCode(true);
		}else if (command.equals(MenuUtil2.ACTION_STR_OPEN_BROWSER)) {// 打开浏览器
			String urlTmp=qRCodePanel.deCode(false);
			if(!ValueWidget.isNullOrEmpty(urlTmp)){
				try {
					URI uri = new URI(urlTmp);
					Desktop.getDesktop().browse(uri);
				} catch (URISyntaxException e2) {
					e2.printStackTrace();
					qRCodePanel.appendResult(e2.getMessage());
				} catch (IOException e2) {
					e2.printStackTrace();
					qRCodePanel.appendResult(e2.getMessage());
				}
			}
			
		}
	}
}
