package com.common.bean;

import java.util.TreeMap;

/***
 * 必须以"{{"打头,<br />
 * 为了复制"/home/house/house_endpoint/logs/house_rest.log"中请求的效率<br />
 * 见 House_Server 项目的RequestbodyFilter
 */
public class RestfulParamDto {
    private String servletPath;
    /***
     * Constant2.HTTP_REQUESTMETHOD_POST or Constant2.HTTP_REQUESTMETHOD_GET
     */
    private String method;
    private String Cookie;
    private String param;
    private TreeMap<String, Object> paramMap;

    public String getServletPath() {
        return servletPath;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCookie() {
        return Cookie;
    }

    public void setCookie(String cookie) {
        Cookie = cookie;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public TreeMap<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(TreeMap<String, Object> paramMap) {
        this.paramMap = paramMap;
    }
}
