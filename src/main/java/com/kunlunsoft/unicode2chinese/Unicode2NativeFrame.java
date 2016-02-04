package com.kunlunsoft.unicode2chinese;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class Unicode2NativeFrame extends JFrame implements ItemListener
{
    private static final long serialVersionUID = 2302946361853646828L;
    JTextField                 oldValueTF;
    JTextField                 newValueTF;
    JRadioButton              toUnicodeButton;
    JRadioButton              toNativeButton;
    JButton conversionButton;
    boolean                   isToUnicode=true;

    public static void main(String[] args)
    {
        //        String command="native2ascii e:/test/a.txt";
        //        String cmdResult=CMDUtil.cmdCmdreStr(command);
        //        System.out.println(cmdResult);
        //        String str="中国";
        //        System.out.println(CMDUtil.resolveUnicode(str,true));
        new Unicode2NativeFrame().launchFrame();
    }

    public void launchFrame()
    {
        this.setTitle("Unicode 与中文转化器1.1");
        oldValueTF = new JTextField(10);
        newValueTF = new JTextField(10);
        oldValueTF.setBackground(new Color(255, 0, 0));
        newValueTF.setBackground(new Color(255, 255, 200));
        newValueTF.setEditable(false);
        this.setLayout(new BorderLayout());
        this.add(oldValueTF, BorderLayout.NORTH);
        this.add(newValueTF, BorderLayout.SOUTH);
        JPanel centPanel = new JPanel();
        conversionButton= new JButton("转化");
        oldValueTF.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				conversionButton.doClick();
				
			}
		});
        conversionButton.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                String oldValue = oldValueTF.getText();
                if (null != oldValue && !"".equals(oldValue))
                {
                    String result = Conversion.resolveUnicode(oldValue,
                        isToUnicode);
                    //                    String result=Conversion.unicodeToChinese(oldValue);
                    newValueTF.setText(result);
                }
            }
        });
        Panel unicodeRadioPanel = new Panel();
        ButtonGroup oldScaleGroup = new ButtonGroup();
        toUnicodeButton = new JRadioButton("中文-- > Unicode");
        toUnicodeButton.setSelected(true);
        toNativeButton = new JRadioButton("Unicode-- > 中文");
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
        this.setBounds(400, 250, 400, 150);
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
        //        System.out.println("press");
        newValueTF.setText("");
        if (e.getSource() == this.toNativeButton)
        {
            this.isToUnicode = false;
        }
        else
        {
            this.isToUnicode = true;
        }

    }
}
