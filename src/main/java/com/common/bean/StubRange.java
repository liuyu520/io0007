package com.common.bean;

import java.util.List;

/**
 * Created by 黄威 on 5/3/16.<br >
 */
public class StubRange {
    private List<String> stubs;
    /***
     * 当前的stub,序号从零开始
     */
    private int selectedIndex;

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public List<String> getStubs() {
        return stubs;
    }

    public void setStubs(List<String> stubs) {
        this.stubs = stubs;
    }
}
