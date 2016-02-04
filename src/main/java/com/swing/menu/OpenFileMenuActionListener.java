package com.swing.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JTextField;

import com.cmd.dos.hw.util.CMDUtil;
import com.common.util.SystemHWUtil;
import com.io.hw.file.util.FileUtils;
import com.string.widget.util.ValueWidget;
/***
 * @see OpenDirTextField
 * @author huangweii
 * 2015年10月30日
 */
public class OpenFileMenuActionListener implements ActionListener{
	private JTextField tf;
	
	public OpenFileMenuActionListener(JTextField tf) {
		super();
		this.tf = tf;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals("浏览文件所在目录")){
			String pathStr=this.tf.getText();
			if(!ValueWidget.isNullOrEmpty(pathStr)){
				FileUtils.open_directory(SystemHWUtil.getParentDir(pathStr));
			}
		}else if(command.equals("notepad 编辑文件")){
			final String pathStr=this.tf.getText();
			if(!ValueWidget.isNullOrEmpty(pathStr)){
				new Thread(new Runnable() {
					@Override
					public void run() {
						String result;
						try {
							result = CMDUtil.execute(new String[]{"cmd", "/c", "notepad", pathStr}, null, null);
							System.out.println(result);
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				}).start();
			}
		}
	}

}
