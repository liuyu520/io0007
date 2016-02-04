package com.swing.dialog.inf;

import java.awt.*;
import java.io.File;
import java.util.List;

public interface DialogInterface {
    /***
     * 拖拽的处理方法
     *
     * @param list
     * @param component
     */
    void dragResponse(List<File> list, Component component);
}
