package com.swing.dialog;

import com.common.bean.FindTxtResultBean;
import com.common.dict.Constant2;
import com.common.util.ReflectHWUtils;
import com.common.util.WindowUtil;
import com.string.widget.util.ValueWidget;
import com.swing.component.AssistPopupTextField;
import com.swing.messagebox.GUIUtil23;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchInputDialog extends GenericDialog {

    private static final long serialVersionUID = 5380855381490259223L;
    private final JPanel contentPanel = new JPanel();
    private JTextComponent targetTF;
    private AssistPopupTextField textArea;

    /**
     * Create the dialog.
     */
    public SearchInputDialog(JTextComponent targetTF, String keyword) {
        setModal(true);
        this.targetTF = targetTF;
        setTitle("搜索");
        setLoc(450, 100);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        textArea = new AssistPopupTextField(false/*needSearch*/);
        textArea.placeHolder("请输入关键字,不区分大小写");
        contentPanel.add(textArea, BorderLayout.NORTH);
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
                JButton searchButton = new JButton("搜索");
                searchButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String json = textArea.getText2();
                        if (ValueWidget.isNullOrEmpty(json)) {
                            textArea.requestFocus();
                            GUIUtil23.alert("请输入关键字.");
                            return;
                        }
                        //一定从索引为0的地方查找
                        FindTxtResultBean findTxtResultBean = DialogUtil.searchText(SearchInputDialog.this.targetTF, 0, json);
                        try {
                            ReflectHWUtils.setObjectValue(SearchInputDialog.this.targetTF, Constant2.FINDTXTRESULTBEAN_FIELD, findTxtResultBean, false);
                            SearchInputDialog.this.dispose();
                        } catch (IllegalAccessException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
                buttonPane.add(searchButton);
                getRootPane().setDefaultButton(searchButton);
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SearchInputDialog.this.dispose();
                    }
                });
                buttonPane.add(cancelButton);
        }
        //优先搜索上次搜索的关键字
        if (ValueWidget.isNullOrEmpty(keyword)) {
            FindTxtResultBean findTxtResultBean = null;
            try {
                findTxtResultBean = (FindTxtResultBean) ReflectHWUtils.getObjectValue(SearchInputDialog.this.targetTF, Constant2.FINDTXTRESULTBEAN_FIELD);
                if (null != findTxtResultBean) {
                    keyword = findTxtResultBean.getKeyWord();
                }
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
        if (ValueWidget.isNullOrEmpty(keyword)) {
            keyword = WindowUtil.getSysClipboardText();
            if (!ValueWidget.isNullOrEmpty(keyword) && keyword.length() < 20) {//太长的字符串就忽略
                textArea.setText(keyword);
                textArea.requestFocus();
                textArea.selectAll();
            }
        } else {
            textArea.setText(keyword);
            textArea.requestFocus();
            textArea.selectAll();
        }

    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            SearchInputDialog dialog = new SearchInputDialog(null, null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
