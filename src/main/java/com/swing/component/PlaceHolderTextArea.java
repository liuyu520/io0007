package com.swing.component;

import com.common.util.SystemHWUtil;
import com.swing.component.inf.IPlaceHolder;

/**
 * Created by Administrator on 2016/1/10.
 */
public class PlaceHolderTextArea extends UndoTextArea implements IPlaceHolder {
    public PlaceHolderTextArea(String text) {
        super(text);
        initlize();
    }

    public PlaceHolderTextArea() {
        super();
        initlize();

    }

    public PlaceHolderTextArea(int rows, int columns) {
        super(rows, columns);
        initlize();

    }

    /***
     * 排除了placeholder
     *
     * @return
     */
    public String getText2() {
        String text = super.getText();
        if (TextCompUtil2.isPlaceHolder(this, text, placeHolderText)) {
            return SystemHWUtil.EMPTY;
        } else {
            return text;
        }
    }

    /***
     * 设置默认提示语
     *
     * @param placeHolder
     */
    public void setPlaceHolder(String placeHolder) {
        TextCompUtil2.setPlaceHolder(this, placeHolder);
    }

    /***
     * 判断当前文本是否是默认提示语
     *
     * @param placeHolder
     * @return
     */
    public boolean isPlaceHolder(String placeHolder) {
        return TextCompUtil2.isPlaceHolder(this, placeHolder);
    }

    /***
     * 设置默认提示语,包括监听focus事件
     *
     * @param placeHolder
     */
    public void placeHolder(String placeHolder) {
        setPlaceHolder(placeHolder);
        TextCompUtil2.placeHolderFocus(this, placeHolder);
    }
}
