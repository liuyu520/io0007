package com.http.bean;

import java.util.Map;

/**
 * Created by whuanghkl on 17/5/23.
 */
public class HttpRequestParameter {
    /***
     * 完整的链接地址<br />
     * https://blog.yhskyc.com/list?_dc=1495527165578&username=whuang
     */
    private String url;
    /***
     * true:https <br />
     * false:http
     */
    private boolean ssl;
    /***
     * true-sendData为空,依然以POST方式
     */
    private boolean forcePost;

    private String sendData;
    private String contentType;
    /***
     * 例如:<br />
     *  CLI_LONG_BD_ID=7f4fa441-7dd4-07bf-fe31-027a35c8baa1; sessionid=34e4a64ad33e400d63804b19496d9025
     */
    private String requestCookie;
    private Map<String, String> headers;
    private String requestCharset;
    private Integer connectTimeout;
    private Integer readTimeout;
    /***
     * 特殊场景才使用,一般情况不用关心
     */
    private String sizeHeadKey;

    public String getUrl() {
        return url;
    }

    public HttpRequestParameter setUrl(String url) {
        this.url = url;
        return this;
    }

    public boolean isSsl() {
        return ssl;
    }

    public HttpRequestParameter setSsl(boolean ssl) {
        this.ssl = ssl;
        return this;
    }

    public boolean isForcePost() {
        return forcePost;
    }

    public HttpRequestParameter setForcePost(boolean forcePost) {
        this.forcePost = forcePost;
        return this;
    }


    public String getSendData() {
        return sendData;
    }

    public HttpRequestParameter setSendData(String sendData) {
        this.sendData = sendData;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public HttpRequestParameter setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public String getRequestCookie() {
        return requestCookie;
    }

    public HttpRequestParameter setRequestCookie(String requestCookie) {
        this.requestCookie = requestCookie;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public HttpRequestParameter setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public String getRequestCharset() {
        return requestCharset;
    }

    public HttpRequestParameter setRequestCharset(String requestCharset) {
        this.requestCharset = requestCharset;
        return this;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public HttpRequestParameter setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public Integer getReadTimeout() {
        return readTimeout;
    }

    public HttpRequestParameter setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public String getSizeHeadKey() {
        return sizeHeadKey;
    }

    public HttpRequestParameter setSizeHeadKey(String sizeHeadKey) {
        this.sizeHeadKey = sizeHeadKey;
        return this;
    }
}
