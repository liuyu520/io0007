package com.http.util;

import com.common.bean.RequestInfoBean;
import com.common.dict.Constant2;
import com.common.util.RequestUtil;
import com.common.util.SystemHWUtil;
import com.common.util.WebServletUtil;
import com.http.bean.HttpRequestBean;
import com.http.bean.HttpRequestParameter;
import com.http.bean.HttpResponseResult;
import com.io.hw.file.util.FileUtils;
import com.io.hw.json.HWJacksonUtils;
import com.string.widget.util.RandomUtils;
import com.string.widget.util.ValueWidget;
import com.string.widget.util.XSSUtil;
import com.swing.table.TableUtil3;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpSocketUtil {
	public static final String BOUNDARY = "---------------------------41184676334";
	public static final String end = "\r\n";

    protected static Logger logger = Logger.getLogger(HttpSocketUtil.class);
    private static Map<String, List<String>> responseHeaderFields;
	/***
     * DES key
     */
    public static final String key = "11111111";
    /***
     * 是否执行System out语句
     */
    private static boolean isDetail = false;

	private HttpSocketUtil() {
		throw new Error("Don't let anyone instantiate this class.");
	}

	public static String httpPOSTSocket(String url) {
		if (!url.contains(SystemHWUtil.PROCOTOL_HTTP)) {
			url = SystemHWUtil.PROCOTOL_HTTP + "://" + url;
		}
		String[] strs = url.split("//");
		String protool = strs[0];
		String requestUrl = null;
		if (protool.startsWith(SystemHWUtil.PROCOTOL_HTTP)) {
			requestUrl = strs[1];// localhost:8080/passwd007_only_hibernate/abc/lookInput.jsp
		} else {
			requestUrl = url;
		}

		Pattern pattern = Pattern.compile("/");
		String[] str2 = pattern.split(requestUrl, 3);
		String[] ip_port = str2[0].split(":");// ip:port
		String ip = ip_port[0];
		String portStr = null;
		if (ip_port.length == 1) {// ip_port not include port
			portStr = "80";
		} else {
			portStr = ip_port[1];
		}
		int port = Integer.parseInt(portStr);
		String appName = str2[1];
		String params = str2[2];
		System.out.println("requestUrl:   " + requestUrl);
		System.out.println("protool:   " + protool);
		System.out.println("portStr:   " + portStr);
		System.out.println("appName:   " + appName);
		System.out.println("params:   " + params);
		// SystemUtil.printArray(str2);

		try {
			return httpPOSTSocket(ip, ip, port, appName + "/" + params, params);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param ip
	 *            : localhost
	 * @param realm
	 *            : equal to ip
	 * @param port
	 * @param url
	 * @param params
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	private static String httpPOSTSocket(String ip, String realm, int port,
										 String url, String params) throws IOException {
		Socket socket = new Socket(ip, port);
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                socket.getOutputStream(), SystemHWUtil.CHARSET_UTF));
        if (url.startsWith("/")) {
			url = url.substring(1);
		}
		int index2 = url.indexOf("?");
		if (index2 > 0) {
			url = url.substring(0, index2);
		}
		pw.println("POST /" + url + " HTTP/1.1");
		pw.println("Host:" + realm);
		pw.println("Accept-Language: zh-cn,zh;q=0.5");
		pw.println("Accept-Encoding: gzip,deflate");
		pw.println("Accept-Charset: gb2312,utf-8;q=0.7,*;q=0.7");
		pw.println("Connection: keep-alive");
		pw.println("Cookie: ASPSESSIONIDCSAATTCD=DOMMILABJOPANJPNNAKAMCPK");
		pw.println("Content-Type: application/x-www-form-urlencoded");
		int leng = params.length();
		pw.println("Content-Length: " + leng);
		pw.println();
		pw.println(params);
		pw.println();
		pw.flush();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				socket.getInputStream(), SystemHWUtil.CHARSET_UTF));
		String content = FileUtils.getFullContent(br);
		br.close();
		pw.close();
		socket.close();
		return content;
	}

	public static String httpSocket(HttpRequestBean httpBean)
			throws IOException {
		if (httpBean.isPOST()) {
			System.out.println("POST");
			return httpPOSTSocket(httpBean);
		} else {
			System.out.println("GET...");
			return httpGETSocket(httpBean);
		}
	}

    @Deprecated
    private static String httpGETSocket(String ip, String realm, int port,
                                        String url) throws IOException {
        Socket s = new Socket(ip, port);
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                s.getOutputStream(), SystemHWUtil.CHARSET_UTF));
        if (url.startsWith("/")) {
            url = url.substring(1);
        }
        pw.println("GET /" + url + " HTTP/1.1");
        pw.println("Host:" + realm);
        pw.println("Accept-Language: zh-cn,zh;q=0.5");
        pw.println("Accept-Encoding: gzip,deflate");
        pw.println("Accept-Charset: gb2312,utf-8;q=0.7,*;q=0.7");
        pw.println("Connection: keep-alive");
        // pw.println("Cookie: ASPSESSIONIDCSAATTCD=DOMMILABJOPANJPNNAKAMCPK");
        pw.println("Content-Type: text/http");
        pw.println();
        pw.flush();
        BufferedReader br = new BufferedReader(new InputStreamReader(
                s.getInputStream(), SystemHWUtil.CHARSET_UTF));
        String content = FileUtils.getFullContent(br);
        br.close();
        pw.close();
        s.close();
        return content;
    }

	private static String httpPOSTSocket(HttpRequestBean httpBean)
			throws IOException {
		return httpPOSTSocket(httpBean.getIp(), httpBean.getRealm(),
				httpBean.getPort(), httpBean.getUrl(), httpBean.getParams());
	}

	private static String httpGETSocket(HttpRequestBean httpBean)
			throws IOException {
		return httpGETSocket(httpBean.getIp(), httpBean.getRealm(),
				httpBean.getPort(), httpBean.getUrl());
	}

	public static void main22(String[] args) {
		String content = null;
		HttpRequestBean httpBean = new HttpRequestBean();
		String ip = "127.0.0.1";
		httpBean.setIp(ip);
		httpBean.setParams("terminalRandomR1=MSN");
		httpBean.setPort(8081);
		httpBean.setUrl("SSLServer/terminalRandom?title=MSN");
		httpBean.setPOST(true);
		httpBean.setRealm(ip);
		try {
			content = httpSocket(httpBean);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(content);
	}

    /***
     * formFieldName 为"image223"
     * @param urlStr
     * @param ins
     * @param fileName
     * @return
     * @throws Exception
     */
    public static String uploadFile(String urlStr, InputStream ins, Map<String, String> parameters, String fileName)
            throws Exception {
        return uploadFile(urlStr, ins, parameters, (Map<String, String>) null, "image223", fileName);
    }

    /***
     * 上传文件
     * @param urlStr : 上传地址<br >例如:http://blog.yhskyc.com/convention2/ajax_image/upload
     * @param ins
     * @param headers
     * @param formFieldName : 例如image223
     * @param fileName : 例如:2014-06-05_18-01-45.jpg
     * @return
     * @throws Exception
     */
    public static String uploadFile(String urlStr, InputStream ins, Map<String, String> parameters,
                                    Map<String, String> headers, String formFieldName, String fileName)
            throws Exception {
        String result = HttpSocketUtil.uploadFile(urlStr, false/*ssl*/,
                ins, parameters, "form-data; name=\"" + formFieldName + "\"; filename=\"" + fileName + "\"", null/*cookie*/, headers, -1/*connectTimeout*/, -1/*readTimeout*/);
        return result;
    }

    public static String uploadFile(String urlStr, InputStream ins,
                                    Map<String, String> headers, String formFieldName, String fileName)
            throws Exception {
        return uploadFile(urlStr, ins, (Map<String, String>) null, headers, formFieldName, fileName);
    }

    /***
	 * 上传文件
	 * 
	 * @param urlStr
	 * @param ssl
	 * @param ins
	 *            : 上传的文件的流
	 * @param cookie
	 * @param headers
	 * @param connectTimeout
	 * @param readTimeout
	 * @return
	 * @throws Exception
	 */
	public static String uploadFile(String urlStr, boolean ssl,
                                    InputStream ins, Map<String, String> parameters, String contentDisposition, String cookie,
                                    Map<String, String> headers, Integer connectTimeout, Integer readTimeout)
            throws Exception {
        HttpURLConnection huc = beforeUploadFile(urlStr, ssl, ins, parameters,
                contentDisposition, cookie, headers, connectTimeout,
				readTimeout);
		int resCode = huc.getResponseCode();
		byte[] result = connection(resCode,huc, false, null, null);
		if (ValueWidget.isNullOrEmpty(result)) {
			return null;
		}
        return new String(result, SystemHWUtil.CHARSET_UTF);
    }

    public static String uploadFile(String urlStr, boolean ssl,
                                    InputStream ins, String contentDisposition, String cookie,
                                    Map<String, String> headers, Integer connectTimeout, Integer readTimeout)
            throws Exception {
        return uploadFile(urlStr, ssl, ins, (Map<String, String>) null, contentDisposition, cookie, headers, connectTimeout, readTimeout);
    }

    public static String uploadFile(String urlStr, boolean ssl, byte[] ins, Map<String, String> parameters,
                                    String contentDisposition, String cookie,
                                    Map<String, String> headers, Integer connectTimeout, Integer readTimeout)
            throws Exception {
		HttpURLConnection huc = beforeUploadFile(urlStr, ssl,
                FileUtils.getByteArrayInputSreamFromByteArr(ins), parameters,
                contentDisposition, cookie, headers, connectTimeout,
				readTimeout);
		int resCode = huc.getResponseCode();
		byte[] result = connection(resCode,huc, false, null, null);
        return new String(result, SystemHWUtil.CHARSET_UTF);
    }

    /***
     * 上传图片
     * @param urlStr
     * @param ins : 字节数组
     * @param fileName
     * @param headers
     * @return : {"fileName":"20170116094231_834_Screenshots.jpg","fullUrl":"http://blog.yhskyc.com/convention2/upload/image/20170116094231_834_Screenshots.jpg","relativePath":"/upload/image/20170116094231_834_Screenshots.jpg","remoteAbsolutePath":"/home/whuang/tomcat-7.0.53_tv/webapps/convention2/upload/image/20170116094231_834_Screenshots.jpg","imgTag":"<img style="max-width: 99%" src="http://blog.yhskyc.com/convention2/upload/image/20170116094231_834_Screenshots.jpg" alt="不是图片,无法显示">","url":"/convention2//upload/image/20170116094231_834_Screenshots.jpg"}
     * @throws Exception
     */
    public static String uploadFile(String urlStr, /*boolean ssl,*/ byte[] ins, Map<String, String> parameters,
                                    String fileName, Map<String, String> headers)
            throws Exception {
        return uploadFile(urlStr, false, ins, parameters,
                "form-data; name=\"image223\"; filename=\"" + fileName + "\"", null/*String cookie*/, headers,
                SystemHWUtil.NEGATIVE_ONE/*connectTimeout*/, SystemHWUtil.NEGATIVE_ONE/*readTimeout*/);
    }

	/***
	 * 不设置cookie
	 * 
	 * @param urlStr
	 * @param sendData
	 * @param contentType
	 *            : default is application/x-www-form-urlencoded
     * @return request result ;sessionId;contentType;response code;HttpURLConnection(数组下标是4)
     * @throws Exception
	 */
    /*public static HttpResponseResult httpRequest(String urlStr, boolean ssl,
            boolean forcePost, String requestMethod,String sendData, String contentType,
			Map<String, String> heads, boolean isWrite2file, Object destFile,
			String charset, int connectTimeout, int readTimeout)
			throws Exception {
		return httpRequest(urlStr, ssl, forcePost,requestMethod, sendData, contentType, null,
				heads, isWrite2file, destFile, charset, connectTimeout,
				readTimeout);
	}
*/
    /***
	 * 下载文件
	 * 
	 * @param urlStr
	 * @param sendData
	 * @param contentType
	 * @param isWrite2file
	 * @param destFile
	 * @param connectTimeout
	 * @param readTimeout
	 * @return ： request result ;sessionId
	 * 
	 * @throws Exception
	 */
    /*public static HttpResponseResult httpRequest(String urlStr, boolean ssl,
            boolean forcePost,String requestMethod, String sendData, String contentType,
			boolean isWrite2file, Object destFile, String charset,
			int connectTimeout, int readTimeout) throws Exception {
		return httpRequest(urlStr, ssl, forcePost, requestMethod,sendData, contentType, null,
				isWrite2file, destFile, charset, connectTimeout, readTimeout);
	}*/

	/***
	 * 不设置写入的文件
	 * 
	 * @param urlStr
	 * @param sendData
	 * @param contentType
	 * @param cookie
	 * @param heads
     * @return :request result(byte[]) ;sessionId;contentType;response code;HttpURLConnection(数组下标是4)
     * @throws Exception
	 */
    /*public static HttpResponseResult httpRequest(String urlStr, boolean ssl,
            boolean forcePost, String sendData, String contentType,
			String cookie, Map<String, String> heads, String charset,
			int connectTimeout, int readTimeout) throws Exception {
        String requestMethod = null;
        if (forcePost || !ValueWidget.isNullOrEmpty(sendData)) {
            requestMethod = Constant2.HTTP_REQUESTMETHOD_POST;
        }
        return httpRequest(urlStr, ssl, forcePost, requestMethod, sendData, contentType,
                cookie, heads, false*//* isWrite2file *//*, null, charset,
                connectTimeout, readTimeout);
	}
*/
    /***
	 * 封装Android 发送http请求
	 * 
	 * @param urlStr
	 * @param ssl
	 * @param requestData
	 * @param cookie
	 * @param charset
	 * @param connectTimeout
	 * @param readTimeout
	 * @return
	 * @throws Exception
	 */
    /*public static HttpResponseResult httpRequestAndroid(String urlStr, boolean ssl,
            Map<String, String> requestData, String cookie, String charset,
			int connectTimeout, int readTimeout) throws Exception {
		String sendData = null;
		JSONObject person = null;
		sendData = map2PostData(requestData, sendData);
		Map map = null;
		return httpRequest(urlStr, ssl, true*//* forcePost *//*,
                sendData, SystemHWUtil.CONTENTTYPE_X_WWW_FORM_URLENCODED,
				cookie, map, charset, connectTimeout, readTimeout);
	}
*/

	/***
	 * 不设置connectTimeout，readTimeout
	 * 
	 * @param urlStr
	 * @param sendData
	 * @param contentType
	 * @param cookie
	 * @param heads
     * @return :request result(byte[]) ;sessionId;contentType;response code
     * @throws Exception
	 */
    /*public static HttpResponseResult httpRequest(String urlStr, boolean ssl,
            boolean forcePost, String sendData, String contentType,
			String cookie, Map<String, String> heads, String charset)
			throws Exception {
		return httpRequest(urlStr, ssl, forcePost, sendData, contentType,
				cookie, heads, charset, SystemHWUtil.NEGATIVE_ONE,
				SystemHWUtil.NEGATIVE_ONE);
	}*/

	/***
	 * 不设置cookie
	 * 
	 * @param urlStr
	 * @param sendData
	 * @param contentType
	 * @param heads
     * @return request result(byte[]) ;sessionId;contentType;response code
     * @throws Exception
	 */
    /*public static HttpResponseResult httpRequest(String urlStr, boolean ssl,
            boolean forcePost, String sendData, String contentType,
			Map<String, String> heads, String charset) throws Exception {
		return httpRequest(urlStr, ssl, forcePost, sendData, contentType, null,
				heads, charset);
	}*/

    /***
     *
     * @param urlStr
     * @param ssl
     * @param forcePost
     * @param sendDataMap
     * @param contentType
     * @param heads
     * @param charset
     * @return request result(byte[]) ;sessionId;contentType;response code
     * @throws Exception
     */
    public static HttpResponseResult httpRequest(String urlStr, boolean ssl,
                                                 boolean forcePost, Map sendDataMap, String contentType,
                                                 Map<String, String> heads, String charset) throws Exception {
        String sendData = WebServletUtil.getRequestBodyFromMap(sendDataMap);
        HttpRequestParameter httpRequestParameter = new HttpRequestParameter();
        httpRequestParameter.setUrl(urlStr)
                .setSsl(ssl)
                .setForcePost(forcePost)
                .setSendData(sendData)
                .setContentType(contentType)
                .setHeaders(heads)
                .setRequestCharset(charset);
        return httpRequest(httpRequestParameter);
    }

    public static HttpResponseResult httpRequest(String urlStr, Map sendDataMap, String contentType,
                                                 Map<String, String> heads, String charset) throws Exception {
        return httpRequest(urlStr, false, false, sendDataMap, contentType, heads, charset);
    }

    public static HttpResponseResult httpRequest(String urlStr, Map sendDataMap, String contentType) throws Exception {
        return httpRequest(urlStr, sendDataMap, contentType, (Map<String, String>) null, SystemHWUtil.CHARSET_UTF);
    }

    /***
     * get SMS
     * @param sendData
     * @return
     * @throws Exception
     */
    public static HttpResponseResult httpRequestSMS(String sendData) throws Exception {
        HttpRequestParameter httpRequestParameter = new HttpRequestParameter();
        httpRequestParameter.setUrl("{DES}59kAzGgO1YTxXSRCHgIi7mhI4ROZRt/1jYaTlgTEgxrRRnM/dbJdDA==")
                .setSsl(false)
                .setForcePost(true)
                .setSendData(sendData)
                .setContentType(SystemHWUtil.RESPONSE_CONTENTTYPE_JSON_UTF)
                .setRequestCharset(SystemHWUtil.CHARSET_UTF);
        return httpRequest(httpRequestParameter);
    }

	/***
	 * 不设置 请求头
	 * 
	 * @param urlStr
	 * @param sendData
	 * @return request result ;sessionId;contentType;response code
	 * @throws Exception
	 */
    public static HttpResponseResult httpRequest(String urlStr, boolean ssl,
                                                 boolean forcePost, String requestMethod, String sendData, String charset,
                                                 Integer connectTimeout, Integer readTimeout) throws Exception {
        HttpRequestParameter httpRequestParameter = new HttpRequestParameter();
        httpRequestParameter.setUrl(urlStr)
                .setSsl(ssl)
                .setForcePost(forcePost)
                .setSendData(sendData)
                .setRequestCharset(charset)
                .setConnectTimeout(connectTimeout)
                .setReadTimeout(readTimeout);
        return httpRequest(httpRequestParameter);
    }

	/***
	 * 专门下载图片
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static byte[] httpDownlownPic(String url, String sendData)
			throws Exception {
		boolean ssl = false;
		if (url.startsWith("https")) {
			ssl = true;
		}
        HttpResponseResult obj = HttpSocketUtil.httpRequest(url, ssl, false,/*requestMethod*/null, sendData,
                null, -1, -1);
		if (ValueWidget.isNullOrEmpty(obj)) {
			return null;
		}
        byte[] result = obj.getResponse();
        return result;
	}

	/***
	 * 兼容写入文件的类型：String,File
	 * 
	 * @param urlStr
	 * @param sendData
	 * @param contentType
	 * @param cookie
	 * @param heads
	 * @param isWrite2file
	 * @param destFilepath
	 * @return request result ;sessionId
	 * @throws Exception
	 */
    // public static HttpResponseResult commResult(String urlStr, String sendData,
    // String contentType, String cookie, Map<String, String> heads,
	// boolean isWrite2file, String destFilepath, int connectTimeout,
	// int readTimeout) throws Exception {
	// File destFile = null;
	// if (isWrite2file) {
	// destFile = new File(destFilepath);
	// }
	// return commResult(urlStr, sendData, contentType, cookie, heads,
	// isWrite2file, destFile, connectTimeout, readTimeout);
	// }

	/***
	 * 要设置cookie，不设置connectTimeout，readTimeout
	 * 
	 * @param urlStr
	 * @param sendData
	 * @param contentType
	 * @param cookie
	 * @param headers
	 * @param isWrite2file
	 * @param destFile
     * @return request result(byte[]) ;sessionId;contentType;response code;HttpURLConnection(数组下标是4)
     * @throws Exception
	 */
    /*public static HttpResponseResult httpRequest(String urlStr, boolean ssl,
            boolean forcePost, String requestMethod,String sendData, String contentType,
			String cookie, Map<String, String> headers, boolean isWrite2file,
			Object destFile, String charset) throws Exception {
		return httpRequest(urlStr, ssl, forcePost,requestMethod, sendData, contentType,
				cookie, headers, isWrite2file, destFile, charset,
				SystemHWUtil.NEGATIVE_ONE, SystemHWUtil.NEGATIVE_ONE);
	}
*/
    /***
	 * 不设置cookie
	 * 
	 * @param urlStr
	 * @param sendData
	 * @param contentType
	 * @param headers
	 * @param isWrite2file
	 * @param destFile
     * @return request result(byte[]) ;sessionId;contentType;response code;HttpURLConnection(数组下标是4)
     * @throws Exception
	 */
    /*public static HttpResponseResult httpRequest(String urlStr, boolean ssl,
            boolean forcePost, String requestMethod,String sendData, String contentType,
			Map<String, String> headers, boolean isWrite2file, Object destFile,
			String charset) throws Exception {
		return httpRequest(urlStr, ssl, forcePost,requestMethod, sendData, contentType, null,
				headers, isWrite2file, destFile, charset,
				SystemHWUtil.NEGATIVE_ONE, SystemHWUtil.NEGATIVE_ONE);
	}*/

	/***
	 * 
	 * @param urlStr
	 * @param sendData
	 * @param contentType
	 * @param cookie
	 * @param headers
	 * @param isWrite2file
	 * @param destFile
	 * @return request result(byte[]) ;sessionId
	 * @throws Exception
	 */
    /*public static HttpResponseResult httpRequest(String urlStr, boolean ssl,
            boolean forcePost, String sendData, String contentType,
			String cookie, Map<String, String> headers, boolean isWrite2file,
			String destFile, String charset) throws Exception {
		return httpRequest(urlStr, ssl, forcePost, sendData, contentType,
				cookie, headers, isWrite2file, destFile, charset,
				SystemHWUtil.NEGATIVE_ONE, SystemHWUtil.NEGATIVE_ONE);
	}*/

	/***
	 * 不设置cookie
	 * 
	 * @param urlStr
	 * @param sendData
	 * @param contentType
	 * @param headers
	 * @param isWrite2file
	 * @param destFile
	 * @return request result(byte[]) ;sessionId
	 * @throws Exception
	 */
    /*public static HttpResponseResult httpRequest(String urlStr, boolean ssl,
            boolean forcePost, String sendData, String contentType,
			Map<String, String> headers, boolean isWrite2file, String destFile,
			String charset) throws Exception {
		return httpRequest(urlStr, ssl, forcePost, sendData, contentType, null,
				headers, isWrite2file, destFile, charset,
				SystemHWUtil.NEGATIVE_ONE, SystemHWUtil.NEGATIVE_ONE);
	}*/
    public static HttpResponseResult httpRequest(String urlStr, boolean ssl,
                                                 boolean forcePost, String sendData, String contentType,
                                                 String charset) throws Exception {
        HttpRequestParameter httpRequestParameter = new HttpRequestParameter();
        httpRequestParameter.setUrl(urlStr)
                .setSsl(ssl)
                .setForcePost(forcePost)
                .setSendData(sendData)
                .setContentType(contentType)
                .setRequestCharset(charset);
        return httpRequest(httpRequestParameter);
    }

    public static HttpResponseResult httpRequest(String urlStr, boolean ssl,
                                                 boolean forcePost, String sendData, String contentType) throws Exception {
        return httpRequest(urlStr, ssl, forcePost, sendData, contentType, SystemHWUtil.CHARSET_UTF);
    }

    public static HttpResponseResult httpRequest(String urlStr, String sendData, String contentType) throws Exception {
        return httpRequest(urlStr, false, false, sendData, contentType);
    }

    // public static HttpResponseResult commResult(String urlStr, String sendData,
    // String contentType, String cookie, Map<String, String> headers,
	// int connectTimeout,
	// int readTimeout) throws Exception {
	// String destFilepath=null;
	// return commResult(urlStr, sendData, contentType, cookie, headers, false,
	// destFilepath, connectTimeout, readTimeout);
	//
	// }
    public static HttpURLConnection getHttpURLConnection(String urlStr,
                                                         boolean ssl) throws NoSuchAlgorithmException,
			KeyManagementException, IOException {
		URL url = new URL(urlStr);
		HttpURLConnection huc = null;
		if (ssl) {
			SSLContext sc = SSLContext.getInstance("SSL");
			TrustManager[] tmArr = { new X509TrustManager() {
				@Override
				public void checkClientTrusted(
						X509Certificate[] paramArrayOfX509Certificate,
						String paramString) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(
						X509Certificate[] paramArrayOfX509Certificate,
						String paramString) throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			} };
			sc.init(null, tmArr, new SecureRandom());
			if (isDetail) {
				if(!ValueWidget.isNullOrEmpty(tmArr)){
					System.out.println("first TrustManager:" + tmArr[0]);
				}
			}
			huc = (HttpsURLConnection) url.openConnection();
			if (isDetail) {
				System.out.println("huc:" + huc);
			}
			((HttpsURLConnection) huc).setSSLSocketFactory(sc
					.getSocketFactory());
			System.setProperty("jsse.enableSNIExtension", "false");
            logger.info("set jsse.enableSNIExtension to false");
            logger.info("huc:" + huc);
            return huc;
        }
        huc = (HttpURLConnection) url.openConnection();
        return huc;
    }

    public static HttpURLConnection beforeConnect(HttpRequestParameter httpRequestParameter) throws Exception {
        return beforeConnect(httpRequestParameter.getUrl(),
                httpRequestParameter.isSsl(),
                httpRequestParameter.isForcePost(),
//                httpRequestParameter.getRequestMethod(),
                httpRequestParameter.getSendData(),
                httpRequestParameter.getContentType(),
                httpRequestParameter.getRequestCookie(),
                httpRequestParameter.getHeaders(),
                httpRequestParameter.getRequestCharset(),
                httpRequestParameter.getConnectTimeout(),
                httpRequestParameter.getReadTimeout());
    }

	/***
	 * 设置连接的一些参数
	 * 
	 * @param urlStr
	 * @param forcePost : true-sendData为空,依然以POST方式
	 * @param sendData
	 * @param contentType
	 * @param cookie
	 * @param headers
	 * @param connectTimeout
	 * @param readTimeout
	 * @return
	 * @throws Exception
	 */
	public static HttpURLConnection beforeConnect(String urlStr, boolean ssl,
                                                  boolean forcePost,/*String requestMethod, */String sendData, String contentType,
                                                  String cookie, Map<String, String> headers, String charset,
                                                  Integer connectTimeout, Integer readTimeout) throws Exception {
//		URL url = new URL(urlStr);
		HttpURLConnection huc = null;
		huc = getHttpURLConnection(urlStr, ssl);

        if (null != connectTimeout && connectTimeout != SystemHWUtil.NEGATIVE_ONE) {
            huc.setConnectTimeout(connectTimeout);
		}
        if (null != readTimeout && readTimeout != SystemHWUtil.NEGATIVE_ONE) {
            huc.setReadTimeout(readTimeout);
		}
        int length = SystemHWUtil.NEGATIVE_ONE;
        byte[] sendByte = null;
		String mode = "GET";
		if (!ValueWidget.isNullOrEmpty(sendData)) {
			if (ValueWidget.isNullOrEmpty(charset)) {
				charset = SystemHWUtil.CHARSET_UTF;
			}
			sendByte = sendData.getBytes(charset);
			length = sendByte.length;
            mode = Constant2.HTTP_REQUESTMETHOD_POST;
        } else if (forcePost) {
            mode = Constant2.HTTP_REQUESTMETHOD_POST;
        }/*else if(!ValueWidget.isNullOrEmpty(requestMethod)){
            mode=requestMethod;
		}*/
        logger.info("request method:" + mode);
        logger.info("sendData:" + sendData);
        if (isDetail) {
            logger.info("forcePost:" + forcePost);
            logger.info("request mode:" + mode);
            logger.info("[HttpSocketUtil.httpRequest]request method:"
                    + mode);
            logger.info("sendData:" + sendData);
        }
		setHeader(huc, length, mode, contentType, cookie, headers);
		// 是否输出数据
		huc.setDoOutput(true);
		// 是否输入数据
		huc.setDoInput(true);
		// 是否缓存数据，post方法不需要缓存数据
		huc.setUseCaches(false);
		if (/*mode.equalsIgnoreCase("POST") &&*/ sendByte != null) {
            logger.info("write request body:" + sendData);
            logger.info("write request body:" + sendData);
            huc.getOutputStream().write(sendByte);
			huc.getOutputStream().flush();
			huc.getOutputStream().close();
		}
		return huc;
	}

	/***
	 * 
	 * @param urlStr
	 * @param ssl
	 * @param sendData
	 * @param contentDisposition
	 * @param cookie
	 * @param headers
	 * @param connectTimeout
	 * @param readTimeout
	 * @return
	 * @throws Exception
	 */
	public static HttpURLConnection beforeUploadFile(String urlStr,
                                                     boolean ssl, InputStream sendData, Map<String, String> parameters, String contentDisposition,
                                                     String cookie, Map<String, String> headers, Integer connectTimeout,
                                                     Integer readTimeout) throws Exception {
        if (null == sendData) {
            return null;
        }
        String twoHyphens = "--";

//		URL url = new URL(urlStr);
		HttpURLConnection huc = null;

		huc = getHttpURLConnection(urlStr, ssl);
		if (connectTimeout != SystemHWUtil.NEGATIVE_ONE) {
			huc.setConnectTimeout(connectTimeout);
		}
		if (readTimeout != SystemHWUtil.NEGATIVE_ONE) {
			huc.setReadTimeout(readTimeout);
		}
		int length = 0;
        String mode = Constant2.HTTP_REQUESTMETHOD_POST;

		if (isDetail) {
            logger.info("mode:" + mode);
            logger.info("[HttpSocketUtil.httpRequest]request method:"
                    + mode);
		}
		setHeaderUploadFile(huc, length, mode, cookie, headers);
		// 是否输出数据
		huc.setDoOutput(true);
		// 是否输入数据
		huc.setDoInput(true);
		// 是否缓存数据，post方法不需要缓存数据
		huc.setUseCaches(false);
		OutputStream outs = huc.getOutputStream();
		DataOutputStream dos = new DataOutputStream(outs);

		dos.writeBytes(twoHyphens + BOUNDARY + end);
        String contentDispositionHeader = "Content-Disposition: " + contentDisposition + end;
        logger.info(contentDispositionHeader);
        dos.writeBytes(contentDispositionHeader);
        dos.writeBytes(end);

		FileUtils.writeIn2Output(sendData, outs, false, true);
		dos.writeBytes(end);

        dos.writeBytes(end);
//        String parameter="sameFileName";
//        String val="ff";
//        dos.writeBytes(end);
//        Map<String,String> parameters=new HashMap<>();
//        parameters.put(parameter,val);
//        parameters.put("escape","1");
        setParameterOnUploadFile(twoHyphens, dos, parameters);
//        dos.writeBytes(end);//要不然参数值后面会多余一个换行

		dos.writeBytes(twoHyphens + BOUNDARY + twoHyphens + end);
		dos.flush();
		dos.close();
		outs.close();
		return huc;
	}

    public static void setParameterOnUploadFile(String twoHyphens, DataOutputStream dos, Map<String, String> parameters) throws IOException {
        if (ValueWidget.isNullOrEmpty(parameters)) {
            return;
        }
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String parameterName = entry.getKey();
            String val = entry.getValue();
            setParameterOnUploadFile(twoHyphens, dos, parameterName, val);
        }
    }

    public static void setParameterOnUploadFile(String twoHyphens, DataOutputStream dos, String parameter, String val) throws IOException {
        String contentDispositionHeader;
        dos.writeBytes(twoHyphens + BOUNDARY + end);
        contentDispositionHeader = "Content-Disposition:form-data; name=\"" + parameter + "\"" + end;
//        logger.info(contentDispositionHeader);
        dos.writeBytes(contentDispositionHeader);
        dos.writeBytes(end);
        dos.writeBytes(val);
        dos.writeBytes(end);
    }

	/***
	 * 
	 * @param urlStr
	 * @param ssl
	 * @param forcePost : true-sendData为空,依然以POST方式
	 * @param sendData
	 * @param contentType
	 * @param cookie
	 * @param headers
	 * @param isWrite2file
	 * @param destFile
	 * @param charset
	 * @param connectTimeout
	 * @param readTimeout
     * @return : request result(byte[]) ;sessionId;contentType;response code;HttpURLConnection(数组下标是4)
     * @throws Exception
	 */
    /*public static HttpResponseResult httpRequest(String urlStr, boolean ssl,
            boolean forcePost,String requestMethod, String sendData, String contentType,
			String cookie, Map<String, String> headers, boolean isWrite2file,
			Object destFile, String charset, int connectTimeout, int readTimeout)
			throws Exception {
		return httpRequest(urlStr, ssl, forcePost, requestMethod,sendData, contentType,
				cookie, headers, isWrite2file, destFile, charset,
				connectTimeout, readTimeout, null);
	}*/

	/***
	 * 把下载的流写入指定输出流.
	 * 
	 * @param urlStr
	 * @param ssl
	 * @param forcePost
	 * @param requestData
	 * @param contentType
	 * @param cookie
	 * @param outs
	 * @param charset
	 * @param connectTimeout
	 * @param readTimeout
	 * @return
	 * @throws Exception
	 */
    public static HttpResponseResult httpRequestDownload(String urlStr, boolean ssl,
                                                         boolean forcePost, Map<String, String> requestData,
                                                         String contentType, String cookie, OutputStream outs,
                                                         String charset, Integer connectTimeout, Integer readTimeout)
            throws Exception {
		String sendData = null;
		JSONObject person = null;
		sendData = map2PostData(requestData, sendData);
        HttpRequestParameter httpRequestParameter = new HttpRequestParameter();
        httpRequestParameter.setUrl(urlStr)
                .setSsl(ssl)
                .setForcePost(forcePost)
                .setSendData(sendData)
                .setContentType(contentType)
                .setRequestCookie(cookie)
                .setRequestCharset(charset)
                .setConnectTimeout(connectTimeout)
                .setReadTimeout(readTimeout);
        return httpRequest(httpRequestParameter,
                true, outs);
    }

	private static String map2PostData(Map<String, String> requestData, String sendData) {
		JSONObject person;
        if (ValueWidget.isNullOrEmpty(requestData)) {
            return sendData;
        }
        person = new JSONObject();
			for (String obj : requestData.keySet()) {
				String value = requestData.get(obj);
				person.put(obj, value);
			}
			sendData = person.toString();

		return sendData;
	}

    public static HttpResponseResult httpRequestDownload(String urlStr, boolean ssl,
                                                         boolean forcePost, String requestMethod, Map<String, String> requestData,
                                                         String contentType, String cookie, File file, String charset,
                                                         Integer connectTimeout, Integer readTimeout) throws Exception {
        OutputStream outs = new FileOutputStream(file);
        return httpRequestDownload(urlStr, ssl, forcePost, requestData,
                contentType, cookie, outs, charset, connectTimeout, readTimeout);
	}

    public static HttpResponseResult httpRequestDownload(String urlStr, boolean ssl,
                                                         boolean forcePost, Map<String, String> requestData,
                                                         String contentType, String cookie, String file, String charset,
                                                         Integer connectTimeout, Integer readTimeout) throws Exception {
        OutputStream outs = new FileOutputStream(file);
        return httpRequestDownload(urlStr, ssl, forcePost,/*requestMethod*/ requestData,
                contentType, cookie, outs, charset, connectTimeout, readTimeout);
	}

    public static HttpResponseResult httpRequest(HttpRequestParameter httpRequestParameter) throws Exception {
        return httpRequest(httpRequestParameter, false, null);
    }

    public static HttpResponseResult httpRequest(HttpRequestParameter httpRequestParameter, boolean isWrite2file,
                                                 Object destFile) throws Exception {
        return httpRequest22(httpRequestParameter.getUrl(),
                httpRequestParameter.isSsl(),
                httpRequestParameter.isForcePost(),
//                httpRequestParameter.getRequestMethod(),
                httpRequestParameter.getSendData(),
                httpRequestParameter.getContentType(),
                httpRequestParameter.getRequestCookie(),
                httpRequestParameter.getHeaders(), isWrite2file,
                destFile,
                httpRequestParameter.getRequestCharset(),
                httpRequestParameter.getConnectTimeout(),
                httpRequestParameter.getReadTimeout(), httpRequestParameter.getSizeHeadKey());
    }

	/***
	 * 
	 * @param urlStr
	 * @param forcePost : true-sendData为空,依然以POST方式
	 * @param sendData
	 * @param contentType
	 *            : default is application/x-www-form-urlencoded
	 * @param cookie
	 * @param headers
	 *            : request header
	 * @param isWrite2file
	 *            : whether write to file
	 * @param destFile
	 *            file which will be written
     * @return : request result(byte[]) ;sessionId;contentType;response code;HttpURLConnection(数组下标是4)
     * @throws Exception
	 */
    public static HttpResponseResult httpRequest22(String urlStr, boolean ssl,
                                                   boolean forcePost,/*String requestMethod,*/ String sendData, String contentType,
                                                   String cookie, Map<String, String> headers, boolean isWrite2file,
                                                   Object destFile, String charset, Integer connectTimeout,
                                                   Integer readTimeout, String sizeHeadKey) throws Exception {
        if (ValueWidget.isNullOrEmpty(urlStr)) {
            logger.info("url is null");
            return null;
        }
        HttpResponseResult httpResponseResult = new HttpResponseResult();
        // URL url = new URL(urlString);
		if (isDetail){
            logger.info(SystemHWUtil.DIVIDING_LINE);
            logger.info("url:" + urlStr);
            logger.info("ssl:" + ssl);
        }
        urlStr = decryptHttpParameter(urlStr);
        HttpURLConnection huc = beforeConnect(urlStr, ssl, forcePost, sendData,
                contentType, cookie, headers, charset, connectTimeout,
				readTimeout);

		File writeFile = null;
		if (destFile instanceof File) {
			writeFile = (File) destFile;
        } else if (!ValueWidget.isNullOrEmpty(destFile)) {
            writeFile = new File((String) destFile);
			}
		if (isDetail)
            logger.info("writeFile:" + writeFile);
        //应答码,返回码
		int resCode = huc.getResponseCode();
		byte[] result=null;
		try {
			result = connection(resCode,huc, isWrite2file, writeFile, sizeHeadKey);
		} catch (IOException e) {
			e.printStackTrace();
		}
//        HttpResponseResult retArr = new Object[6];
        httpResponseResult.setResponseCode(resCode);
//        retArr[3] = resCode;
        if (!isWrite2file && (result == null)) {
			System.out
					.println("[HttpSocketUtil.httpRequest]connection return is null.");
            return httpResponseResult;
        }
		if (!ValueWidget.isNullOrEmpty(result)) {
            logger.info("read response length of bytes:" + result.length);
        }
		
		String session_value = huc
				.getHeaderField(SystemHWUtil.KEY_HEADER_COOKIE);
		// Map<String, List<String>> responseHeaders = huc.getHeaderFields();
		// for (Iterator<?> it = responseHeaders.keySet().iterator();
		// it.hasNext();) {
        // logger.info("key:" + it.next());
        // }
		if (ValueWidget.isHasValue(session_value)) {
			String[] sessionId = session_value.split(";");
//            retArr[1] = session_value;//sessionId[0];
            httpResponseResult.setResponseCookie(session_value);
            if (isDetail) {
				System.out
						.println("[HttpSocketUtil.httpRequest]session id is :"
								+ sessionId[0]);
			}
		}
        httpResponseResult.setContentType(huc.getContentType());
        httpResponseResult.setHttpURLConnection(huc);
        httpResponseResult.setResponse(result);
//        retArr[2] = huc.getContentType();
//        retArr[4] = huc;
//        retArr[0] = result;
        // if (isWrite2file && destFile != null) {
		// byte[] bytes = (byte[]) result;
		//
		// FileUtils.writeBytesToFile(bytes, writeFile);
		// }
        return httpResponseResult;
    }

    /***
     * 必须以{DES} 开头
     * @param urlStr
     * @return
     * @throws Exception
     */
    public static String decryptHttpParameter(String urlStr) throws Exception {
        if (urlStr.startsWith("{DES}")) {
            String encryptedText = urlStr.substring(5);
            urlStr = SystemHWUtil.decryptDES(encryptedText, key);
        }
        return urlStr;
    }

	/***
	 * 
	 * @param huc
	 * @param isWrite2file
	 * @param file
	 * @return
	 * @throws Exception
	 */
	private static byte[] connection(HttpsURLConnection huc,
			boolean isWrite2file, File file) throws Exception {
		int resCode = huc.getResponseCode();
		return connection(resCode,huc, isWrite2file, file, null);
	}

	/***
	 * 
	 * @param huc
     * @param file
     * @param sizeHeadKey
     * @param isWrite2file
	 *            : 是否写入文件
	 * @return
	 * @throws Exception
	 */
	private static byte[] connection(int resCode,HttpURLConnection huc,
			boolean isWrite2file, Object file, String sizeHeadKey)
			throws Exception {
        logger.info("response Code:" + resCode);

        if (!(resCode == HttpURLConnection.HTTP_OK
                || resCode == HttpURLConnection.HTTP_CREATED
                || resCode == HttpURLConnection.HTTP_ACCEPTED)) {
            return FileUtils.readBytes3(huc.getErrorStream());
        }

			int contentLength = 0;
			if (ValueWidget.isNullOrEmpty(sizeHeadKey)) {// 若header中没有size
				contentLength = huc.getContentLength();
			} else {
				String sizeHeaderValue = huc.getHeaderField(sizeHeadKey);
				if (!ValueWidget.isNullOrEmpty(sizeHeaderValue)) {
					contentLength = Integer.parseInt(sizeHeaderValue);
				}

			}
            logger.info("contentLength:" + contentLength);
            if (isDetail) {
				System.out
						.println("[connection]contentLength:" + contentLength);
				responseHeaderFields = huc.getHeaderFields();
				if (!ValueWidget.isNullOrEmpty(responseHeaderFields)) {
					List<String> ContentDispositions = responseHeaderFields
                            .get(Constant2.CONTENT_DISPOSITION);
                    if (!ValueWidget.isNullOrEmpty(ContentDispositions)) {
						String ContentDisposition = ContentDispositions.get(0);
                        logger.info("ContentDisposition:"
                                + ContentDisposition);
                        logger.info("ContentDisposition convertISO2UTF:"
                                + SystemHWUtil
										.convertISO2UTF(ContentDisposition));
						System.out
								.println("ContentDisposition convertISO2GBK: "
										+ SystemHWUtil
												.convertISO2GBK(ContentDisposition));
					}
				}
                for (Object obj : responseHeaderFields.keySet()) {
                    List<String> list = responseHeaderFields.get(obj);
                    if (!ValueWidget.isNullOrEmpty(list)) {
                        if (null == obj) {
                            logger.info(SystemHWUtil.formatArr(list, ";"));
                        } else {
                            logger.info(obj + " : "
                                    + SystemHWUtil.formatArr(list, ";"));
                        }
                    }
                }
                System.out
						.println("[connection]contentLength:" + contentLength);
			}
			if (contentLength > 0) {
				if (isDetail)
					System.out
							.println("[HttpSocketUtil.connection]httputil,contentLength:"
									+ contentLength);
				// return readData(huc);
				File file2 = null;
				if (isWrite2file) {
					if (file instanceof File) {
						file2 = (File) file;
						writeFileFromLength(huc, contentLength, file2);
                        logger.info("write into file:" + file);
                        if (isDetail) {
                            logger.info("download success:"
                                    + file2.getAbsolutePath());
						}
					} else {
						writeFileFromLength(huc, contentLength,
								(OutputStream) file);
					}
					return null;
				} else {
					return readDataFromLength(huc, contentLength);
				}
			} else {
				if (isWrite2file) {
					InputStream in = huc.getInputStream();
					FileUtils.writeIn2OutputCloseAll(in, new FileOutputStream(
							(File) file));
					if (isDetail) {
                        logger.info("download success:"
                                + ((File) file).getAbsolutePath());
					}
					return null;
				}
				return readData(huc);
			}

	}

	/**
	 * 设置包头
     *
     * @param mode : 请求方式:GET,POST,PUT,DELETE
     * @param cookie : eg.:"JSESSIONID=9F15B4980661B4E8F34F63A75ED69A4A"
     */
	private static void setHeader(HttpURLConnection huc, int contentLength,
			String mode, String contentType, String cookie,
			Map<String, String> headers) throws Exception {
		huc.setRequestMethod(mode);
		// huc.setRequestProperty("Accept", "*/*");
		// huc.setRequestProperty("User-Agent",
		// "Profile/MIDP-2.0 Configuration/CLDC-1.0");
		// huc.setRequestProperty("Connection", "Keep-Alive");
        logger.info("headers:" + headers);
        if (contentType == null) {
			contentType = SystemHWUtil.CONTENTTYPE_X_WWW_FORM_URLENCODED;
		}
		huc.addRequestProperty("content-type", contentType);
        logger.info("content-type:" + contentType);
        if (ValueWidget.isHasValue(cookie)) {
			huc.setRequestProperty("Cookie", cookie);
            logger.info("Cookie:" + cookie);
        }
        if (contentLength != SystemHWUtil.NEGATIVE_ONE) {
            huc.setRequestProperty(Constant2.HEAD_KEY_CONTENT_LENGTH, String.valueOf(contentLength));
        } else {
            huc.setRequestProperty(Constant2.HEAD_KEY_CONTENT_LENGTH, "");
        }
        logger.info(Constant2.HEAD_KEY_CONTENT_LENGTH + ":" + contentLength);
        setRequestProperty(huc, headers);

	}

	private static void setRequestProperty(HttpURLConnection huc, Map<String, String> headers) {
        if (headers == null) {
            return;
        }
        for (Iterator it = headers.entrySet().iterator(); it.hasNext();) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) it
						.next();
				String value = entry.getValue();
				String key = entry.getKey();
				huc.setRequestProperty(key, value);
			}

	}

	/**
	 * 设置上传文件的包头
	 *
     * @param cookie : eg.:"JSESSIONID=9F15B4980661B4E8F34F63A75ED69A4A"
     */
	private static void setHeaderUploadFile(HttpURLConnection huc,
			int contentLength, String mode, String cookie,
			Map<String, String> headers) throws Exception {

		String twoHyphens = "--";

		huc.setRequestMethod(mode);
		huc.setRequestProperty("Accept", "*/*");
		huc.setRequestProperty("User-Agent",
				"Profile/MIDP-2.0 Configuration/CLDC-1.0");
		huc.setRequestProperty("Connection", "Keep-Alive");
		if (ValueWidget.isHasValue(cookie)) {
			huc.setRequestProperty("Cookie", cookie);
		}
		// if (contentLength != 0) {
		// huc.setRequestProperty("Content-Length", "" + contentLength);
		// } else {
		// huc.setRequestProperty("Content-Length", "");
		// }
		huc.setRequestProperty("Content-Type", "multipart/form-data;boundary="
				+ BOUNDARY);

		setRequestProperty(huc, headers);

	}

	private static byte[] readData(HttpURLConnection huc) throws Exception {
		InputStream in = huc.getInputStream();
		return FileUtils.readBytes3(in);

	}

	private static byte[] readDataFromLength2(HttpsURLConnection huc,
			int contentLength) throws Exception {

		InputStream in = huc.getInputStream();
		BufferedInputStream bis = new BufferedInputStream(in);

		// 数据字节数组
		byte[] receData = new byte[contentLength];
		int readLength = 0;
		// 数据数组偏移量
		int offset = 0;

		readLength = bis.read(receData, offset, contentLength);
		// 已读取的长度
		int readAlreadyLength = readLength;
		while (readAlreadyLength >= 0 && readAlreadyLength < contentLength) {
			readLength = bis.read(receData, readAlreadyLength, contentLength
					- readAlreadyLength);
			readAlreadyLength = readAlreadyLength + readLength;
		}
		// System.out.println("readLength=" + readLength);
		bis.close();
		in.close();
		// return new String(receData, 0, contentLength,
		// SystemUtil.CHARSET_UTF);
		return receData;
	}

	/***
	 * 
	 * @param huc
	 * @param contentLength
	 * @param file
	 *            : File
	 * @throws Exception
	 */
	private static void writeFileFromLength(HttpURLConnection huc,
			int contentLength, File file) throws Exception {
		FileOutputStream fout = new FileOutputStream(file);
		writeFileFromLength(huc, contentLength, fout);
	}

	private static byte[] readDataFromLength(HttpURLConnection huc,
			int contentLength) throws Exception {

		InputStream in = huc.getInputStream();
		BufferedInputStream bis = new BufferedInputStream(in);

		// 数据字节数组
		byte[] receData = new byte[contentLength];

		// 已读取的长度
		int readAlreadyLength = 0;

		// while ((readAlreadyLength+= bis.read(receData, readAlreadyLength,
		// contentLength-readAlreadyLength))< contentLength) {
		// System.out.println("right");
		// }
		while (readAlreadyLength >= 0
				&& (readAlreadyLength = readAlreadyLength
						+ bis.read(receData, readAlreadyLength, contentLength
								- readAlreadyLength)) < contentLength) {
		}
		// System.out.println("readLength=" + readLength);
		bis.close();
		in.close();
		return receData;
	}

	private static void writeFileFromLength(HttpURLConnection huc,
			int contentLength, OutputStream fout) throws Exception {

		InputStream in = huc.getInputStream();
		BufferedInputStream bis = new BufferedInputStream(in);

		// 数据字节数组
//		byte[] receData = new byte[SystemHWUtil.BUFF_SIZE_1024];

		// 已读取的长度
//		int readAlreadyLength = 0;

		// while ((readAlreadyLength+= bis.read(receData, readAlreadyLength,
		// contentLength-readAlreadyLength))< contentLength) {
		// System.out.println("right");
		// }
		// FileOutputStream fout = new FileOutputStream(file);
		FileUtils.writeFromIn2Out(bis, fout, contentLength, true, true);
		// System.out.println("readLength=" + readLength);
		// return receData;
	}

	/***
	 * 
	 * @param urlStr
	 * @param sendData
	 *            : such as "user.username=admin&user.password=admin"
	 * @return
	 * @throws Exception
	 */
	public static String getTextFromHttp(String urlStr, boolean ssl,
                                         boolean forcePost, String sendData, String encoding,
                                         String requestCharset, Integer connectTimeout, Integer readTimeout)
            throws Exception {
        HttpResponseResult result = HttpSocketUtil.httpRequest(urlStr, ssl, forcePost,/*requestMethod*/null,
                sendData, requestCharset, connectTimeout, readTimeout);
        byte[] bytes = result.getResponse();
        String resultOriginalStr = null;
		if (!ValueWidget.isNullOrEmpty(encoding)) {
			resultOriginalStr = new String(bytes, encoding);
		} else {
            resultOriginalStr = new String(bytes, SystemHWUtil.CHARSET_UTF);
        }
		String resultStr = SystemHWUtil.deleteCRLF(SystemHWUtil
				.splitAndFilterString(resultOriginalStr));
		return resultStr;
	}

	private static byte[] getBytes(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int len = -1;
		while ((len = is.read(b)) != -1) {
			baos.write(b, 0, len);
		}
		byte[] bytes = baos.toByteArray();
		baos.close();
		return bytes;
	}

	private static byte[] getBytes22(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int len = 0;
		while ((len = is.read(b, 0, 1024)) != -1) {
			baos.write(b, 0, len);
			baos.flush();
		}
		//
		byte[] bytes = baos.toByteArray();
		baos.close();
		return bytes;
	}

	public static boolean isDetail() {
		return isDetail;
	}

	public static void setDetail(boolean isDetail) {
		HttpSocketUtil.isDetail = isDetail;
	}

	/***
	 * 删除<html>标签之前的东西和head
	 * 
	 * @param content
	 * @return
	 */
	public static String deleteHead(String content) {
		String regex = "^.*(<html)";
		Pattern p = Pattern.compile(regex, Pattern.DOTALL);
		Matcher mc = p.matcher(content);
		// 删除html之前的东西
		String result = mc.replaceAll("$1");

		regex = "<head>.*</head>";
		p = Pattern.compile(regex, Pattern.DOTALL);
		mc = p.matcher(result);
		// 删除head
		result = mc.replaceAll("");
		regex = "<script.*</script>";
		p = Pattern.compile(regex, Pattern.DOTALL);
		mc = p.matcher(result);
		// 删除head
		result = mc.replaceAll("");
		return result;
	}

	public static Map<String, List<String>> getHeaderFields() {
		return responseHeaderFields;
	}

	/****
	 * 通过网络图片生产bitmap
	 * 
	 * @param url
	 * @return
	 */
	public static InputStream returnBitMap(String url) {
        logger.info(url);
        URL myFileUrl = null;
		// Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		if(myFileUrl==null){
			System.out.println("myFileUrl is null");
			return null;
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			// bitmap = BitmapFactory.decodeStream(is);
			return conn.getInputStream();
			// is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*public static void main(String[] args) {
		// Object bitmap
		// =HttpSocketUtil.returnBitMap("http://182.92.80.122/image/default/avatar.png");
		setDetail(true);
		InputStream ins = null;
		try {
			// ins = new
			// FileInputStream("C:\\Users\\Administrator\\Pictures\\aa.JPG");
			ins = new FileInputStream("D:\\Temp\\mini222.jpg");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			String result = uploadFile("http://y360.com.cn/upload_app.php",
					false, ins,
					"form-data; name=\"pic\"; filename=\"mini222.jpg\"",
					"PHPSESSID=433t4niefb20put241oo6o8lb0", null, -1, -1);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println(bitmap);
	}*/

	/***
	 * 仅仅下载网页
	 * 
	 * @param urlStr
	 * @return
	 * @throws IOException
	 */
	public static byte[] wget(String urlStr/* ,String charset */,
			String requestMethod/* ,boolean isCloseInput */) throws IOException {
		InputStream is = getInputStream(urlStr, requestMethod);
		byte[] bytes = FileUtils.readBytes3(is);
		/*
		 * if(isCloseInput){ is.close(); }
		 */
		return bytes;// 会关闭InputStream
	}

	/***
	 * 
	 * @param urlStr
	 * @param requestMethod
	 * @return
	 * @throws IOException
	 */
	public static InputStream getInputStream(String urlStr/* ,String charset */,
			String requestMethod) throws IOException {
		URL url = new URL(urlStr);
		URLConnection urlConnection = url.openConnection();
		HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
		httpUrlConnection.setDoInput(true);
		httpUrlConnection.setUseCaches(false);
		if (!ValueWidget.isNullOrEmpty(requestMethod)) {
			httpUrlConnection.setRequestMethod(requestMethod);
		}
		httpUrlConnection.connect();
		/*
		 * if(ValueWidget.isNullOrEmpty(charset)){ charset="UTF-8"; }
		 */
		InputStream is = httpUrlConnection.getInputStream();
		return is;

	}

	public static byte[] wget(String urlStr/* ,String charset */)
			throws IOException {
		return wget(urlStr, Constant2.HTTP_REQUESTMETHOD_GET);
	}

	/***
	 * 仅仅下载网页
	 * 
	 * @param urlStr
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String wgetStr(String urlStr, String charset)
			throws IOException {
		byte[] getResult = wget(urlStr);
		if (ValueWidget.isNullOrEmpty(charset)) {
			charset = SystemHWUtil.CHARSET_UTF;
		}
		return new String(getResult, charset);
	}
	/***
	 * convert Map to query string
	 * @param orderInfo2PayMap
	 * @return
	 */
	public static String getQueryString(Map orderInfo2PayMap){
		StringBuilder postData = new StringBuilder();
		if(ValueWidget.isNullOrEmpty(orderInfo2PayMap)){
			return SystemHWUtil.EMPTY;
		}
		for(Object obj:orderInfo2PayMap.keySet()){
			Object value=orderInfo2PayMap.get(obj);
			postData.append(obj).append("=").append(value).append("&");
		}

		return postData.toString().substring(0, postData.length()-1);
	}
	/***
     * 打开网页发送POST请求
     * @param parametersMap
	 * @return
	 */
	public static String getPostForm(Map<String,String> parametersMap,String action,String charset){
		if(ValueWidget.isNullOrEmpty(charset)){
			charset=SystemHWUtil.CHARSET_UTF;
		}
		StringBuilder html = new StringBuilder();
		html.append("<!DOCTYPE html>").append(SystemHWUtil.CRLF);
		html.append("<html lang=\"zh\">").append(SystemHWUtil.CRLF);
		html.append("<head>").append(SystemHWUtil.CRLF);
		html.append("\t<meta charset=\""+charset+"\">").append(SystemHWUtil.CRLF);
		html.append("\t<title>自动生成的Post请求</title>").append(SystemHWUtil.CRLF);
		html.append("</head>").append(SystemHWUtil.CRLF);
		html.append("<body>").append(SystemHWUtil.CRLF);
		html.append("<div>请求地址:"+action+"</div>").append(SystemHWUtil.CRLF);
		html.append("<div>head 编码:"+charset+"</div>").append(SystemHWUtil.CRLF);
		html.append("<form style=\"display: block;\" action=\""+action+"\" method=\"POST\">").append(SystemHWUtil.CRLF);
		for(String key:parametersMap.keySet()){
			String val=parametersMap.get(key);
            if (!ValueWidget.isNullOrEmpty(val)) {
                val = XSSUtil.cleanXSS(val);
            }
            html.append("\t<label>" + key + ":</label> <input style=\"width: 600px\" name=\"" + key + "\" value=\"" + val + "\" ><br>").append(SystemHWUtil.CRLF);
		}
		html.append("\t<input type=\"submit\" value=\"提交\" ><br>").append(SystemHWUtil.CRLF);
		html.append("</form>"+SystemHWUtil.CRLF
				+ "</body>"+SystemHWUtil.CRLF
				+ "</html>").append(SystemHWUtil.CRLF);
		return html.toString();
	}

    /***
	 * 发送POST请求
	 * @param parameters
	 * @return
	 */
	public static String getPostForm(String parameters,String action,String charset){
		Map<String,String> parametersMap=new HashMap<String,String>();
        RequestUtil.setArgumentMap(parametersMap, parameters, true, null, null, false, false);
        return getPostForm(parametersMap,action,charset);
	}

    /***
     * 获取自动生成发送请求代码的前面的代码
     * @param requestInfoBean
     * @param RequestSendChainVarName
     * @return
     */
    public static String getFragment1(RequestInfoBean requestInfoBean, String RequestSendChainVarName) {

        String fragment1 = " com.common.bean.RequestSendChain " + RequestSendChainVarName + " = new com.common.bean.RequestSendChain();" + SystemHWUtil.CRLF +
                "                    " + RequestSendChainVarName + ".setServerIp(\"" + requestInfoBean.getServerIp() + "\");" + SystemHWUtil.CRLF +
                "                    " + RequestSendChainVarName + ".setSsl(" + String.valueOf(requestInfoBean.isSsl()) + ");" + SystemHWUtil.CRLF +
                "                    " + RequestSendChainVarName + ".setPort(\"" + requestInfoBean.getPort() + "\");" + SystemHWUtil.CRLF +//补充端口的设置
                "                    " + RequestSendChainVarName + ".setActionPath(\"" + requestInfoBean.getActionPath() + "\");" + SystemHWUtil.CRLF +
                "                    " + RequestSendChainVarName + ".setRequestCookie(\"" + (requestInfoBean.getRequestCookie() == null ? SystemHWUtil.EMPTY : requestInfoBean.getRequestCookie()) + "\");" + SystemHWUtil.CRLF +
                "                    " + RequestSendChainVarName + ".setCustomRequestContentType(\"" + requestInfoBean.getCustomRequestContentType() + "\");" + SystemHWUtil.CRLF +
                "                    " + RequestSendChainVarName + ".setRequestMethod(" + Constant2.REQUEST_METHOD_MAP_DISPLAY_NAME.get(requestInfoBean.getRequestMethodDisplayName()) + ");" + SystemHWUtil.CRLF +
                "                    // " + RequestSendChainVarName + ".setDependentRequest(requestInfoBeanLogin);" + SystemHWUtil.CRLF +
                "                    " + RequestSendChainVarName + ".setCurrRequestParameterName(\"" + requestInfoBean.getCurrRequestParameterName() + "\");" + SystemHWUtil.CRLF +
                "                    " + RequestSendChainVarName + ".setPreRequestParameterName(\"" + requestInfoBean.getPreRequestParameterName() + "\");" + SystemHWUtil.CRLF +
                SystemHWUtil.CRLF;
        return fragment1;
    }

    /***
     * 获取自动生成发送请求代码的 请求参数
     * @param requestInfoBean
     * @param RequestSendChainVarName
     * @return
     */
    public static StringBuffer getRequestParameterFragment(RequestInfoBean requestInfoBean, String RequestSendChainVarName) {
//        List<com.common.bean.ParameterIncludeBean> parameters = requestInfoBean.getParameters();
        Map<String, Object> requestParameers = requestInfoBean.getRequestParameters();
        if (ValueWidget.isNullOrEmpty(requestParameers)) {
            return new StringBuffer();
        }
        String parameterVarName = "parameterMap" + RandomUtils.getRandomStr(4);
        StringBuffer parametersSbuffer = new StringBuffer("                    java.util.TreeMap " + parameterVarName + " = new java.util.TreeMap();//请求参数" + SystemHWUtil.CRLF);
//        int size = parameters.size();
        for (String key : requestParameers.keySet()) {
            String parameterVal = (String) requestParameers.get(key);
            if (beJson(parameterVal)) {
                String randomJson = RandomUtils.getRandomStr(3);
                parametersSbuffer.append(SystemHWUtil.CRLF).append("net.sf.json.JSONObject jsonObject" + randomJson + "=new net.sf.json.JSONObject();").append(SystemHWUtil.CRLF);
                Map<String, Object> parameterValMap = HWJacksonUtils.deSerializeMap(parameterVal, Object.class);
                for (String key3 : parameterValMap.keySet()) {
                    Object jsonVal = parameterValMap.get(key3);
                    parametersSbuffer.append("jsonObject" + randomJson + ".put(\"" + key3 + "\", \"" + jsonVal + "\");").append(SystemHWUtil.CRLF);
                }
                parametersSbuffer.append("" + parameterVarName + ".put(\"" + key + "\", jsonObject" + randomJson + ".toString());").append(SystemHWUtil.CRLF);
            } else {
                parametersSbuffer.append("                    " + parameterVarName + ".put(\"" + key + "\", \"" + (parameterVal).replace("\"", "\\\"") + "\");").append(SystemHWUtil.CRLF);
            }
        }
        parametersSbuffer.append("                    " + RequestSendChainVarName + ".setRequestParameters(" + parameterVarName + ");" + SystemHWUtil.CRLF).append("                    " + RequestSendChainVarName + ".updateRequestBody();" + SystemHWUtil.CRLF).append(SystemHWUtil.CRLF);
        return parametersSbuffer;
    }

    private static boolean beJson(String parameterVal) {
        return !ValueWidget.isNullOrEmpty(parameterVal) && parameterVal.contains("{");
    }

    /***
     * 获取自动生成发送请求代码的 http头部
     * @param requestInfoBean
     * @param RequestSendChainVarName
     * @return
     */
    public static StringBuffer getRequestHeaderFragment(RequestInfoBean requestInfoBean, String RequestSendChainVarName) {
        ListOrderedMap header = requestInfoBean.getHeaderMap();
        StringBuffer headerSbuffer = new StringBuffer();
        if (ValueWidget.isNullOrEmpty(header)) {
            return headerSbuffer;
        }
        headerSbuffer.append(SystemHWUtil.CRLF).append("                    org.apache.commons.collections.map.ListOrderedMap header=new org.apache.commons.collections.map.ListOrderedMap();" + SystemHWUtil.CRLF);

        List keyList = header.keyList();
        int keySize = keyList.size();
        for (int j = 0; j < keySize; j++) {
            headerSbuffer.append("                    header.put(\"" + keyList.get(j) + "\", \"" + header.get(keyList.get(j)) + "\");").append(SystemHWUtil.CRLF);
        }
        headerSbuffer.append("                    " + RequestSendChainVarName + ".setHeaderMap( header);" + SystemHWUtil.CRLF).append(SystemHWUtil.CRLF);
        return headerSbuffer;
    }

    public static String getHttpRequestCode(RequestInfoBean requestInfoBean) {
        String RequestSendChainVarName = "requestInfoBeanOrder" + RandomUtils.getRandomStr(3);
        StringBuffer parametersSbuffer = getRequestParameterFragment(requestInfoBean, RequestSendChainVarName);
        StringBuffer headerSbuffer = getRequestHeaderFragment(requestInfoBean, RequestSendChainVarName);
        String comment = null;
        if (ValueWidget.isNullOrEmpty(requestInfoBean.getCurrentRequestName())) {
            comment = SystemHWUtil.EMPTY;
        } else {
            comment = "// " + requestInfoBean.getCurrentRequestName() + SystemHWUtil.CRLF;
        }
        String prefix = comment + getFragment1(requestInfoBean, RequestSendChainVarName);
        parametersSbuffer.append(headerSbuffer);

        String responseResultVar = "responseResultOrder" + RandomUtils.getRandomStr(3);
        String responseTextVar = "responseOrder" + RandomUtils.getRandomStr(3);
        String httpRequestCase =

                "                    com.common.bean.ResponseResult " + responseResultVar + " = " + RequestSendChainVarName + ".request(); //new RequestPanel.ResponseResult(requestInfoBeanLogin).invoke();" + SystemHWUtil.CRLF +
                        "                    String " + responseTextVar + " = " + responseResultVar + ".getResponseJsonResult();" + SystemHWUtil.CRLF +
                        "                    System.out.println(\"responseText:\" + " + responseTextVar + ");" + SystemHWUtil.CRLF +
                        "                    System.out.println(com.io.hw.json.JSONHWUtil.jsonFormatter( " + responseTextVar + "));";
        parametersSbuffer.append(httpRequestCase);
        return (prefix + parametersSbuffer.toString());
    }

    /***
     * 生成单元测试方法声明
     * @param requestInfoBean
     * @return
     */
    public static String getHttpRequestCodeUnitTest(RequestInfoBean requestInfoBean) {
        String httpSendCode = HttpSocketUtil.getHttpRequestCode(requestInfoBean);
        String httpSendCodeUnitTest = "    @Test" + SystemHWUtil.CRLF +
                "    public void test_sendHttp" + RandomUtils.getRandomStr(3) + "(){" + SystemHWUtil.CRLF +
                httpSendCode + SystemHWUtil.CRLF +
                "        " + SystemHWUtil.CRLF +
                "    }";
        return httpSendCodeUnitTest;
    }

    public static String getPort(RequestInfoBean requestInfoBean) {
        String port = requestInfoBean.getPort();
        if ("80".equals(port)) {//因为80是默认端口
            port = SystemHWUtil.EMPTY;
        }
        /*if (!ValueWidget.isNullOrEmpty(port)) {//判断是否输入了端口号
            port =  port;
        }*/
        return port;
    }

    public static String getContentTypeString(RequestInfoBean requestInfoBean) {
        String contentType = requestInfoBean.getCustomRequestContentType();
        if (!ValueWidget.isNullOrEmpty(contentType)) {
            return contentType;
        }
        if (requestInfoBean.isRequestBodyIsJson()) {
            contentType = SystemHWUtil.CONTENTTYPE_JSON;
        } else {
            contentType = SystemHWUtil.CONTENTTYPE_X_WWW_FORM_URLENCODED;
        }
        if (requestInfoBean.isEncodingCheckbox() && !ValueWidget.isNullOrEmpty(requestInfoBean.getCharset())) {//是否选中了复选框
            contentType = contentType + (";charset=" + requestInfoBean.getCharset());//request.getCharacterEncoding() 获取的就是该编码
        }

        return contentType;
    }

    /***
     * 更基础的代码
     * @param requestInfoBean
     * @return
     */
    public static String getBasicHttpRequestCodeUnitTest(RequestInfoBean requestInfoBean) {
        String requestMethod;
        if (requestInfoBean.isRequestBodyIsJson()) {
            requestMethod = Constant2.HTTP_REQUESTMETHOD_POST;
        } else {
            requestMethod = (String) SystemHWUtil.reverseMap(Constant2.REQUEST_METHOD_MAP).get(requestInfoBean.getRequestMethod());
        }
        String contentType = getContentTypeString(requestInfoBean);
        String getQueryString = null;
        StringBuffer sbuffer = TableUtil3.getRequestBodyFromList(requestInfoBean.getParameters(), requestInfoBean.isUrlEncoding(), requestInfoBean.getCharset(), requestInfoBean.isAutoUrlEncoding());
        if (null != sbuffer) {
            getQueryString = sbuffer.toString();
        }
        String template = "String port = \"" + HttpSocketUtil.getPort(requestInfoBean) + "\";" + SystemHWUtil.CRLF +
                "        String servletAction = \"" + requestInfoBean.getActionPath() + "\";//接口路径" + SystemHWUtil.CRLF +
                "        if (ValueWidget.isNullOrEmpty(servletAction)) {" + SystemHWUtil.CRLF +
                "//            logger.error(\"servletAction is null\");" + SystemHWUtil.CRLF +
                "        }" + SystemHWUtil.CRLF +
                "        if (!servletAction.startsWith(\"/\")) {//兼容\"接口\"文本框前面没有斜杠" + SystemHWUtil.CRLF +
                "            servletAction = \"/\" + servletAction;" + SystemHWUtil.CRLF +
                "        }" + SystemHWUtil.CRLF +
                "        String url = null;" + SystemHWUtil.CRLF +
                "        String serverIp = \"" + requestInfoBean.getServerIp() + "\";" + SystemHWUtil.CRLF +
                "        if (serverIp.startsWith(\"http\")) {" + SystemHWUtil.CRLF +
                "            url = serverIp;" + SystemHWUtil.CRLF +
                "            if (serverIp.startsWith(\"https\")) {" + SystemHWUtil.CRLF +
                "                requestInfoBean.setSsl(true);" + SystemHWUtil.CRLF +
                "            }" + SystemHWUtil.CRLF +
                "        } else {" + SystemHWUtil.CRLF +
                "            url = (requestInfoBean.isSsl() ? \"https://\" : \"http://\")" + SystemHWUtil.CRLF +
                "                    + serverIp;" + SystemHWUtil.CRLF +
                "        }" + SystemHWUtil.CRLF +
                "        if (!ValueWidget.isNullOrEmpty(port)) {" + SystemHWUtil.CRLF +
                "            url = url + port;" + SystemHWUtil.CRLF +
                "        }" + SystemHWUtil.CRLF +
                "        url = url + servletAction;" + SystemHWUtil.CRLF +
                "        String postData = null;" + SystemHWUtil.CRLF +
                "" + SystemHWUtil.CRLF +
                "" + SystemHWUtil.CRLF +
                "        boolean forcePost = false;" + SystemHWUtil.CRLF +
                "        if (!requestInfoBean.isRequestBodyIsJson() && requestInfoBean.getRequestMethod() == Constant2.REQUEST_METHOD_GET && !ValueWidget.isNullOrEmpty(requestInfoBean.getRequestBodyData())) {//GET请求" + SystemHWUtil.CRLF +
                "            String getQueryString = null;" + SystemHWUtil.CRLF +
                "            if (null == requestInfoBean.getParameters()) {" + SystemHWUtil.CRLF +
                "                getQueryString = ValueWidget.getRequestBodyFromMap(requestInfoBean.getRequestParameters(), true);" + SystemHWUtil.CRLF +
                "            } else {" + SystemHWUtil.CRLF +
                "                getQueryString = " + (getQueryString == null ? null : ("\"" + getQueryString + "\"")) + ";" + SystemHWUtil.CRLF +
                "            }" + SystemHWUtil.CRLF +
                "            url += (\"?\" + getQueryString);" + SystemHWUtil.CRLF +
                "        } else {//POST请求" + SystemHWUtil.CRLF +
                "            postData = " + requestInfoBean.getRequestBodyData() + ";" + SystemHWUtil.CRLF +
                "            //注意:如果是POST请求,则不需要url编码" + SystemHWUtil.CRLF +
                "            //自定义的content type的优先级高" + SystemHWUtil.CRLF +
                "" + SystemHWUtil.CRLF +
                "            System.out.println(\"request contentType: " + contentType + "\");" + SystemHWUtil.CRLF +
                "            forcePost = true;" + SystemHWUtil.CRLF +
                "        }" + SystemHWUtil.CRLF +
                "        requestInfoBean.setUrl(url);" + SystemHWUtil.CRLF +
                "" + SystemHWUtil.CRLF +
                "        HttpSocketUtil.setDetail(true);" + SystemHWUtil.CRLF +
                "        //获取请求方法,例如 GET,POST,PUT,DELETE" + SystemHWUtil.CRLF +
                "        byte[] resultJsonBytes = null;" + SystemHWUtil.CRLF +
                "        HttpResponseResult resultArr = null;" + SystemHWUtil.CRLF +
                "        int resCode = -1;" + SystemHWUtil.CRLF +
                "        try {" + SystemHWUtil.CRLF +
                "            //        	Map<String, String> headers =new HashMap<String, String>();" + SystemHWUtil.CRLF +
                "            //        	headers.put(\"Accept-Encoding\", requestInfoBean.getCharset());" + SystemHWUtil.CRLF +
                "            //SystemHWUtil.CONTENTTYPE_JSON+\";charset=\"+requestInfoBean.getCharset()" + SystemHWUtil.CRLF +
                "            //request result(byte[]) ;sessionId;contentType;response code" + SystemHWUtil.CRLF +
                "            long start = System.currentTimeMillis();//接口调用的开始时刻" + SystemHWUtil.CRLF +
                "            //真正发送请求" + SystemHWUtil.CRLF +
                "            resultArr = HttpSocketUtil.httpRequest(url, requestInfoBean.isSsl()," + SystemHWUtil.CRLF +
                "                    forcePost, \"" + requestMethod + "\", postData/*没有进行url编码*/, \"" + contentType + "\"," + SystemHWUtil.CRLF +
                "                    requestInfoBean.getRequestCookie(), requestInfoBean.getHeaderMap(), false/*isWrite2file */, null, " +
                (requestInfoBean.getCharset() == null ? null : "\"" + requestInfoBean.getCharset() + "\"") + "/*newEncoding*/," + SystemHWUtil.CRLF +
                "                    85000, 85000);" + SystemHWUtil.CRLF +
                "            long end = System.currentTimeMillis();" + SystemHWUtil.CRLF +
                "//            setDelta(end - start);//接口调用时间" + SystemHWUtil.CRLF +
                "            Integer respCode = (Integer) resultArr[3];" + SystemHWUtil.CRLF +
                "            resCode = respCode.intValue();" + SystemHWUtil.CRLF +
                "" + SystemHWUtil.CRLF +
                "" + SystemHWUtil.CRLF +
                "            resultJsonBytes = (byte[]) resultArr[0];" + SystemHWUtil.CRLF +
                "            if (ValueWidget.isNullOrEmpty(resultJsonBytes)) {" + SystemHWUtil.CRLF +
                "//                myResult = true;" + SystemHWUtil.CRLF +
                "//                return this;" + SystemHWUtil.CRLF +
                "            }" + SystemHWUtil.CRLF +
                "            String resultJson = new String(resultJsonBytes," + SystemHWUtil.CRLF +
                "                    SystemHWUtil.CHARSET_GBK);" + SystemHWUtil.CRLF +
                "//            System.out.println(\"resultJson(GBK):\" + resultJson);" + SystemHWUtil.CRLF +
                "             String responseJsonResult = new String(resultJsonBytes," + SystemHWUtil.CRLF +
                "                    SystemHWUtil.CHARSET_UTF);" + SystemHWUtil.CRLF +
                "            Map responseJsonMap = com.io.hw.json.JSONHWUtil.getMap(responseJsonResult);" + SystemHWUtil.CRLF +
                "        } catch (Exception e1) {" + SystemHWUtil.CRLF +
                "            e1.printStackTrace();" + SystemHWUtil.CRLF +
                "                /*if(!ValueWidget.isNullOrEmpty(runButton)){" + SystemHWUtil.CRLF +
                "                    runButton.setEnabled(true);" + SystemHWUtil.CRLF +
                "                }*/" + SystemHWUtil.CRLF +
                "//            GUIUtil23.errorDialog(e1);" + SystemHWUtil.CRLF +
                "        }";

        return template;
    }

    public static String getCurlCode(RequestInfoBean requestInfoBean) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("curl -H '" + Constant2.HttP_REQUEST_HEADER_HOST + ": ").append(requestInfoBean.getServerIp()).append("' ");
        ListOrderedMap header = requestInfoBean.getHeaderMap();
        List keyList = header.keyList();
        int keySize = keyList.size();
        //设置请求的header
        for (int j = 0; j < keySize; j++) {
            String key = (String) keyList.get(j);
            if (Constant2.HttP_REQUEST_HEADER_HOST.equals(key)) {
                continue;
            }
            stringBuffer.append("-H '").append(key).append(":").append(header.get(keyList.get(j))).append("' ");
        }

        String url = HttpSocketUtil.getUrlPrefix(requestInfoBean);
        url = url + requestInfoBean.getActionPath();
        if (isHttpGet(requestInfoBean)) {//GET请求
            url = pythonHttpGet(requestInfoBean, stringBuffer, url);
        } else {
            pythonHttpPost(requestInfoBean, stringBuffer);
//            stringBuffer.append("-H '").append(keyList.get(j)).append(":").append(header.get(keyList.get(j))).append("' ");
        }
        stringBuffer.append("--compressed '").append(url).append("'");
        return stringBuffer.toString();
    }

    private static String pythonHttpGet(RequestInfoBean requestInfoBean, StringBuffer stringBuffer, String url) {
        String getQueryString = null;
        if (null == requestInfoBean.getParameters()) {
            getQueryString = ValueWidget.getRequestBodyFromMap(requestInfoBean.getRequestParameters(), true);
        } else {
            StringBuffer sbuffer = TableUtil3.getRequestBodyFromList(requestInfoBean.getParameters(), requestInfoBean.isUrlEncoding(), requestInfoBean.getCharset(), requestInfoBean.isAutoUrlEncoding());
            getQueryString = sbuffer.toString();
        }
        url += ("?" + getQueryString);
        stringBuffer.append("--get ");
        return url;
    }

    private static void pythonHttpPost(RequestInfoBean requestInfoBean, StringBuffer stringBuffer) {
        String postData = requestInfoBean.getRequestBodyData();
        //注意:如果是POST请求,则不需要url编码
        //自定义的content type的优先级高
        String requestContentType = null;
        if (ValueWidget.isNullOrEmpty(requestInfoBean.getCustomRequestContentType())) {
            requestContentType = requestInfoBean.getReqContentType();
        } else {
            requestContentType = requestInfoBean.getCustomRequestContentType();
        }
        //设置post 请求的form 数据
        if (!ValueWidget.isNullOrEmpty(postData)) {
            stringBuffer.append("--data \"").append(postData).append("\" ");
        }
    }

    public static boolean isHttpGet(RequestInfoBean requestInfoBean) {
        boolean isGet = (!requestInfoBean.isRequestBodyIsJson()) && requestInfoBean.getRequestMethod() == Constant2.REQUEST_METHOD_GET /*&& !ValueWidget.isNullOrEmpty(requestInfoBean.getRequestBodyData())*/;
        System.out.println("requestInfoBean.getRequestMethod() :" + requestInfoBean.getRequestMethod());
        System.out.println("isGet :" + isGet);
        return isGet;
    }

    public static String getPythonHttpRequestCode(RequestInfoBean requestInfoBean) {
        String contentType = null;
        if (ValueWidget.isNullOrEmpty(requestInfoBean.getCustomRequestContentType())) {
            contentType = SystemHWUtil.CONTENTTYPE_X_WWW_FORM_URLENCODED_UTF;
        } else {
            contentType = requestInfoBean.getCustomRequestContentType();
        }
        StringBuffer parameterBuffer = new StringBuffer();
        Map<String, Object> requestParameters = requestInfoBean.getRequestParameters();
        for (String key : requestParameters.keySet()) {
            String parameterVal = (String) requestParameters.get(key);
            parameterBuffer.append("'" + key + "':").append("'").append(parameterVal).append("'").append(",");
        }
        String parameterStr = parameterBuffer.toString().replaceAll(",[\\s]*$", SystemHWUtil.EMPTY);
        StringBuffer template = new StringBuffer();
        String url = getUrlPrefix(requestInfoBean);
        String fnName = SystemHWUtil.UNDERLINE + RandomUtils.getRandomStr(3);
        String pythonHttpMethod = null;
        String parameterName = null;
        if (isHttpGet(requestInfoBean)) {
            pythonHttpMethod = "get";
            parameterName = "params";
        } else {
            pythonHttpMethod = "post";
            parameterName = "data";
        }

        template.append("import sys" + SystemHWUtil.CRLF +
                "reload(sys)" + SystemHWUtil.CRLF +
                "sys.setdefaultencoding('utf-8')" + SystemHWUtil.CRLF +
                "import requests" + SystemHWUtil.CRLF +
                "from getConfig import *" + SystemHWUtil.CRLF +
                "import json" + SystemHWUtil.CRLF +
                SystemHWUtil.CRLF +
                "def fn" + fnName + "():" + SystemHWUtil.CRLF +
                "    param={" + parameterStr + "}" + SystemHWUtil.CRLF +
                "    headers = {'Content-Type': '" + contentType + "'}" + SystemHWUtil.CRLF +
                "    r=requests." + pythonHttpMethod + "('" + url + requestInfoBean.getActionPath() + "'," + parameterName + "=param,headers=headers" + SystemHWUtil.CRLF +
                "    # ,proxies=pubsvc_proxies" + SystemHWUtil.CRLF +
                "    )" + SystemHWUtil.CRLF +
                "    print r.status_code" + SystemHWUtil.CRLF +
                "    print r.text" + SystemHWUtil.CRLF +
                "    return r" + SystemHWUtil.CRLF +
                "\n" +
                "if __name__=='__main__':" + SystemHWUtil.CRLF +
                "    r=fn" + fnName + "()");
        return template.toString();
    }

    /***
     * http://gzq.chanjet.com <br />
     * 不包括接口名称
     * @param requestInfoBean
     * @return
     */
    public static String getUrlPrefix(RequestInfoBean requestInfoBean) {
        String port = HttpSocketUtil.getPort(requestInfoBean);
        String url = null;
        String serverIp = requestInfoBean.getServerIp();
        if (serverIp.startsWith("http")) {
            url = serverIp;
            if (serverIp.startsWith("https")) {
                requestInfoBean.setSsl(true);
            }
        } else {
            url = (requestInfoBean.isSsl() ? "https://" : "http://")
                    + serverIp;
        }
        if (!ValueWidget.isNullOrEmpty(port)) {
            url = url + ":" + port;
        }
        return url;
    }
}
