package com.cmd.dos.hw;

import com.cmd.dos.hw.callback.CallbackWorker;
import com.cmd.dos.hw.util.ShellSwingWorker;
import com.common.dict.Constant2;
import com.common.util.SystemHWUtil;
import com.io.hw.awt.color.CustomColor;
import com.string.widget.util.ValueWidget;
import com.swing.component.AssistPopupTextField;
import com.swing.component.AssistPopupTextPane;
import com.swing.dialog.DialogUtil;
import com.swing.dialog.GenericPanel;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class ShellScriPanel extends GenericPanel{
	public static final String exit_code_label = "<html>exit code:%s<html/>";
	private static final long serialVersionUID = 1644076682819874235L;
	/***
	 * logging
	 */
	protected Logger logger = Logger.getLogger(this.getClass());
	private AssistPopupTextPane resultTP = null;
	private AssistPopupTextField shScriptTF = null;
	private JLabel exitcodeLabel;
	private ProcessBuilder pb = null;
	private JComboBox<String> encodingComboBox;
	private CallbackWorker callbackWorker;

	/*public static void main(String[] args) {
		ShellScriptExecutorApp app = new ShellScriptExecutorApp();
		app.launchFrame();
	}*/
	private JLabel envDNSLabel;
	private JProgressBar copyProgressBar;
	/***
	 * 进度条的颜色
	 */
	private Color foregroundColor;
	private ShellScriInfo shellScriInfo;

	public ShellScriPanel(CallbackWorker callbackWorker, String borderTitle, ShellScriInfo shellScriInfo) {
		this(callbackWorker, borderTitle, null, shellScriInfo);
	}
	
	public ShellScriPanel(CallbackWorker callbackWorker,String borderTitle,Color foregroundColor,ShellScriInfo shellScriInfo) {
		super();
		this.foregroundColor=foregroundColor;
		this.shellScriInfo=shellScriInfo;
		layout3(this,borderTitle,shellScriInfo);
		this.callbackWorker=callbackWorker;
		if(this.callbackWorker!=null){
			this.callbackWorker.setShellScriPanel(this);
		}
	}

	/***
	 * 覆写了父类方法
	 */
	public void launchFrame(String borderTitle) {
		layout3(this, borderTitle, null);
//		this.setVisible(true);
	}

	public void layout3(Container contentPane,String borderTitle,ShellScriInfo shellScriInfo) {
		setLayout(new BorderLayout());
		DialogUtil.lookAndFeel2();
		
		shScriptTF = new AssistPopupTextField(8);
		boolean shScriptTFIsEditable=true;
		if(null!=shellScriInfo){
			shScriptTF.setText(shellScriInfo.getCmd());
			shScriptTFIsEditable=shellScriInfo.isEditable();
		}
		shScriptTF.setEditable(shScriptTFIsEditable);
		drag(shScriptTF);
		resultTP = new AssistPopupTextPane();
		resultTP.setEditable(false);
		resultTP.setFocusable(true);
		JScrollPane js = new JScrollPane(resultTP);
		Border border1 = BorderFactory.createEtchedBorder(Color.white,CustomColor.getColor());
		if(borderTitle==null){
			borderTitle=SystemHWUtil.EMPTY;
		}
		TitledBorder openFileTitle = new TitledBorder(border1, borderTitle);
		openFileTitle.setTitleColor(CustomColor.getDeepColor());
		js.setBorder(openFileTitle);

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1));
		panel.add(shScriptTF);

		// 字符编码下拉框
		encodingComboBox = new JComboBox<String>();
		// 设置下拉框中的选项
		for (int i = 0; i < SystemHWUtil.CHARSET_ARRAY.length; i++) {
			String charSet = SystemHWUtil.CHARSET_ARRAY[i];
			encodingComboBox.addItem(charSet);
		}
		encodingComboBox.setSelectedIndex(1);
		panel.add(encodingComboBox);

		contentPane.add(panel, BorderLayout.NORTH);
		contentPane.add(js, BorderLayout.CENTER);
		// 文本框回车事件
		shScriptTF.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!DialogUtil.verifyTFEmpty(shScriptTF, "命令")){
					return;
				}
				// 执行命令
				exeCommandAction(/*ShellScriPanel.this.callbackWorker*/);
			}
		});
		/* 增加双击事件，目的：当文本框置灰时，双击可以使其有效 */
		shScriptTF.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (!shScriptTF.isEditable()) {
						shScriptTF.setEditable(true);
						DialogUtil.focusSelectAllTF(shScriptTF);
					}
				}
				super.mouseClicked(e);
			}

		});
		JPanel bottomPane = new JPanel();
		GridBagLayout gbl_bottomPane = new GridBagLayout();
		gbl_bottomPane.columnWidths = new int[] {225, 225, 10};
		gbl_bottomPane.rowHeights = new int[] {16, 16, 0};
		gbl_bottomPane.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_bottomPane.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		bottomPane.setLayout(gbl_bottomPane);
		exitcodeLabel = new JLabel();
		exitcodeLabel.setText(String.format(exit_code_label, "未知"));
		GridBagConstraints gbc_exitcodeLabel = new GridBagConstraints();
		gbc_exitcodeLabel.fill = GridBagConstraints.BOTH;
		gbc_exitcodeLabel.insets = new Insets(0, 0, 5, 5);
		gbc_exitcodeLabel.gridx = 0;
		gbc_exitcodeLabel.gridy = 0;
		bottomPane.add(exitcodeLabel, gbc_exitcodeLabel);
		
		envDNSLabel=new JLabel();
		GridBagConstraints gbc_envDNSLabel = new GridBagConstraints();
		gbc_envDNSLabel.fill = GridBagConstraints.BOTH;
		gbc_envDNSLabel.insets = new Insets(0, 0, 5, 0);
		gbc_envDNSLabel.gridx = 1;
		gbc_envDNSLabel.gridy = 0;
		bottomPane.add(envDNSLabel, gbc_envDNSLabel);
		
		copyProgressBar = new JProgressBar();
		copyProgressBar.setBorderPainted(true);
//		copyProgressBar.setString(PROGRESSINITSTR);
		copyProgressBar.setStringPainted(false);
		GridBagConstraints gbc_copyProgressBar = new GridBagConstraints();
		gbc_copyProgressBar.gridwidth = 2;
		gbc_copyProgressBar.fill = GridBagConstraints.BOTH;
		gbc_copyProgressBar.insets = new Insets(0, 0, 0, 20);
		gbc_copyProgressBar.gridx = 0;
		gbc_copyProgressBar.gridy = 1;
		bottomPane.add(copyProgressBar, gbc_copyProgressBar);
		contentPane.add(bottomPane, BorderLayout.SOUTH);
	}
	public void updateDnsEnvInfo(String envInfo){
		if(envInfo.contains("集测环境")){
			envDNSLabel.setForeground(Color.red);
		}else if(envInfo.contains("线上")) {
			envDNSLabel.setForeground(Color.blue);
		}else if(envInfo.contains("仿真")) {
			envDNSLabel.setForeground(Color.yellow);
		}else{
			envDNSLabel.setForeground(Color.green);
//			envDNSLabel.setBackground(Color.red);
		}
		envDNSLabel.setText(envInfo);
	}
	/***
	 * 清空DNS环境信息
	 */
	public void cleanUpDnsEnvInfo(){
		envDNSLabel.setText(SystemHWUtil.EMPTY);
	}

	/***
	 * 更新DNS环境信息
	 * @param envInfo
	 * @param color
	 */
	public void updateDnsEnvInfo(String envInfo,Color color){
		envDNSLabel.setForeground(color);
		envDNSLabel.setText(envInfo);
	}
	
	/***
	 * 执行操作系统本地命令.
	 * 
	 * @param input
	 * @param pb
	 * @param resultTP
	 * @param encoding
	 * @return
	 */
	public /*static*/ int executeLocalCmd(String input, ProcessBuilder pb,
			JTextPane resultTP, String encoding,CallbackWorker callbackWorker) {
		if(ValueWidget.isNullOrEmpty(input)||ValueWidget.isNullOrEmpty(pb)){
			return SystemHWUtil.NEGATIVE_ONE;
		}
		copyProgressBar.setStringPainted(false); 
		copyProgressBar.setIndeterminate(true);
		callback2(input);
		if (input.equals("pwd")) {/*windows 没有“pwd” 命令*/
			input = "cd";
		}
        if (SystemHWUtil.isWindows) {
            input = Constant2.FIX_PREFIX_COMMAND + input;
        }

        String[] commands = input.split("[ \t]");
		pb.command(commands);

		// if (directory != null)
		// {
		// pb.directory(directory);
		// }
		// Map<String, String> env = pb.environment();
		// env.put("$HOME", "/home/whuang2");
		
		resultTP.setText(SystemHWUtil.EMPTY);
		try {
			ShellSwingWorker worker=null;
			synchronized (ShellScriPanel.class) {//解决ShellSwingWorker 中done()方法相互影响的问题<br>important
				worker = new ShellSwingWorker(pb.start(), resultTP,
						encoding);
			}
//			System.out.println("worker:"+worker);
//			System.out.println("callbackWorker:"+callbackWorker);
			if(callbackWorker!=null){
				worker.setCallbackWorker(callbackWorker);
			}
			
			worker.execute();
			/*if(worker.isDone()){
				System.out.println("worker.get():"+worker.get());
			}*/
			
			Process proc = worker.getProcess();
			proc.waitFor();
			System.out.println("proc.waitFor() is executed.");
			// result2 = worker.getStringbuf().toStSring();
			int exitCode2 = proc.exitValue();
			return exitCode2;
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			resultTP.setText(SystemHWUtil.convertUTF2GBK(e1
					.getLocalizedMessage()));
		} catch (IOException e2) {
			e2.printStackTrace();
			resultTP.setText(e2.getMessage());
			System.out.println(SystemHWUtil.convertUTF2GBK(e2.getMessage()));
			System.out.println("---------------");
			System.out.println(SystemHWUtil.convertGBK2UTF(e2.getMessage()));
		}
		return SystemHWUtil.NEGATIVE_ONE;
	}

	/***
	 * 可以覆写,用于记录cmd history
	 */
	public void callback2(String input) {
		
	}


	/***
	 * 执行操作系统本地命令
	 */
	public void exeCommandAction(/*final CallbackWorker callbackWorker*/) {
		final String encoding = (String) encodingComboBox.getSelectedItem();
		if(ValueWidget.isNullOrEmpty(shScriptTF.getText())){
			return;
		}

		Runnable run2=new Runnable() {
			@Override
			public void run() {
				shScriptTF.setEditable(false);
				String input = shScriptTF.getText();
				if(ValueWidget.isNullOrEmpty(input)){
					return;
				}
//				if (pb == null) {//不然 pb.start() 产生的两个Process 对象相同
					pb = new ProcessBuilder();
//					System.out.println("pb\t:"+pb);
//				}
				// 删除命令前后的空格
				input = input.trim();
				if(input.equals("ls"))
				{
					input="dir";
				}
				// 清空结果文本域
				resultTP.setText("");
				if (input.startsWith("cd ")) {
					callback2(input);
					String[] strs = input.split("[ \\s]");
					String relativePath = strs[1];
					if (!relativePath.startsWith(File.separator)) {
						File oldFile = pb.directory();
						if (oldFile != null) {
							relativePath = oldFile.getAbsolutePath()
									+ File.separator + relativePath;
						}
					}
					File dir=new File(relativePath);
					Document docment = resultTP.getDocument();
					if(!dir.exists()){//cd的目录不存在
						try {
							docment.insertString(0, "dir \""+relativePath+"\" does not exist.", null);
						} catch (BadLocationException e) {
							e.printStackTrace();
						}
					}else{
						pb.directory(dir);
						try {
							docment.insertString(0, pb.directory().getAbsolutePath(), null);
						} catch (BadLocationException e) {
							e.printStackTrace();
						}
					}
					
				} else {
					int exitCode2 = executeLocalCmd(input, pb, resultTP,
							encoding,callbackWorker);
					exitcodeLabel.setText(String.format(exit_code_label, String.format(
							exitCode2 == 0 ? SystemHWUtil.SWING_DIALOG_BLUE
									: SystemHWUtil.SWING_DIALOG_RED,
							exitCode2)));
					// System.out.println("result:" + result);
					// System.out.println("after exitValue");
					// return result2;

				}
				if(null==shellScriInfo||shellScriInfo.isEditable()){
					shScriptTF.setEditable(true);
					DialogUtil.focusSelectAllTF(shScriptTF);
				}
				
				if(copyProgressBar!=null){
					copyProgressBar.setIndeterminate(false);
					copyProgressBar.setStringPainted(true);
					if(ValueWidget.isNullOrEmpty(foregroundColor)){
						foregroundColor=CustomColor.getDeepColor();
					}
					copyProgressBar.setForeground(foregroundColor);
					copyProgressBar.setValue(100); 
				}
			}
		};
//		SwingUtilities.invokeLater(run2);
		new Thread(run2).start();
		// System.out.println("启动了线程");
	}


	public AssistPopupTextField getShScriptTF() {
		return shScriptTF;
	}


	public void setShScriptTF(AssistPopupTextField shScriptTF) {
		this.shScriptTF = shScriptTF;
	}
	public void setCmd(String cmd){
		this.shScriptTF.setText(cmd);
	}
}
