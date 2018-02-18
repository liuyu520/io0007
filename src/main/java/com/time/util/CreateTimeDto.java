package com.time.util;

import java.util.Date;

/***
 * 专门用来设置更新时间的
 */
public class CreateTimeDto {
    private String createTime;
    private Long createTimestamp;
    private String updateTime;
    private Long updateTimestamp;
    private String createDay;
    private String updateDay;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Long createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Long updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public String getCreateDay() {
        return createDay;
    }

    public void setCreateDay(String createDay) {
        this.createDay = createDay;
    }

    public String getUpdateDay() {
        return updateDay;
    }

    public void setUpdateDay(String updateDay) {
        this.updateDay = updateDay;
    }

    public CreateTimeDto init() {
        Date now = new Date();
        String dateTime2 = TimeHWUtil.formatDateTime(now);
        String date2 = TimeHWUtil.formatDate(now);
        setCreateDay(date2);
        setUpdateDay(date2);
        setCreateTime(dateTime2);
        setUpdateTime(dateTime2);
        setCreateTimestamp(now.getTime());
        setUpdateTimestamp(now.getTime());
        return this;
    }
}
