package com.common.bean;

public class RSAFormatConf {
    /***
     * appendComment is true default,
     * @param isPrivate
     * @return
     */
    public static RSAFormatConf getInstance(boolean isPrivate) {
        RSAFormatConf rsaFormatConf = new RSAFormatConf();
        rsaFormatConf.setAppendComment(true);
        rsaFormatConf.setPrivate(isPrivate);
        return rsaFormatConf;
    }

    /***
     * s是否是私钥
     */
    private boolean isPrivate;
    /***
     * 是否增加"-----BEGIN PRIVATE KEY-----"<br />
     *      * "-----BEGIN PUBLIC KEY-----"
     */
    private boolean appendComment;

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean isAppendComment() {
        return appendComment;
    }

    public void setAppendComment(boolean appendComment) {
        this.appendComment = appendComment;
    }
}
