package com.swing.component;

import com.common.MyTask;
import com.common.util.QRCodeUtil;
import com.common.util.SystemHWUtil;
import com.common.util.WindowUtil;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.io.hw.file.util.FileUtils;
import com.string.widget.util.ValueWidget;
import com.swing.dialog.DialogUtil;
import com.swing.dialog.GenericFrame;
import com.swing.dialog.GenericPanel;
import com.swing.event.QRCodeMenuActionListener;
import com.swing.menu.MenuUtil2;
import com.swing.messagebox.GUIUtil23;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.MouseInputAdapter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Timer;

public class QRCodePanel extends GenericPanel {

	public static final String statusInfo = "当前的字符编码是：%s , 二维码格式：%s";
	/***
	 * 请先生成二维码.
	 */
	public static final String ERROR_MESSAGE_GENERATEQR_FIRST="请先生成二维码.";
	public static final String read_qrcode_from_pic_format="jpg";
	/***
	 * 生成二维码输入框下面的下拉框
	 */
	public static final String PROP_KEY_QR_CODE_COMBOBOX="qr_code_combobox";
	private static final long serialVersionUID = 1101647354004006850L;
	private JPanel contentPane;
	private Timer timer = new Timer();
	private MyTask task = null;
	private boolean isLocked = false;
	/***
	 * 二维码的输入
	 */
	private AssistPopupTextArea inputQRTextArea;
	/***
	 * 二维码的显示区域
	 */
	private JLabel qrResultLabel;
	private String charset = SystemHWUtil.CHARSET_UTF;
	/***
	 * 二维码的输出格式
	 */
	private String picFormat = "jpg";
	/***
	 * 状态信息
	 */
	private JLabel statusLabel;
//	URL url = this.getClass().getResource("/com/qr/yj/img/qr.jpg");
	private byte[] QRbytes = null;
	/***
	 * 【生成二维码】按钮
	 */
	private JButton genQRbutton;
	/***
	 * 【保存】对话框选择的文件（历史记录）
	 */
	private File selectedFile;
	/***
	 * 是否修改过编码
	 */
	private boolean isModifyCharset=false;
	/***
	 * 上传ftp的菜单
	 */
	private JMenuItem fileM_uploadFtp;
	/***
	 * 用于复制图片到剪切板
	 */
	private Image image;
	/***
	 * 二维码的宽度
	 */
	private int qrImageWidth=300;
	private GenericFrame frame;
	private JComboBox<String> qrComboBox;
	private AssistPopupTextArea resultArea;
	private KeyListener keyListener = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			System.out.println("KeyEvent");
			System.out.println(e.getKeyCode());
			if ((e.getKeyCode() == KeyEvent.VK_EQUALS)
					&& (((InputEvent) e)
							.isControlDown())) {
				enlarge();
			}else if ((e.getKeyCode() == KeyEvent.VK_MINUS)
					&& (((InputEvent) e)
							.isControlDown())) {
				reduce();
			}
		}
	};

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QRCodePanel frame = new QRCodePanel();
					frame.setVisible(true);
					frame.inputQRTextArea.requestFocus();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

//	private void setIcon() {
//		ImageIcon icon = new ImageIcon(url);
//		this.setIconImage(icon.getImage());
//
//	}

	public QRCodePanel(GenericFrame frame) {
		super();
		this.frame = frame;
		layoutPanel();
	}

	/***
	 * setting menu
	 */
	private void setMenu2() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileM = new JMenu("File");

		JMenuItem fileM_close = new JMenuItem("close");
		fileM_uploadFtp = new JMenuItem("上传ftp");
		JMenuItem fileM_applyZip = new JMenuItem("应用最新zip");
		fileM_applyZip.setActionCommand("apply_zip");
		fileM_uploadFtp.setActionCommand("upload_ftp");
		fileM_uploadFtp.setEnabled(false);
		fileM_close.setActionCommand("close");



		fileM.add(fileM_applyZip);
		fileM.add(fileM_uploadFtp);
		fileM.add(fileM_close);

		JMenu charsetM = new JMenu("编码");
		JMenuItem fileM_utf8 = new JMenuItem(SystemHWUtil.CHARSET_UTF);





		// helpM.setEnabled(false);
//		this.setJMenuBar(menuBar);
	}

	/**
	 * Create the frame.
	 */
	public void layoutPanel() {
		DialogUtil.lookAndFeel2();
//		setMenu2();
//		init33();
//		setIcon();
//		this.setTitle(Constant.title);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setBounds(100, 50, 900, 680);
//		contentPane = new JPanel();
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setLayout(new BorderLayout(0, 0));
//		setContentPane(contentPane);

//		tabbedPane = new TabbedPane(JTabbedPane.TOP);
//		tabbedPane.setCloseButtonEnabled(true);


		JPanel panel = new JPanel();
		this.add(panel, BorderLayout.CENTER);
//		tabbedPane.addTab("二维码", null, this, null);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 100, 0/* 输入框 */, 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, 0.0, 1.0,
				Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0/* 输入框 */, 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		inputQRTextArea = new AssistPopupTextArea();
		inputQRTextArea.setText("Hello world");
		inputQRTextArea.setWrapStyleWord(true);
		inputQRTextArea.setLineWrap(true);

		//黏贴时触发[生成二维码]按钮的单击事件
		inputQRTextArea.getActionMap().put("Paste", new AbstractAction("Paste111") {
			private static final long serialVersionUID = -3548620001691220571L;

			public void actionPerformed(ActionEvent evt) {
				inputQRTextArea.paste();
				genQRbutton.doClick();
			}
		});
		inputQRTextArea.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
			}

			@Override
			public void focusGained(FocusEvent e) {
				if(ValueWidget.isNullOrEmpty(inputQRTextArea.getText())){
					inputQRTextArea.setText(WindowUtil.getSysClipboardText());
				}
			}
		});
		// 增加滚动条
		JScrollPane inputScroll = new JScrollPane(inputQRTextArea);

		Border borderResultTA = BorderFactory.createEtchedBorder(Color.white,
				new Color(148, 145, 140));
		TitledBorder resultTATitle = new TitledBorder(borderResultTA, "请输入网址");
		inputScroll.setBorder(resultTATitle);

		GridBagConstraints gbc_inputQRTextArea = new GridBagConstraints();
		gbc_inputQRTextArea.gridwidth = 3;
		gbc_inputQRTextArea.insets = new Insets(0, 0, 5, 0);
		gbc_inputQRTextArea.fill = GridBagConstraints.BOTH;
		gbc_inputQRTextArea.gridx = 0;
		gbc_inputQRTextArea.gridy = 0;
		panel.add(inputScroll, gbc_inputQRTextArea);

		qrComboBox = new JComboBox<String>();
		GridBagConstraints gbc_qrComboBox = new GridBagConstraints();
		gbc_qrComboBox.gridwidth = 3;
		gbc_qrComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_qrComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_qrComboBox.gridx = 0;
		gbc_qrComboBox.gridy = 1;
		panel.add(qrComboBox, gbc_qrComboBox);
		qrComboBox.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent arg0)
            {
                String  selectedPic=(String)qrComboBox.getSelectedItem();
                if(!ValueWidget.isNullOrEmpty(selectedPic)){
                	inputQRTextArea.setText(selectedPic);
                	generateQRAction(false);
                	System.out.println("addItemListener");
//                	genQRbutton.doClick();
                }
            }
        });
		qrComboBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String  selectedPort=(String)qrComboBox.getSelectedItem();
                if(!ValueWidget.isNullOrEmpty(selectedPort)&&!selectedPort.equals(inputQRTextArea.getText())){
                	inputQRTextArea.setText(selectedPort);
//                	generateQRAction(false);
                	System.out.println("addMouseListener");
//                	genQRbutton.doClick();
                }
			}
		});

		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.gridwidth = 3;
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 2;
		panel.add(panel_2, gbc_panel_2);

		JButton copyButton = new JButton("黏贴内容");

		copyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text=WindowUtil.getSysClipboardText();
				if(ValueWidget.isNullOrEmpty(text)){
					return;
				}
				inputQRTextArea.setText(text);
				genQRbutton.doClick();
			}
		});

		genQRbutton = new JButton("生成二维码(A)");
		genQRbutton.setMnemonic('A');
		genQRbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				qrResultLabel.setIcon(null);
				if (!DialogUtil.verifyTFEmpty(inputQRTextArea, "输入框")) {
					return;
				}
				generateQRAction(true);
			}
		});
		panel_2.add(genQRbutton);
		panel_2.add(copyButton);


		JButton cleanUpButton = new JButton("清空结果");
		cleanUpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resultArea.setText(SystemHWUtil.EMPTY);
			}
		});
		panel_2.add(cleanUpButton);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerLocation(getFullHeight()/2);
		splitPane.setDividerSize(8);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.gridwidth = 3;
		gbc_splitPane.insets = new Insets(0, 0, 5, 0);
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 3;
		panel.add(splitPane, gbc_splitPane);


		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);
		/*GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 4;
		panel.add(scrollPane, gbc_scrollPane);*/

		qrResultLabel = new JLabel();
		setPopupMenu(qrResultLabel);
//		addKeyListener22(QRCodePanel.this);
		URL url2 = this.getClass().getResource("/com/qr/yj/img/cat.jpg");
		if(null!=url2){
			ImageIcon icon = new ImageIcon(url2);
			qrResultLabel.setIcon(icon);
		}

		qrResultLabel.setEnabled(true);

		Border border2323 = BorderFactory.createEtchedBorder(Color.red,
		new Color(148, 145, 140));
		TitledBorder qrCodeTitle = new TitledBorder(border2323, "二维码扫描区");
		scrollPane.setBorder(qrCodeTitle);

		scrollPane.setViewportView(qrResultLabel);

		JScrollPane scrollPane_1 = new JScrollPane();
		splitPane.setRightComponent(scrollPane_1);
		/*GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_1.gridwidth = 3;
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 5;
		panel.add(scrollPane_1, gbc_scrollPane_1);*/

		resultArea = new AssistPopupTextArea();
		resultArea.setLineWrap(true);
		resultArea.setWrapStyleWord(true);
		resultArea.setEditable(false);
		scrollPane_1.setViewportView(resultArea);
		Border border243 = BorderFactory.createEtchedBorder(Color.white,
				new Color(148, 145, 140));
		TitledBorder openFileTitle2 = new TitledBorder(border243, "结果");
		scrollPane_1.setBorder(openFileTitle2);

		JPanel panel_3 = new JPanel();
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.gridwidth = 3;
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 0;
		gbc_panel_3.gridy = 4;
		panel.add(panel_3, gbc_panel_3);


//		panel_3.add(statusLabel);

		JButton exportButton = new JButton("导出图片");
		exportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveQRCodeDialog();
			}
		});
		panel_3.add(exportButton);

		//把图片复制到剪切板,相当于截图
		JButton clipButton = new JButton("复制图片到剪切板");
		clipButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(ValueWidget.isNullOrEmpty(image)){
					GUIUtil23.warningDialog(ERROR_MESSAGE_GENERATEQR_FIRST);
					genQRbutton.requestFocus();
					return;
				}

				clipboardImageAction();
			}
		});
		panel_3.add(clipButton);

				JButton scaleButton = new JButton("放大");
				scaleButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						enlarge();
					}
				});
				panel_3.add(scaleButton);

		JButton narrowButton = new JButton("缩小");
		narrowButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reduce();
			}
		});
		panel_3.add(narrowButton);

		JButton deletePicBtn=new JButton("删除二维码");
		deletePicBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(selectedFile==null|| !selectedFile.exists()){
					return;
				}
				int result = JOptionPane
						.showConfirmDialog(
								null,
								"<html>Are you sure to remove <font color=\"red\"  style=\"font-weight:bold;\">"
										+ selectedFile.getAbsolutePath() + "</font> ?</html>",
								"确认提示框", JOptionPane.OK_CANCEL_OPTION);
				if (result == 0) {
					selectedFile.delete();//删除保存二维码的文件
				}

			}
		});
		panel_3.add(deletePicBtn);

//		JPanel panel_1 = new JPanel();
//		tabbedPane.setEnabledAt(1, false);
//		tabbedPane.setSelectedIndex(1);
		statusLabel = new JLabel(String.format(statusInfo, charset, picFormat));
		this.add(statusLabel ,BorderLayout.SOUTH);

//		Toolkit toolkit = Toolkit.getDefaultToolkit();
        // 注册应用程序全局键盘事件, 所有的键盘事件都会被此事件监听器处理.
//        toolkit.addAWTEventListener(aWTEventListener, java.awt.AWTEvent.KEY_EVENT_MASK);
	}

	public void clipboardImageAction(){
		if(ValueWidget.isNullOrEmpty(image)){
			GUIUtil23.warningDialog(ERROR_MESSAGE_GENERATEQR_FIRST);
			genQRbutton.requestFocus();
			return;
		}
		ComponentUtil.setClipboardImage(frame,image);
	}
//	private void init33() {
//		panel.addWindowListener(new WindowAdapter() {
//
//			@Override
//			public void windowDeactivated(WindowEvent e) {
//				System.out.println("window Deactivated");
//				if (isLocked) {// over three times and is still locked,meanwhile use
//					if (task != null) {
//						task.cancel();
//						task = null;
//					}
//				} else {// first into this if clause(if (timesFail >=
//						// LoginUtil.MAX_LOGIN_FAIL_TIMES ))
//					task = null;
//				}
//				if (timer == null) {
//					timer = new Timer();
//				}
//			
//				if (task == null) {
//					task = new MyTask(QRCodeApp.this);
//				}
//				timer.schedule(task, Constant2.MILLISECONDS_WAIT_WHEN_FAIL);
//				System.out.println("开始计时");
//				isLocked = true;
//				super.windowDeactivated(e);
//			}
//
//		
//		});
//	}

	/***
	 * 把系统剪切板中的图片黏贴到swing的Label控件中
	 */
	public void pasteClipboardImageAction(){
		BufferedImage bufferedimage=ComponentUtil.getClipboardImage(this.frame);

		if(ValueWidget.isNullOrEmpty(bufferedimage)){
			GUIUtil23.alert("系统剪切板中无图片,请先复制图片");
			return;
		}
		image=bufferedimage;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {//把粘贴过来的图片转为为二进制(字节数组)
			ImageIO.write(bufferedimage, read_qrcode_from_pic_format/*jpg*/, baos);
			QRbytes= baos.toByteArray();
			ComponentUtil.appendResult(resultArea, "粘贴的二维码大小:\t"+QRbytes.length, true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		qrResultLabel.setIcon(new ImageIcon(image));
	}

	/***
	 * 设置弹出菜单
	 * @param qrResultLabel
	 */
	private void setPopupMenu(JComponent qrResultLabel)
    {
		final QRCodeMenuActionListener myMenuListener=new QRCodeMenuActionListener(this);
		qrResultLabel.addMouseListener(new MouseInputAdapter()
        {

            @Override
            public void mouseReleased(MouseEvent e)
            {
                //                super.mousePressed(e);
                if (e.getButton() == MouseEvent.BUTTON3)
                {
                    JPopupMenu textMenu = new JPopupMenu();
                    JMenuItem cleanUpM = new JMenuItem(MenuUtil2.ACTION_STR_CLEANUP);
                    JMenuItem copy22M = new JMenuItem(
                        MenuUtil2.ACTION_IMAGE_COPY);
                    JMenuItem paste22M = new JMenuItem(
                            MenuUtil2.ACTION_IMAGE_PASTE);
                    JMenuItem enlargeM = new JMenuItem(
                            MenuUtil2.ACTION_ENLARGE);
                    JMenuItem reduceM = new JMenuItem(
                            MenuUtil2.ACTION_REDUCE);
//                    JMenuItem pasteM = new JMenuItem(MenuUtil2.ACTION_STR_PASTE);

                    JMenuItem exportM = new JMenuItem(
                        MenuUtil2.ACTION_STR_EXPORT);
                    JMenuItem readQRCodeM = new JMenuItem(
                            MenuUtil2.ACTION_READ_QR_CODE);

                    JMenuItem openBrowserM = new JMenuItem(
                            MenuUtil2.ACTION_STR_OPEN_BROWSER);
                    copy22M.addActionListener(myMenuListener);
                    cleanUpM.addActionListener(myMenuListener);
                    exportM.addActionListener(myMenuListener);
                    enlargeM.addActionListener(myMenuListener);
                    reduceM.addActionListener(myMenuListener);
                    paste22M.addActionListener(myMenuListener);
                    readQRCodeM.addActionListener(myMenuListener);
                    openBrowserM.addActionListener(myMenuListener);
                    textMenu.add(cleanUpM);
                    textMenu.add(copy22M);
                    textMenu.add(paste22M);
//                    exportM.add(pasteM);
                    textMenu.add(exportM);
                    textMenu.add(enlargeM);
                    textMenu.add(reduceM);
                    textMenu.add(readQRCodeM);
                    textMenu.add(openBrowserM);
                    textMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

        });

    }

	/***
	 * @param tf :
	 */
	private void addKeyListener22(JComponent tf){
		tf.addKeyListener(keyListener);
	}
	
	/***
	 * 二维码编码
	 * 
	 * @param charset
	 */
	public void updateStatusCharset(String charset) {
		this.charset = charset;
		statusLabel.setText(String.format(statusInfo, charset, picFormat));
	}

	/***
	 * 二维码输入格式：png，jpg
	 * 
	 * @param charset
	 */
	public void updateStatusFormat(String picFormat) {
		this.picFormat = picFormat;
		statusLabel.setText(String.format(statusInfo, charset, picFormat));
	}
	public void saveQRCodeDialog(){
		if(ValueWidget.isNullOrEmpty(QRbytes)){
			if(image instanceof BufferedImage){
				ByteArrayOutputStream byteOutput=new ByteArrayOutputStream();
				try {
					ImageIO.write((BufferedImage)image,picFormat,byteOutput);
					byteOutput.flush();
					this.QRbytes=byteOutput.toByteArray();
					byteOutput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(ValueWidget.isNullOrEmpty(QRbytes)){
			GUIUtil23.warningDialog("请先点击【生成二维码】按钮.");
			genQRbutton.requestFocus();
			return;
		}
		JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if(!ValueWidget.isNullOrEmpty(selectedFile)){
        	chooser.setSelectedFile(selectedFile);
        }
        chooser.setName("二维码."+picFormat);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "picture Files", picFormat, "二维码");
            chooser.setFileFilter(filter);
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        chooser.setControlButtonsAreShown(true);
        chooser.setDialogTitle("保存二维码");
        //            chooser.setVisible(true);
        int result = chooser.showSaveDialog(QRCodePanel.this);
        System.out.println("New file:" + result);
        if (result == JOptionPane.OK_OPTION)
        {
            selectedFile = chooser.getSelectedFile();
            if(! SystemHWUtil.isHasSuffix(selectedFile)){
            	selectedFile=new File(selectedFile.getAbsolutePath()+ SystemHWUtil.ENGLISH_PERIOD+picFormat);
            }
            try {
				FileUtils.writeBytesToFile(QRbytes, selectedFile);
				ComponentUtil.appendResult(resultArea, "导出到:\t"+selectedFile.getAbsolutePath(), true);
			} catch (IOException e1) {
				e1.printStackTrace();
				GUIUtil23.errorDialog(e1.getMessage());
			}
            System.out.println("select file:" + selectedFile);
        }
	}
	public void cleanUpQRLabel(){
		this.qrResultLabel.setIcon(null);//清空Label中显示的图片
		QRbytes=null;//清空保存QR的字节数组
		image=null;
	}


	/***
	 * 通过菜单选择的编码
	 * @return
	 */
	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public boolean isModifyCharset() {
		return isModifyCharset;
	}

	public void setModifyCharset(boolean isModifyCharset) {
		this.isModifyCharset = isModifyCharset;
	}

	
	public void enableFtpMenu(){
		this.fileM_uploadFtp.setEnabled(true);
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
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

	

	/***
	 * 生成二维码
	 * @param qrInput
	 */
	private void generateQR(String qrInput){
		try {
			/*byte[]*/ QRbytes = QRCodeUtil.encode(qrInput, charset,
					picFormat, qrImageWidth, qrImageWidth);
			/*BufferedInputStream isr = new BufferedInputStream(FileUtils
					.getByteArrayInputSreamFromByteArr(bytes));
			
			try {
				QRbytes = new byte[isr.available()];
				isr.read(QRbytes);
				System.out.println("byte length:"+QRbytes.length);
			} catch (IOException e2) {
				e2.printStackTrace();
			}*/
			if (QRbytes != null) {
				ImageIcon imageIcon = new ImageIcon(QRbytes);
				image=imageIcon.getImage();
				qrResultLabel.setIcon(imageIcon);
//				ComponentUtil.appendResult(resultArea, "二维码字节数:\t\t"+QRbytes.length, true);
				resultArea.setText(qrInput);
			}

		} catch (WriterException e1) {
			e1.printStackTrace();
			GUIUtil23.errorDialog(e1);
		} catch (IOException e1) {
			e1.printStackTrace();
			GUIUtil23.errorDialog(e1);
		}

	}
	/***
	 * 生成二维码
	 */
	private void generateQRAction(boolean isRefreshCombox){
		String qrInput = inputQRTextArea.getText();
		generateQR(qrInput);
//		if(isRefreshCombox){
			this.frame.setCombox(PROP_KEY_QR_CODE_COMBOBOX, inputQRTextArea, qrComboBox,isRefreshCombox);
//		}
	}
	/***
	 * 放大二维码
	 */
	public void enlarge(){
		if(ValueWidget.isNullOrEmpty(image)){
			GUIUtil23.warningDialog(ERROR_MESSAGE_GENERATEQR_FIRST);
			genQRbutton.requestFocus();
			return;
		}
		
		QRCodePanel.this.qrImageWidth+=50;
		generateQRAction(false);
	}
	/***
	 * 缩小二维码
	 */
	public void reduce(){
		if(ValueWidget.isNullOrEmpty(image)){
			GUIUtil23.warningDialog(ERROR_MESSAGE_GENERATEQR_FIRST);
			genQRbutton.requestFocus();
			return;
		}
		if(QRCodePanel.this.qrImageWidth>100){
			QRCodePanel.this.qrImageWidth-=50;
		}else{
			GUIUtil23.warningDialog("不能再小了.");
			return;
		}
		generateQRAction(false);
	}

	public AssistPopupTextArea getInputQRTextArea() {
		return inputQRTextArea;
	}

	public JComboBox<String> getQrComboBox() {
		return qrComboBox;
	}
	public String deCode(boolean isAppend){
		return deCode(QRbytes,isAppend);
	}
	public String deCode(byte[]qrCodeBytes,boolean isAppend){
		System.out.println("start decode qrcode");
		try {
			String sourcce=QRCodeUtil.decode(qrCodeBytes);
			if(isAppend){
				ComponentUtil.appendResult(resultArea, sourcce, true);
			}
			
			return sourcce;
		} catch (NotFoundException e) {
			GUIUtil23.errorDialog(e);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			GUIUtil23.errorDialog(e);
		}
		return null;
	}

	/***
	 * 放大或缩小二维码
	 */
	/*java.awt.event.AWTEventListener aWTEventListener=new java.awt.event.AWTEventListener() {  
        public void eventDispatched(AWTEvent event) {
        	if(isSelectedTab()){
        		if (event.getClass() == KeyEvent.class) {  
                    KeyEvent kE = ((KeyEvent) event);  
                    // 处理按键事件 Ctrl+Enter  
                    if ((kE.getKeyCode() == KeyEvent.VK_EQUALS)  
                            && (((InputEvent) event)  
                                    .isControlDown())&&kE.getID() == KeyEvent.KEY_PRESSED) {  

                    	enlarge();
                    } else if ((kE.getKeyCode() == KeyEvent.VK_MINUS)  
                            && (((InputEvent) event)  
                                    .isControlDown())&&kE.getID() == KeyEvent.KEY_PRESSED) {  

                    	reduce();
                    }
                }
        	}
            
        }};
        *//***
         * 判断该panel 是否被选中
         * @return
         *//*
        private boolean isSelectedTab(){
        	return this.frame.isSelectedTab(this);
        }*/
	public void appendResult(String message){
		ComponentUtil.appendResult(resultArea, SystemHWUtil.CRLF+ message, true,false);
	}
}
