package com.swing.dialog;

import com.swing.component.AssistPopupTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimpleTextEditDialog extends GenericDialog {

    private static final long serialVersionUID = 5380855381490259223L;
    private final JPanel contentPanel = new JPanel();
    private JTextComponent targetTF;
    //    private AssistPopupTextArea textArea;
    private Container parent;

    /**
     * Create the dialog.
     */
    public SimpleTextEditDialog(final JTextComponent targetTF) {
        if (null != targetTF) {
            parent = targetTF.getParent();
            this.targetTF = targetTF;
        } else {
            this.targetTF = new AssistPopupTextField();
        }

        setModal(true);
        setTitle("编辑");
        setLoc(450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
            JScrollPane scrollPane = new JScrollPane();
            contentPanel.add(scrollPane);
                /*textArea = new AssistPopupTextArea();
                if (!ValueWidget.isNullOrEmpty(SimpleTextEditDialog.this.targetTF)) {
                    String text = null;
                    if (targetTF instanceof IPlaceHolder) {
                        IPlaceHolder placeHolder = (IPlaceHolder) targetTF;
                        text = placeHolder.getText2();
                    } else {
                        text = SimpleTextEditDialog.this.targetTF.getText();
                    }
                    textArea.setText(text);
                }*/
                scrollPane.setViewportView(targetTF);
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                       /* String json = textArea.getText();
                        if (!ValueWidget.isNullOrEmpty(SimpleTextEditDialog.this.targetTF)
                                && !ValueWidget.isNullOrEmpty(json)) {
                            SimpleTextEditDialog.this.targetTF.setText(json);
                        }*/
                        parent.add(targetTF);
                        SimpleTextEditDialog.this.dispose();
                    }
                });
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SimpleTextEditDialog.this.dispose();
                    }
                });
                buttonPane.add(cancelButton);
            }
        }

   /* *//**
     * Launch the application.
     *//*
    public static void main(String[] args) {
        try {
            SimpleTextEditDialog dialog = new SimpleTextEditDialog(null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
