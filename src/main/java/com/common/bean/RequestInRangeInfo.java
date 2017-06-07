package com.common.bean;

import java.io.Serializable;

/**
 * Created by whuanghkl on 17/5/29.
 */
public class RequestInRangeInfo implements Serializable {
    /***
     * 参数名称,例如:<br />
     * "username",or "password"
     */
    private String parameterName;
    /***
     * 1:手动设定;<br />
     * 2:从接口返回获取
     */
    private int type;
    /***
     * 取值范围,例如:<br />
     * [1,2,3]
     */
    private String range;
    private boolean activate = false;
    /***
     * 是否格式化json
     */
    private boolean formatJson = false;
    /***
     * 应答请求保存路径
     */
    private String responseSavePath;

    public String getParameterName() {
        return parameterName;
    }

    public RequestInRangeInfo setParameterName(String parameterName) {
        this.parameterName = parameterName;
        return this;
    }

    public int getType() {
        return type;
    }

    public RequestInRangeInfo setType(int type) {
        this.type = type;
        return this;
    }

    public String getRange() {
        return range;
    }

    public RequestInRangeInfo setRange(String range) {
        this.range = range;
        return this;
    }

    public boolean isActivate() {
        return activate;
    }

    public RequestInRangeInfo setActivate(boolean activate) {
        this.activate = activate;
        return this;
    }

    public String getResponseSavePath() {
        return responseSavePath;
    }

    public RequestInRangeInfo setResponseSavePath(String responseSavePath) {
        this.responseSavePath = responseSavePath;
        return this;
    }

    public boolean isFormatJson() {
        return formatJson;
    }

    public RequestInRangeInfo setFormatJson(boolean formatJson) {
        this.formatJson = formatJson;
        return this;
    }
}
