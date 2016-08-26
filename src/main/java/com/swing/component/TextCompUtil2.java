package com.swing.component;

import com.common.bean.FindTxtResultBean;
import com.common.bean.HeightWidthBean;
import com.common.dict.Constant2;
import com.common.util.ReflectHWUtils;
import com.common.util.SystemHWUtil;
import com.common.util.WindowUtil;
import com.string.widget.util.RegexUtil;
import com.string.widget.util.ValueWidget;
import com.swing.callback.ActionCallback;
import com.swing.dialog.*;
import com.swing.dialog.toast.ToastMessage;
import com.swing.event.EventHWUtil;
import com.swing.menu.MenuUtil2;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/***
 * 
 * @author huangweii
 * 2015年11月5日
 */
public class TextCompUtil2 {
	public static final String PLACEHOLDER_DOUBLE_SHIFT="双击Shift,弹出下拉框(生成MD5,生成json)";
	/***
	 * 文本框的字体默认颜色
	 */
	public static final Color DEFAULT_TF_FOREGROUND=Color.black;
	public static final Color PLACEHOLDER_BACKGROUND_COLOR = new Color(204, 204, 204);
	public static final AbstractAction searchAction=new AbstractAction("search111") {
		private static final long serialVersionUID = -3548620001691220571L;

		public void actionPerformed(ActionEvent evt) {
			searchMnemonicAction(evt);
		}
	};
	public static final AbstractAction maximizeTAAction=new AbstractAction("maximizeTA111") {
		private static final long serialVersionUID = -3548620001691220571L;

		public void actionPerformed(ActionEvent evt) {
			JTextComponent tf = (JTextComponent) evt.getSource();
			DialogUtil.showMaximizeDialog(tf);
		}
	};
	/***
	 * 用于截图的对话框
	 */
	public static GenericDialog screenshotDialog = getScreenshotGenericDialog();
	public static final AbstractAction copyImage2ClipAction=new AbstractAction("search111") {
		private static final long serialVersionUID = -3548620001691220571L;

		public void actionPerformed(ActionEvent evt) {
			JTextComponent tf = (JTextComponent) evt.getSource();
			TextCompUtil2.copyImgAction(tf);
		}
	};

	private static GenericDialog getScreenshotGenericDialog() {
		return new GenericDialog(){
            @Override
            public void layout3(Container contentPane) {
                super.layout3(contentPane);
                setUndecorated(true);//必需的
                setBackground(Color.RED);
                this.setOpacity(0.3f);//透明度
                ((JPanel) this.getContentPane()).setOpaque(false);
            }
        };
	}

	/***
	 * 文本框中必须有成员变量"findTxtResultBean"
	 * @param evt
     */
	public static void searchMnemonicAction(ActionEvent evt) {
		JTextComponent tf = (JTextComponent) evt.getSource();
		FindTxtResultBean findTxtResultBean = null;
		try {

            findTxtResultBean = (FindTxtResultBean) ReflectHWUtils.getObjectValue(tf, Constant2.FINDTXTRESULTBEAN_FIELD);
            int index;
            String keyword = null;
            if (!ValueWidget.isNullOrEmpty(findTxtResultBean)) {
                index = findTxtResultBean.getFoundIndex();
                keyword = findTxtResultBean.getKeyWord();
            } else {
                index = 0;
            }
            if (ValueWidget.isNullOrEmpty(keyword)) {//无关键字,则弹框
                showSearchDialog(tf,null);
                return;
            }
            findTxtResultBean = DialogUtil.searchText(tf, index, keyword);
            if(findTxtResultBean==null){//弹框
                showSearchDialog(tf,keyword);
                return;
            }
            ReflectHWUtils.setObjectValue(tf, Constant2.FINDTXTRESULTBEAN_FIELD, findTxtResultBean);//如果findTxtResultBean为null,则忽略
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
	}

    public static void addActionMap(final JTextComponent tc, final UndoManager undo, final Map<String, ActionCallback> actionCallbackMap) {
        addActionMap(tc, undo, true, actionCallbackMap);
    }

    public static void addActionMap(final JTextComponent tc, final UndoManager undo, boolean needSearch, final Map<String, ActionCallback> actionCallbackMap) {
        tc.getActionMap().put("Undo", new AbstractAction("Undo11") {
			private static final long serialVersionUID = 2434402629308759912L;
			public void actionPerformed(ActionEvent evt) {
				try {
					boolean b = undo.canUndo();
					// System.out.println("whether undo : "+b);
					if (b) {
						undo.undo();
					}
				} catch (CannotUndoException e) {
				}
			}
		});
        tc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, getDefaultModifier()/*"control Z"*/), "Undo");

		//为什么要注释?因为下面有control R,表示只读
		/*tc.getActionMap().put("Redo", new AbstractAction("Redo1111") {
			private static final long serialVersionUID = 5348330289578410517L;

			public void actionPerformed(ActionEvent evt) {
				try {
					if (undo.canRedo()) {
						undo.redo();
					}
				} catch (CannotRedoException e) {
				}
			}
		});
		tc.getInputMap().put(KeyStroke.getKeyStroke("control R"), "Redo");*/

		tc.getActionMap().put("Copy", new AbstractAction("Copy111") {
			private static final long serialVersionUID = -5151480809625853288L;
			public void actionPerformed(ActionEvent evt) {
				String selectText=tc.getSelectedText();
				if(ValueWidget.isNullOrEmpty(selectText)){
					//如果没有选择的文本,则复制全部
					WindowUtil.setSysClipboardText(tc.getText());
				}else{
					tc.copy();
				}
			}

		});
		tc.getInputMap().put(KeyStroke.getKeyStroke("control C"), "Copy");

		tc.getActionMap().put("Cut", new AbstractAction("Cut") {

			private static final long serialVersionUID = 7316612864835857713L;

			public void actionPerformed(ActionEvent evt) {
				tc.cut();
			}

		});
        tc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_X, getDefaultModifier()/*"control X"*/), "Cut");

		tc.getActionMap().put("Paste", new AbstractAction("Paste111") {
			private static final long serialVersionUID = -3548620001691220571L;

			public void actionPerformed(ActionEvent evt) {
				tc.paste();
			}
		});
        tc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_V, getDefaultModifier()/*"control V"*/), "Paste");

		// redo Ctrl + Y
		tc.getActionMap().put("Redo", new AbstractAction("reDo111") {
			private static final long serialVersionUID = -3548620001691220571L;

			public void actionPerformed(ActionEvent evt) {
				if (undo.canRedo()) {
					undo.redo();
				}
			}
		});
        tc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, getDefaultModifier()/*"control Y"*/), "Redo");

		/*tc.getActionMap().put("Save", new AbstractAction("save111") {
			private static final long serialVersionUID = -3548620001691220571L;
			public void actionPerformed(ActionEvent evt) {
				dealSave((JTextComponent)evt.getSource());
			}
		});
		tc.getInputMap().put(KeyStroke.getKeyStroke("control S"), "Save");*/

		//按Ctrl+R 使文本框只读,不可编辑
		tc.getActionMap().put("Readonly", new AbstractAction("Readonly111") {
			private static final long serialVersionUID = -3548620001691220571L;
			public void actionPerformed(ActionEvent evt) {
				JTextComponent tf=(JTextComponent)evt.getSource();
				if(!ValueWidget.isNullOrEmpty(tf)){
					tf.setEditable(false);
				}
			}
		});
        tc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_R, getDefaultModifier()/*"control R"*/), "Readonly");

        //按Ctrl+E 使文本框可编辑
		tc.getActionMap().put("Editable", new AbstractAction("Editable111") {
			private static final long serialVersionUID = -3548620001691220571L;
			public void actionPerformed(ActionEvent evt) {
				JTextComponent tf=(JTextComponent)evt.getSource();
				if(!ValueWidget.isNullOrEmpty(tf)){
					tf.setEditable(true);
					tf.requestFocus();
					tf.repaint();
					tf.updateUI();
				}
			}
		});
        //on Mac ,this would be command key
        tc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, getDefaultModifier()/*"control E"*/), "Editable");

        tc.getActionMap().put("Command_enter", new AbstractAction("Command_enter111") {
            private static final long serialVersionUID = -3548620001691220571L;

            public void actionPerformed(ActionEvent evt) {
                JTextComponent tf = (JTextComponent) evt.getSource();
                System.out.println("Command_enter");
                Map<String, ActionCallback> actionCallbackMap1 = getActionCallbackMap(tf);
                if (actionCallbackMap1 == null) {
                    actionCallbackMap1 = actionCallbackMap;
                }

                if (!ValueWidget.isNullOrEmpty(actionCallbackMap1)) {
                    ActionCallback actionCallback = actionCallbackMap1.get("Command_enter");
                    if (null != actionCallback) {
                        actionCallback.actionPerformed(evt);
                    }
                }

            }
        });
        tc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, getDefaultModifier()/*"control E"*/), "Command_enter");


        tc.getActionMap().put("Ctrl_enter", new AbstractAction("Ctrl_enter111") {
            private static final long serialVersionUID = -3548620001691220571L;

            public void actionPerformed(ActionEvent evt) {
                JTextComponent tf = (JTextComponent) evt.getSource();
                System.out.println("Ctrl_enter");
                Map<String, ActionCallback> actionCallbackMap1 = getActionCallbackMap(tf);
                if (actionCallbackMap1 == null) {
                    actionCallbackMap1 = actionCallbackMap;
                }
                if (!ValueWidget.isNullOrEmpty(actionCallbackMap1)) {
                    ActionCallback actionCallback = actionCallbackMap1.get("Ctrl_enter");
                    if (null != actionCallback) {
                        actionCallback.actionPerformed(evt);
                    }
                }
            }
        });
        tc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_MASK), "Ctrl_enter");


        if (needSearch) {
            //按Ctrl+F 搜索文本
			//需要区分对待,因为有的文本框不需要Ctrl+F 快捷键
			tc.getActionMap().put("search", searchAction);
            tc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F, getDefaultModifier() /*"control F"*/), "search");

		}

		//按Ctrl+G 截屏(只截文本框)
		tc.getActionMap().put("screenshotDialog", copyImage2ClipAction);
        tc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_G, getDefaultModifier()/*"control G"*/), "screenshotDialog");

		//按Ctrl+L 最大化文本框
		tc.getActionMap().put("rtaMaximizeTAAction", maximizeTAAction);
        tc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_L, getDefaultModifier()/*"control L"*/), "rtaMaximizeTAAction");


		//按Ctrl+D 清空文本框
		tc.getActionMap().put("cleanUp", new AbstractAction("cleanUp111") {
			private static final long serialVersionUID = -3548620001691220571L;
			public void actionPerformed(ActionEvent evt) {
				JTextComponent tf=(JTextComponent)evt.getSource();
				if(!ValueWidget.isNullOrEmpty(tf)){
					tf.setText(SystemHWUtil.EMPTY);
					tf.requestFocus();
				}
			}
		});
        tc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_D, getDefaultModifier()/*"control D"*/), "cleanUp");

        tc.addKeyListener(new KeyListener() {
            private long lastTimeMillSencond;

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (EventHWUtil.isJustCtrlDown(e)) {
                    if (lastTimeMillSencond == 0) {
                        lastTimeMillSencond = System.currentTimeMillis();
                    } else {
                        long currentTime = System.currentTimeMillis();
                        if (MenuUtil2.isDoubleClick(currentTime - lastTimeMillSencond)) {
                            System.out.println("双击Ctrl");
                            String content = WindowUtil.getSysClipboardText();
                            if (ValueWidget.isNullOrEmpty(content)) {
                                return;
                            }
                            tc.setText(content);
                            tc.requestFocus();
                            lastTimeMillSencond = 0;
                        } else {
                            lastTimeMillSencond = System.currentTimeMillis();
                        }
                    }
                }
            }
        });

    }

    /**
     * Returns the default modifier key for a system.  For example, on Windows
     * this would be the CTRL key (<code>InputEvent.CTRL_MASK</code>).<br>
     *     on Mac ,this would be command key
     *
     * @return The default modifier key.
     */
    protected static final int getDefaultModifier() {
        return Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    }

	public static void showSearchDialog(JTextComponent tf,String keyword) {
		SearchInputDialog searchInputDialog = new SearchInputDialog(tf,keyword);
		searchInputDialog.setVisible(true);
	}

	/***
	 * 设置默认提示语
	 * @param inputTextArea
	 * @param placeHolder
	 */
	public static void setPlaceHolder(JTextComponent inputTextArea,String placeHolder){
		String oldText=inputTextArea.getText();
		if(ValueWidget.isNullOrEmpty(oldText)){
			inputTextArea.setForeground(PLACEHOLDER_BACKGROUND_COLOR);
			inputTextArea.setText(placeHolder);
		}
		
		try {
			ReflectHWUtils.setObjectValue(inputTextArea, "placeHolderText", placeHolder);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	public static boolean isPlaceHolder(JTextComponent inputTextArea,String placeHolder){
		return isPlaceHolder(inputTextArea, null, placeHolder);
	}
	/***
	 * 判断是否是默认提示语
	 * @param inputTextArea
	 * @param placeHolder
	 * @return
	 */
	public static boolean isPlaceHolder(JTextComponent inputTextArea,String text,String placeHolder){
		if(ValueWidget.isNullOrEmpty(text)){
			text=inputTextArea.getText();
		}
		if(ValueWidget.isNullOrEmpty(text)){
			return false;
		}
		return text.equals(placeHolder)
				&& inputTextArea.getForeground().equals(PLACEHOLDER_BACKGROUND_COLOR);
	}
	public static void placeHolderFocus(final JTextComponent inputTextArea,final String placeHolder){
		inputTextArea.addFocusListener(new FocusAdapter() {
			/***
			 * 解决一启动图形界面,就聚焦的情况
			 */
//			private boolean isFirstFocus=true;
			/***
			 * 失去焦点
			 */
			@Override
			public void focusLost(FocusEvent e) {
				super.focusLost(e);
				if(ValueWidget.isNullOrEmpty(inputTextArea.getText())){
					setPlaceHolder(inputTextArea,placeHolder);
				}
			}
			/***
			 * 获取焦点
			 */
			@Override
			public void focusGained(FocusEvent e) {
				super.focusGained(e);
				if(isPlaceHolder(inputTextArea, placeHolder)){
//					if(!isFirstFocus){
						inputTextArea.setText(SystemHWUtil.EMPTY);
						inputTextArea.setForeground(Color.black);
					/*}else{
						isFirstFocus=false;
					}*/
				}
			}
		});
		inputTextArea.addKeyListener(new KeyAdapter() {
			/***
			 * 先按下再松开
			 */
			@Override
			public void keyTyped(KeyEvent e) {
				super.keyTyped(e);
				cleanPlaceHolder(inputTextArea, placeHolder);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				super.keyTyped(e);
				cleanPlaceHolder(inputTextArea, placeHolder);
			}
		});
		/***
		 * 鼠标点击
		 */
		inputTextArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				cleanPlaceHolder(inputTextArea, placeHolder);
			}
			
			@Override
		 	public void mouseClicked(MouseEvent e) {
				cleanPlaceHolder(inputTextArea, placeHolder);
			}
		});
		final Document doc = inputTextArea.getDocument();
		DocumentListener docLis = new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if(!isPlaceHolder(inputTextArea, placeHolder)){
					inputTextArea.setForeground(TextCompUtil2.DEFAULT_TF_FOREGROUND);
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				if(!isPlaceHolder(inputTextArea, placeHolder)){
					inputTextArea.setForeground(TextCompUtil2.DEFAULT_TF_FOREGROUND);
				}
			}
		};
		doc.addDocumentListener(docLis);
	}
	/***
	 * 清除placeholder
	 * @param inputTextArea
	 * @param placeHolder
	 */
	private static void cleanPlaceHolder(final JTextComponent inputTextArea,final String placeHolder){
		if(isPlaceHolder(inputTextArea, placeHolder)){
			inputTextArea.setText(SystemHWUtil.EMPTY);
			inputTextArea.setForeground(TextCompUtil2.DEFAULT_TF_FOREGROUND);
		}
	}
	public static void generateJsonPopup(){
	}

	/***
	 * 双击Shift 弹出菜单
	 *
	 * @param e
	 */
    private static void popupMenu(JTextComponent inputTextArea, KeyEvent e, boolean isSimple) {
//		System.out.println("双击Shift");
		JPopupMenu textPopupMenu = new JPopupMenu();
		textPopupMenu.setLabel("打开文件");
		textPopupMenu.setLightWeightPopupEnabled(true);
		textPopupMenu.setBackground(Color.GREEN);
		GenerateJsonActionListener dropListMenuActionListener = new GenerateJsonActionListener(
				inputTextArea);
        if (!isSimple) {
            JMenuItem openFolderM = new JMenuItem("获取json");
            openFolderM.addActionListener(dropListMenuActionListener);
            textPopupMenu.add(openFolderM);

            JMenuItem generateMD5M = new JMenuItem(MenuUtil2.ACTION_CREATE_MD5);
            generateMD5M.addActionListener(dropListMenuActionListener);
            textPopupMenu.add(generateMD5M);

            JMenuItem deMD5M = new JMenuItem(MenuUtil2.ACTION_MD5_DECODE);
            deMD5M.addActionListener(dropListMenuActionListener);
            textPopupMenu.add(deMD5M);

            JMenuItem browserFileM = new JMenuItem(MenuUtil2.ACTION_STR_BROWSER);
            browserFileM.addActionListener(dropListMenuActionListener);
            textPopupMenu.add(browserFileM);

            //最大化窗口之后编辑
            JMenuItem editM = new JMenuItem(MenuUtil2.ACTION_STR_EDIT);
            editM.addActionListener(dropListMenuActionListener);
            textPopupMenu.add(editM);

            //去掉双引号
            JMenuItem deleteTwoQuoteM = new JMenuItem(MenuUtil2.ACTION_DELETE_TWO_QUOTE);
            deleteTwoQuoteM.addActionListener(dropListMenuActionListener);
            textPopupMenu.add(deleteTwoQuoteM);
        }

        //删除后黏贴
        JMenuItem pasteAfterClearM = new JMenuItem(MenuUtil2.ACTION_STR_PASTE_AFTER_DELETE);
        pasteAfterClearM.addActionListener(dropListMenuActionListener);
        textPopupMenu.add(pasteAfterClearM);

        JMenuItem urlDecodeM = new JMenuItem(
                MenuUtil2.ACTION_URL_DECODE);
        urlDecodeM.addActionListener(dropListMenuActionListener);
        textPopupMenu.add(urlDecodeM);

        JMenuItem urlEncodeM = new JMenuItem(
                MenuUtil2.ACTION_URL_ENCODE);
        urlEncodeM.addActionListener(dropListMenuActionListener);
        textPopupMenu.add(urlEncodeM);

        JMenuItem queryString2Json = new JMenuItem(MenuUtil2.ACTION_QUERY_STRING2JSON);
        queryString2Json.setActionCommand(MenuUtil2.ACTION_QUERY_STRING2JSON);
        queryString2Json.addActionListener(dropListMenuActionListener);
        textPopupMenu.add(queryString2Json);

        JMenuItem json2QueryString = new JMenuItem(MenuUtil2.ACTION_JSON2QUERY_STRING);
        json2QueryString.setActionCommand(MenuUtil2.ACTION_JSON2QUERY_STRING);
        json2QueryString.addActionListener(dropListMenuActionListener);
        textPopupMenu.add(json2QueryString);
        /*JMenuItem notepadM = new JMenuItem("notepad 编辑文件");
		notepadM.addActionListener(dropListMenuActionListener);
		textPopupMenu.add(notepadM);*/

		JTextComponent tf = (JTextComponent) e.getSource();
		Point point = tf.getParent().getLocation();
		textPopupMenu.show(e.getComponent(), point.x + 20,
				point.y + 2);// 下移一点
	}

    public static void dropListMenu(final JTextComponent inputTextArea, final boolean isSimple) {
        inputTextArea.addKeyListener(new KeyListener() {
			private long lastTimeMillSencond;

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) { 
				if (EventHWUtil.isJustShiftDown(e)) {
					if (lastTimeMillSencond == 0) {
						lastTimeMillSencond = System.currentTimeMillis();
					} else {
						long currentTime = System.currentTimeMillis();
						long delta=currentTime
								- lastTimeMillSencond;
						if (MenuUtil2.isDoubleClick(delta)) {//双击Shift
                            popupMenu(inputTextArea, e, isSimple);
                            lastTimeMillSencond = 0;
						} else {
							lastTimeMillSencond = System.currentTimeMillis();
						}
						delta=0l;
					}
				}
			}
		});
	}

	public static void copyImgAction(JTextComponent area2) {
		HeightWidthBean heightWidthBean=new HeightWidthBean();
		heightWidthBean.setHeight(area2.getHeight());//默认高度
		heightWidthBean.setWidth(area2.getWidth());//默认宽度
		GenericDialog genericDialogTmp= showScreenshotDialog(area2,heightWidthBean.getWidth(),heightWidthBean.getHeight());//只是为了兼容Hijson
		if(null==genericDialogTmp){
			screenshotDialog=getScreenshotDialog(area2);
		}else{
			screenshotDialog=genericDialogTmp;
		}
		SpecifyWidthAndHeightDialog specifyWidthAndHeightDialog=new SpecifyWidthAndHeightDialog(heightWidthBean,screenshotDialog);
		specifyWidthAndHeightDialog.setVisible(true);
		System.out.println(heightWidthBean.isSuccess());

		if(heightWidthBean.isSuccess()){
			if(heightWidthBean.isValid()){
				ComponentUtil.generateImageAndCopy(area2,heightWidthBean.getHeight(),heightWidthBean.getWidth());
			}else{
				ToastMessage.toast("高度或宽度不合法",2000, Color.RED);
				return;
			}
		}else{
			ToastMessage.toast("已取消",1000, Color.RED);
		}
	}

	public static GenericDialog showScreenshotDialog(JTextComponent area2,int width,int height){
		Class clazz=area2.getClass();
		  Object obj=null;
		    Method m = null;
			try {
				m = clazz.getMethod("showScreenshotDialog", new Class[]{int.class,int.class});
				m.setAccessible(true);
			    obj=m.invoke(area2, width,height);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			if(null==m){//抛异常 :NoSuchMethodException
				GenericDialog genericDialog= screenshotDialog;
				Point point= area2.getLocationOnScreen();
				genericDialog.setBounds(point.x,point.y,width,height);
				genericDialog.launchFrame();
				return genericDialog;
			}
		return null;
	}

    public static GenericDialog getScreenshotDialog(JTextComponent area2) {
        return (GenericDialog) getReflectGetMethod(area2, "getScreenshotDialog");
    }

    public static Object getReflectGetMethod(JTextComponent area2, String methodName) {
        Class clazz=area2.getClass();
		  Object obj=null;
		    Method m;
			try {
                m = clazz.getMethod(methodName, new Class[]{});
                m.setAccessible(true);
			    obj=m.invoke(area2, null);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
        return obj;
    }


    public static Map<String, ActionCallback> getActionCallbackMap(JTextComponent area2) {
        return (Map<String, ActionCallback>) getReflectGetMethod(area2, "getActionCallbackMap");
    }

	static class GenerateJsonActionListener implements ActionListener {
		private JTextComponent ta;

		public GenerateJsonActionListener(JTextComponent tf) {
			super();
			this.ta = tf;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("获取json")) {
//				System.out.println(command);
				GenerateJsonPane generateJsonPane = new GenerateJsonPane(ta, true);
				generateJsonPane.setVisible(true);
			}else
			if (command.equals(MenuUtil2.ACTION_CREATE_MD5)) {//获取MD5值
				String text = ta.getText();
				try {
					ta.setText(SystemHWUtil.getMD5(text, SystemHWUtil.CHARSET_UTF));
				} catch (NoSuchAlgorithmException e1) {
					e1.printStackTrace();
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
			}else if (command.equals(MenuUtil2.ACTION_STR_BROWSER)) {//弹出文件选择窗口
				boolean isSuccess = DialogUtil.browserFile(ta, JFileChooser.FILES_ONLY, ta);
			} else if (command.equalsIgnoreCase(MenuUtil2.ACTION_MD5_DECODE)) {
				String text = ta.getText();
				String source;
				source = SystemHWUtil.md5Map.get(text);
				if (ValueWidget.isNullOrEmpty(source)) {
					ToastMessage.toast("暂无md5对应的原文", 3000, Color.red);
				} else {
					ta.setText(source);
				}
			}else if (command.equals(MenuUtil2.ACTION_STR_EDIT)) {//
				/*SimpleTextEditDialog generateJsonPane = new SimpleTextEditDialog(ta);
				generateJsonPane.setVisible(true);*/
				DialogUtil.showMaximizeDialog(ta);
			}else if (command.equals(MenuUtil2.ACTION_DELETE_TWO_QUOTE)) {
				String content=this.ta.getText();
				if(!ValueWidget.isNullOrEmpty(content)){
					content= RegexUtil.deleteTwoQuote(content);
					this.ta.setText(content);
				}
            } else if (command.equals(MenuUtil2.ACTION_STR_PASTE_AFTER_DELETE)) {//删除后黏贴
                String content = WindowUtil.getSysClipboardText();
                if (ValueWidget.isNullOrEmpty(content)) {
                    return;
                }
                this.ta.setText(content);
                this.ta.requestFocus();
            } else if (command.startsWith(MenuUtil2.ACTION_URL_DECODE)) {// 退出应用程序
                MenuUtil2.urlDecode(this.ta);
            } else if (command.startsWith(MenuUtil2.ACTION_URL_ENCODE)) {// 退出应用程序
                MenuUtil2.urlEncode(this.ta);
            } else if (command.startsWith(MenuUtil2.ACTION_QUERY_STRING2JSON)) {
                MenuUtil2.queryString2Json(this.ta, true, false/*isFurther*/);
            } else if (command.startsWith(MenuUtil2.ACTION_JSON2QUERY_STRING)) {
                //{"username":"whuang","age":23} -->username=whuang&age=23
                MenuUtil2.json2queryString(this.ta);
            }
        }

	}
}
