package com.common.bean;

import com.common.dict.Constant2;
import com.common.util.SystemHWUtil;
import com.common.util.WebServletUtil;
import com.string.widget.util.RegexUtil;
import com.string.widget.util.ValueWidget;
import org.apache.commons.collections.map.ListOrderedMap;

import java.io.Serializable;
import java.util.*;

/***
 * 一次具体的请求信息
 *
 * @author huangweii
 *         2015年6月28日
 */
public class RequestInfoBean implements Serializable {
    private static final long serialVersionUID = 2426877461354119516L;
    /***
     * 服务器的ip
     */
    private String serverIp;
    /***
     * 端口号,比如80,8080
     */
    private String port;
    /***
     * 项目名称,例如"tv_mobile"
     */
    private String actionPath;
    /***
     * 请求参数的编码
     */
    private String charset;
    /***
     * 请求的Content Type
     */
    private String customRequestContentType;
    /***
     * POST:2<br>GET:0
     */
    private int requestMethod;
    /***
     * "GET","POST","PUT"
     */
    private String requestMethodDisplayName;
    /***
     * 请求体
     */
    private String requestBodyData;
    /***
     * 前置请求的参数名
     */
    private String preRequestParameterName;
    /***
     * 请求ID
     */
    private String requestID;
    /***
     * 当期的请求名称
     * <br>用于搜索
     */
    private String currentRequestName;

    /***
     * 本请求的参数名称
     */
    private String currRequestParameterName;
    private String preRequestId;
    /***
     * 请求的cookie
     */
    private String requestCookie;
    /***
     * 连续发送请求的次数
     */
    private String sendCount;
    /***
     * 是否对参数进行url编码
     */
    private boolean isUrlEncoding;
    /***
     * 是否应用HTTP请求默认值
     */
    private boolean isApplyDefaultHTTP;
    /***
     * 请求体是否是json字符串(不是标准的query string)
     */
    private boolean requestBodyIsJson;
    /***
     * 是否有前置请求("前置请求id"复选框)
     */
    private boolean isHasPreRequest;
    /***
     * 应答体的json格式
     */
    private String jsonResult;
    /***
     * 是否每次都请求前置请求
     */
    private boolean sendPreRequestPer;
    /***
     * 默认的请求要素(可以把"默认的"去掉)
     */
    private TreeMap<String, Object> requestParameters;
    /***
     * 定时发送的倒计时,单位:秒
     */
    private int timingSecond;
    /***
     * 单个请求的备忘
     */
    private String singleRequestNote;
    private boolean isEncodingCheckbox;
    /***
     * http or https
     */
    private boolean ssl;
    /***
     * 请求的url
     */
    private String url;
    /***
     * 别名<br >用于搜索
     */
    private String alias;
    /***
     * 请求参数模板
     */
    private List<RequestParameterTemplate> requestParameterTemplates;
    /***
     * 请求参数历史记录,key是参数名称,比如username,password等
     */
    private Map<String, List> parametersHistory;
    /***
     * "前置请求id"下拉框中的数据 <br>
     * 注意:不是从配置文件中读取的,而是计算出来的.
     */
    private Set<String> preRequestIds;
    /***
     * 冗余字段,不序列化,只是为了传递到com/common/bean/ResponseResult.java 中
     */
    private List<ParameterIncludeBean> parameters;
    /***
     * 是否智能地判断 <br />
     * 注意:不是强制进行url编码,
     * <br /> http请求参数中含有& 或者空格,才URL编码
     * <br /> see /Users/whuanghkl/work/mygit/io0007_new/src/main/java/com/common/util/WebServletUtil.java 中的方法isShouldURLEncode
     *
     */
    private boolean isAutoUrlEncoding;
    /***
     * 是否立即发送请求<br />
     * 存储到什么地方呢?<br />
     * RequestPanel的成员变量OriginalRequestInfoBean 中
     */
    private boolean isSendRightNow;
    /***
     * 请求头
     */
    private ListOrderedMap headerMap;
    /***
     * 是否是请求体参数,<br />而不是header
     */
    private boolean isHttpRequestParameter = true;
    /***
     * "http请求结果","格式化的json","备忘"标签页的selectedIndex
     */
    private int responseResultTabIndex = 0;
    /***
     * 是否复制请求结果中的字段<br />
     * 通过解析json得到
     */
    private boolean isCopyFieldInResponse;
    /***
     * 与<param>isCopyFieldInResponse</param>配合使用<br />
     * 要复制的属性名,例如复制<br />
     * {
     "auth_code": "TzIcph"
     }
     中的auth_code 的值
     */
    private String copyFieldName;
    private RequestInRangeInfo requestInRangeInfo;

    public String getPreRequestId() {
        return preRequestId;
    }

    public void setPreRequestId(String preRequestId) {
        this.preRequestId = preRequestId;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }


    public String getPreRequestParameterName() {
        return preRequestParameterName;
    }

    public void setPreRequestParameterName(String preRequestParameterName) {
        this.preRequestParameterName = preRequestParameterName;
    }


    public String getCurrRequestParameterName() {
        return currRequestParameterName;
    }

    public void setCurrRequestParameterName(String currRequestParameterName) {
        this.currRequestParameterName = currRequestParameterName;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getActionPath() {
        return actionPath;
    }

    public void setActionPath(String actionPath) {
        this.actionPath = actionPath;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public int getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(int requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Map<String, Object> getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(TreeMap<String, Object> requestParameters) {
        this.requestParameters = requestParameters;
    }

    public String getRequestBodyData() {
        return requestBodyData;
    }

    public void setRequestBodyData(String requestBodyData) {
        this.requestBodyData = requestBodyData;
    }

    public String getCurrentRequestName() {
        return currentRequestName;
    }

    public void setCurrentRequestName(String currentRequestName) {
        this.currentRequestName = currentRequestName;
    }

    @Override
    public String toString() {
        return "RequestInfoBean [serverIp=" + serverIp + ", port=" + port
                + ", actionPath=" + actionPath + ", charset=" + charset
                + ", requestMethod=" + (requestMethod == Constant2.REQUEST_METHOD_GET ? "Get" : "Post") + ", requestBodyData="
                + requestBodyData + ", requestParameters=" + requestParameters
                + "]";
    }

    public String getRequestCookie() {
        return requestCookie;
    }

    public void setRequestCookie(String requestCookie) {
        this.requestCookie = requestCookie;
    }

    public String getSendCount() {
        return sendCount;
    }

    public void setSendCount(String sendCount) {
        this.sendCount = sendCount;
    }

    public boolean isUrlEncoding() {
        return isUrlEncoding;
    }

    public void setUrlEncoding(boolean isUrlEncoding) {
        this.isUrlEncoding = isUrlEncoding;
    }

    public boolean isApplyDefaultHTTP() {
        return isApplyDefaultHTTP;
    }

    public void setApplyDefaultHTTP(boolean isApplyDefaultHTTP) {
        this.isApplyDefaultHTTP = isApplyDefaultHTTP;
    }

    public boolean isRequestBodyIsJson() {
        return requestBodyIsJson;
    }

    public void setRequestBodyIsJson(boolean requestBodyIsJson) {
        this.requestBodyIsJson = requestBodyIsJson;
    }

    public boolean isHasPreRequest() {
        return isHasPreRequest;
    }

    public void setHasPreRequest(boolean isHasPreRequest) {
        this.isHasPreRequest = isHasPreRequest;
    }

    public String getJsonResult() {
        return jsonResult;
    }

    public void setJsonResult(String jsonResult) {
        this.jsonResult = jsonResult;
    }

    /***
     * 是否每次都请求前置请求
     *
     * @return
     */
    public boolean isSendPreRequestPer() {
        return sendPreRequestPer;
    }

    /***
     * 是否每次都请求前置请求
     *
     * @param sendPreRequestPer
     */
    public void setSendPreRequestPer(boolean sendPreRequestPer) {
        this.sendPreRequestPer = sendPreRequestPer;
    }

    public int getTimingSecond() {
        return timingSecond;
    }

    public void setTimingSecond(int timingSecond) {
        this.timingSecond = timingSecond;
    }

    public String getSingleRequestNote() {
        return singleRequestNote;
    }

    public void setSingleRequestNote(String singleRequestNote) {
        this.singleRequestNote = singleRequestNote;
    }

    public boolean isEncodingCheckbox() {
        return isEncodingCheckbox;
    }

    public void setEncodingCheckbox(boolean encodingCheckbox) {
        isEncodingCheckbox = encodingCheckbox;
    }

    public boolean isSsl() {
        return ssl;
    }

    /***
     * 表示是https
     * @param ssl
     */
    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void updateRequestBody() {
        this.setRequestBodyData(WebServletUtil.getRequestBodyFromMap(this.getRequestParameters()));
    }


    public String getCustomRequestContentType() {
        return customRequestContentType;
    }

    public void setCustomRequestContentType(String customRequestContentType) {
        this.customRequestContentType = customRequestContentType;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public List<RequestParameterTemplate> getRequestParameterTemplates() {
        return requestParameterTemplates;
    }

    public void setRequestParameterTemplates(List<RequestParameterTemplate> requestParameterTemplates) {
        this.requestParameterTemplates = requestParameterTemplates;
    }

    public Map<String, List> getParametersHistory() {
        return parametersHistory;
    }

    public void setParametersHistory(Map<String, List> parametersHistory) {
        this.parametersHistory = parametersHistory;
    }

    public List getParametersHistory(String parameterName) {
        if (ValueWidget.isNullOrEmpty(this.parametersHistory)) {
            return null;
        } else {
            return this.parametersHistory.get(parameterName);
        }
    }

    /***
     * 添加参数的一条历史记录
     * @param parameterName
     * @param parameterVal
     */
    public void addParameterHistory(String parameterName, String parameterVal) {
        if (null == this.parametersHistory) {
            this.parametersHistory = new HashMap<String, List>();
            addNewParameterHistory(parameterName, parameterVal, parametersHistory);
        } else {
            if (this.parametersHistory.containsKey(parameterName)) {
                getParametersHistory(parameterName).add(parameterVal);
            } else {
                addNewParameterHistory(parameterName, parameterVal, parametersHistory);
            }
        }

    }

    public static void addNewParameterHistory(String parameterName, String parameterVal, Map<String, List> parametersHistory) {
        List valList = new ArrayList();
        valList.add(parameterVal);
        parametersHistory.put(parameterName, valList);
    }

    public TreeMap<String, Object> addParameter(String key, Object val) {
        if (null == this.requestParameters) {
            this.requestParameters = new TreeMap<String, Object>();
        }
        this.requestParameters.put(key, val);
        return this.requestParameters;
    }

    public Set<String> getPreRequestIds() {
        return preRequestIds;
    }

    public void setPreRequestIds(Set<String> preRequestIds) {
        this.preRequestIds = preRequestIds;
    }

    public List<ParameterIncludeBean> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterIncludeBean> parameters) {
        this.parameters = parameters;
    }

    /***
     * 是否智能地判断 <br />
     * 注意:不是强制进行url编码,
     * <br /> http请求参数中含有& 或者空格,才URL编码
     * <br /> see /Users/whuanghkl/work/mygit/io0007_new/src/main/java/com/common/util/WebServletUtil.java 中的方法isShouldURLEncode
     *
     */
    public boolean isAutoUrlEncoding() {
        return isAutoUrlEncoding;
    }

    /***
     * 是否智能地判断 <br />
     * 注意:不是强制进行url编码,
     * <br /> http请求参数中含有& 或者空格,才URL编码
     * <br /> see /Users/whuanghkl/work/mygit/io0007_new/src/main/java/com/common/util/WebServletUtil.java 中的方法isShouldURLEncode
     *
     */
    public void setAutoUrlEncoding(boolean autoUrlEncoding) {
        isAutoUrlEncoding = autoUrlEncoding;
    }

    /***
     * 是否立即发送请求<br />
     * 存储到什么地方呢?<br />
     * RequestPanel的成员变量OriginalRequestInfoBean 中
     */
    public boolean isSendRightNow() {
        return isSendRightNow;
    }

    /***
     * 是否立即发送请求<br />
     * 存储到什么地方呢?<br />
     * RequestPanel的成员变量OriginalRequestInfoBean 中
     */
    public void setSendRightNow(boolean sendRightNow) {
        isSendRightNow = sendRightNow;
    }

    public ListOrderedMap getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(ListOrderedMap headerMap) {
        this.headerMap = headerMap;
    }

    public String getRequestMethodDisplayName() {
        return requestMethodDisplayName;
    }

    public void setRequestMethodDisplayName(String requestMethodDisplayName) {
        this.requestMethodDisplayName = requestMethodDisplayName;
    }

    public boolean isHttpRequestParameter() {
        return isHttpRequestParameter;
    }

    public void setHttpRequestParameter(boolean httpRequestParameter) {
        isHttpRequestParameter = httpRequestParameter;
    }

    /***
     * 获取请求的content type
     * @return
     */
    public String getReqContentType() {
        RequestInfoBean requestInfoBean = this;
        String contentType = null;
        if (requestInfoBean.isRequestBodyIsJson()) {
            contentType = SystemHWUtil.CONTENTTYPE_JSON;
        } else {
            contentType = SystemHWUtil.CONTENTTYPE_X_WWW_FORM_URLENCODED;
        }

        if (requestInfoBean.isEncodingCheckbox() && (!RegexUtil.contain2(contentType, "charset"))) {//是否选中了复选框
            contentType = contentType + (";charset=" + requestInfoBean.getCharset());//request.getCharacterEncoding() 获取的就是该编码
        }
        return contentType;
    }

    public int getResponseResultTabIndex() {
        return responseResultTabIndex;
    }

    public void setResponseResultTabIndex(int responseResultTabIndex) {
        this.responseResultTabIndex = responseResultTabIndex;
    }

    public boolean isCopyFieldInResponse() {
        return isCopyFieldInResponse;
    }

    public void setCopyFieldInResponse(boolean copyFieldInResponse) {
        isCopyFieldInResponse = copyFieldInResponse;
    }

    public String getCopyFieldName() {
        return copyFieldName;
    }

    public void setCopyFieldName(String copyFieldName) {
        this.copyFieldName = copyFieldName;
    }

    public RequestInRangeInfo getRequestInRangeInfo() {
        if (null == requestInRangeInfo) {
            return new RequestInRangeInfo();
        }
        return requestInRangeInfo;
    }

    public void setRequestInRangeInfo(RequestInRangeInfo requestInRangeInfo) {
        this.requestInRangeInfo = requestInRangeInfo;
    }
}
