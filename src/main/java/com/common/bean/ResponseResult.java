package com.common.bean;

import com.common.dict.Constant2;
import com.common.util.RequestUtil;
import com.common.util.SystemHWUtil;
import com.http.bean.HttpRequestParameter;
import com.http.bean.HttpResponseResult;
import com.http.util.HttpSocketUtil;
import com.io.hw.json.JSONHWUtil;
import com.string.widget.util.RegexUtil;
import com.string.widget.util.ValueWidget;
import com.swing.dialog.toast.ToastMessage;
import com.swing.messagebox.GUIUtil23;
import com.swing.table.TableUtil3;
import org.apache.log4j.Logger;

import java.awt.*;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * Created by huangweii on 2016/3/10.
 */
public class ResponseResult {
    protected static Logger logger = Logger.getLogger(ResponseResult.class);
    private boolean myResult;
    private RequestInfoBean requestInfoBean;
    private String requestCharset;
    private HttpResponseResult resultArr;
    private int resCode;
    private String responseJsonResult;
    private Map<String, String> responseJsonMap;
    /*private JTextArea respTextArea_9;
    private AutoTestPanel autoTestPanel;
    private MemoAssistPopupTextArea resultTextPane;*/
    private String requestContentType = null;
    private Map<String, java.util.List<String>> responseHeaderFields;
    private HttpURLConnection huc;
    /***
     * 接口调用时间<br>
     * 单位:毫秒
     */
    private long delta;

    public ResponseResult(RequestInfoBean requestInfoBean/*, JTextArea respTextArea_9, AutoTestPanel autoTestPanel, MemoAssistPopupTextArea resultTextPane*/) {
        this.requestInfoBean = requestInfoBean;
        this.requestCharset = requestInfoBean.getCharset();
           /* this.respTextArea_9 = respTextArea_9;
            this.autoTestPanel = autoTestPanel;
            this.resultTextPane = resultTextPane;*/
    }

    private static String getRequestMethod(RequestInfoBean requestInfoBean) {
        return (String) SystemHWUtil.reverseMap(Constant2.REQUEST_METHOD_MAP).get(requestInfoBean.getRequestMethod());
    }

    private static String getPort(RequestInfoBean requestInfoBean) {
        String port = requestInfoBean.getPort();
        if ("80".equals(port)) {//因为80是默认端口
            port = SystemHWUtil.EMPTY;
        }
        if (!ValueWidget.isNullOrEmpty(port)) {//判断是否输入了端口号
            port = ":" + port;
        }
        return port;
    }

    public boolean is() {
        return myResult;
    }

    public HttpResponseResult getResultArr() {
        return resultArr;
    }

    public void setResultArr(HttpResponseResult resultArr) {
        this.resultArr = resultArr;
    }

    public int getResCode() {
        return resCode;
    }

    public void setResCode(int resCode) {
        this.resCode = resCode;
    }

    public ResponseResult invoke() {
        String servletAction = requestInfoBean.getActionPath();//接口路径
        if (ValueWidget.isNullOrEmpty(servletAction)) {
            String errorMessage = "servletAction is null";
            logger.error(errorMessage);
            System.out.println(errorMessage);
            return null;
        }
        if (!servletAction.startsWith("/")) {//兼容"接口"文本框前面没有斜杠
            servletAction = "/" + servletAction;
        }
        String url = HttpSocketUtil.getUrlPrefix(requestInfoBean);
        url = url + servletAction;
        String postData = null;


        boolean forcePost = false;
        if (!requestInfoBean.isRequestBodyIsJson() && requestInfoBean.getRequestMethod() == Constant2.REQUEST_METHOD_GET && !ValueWidget.isNullOrEmpty(requestInfoBean.getRequestBodyData())) {//GET请求
            String getQueryString = null;
            if (null == requestInfoBean.getParameters()) {
                getQueryString = ValueWidget.getRequestBodyFromMap(requestInfoBean.getRequestParameters(), true);
            } else {
                StringBuffer sbuffer = TableUtil3.getRequestBodyFromList(requestInfoBean.getParameters(), requestInfoBean.isUrlEncoding(), requestCharset, requestInfoBean.isAutoUrlEncoding());
                getQueryString = sbuffer.toString();
            }
            url += ("?" + getQueryString);
        } else {//POST请求
            postData = requestInfoBean.getRequestBodyData();
            //注意:如果是POST请求,则不需要url编码
            //自定义的content type的优先级高
            if (ValueWidget.isNullOrEmpty(requestInfoBean.getCustomRequestContentType())) {
                requestContentType = requestInfoBean.getReqContentType();
            } else {
                requestContentType = requestInfoBean.getCustomRequestContentType();
            }

            System.out.println("request contentType:" + requestContentType);
            forcePost = true;
        }
        requestInfoBean.setUrl(url);

        HttpSocketUtil.setDetail(true);
        //获取请求方法,例如 GET,POST,PUT,DELETE
        if (requestInfoBean.isRequestBodyIsJson()) {
            forcePost = true;
        }
        byte[] resultJsonBytes = null;
        resultArr = null;
        resCode = -1;
        try {
            //        	Map<String, String> headers =new HashMap<String, String>();
            //        	headers.put("Accept-Encoding", requestInfoBean.getCharset());
            //SystemHWUtil.CONTENTTYPE_JSON+";charset="+requestInfoBean.getCharset()
            //request result(byte[]) ;sessionId;contentType;response code
            long start = System.currentTimeMillis();//接口调用的开始时刻
            //真正发送请求
            HttpRequestParameter httpRequestParameter = new HttpRequestParameter();
            httpRequestParameter.setUrl(url)
                    .setSsl(requestInfoBean.isSsl())
                    .setForcePost(forcePost)
                    .setSendData(postData)
                    .setContentType(requestContentType)
                    .setRequestCookie(requestInfoBean.getRequestCookie())
                    .setHeaders(requestInfoBean.getHeaderMap())
                    .setRequestCharset(requestCharset)
                    .setConnectTimeout(85000)
                    .setReadTimeout(85000);
            /*resultArr = HttpSocketUtil.httpRequest(url, requestInfoBean.isSsl(),
                    forcePost, requestMethod, postData*//*没有进行url编码*//*, requestContentType,
                    requestInfoBean.getRequestCookie(), requestInfoBean.getHeaderMap(), false*//*isWrite2file *//*, null, requestCharset*//*newEncoding*//*,
                    85000, 85000);*/
            resultArr = HttpSocketUtil.httpRequest(httpRequestParameter);
            long end = System.currentTimeMillis();
            setDelta(end - start);//接口调用时间
            Integer respCode = resultArr.getResponseCode();
            String responseContentType = resultArr.getContentType();
            System.out.println("response Content-Type:" + responseContentType);
            resCode = respCode.intValue();

            resultJsonBytes = resultArr.getResponse();
            if (ValueWidget.isNullOrEmpty(resultJsonBytes)) {
                myResult = true;
                return this;
            }
            String responseCharset = null;
            //response:
            //Content-Type : text/html;charset=GBK
            if (!ValueWidget.isNullOrEmpty(responseContentType) && RegexUtil.contain2(responseContentType, "charset=GBK")) {
                responseCharset = SystemHWUtil.CHARSET_GBK;
            } else {
                responseCharset = SystemHWUtil.CHARSET_UTF;
            }
            this.huc = resultArr.getHttpURLConnection();
            this.responseHeaderFields = huc.getHeaderFields();
//            String resultJson = new String(resultJsonBytes,
//                    SystemHWUtil.CHARSET_GBK);
//            System.out.println("resultJson(GBK):" + resultJson);
            this.responseJsonResult = new String(resultJsonBytes, responseCharset);
            responseJsonMap = JSONHWUtil.getMap(this.responseJsonResult);
        } catch (Exception e1) {
            e1.printStackTrace();
                /*if(!ValueWidget.isNullOrEmpty(runButton)){
                    runButton.setEnabled(true);
                }*/
            GUIUtil23.errorDialog(e1);
            if (requestInfoBean.isSsl()) {//让错误提示更加人性化,更精确
                ToastMessage.toast("请确认是否支持httpS", 3000, Color.RED);
            }
        }
        myResult = false;
        return this;
    }

    public String getReqContentType() {
        String contentType = null;
        if (requestInfoBean.isRequestBodyIsJson()) {
            contentType = SystemHWUtil.CONTENTTYPE_JSON;
        } else {
            contentType = SystemHWUtil.CONTENTTYPE_X_WWW_FORM_URLENCODED;
        }

        if (requestInfoBean.isEncodingCheckbox()) {//是否选中了复选框
            contentType = contentType + (";charset=" + requestCharset);//request.getCharacterEncoding() 获取的就是该编码
        }
        return contentType;
    }

    public String getResponseJsonResult() {
        return responseJsonResult;
    }

    public void setResponseJsonResult(String responseJsonResult) {
        this.responseJsonResult = responseJsonResult;
    }

    public String getRequestCharset() {
        return requestCharset;
    }

    public void setRequestCharset(String requestCharset) {
        this.requestCharset = requestCharset;
    }


    public RequestInfoBean getRequestInfoBean() {
        return requestInfoBean;
    }

    public void setRequestInfoBean(RequestInfoBean requestInfoBean) {
        this.requestInfoBean = requestInfoBean;
    }

    public String getRequestContentType() {
        return requestContentType;
    }

    public void setRequestContentType(String requestContentType) {
        this.requestContentType = requestContentType;
    }

    public Map<String, String> getResponseJsonMap() {
        return responseJsonMap;
    }

    public void setResponseJsonMap(Map<String, String> responseJsonMap) {
        this.responseJsonMap = responseJsonMap;
    }

    public long getDelta() {
        return delta;
    }

    public void setDelta(long delta) {
        this.delta = delta;
    }

    public String getHeaderFieldVal(String key) {
        if (null == this.huc) {
            return null;
        }
        CookieSetInfo cookieSetInfo = RequestUtil.buildCookieSetInfo(this.responseHeaderFields, key);
        return cookieSetInfo.getCookies();
    }


}
