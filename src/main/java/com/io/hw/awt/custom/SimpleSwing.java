package com.io.hw.awt.custom;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class SimpleSwing extends JFrame implements Runnable
{

    private static final long serialVersionUID = 1809531460832873466L;
    private Container c = this.getContentPane();
    public abstract void customLayout(Container c);
    
    @Override
    public void run()
    {
        this.setTitle("Java");
        this.setVisible(true);
        this.setBounds(100, 200, 1200, 440);
        c.setLayout(new BorderLayout());
        JPanel originalPanel=new JPanel();
//        JButton button=new JButton("test");
//        originalPanel.add(button);
        c.add(originalPanel,BorderLayout.NORTH);
        customLayout(c);
        c.validate();
        this.addWindowListener(new WindowAdapter()
        {

            @Override
            public void windowClosing(WindowEvent arg0)
            {
                super.windowClosing(arg0);
                System.exit(0);
            }});
//        this.repaint();
    }
}
