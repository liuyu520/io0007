package com.swing.dialog;

import com.common.bean.ParameterIncludeBean;
import com.common.util.WindowUtil;
import com.io.hw.awt.color.CustomColor;
import com.io.hw.json.HWJacksonUtils;
import com.string.widget.util.ValueWidget;
import com.swing.callback.ActionCallback;
import com.swing.component.AssistPopupTextArea;
import com.swing.component.ComponentUtil;
import com.swing.component.GenerateJsonTextArea;
import com.swing.component.RadioButtonPanel;
import com.swing.table.MyButtonEditor;
import com.swing.table.MyButtonRender;
import com.swing.table.MyTextFieldEditor;
import com.swing.table.TableUtil3;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GenerateJsonPane extends GenericDialog {

	private static final long serialVersionUID = -6652567670138676066L;
	public static String[] columnNames = {"名称", "值", "是否包含"};
	private static int index = 0;
	private JTable parameterTable_1;
	private JTextComponent targetTF;
	private AssistPopupTextArea textArea;

	/**
	 * Create the frame.
	 */
	public GenerateJsonPane(JTextComponent targetTF, boolean isModal) {
		index++;
		setTitle("第" + index + "个对话框");
		setModal(isModal);
		this.targetTF = targetTF;
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Random dom = new Random();
		setBounds(100 + dom.nextInt(100), 100 + dom.nextInt(100), 550, 400);
		JPanel contentPane = new JPanel();
		Color backgroundColor = CustomColor.getMoreLightColor();
		contentPane.setBackground(backgroundColor);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setBackground(backgroundColor);
		JButton getDataButton = new JButton("获取数据(S)");
		getDataButton.setBackground(backgroundColor);
		getDataButton.setMnemonic('S');
		panel.add(getDataButton);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(backgroundColor);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("表格", null, panel_2, null);
		panel_2.setLayout(new BorderLayout());
		panel_2.setBackground(backgroundColor);

		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
				int selectedIndex = tabbedPane.getSelectedIndex();
				System.out.println(selectedIndex);
				if (selectedIndex == 1) {
					String json = preview2();
					textArea.setText(json);
				}//if(selectedIndex==1)

			}
		});
		//包含表格
		JPanel panel_5 = new JPanel();
		JScrollPane scrollPane = new JScrollPane(panel_5);
		scrollPane.setToolTipText("请求参数");
		scrollPane.setBackground(backgroundColor);


		panel_2.add(scrollPane, BorderLayout.CENTER);
		panel_5.setLayout(new BorderLayout(0, 0));

		parameterTable_1 = new JTable();
		panel_5.add(parameterTable_1);
		parameterTable_1.setBackground(backgroundColor);
        parameterTable_1.addMouseListener(new MouseInputListener() {
                                              public int count;

                                              public void mouseClicked(MouseEvent e) {
                                                  System.out.println("mouseClicked" + (count++));
                                                  processEvent(e);

                                              }

                                              /***
                                               * in order to trigger Left-click the event
                                               */
                                              public void mousePressed(MouseEvent e) {
                                                  processEvent(e);// is necessary!!!
//                System.out.println("mousePressed"+(count++));
                                              }

                                              public void mouseReleased(final MouseEvent e) {
                                                  //
                                                  final int rowCount = parameterTable_1.getSelectedRow();
                                                  final int columnCount = parameterTable_1.getSelectedColumn();
                                                  if (e.getButton() == MouseEvent.BUTTON2) {//鼠标中键
                                                      TableUtil3.tableCellMidClick(e, parameterTable_1);

                                                  }

                /*else if (e.getButton() == MouseEvent.BUTTON1&& e.getClickCount()==1){
                    System.out.println("左键");
                	int modifiers = e.getModifiers();
                	modifiers |= MouseEvent.FOCUS_EVENT_MASK;
                	MouseEvent ne = new MouseEvent(e.getComponent(), e.getID(),
							e.getWhen(), modifiers, e.getX(), e.getY(),
							2, false);

//                	processEvent(ne);
//                	jTable.editCellAt(rowCount, columnCount,ne);
//                	CellEditor cellEditor=jTable.getCellEditor(rowCount, columnCount);
//                	cellEditor.shouldSelectCell(ne);
                	jTable.dispatchEvent(ne);
                }*/
//                System.out.println("mouseReleased"+(count++));
                                              }

                                              public void mouseEntered(MouseEvent e) {
                                                  processEvent(e);
                                              }

                                              public void mouseExited(MouseEvent e) {
                                                  processEvent(e);
                                              }

                                              public void mouseDragged(MouseEvent e) {
                                                  processEvent(e);
                                              }

                                              public void mouseMoved(MouseEvent e) {
                                                  processEvent(e);
                                              }

                                          }
        );
        JPanel panel_1 = new JPanel();
		panel_2.add(panel_1, BorderLayout.SOUTH);
		panel_1.setBackground(backgroundColor);
		JButton addBtn = new JButton("add");
		addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                addParameter(null, (Map<String, ActionCallback>) null);
            }
		});
		panel_1.add(addBtn);

		JButton btnAddFromC = new JButton("从剪切板添加");
		panel_1.add(btnAddFromC);

		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("预览", null, panel_3, null);
		panel_3.setLayout(new BorderLayout(0, 0));
		panel_3.setBackground(backgroundColor);
		textArea = new AssistPopupTextArea();
		JScrollPane scrollPane_1 = new JScrollPane(textArea);
		panel_3.add(scrollPane_1);

		JPanel panel_4 = new JPanel();
		panel_3.add(panel_4, BorderLayout.NORTH);
		panel_4.setBackground(backgroundColor);
		JButton button = ComponentUtil.getCopyBtn(textArea);
		panel_4.add(button);


		btnAddFromC.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = WindowUtil.getSysClipboardText();
				if (!ValueWidget.isNullOrEmpty(text)) {
                    addParameter(text, (Map<String, ActionCallback>) null);
                }
			}

		});
		getDataButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				Object[][] data2 =TableUtil.getParameter4Table(parameterTable_1,true);
//				List<ParameterIncludeBean> parameters =TableUtil.getTableParameters(parameterTable_1,true);
				getDataAction();
//				System.out.println(parameters.size());
			}
		});

		layoutTable();
		rendTable();
		if (!ValueWidget.isNullOrEmpty(this.targetTF)) {
			readJson(this.targetTF.getText());
		}
        /*MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
			public void mousePressed(MouseEvent e) {
				int buttonType = e.getButton();
				if (buttonType == MouseEvent.BUTTON2) {
					System.out.println("鼠标中键");
					getDataAction();
				}
			}

		};
		contentPane.addMouseListener(mouseAdapter);
		panel_2.addMouseListener(mouseAdapter);
		panel_5.addMouseListener(mouseAdapter);
		parameterTable_1.addMouseListener(mouseAdapter);
		scrollPane.addMouseListener(mouseAdapter);
		*/
        //没有生效
		contentPane.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				System.out.println("keyTyped");
			}

			@Override
			public void keyReleased(KeyEvent e) {
				System.out.println("keyReleased1");
			}

			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println("keyPressed");
			}
		});
		//TODO 没有生效
		contentPane.getActionMap().put("Save", new AbstractAction("save111") {
			private static final long serialVersionUID = -3548620001691220571L;

			public void actionPerformed(ActionEvent evt) {
				System.out.println("111");
			}
		});

		contentPane.getInputMap().put(KeyStroke.getKeyStroke("control S"), "Save");
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GenerateJsonPane frame = new GenerateJsonPane(null, false);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void getDataAction(){
		String json=preview2();
		System.out.println(json);
		if(!ValueWidget.isNullOrEmpty(GenerateJsonPane.this.targetTF)
				&&!ValueWidget.isNullOrEmpty(json)){
			GenerateJsonPane.this.targetTF.setText(json);
		}
		GenerateJsonPane.this.dispose();
	}
	private void readJson(String text) {
		if(!ValueWidget.isNullOrEmpty(text))
		{
            text = text.trim();
            if(text.startsWith("{")&&text.endsWith("}")){
				System.out.println("是json");
				Map map=(Map) HWJacksonUtils.deSerialize(text, Map.class);
                TableUtil3.setTableData3(parameterTable_1, map, false, true, columnNames, (Map<String, ActionCallback>) null);
                rendTable();
			}
		}
	}

	private String preview2(){
		List<ParameterIncludeBean> parameters =TableUtil3.getTableParameters(parameterTable_1/*,true*/);
        Map parametersMap= TableUtil3.getParameterMap(parameters, false, null);
		return HWJacksonUtils.getJsonP(parametersMap);
	}
	/***
     * 表格增加一行
     */
    private void addParameter(String key, Map<String, ActionCallback> actionCallbackMap) {
        TableUtil3.addParameter(this.parameterTable_1, key, false, true, actionCallbackMap);
    }
    
	private void rendTable(){
    	parameterTable_1.getColumnModel().getColumn(2)
		.setCellEditor(new MyButtonEditor());
    	parameterTable_1.getColumnModel().getColumn(2)
		.setCellRenderer(new MyButtonRender());
    	
    	parameterTable_1.getColumnModel().getColumn(0)
                .setCellEditor(new MyTextFieldEditor(null));
        parameterTable_1.getColumnModel().getColumn(0)
		.setCellRenderer(new MyButtonRender());
    	parameterTable_1.getColumnModel().getColumn(1)
                .setCellEditor(new MyTextFieldEditor(null));
        parameterTable_1.getColumnModel().getColumn(1)
		.setCellRenderer(new MyButtonRender());
//        final MouseInputListener mouseInputListener = getMouseInputListener(parameterTable_1,RequestPanel.this,null);
//        parameterTable_1.addMouseListener(mouseInputListener);
    }
    public void layoutTable( ) {
//    	Object[][] datas=new Object[1][2];
    	Object[][] datas=new Object[1][3];
    	for(int i=0;i<datas.length;i++){
    		Object[]objs=new Object[3];
    		RadioButtonPanel panel=new RadioButtonPanel();
    		panel.init(false);
    		objs[2]=panel;
    		objs[0]=new JScrollPane(new AssistPopupTextArea());
    		objs[1]=new JScrollPane(new GenerateJsonTextArea());
    		datas[i]=objs;
    	}
        setTableData2(datas);
//    	repaintTable();
		
	}
    private void setTableData2(Object[][] datas) {
        DefaultTableModel model = new DefaultTableModel(datas, columnNames);
        parameterTable_1.setModel(model);
        this.parameterTable_1.setRowHeight(40);
        rendTable();
    }

}
