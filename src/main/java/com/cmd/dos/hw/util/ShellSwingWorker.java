package com.cmd.dos.hw.util;

import com.cmd.dos.hw.callback.CallbackWorker;
import com.common.util.MyProcess;
import com.common.util.SystemHWUtil;
import com.string.widget.util.ValueWidget;
import com.swing.messagebox.GUIUtil23;
import com.time.util.TimeHWUtil;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class ShellSwingWorker extends SwingWorker<Boolean, Character> {
	/***
	 * logging
	 */
	protected Logger logger = Logger.getLogger(this.getClass());
	/***
	 * 执行命令的正常输出（对程序来说是输入）
	 */
	private BufferedReader br_right = null;
	/***
	 * 执行命令的错误输出（对程序来说是输入）
	 */
	private BufferedReader br_error = null;
	/***
	 * 进程封装类
	 */
	private MyProcess myprocess = null;
	private boolean isPrintVerbose = false;
	private StringBuffer stringbuf = new StringBuffer();
	private JTextPane resultTP = null;
	/***
	 * result textarea' document
	 */
	private Document docment;
	/***
	 * 是否中断
	 */
	private boolean isBreak;
	/***
	 * 命令前缀
	 */
	private CallbackWorker callbackWorker;
	public ShellSwingWorker(MyProcess myprocess, BufferedReader br) {
		this.br_right = br;
		this.myprocess = myprocess;

	}

	/***
	 * 构造方法.
	 * 
	 * @param process
	 * @param textPane
	 * @param encoding
	 */
	public ShellSwingWorker(Process process, JTextPane textPane,
			String encoding)
	{
		MyProcess proc = null;
		proc=new MyProcess(process);
//		System.out.println("process:"+process);
		construct2(proc,textPane,encoding);

	}
	/***
	 * 构造方法.
	 * 
	 * @param myprocess
	 * @param textPane
	 * @param encoding
	 */
	public ShellSwingWorker(MyProcess myprocess, JTextPane textPane,
			String encoding) {
		construct2(myprocess, textPane, encoding);

	}

	private void construct2(MyProcess myprocess, JTextPane textPane,
			String encoding){
		this.myprocess = myprocess;
//		System.out.println("myprocess:"+myprocess);
		if (ValueWidget.isNullOrEmpty(encoding)) {
			encoding = SystemHWUtil.CURR_ENCODING;
		}

		try {
			br_right = new BufferedReader(new InputStreamReader(
					myprocess.getInputStream(), encoding), 4096);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
			br_error = new BufferedReader(new InputStreamReader(
					myprocess.getErrorStream(), encoding), 4096);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		this.resultTP = textPane;
		docment = this.resultTP.getDocument();
	}
	/***
	 * SwingWorker 的子类必须实现 doInBackground() 方法，以执行后台计算
	 */
	@Override
	protected Boolean doInBackground() throws Exception {
//		System.out.println(this+"\t"+System.currentTimeMillis());
		char word = ' ';
		int tmp = 0;
		while ((isContinue())&&(tmp = br_right.read()) != -1) {
			word = (char) tmp;
			publish(word);
		}
//		System.out.println(this+"\t"+System.currentTimeMillis());
		while ((isContinue())&&(tmp = br_error.read()) != -1) {
			word = (char) tmp;
			publish(word);
		}
//		System.out.println(this+"\t"+System.currentTimeMillis());
		if (isPrintVerbose)// 是否打印详细信息
		{
			System.out.println("doInBackground() over");
		}
		return true;
	}

	@Override
	protected void process(List<Character> chunks) {
		if((isContinue())){
		final List<Character> chunks2=chunks;
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
				for (char temp : chunks2) {
					// System.out.print(temp);
					// this.resultTP.setText(this.stringbuf.toString());//效率低
				int leng2 = ShellSwingWorker.this.stringbuf.length();//必须在 this.stringbuf.append(temp); 之前
				ShellSwingWorker.this.stringbuf.append(temp);
				if(callbackWorker!=null){
					if(ShellSwingWorker.this.callbackWorker.callback(ShellSwingWorker.this,stringbuf, temp)){
//						synchronized (ShellSwingWorker.this) {
							if((isContinue())){
								String text=ShellSwingWorker.this.resultTP.getText();
								text=text.replaceAll("请求超时[\\s]*$", SystemHWUtil.EMPTY);
								text=text.replaceAll("请求超[\\s]*$", SystemHWUtil.EMPTY);
								ShellSwingWorker.this.resultTP.setText(text+SystemHWUtil.CRLF+"执行完成!");
								/*try {
									// 追加
									docment.insertString(text.length(), SystemHWUtil.CRLF+"执行完成!", null);
								} catch (BadLocationException e) {
									GUIUtil23.warningDialog(e.getMessage());
									e.printStackTrace();
								}*/
							}
							setBreak(true);
//						}
						return;
					}
				}
					
					try {
						// 追加
						docment.insertString(leng2, String.valueOf(temp), null);
					} catch (BadLocationException e) {
						GUIUtil23.warningDialog(e.getMessage());
						e.printStackTrace();
					}
					
			}
				
			}
//		}).start();
	}

	public StringBuffer getStringbuf() {
		return stringbuf;
	} 

	/***
	 * main thread can't execute next command(below waitFor()) until done() is
	 * executed;if done() hasn't be executed,this.myprocess.waitFor() will wait
	 */
	@Override
	protected void done() {
		if (isPrintVerbose) {
			System.out.println("done() is executed");
		}
		if (!ValueWidget.isNullOrEmpty(br_right)) {
			try {
				br_right.close();
				br_error.close();
				br_right=null;
				br_error=null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		String logMesg=this.getClass().getSimpleName()+".done() is executed successfully.";
		logger.debug(logMesg);
		System.out.println(TimeHWUtil.getMiniuteSecond(null)+"\t"+ logMesg);
//		System.out.println("work:\t"+this);
		this.myprocess.stopLoop();//作用于MyProcess 中的waitFor 方法
		if(callbackWorker!=null){
			callbackWorker.complete(this.stringbuf.toString());
		}
		/*if(!isBreak()&&!this.resultTP.getText().endsWith("执行完成!")){
			try {
				// 追加
				docment.insertString(this.stringbuf.length(), SystemHWUtil.CRLF+"执行完成!", null);
			} catch (BadLocationException e) {
				GUIUtil23.warningDialog(e.getMessage());
				e.printStackTrace();
			}
		}*/
	}

	public MyProcess getProcess() {
		return myprocess;
	}

	public CallbackWorker getCallbackWorker() {
		return callbackWorker;
	}

	public void setCallbackWorker(CallbackWorker callbackWorker) {
		this.callbackWorker = callbackWorker;
	}

	public synchronized boolean isContinue() {
		return !isBreak;
	}

	public synchronized void setBreak(boolean isBreak) {
		this.isBreak = isBreak;
	}

}
