package com.swing.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuDto {
    List<String> menuItemLabelList = new ArrayList<>();
    private Map<String, MenuCallback2> callback2Map = new HashMap<>();

    public List<String> getMenuItemLabelList() {
        return menuItemLabelList;
    }

    public void setMenuItemLabelList(List<String> menuItemLabelList) {
        this.menuItemLabelList = menuItemLabelList;
    }

    public Map<String, MenuCallback2> getCallback2Map() {
        return callback2Map;
    }

    public void setCallback2Map(Map<String, MenuCallback2> callback2Map) {
        this.callback2Map = callback2Map;
    }

    public MenuDto put(String menuItemLabel, MenuCallback2 callback2) {
        this.menuItemLabelList.add(menuItemLabel);
        this.callback2Map.put(menuItemLabel, callback2);
        return this;
    }
}
