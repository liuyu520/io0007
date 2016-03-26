package com.swing.dialog;

import com.common.bean.DialogBean;
import com.common.bean.FindTxtResultBean;
import com.common.util.SystemHWUtil;
import com.io.hw.file.util.FileUtils;
import com.string.widget.util.ValueWidget;
import com.swing.dialog.inf.DialogInterface;
import com.swing.dialog.toast.ToastMessage;
import com.swing.messagebox.GUIUtil23;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public final class DialogUtil {
	private DialogUtil() {
		throw new Error("Don't let anyone instantiate this class.");
	}

	public static boolean browser3(JTextComponent field, int selectMode,
			Component comp) {
		return browser3(field, selectMode, comp, null);

	}

	/***
	 * open a save dialog.
	 * 
	 * @param field
	 * @param comp
	 * @param selectFile
	 * @return
	 */
	public static DialogBean showSaveDialog(JTextField field, Component comp,
			File selectFile) {
		JFileChooser file = new JFileChooser();
		file.setDialogType(JFileChooser.SAVE_DIALOG);
		if (!ValueWidget.isNullOrEmpty(selectFile)) {
			file.setSelectedFile(/* new File("/home/whuang2/bin") */selectFile);
		}
		DialogBean bean = new DialogBean();
		int result = file.showSaveDialog(comp);
		// System.out.println(" :" + resule);
		// if (resule == JFileChooser.DIRECTORIES_ONLY) {
		// String fileName = file.getSelectedFile().getName();
		if (result != JFileChooser.APPROVE_OPTION) {
			bean.setSuccess(false);
			return bean;
		}
		File selectedFile = file.getSelectedFile();
		String dir = null;
		if (selectedFile != null) {
			dir = selectedFile.getAbsolutePath();
			bean.setSelectedFile(selectedFile);
			if (!ValueWidget.isNullOrEmpty(field)) {
				field.setText(dir);
			}
			bean.setSuccess(true);
		} else {
			bean.setSuccess(false);
		}
		return bean;
	}

	/***
	 * 
	 * @param field
	 * @param selectMode
	 * @param comp
	 * @param selectFile
	 * @return
	 */
	public static boolean browser3(JTextComponent field, int selectMode,
			Component comp, File selectFile) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(selectMode);
		if (null == selectFile) {
			String text=field.getText();
			File tmpFile=new File(text);
			if(tmpFile.exists()){
				selectFile=tmpFile;
			}
		}
		if (null != selectFile) {
			fileChooser.setSelectedFile(selectFile);
		}
		/* int resule = */fileChooser.showOpenDialog(comp);
		// System.out.println(" :" + resule);
		// if (resule == JFileChooser.DIRECTORIES_ONLY) {
		// String fileName = file.getSelectedFile().getName();
		File selectedFile = fileChooser.getSelectedFile();
		String dir = null;
		if (selectedFile != null) {
			dir = selectedFile.getAbsolutePath();
			// int isOpen = JOptionPane.showConfirmDialog(null, dir, " ",
			// JOptionPane.YES_OPTION);
			// if (isOpen == 0)
			// {
			field.setText(dir);
			return true;
			// System.out.println(fullContent);
			// textArea.setText(fullContent);
			// }
			// else if (isOpen == 1)
			// {
			// // System.out.println(" ...");
			// }
		}
		return false;
	}

	/***
	 * 
	 * @param selectMode
	 * @param frame
	 *            : parent component
	 * @return
	 */
	public static DialogBean browser4(int selectMode, JFrame frame,
			File selectFile) {
//		Boolean bool = null;
		JFileChooser file = new JFileChooser();
		file.setFileSelectionMode(selectMode);
		if (!ValueWidget.isNullOrEmpty(selectFile)) {
			file.setSelectedFile(selectFile);
		}
		DialogBean bean = new DialogBean();
		int resule = file.showOpenDialog(frame);/* JFileChooser.APPROVE_OPTION */
		if(resule != JFileChooser.APPROVE_OPTION){
			bean.setSuccess(false);
			return bean;
		}
		File selectedFile = file.getSelectedFile();
		String dir = null;
		File file4 = null;
		
		if (selectedFile != null) {
			dir = selectedFile.getAbsolutePath();
			bean.setSelectedFile(selectedFile);
			// int isOpen = JOptionPane.showConfirmDialog(null, dir, " ",
			// JOptionPane.YES_OPTION);
			// if (isOpen == 0)
			// {
			// System.out.println("dir:"+dir);
//			file4 = new File(dir);
//			if (!file4.exists()) {
//				bool = false;
//			} else {
//				bool = true;
//			}
			bean.setSuccess(true);
		} else {
			bean.setSuccess(false);
		}
		return bean;
	}

	/***
	 * 设置透明度.
	 * 
	 * @param f_data
	 */
	public static void setWindowOpacity3(Window window, float f_data) {
		try {
			Class clazz = Class.forName("com.sun.awt.AWTUtilities");
			Method method3 = clazz.getDeclaredMethod("setWindowOpacity",
					java.awt.Window.class, float.class);
			method3.setAccessible(true);
			method3.invoke(null, new Object[] { window, f_data });
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		// com.sun.awt.AWTUtilities.setWindowOpacity(this, 0.5f);
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public static void setStatus_encoding(Container c) {
		JPanel bottomPane = new JPanel();
		bottomPane.setFocusable(true);
		bottomPane.add(getEncodingLabel());
		c.add(bottomPane, BorderLayout.SOUTH);
	}
	/***
	 * 获取显示当前编码和操作系统的label
	 * @return
	 */
	public static JLabel getEncodingLabel(){
		JLabel statusLabel = new JLabel();
		statusLabel
				.setText("<html>current os : <font color=\"red\">"
						+ SystemHWUtil.OSNAME
						+ "</font> &nbsp;&nbsp;&nbsp;&nbsp; current java encoding:<font color=\"red\">"
						+ SystemHWUtil.CURR_ENCODING + "</font></html>");
		statusLabel.setFocusable(true);
		statusLabel.setRequestFocusEnabled(true);
		
		return statusLabel;
	}

	/***
	 * focus and selectAll
	 * 
	 * @param textfield
	 */
	public static void focusSelectAllTF(JTextField textfield) {
		textfield.requestFocus();
		if (!ValueWidget.isNullOrEmpty(textfield.getText())) {
			textfield.selectAll();
		}
	}
	/***
	 * 先聚焦,后全部选中
	 * @param ta
	 */
	public static void focusSelectAllTF(JTextArea ta) {
		ta.requestFocus();
		if (!ValueWidget.isNullOrEmpty(ta.getText())) {
			ta.selectAll();
		}
	}
	public static boolean verifyTFEmpty(JTextField sourceFileTF, String title){
		return verifyTFEmpty(sourceFileTF, title, false);
	}
	/***
	 * To determine whether a text box is empty
	 * 
	 * @param sourceFileTF
	 * @param title
	 * @return
	 */
	public static boolean verifyTFEmpty(JTextField sourceFileTF, String title,boolean custome) {
		String targetFile_dir = sourceFileTF.getText();
		if (ValueWidget.isNullOrEmpty(targetFile_dir)) {
			String tips=null;
			if(custome){
				tips=title;
			}else{
				tips=title
						+ " can not be empty,please select again  !";
			}
			GUIUtil23.warningDialog(tips);
			DialogUtil.focusSelectAllTF(sourceFileTF);
			return false;
		}
		return true;
	}
	/***
	 * To determine whether a text box is empty
	 * 
	 * @param sourceFileTA
	 * @param title
	 * @return
	 */
	public static boolean verifyTFEmpty(JTextArea sourceFileTA, String title) {
		String targetFile_dir = sourceFileTA.getText();
		if (ValueWidget.isNullOrEmpty(targetFile_dir)) {
			GUIUtil23.warningDialog(title
					+ " can not be empty,please select again  !");
			DialogUtil.focusSelectAllTF(sourceFileTA);
			return false;
		}
		return true;
	}

	/***
	 * To determine whether a text box is empty,whether File exists.
	 * 
	 * @param sourceFileTF
	 * @param title
	 * @return
	 */
	public static boolean verifyTFAndExist(JTextField sourceFileTF, String title) {
		if (verifyTFEmpty(sourceFileTF, title)) {
			String targetFile_dir = sourceFileTF.getText();
			File srcfile = new File(targetFile_dir);
			if (!srcfile.exists()) {
				GUIUtil23.warningDialog("\" "+title
						+ "\" does not exist,please select again!");
				DialogUtil.focusSelectAllTF(sourceFileTF);
				return false;
			}else{
				return true;
			}
		} else {
			return false;
		}
	}
	/***
	 * 校验文本框不为空,且是数字,可以有小数点
	 * @param sourceFileTF
	 * @param title
	 * @return
	 */
	public static boolean verifyTFAndDouble(JTextField sourceFileTF, String title) {
		if (verifyTFEmpty(sourceFileTF, title)) {
			String targetFile_dir = sourceFileTF.getText();
			if(ValueWidget.isDouble(targetFile_dir)){
				return true;
			}else{
				GUIUtil23.warningDialog("必须是数字(可以包含小数点)");
//				sourceFileTF.requestFocus();
//				sourceFileTF.selectAll();
				DialogUtil.focusSelectAllTF(sourceFileTF);
				return false;
			}
		} else {
			return false;
		}
	}
	/***
	 * 校验文本框不为空,且是数字<br>通过正则表达式判断
	 * @param sourceFileTF
	 * @param title
	 * @return
	 */
	public static boolean verifyTFAndInteger(JTextField sourceFileTF, String title) {
		if (verifyTFEmpty(sourceFileTF, title)) {
			String targetFile_dir = sourceFileTF.getText();
			if(ValueWidget.isInteger(targetFile_dir)){
				return true;
			}else{
				GUIUtil23.warningDialog("必须是数字");
//				sourceFileTF.requestFocus();
//				sourceFileTF.selectAll();
				DialogUtil.focusSelectAllTF(sourceFileTF);
				return false;
			}
		} else {
			return false;
		}
	}
	/***
	 * 校验是否是IP
	 * @param sourceFileTF
	 * @param title
	 * @return
	 */
	public static boolean verifyTFAndIP(JTextField sourceFileTF, String title) {
		if (verifyTFEmpty(sourceFileTF, title)) {
			String targetFile_dir = sourceFileTF.getText();
			if(ValueWidget.isValidIP(targetFile_dir)){
				return true;
			}else{
				GUIUtil23.warningDialog("必须是数字");
//				sourceFileTF.requestFocus();
//				sourceFileTF.selectAll();
				DialogUtil.focusSelectAllTF(sourceFileTF);
				return false;
			}
		} else {
			return false;
		}
	}
	public static void lookAndFeel2(){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
	/***
	 * mac 样式
	 */
	public static void lookAndFeelIos(){
		try {
			javax.swing.UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
//			org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
//			UIManager.put("RootPane.setupButtonVisible", false);
//			javax.swing.UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
			
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}//最好看
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/***
	 * 获取最顶层的Frame
	 * @param parent
	 * @return
	 */
	public static JFrame getTopFrame(JComponent parent){
		if(ValueWidget.isNullOrEmpty(parent)){
			return null;
		}
		Container c=parent.getParent();
		while(!ValueWidget.isNullOrEmpty(c)){
			if(c instanceof GenericFrame){
				return (JFrame)c;
			}
			c=c.getParent();
		}
		return null;
	}
	public static JButton getBrowserBtn(String btnLabel, final JTextField tf, final int selectMode, final JComponent parent){
		JButton browserSourceBtn=new JButton(btnLabel); 
		browserSourceBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// System.out.println("source");
//				System.out.println(parent);
//				System.out.println(parent.getParent());
				boolean isSuccess =browserFile(tf, selectMode, parent);
				
				// if (isSuccess)
				// {
				// targetFileTF.setText(SystemUtil.getParentDir(sourceFileTF
				// .getText()));
				// }
			}
		});
		return browserSourceBtn;
	}
	public static boolean browserFile(JTextComponent tf,int selectMode,JComponent parent){
		JFrame frame22=getTopFrame(parent);
		if( !ValueWidget.isNullOrEmpty(frame22) &&(frame22 instanceof GenericFrame)){
			GenericFrame frame=(GenericFrame)frame22;
			frame.setTiming(false);
		}
		return DialogUtil.browser3(tf,
				selectMode, parent);
	}
	public static JButton getBrowserBtn(String btnLabel, final JTextField tf, final int selectMode){
		return getBrowserBtn(btnLabel, tf, selectMode, null);
	}
	public static JButton getBrowserFileBtn(String btnLabel, final JTextField tf, final JComponent parent){
		return getBrowserBtn(btnLabel, tf, JFileChooser.FILES_ONLY, parent);
	}
	public static JButton getBrowserFolderBtn(String btnLabel, final JTextField tf, final JComponent parent){
		return getBrowserBtn(btnLabel, tf, JFileChooser.DIRECTORIES_ONLY, parent);
	}
	
	/***
	 * 按Alt+Enter时,用户名文本框聚焦,<br>按Esc 文本框失去焦点,并变为不可编辑
	 * @param tf :密码输入框
	 */
	public static void addKeyListener22(final JTextComponent tf,final JTextComponent tf2){
		tf.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if ((e.getKeyCode() == KeyEvent.VK_ESCAPE)
						) {
//					System.out.println("esc");
					tf.setEditable(false);
				}else if ((e.getKeyCode() == KeyEvent.VK_ENTER)
						&& (((InputEvent) e )
								.isAltDown())) {
					if(!ValueWidget.isNullOrEmpty(tf2)){
						if (!tf2.isEditable()) {
							tf2.setEditable(true);
						}
						tf2.requestFocus();
						tf2.selectAll();
					}
				}
			}
		});
	}
	/***
	 * 按Esc 文本框失去焦点
	 * @param tf
	 */
	public static void addKeyListener22(final JTextComponent tf){
		addKeyListener22(tf, null);
	}
	
	/***
	 * 拖拽文件到文本框
	 * @param component
	 * @param tf : 场景:拖拽到label或者按钮时,则把值设置到后面的文本框tf中
	 */
	public static void drag(final Component component,final Component tf,final DialogInterface dialogInterface)// 定义的拖拽方法
	{
		// panel表示要接受拖拽的控件
		new DropTarget(component, DnDConstants.ACTION_COPY_OR_MOVE,
				new DropTargetAdapter() {
					@Override
					public void drop(DropTargetDropEvent dtde)// 重写适配器的drop方法
					{
						try {
							if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor))// 如果拖入的文件格式受支持
							{
								dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);// 接收拖拽来的数据
								List<File> list = (List<File>) (dtde
										.getTransferable()
										.getTransferData(DataFlavor.javaFileListFlavor));
//								String temp = "";
//								for (File file : list)
//									temp += file.getAbsolutePath() + ";\n";
//								JOptionPane.showMessageDialog(null, temp);
								if(ValueWidget.isNullOrEmpty(tf)){
									dialogInterface.dragResponse(list,component);
							}else{
								dialogInterface.dragResponse(list,tf);
							}
								dtde.dropComplete(true);// 指示拖拽操作已完成
							} else {
								dtde.rejectDrop();// 否则拒绝拖拽来的数据
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}
	
	public static void dragResponse(List<File> list, Component component) {
		String filePath=list.get(0).getAbsolutePath();
		if(component instanceof  JTextComponent){
			JTextComponent text=(JTextComponent)component;
			//把文本框的内容设置为拖拽文件的全路径
			text.setText(filePath);
		}
		
	}

    /***
     * 启用系统默认浏览器来打开网址。
     *
     * @param selectContent
     */
    public static void openBrowser(String selectContent) {
        try {
            URI uri = new URI(selectContent);
            Desktop.getDesktop().browse(uri);
        } catch (URISyntaxException e2) {
        	ToastMessage.toast(e2.getMessage(), 2000, Color.RED);
            e2.printStackTrace();
        } catch (IOException e2) {
            ToastMessage.toast(e2.getMessage(), 2000, Color.RED);
            e2.printStackTrace();
        }
    }

	public static FindTxtResultBean searchText(JTextComponent area2, int startIndex, String keyword) {
		String content = area2.getText();
        if (!ValueWidget.isNullOrEmpty(keyword)) {
			FindTxtResultBean findTxtResultBean = SystemHWUtil.findStr(content, keyword, startIndex);
			if (findTxtResultBean.getCount() > 0) {
				area2.setSelectionStart(findTxtResultBean.getFoundIndex() - keyword.length());
				area2.setSelectionEnd(findTxtResultBean.getFoundIndex());
				findTxtResultBean.setKeyWord(keyword);
				return findTxtResultBean;
			} else {
				ToastMessage.toast("没有搜索到:" + keyword, 1000, Color.red);
			}
        }
		return null;
	}
	public static ImageIcon getImageIcon(String resourcePath, Class<?> clazz) throws IOException {
		String slash="/";
		if(!resourcePath.contains(slash)&& SystemHWUtil.findStr3(resourcePath, ".").getCount()>1){
			//兼容:com.common.jn.img.path.png ,把.转化为/,除了最后一个点
			resourcePath=SystemHWUtil.replaceStrExceptLast(resourcePath, ".", slash );
		}
		if (resourcePath.startsWith(slash)) {// eg
			// "/com/pass/img/posterous_uploader.png"
			URL url = clazz/*this.getClass()*/.getResource(resourcePath);
			ImageIcon icon = new ImageIcon(url);
			return icon;
		} else {// eg "com/pass/img/posterous_uploader.png"
			InputStream is = clazz.getClassLoader()
					.getResourceAsStream(resourcePath);
			if (null == is) {
				return null;
			}
			BufferedInputStream isr = new BufferedInputStream(is);
			byte[] b = null;
			/*
			try {
				b = new byte[isr.available()];
				isr.read(b);
			} catch (IOException e2) {
				e2.printStackTrace();
			} finally {
				isr.close();
				is.close();
			}
			*/
			b = FileUtils.readBytesFromInputStream(isr, isr.available(),true);
			if(b.length==0){//b can not be null
				ToastMessage.toast("文件 "+resourcePath+" 内容为空",10000, Color.red);
				return null;
			}
			ImageIcon imageIcon = new ImageIcon(b);
			return imageIcon;
		}
	}
	public static void showMaximizeDialog(final JComponent area2) {
		showMaximizeDialog(area2,true);
	}
	public static void showMaximizeDialog(final JComponent area2,final boolean isNeedScrollPane) {
		final Container parent = area2.getParent();
		final JLabel waitingLabel=new JLabel("请稍后");
		GenericDialog dialog = new GenericDialog() {
			@Override
			public void layout3(Container contentPane) {
				setTitle("窗口最大化");
				super.layout3(contentPane);
				setModal(true);
				if(isNeedScrollPane){
					contentPane.add(new JScrollPane(area2));
				}else{
					contentPane.add(area2);
				}
				
				parent.add(waitingLabel);
				addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						super.windowClosing(e);
						parent.remove(waitingLabel);
						parent.add(area2);
						parent.repaint();
					}
				});
				fullScreen();
				parent.repaint();
				JFrame frame= DialogUtil.getTopFrame(area2);
				if(null!=frame){
					frame.repaint();
				}
			}
		};
		dialog.launchFrame();
	}

}
	
