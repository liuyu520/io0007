package com.http.bean;

public class HttpRequestBean
{
    private String ip;
    private String realm;
    private int port;
    private String url;
    private String params;
    private boolean isPOST;
    public String getIp()
    {
        return ip;
    }
    public void setIp(String ip)
    {
        this.ip = ip;
    }
    public String getRealm()
    {
        return realm;
    }
    public void setRealm(String realm)
    {
        this.realm = realm;
    }
    public int getPort()
    {
        return port;
    }
    public void setPort(int port)
    {
        this.port = port;
    }
    public String getUrl()
    {
        return url;
    }
    public void setUrl(String url)
    {
        this.url = url;
    }
    public String getParams()
    {
        return params;
    }
    public void setParams(String params)
    {
        this.params = params;
    }
    public boolean isPOST()
    {
        return isPOST;
    }
    public void setPOST(boolean isPOST)
    {
        this.isPOST = isPOST;
    }
    
    

}
