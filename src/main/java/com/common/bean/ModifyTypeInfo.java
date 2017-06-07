package com.common.bean;

/**
 * Created by 黄威 on 19/01/2017.<br >
 * 修改类型:1-完全替换;<br />2-关键字替换;<br />3-正则表达式替换
 */
public class ModifyTypeInfo {
    /***
     * 1-完全替换
     */
    public static final int MODIFY_TYPE_REPLACE_TOTALLY = 1;
    /***
     * 2-关键字替换
     */
    public static final int MODIFY_TYPE_KEY_WORD = 2;
    /***
     * 3-正则表达式替换
     */
    public static final int MODIFY_TYPE_REGUX_REPLACE = 3;
    private int modifyType;
    private String keyword;
    private String replacement;
    /***
     * 当值为Constant2.NULL,表示不替换
     */
    private String contentType;

    public int getModifyType() {
        return modifyType;
    }

    public void setModifyType(int modifyType) {
        this.modifyType = modifyType;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getReplacement() {
        return replacement;
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }

    @Override
    public String toString() {
        return "ModifyTypeInfo{" +
                "modifyType=" + modifyType +
                ", keyword='" + keyword + '\'' +
                ", replacement='" + replacement + '\'' +
                '}';
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
