package com.common.bean;

import com.string.widget.util.ValueWidget;
import com.swing.table.TableUtil3;

/**
 * Created by huangweii on 2016/3/10.
 */
public class RequestSendChain extends RequestInfoBean {
    /***
     * 依赖的(前置)请求
     */
    private RequestSendChain dependentRequest;

    public RequestSendChain getDependentRequest() {
        return dependentRequest;
    }

    public void setDependentRequest(RequestSendChain dependentRequest) {
        this.dependentRequest = dependentRequest;
    }

    public ResponseResult request() {
        ResponseResult preResponseResult = null;
        if (null == dependentRequest) {
            preResponseResult = new ResponseResult(this).invoke();
        } else if (null == dependentRequest.dependentRequest) {//递归的退出 出口
            preResponseResult = new ResponseResult(dependentRequest).invoke();
        } else {
            preResponseResult = dependentRequest.request();
        }
        if (ValueWidget.isNullOrEmpty(preResponseResult)) {
            System.out.println(dependentRequest.getActionPath() + " return null");
            return null;
        }
        String preRequestParameterValue = TableUtil3.getPreRequestVal(preResponseResult, this);
        if (ValueWidget.isNullOrEmpty(preRequestParameterValue)) {//preRequestParameterValue为空,表示接口返回false
            System.out.println("preRequestParameterValue is null");
            return preResponseResult;
        }
        String key = this.getCurrRequestParameterName();
        this.addParameter(key, preRequestParameterValue);
        this.updateRequestBody();//因为新增参数,所以需要更新请求体
        ResponseResult responseResult = new ResponseResult(this).invoke();
        return responseResult;
    }


}
