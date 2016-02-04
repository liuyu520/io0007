package com.swing.table;

import com.string.widget.util.ValueWidget;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/***
 * @author huangweii
 *         2015年7月16日
 */
public class ParameterIncludeBean {
    private String key;
    private String value;
    /***
     * 是否包含
     */
    private boolean include;
    /***
     * 是否忽略
     */
    private boolean ignore;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /***
     * 要保证key不为空
     *
     * @return
     */
    public boolean isInclude() {
        return ((!ValueWidget.isNullOrEmpty(key)) && include);
    }

    public void setInclude(boolean include) {
        this.include = include;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    @Override
    public String toString() {
        return this.key + "=" + this.value + "; " + (include ? "包含" : "忽略");
    }

    public String getQueryString(boolean isUrlEncoding, String requestCharset) {
        if (isUrlEncoding
                && !ValueWidget.isNullOrEmpty(this.value)
                && !ValueWidget.isNullOrEmpty(requestCharset)) {
            try {
                this.value = URLEncoder.encode(this.value, requestCharset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return this.key + "=" + this.value;
    }
}
