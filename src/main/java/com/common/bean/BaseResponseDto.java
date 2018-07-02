package com.common.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.io.hw.json.HWJacksonUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by David on 15/2/12.
 */
@JsonIgnoreProperties(value = {"codeOfError", "msgOfError", "logger", "serialVersionUID"})
public class BaseResponseDto implements Serializable {

    private static final long serialVersionUID = 2409850391232289737L;
    public Boolean result = false;
    public String param;
    /***
     * 错误码
     */
    private String errorCode;
    /***
     * 错误提示信息
     */
    private String errorMessage;
    private Object value;

    public ErrorObject error;
    private Object extraInfo;
    /***
     * cookie<br />
     * 只有登录接口和注册接口  才会返回
     */
    private String conventionk;
    /***
     * 数据的版本
     */
    private Long version;

    public BaseResponseDto() {

    }

    public BaseResponseDto(boolean result) {
        this.result = result;
    }

    public BaseResponseDto put(String key, Object value) {
        Map map = new HashMap();
        map.put(key, value);
        return setValue(map);
    }

    public static String jsonValue(Object value) {
        return new BaseResponseDto(true).setValue(value).toJson();
    }
    public BaseResponseDto(String errorCode, String errorMsg) {
        this.result = false;
        this.errorCode = errorCode;
        if (this.result == false) {
            this.errorMessage = errorMsg;
        }
        setErrorObjectContent();
    }

    public void setErrorObjectContent() {
        if (null == this.error) {
            this.error = new ErrorObject();
        }
        this.error.code = this.errorCode;
        this.error.msg = this.errorMessage;
    }

    public BaseResponseDto setHint(String hint) {
        if (null == this.error) {
            this.error = new ErrorObject();
        }
        this.error.hint = hint;
        return this;
    }
    public BaseResponseDto(boolean result, String errorCode, String param) {
        this.result = result;
        this.param = param;
        this.errorCode = errorCode;
        setErrorObjectContent();
        this.error.hint = param;
    }

    public String getCodeOfError() {
        if (error == null) {
            return errorCode;
        }
        return error.code;
    }

    public String getMsgOfError() {
        if (error == null) {
            return errorMessage;
        }
        return error.msg;
    }

    public String toJson() {
        if (this.result == null) {//modified by huangweii @2015-08-17
            System.out.println("this.result is null");
        } else {
            if (this.result != false) {
                this.errorMessage = null;
            }

        }
        return HWJacksonUtils.getJsonP(this);
    }

    public String getErrorCode() {
        if (error != null) {
            return error.code;
        } else {
            return errorCode;//modified by huangweii @2016-03-23 20:40:17
        }
    }

    public Object getValue() {
        return value;
    }

    public BaseResponseDto setValue(Object value) {
        this.value = value;
        return this;
    }
//    public String getErrorMsg() {
//        return error.toString();
//    }

    public BaseResponseDto setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        if (null == errorCode) {
            return this;
        }
        if (null == this.error) {
            this.error = new ErrorObject();
        }
        this.error.setCode(errorCode);
        return this;
    }

    public BaseResponseDto setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        if (null == this.error) {
            this.error = new ErrorObject();
        }
        this.error.setMsg(errorMessage);
        return this;
    }

    public Object getExtraInfo() {
        return extraInfo;
    }

    public BaseResponseDto setExtraInfo(Object extraInfo) {
        this.extraInfo = extraInfo;
        return this;
    }

    public String getConventionk() {
        return conventionk;
    }

    public void setConventionk(String conventionk) {
        this.conventionk = conventionk;
    }

    public Long getVersion() {
        return version;
    }

    public BaseResponseDto setVersion(Long version) {
        this.version = version;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public BaseResponseDto setResult(Boolean result) {
        this.result = result;
        return this;
    }
}

class ErrorObject implements Serializable {
    public String code;
    public String hint;
    public String msg;

    @Override
    public String toString() {
        return "ErrorObject [code=" + code + ", hint=" + hint + ", msg=" + msg
                + "]";
    }

    public String getCode() {
        return code;
    }

    public ErrorObject setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ErrorObject setMsg(String msg) {
        this.msg = msg;
        return this;
    }

}
