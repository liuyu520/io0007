package com.common.bean;

import com.io.hw.json.HWJacksonUtils;

import java.io.Serializable;

/**
 * Created by David on 15/2/12.
 */
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

    public BaseResponseDto() {

    }

    public BaseResponseDto(boolean result) {
        this.result = result;
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
        return this;
    }

    public BaseResponseDto setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
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
