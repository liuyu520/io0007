package com.common.bean;

/**
 * 时间区间类 时间格式yyyyMMddHHmm 201705200101
 *
 * @author eagle
 */
public class TimeInterval {
    private String startTime;
    private String endTime;

    public TimeInterval(String startTime, String endTime) {
        super();
        this.startTime = startTime;
        this.endTime = endTime;
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
}
