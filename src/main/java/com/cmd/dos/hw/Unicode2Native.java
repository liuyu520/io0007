package com.cmd.dos.hw;

import com.cmd.dos.hw.util.CMDUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Unicode2Native extends Frame implements ItemListener
{
    private static final long serialVersionUID = 2302946361853646828L;
    boolean isToUnicode;
    private JTextField    oldValueTF;
    private JTextField    newValueTF;
    private JRadioButton toNativeButton;

    public static void main(String[] args)
    {
        //        String command="native2ascii e:/test/a.txt";
        //        String cmdResult=CMDUtil.cmdCmdreStr(command);
        //        System.out.println(cmdResult);
        //        String str="中国";
        //        System.out.println(CMDUtil.resolveUnicode(str,true));
        new Unicode2Native().launchFrame();
    }

    public void launchFrame()
    {
        this.setTitle("Unicode 与中文转化器1.0");
        oldValueTF = new JTextField(10);
        newValueTF = new JTextField(10);
        oldValueTF.setBackground(new Color(255, 0, 0));
        newValueTF.setBackground(new Color(255, 255, 200));
        newValueTF.setEditable(false);
        this.setLayout(new BorderLayout());
        this.add(oldValueTF, BorderLayout.NORTH);
        this.add(newValueTF, BorderLayout.SOUTH);
        Panel centPanel = new Panel();
        Button conversionButton = new Button();
        conversionButton.setLabel("转化");
        conversionButton.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                String oldValue = oldValueTF.getText();
                if (null != oldValue && !"".equals(oldValue))
                {
                    newValueTF.setText(CMDUtil.resolveUnicode(oldValue,
                        isToUnicode));
                }
            }
        });
        Panel unicodeRadioPanel = new Panel();
        ButtonGroup oldScaleGroup = new ButtonGroup();
        JRadioButton toUnicodeButton = new JRadioButton("-- > Unicode");
        toNativeButton = new JRadioButton("-- > 中文");
        oldScaleGroup.add(toUnicodeButton);
        oldScaleGroup.add(toNativeButton);
        toUnicodeButton.addItemListener(this);
        toNativeButton.addItemListener(this);
        centPanel.setLayout(new BorderLayout());
        unicodeRadioPanel.add(toUnicodeButton);
        unicodeRadioPanel.add(toNativeButton);
        centPanel.add(unicodeRadioPanel, BorderLayout.NORTH);
        centPanel.add(conversionButton, BorderLayout.SOUTH);
        this.add(centPanel, BorderLayout.CENTER);
        this.setVisible(true);
        this.setBounds(400, 250, 300, 150);
        this.setBackground(new Color(255, 200, 200));
        //        this.pack();
        this.addWindowListener(new WindowAdapter()
        {

            @Override
            public void windowClosing(WindowEvent e)
            {
                super.windowClosing(e);
                System.exit(0);
            }

        });
    }

    @Override
    public void itemStateChanged(ItemEvent e)
    {
        newValueTF.setText("");
        this.isToUnicode = e.getSource() != this.toNativeButton;

    }
}
