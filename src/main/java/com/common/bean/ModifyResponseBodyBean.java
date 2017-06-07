package com.common.bean;

import java.io.Serializable;

/**
 * Created by 黄威 on 19/01/2017.<br >
 */
public class ModifyResponseBodyBean implements Serializable {
    private String configFilePath;
    private String servletPath;

    public String getConfigFilePath() {
        return configFilePath;
    }

    public void setConfigFilePath(String configFilePath) {
        this.configFilePath = configFilePath;
    }

    public String getServletPath() {
        return servletPath;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }
}
