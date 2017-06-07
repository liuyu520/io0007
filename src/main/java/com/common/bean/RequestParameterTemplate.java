package com.common.bean;

import com.string.widget.util.ValueWidget;
import org.apache.commons.collections.map.ListOrderedMap;

import java.io.Serializable;

/**
 * Created by 黄威 on 16/10/2016.<br >
 */
public class RequestParameterTemplate implements Serializable {
    private static final long serialVersionUID = 8400239923270285633L;
    private String displayName;
    private ListOrderedMap parameters;
    /***
     * 服务器ip,例如:<br />
     * "store2.kunlunsoft.com"
     */
    private String serverIp;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ListOrderedMap getParameters() {
        return parameters;
    }

    public void setParameters(ListOrderedMap parameters) {
        this.parameters = parameters;
    }

    public String getParameter(String key) {
        if (ValueWidget.isNullOrEmpty(this.parameters)) {
            return null;
        } else {
            return (String) this.parameters.get(key);
        }
    }

    @Override
    public String toString() {
        return "RequestParameterTemplate [" + displayName + ": parameters=" + parameters + "]";
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }
}
