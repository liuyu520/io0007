package com.swing.listener;

import java.awt.event.KeyAdapter;

/**
 * Created by whuanghkl on 17/3/7.
 */
public class DoubleKeyAdapter extends KeyAdapter {
    private long lastTimeMillSencond;
    private long lastTimeMillSencondDown;

    public long getLastTimeMillSencond() {
        return lastTimeMillSencond;
    }

    public void setLastTimeMillSencond(long lastTimeMillSencond) {
        this.lastTimeMillSencond = lastTimeMillSencond;
    }

    public long getLastTimeMillSencondDown() {
        return lastTimeMillSencondDown;
    }

    public void setLastTimeMillSencondDown(long lastTimeMillSencondDown) {
        this.lastTimeMillSencondDown = lastTimeMillSencondDown;
    }
}
