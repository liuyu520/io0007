package com.common.bean;

import com.string.widget.util.ValueWidget;

import java.io.Serializable;

/**
 * Created by 黄威 on 07/12/2016.<br >
 * 全局配置
 */
public class GlobalConfig implements Serializable {
    private boolean autoUrlEncoding;
    /***
     * 自动生成发送http请求代码的时候是否增加单元测试方法声明
     */
    private boolean generateRequestCodeWithUnitTest;
    /**
     * 是否立即上传文件<br />
     * see com/yunma/dialog/UploadPicDialog.java的dragResponse方法
     */
    private boolean autoUploadImmediately;
    /***
     * 创建请求时,是否自动选择"应用http默认值"
     */
    private boolean autoApplyDefaultHTTPWhenNewRequest;
    /***
     * 是否生成更基础的发送http请求代码
     */
    private boolean moreBasicHttpSendCode;
    /***
     * 保存自动生成的编程代码(java or python)时,默认的保存目录
     */
    private String saveCodeDefaultFolder = null;
    /**
     * 85000
     */
    private Integer connectTimeout;
    /**
     * 85000
     */
    private Integer readTimeout;
    /***
     * 智能模式
     */
    private boolean intelligenceMode;
    /***
     * 上传文件是否支持文件夹
     */
    private boolean isUploadSupportFolder;
    /***
     * 切换server ip 到 127.0.0.1 时,自动修改端口号为下
     */
    private Integer localhostSwitchPort;
    /***
     * 添加请求panel时免确认
     */
    private boolean noConfirmOnAddRequestPane;
    /***
     * 删除请求panel时免确认
     */
    private boolean noConfirmOnDeleteRequestPane;

    /***
     * 请求应用商店线上接口 时,是否弹框确认
     */
    private boolean isAlertConfirmWhenRequestOnline = true;
    /***
     * 聚焦searchTextField 时,是否选择全部(全选)
     */
    private boolean isSelectAllOnFocusSearchTF;
    private String sendRetryServletPath;
    private Integer sendRetryTimes;

    public boolean isAutoUrlEncoding() {
        return autoUrlEncoding;
    }

    public void setAutoUrlEncoding(boolean autoUrlEncoding) {
        this.autoUrlEncoding = autoUrlEncoding;
    }

    public boolean isGenerateRequestCodeWithUnitTest() {
        return generateRequestCodeWithUnitTest;
    }

    public void setGenerateRequestCodeWithUnitTest(boolean generateRequestCodeWithUnitTest) {
        this.generateRequestCodeWithUnitTest = generateRequestCodeWithUnitTest;
    }

    /**
     * 是否立即上传文件<br />
     * see com/yunma/dialog/UploadPicDialog.java的dragResponse方法
     *
     * @return
     */
    public boolean isAutoUploadImmediately() {
        return autoUploadImmediately;
    }

    public void setAutoUploadImmediately(boolean autoUploadImmediately) {
        this.autoUploadImmediately = autoUploadImmediately;
    }

    public boolean isAutoApplyDefaultHTTPWhenNewRequest() {
        return autoApplyDefaultHTTPWhenNewRequest;
    }

    public void setAutoApplyDefaultHTTPWhenNewRequest(boolean autoApplyDefaultHTTPWhenNewRequest) {
        this.autoApplyDefaultHTTPWhenNewRequest = autoApplyDefaultHTTPWhenNewRequest;
    }

    /***
     * 是否生成更基础的发送http请求代码
     * @return
     */
    public boolean isMoreBasicHttpSendCode() {
        return moreBasicHttpSendCode;
    }

    public void setMoreBasicHttpSendCode(boolean moreBasicHttpSendCode) {
        this.moreBasicHttpSendCode = moreBasicHttpSendCode;
    }

    /***
     * 保存自动生成的编程代码(java or python)时,默认的保存目录
     * @return
     */
    public String getSaveCodeDefaultFolder() {
        return saveCodeDefaultFolder;
    }

    /**
     * 保存自动生成的编程代码(java or python)时,默认的保存目录
     *
     * @param saveCodeDefaultFolder
     */
    public void setSaveCodeDefaultFolder(String saveCodeDefaultFolder) {
        this.saveCodeDefaultFolder = saveCodeDefaultFolder;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }

    public boolean isIntelligenceMode() {
        return intelligenceMode;
    }

    public void setIntelligenceMode(boolean intelligenceMode) {
        this.intelligenceMode = intelligenceMode;
    }

    public Integer getLocalhostSwitchPort() {
        return localhostSwitchPort;
    }

    public void setLocalhostSwitchPort(Integer localhostSwitchPort) {
        this.localhostSwitchPort = localhostSwitchPort;
    }

    public boolean isUploadSupportFolder() {
        return isUploadSupportFolder;
    }

    public void setUploadSupportFolder(boolean uploadSupportFolder) {
        isUploadSupportFolder = uploadSupportFolder;
    }

    public boolean isNoConfirmOnAddRequestPane() {
        return noConfirmOnAddRequestPane;
    }

    public void setNoConfirmOnAddRequestPane(boolean noConfirmOnAddRequestPane) {
        this.noConfirmOnAddRequestPane = noConfirmOnAddRequestPane;
    }

    public boolean isAlertConfirmWhenRequestOnline() {
        return isAlertConfirmWhenRequestOnline;
    }

    public void setAlertConfirmWhenRequestOnline(boolean alertConfirmWhenRequestOnline) {
        isAlertConfirmWhenRequestOnline = alertConfirmWhenRequestOnline;
    }

    public boolean isNoConfirmOnDeleteRequestPane() {
        return noConfirmOnDeleteRequestPane;
    }

    public void setNoConfirmOnDeleteRequestPane(boolean noConfirmOnDeleteRequestPane) {
        this.noConfirmOnDeleteRequestPane = noConfirmOnDeleteRequestPane;
    }

    public boolean isSelectAllOnFocusSearchTF() {
        return isSelectAllOnFocusSearchTF;
    }

    public void setSelectAllOnFocusSearchTF(boolean selectAllOnFocusSearchTF) {
        isSelectAllOnFocusSearchTF = selectAllOnFocusSearchTF;
    }

    public String getSendRetryServletPath() {
        return sendRetryServletPath;
    }

    public void setSendRetryServletPath(String sendRetryServletPath) {
        this.sendRetryServletPath = sendRetryServletPath;
    }

    public Integer getSendRetryTimes() {
        return sendRetryTimes;
    }

    public void setSendRetryTimes(Integer sendRetryTimes) {
        this.sendRetryTimes = sendRetryTimes;
    }

    /**
     * 如果想支持多个servletPath,可以使用逗号分割<br />
     * 输入框中输入/info/request_a,/logview/setConfig<br />
     * 那么同时支持两个请求地址
     *
     * @param servletPath
     * @return
     */
    public boolean needSendRetryTimes(String servletPath) {
        if (ValueWidget.isNullOrEmpty(getSendRetryServletPath())
                || null == getSendRetryTimes() || 0 == getSendRetryTimes()) {
            return false;
        }
        return getSendRetryServletPath().trim().contains(servletPath);
    }
}
