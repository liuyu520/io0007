package com.swing.dialog;

import com.common.bean.HeightWidthBean;
import com.common.util.SystemHWUtil;
import com.string.widget.util.ValueWidget;
import com.swing.component.AssistPopupTextField;
import com.swing.dialog.toast.ToastMessage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SpecifyWidthAndHeightDialog extends GenericDialog {

    private static final long serialVersionUID = 7703582006425540953L;
    private JPanel contentPane;
    private AssistPopupTextField heightTextField;
    private AssistPopupTextField widthTextField;
    //	private JTextField textField;
    private HeightWidthBean heightWidthBean;
    private GenericDialog screenshotDialog;
    private JComboBox<String> resolutionComboBox;

    /**
     * Create the frame.
     */
    public SpecifyWidthAndHeightDialog(final HeightWidthBean heightWidthBean, GenericDialog screenshotDialog) {
        this.heightWidthBean = heightWidthBean;
        this.screenshotDialog = screenshotDialog;
        setModal(true);
        setTitle("指定高度和宽度");
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLoc(450, 150);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{0, 0, 0};
        gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_contentPane.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);

        JLabel label = new JLabel("高度");
        GridBagConstraints gbc_label = new GridBagConstraints();
        gbc_label.insets = new Insets(0, 0, 5, 5);
        gbc_label.anchor = GridBagConstraints.EAST;
        gbc_label.gridx = 0;
        gbc_label.gridy = 0;
        contentPane.add(label, gbc_label);

        heightTextField = new AssistPopupTextField();
        if (null != heightWidthBean) {
            heightTextField.setText(String.valueOf(heightWidthBean.getHeight()));
        }
        GridBagConstraints gbc_heightTextField = new GridBagConstraints();
        gbc_heightTextField.insets = new Insets(0, 0, 5, 0);
        gbc_heightTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_heightTextField.gridx = 1;
        gbc_heightTextField.gridy = 0;
        contentPane.add(heightTextField, gbc_heightTextField);
        heightTextField.setColumns(10);

        JLabel label_1 = new JLabel("宽度");
        GridBagConstraints gbc_label_1 = new GridBagConstraints();
        gbc_label_1.anchor = GridBagConstraints.EAST;
        gbc_label_1.insets = new Insets(0, 0, 5, 5);
        gbc_label_1.gridx = 0;
        gbc_label_1.gridy = 1;
        contentPane.add(label_1, gbc_label_1);

        widthTextField = new AssistPopupTextField();
        if (null != heightWidthBean) {
            widthTextField.setText(String.valueOf(heightWidthBean.getWidth()));
        }
        GridBagConstraints gbc_widthTextField = new GridBagConstraints();
        gbc_widthTextField.insets = new Insets(0, 0, 5, 0);
        gbc_widthTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_widthTextField.gridx = 1;
        gbc_widthTextField.gridy = 1;
        contentPane.add(widthTextField, gbc_widthTextField);
        widthTextField.setColumns(10);
        bindScreenshotEvent(widthTextField, heightTextField);
        /*JLabel label_2 = new JLabel("预留");
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.anchor = GridBagConstraints.EAST;
		gbc_label_2.insets = new Insets(0, 0, 5, 5);
		gbc_label_2.gridx = 0;
		gbc_label_2.gridy = 2;
		contentPane.add(label_2, gbc_label_2);

		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 2;
		contentPane.add(textField, gbc_textField);
		textField.setColumns(10);*/

        JLabel label_2 = new JLabel("分辨率");
        GridBagConstraints gbc_label_2 = new GridBagConstraints();
        gbc_label_2.anchor = GridBagConstraints.EAST;
        gbc_label_2.insets = new Insets(0, 0, 5, 5);
        gbc_label_2.gridx = 0;
        gbc_label_2.gridy = 2;
        contentPane.add(label_2, gbc_label_2);

        resolutionComboBox = new JComboBox();
        resolutionComboBox.addItem("1倍");
        resolutionComboBox.addItem("2倍");
        resolutionComboBox.addItem("3倍");
        resolutionComboBox.addItem("4倍");
        resolutionComboBox.setSelectedIndex(1);
        GridBagConstraints gbc_ersolutionComboBox = new GridBagConstraints();
        gbc_ersolutionComboBox.insets = new Insets(0, 0, 5, 0);
        gbc_ersolutionComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_ersolutionComboBox.gridx = 1;
        gbc_ersolutionComboBox.gridy = 2;
        contentPane.add(resolutionComboBox, gbc_ersolutionComboBox);

        JPanel panel = new JPanel();
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.gridwidth = 2;
        gbc_panel.insets = new Insets(0, 0, 0, 5);
        gbc_panel.fill = GridBagConstraints.BOTH;
        gbc_panel.gridx = 0;
        gbc_panel.gridy = 3;
        contentPane.add(panel, gbc_panel);

        JButton button = new JButton("确定截图");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                screenshots(heightWidthBean, false, false);
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if (null != heightWidthBean) {
                    heightWidthBean.setBeSuccess(false);
                }
                closeScreenshotDialog();
            }
        });

        panel.add(button);

        JButton editButton = new JButton("编辑截图");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                screenshots(heightWidthBean, false, true);
            }
        });
        panel.add(editButton);

        JButton saveBtn = new JButton("保存为");
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                screenshots(heightWidthBean, true, false);
            }
        });
        panel.add(saveBtn);
        JButton upload2serverBtn = new JButton("上传到服务器");
        upload2serverBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                screenshots(heightWidthBean, false, true/*isUpload2Server*/);
            }
        });
        panel.add(upload2serverBtn);
    }

    public void screenshots(HeightWidthBean heightWidthBean, boolean isSaveToFile, boolean editScreenshots) {
        screenshots(heightWidthBean, isSaveToFile, false, editScreenshots);
    }

    public void screenshots(HeightWidthBean heightWidthBean, boolean isSaveToFile, boolean isUpload2Server, boolean editScreenshots) {
        //校验
				/*if(!DialogUtil.verifyTFAndInteger(heightTextField,"高度")){
					return;
				}*/
        if (!DialogUtil.verifyTFAndInteger(widthTextField, "宽度")) {
            return;
        }
        int height2 = Integer.parseInt(heightTextField.getText());
        int width2 = Integer.parseInt(widthTextField.getText());
        if (height2 <= 0 || width2 <= 0) {
            ToastMessage.toast("高度或宽度不合法", 1000, Color.RED);
            return;
        }
        heightWidthBean.setHeight(height2);
        heightWidthBean.setWidth(width2);
        heightWidthBean.setSaveToFile(isSaveToFile);
        heightWidthBean.setUpload2Server(isUpload2Server);
        heightWidthBean.setEditScreenshots(editScreenshots);
        heightWidthBean.setMultiple(resolutionComboBox.getSelectedIndex() + 2);
//                System.out.println("分辨率:"+heightWidthBean.getMultiple());
        heightWidthBean.setBeSuccess(true);
        SpecifyWidthAndHeightDialog.this.dispose();//关闭对话框
        closeScreenshotDialog();
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SpecifyWidthAndHeightDialog frame = new SpecifyWidthAndHeightDialog(null, null);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /***
     * 关闭截屏的对话框
     */
    public void closeScreenshotDialog() {
    	GenericDialog screenshotDialog2=SpecifyWidthAndHeightDialog.this.screenshotDialog;
    	if(null!=screenshotDialog2){
    		screenshotDialog2.dispose();
    	}
    }

    private void bindScreenshotEvent(final JTextComponent widthTextField, final JTextComponent heightTextField) {
        widthTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
//                String widthStr = widthTextField.getText();
//                updateWidth(widthStr);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                String widthStr = widthTextField.getText();
                updateWidth(widthStr);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                String widthStr = widthTextField.getText();
                updateWidth(widthStr);
            }
        });

        bindEnterEvent(widthTextField);

        heightTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
//                String widthStr = heightTextField.getText();
//                updateHeight(widthStr);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                String widthStr = heightTextField.getText();
                updateHeight(widthStr);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                String widthStr = heightTextField.getText();
                updateHeight(widthStr);
            }
        });
        bindEnterEvent(heightTextField);
    }

    private void bindEnterEvent(final JTextComponent widthTextField) {
        if (widthTextField instanceof JTextField) {
            JTextField widthTF = (JTextField) widthTextField;
            widthTF.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String widthStr = widthTextField.getText();
                    updateWidth(widthStr);
                }
            });
        }
    }

    private void updateWidth(String widthStr) {
        if (ValueWidget.isInteger(widthStr)) {
            Integer width = Integer.parseInt(widthStr);
            updateScreenshotDialogSize(width, SystemHWUtil.NEGATIVE_ONE);
        }
    }

    private void updateHeight(String heightStr) {
        if (ValueWidget.isInteger(heightStr)) {
            Integer height = Integer.parseInt(heightStr);
            updateScreenshotDialogSize(SystemHWUtil.NEGATIVE_ONE, height);
        }
    }

    private void updateScreenshotDialogSize(int width, int height) {
        if (SystemHWUtil.NEGATIVE_ONE == width) {
            width = this.screenshotDialog.getWidth();
        }
        if (SystemHWUtil.NEGATIVE_ONE == height) {
            height = this.screenshotDialog.getHeight();
        }
        this.screenshotDialog.setSize(width, height);//不要设置位置,已经在RTextArea 的showScreenshotDialog 方法中设置了位置
    }
}
