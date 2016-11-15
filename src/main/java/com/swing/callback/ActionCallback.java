package com.swing.callback;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by 黄威 on 8/26/16.<br >
 */
public interface ActionCallback {
    public void actionPerformed(ActionEvent evt, JComponent component);
}
