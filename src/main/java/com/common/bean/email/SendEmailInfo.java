package com.common.bean.email;

import com.string.widget.util.ValueWidget;

public class SendEmailInfo {
    private String emailHost;
    private Integer port;
    private String from;
    private String username;
    private String password;
    /***
     * STARTTLS on port 25
     SSL on port 465<br />
     设置参数时使用"sSL"
     */
    private boolean isSSL;
    /***
     * STARTTLS
     */
    private Boolean startTLSRequired;

    public String getEmailHost() {
        return emailHost;
    }

    public SendEmailInfo setEmailHost(String emailHost) {
        this.emailHost = emailHost;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public SendEmailInfo setPort(Integer port) {
        this.port = port;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public SendEmailInfo setFrom(String from) {
        this.from = from;
        if (ValueWidget.isNullOrEmpty(this.getUsername())) {
            this.setUsername(from);
        }
        return this;
    }

    public String getUsername() {
        return username;
    }

    public SendEmailInfo setUsername(String username) {
        this.username = username;
        if (ValueWidget.isNullOrEmpty(this.getFrom())) {
            this.setFrom(username);
        }
        return this;
    }

    public String getPassword() {
        return password;
    }

    public SendEmailInfo setPassword(String password) {
        this.password = password;
        return this;
    }

    /***
     * 设置参数时使用"sSL"
     * @return
     */
    public boolean isSSL() {
        return isSSL;
    }

    /***
     * 设置参数时使用"sSL"
     * @param SSL
     */
    public void setSSL(boolean SSL) {
        isSSL = SSL;
    }

    public Boolean getStartTLSRequired() {
        return startTLSRequired;
    }

    public SendEmailInfo setStartTLSRequired(Boolean startTLSRequired) {
        this.startTLSRequired = startTLSRequired;
        return this;
    }
}
