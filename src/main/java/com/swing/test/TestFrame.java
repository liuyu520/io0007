package com.swing.test;

import java.awt.Container;

import javax.swing.JTextArea;

import com.swing.dialog.GenericFrame;

/**
 * Created by huangweii on 2016/1/10.
 */
public class TestFrame extends GenericFrame {
    @Override
    public void layout3(Container contentPane) {
        super.layout3(contentPane);
        setLoc(200,200);
        JTextArea rTextArea=new JTextArea("文本内容");
//        rTextArea.setText("文本内容");
        contentPane.add(rTextArea);
    }
    public static void main(String[]args){
        new TestFrame().launchFrame();
    }
}
