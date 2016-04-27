package com.swing.dialog;

import com.common.util.SystemHWUtil;
import com.common.util.WindowUtil;
import com.string.widget.util.ValueWidget;
import com.swing.component.AssistPopupTextArea;
import com.swing.component.AssistPopupTextField;
import com.swing.component.ComponentUtil;
import com.swing.dialog.toast.ToastMessage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class GenerateMD5Dialog extends GenericDialog {

    private static final long serialVersionUID = -4151740071609032069L;
    private final AssistPopupTextField sourceTxt;
    private final JComboBox<String> encodingComboBox;
    private AssistPopupTextArea resultTextArea;

    /**
     * Create the frame.
     */
    public GenerateMD5Dialog(boolean modal) {
        setTitle("生成MD5值");
        setModal(modal);
        setLoc(450, 300);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0};
        gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);

        JLabel label = new JLabel("原文本");
        GridBagConstraints gbc_label = new GridBagConstraints();
        gbc_label.insets = new Insets(0, 0, 5, 5);
        gbc_label.gridx = 0;
        gbc_label.gridy = 0;
        contentPane.add(label, gbc_label);

        sourceTxt = new AssistPopupTextField();
        GridBagConstraints gbc_sourceTxt = new GridBagConstraints();
        gbc_sourceTxt.insets = new Insets(0, 0, 5, 0);
        gbc_sourceTxt.fill = GridBagConstraints.HORIZONTAL;
        gbc_sourceTxt.gridx = 2;
        gbc_sourceTxt.gridy = 0;
        contentPane.add(sourceTxt, gbc_sourceTxt);
        sourceTxt.setColumns(10);

        JLabel label_1 = new JLabel("文件编码");
        GridBagConstraints gbc_label_1 = new GridBagConstraints();
        gbc_label_1.insets = new Insets(0, 0, 5, 5);
        gbc_label_1.gridx = 0;
        gbc_label_1.gridy = 1;
        contentPane.add(label_1, gbc_label_1);

        encodingComboBox = new JComboBox<String>();
        GridBagConstraints gbc_encodingComboBox = new GridBagConstraints();
        gbc_encodingComboBox.insets = new Insets(0, 0, 5, 0);
        gbc_encodingComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_encodingComboBox.gridx = 2;
        gbc_encodingComboBox.gridy = 1;
        contentPane.add(encodingComboBox, gbc_encodingComboBox);
        //文件的编码,window里面一般是GBK,linux中一般是UTF-8
        encodingComboBox.addItem(SystemHWUtil.EMPTY);
        encodingComboBox.addItem(SystemHWUtil.CHARSET_UTF);
        encodingComboBox.addItem(SystemHWUtil.CHARSET_GBK);
        encodingComboBox.addItem(SystemHWUtil.CHARSET_GB2312);
        encodingComboBox.addItem(SystemHWUtil.CHARSET_ISO88591);
        //设置默认选中的项
        encodingComboBox.setSelectedIndex(1);//默认使用UTF-8编码

        JPanel panel = new JPanel();
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.insets = new Insets(0, 0, 5, 0);
        gbc_panel.gridwidth = 3;
        gbc_panel.fill = GridBagConstraints.BOTH;
        gbc_panel.gridx = 0;
        gbc_panel.gridy = 2;
        contentPane.add(panel, gbc_panel);

        JButton generateMDbutton = new JButton("生成");
        generateMDbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String inputText = sourceTxt.getText();
                String charset = getSelectedItem4ComboBox(encodingComboBox);
                if (ValueWidget.isNullOrEmpty(charset)) {
                    charset = SystemHWUtil.CURR_ENCODING;
                }
                try {
                    String md5 = SystemHWUtil.getMD5(inputText, charset);
                    resultTextArea.setText(md5);
                } catch (NoSuchAlgorithmException e1) {
                    e1.printStackTrace();
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }

            }
        });
        panel.add(generateMDbutton);
        resultTextArea = new AssistPopupTextArea();
        JButton btnCopy = ComponentUtil.getCopyBtn(resultTextArea);
        panel.add(btnCopy);

        JButton copyAndClose = new JButton("复制并关闭");
        copyAndClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ValueWidget.isNullOrEmpty(resultTextArea)) {
                    ToastMessage.toast("文本框为null,请确认文本框是否已经创建", 3000, Color.red);
                } else {
                    String input = resultTextArea.getText();
                    if (!ValueWidget.isNullOrEmpty(input)) {
                        WindowUtil.setSysClipboardText(input);
                    }
                    GenerateMD5Dialog.this.dispose();//关闭对话框
                }
            }
        });
        panel.add(copyAndClose);

        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.gridwidth = 3;
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 3;
        contentPane.add(scrollPane, gbc_scrollPane);

        //结果文本域

        resultTextArea.setLineWrap(true);
        resultTextArea.setWrapStyleWord(true);
        resultTextArea.setEditable(false);
        scrollPane.setViewportView(resultTextArea);
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GenerateMD5Dialog frame = new GenerateMD5Dialog(false);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
