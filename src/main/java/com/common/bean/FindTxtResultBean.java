package com.common.bean;

/**
 * Created by huangweii on 2015/12/16.
 * 搜索结果
 */
public class FindTxtResultBean {
    /***
     * 出现了多少次
     */
    private int count;
    /***
     * 在哪个位置发现的
     */
    private int foundIndex;
    /***
     * 搜索的关键字
     */
    private String keyWord;
    /***
     * 当查询到最后一个时,是否弹框
     */
    private Boolean isHideDialog;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getFoundIndex() {
        return foundIndex;
    }

    public void setFoundIndex(int foundIndex) {
        this.foundIndex = foundIndex;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    @Override
    public String toString() {
        return "FindTxtResultBean [count=" + count + ", foundIndex="
                + foundIndex + "]";
    }

    public Boolean getHideDialog() {
        return isHideDialog;
    }

    public void setHideDialog(Boolean isHideDialog) {
        this.isHideDialog = isHideDialog;
    }
}
