package com.swing.dialog;

import com.common.bean.DialogBean;
import com.common.bean.FindTxtResultBean;
import com.common.util.SystemHWUtil;
import com.io.hw.file.util.FileUtils;
import com.string.widget.util.ValueWidget;
import com.swing.component.AssistPopupTextArea;
import com.swing.component.TextCompUtil2;
import com.swing.component.inf.IPlaceHolder;
import com.swing.dialog.inf.DialogInterface;
import com.swing.dialog.toast.ToastMessage;
import com.swing.event.EventHWUtil;
import com.swing.messagebox.GUIUtil23;
import com.time.util.TimeHWUtil;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (selectedFile == null) {
            bean.setSuccess(false);
            return bean;
        }
        dir = selectedFile.getAbsolutePath();
			bean.setSelectedFile(selectedFile);
			if (!ValueWidget.isNullOrEmpty(field)) {
				field.setText(dir);
			}
			bean.setSuccess(true);
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
        int result =
                fileChooser.showOpenDialog(comp);
//         System.out.println(" :" + result);
        // if (resule == JFileChooser.DIRECTORIES_ONLY) {
		// String fileName = file.getSelectedFile().getName();
		File selectedFile = fileChooser.getSelectedFile();
		String dir = null;
        //注意:选中了文件,但是点击[取消],返回值selectedFile不为null
        if (selectedFile == null || JOptionPane.YES_OPTION != result) {
            return false;
        }
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
		if (!ValueWidget.isNullOrEmpty(targetFile_dir)) {
			if (sourceFileTF instanceof IPlaceHolder) {
				IPlaceHolder placeHolder = (IPlaceHolder) sourceFileTF;
				targetFile_dir = placeHolder.getText2();
			}
		}
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

    /***
     * 只显示第一个文件
     * @param list
     * @param component
     */
    public static void dragResponse(List<File> list, Component component) {
        String filePath=list.get(0).getAbsolutePath();
        if (null != filePath) {
            filePath = filePath.trim();
        }
        if (!ValueWidget.isNullOrEmpty(filePath)) {
            if (component instanceof JTextComponent) {
                JTextComponent text = (JTextComponent) component;
                //把文本框的内容设置为拖拽文件的全路径
                text.setText(filePath);
            }
        }

    }

    /***
     * 显示所有的文件
     * @param list
     * @param component
     */
    public static void dragResponseAllFiles(List<File> list, Component component) {
        if (!ValueWidget.isNullOrEmpty(list)) {
            if (component instanceof JTextComponent) {
                StringBuffer filePathBuffer = new StringBuffer();
                for (File file : list) {
                    filePathBuffer.append(file.getAbsolutePath()).append(SystemHWUtil.CRLF);
                }
                JTextComponent text = (JTextComponent) component;
                //把文本框的内容设置为拖拽文件的全路径
                text.setText(filePathBuffer.toString().replaceAll(SystemHWUtil.CRLF + "$", SystemHWUtil.EMPTY));
            }
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
        if (!resourcePath.contains(slash) && SystemHWUtil.findStr3(resourcePath, SystemHWUtil.ENGLISH_PERIOD).getCount() > 1) {
            //兼容:com.common.jn.img.path.png ,把.转化为/,除了最后一个点
            resourcePath = SystemHWUtil.replaceStrExceptLast(resourcePath, SystemHWUtil.ENGLISH_PERIOD, slash);
        }
		if (resourcePath.startsWith(slash)) {// eg
			// "/com/pass/img/posterous_uploader.png"
            ImageIcon icon = getImageIconByClass(resourcePath, clazz);
            return icon;
        } else {// eg "com/pass/img/posterous_uploader.png",前面没有斜杠
            InputStream is = clazz.getClassLoader()
					.getResourceAsStream(resourcePath);
			if (null == is) {
                return getImageIconByClass(resourcePath, clazz);//2017.5.16 增加容错
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

    private static ImageIcon getImageIconByClass(String resourcePath, Class<?> clazz) {
        URL url = clazz/*this.getClass()*/.getResource(resourcePath);
        if (null == url) {
            return null;
        }
        return new ImageIcon(url);
    }

    /***
     * @param resourcePath : "/com/pass/img/posterous_uploader.png"
     * @throws IOException
     */
    public static void setIcon(JFrame jframe, String resourcePath, Class<?> clazz) throws IOException {
        ImageIcon imageIcon = DialogUtil.getImageIcon(resourcePath, clazz);
        if (null != imageIcon) {
            jframe.setIconImage(imageIcon.getImage());
            if (SystemHWUtil.isMacOSX) {//mac os
                com.apple.eawt.Application.getApplication().setDockIconImage(
                        imageIcon.getImage());
            }
        }
    }

    public static void showMaximizeDialog(final JComponent area2) {
		showMaximizeDialog(area2,true);
	}

    public static GenericDialog showMaximizeDialog(final JComponent area2, final boolean isNeedScrollPane) {
        final Container parent = area2.getParent();
		final JLabel waitingLabel=new JLabel("请稍后");
		GenericDialog dialog = new GenericDialog() {
            private JComponent textComp;
            KeyAdapter keyAdapter = new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    super.keyTyped(e);
                }

                /***
                 * command +W 关闭对话框
                 * @param e
                 */
                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
//                    System.out.println(e);
//                    System.out.println("close......");
//                    System.out.println(e.getModifiers()==4);
//                    System.out.println(e);
//                    System.out.println(e.getModifiers()==4);
//                    System.out.println(e.getKeyCode());
                    if ((e.getKeyCode() == KeyEvent.VK_W)/*KEY_PRESSED,keyCode=87,keyText=W,keyChar='w',modifiers=⌘,extModifiers=⌘*/
                            && (EventHWUtil.isControlDown(e)) && e.getID() == KeyEvent.KEY_PRESSED) {
                        System.out.println("close......");
//                        if(null!=keyAdapter)textComp.removeKeyListener(keyAdapter);
//                        keyAdapter=null;
                        closeDialog();
                        dispose();
                    }
                }
            };

			@Override
			public void layout3(Container contentPane) {
				setTitle("窗口最大化");
				super.layout3(contentPane);
				setModal(true);
                if (area2 instanceof JTextField) {//如果传入的area2 是单行文本框,则转化为文本域
                    JTextField textField = (JTextField) area2;
                    textComp = new AssistPopupTextArea();
                    ((AssistPopupTextArea) textComp).setLineWrap(true);
                    ((AssistPopupTextArea) textComp).setWrapStyleWord(true);
                    ((AssistPopupTextArea) textComp).setText(textField.getText());
                } else {
                    textComp = area2;
                }
                if(isNeedScrollPane){
                    contentPane.add(new JScrollPane(textComp));
                }else{
                    contentPane.add(textComp);
                }
				
				parent.add(waitingLabel);
				addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						super.windowClosing(e);
                        closeDialog();
                    }
				});
                textComp.addKeyListener(keyAdapter);
                fullScreen();
				parent.repaint();
                JFrame frame = DialogUtil.getTopFrame(textComp);
                if(null!=frame){
					frame.repaint();
				}
			}

            public void closeDialog() {
                parent.remove(waitingLabel);
                if (area2 instanceof JTextField) {//如果传入的area2 是单行文本框,则转化为文本域
                    JTextField textField = (JTextField) area2;
                    textField.setText(((AssistPopupTextArea) textComp).getText());
                }
                parent.add(area2);
                parent.repaint();
                area2.requestFocus();
                textComp.removeKeyListener(keyAdapter);
            }
        };
        if (area2 instanceof JTextComponent) {
            //此处调用了反射
            TextCompUtil2.setMaxDialog((JTextComponent) area2, dialog, JDialog.class);
        }
        dialog.launchFrame();
        return dialog;
    }

    /***
     * 追加内容到日志
     * @param content
     */
    public static void appendStr2LogFile(final String content, final File logFile2, final boolean isCloseOutput) {
        if (!ValueWidget.isNullOrEmpty(content)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        FileUtils.appendStr2File(logFile2, TimeHWUtil.getCurrentFormattedTime() + SystemHWUtil.CRLF, SystemHWUtil.CHARSET_UTF, false);
                        FileUtils.appendStr2File(logFile2, content + SystemHWUtil.CRLF, SystemHWUtil.CHARSET_UTF, isCloseOutput);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }

    /***
     * 选择文件
     * @param selectedFile2
     * @param title
     * @param qrCodePanel
     * @param picFormat
     * @return
     */
    public static File chooseFileDialog(File selectedFile2, String title, Component qrCodePanel, String picFormat) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (!ValueWidget.isNullOrEmpty(selectedFile2)) {
            chooser.setSelectedFile(selectedFile2);
        }
        Map<String, String> fileTypeMap = new HashMap();
        fileTypeMap.put("py", "python Files");
        fileTypeMap.put("java", "Java Files");
        fileTypeMap.put("js", "Javascript Files");
        String saveTips = fileTypeMap.get(picFormat);
        if (ValueWidget.isNullOrEmpty(saveTips)) {
            saveTips = "picture Files";
        }
        FileNameExtensionFilter filter = new FileNameExtensionFilter(saveTips/* 文件类型提示 */, picFormat, title);
        chooser.setFileFilter(filter);
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        chooser.setControlButtonsAreShown(true);
        chooser.setDialogTitle(title);
        //            chooser.setVisible(true);
        int result = -1;
        try {
            result = chooser.showSaveDialog(qrCodePanel);
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
//            e.printStackTrace();
        }

        System.out.println("chooseFileDialog New file:" + result);
        if (result == JOptionPane.OK_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            if (null == selectedFile) {
                return null;
            }
            if (!selectedFile.getAbsolutePath().endsWith(picFormat) && !ValueWidget.isNullOrEmpty(picFormat)) {//增加后缀名
                selectedFile = new File(selectedFile.getAbsolutePath() + SystemHWUtil.ENGLISH_PERIOD + picFormat);
            }
            return selectedFile;
        }
        return null;
    }
}
	
