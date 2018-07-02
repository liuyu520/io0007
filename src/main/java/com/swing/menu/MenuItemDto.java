package com.swing.menu;

import com.string.widget.util.ValueWidget;

public class MenuItemDto {
    private String menuItemLabel;
    private String command;
    private MenuCallback2 callback2;

    public String getMenuItemLabel() {
        return menuItemLabel;
    }

    public void setMenuItemLabel(String menuItemLabel) {
        this.menuItemLabel = menuItemLabel;
    }

    public String getCommand() {
        if (ValueWidget.isNullOrEmpty(command)) {
            return this.getMenuItemLabel();
        }
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public MenuCallback2 getCallback2() {
        return callback2;
    }

    public void setCallback2(MenuCallback2 callback2) {
        this.callback2 = callback2;
    }
}
