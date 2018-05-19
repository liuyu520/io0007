package com.swing.image;

import com.common.util.ReflectHWUtils;
import com.common.util.SystemHWUtil;
import com.string.widget.util.ValueWidget;
import com.swing.dialog.DialogUtil;
import com.swing.dialog.toast.ToastMessage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class SettingIconUtil {

    /***
     * @param resourcePath : "/com/pass/img/posterous_uploader.png"<br />
     *                     建议图标文件放在 Resource 中,
     * @throws IOException
     */
    public static void setIcon(JFrame jframe, String resourcePath, Class<?> clazz) throws IOException {
        ImageIcon imageIcon = DialogUtil.getImageIcon(resourcePath, clazz);
        if (null != imageIcon) {
            jframe.setIconImage(imageIcon.getImage());
            if (SystemHWUtil.isMacOSX) {//mac os  使用反射
//                com.apple.eawt.Application.getApplication().setDockIconImage(
//                        imageIcon.getImage());
                ReflectHWUtils.setDockIconImage(imageIcon.getImage());
            }
        } else if (!ValueWidget.isNullOrEmpty(resourcePath)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String errorMessage = "未找到图标文件,请确认图标文件可能在 java 文件夹中";
                    ToastMessage.toast(errorMessage, 5000, Color.red);
                    System.out.println(errorMessage + ",建议图标文件放在 Resource 中 :" + resourcePath);
                }
            }).start();
        }
    }

}
