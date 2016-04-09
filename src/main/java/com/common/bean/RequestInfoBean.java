package com.common.bean;

import com.common.dict.Constant2;
import com.common.util.WebServletUtil;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/***
 * 一次具体的请求信息
 *
 * @author huangweii
 *         2015年6月28日
 */
public class RequestInfoBean implements Serializable {
    private static final long serialVersionUID = 2426877461354119516L;
    /***
     * 服务器的ip
     */
    private String serverIp;
    /***
     * 端口号,比如80,8080
     */
    private String port;
    /***
     * 项目名称,例如"tv_mobile"
     */
    private String actionPath;
    /***
     * 请求参数的编码
     */
    private String charset;
    /***
     * POST:2<br>GET:0
     */
    private int requestMethod;
    /***
     * 请求体
     */
    private String requestBodyData;
    /***
     * 前置请求的参数名
     */
    private String preRequestParameterName;
    /***
     * 请求ID
     */
    private String requestID;
    /***
     * 当期的请求名称
     * <br>用于搜索
     */
    private String currentRequestName;

    /***
     * 本请求的参数名称
     */
    private String currRequestParameterName;
    private String preRequestId;
    /***
     * 请求的cookie
     */
    private String requestCookie;
    /***
     * 连续发送请求的次数
     */
    private String sendCount;
    /***
     * 是否对参数进行url编码
     */
    private boolean isUrlEncoding;
    /***
     * 是否应用HTTP请求默认值
     */
    private boolean isApplyDefaultHTTP;
    /***
     * 请求体是否是json字符串(不是标准的query string)
     */
    private boolean requestBodyIsJson;
    /***
     * 是否有前置请求("前置请求id"复选框)
     */
    private boolean isHasPreRequest;
    /***
     * 应答体的json格式
     */
    private String jsonResult;
    /***
     * 是否每次都请求前置请求
     */
    private boolean sendPreRequestPer;
    /***
     * 默认的请求要素(可以把"默认的"去掉)
     */
    private TreeMap<String, Object> requestParameters;
    /***
     * 定时发送的倒计时,单位:秒
     */
    private int timingSecond;
    /***
     * 单个请求的备忘
     */
    private String singleRequestNote;
    private boolean isEncodingCheckbox;
    /***
     * http or https
     */
    private boolean ssl;
    /***
     * 请求的url
     */
    private String url;

    public String getPreRequestId() {
        return preRequestId;
    }

    public void setPreRequestId(String preRequestId) {
        this.preRequestId = preRequestId;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }


    public String getPreRequestParameterName() {
        return preRequestParameterName;
    }

    public void setPreRequestParameterName(String preRequestParameterName) {
        this.preRequestParameterName = preRequestParameterName;
    }


    public String getCurrRequestParameterName() {
        return currRequestParameterName;
    }

    public void setCurrRequestParameterName(String currRequestParameterName) {
        this.currRequestParameterName = currRequestParameterName;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getActionPath() {
        return actionPath;
    }

    public void setActionPath(String actionPath) {
        this.actionPath = actionPath;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public int getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(int requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Map<String, Object> getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(TreeMap<String, Object> requestParameters) {
        this.requestParameters = requestParameters;
    }

    public String getRequestBodyData() {
        return requestBodyData;
    }

    public void setRequestBodyData(String requestBodyData) {
        this.requestBodyData = requestBodyData;
    }

    public String getCurrentRequestName() {
        return currentRequestName;
    }

    public void setCurrentRequestName(String currentRequestName) {
        this.currentRequestName = currentRequestName;
    }

    @Override
    public String toString() {
        return "RequestInfoBean [serverIp=" + serverIp + ", port=" + port
                + ", actionPath=" + actionPath + ", charset=" + charset
                + ", requestMethod=" + (requestMethod == Constant2.REQUEST_METHOD_GET ? "Get" : "Post") + ", requestBodyData="
                + requestBodyData + ", requestParameters=" + requestParameters
                + "]";
    }

    public String getRequestCookie() {
        return requestCookie;
    }

    public void setRequestCookie(String requestCookie) {
        this.requestCookie = requestCookie;
    }

    public String getSendCount() {
        return sendCount;
    }

    public void setSendCount(String sendCount) {
        this.sendCount = sendCount;
    }

    public boolean isUrlEncoding() {
        return isUrlEncoding;
    }

    public void setUrlEncoding(boolean isUrlEncoding) {
        this.isUrlEncoding = isUrlEncoding;
    }

    public boolean isApplyDefaultHTTP() {
        return isApplyDefaultHTTP;
    }

    public void setApplyDefaultHTTP(boolean isApplyDefaultHTTP) {
        this.isApplyDefaultHTTP = isApplyDefaultHTTP;
    }

    public boolean isRequestBodyIsJson() {
        return requestBodyIsJson;
    }

    public void setRequestBodyIsJson(boolean requestBodyIsJson) {
        this.requestBodyIsJson = requestBodyIsJson;
    }

    public boolean isHasPreRequest() {
        return isHasPreRequest;
    }

    public void setHasPreRequest(boolean isHasPreRequest) {
        this.isHasPreRequest = isHasPreRequest;
    }

    public String getJsonResult() {
        return jsonResult;
    }

    public void setJsonResult(String jsonResult) {
        this.jsonResult = jsonResult;
    }

    /***
     * 是否每次都请求前置请求
     *
     * @return
     */
    public boolean isSendPreRequestPer() {
        return sendPreRequestPer;
    }

    /***
     * 是否每次都请求前置请求
     *
     * @param sendPreRequestPer
     */
    public void setSendPreRequestPer(boolean sendPreRequestPer) {
        this.sendPreRequestPer = sendPreRequestPer;
    }

    public int getTimingSecond() {
        return timingSecond;
    }

    public void setTimingSecond(int timingSecond) {
        this.timingSecond = timingSecond;
    }

    public String getSingleRequestNote() {
        return singleRequestNote;
    }

    public void setSingleRequestNote(String singleRequestNote) {
        this.singleRequestNote = singleRequestNote;
    }

    public boolean isEncodingCheckbox() {
        return isEncodingCheckbox;
    }

    public void setEncodingCheckbox(boolean encodingCheckbox) {
        isEncodingCheckbox = encodingCheckbox;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void updateRequestBody() {
        this.setRequestBodyData(WebServletUtil.getRequestBodyFromMap(this.getRequestParameters()));
    }

    public TreeMap<String, Object> addParameter(String key, Object val) {
        if (null == this.requestParameters) {
            this.requestParameters = new TreeMap<String, Object>();
        }
        this.requestParameters.put(key, val);
        return this.requestParameters;
    }
}
