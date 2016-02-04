package com.swing.dialog;

import com.common.util.SystemHWUtil;
import com.common.util.WindowUtil;
import com.string.widget.util.ValueWidget;
import com.swing.component.AssistPopupTextPane;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/***
 * 功能:显示帮助信息
 * 调用方式:
 * <code>
 * CustomDefaultDialog customDefaultDialog=new CustomDefaultDialog("蜜罐系统",null);
			customDefaultDialog.setVisible(true);
 * </code>
 * @author huangweii
 * 2015年7月30日
 */
public class CustomDefaultDialog extends GenericDialog {

    public static final String contactEmail = "1287789687@qq.com";
    public static final String blog = "http://hw1287789687.iteye.com/";
    private static final long serialVersionUID = 2470499413256643952L;
//	public static final String helpDesc="";

	

	/**
	 * Create the frame.
	 */
	public CustomDefaultDialog(String helpDesc,String title,boolean isParseHtml) {
		if(helpDesc==null){
			helpDesc=SystemHWUtil.EMPTY;
		}
		if(ValueWidget.isNullOrEmpty(title)){
			title="帮助";
		}
		setTitle(title);
		setModal(true);
		setLoc(450, 400);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		AssistPopupTextPane textPane = new AssistPopupTextPane();
		textPane.setContentType("text/html; charset=UTF-8");
		String content=null;
		if(isParseHtml){
			content=helpDesc;
		}else{
			content=" <html lang=\"en\"> <head> <meta charset=\"UTF-8\"> <title></title> </head> <body style=\"font-family: 微软雅黑;\"> <div style=\"border: 1px solid #ea4748;\"> <ul style=\"list-style: outside none none\"> <li style=\"border-bottom: 1px solid green;padding-bottom: 5px;\"> <font style=\"width: 10%;float: left\">作者</font> <font style=\"font-weight: bolder;color: #ff00ff\">黄威</font> </li> <li style=\"border-bottom: 1px solid green;padding-bottom: 5px;background-color: #F0F6FF\"> <font style=\"width: 10%;float: left\">联系邮箱</font> <font style=\"font-weight: bolder;color: #ff00ff\">"+contactEmail+"</font> </li> <li style=\"border-bottom: 1px solid green;padding-bottom: 5px;\"> <font style=\"width: 10%;float: left\">百度账号</font> <font style=\"font-weight: bolder;color: #ff00ff\">hw763832948</font> </li> <li style=\"border-bottom: 1px solid green;padding-bottom: 5px;background-color: #F0F6FF\"> <font style=\"width: 10%;float: left\">备用邮箱</font> <font style=\"font-weight: bolder;color: #ff00ff\">huangweii@chanjet.com</font> </li> <li> <font style=\"width: 10%;float: left\">技术博客</font> <font style=\"font-weight: bolder;color: #ff00ff\">"+blog+"</font> </li> <li> <font style=\"width: 10%;float: left\">&nbsp;</font> <font style=\"font-weight: bolder;color: #ff00ff\">http://blog.csdn.net/hw1287789687</font> </li> </ul> <div > <h3 style=\"color: blue\">功能说明:</h3> <p> "+helpDesc+" </p> </div> </div> </body> </html>";
		}
		textPane.setText(content);
		textPane.setEditable(false);
		DefaultCaret caret = (DefaultCaret)textPane.getCaret();
    	caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		scrollPane.setViewportView(textPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		if(!isParseHtml){
			JButton copyButton_1 = new JButton("复制联系方式");
			copyButton_1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String content=contactEmail;
					if(!ValueWidget.isNullOrEmpty(content)){
						WindowUtil.setSysClipboardText(content);
					}
				}
			});
			panel.add(copyButton_1);
		}
		
		
		JButton closeButton = new JButton("关闭");
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CustomDefaultDialog.this.dispose();
			}
		});

		if(!isParseHtml){
			JButton button = new JButton("打开博客");
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
                    DialogUtil.openBrowser(blog);
                }
			});
			panel.add(button);
		}
		
		panel.add(closeButton);
		
		JButton maxButton = new JButton("最大化");
		maxButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CustomDefaultDialog.this.fullScreen();
			}
		});
		panel.add(maxButton);
	}

}
