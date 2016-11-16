package com.common.task;

import com.swing.component.QRCodePanel;

/**
 * Created by 黄威 on 16/11/2016.<br >
 */
public class TextBoxTask extends java.util.TimerTask {
    private QRCodePanel qrCodePanel;

    public TextBoxTask(QRCodePanel qrCodePanel) {
        super();
        this.qrCodePanel = qrCodePanel;
    }

    @Override
    public void run() {
        //输入完成之后生成二维码
        System.out.println("$$$$$");
        this.qrCodePanel.generateQRAction(false);
    }
}
