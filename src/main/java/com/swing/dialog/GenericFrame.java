package com.swing.dialog;

import com.common.MyTask;
import com.common.dict.Constant2;
import com.common.util.SystemHWUtil;
import com.io.hw.file.util.FileUtils;
import com.string.widget.util.ValueWidget;
import com.swing.component.ComponentUtil;
import com.time.util.TimeHWUtil;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import java.util.Timer;

public class GenericFrame extends AbstractFrame {

	/***
	 * 定时关闭的时间[unit:second]
	 */
	public static final int MILLISECONDS_WAIT_WHEN_BLUR = 7200;
	private static final long serialVersionUID = 3160202220512707196L;
	protected Timer timer = new Timer();
	protected MyTask task = null;
	protected boolean isLocked = false;
	/***
	 * 是否执行定时任务
	 */
	protected boolean isTiming=true;
	/***
	 * 窗口是否处于活跃状态
	 */
	protected boolean isActived22;
	protected Properties  props= new Properties();
	/***
	 * 配置文件
	 */
	protected File configFile;
	/***
	 * 日志文件,不是log4j
	 */
	protected File logFile2;

	/***
	 * 必须执行launchFrame ,layout3才会被调用
	 * @param contentPane
     */
	@Override
	public void layout3(Container contentPane) {
		
	}

	public void init33(final GenericFrame frame) {
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowActivated(WindowEvent e) {
				setActived22(true);
				System.out.println("window Activated");
				if (task != null) {
//					System.out.println("取消任务");
					task.cancel();
					task = null;
				}
				GenericFrame.this.setTiming(true);//恢复,不然后面就不会定时了.
				
				super.windowActivated(e);
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				setActived22(false);
//				System.out.println("window Deactivated");
				if(isTiming){
					windowDeactivated2(frame);
				}
				super.windowDeactivated(e);
			}

			@Override
			public void windowGainedFocus(WindowEvent e) {
				System.out.println("window GainedFocus");
				super.windowGainedFocus(e);
			}

			@Override
			public void windowLostFocus(WindowEvent e) {
				System.out.println("window LostFocus");
				super.windowLostFocus(e);
			}
			/****
			 * 关闭窗口前的收尾操作
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("closing....");
				beforeDispose();
				super.windowClosing(e);
			}
		});
	}
	protected void windowDeactivated2(GenericFrame frame){
		if (isLocked) {// over three times and is still locked,meanwhile use
			// try to log in
			if (task != null) {
//				System.out.println("取消任务");
				task.cancel();
				task = null;
			}
		} else {// first into this if clause(if (timesFail >=
				// LoginUtil.MAX_LOGIN_FAIL_TIMES ))
			task = null;
		}
		if (timer == null) {
			timer = new Timer();
		}
	
		if (task == null) {
			task = new MyTask(frame);
		}
		timer.schedule(task, MILLISECONDS_WAIT_WHEN_BLUR*1000);
//		System.out.println("开始计时(second):"+TimeHWUtil.formatDateZhAll(TimeHWUtil.getCurrentTimestamp()));
//		System.out.println("定时时间(second):"+MILLISECONDS_WAIT_WHEN_BLUR);
		isLocked = true;
	}
	public MyTask getTask() {
		return task;
	}

	public void setTask(MyTask task) {
		this.task = task;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	public boolean isTiming() {
		return isTiming;
	}

	public void setTiming(boolean isTiming) {
		this.isTiming = isTiming;
	}

	public synchronized boolean isActived22() {
		return isActived22;
	}

	public synchronized void setActived22(boolean isActived) {
		this.isActived22 = isActived;
	}

	/***
	 * 默认实现
	 */
	@Override
	public void dragResponse(List<File> list,Component component) {
		DialogUtil.dragResponse(list, component);
	}

	/***
	 * Ctrl+S 保存配置文件
	 */
	@Override
	protected void saveConfig() {
	}
	public Properties getProps() {
		return props;
	}
	
	public String getPropValue(String key){
		if(ValueWidget.isNullOrEmpty(props)||ValueWidget.isNullOrEmpty(key)){
			return null;
		}
		return this.props.getProperty(key);
	}
	/***
	 * 根据properties配置文件来恢复现场
	 * @param dirTextField
	 * @param key
	 */
	protected void setSwingInput(JTextComponent dirTextField,String key){
		String input_dir=getPropValue(key);
		if(!ValueWidget.isNullOrEmpty(input_dir)){
			dirTextField.setText(input_dir);
		}
	}
	/***
	 * 
	 * @param comb
	 * @param key : properties 的key
	 */
	public void fillComboBox(JComboBox<String> comb,String key){
		String java_path=getPropValue(key);
		ComponentUtil.fillComboBox(comb, java_path);
	}
	/***
     * 以;;分隔
     * @param prop_key
     * @param tf
     * @param freshCombox : 只有增加新的项时才刷新
     */
    public void setCombox(String prop_key,JTextComponent tf,JComboBox<String>comboBox,boolean freshCombox){
        String rootPath=tf.getText();
        if(ValueWidget.isNullOrEmpty(rootPath)){
        	return;
        }
        String roots=props.getProperty(prop_key);
        if(ValueWidget.isNullOrEmpty(roots)){
        	roots=rootPath;
        }else{
        	String roots_old[]=roots.split(Constant2.SHAREDPICDIVISION);
        	int index;
        	String urls_new[] = null;
			if ((index = SystemHWUtil.indexOf(roots_old, rootPath)) == SystemHWUtil.NEGATIVE_ONE) {//不包含
				roots=rootPath+Constant2.SHAREDPICDIVISION+roots;//把新增的item插入到最前面
        		if(freshCombox){
        			comboBox.removeAllItems();
            		ComponentUtil.fillComboBox(comboBox, roots);
        		}
        		String urls_old2[]=roots.split(Constant2.SHAREDPICDIVISION);
            	urls_new=SystemHWUtil.unique(urls_old2);
        	}else if(index!=0){//互换位置
        		String tmp=roots_old[index];
        		roots_old[index]=roots_old[0];
        		roots_old[0]=tmp;
        		urls_new=roots_old;
        	}else{
        		urls_new=roots_old;
        	}
        	
        	roots=StringUtils.join(urls_new, Constant2.SHAREDPICDIVISION);
        }
        if(!ValueWidget.isNullOrEmpty(roots)){
        	props.setProperty(prop_key, roots);
        }
    }
    /***
     * 关闭窗口之前执行的操作<br>
     * 善后,现场保留
     */
    public void beforeDispose(){
    	
    }
    /***
	 * 追加内容到日志
	 * @param content
	 */
	public void appendStr2LogFile(final String content){
		appendStr2LogFile(content, true);
	}
	/***
	 * 追加内容到日志
	 * @param content
	 */
	public void appendStr2LogFile(final String content,final boolean isCloseOutput ){
		if(!ValueWidget.isNullOrEmpty(content)){
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						FileUtils.appendStr2File(logFile2, TimeHWUtil.getCurrentFormattedTime()+SystemHWUtil.CRLF, SystemHWUtil.CHARSET_UTF, false);
						FileUtils.appendStr2File(logFile2, content+SystemHWUtil.CRLF, SystemHWUtil.CHARSET_UTF, isCloseOutput);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}).start();
		}
	}
}
