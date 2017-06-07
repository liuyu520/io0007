package com.http.bean;

import java.net.HttpURLConnection;

/**
 * Created by whuanghkl on 17/5/23.
 */
public class HttpResponseResult {
    private byte[] response;
    private String responseCookie;
    private String contentType;
    private int responseCode;
    private HttpURLConnection httpURLConnection;

    public byte[] getResponse() {
        return response;
    }

    public void setResponse(byte[] response) {
        this.response = response;
    }

    public String getResponseCookie() {
        return responseCookie;
    }

    public void setResponseCookie(String responseCookie) {
        this.responseCookie = responseCookie;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public HttpURLConnection getHttpURLConnection() {
        return httpURLConnection;
    }

    public void setHttpURLConnection(HttpURLConnection httpURLConnection) {
        this.httpURLConnection = httpURLConnection;
    }
}
