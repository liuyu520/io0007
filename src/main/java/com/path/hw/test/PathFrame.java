package com.path.hw.test;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import com.path.hw.PathUtils;
import com.swing.dialog.GenericFrame;

public class PathFrame extends GenericFrame implements ItemListener
{
    private static final long serialVersionUID  = 2302946361853646828L;
    public static final int   LINUX_UNIX_PATH        = 1;
    public static final int   WINDOWS_PATH         = 2;
    public static final int   UTF8_OR_GBK_TO_CH = 3;

    TextField                 oldValueTF;
    TextField                 newValueTF;
    JRadioButton              toLinuxPathButton;
    JRadioButton              toWindowsPathButton;
    int                       isToUnicode;

    public static void main(String[] args)
    {
        new PathFrame().launchFrame();
    }

    public void launchFrame()
    {
        this.setTitle("Linux/Windows");
        oldValueTF = new TextField(10);
        newValueTF = new TextField(10);
        oldValueTF.setBackground(new Color(255, 0, 0));
        newValueTF.setBackground(new Color(255, 255, 200));
        newValueTF.setEditable(false);
        this.setLayout(new BorderLayout());
        this.add(oldValueTF, BorderLayout.NORTH);
        this.add(newValueTF, BorderLayout.SOUTH);
        Panel centPanel = new Panel();
        Button conversionButton = new Button();
        conversionButton.setLabel("convert");
        conversionButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String oldValue = oldValueTF.getText();
                if (null != oldValue && !"".equals(oldValue))
                {
                    //                    String result = Conversion.resolveUnicode(oldValue,
                    String result = null;
                    if (isToUnicode == WINDOWS_PATH)
                    {
                        result = PathUtils.windowsPath(oldValue,1);

                    }
                    else if (isToUnicode == LINUX_UNIX_PATH)
                    {
                        result = PathUtils.linuxPath(oldValue,1);
                    }
                    if (null != result && !"".equals(result))
                    {
                        newValueTF.setText(result);
                    }
                }
            }
        });
        Panel unicodeRadioPanel = new Panel();
        ButtonGroup oldScaleGroup = new ButtonGroup();
        toLinuxPathButton = new JRadioButton("-- > Linux ");
        toWindowsPathButton = new JRadioButton("-- > Windows ");
        oldScaleGroup.add(toLinuxPathButton);
        oldScaleGroup.add(toWindowsPathButton);
        toLinuxPathButton.addItemListener(this);
        toWindowsPathButton.addItemListener(this);
        centPanel.setLayout(new BorderLayout());
        unicodeRadioPanel.add(toLinuxPathButton);
        unicodeRadioPanel.add(toWindowsPathButton);
        centPanel.add(unicodeRadioPanel, BorderLayout.NORTH);
        centPanel.add(conversionButton, BorderLayout.SOUTH);
        this.add(centPanel, BorderLayout.CENTER);
        this.setVisible(true);
//        this.setBounds(400, 250, 400, 150);
        setLoc(400, 150);
        this.setResizable(false);
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
        if (e.getSource() == this.toWindowsPathButton)
        {
            this.isToUnicode = WINDOWS_PATH;
        }
        else if (e.getSource() == this.toLinuxPathButton)
        {
            this.isToUnicode = LINUX_UNIX_PATH;
        }
        else
        {
            this.isToUnicode = UTF8_OR_GBK_TO_CH;
        }

    }
}
