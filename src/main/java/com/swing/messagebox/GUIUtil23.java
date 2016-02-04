package com.swing.messagebox;

import javax.swing.JOptionPane;

import com.string.widget.util.ValueWidget;

public class GUIUtil23 {
	public static final String MESSAGE_WARNING = " 警告";
	public static final String MESSAGE_INFORMATION = " 消息";
	public static final String MESSAGE_ERROR = " 错误";

	private GUIUtil23() {
		throw new Error("Don't let anyone instantiate this class.");
	}

	/***
	 * Warning boxes
	 * 
	 * @param mesg
	 */
	public static void warningDialog(String mesg) {
		if(ValueWidget.isNullOrEmpty(mesg)){
			return;
		}
		String mesgResult = null;
		if (!mesg.matches("^[\\s]*<html>.*</html>[\\s]*$")) {
			mesgResult = "<html><font color=\"yellow\"  style=\"font-weight:bold;"
					+ "background-color:#666666\" >" + mesg + "</font></html>";
		}else{
			mesgResult=mesg;
		}
		JOptionPane.showMessageDialog(null, mesgResult, MESSAGE_WARNING,
				JOptionPane.WARNING_MESSAGE);
	}
	public static void alert(String mesg){
		warningDialog(mesg);
	}
	public static void errorDialog(Exception e){
		errorDialog(e.getMessage());
	}
	/***
	 * error
	 * 
	 * @param mesg
	 */
	public static void errorDialog(String mesg) {
		String mesgResult = null;
		if(ValueWidget.isNullOrEmpty(mesg)){
			return;
		}
		if (!mesg.matches("^[\\s]*<html>.*</html>[\\s]*$")) {
			mesgResult = "<html><font color=\"red\"  style=\"font-weight:bold;"
					+ "background-color:white\" >" + mesg + "</font></html>";
		}
		JOptionPane.showMessageDialog(null, mesgResult, MESSAGE_ERROR,
				JOptionPane.ERROR_MESSAGE);
	}
	public static void errorDialog(int mesg) {
		errorDialog(String.valueOf(mesg));
	}
	/***
	 * information
	 * 
	 * @param mesg
	 */
	public static void infoDialog(String mesg) {
		if(ValueWidget.isNullOrEmpty(mesg)){
			return;
		}
		String mesgResult = null;
		if (!mesg.matches("^[\\s]*<html>.*</html>[\\s]*$")) {
			mesgResult = "<html><font color=\"green\"  style=\"font-weight:bold;\" >"
					+ mesg + "</font></html>";
		}
		JOptionPane.showMessageDialog(null, mesgResult, MESSAGE_INFORMATION,
				JOptionPane.INFORMATION_MESSAGE);
	}
	public static void infoDialog(int mesg) {
		infoDialog(String.valueOf(mesg));
	}
}
