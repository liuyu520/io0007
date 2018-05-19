package com.common.bean.exception;

/**
 * Created by whuanghkl on 3/31/16.<br />
 * 功能:捕获应用商店业务方便的错误,比如提交订单失败,参数校验错误
 */
public class LogicBusinessException extends RuntimeException {
    private String errorCode;
    private String errorMessage;//同时设置RuntimeException 自己的message
    /***
     * 重定向的地址,只要不为空,则必须跳转
     */
    private String redirectUrl;
    /***
     * 返回json
     */
    private String responseBody;
    private LogicBusinessGroup logicBusinessGroup;
    /***
     * 是否是移动端
     */
    private boolean wap;

    public LogicBusinessException() {
    }

    public LogicBusinessException(String message) {
        super(message);
        this.errorMessage = message;
    }

    public LogicBusinessException(String message, Throwable cause) {
        super(message, cause);
        this.errorMessage = message;
    }

    public LogicBusinessException(Throwable cause) {
        super(cause);
    }

    public LogicBusinessException(String errorCode, String errorMessage, String redirectUrl) {
        super(errorMessage);
        this.redirectUrl = redirectUrl;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public LogicBusinessException(boolean wap, String responseBody) {
        this.wap = wap;
        this.setResponseBody(responseBody);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public LogicBusinessException setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public LogicBusinessException setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public LogicBusinessException setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
        return this;
    }

    @Override
    public String toString() {
        return "LogicBusinessException{" +
                ", errorCode='" + errorCode + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }

    public String getResponseBody() {
        return responseBody;
    }

    public LogicBusinessException setResponseBody(String responseBody) {
        this.responseBody = responseBody;
        return this;
    }

    public boolean isWap() {
        return wap;
    }

    public LogicBusinessException setWap(boolean wap) {
        this.wap = wap;
        return this;
    }

    public static void throwException(String errorCode, String msg, String redirectUrl) throws LogicBusinessException {
        throw new LogicBusinessException(errorCode, msg, redirectUrl);
    }

    public static void throwException(String errorCode, String msg) throws LogicBusinessException {
        throw new LogicBusinessException(errorCode, msg, null);
    }

    public static void throwException(String errorCode, String msg, boolean isJson) throws LogicBusinessException {
        throw new LogicBusinessException(errorCode, msg, null).setWap(isJson);
    }

    public static void throwExceptionWithGroup(String errorCode, String msg, LogicBusinessGroup logicBusinessGroup) throws LogicBusinessException {
        LogicBusinessException logicBusinessException = new LogicBusinessException(errorCode, msg, null);
        logicBusinessException.setLogicBusinessGroup(logicBusinessGroup);
        throw logicBusinessException;
    }

    public static void throwException(String msg, Throwable cause) throws LogicBusinessException {
        throw new LogicBusinessException(msg, cause);
    }

    public static void throwException(String errorCode) throws LogicBusinessException {
        throw new LogicBusinessException(errorCode, null, null);
    }

    public static void throwExceptionNoMsg(String errorCode, String redirectUrl) throws LogicBusinessException {
        throw new LogicBusinessException(errorCode, null, redirectUrl);
    }

    public LogicBusinessGroup getLogicBusinessGroup() {
        return logicBusinessGroup;
    }

    public void setLogicBusinessGroup(LogicBusinessGroup logicBusinessGroup) {
        this.logicBusinessGroup = logicBusinessGroup;
    }
}
