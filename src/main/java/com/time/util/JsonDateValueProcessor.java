package com.time.util;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.text.SimpleDateFormat;

/**
 * Created by whuanghkl on 17/6/18.<br />
 * json lib 的日期转化器
 */
public class JsonDateValueProcessor implements JsonValueProcessor {
    @Override
    public Object processArrayValue(Object o, JsonConfig jsonConfig) {
        return process(o);
    }

    @Override
    public Object processObjectValue(String s, Object o, JsonConfig jsonConfig) {
        return process(o);
    }

    private Object process(Object value) {
        if (value instanceof java.sql.Timestamp) {
            SimpleDateFormat sdf = new SimpleDateFormat(TimeHWUtil.yyyyMMddHHmmss);
            return sdf.format(value);
        }
        return value == null ? "" : value.toString();
    }
}
