package com.swing.dialog;

import com.common.util.SystemHWUtil;
import com.io.hw.awt.color.CustomColor;
import com.string.widget.util.ValueWidget;
import com.swing.component.AssistPopupTextArea;
import com.swing.component.ComponentUtil;
import com.swing.component.MyNamePanel;
import com.swing.dialog.callback.Callback2;
import com.swing.dialog.toast.ToastMessage;
import com.swing.event.EventHWUtil;
import com.swing.menu.MenuUtil2;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;

public class UnicodePanel extends MyNamePanel {

    public static final String please_input = "<请输入内容>";
    private static final long serialVersionUID = -2037999428860954218L;
    private JPanel contentPane;
    /***
     * 进度条
     */
    private JProgressBar progressBar;
    /***
     * 输入
     */
    private AssistPopupTextArea inputTextArea;
    /***
     * 输出
     */
    private JTextArea resultTextArea;
    /***
     * 编码时指定的编码
     */
    private JComboBox<String> encodingComboBox;
    private Callback2 callback;
    private Map<String, Callback2> callbackMap;
    private String action;
    private JToggleButton toggleButton_1;
    private JSplitPane splitPane;
    private int screenHeight;
    private JTabbedPane tabbedPane;
    /**
     * Launch the application.
     */
    /*public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UnicodePanel frame = new UnicodePanel();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

    /**
     * Create the frame.
     */
    public UnicodePanel(final String action, final Map<String, Callback2> callbackMap, final int screenHeight, JTabbedPane tabbedPane) {
        this.callbackMap = callbackMap;
        this.action = action;
        this.screenHeight = screenHeight;
        this.tabbedPane = tabbedPane;
//		fullScreen2(this);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setBounds(100, 100, 450, 300);
        contentPane = this;
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
//		setContentPane(contentPane);

        splitPane = new JSplitPane();
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation((screenHeight - 120) / 2);
        splitPane.setContinuousLayout(true);
        contentPane.add(splitPane, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane();
        splitPane.setLeftComponent(scrollPane);

        inputTextArea = new AssistPopupTextArea();
        inputTextArea.placeHolder("请输入内容(双击Shift 即可执行;Ctrl+K 聚焦)");
        callback = callbackMap.get(action);
        callback.setUnicodePanel(this);
        inputTextArea.setBackground(callback.getBackGroundColor());
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
                        if (MenuUtil2.isDoubleClick(currentTime - lastTimeMillSencond)) {
                            System.out.println("双击Shift");
                            doAction();
                            lastTimeMillSencond = 0;
                        } else {
                            lastTimeMillSencond = System.currentTimeMillis();
                        }
                    }
                }
            }
        });
        scrollPane.setViewportView(inputTextArea);
        Border border1 = BorderFactory.createEtchedBorder(CustomColor.getColor(),
                new Color(148, 145, 140));
        TitledBorder openFileTitle = new TitledBorder(border1, "输入");
        scrollPane.setBorder(openFileTitle);

        JPanel panel = new JPanel();
        splitPane.setRightComponent(panel);
        panel.setLayout(new BorderLayout(0, 0));

        JPanel panel_1 = new JPanel();
        panel.add(panel_1, BorderLayout.NORTH);

        encodingComboBox = callback.getJComboBox();
        if (!ValueWidget.isNullOrEmpty(encodingComboBox)) {
            panel_1.add(encodingComboBox);
        }
        if (null != encodingComboBox) {
            encodingComboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    doAction();
                }
            });
        }


        JButton button = new JButton(callback.getButtonLabel());

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doAction();
                resultTextArea.requestFocus();//聚焦结果文本域
            }
        });
        panel_1.add(button);

        JButton buttonAddtional = callback.getAdditionalBtn();
        if (!ValueWidget.isNullOrEmpty(buttonAddtional)) {
            buttonAddtional.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    doAction(true);
                }
            });
            panel_1.add(buttonAddtional);
        }

		/*JButton button_1 = new JButton("预留");
		panel_1.add(button_1);*/

        resultTextArea = new AssistPopupTextArea();
		/*JPopupMenu textPopupMenu=resultTextArea.getTextPopupMenu();
		JMenuItem copyM = new JMenuItem("haha");//用于测试的菜单
		textPopupMenu.add(copyM);*/
        JButton copyButton = ComponentUtil.getCopyBtn(resultTextArea, "复制结果");
        panel_1.add(copyButton);

        toggleButton_1 = new JToggleButton("扩大");
        toggleButton_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (toggleButton_1.isSelected()) {
                    reduce2();
                } else {
                    enlarge2();
                }
            }
        });
        panel_1.add(toggleButton_1);

        JButton cleanUpButton_1 = new JButton("清空结果");
        cleanUpButton_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultTextArea.setText(SystemHWUtil.EMPTY);
            }
        });
        panel_1.add(cleanUpButton_1);

        JScrollPane scrollPane_1 = new JScrollPane();
        Border border2 = BorderFactory.createEtchedBorder(CustomColor.getColor(),
                new Color(148, 145, 140));
        TitledBorder openFileTitle2 = new TitledBorder(border2, "结果");
        scrollPane_1.setBorder(openFileTitle2);
        panel.add(scrollPane_1, BorderLayout.CENTER);


        resultTextArea.setBackground(callback.getBackGroundColor());
        scrollPane_1.setViewportView(resultTextArea);

        //TODO 进度条暂时没有用
		/*progressBar = new JProgressBar();
		panel.add(progressBar, BorderLayout.SOUTH);*/

        String helpInfo = callback.getHelpInfo();
        JPanel panel_2 = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        add(panel_2, BorderLayout.NORTH);
        if (!ValueWidget.isNullOrEmpty(helpInfo)) {
            JLabel helpLabel = new JLabel(helpInfo);
            panel_2.add(helpLabel);
        }
        JButton pasteBtn = ComponentUtil.getPasteBtn(inputTextArea);
        panel_2.add(pasteBtn);
    }

    public void reduce2() {
        splitPane.setDividerLocation((this.screenHeight - 120) / 2);
        toggleButton_1.setText("扩大");
    }

    public void enlarge2() {
        splitPane.setDividerLocation(15);
        toggleButton_1.setText("缩小");
    }

    private void doAction() {
        doAction(false);
    }

    /***
     * 这个方法是真正干活的
     */
    private void doAction(boolean isAdditional) {
        resultTextArea.setText(SystemHWUtil.EMPTY);
        if (callback == null) {
            callback = callbackMap.get(action);
        }
        Object encoding = null;
        if (!ValueWidget.isNullOrEmpty(encodingComboBox)) {
            encoding = encodingComboBox.getSelectedItem();
        }
        if (null == callback.getUnicodePanel()) {
            callback.setUnicodePanel(this);
        }
        String result = null;
        if (isAdditional) {
            result = callback.callbackAdditional(inputTextArea.getText2(), encoding);
        } else {
            result = callback.callback(inputTextArea.getText2(), encoding);
        }
        tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), null == encoding ? SystemHWUtil.EMPTY : encoding.toString());
        callback.callback(inputTextArea.getText2(), encoding);
        if (!ValueWidget.isNullOrEmpty(result)) {
            resultTextArea.setText(result);
        } else {
            ToastMessage.toast("结果为空", 1000, Color.red);
        }
    }

    public AssistPopupTextArea getInputTextArea() {
        return inputTextArea;
    }

    @Override
    public String getRequestName() {
        return this.action;
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public String getActionName() {
        return null;
    }
}
