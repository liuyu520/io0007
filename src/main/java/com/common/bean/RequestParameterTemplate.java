package com.common.bean;

import com.string.widget.util.ValueWidget;

import java.util.Map;

/**
 * Created by 黄威 on 16/10/2016.<br >
 */
public class RequestParameterTemplate {
    private String displayName;
    private Map<String, String> parameters;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getParameter(String key) {
        if (ValueWidget.isNullOrEmpty(this.parameters)) {
            return null;
        } else {
            return this.parameters.get(key);
        }
    }
}
