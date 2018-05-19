package com.common.bean.json;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by whuanghkl on 17/5/15.
 */
public class ParseJsonInfo {
    private com.alibaba.fastjson.JSONObject jsonObject;
    /***
     * 是否有String格式的json
     */
    private boolean hasString = false;

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public boolean isHasString() {
        return hasString;
    }

    public void setHasString(boolean hasString) {
        this.hasString = hasString;
    }
}
