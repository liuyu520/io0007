package com.common.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by whuanghkl on 17/5/11.
 */
public class CookieSetInfo {
    private String cookies;
    private Set<String> cookieSet = new HashSet<>();

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public Set<String> getCookieSet() {
        return cookieSet;
    }

    public void setCookieSet(Set<String> cookieSet) {
        this.cookieSet = cookieSet;
    }

    public void add(String cookie) {
        this.cookieSet.add(cookie);
    }

    public boolean hasCookie(String cookie) {
        return this.cookieSet.contains(cookie);
    }
}
