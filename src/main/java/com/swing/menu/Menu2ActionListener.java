package com.swing.menu;

import com.JSON_java.JSONObject;
import com.JSON_java.XML;
import com.common.util.ImageHWUtil;
import com.common.util.SystemHWUtil;
import com.common.util.WindowUtil;
import com.io.hw.json.JSONHWUtil;
import com.string.widget.util.RegexUtil;
import com.string.widget.util.ValueWidget;
import com.swing.component.ComponentUtil;
import com.swing.component.TextCompUtil2;
import com.swing.dialog.CustomDefaultDialog;
import com.swing.dialog.DialogUtil;
import com.swing.dialog.SearchInputDialog;
import com.swing.dialog.toast.ToastMessage;
import com.swing.messagebox.GUIUtil23;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/***
 * 文本域的右键菜单响应事件.
 * 
 * @author huangwei
 *
 */
public class Menu2ActionListener implements ActionListener {
	private JTextComponent area2;

	public Menu2ActionListener(JTextComponent area2) {
		super();
		this.area2 = area2;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		if (command.equals(MenuUtil2.ACTION_STR_DELETE_CONTENT)) {
			// System.out.println("delete word");
			/*if (area2 instanceof JTextPane) {
				JTextPane tp = (JTextPane) area2;
				tp.replaceSelection("");
			} else {*/
				area2.replaceSelection("");
//			}
		} else if (command.equals(MenuUtil2.ACTION_STR_DELETE_ALL_CONTENT)) {
			area2.selectAll();

			if (area2 instanceof JTextPane) {
				JTextPane tp = (JTextPane) area2;
				tp.replaceSelection("");
				tp.setText("");
			} else {
				area2.replaceSelection("");
			}
		} else if (command.equals(MenuUtil2.ACTION_STR_SELECT_ALL_CONTENT)) {
			if (area2 != null) {
				area2.selectAll();
				// System.out.println("select all");
			}
		} else if (command.equals(MenuUtil2.ACTION_STR_COPY)) {
			String selectContent = area2.getSelectedText();
			if (ValueWidget.isNullOrEmpty(selectContent)) {
				return;
			}
			WindowUtil.setSysClipboardText(selectContent);
		} else if (command.equals(MenuUtil2.ACTION_STR_COPY_ALL)) {
			String selectContent = area2.getText();
			if (ValueWidget.isNullOrEmpty(selectContent)) {
				return;
			}
			WindowUtil.setSysClipboardText(selectContent);
		} else if (command.equals(MenuUtil2.ACTION_STR_PASTE)) {
			String content = WindowUtil.getSysClipboardText();
			if (content == null || content.equals("")) {
				return;
			}
//			int caret = area2.getCaretPosition();
			area2.replaceSelection(content);
			/*if (area2 instanceof JTextArea) {
				JTextArea ta2 = (JTextArea) area2;
				
				ta2.insert(content, caret);
			} else {
				Document doc = null;
				if (area2 instanceof JTextField) {
					JTextField tf2 = (JTextField) area2;

					doc = tf2.getDocument();
				} else if (area2 instanceof JTextPane) {
					JTextPane tp = (JTextPane) area2;
					doc = tp.getDocument();
				}
				try {
					doc.insertString(caret, content, null);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}*/
		} else if (command.equals(MenuUtil2.ACTION_STR_REPLACE_ALL)) {
			String content = WindowUtil.getSysClipboardText();
			if (content == null || content.equals("")) {
				return;
			}
			area2.setText(content);
		} else if (command.equals(MenuUtil2.ACTION_STR_LENGTH)) {
			String text = area2.getText();
			GUIUtil23.infoDialog(String.valueOf(text.length()));
		} else if (command.equals(MenuUtil2.ACTION_STR_DELETE_CRLF)) {
			String text = area2.getText();
			if(!ValueWidget.isNullOrEmpty(text)){
				text=SystemHWUtil.deleteAllCRLF(text);
				area2.setText(text);
			}
		}else if (command.equals(MenuUtil2.ACTION_STR_TRIM)) {
			String text = area2.getText();
			if(text!=null){
				text=text.trim();
				area2.setText(text.trim());
			}
        } else if (command.equals("删除全局空格")) {
            String text = area2.getText();
            if (text != null) {
                //也可以使用 RegexUtil public static String filterBlank(String input) {
                text = text.replace(" ", SystemHWUtil.EMPTY).replace("\t", SystemHWUtil.EMPTY);
                area2.setText(text);
            }
        } else if (command.equals(MenuUtil2.ACTION_STR2UPPER_CASE)) {
            String text = area2.getText();
			if(text!=null){
				text=text.toUpperCase();
				area2.setText(text);
			}
		}else if (command.equals(MenuUtil2.ACTION_STR2LOWER_CASE)) {
			String text = area2.getText();
			if(text!=null){
				text=text.toLowerCase();
				area2.setText(text);
			}
		}else if (command.equals(MenuUtil2.ACTION_DOUBLE_QUOTES_ESCAPE)) {
			//对选中区域的双引号进行转义
			String selectContent = area2.getSelectedText();
			if (ValueWidget.isNullOrEmpty(selectContent)) {
				return;
			}
			String content=selectContent.replace("\"", "\\\"");
			area2.replaceSelection(content);
			
		}else if (command.startsWith(MenuUtil2.ACTION_STR_EXIT)) {// 退出应用程序
			JFrame frame = DialogUtil.getTopFrame(area2);
			if (frame != null) {
				frame.dispose();
			}
			System.exit(0);
		}else if (command.startsWith(MenuUtil2.ACTION_DELETE_N)) {// 退出应用程序
			String content=area2.getText();
			if(ValueWidget.isNullOrEmpty(content)){
				return;
			}
			content=content.replace("[\\n]", SystemHWUtil.EMPTY);
			area2.setText(content);
		} else if (command.startsWith(MenuUtil2.ACTION_DELETE_N_R)) {// 删除所有的\\n\\r
			String content=area2.getText();
			if(ValueWidget.isNullOrEmpty(content)){
				return;
			}
			content = content.replace("\\n\\r", SystemHWUtil.EMPTY);
			area2.setText(content);
		} else if (command.startsWith(MenuUtil2.ACTION_DELETE_R_N)) {// 删除所有的\\r\\n
			String content = area2.getText();
			if (ValueWidget.isNullOrEmpty(content)) {
				return;
			}
			content = content.replace("\\r\\n", SystemHWUtil.EMPTY);
			area2.setText(content);
		}else if (command.startsWith(MenuUtil2.ACTION_URL_ENCODE)) {// 退出应用程序
            MenuUtil2.urlEncode(area2);

        }else if (command.startsWith(MenuUtil2.ACTION_URL_DECODE)) {// 退出应用程序
            MenuUtil2.urlDecode(area2);

        }else if (command.startsWith(MenuUtil2.ACTION_XML2JSON)) {// 退出应用程序
			String content=area2.getText();
			if(ValueWidget.isNullOrEmpty(content)){
				return;
			}
			JSONObject xmlJSONObj = XML.toJSONObject(content);
			String jsonPrettyPrintString = xmlJSONObj.toString(4);
			area2.setText(jsonPrettyPrintString);
			
		}else if (command.startsWith(MenuUtil2.ACTION_CREATE_MD5)) {// 计算MD5值
			String selectContent = area2.getText();
			if (ValueWidget.isNullOrEmpty(selectContent)) {
				return;
			}
			try {
				String md5=SystemHWUtil.getMD5(selectContent, SystemHWUtil.CURR_ENCODING);
				WindowUtil.setSysClipboardText(md5);
				ToastMessage.toast("已复制MD5值到剪切板",3000);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				GUIUtil23.errorDialog(e);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				GUIUtil23.errorDialog(e);
			}
			
		}else if (command.startsWith(MenuUtil2.ACTION_DELETE_DIGIT_OF_FRONT)) {//删除每行前面的数字
			String content=area2.getText();
			if(ValueWidget.isNullOrEmpty(content)){
				return;
			}
			String jsonPrettyPrintString = RegexUtil.deleteDigit(content);
			area2.setText(jsonPrettyPrintString);
			
		}else if (command.startsWith(MenuUtil2.ACTION_DELETE_NOTES)) {//删除每行前面的数字
			String content=area2.getText();
			if(ValueWidget.isNullOrEmpty(content)){
				return;
			}
			String jsonPrettyPrintString = RegexUtil.deleteNotes(content);
			area2.setText(jsonPrettyPrintString);
			
		}else if (command.equals(MenuUtil2.ACTION_IMAGE_COPY)) {//复制图片到剪切板
			String content=area2.getText();
			if(ValueWidget.isNullOrEmpty(content)){
				return;
			}
			BufferedImage img =ImageHWUtil.genericImage(area2, null, "jpg"/*picFormat*/);
			if(ValueWidget.isNullOrEmpty(img)){
				return;
			}
			ComponentUtil.setClipboardImage(area2.getParent(),img);
			ToastMessage.toast("复制图片到剪切板",3000);
		}else if (command.startsWith(MenuUtil2.ACTION_QUERY_STRING2JSON)) {
            MenuUtil2.queryString2Json(area2, false/*isSuppressWarnings*/, true/*isFurther*/);
        }else if (command.startsWith(MenuUtil2.ACTION_JSON2QUERY_STRING)) {
			//{"username":"whuang","age":23} -->username=whuang&age=23
            MenuUtil2.json2queryString(area2);
        }else if (command.equals(MenuUtil2.ACTION_IMAGE_COPY_SPECIFY_HEIGHT)) {//复制图片到剪切板
			String content=this.area2.getText();
			if(ValueWidget.isNullOrEmpty(content)){
				ToastMessage.toast("无内容,不会复制",2000,Color.red);
				return;
			}
			new Thread(new Runnable() {
				@Override
				public void run() {
					ComponentUtil.copyImage(area2, true);
				}
			}).start();
		}else if(command.equals("替换中文双引号")){
			String content=area2.getText();
			content=ValueWidget.replaceChinaQuotes(content);
			if(!ValueWidget.isNullOrEmpty(content)){
				area2.setText(content);
			}
		}else if(command.equals("& --> 换行")){
			String content=area2.getText();
			content=content.replaceAll("&", SystemHWUtil.CRLF);
			if(!ValueWidget.isNullOrEmpty(content)){
				area2.setText(content);
			}
		}else if(command.equals("以HTML显示")){
			String selectContent = area2.getSelectedText();
			if (ValueWidget.isNullOrEmpty(selectContent)) {
				return;
			}
			//弹出框显示HTML
			CustomDefaultDialog customDefaultDialog=new CustomDefaultDialog(selectContent,"显示HTML",true);
			customDefaultDialog.setVisible(true);
		}else if(command.equals("格式化json")){
            JSONHWUtil.formatJson(area2, false, null, false);
        }else if(command.equals(MenuUtil2.ACTION_TF_EDITABLE)){
			area2.setEditable(true);
			area2.setEnabled(true);
			area2.requestFocus();
		}else if(command.equals(MenuUtil2.ACTION_STR_OPEN_BROWSER)){
			String selectContent = area2.getSelectedText();
			if (ValueWidget.isNullOrEmpty(selectContent)) {
				return;
			}
			DialogUtil.openBrowser(selectContent);
		}else if(command.equals(MenuUtil2.ACTION_STR_SEARCH)){
			SearchInputDialog searchInputDialog = new SearchInputDialog(area2,null);
			searchInputDialog.setVisible(true);
		}else if(command.equals(MenuUtil2.ACTION_IMAGE_COPY_SPECIFY_WIDTH_HEIGHT)){
			TextCompUtil2.copyImgAction(area2);
		}
	}



}
