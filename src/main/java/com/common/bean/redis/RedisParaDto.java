package com.common.bean.redis;


import com.string.widget.util.ValueWidget;

public class RedisParaDto {
    private String id;
    private String key;
    private String value;

    public static RedisParaDto getInstance(String id) {
        RedisParaDto redisParaDto = new RedisParaDto();
        redisParaDto.setId(id);
        return redisParaDto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public boolean isThree() {
        return (!ValueWidget.isNullOrEmpty(id)) && (!ValueWidget.isNullOrEmpty(key));
    }
}
