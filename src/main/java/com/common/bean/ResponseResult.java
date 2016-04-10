package com.common.bean;

import com.common.dict.Constant2;
import com.common.util.SystemHWUtil;
import com.http.util.HttpSocketUtil;
import com.io.hw.json.JSONHWUtil;
import com.string.widget.util.ValueWidget;
import com.swing.messagebox.GUIUtil23;

import java.util.Map;

/**
 * Created by huangweii on 2016/3/10.
 */
public class ResponseResult {
    private boolean myResult;
    private RequestInfoBean requestInfoBean;
    private String requestCharset;
    private Object[] resultArr;
    private int resCode;
    private String responseJsonResult;
    private Map<String, String> responseJsonMap;
    /*private JTextArea respTextArea_9;
    private AutoTestPanel autoTestPanel;
    private MemoAssistPopupTextArea resultTextPane;*/
    private String contentType = null;

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

    public Object[] getResultArr() {
        return resultArr;
    }

    public void setResultArr(Object[] resultArr) {
        this.resultArr = resultArr;
    }

    public int getResCode() {
        return resCode;
    }

    public void setResCode(int resCode) {
        this.resCode = resCode;
    }

    public ResponseResult invoke() {
        String port = getPort(requestInfoBean);
        String servletAction = requestInfoBean.getActionPath();//接口路径
        if (!servletAction.startsWith("/")) {//兼容"接口"文本框前面没有斜杠
            servletAction = "/" + servletAction;
        }
        String url = (requestInfoBean.isSsl() ? "https://" : "http://")
                + requestInfoBean.getServerIp();
        if (!ValueWidget.isNullOrEmpty(port)) {
            url = url + port;
        }
        url = url + servletAction;
        String postData = null;


        boolean forcePost = false;
        if (requestInfoBean.getRequestMethod() == Constant2.REQUEST_METHOD_GET && !ValueWidget.isNullOrEmpty(requestInfoBean.getRequestBodyData())) {//GET请求
            url += ("?" + requestInfoBean.getRequestBodyData());
        } else {//POST请求
            postData = requestInfoBean.getRequestBodyData();
            if (requestInfoBean.isRequestBodyIsJson()) {
                contentType = SystemHWUtil.CONTENTTYPE_JSON;
            } else {
                contentType = SystemHWUtil.CONTENTTYPE_X_WWW_FORM_URLENCODED;
            }
            
            if (requestInfoBean.isEncodingCheckbox()) {//是否选中了复选框
                contentType = contentType + (";charset=" + requestCharset);//request.getCharacterEncoding() 获取的就是该编码
            }
            System.out.println("contentType:" + contentType);
            forcePost = true;
        }
        requestInfoBean.setUrl(url);

        HttpSocketUtil.setDetail(true);
        //获取请求方法,例如 GET,POST,PUT,DELETE
        String requestMethod = getRequestMethod(requestInfoBean);

        byte[] resultJsonBytes = null;
        resultArr = null;
        resCode = -1;
        try {
            //        	Map<String, String> headers =new HashMap<String, String>();
            //        	headers.put("Accept-Encoding", requestInfoBean.getCharset());
            //SystemHWUtil.CONTENTTYPE_JSON+";charset="+requestInfoBean.getCharset()
            //request result(byte[]) ;sessionId;contentType;response code

            //真正发送请求
            resultArr = HttpSocketUtil.httpRequest(url, requestInfoBean.isSsl(),
                    forcePost, requestMethod, postData, contentType,
                    requestInfoBean.getRequestCookie(), null, false/*isWrite2file */, null, requestCharset/*newEncoding*/,
                    85000, 85000);
            Integer respCode = (Integer) resultArr[3];
            resCode = respCode.intValue();


            resultJsonBytes = (byte[]) resultArr[0];
            if (ValueWidget.isNullOrEmpty(resultJsonBytes)) {
                myResult = true;
                return this;
            }
            String resultJson = new String(resultJsonBytes,
                    SystemHWUtil.CHARSET_GBK);
            System.out.println("resultJson(GBK):" + resultJson);
            this.responseJsonResult = new String(resultJsonBytes,
                    SystemHWUtil.CHARSET_UTF);
            responseJsonMap = JSONHWUtil.getMap(this.responseJsonResult);
        } catch (Exception e1) {
            e1.printStackTrace();
                /*if(!ValueWidget.isNullOrEmpty(runButton)){
                    runButton.setEnabled(true);
                }*/
            GUIUtil23.errorDialog(e1);
        }
        myResult = false;
        return this;
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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Map<String, String> getResponseJsonMap() {
        return responseJsonMap;
    }

    public void setResponseJsonMap(Map<String, String> responseJsonMap) {
        this.responseJsonMap = responseJsonMap;
    }
}
