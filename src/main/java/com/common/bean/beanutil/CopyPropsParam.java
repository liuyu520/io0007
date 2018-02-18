package com.common.bean.beanutil;

public class CopyPropsParam {
    private Object source;
    private Object target;

    public CopyPropsParam() {
    }

    public CopyPropsParam(Object source, Object target) {
        this.source = source;
        this.target = target;
    }

    public CopyPropsParam(Object source) {
        this.source = source;
    }

    public static CopyPropsParam getInstance(Object source, Object target) {
        return new CopyPropsParam(source, target);
    }

    public static CopyPropsParam getInstance(Object source) {
        return new CopyPropsParam(source);
    }

    /***
     * 去掉hibernate字段,例如:<br />
     * "handler", "hibernateLazyInitializer", "logger"
     */
    private Boolean excludHibernate;
    /***
     * 不包含值为null的字段,<br />
     * 如果source 中字段的值为null,则不复制该字段
     */
    private Boolean excludeNullProps;

    public Object getSource() {
        return source;
    }

    public CopyPropsParam setSource(Object source) {
        this.source = source;
        return this;
    }

    public Object getTarget() {
        return target;
    }

    public CopyPropsParam setTarget(Object target) {
        this.target = target;
        return this;
    }

    public Boolean getExcludHibernate() {
        if (null == this.excludHibernate) {
            return false;
        }
        return excludHibernate;
    }

    public CopyPropsParam setExcludHibernate(Boolean excludHibernate) {
        this.excludHibernate = excludHibernate;
        return this;
    }

    public Boolean getExcludeNullProps() {
        if (null == this.excludeNullProps) {
            return false;
        }
        return excludeNullProps;
    }

    public CopyPropsParam setExcludeNullProps(Boolean excludeNullProps) {
        this.excludeNullProps = excludeNullProps;
        return this;
    }
}
