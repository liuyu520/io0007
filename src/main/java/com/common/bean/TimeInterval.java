package com.common.bean;

import com.google.common.base.MoreObjects;

/**
 * 时间区间类 时间格式yyyyMMddHHmm 201705200101
 *
 * @author eagle
 */
public class TimeInterval {
    private String startTime;
    private String endTime;
    /***
     * 单位:毫秒
     */
    private Long startTimestamp;
    /***
     * 单位:毫秒
     */
    private Long endTimestamp;

    public TimeInterval(String startTime, String endTime) {
        super();
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TimeInterval() {
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /***
     * 判断给定的时间是否在此区间内
     * @param time
     * @return
     */
    public boolean isBetween(String time) {
        if (isAnyMoment(startTime) || isAnyMoment(endTime)) {
            return true;
        }
        time = time.replace("  ", " ");//把两个空格转化为一个空格
        if (time.compareTo(startTime) > 0 && time.compareTo(endTime) < 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是随时
     *
     * @param time
     * @return
     */
    private boolean isAnyMoment(String time) {
        if (time == null || "".equals(time) || "0".equals(time)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("startTime", startTime)
                .add("endTime", endTime)
                .toString();
    }

    public Long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(Long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public Long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(Long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }
}
