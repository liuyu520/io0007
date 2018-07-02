package com.common.util;

import com.common.bean.ClientOsInfo;
import com.common.dict.Constant2;
import com.http.util.HttpSocketUtil;
import com.io.hw.json.HWJacksonUtils;
import com.string.widget.util.RegexUtil;
import com.string.widget.util.ValueWidget;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/***
 * 注意:部分方法依赖于tomcat.如果使用jetty,可能部分方法不好使
 * @author huangwei
 * @since 2014年11月24日
 */
public final class WebServletUtil {
    private final static Logger logger = Logger.getLogger(WebServletUtil.class);
	private static final String[] HEADERS_TO_TRY = {
	    "X-Forwarded-For",
	    "Proxy-Client-IP",
	    "WL-Proxy-Client-IP",
	    "HTTP_X_FORWARDED_FOR",
	    "HTTP_X_FORWARDED",
	    "HTTP_X_CLUSTER_CLIENT_IP",
	    "HTTP_CLIENT_IP",
	    "HTTP_FORWARDED_FOR",
	    "HTTP_FORWARDED",
	    "HTTP_VIA",
	    "REMOTE_ADDR",
	    "X-Real-IP"};

	/***
	 * java web //
	 * D:\xxx\eclipse\workspace\.metadata\.plugins\org.eclipse.wst.server
	 * .core\tmp0\wtpwebapps\shop_goods\images //
	 * D:\xxx\eclipse\workspace\shop_goods\ upload
	 *
	 * @param uploadFolderName
	 * @param projectName
	 * @param sContext
	 * @return
	 */
	public static String getUploadedPath(String uploadFolderName,
			String projectName, ServletContext sContext,String webappPath/*src\main\webapp*/ ) {
		String uploadFolder_tmp = null;
		if (uploadFolderName.startsWith(File.separator)) {
			uploadFolder_tmp = uploadFolderName;
		} else {
			uploadFolder_tmp = File.separator + uploadFolderName;// "/upload"
		}
		String realpath = sContext.getRealPath(uploadFolder_tmp);//D:\software\eclipse\workspace2\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\springMVC_upload\attached
		//区别在于是否是使用eclipse 来启动http服务(tomcat)
		String reg=".metadata\\.plugins";
		boolean isContain=realpath.contains(reg);
		System.out.println("[getUploadedPath]isContain:"+isContain);
		if(!isContain){
			webappPath=null;
		}
		// project name ,eg.shop_goods
		projectName = SystemHWUtil.deleteFrontSlash(projectName);
		realpath = SystemHWUtil.getRealPath(realpath, projectName);
		String result=null;//D:\software\eclipse\workspace2\springMVC_upload\src\main\webapp\attached
		if(!ValueWidget.isNullOrEmpty(webappPath)){
			//目的:把"http://localhost:8080/springMVC_upload/upload/image/"变为:
			//"http://localhost:8080/springMVC_upload/src/main/webapp/upload/image/"
			result=realpath.replaceAll("("+SystemHWUtil.deleteAfterSlash(uploadFolderName).replaceAll(File.separator.replace("\\", "\\\\")/* 因为\需要转义 */+"$", "") +"[\\/]?)$", webappPath.replace("\\", "\\\\")+"$1");
		}else{
			result=realpath;
		}
		System.out.println("[WebServletUtil.getUploadedPath]result:"+result);
		return result;
	}

	public static String getUploadedPath(String uploadFolderName,
			String projectName, ServletContext sContext) {
		return getUploadedPath(uploadFolderName, projectName, sContext, null);
	}

	/***
	 * download file
	 *
	 * @param response
	 * @param downloadFilePath
	 * @param filename
	 * @throws IOException
	 */
	public static void downloadFile(HttpServletResponse response,
			File downloadFilePath, String filename) throws IOException {
		FileInputStream fin = new FileInputStream(downloadFilePath);
		// 以流的形式下载文件。
		InputStream fis = new BufferedInputStream(fin);
		byte[] buffer = new byte[fis.available()];
		fis.read(buffer);
		fis.close();
		// 清空response
		response.reset();
		// 设置response的Header
		if (filename == null) {
			filename = downloadFilePath.getName();
		}
        setDownloadFileName(response, filename);
        response.addHeader("Content-Length", "" + downloadFilePath.length());
		OutputStream toClient = new BufferedOutputStream(
				response.getOutputStream());
		response.setContentType("application/octet-stream");
		toClient.write(buffer);
		toClient.flush();
		toClient.close();

	}

    public static void setDownloadFileName(HttpServletResponse response, String filename) {
        response.addHeader(Constant2.CONTENT_DISPOSITION, "attachment;filename="
                + filename);//原来的写法,不知道为什么 new String(filename.getBytes())
    }

	/***
	 * download file
	 *
	 * @param response
	 * @param downloadFilePath
	 * @throws IOException
	 */
	public static void downloadFile(HttpServletResponse response,
			File downloadFilePath) throws IOException {
		downloadFile(response, downloadFilePath, null);
	}

	/***
	 * Get all selected(checked) checkboxes
	 *
	 * @param request
	 * @param sumAttribute
	 *            : The total number of the checkbox
	 * @param prefix
	 *            :
	 * @return
	 */
	public static List<String> getSelectedCheckbox(HttpServletRequest request,
			String sumAttribute, String prefix) {
		List<String> selectResults = new ArrayList<String>();

		int question34Sum = SystemHWUtil.parseObj(request
				.getParameter(sumAttribute));
		for (int i = 0; i < question34Sum; i++) {
			String selectResult = request.getParameter(prefix + (i + 1));
			if (null != selectResult) {
				selectResults.add(selectResult);
			}
		}
		return selectResults;
	}

	/***
	 * Get request query string, form method : post
	 * <br> 直接读取流
	 * @param request
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] getRequestPostBytes(HttpServletRequest request)
			throws IOException {
		int contentLength = request.getContentLength();
		/*当无请求参数时，request.getContentLength()返回-1 */
		if(contentLength<0){
			return null;
		}
		byte buffer[] = new byte[contentLength];
		for (int i = 0; i < contentLength;) {
			int readlen = request.getInputStream().read(buffer, i,
					contentLength - i);
			if (readlen == -1) {
				break;
			}
			i += readlen;
		}
		return buffer;
	}

	/***
	 * Get request query string, form method : post
	 *
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static String getRequestPostStr(HttpServletRequest request)
			throws IOException {
		byte buffer[] = getRequestPostBytes(request);
		if(ValueWidget.isNullOrEmpty(buffer)){
			return null;
		}
		String charEncoding = request.getCharacterEncoding();
		if (charEncoding == null) {
			charEncoding = SystemHWUtil.CHARSET_UTF;
		}
		return new String(buffer, charEncoding);
	}

	/***
	 * Compatible with GET and POST
	 *
	 * @param request
	 * @return : <code>byte[]</code>
	 * @throws IOException
	 */
	public static byte[] getRequestQuery(HttpServletRequest request)
			throws IOException {
		String submitMehtod = request.getMethod();
		String queryString = null;

        if (!submitMehtod.equals("GET")) {// GET
            // POST
            return getRequestPostBytes(request);
        }
        //Get
        queryString = request.getQueryString();
        String charEncoding = request.getCharacterEncoding();// charset
        if (charEncoding == null) {
            charEncoding = SystemHWUtil.CHARSET_UTF;
        }
        return queryString.getBytes(charEncoding);
    }

	/**
	 * Compatible with GET and POST
	 *
	 * @param request
	 * @return : <code>String</code>
	 * @throws IOException
	 */
	public static String getRequestQueryStr(HttpServletRequest request,
			String charEncoding) throws IOException {
		String submitMehtod = request.getMethod();
        if (!submitMehtod.equalsIgnoreCase("post")) {
            return request.getQueryString();
        }
        // form method :Get

			byte[] bytes = getRequestPostBytes(request);
			if(bytes==null){
				return null;
			}
			String charEncoding2 = request.getCharacterEncoding();// charset
			System.out.println("[getRequestQueryStr]charEncoding:"
					+ charEncoding2);
			System.out.println("[getRequestQueryStr]Content-Type:"
                    + getRequestContentType(request));
//			System.out.println("[getRequestQueryStr]contentType:"
//					+ request.getHeader("contentType"));
			// if (charEncoding == null) {
			// charEncoding = "GBK";
			// }
			if(ValueWidget.isNullOrEmpty(charEncoding)){
				charEncoding=charEncoding2;
			}
			if(ValueWidget.isNullOrEmpty(charEncoding)){
				charEncoding=SystemHWUtil.CHARSET_UTF;
			}
			return new String(bytes, charEncoding);
	}

    /***
     * 获取请求的Content-Type
     *
     * @param request
     * @return
     */
    public static String getRequestContentType(HttpServletRequest request) {
        return request.getHeader(Constant2.REQUEST_HEADER_CONTENT_TYPE);
    }

    /***
	 *
	 * @param requestStr : username=huangwei&password=123
	 * @param isTrimBank
	 * @return : {"username":"huangwei","password":"123"}
	 */
	public static Map parseRequestStr(String requestStr, boolean isTrimBank){
		Map requestMap = new HashMap();
		String args[] = requestStr.split("&");
		for (int i = 0; i < args.length; i++) {
			String string = args[i];
			String[] strs = string.split("=", 2);
			if (strs.length > 1) {
				if (isTrimBank) {
					if (!ValueWidget.isNullOrEmpty(strs[1])) {
						strs[1] = strs[1].trim();
					}
				}
				requestMap.put(strs[0], strs[1]);
			}
		}
		return requestMap;
	}

	/***
	 * 把请求体转化为map
	 * @param request
	 * @param charEncoding
	 * @param isTrimBank : 对值进行trim操作
	 * @return
	 * @throws IOException
	 */
	public static Map parseRequest(HttpServletRequest request,
			String charEncoding, boolean isTrimBank) throws IOException {
		String requestStr = getRequestQueryStr(request, charEncoding);
		if(ValueWidget.isNullOrEmpty(charEncoding)){//此处设置编码仅仅是为了URLDecoder.decode
			charEncoding=SystemHWUtil.CURR_ENCODING;
		}
		requestStr=URLDecoder.decode(requestStr,charEncoding);
		if(ValueWidget.isNullOrEmpty(requestStr)||ValueWidget.isNullOrEmpty(requestStr.trim())){
			return null;
		}
//		System.out.println("requestStr:"+requestStr);

		return parseRequestStr(requestStr, isTrimBank);
	}

	/***
	 * 把请求体转化为map
	 * @param request
	 * @param charEncoding
	 * @return
	 * @throws IOException
	 */
	public static Map parseRequest(HttpServletRequest request,
			String charEncoding) throws IOException {
		return parseRequest(request, charEncoding, false);
	}

	/***
	 * Send http request test ok
	 *
	 * @param response
	 * @param bytes
	 *            :字节数组
	 * @param contentType
	 *            :if is null,default value is "application/json"
	 * @param encoding
	 *            ： 编码方式
	 * @throws IOException
	 */
	public static void sendRequestWriter(HttpServletResponse response,
			byte[] bytes, String contentType, String encoding)
			throws IOException {
		response.setContentLength(bytes.length);
		if (contentType == null) {
			contentType = SystemHWUtil.CONTENTTYPE_JSON;
		}
		response.setContentType(contentType);

		PrintWriter printer = response.getWriter();
		printer.println(new String(bytes, encoding));
		printer.flush();
		printer.close();
	}

	/***
	 *
	 * @param response
	 * @param sendData
	 *            :<code>String</code>
	 * @param contentType
	 * @param encoding
	 *            : such as GBK/utf-8
	 * @throws IOException
	 */
	public static void sendRequestWriter(HttpServletResponse response,
			String sendData, String contentType, String encoding)
			throws IOException {
		// response.setContentLength(sendData.getBytes(encoding).length);
		if (encoding == null) {
			System.out.println("sendRequestWriter:encoding/charset can not be null.");
			return;
		}
		byte[] bytes = sendData.getBytes(encoding);
		sendRequestWriter(response, bytes, contentType, encoding);
	}

	/***
	 * test ok
	 *
	 * @param response
	 * @param bytes
	 * @param contentType
	 *            : default value:application/json
     * @param responseEncoding
     * @throws IOException
	 */
	public static void sendRequestStream(HttpServletResponse response,
			byte[] bytes, String contentType, String responseEncoding)
			throws IOException {
		response.setContentLength(bytes.length);
		if (contentType == null) {
			contentType = SystemHWUtil.CONTENTTYPE_JSON;
		}
		response.setContentType(contentType);
		if (responseEncoding != null) {
			response.setCharacterEncoding(responseEncoding);
		}
		ServletOutputStream sos = response.getOutputStream();
		sos.write(bytes, 0, bytes.length);
		sos.flush();
		sos.close();
	}

	/***
	 *
	 * @param response
	 * @param bytes
	 * @throws IOException
	 */
	public static void sendRequest(HttpServletResponse response, byte[] bytes)
			throws IOException {
		sendRequestStream(response, bytes, null,
				response.getCharacterEncoding());
	}

	/***
	 *
	 * @param request
	 *            :HttpServletRequest
     * @param parameterName
     *            :parameter name
	 * @return
	 */
	public static int parseIntFromRequest(HttpServletRequest request,
			String parameterName) {
		String parameterValue = request.getParameter(parameterName);
		return SystemHWUtil.parseObj(parameterValue);
	}

    public static Map getHeaderMap(HttpServletRequest request) {
        return getHeaderMap(request, null);
    }
	/***
	 * 获取header 的所有内容
	 * @param request
	 * @return
	 */
    public static Map getHeaderMap(HttpServletRequest request, String[] headerKeys) {
		Enumeration e= request.getHeaderNames();
        Map<String, String> resultMap = new HashMap<String, String>();
		 for(;e.hasMoreElements();){
			 Object obj=e.nextElement();
			 String key=(String)obj;
             if (!ValueWidget.isNullOrEmpty(headerKeys) && (!SystemHWUtil.isContains(headerKeys, key))) {
                 continue;
             }
//			 String message="key:\t\t"+key;
//			 System.out.println(message);
			 String headerValue=request.getHeader(key);
//			 message="header value:\t"+headerValue;
			 resultMap.put(key, headerValue);
//			 System.out.println(message);
		 }
		 return resultMap;
	}

	/***
	 * 获取Parameter 的所有内容
	 * @param request
	 * @return
	 */
	public static Map getParameterMap(HttpServletRequest request){
		Enumeration e= request.getParameterNames();
		Map resultMap=new HashMap();
		 for(;e.hasMoreElements();){
			 Object obj=e.nextElement();
			 String key=(String)obj;
//			 String message="key:\t\t"+key;
//			 System.out.println(message);
			 String headerValue=request.getParameter(key);
//			 message="Parameter value:\t"+headerValue;
			 resultMap.put(key, headerValue);
//			 System.out.println(message);

		 }
		 return resultMap;
	}

	/***
	 * 针对多个cookie键值对.<br>Map的value为false时才表示删除该cookie
	 * @param request
	 * @param response
	 * @param map
	 */
    public static void rememberMe(HttpServletRequest request, HttpServletResponse response, Map map){
        Cookie[] cookies = request.getCookies();
        for(Object obj:map.keySet()){
            Object val22=(map.get(obj));
            String cookieValue=null;
            boolean isSave=true;
            boolean beShouldSave = beShouldSave(val22);
            if (beShouldSave) {
                isSave=false;
            }
            cookieValue = getCookieValue(obj, val22);
            rememberMe(cookies, response, (String) obj, cookieValue, isSave);
        }
    }

    public static String getCookieValue(Object obj, Object val22) {
        if (beShouldSave(val22)) {
            System.out.println("delete cookie key:"+obj);
            return null;
        }
        String cookieValue = (String) val22;
        System.out.println("remember cookie ,key:"+obj);
        return cookieValue;
    }

    public static boolean beShouldSave(Object val22) {
        return !ValueWidget.isNullOrEmpty(val22) && (val22 instanceof Boolean) && (!SystemHWUtil.parse2Boolean(val22));
    }

	/***
	 * 是否保存cookie
	 * @param response
	 * @param emaiCookieName
	 * @param cookieValue
	 * @param isSave : 是否保存用户名(记住用户名)
	 * @return
	 */
	public static Cookie rememberMe(Cookie[] cookies , /*HttpServletRequest request,*/HttpServletResponse response,String emaiCookieName, String cookieValue,
			boolean isSave) {
//		HttpServletRequest request = ServletActionContext.getRequest();
        if (cookies == null && (!isSave)) {
            return null;
        }

		// Cookie passwordCook = null;
		Cookie emailCook = null;
		if (cookies != null) {
            emailCook = getSelectedCookie(cookies, emaiCookieName, cookieValue);
        }
        boolean flag = (emailCook != null);//是否找到
//		HttpServletResponse response = ServletActionContext.getResponse();
        if (isSave && (!flag)) {
            System.out.println("没有找到 " + emaiCookieName);
				// passwordCook = new Cookie(passwordCookieName, URLEncoder
				// .encode(password, "utf-8"));
				try {
					emailCook = new Cookie(emaiCookieName, URLEncoder.encode(
							cookieValue, "utf-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
        if (isSave) {//保存
            return updateCookie(response, emailCook);
        }

        if (flag) {//不保存,并且找到了cookie
            System.out.println("让 cookie 失效");
            emailCook.setMaxAge(0);
            emailCook.setPath("/");//设置cookie时,设置path为根路径 ,如果不设置path ,则无法让cookie 失效
            response.addCookie(emailCook);
        }

        return emailCook;
    }

    /***
     * 更新cookie,主要是更新有效期
     * @param response
     * @param emailCook
     * @return
     */
    public static Cookie updateCookie(HttpServletResponse response, Cookie emailCook) {
        emailCook.setMaxAge(10000000);//单位是秒,所以大概115 天
        emailCook.setPath("/");//设置cookie时,设置path为根路径
        response.addCookie(emailCook);
        System.out.println("保存cookie:" + emailCook.getValue());
        return emailCook;
    }

    public static Cookie getSelectedCookie(Cookie[] cookies, String emaiCookieName, String cookieValue) {
        System.out.println("cookie 不为空");
        Cookie selectedCookie = null;
        for (Cookie c : cookies) {
            if (emaiCookieName.equals(c.getName())) {
                selectedCookie = c;
                break;
            }
        }
        if (null == selectedCookie) {
            return null;
        }
        System.out.println("找到了 " + emaiCookieName);
        System.out.println("cookie的值为 " + selectedCookie.getValue());
        if ((!ValueWidget.isNullOrEmpty(cookieValue))) {
            try {
                selectedCookie.setValue(URLEncoder.encode(cookieValue, SystemHWUtil.CHARSET_UTF));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return selectedCookie;
    }

    public static void setSessionIdCookie(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        setSessionIdCookie(request, response, 96);//先设置cookie有效期为4天
    }
    /***
     * 解决关闭浏览器之后需要重新登录的问题
     *
     * @param request
     * @param response
     * @throws UnsupportedEncodingException
     */
    public static void setSessionIdCookie(HttpServletRequest request, HttpServletResponse response, int hours) throws UnsupportedEncodingException {
        //解决关闭浏览器之后需要重新登录的问题
        Cookie c = new Cookie(Constant2.COOKIE_KEY_JSESSIONID, URLEncoder.encode(request.getSession().getId(), "utf-8"));
        c.setPath("/");
        c.setMaxAge(hours * 60 * 60);
        response.addCookie(c);
    }

    /***
	 * 指定字符编码，无损地读取文本文件.推荐!
	 *
	 * @param charset
	 *            : 字符编码
	 * @return
	 * @throws IOException
	 */
	public static StringBuffer getFullContent3(HttpServletRequest request, String charset,boolean isCloseInputStream)
			throws IOException {
		InputStream in=request.getInputStream();
		StringBuffer sbuffer = new StringBuffer();
		InputStreamReader inReader;
		// 设置字符编码
		if (ValueWidget.isNullOrEmpty(charset)) {
			charset = SystemHWUtil.CURR_ENCODING;
		}
		inReader = new InputStreamReader(in, charset);
		char[] ch = new char[SystemHWUtil.BUFF_SIZE_1024];
		int readCount = 0;
		while ((readCount = inReader.read(ch)) != -1) {
			sbuffer.append(ch, 0, readCount);
		}
		if(isCloseInputStream){
			inReader.close();
			in.close();
		}
		return sbuffer;
	}

	public static String getUploadedFilePath(HttpServletRequest request,
											 String uploadFolderName, String webappPath/* src\main\webapp */) {
		// String
		// envMode=DictionaryParam.get(Constant2.DICTIONARY_GROUP_env_info,
		// "mode");
		// if(envMode==null){
		// System.out.println("please setting env_info mode");
		// return null;
		// }
		// System.out.println("envMode:"+envMode);
		// if(envMode.equals(Constant2.ENVIRONMENT_MODE_PRODUCTION)){
		// webappPath=null;//生产模式
		// }
		String realpath = getUploadPath(request, uploadFolderName, request
				.getSession().getServletContext(), webappPath);
		File savefile = new File(realpath);
		if (!savefile.exists()) {
			savefile.mkdirs();
		}
		return savefile.getAbsolutePath();
	}

	public static String getUploadedPath(String uploadFolderName,
			String projectName, HttpServletRequest request) {
		String uploadFolder_tmp = null;
		if (uploadFolderName.startsWith("/")) {
			uploadFolder_tmp = uploadFolderName;
		} else {
			uploadFolder_tmp = "/" + uploadFolderName;// "/upload"
		}
		String realpath = request.getSession().getServletContext()
				.getRealPath(uploadFolder_tmp);
		// project name ,eg.shop_goods
		projectName = SystemHWUtil.deleteFrontSlash(projectName);
		realpath = SystemHWUtil.getRealPath(realpath, projectName);
		return realpath;
	}

	/***
	 *
	 * @param request
	 * @param uploadFolderName
	 * @return
	 */
	public static String getUploadPath(HttpServletRequest request,
									   String uploadFolderName, ServletContext sContext, String webappPath/*
																				 * src
																				 * \
																				 * main
																				 * \
																				 * webapp
																				 */) {
		// project name:"/demo_channel_terminal"
		String projectName = request.getContextPath();// value:/shop_goods or
														// "/springMVC_upload"
		String uploadPath = WebServletUtil.getUploadedPath(
				uploadFolderName/* "upload\" */, projectName, sContext,
				webappPath/* "src\main\webapp\" */);
		return uploadPath;
	}

	/***
	 *
	 * @param request
	 * @param uploadFolderName
	 *            :eg.WEB-INF/download
	 * @param newFileName
	 * @return
	 */
	public static File getUploadedFilePath(HttpServletRequest request,
										   String uploadFolderName, String newFileName, String webappPath/*
																		 * src\main
																		 * \
																		 * webapp
																		 */) {
		String realpath = getUploadPath(request, uploadFolderName, request
				.getSession().getServletContext(), webappPath);
		File savefile = new File(new File(realpath), newFileName);
		if (!savefile.getParentFile().exists()) {
			savefile.getParentFile().mkdirs();
		}
		return savefile;
	}

	/**
	 * 获取basePath
	 *
	 * @param request
	 * @return
	 */
	public static String getUrlBasePath(HttpServletRequest request) {
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path
				+ "/";
		return basePath;
	}

	/**
	 * 获取客户端ip地址(可以穿透代理)
	 *
	 * @param request
	 * @return
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/***
	 * 获取客户端ip地址(可以穿透代理)
	 * @param request
	 * @return
	 */
	public static String getClientIpAddress(HttpServletRequest request) {
	    for (String header : HEADERS_TO_TRY) {
	        String ip = request.getHeader(header);
	        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
	            return ip;
	        }
	    }
	    return request.getRemoteAddr();
	}
	/***
	 * 获取客户端ip地址(可以穿透代理)
	 * @param request
	 * @return
	 */
	public static String getClientIpAddr(HttpServletRequest request) {  
	    String ip = request.getHeader("X-Forwarded-For");  
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("Proxy-Client-IP");  
	    }  
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("WL-Proxy-Client-IP");  
	    }  
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
	    }  
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("HTTP_X_FORWARDED");  
	    }  
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");  
	    }  
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("HTTP_CLIENT_IP");  
	    }  
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("HTTP_FORWARDED_FOR");  
	    }  
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("HTTP_FORWARDED");  
	    }  
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("HTTP_VIA");  
	    }  
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("REMOTE_ADDR");  
	    }  
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getRemoteAddr();  
	    }  
	    return ip;  
	}
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if (null != ip && !"".equals(ip.trim())
				&& !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
        if (!(null != ip && !"".equals(ip.trim())
                && !"unknown".equalsIgnoreCase(ip))) {
            return request.getRemoteAddr();
        }

			// get first ip from proxy ip
			int index = ip.indexOf(',');
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
	public static String getRealPath(HttpServletRequest request) {
		return getRealPath(request,  "\\");
	}
	/**
	 * 获取相对地址的绝对地址
	 * 
	 * @param request
	 * @param relativePath
	 * @return
	 */
	public static String getRealPath(HttpServletRequest request, String relativePath) {
		return request.getSession().getServletContext().getRealPath(relativePath);
	}
	/***
	 * 当移动端(手机或Pad)访问网页时获取移动端操作系统信息
	 * @param request
	 * @return
	 */
	public static ClientOsInfo getMobileOsInfo(HttpServletRequest request){
		String userAgent=request.getHeader("user-agent");
		if(ValueWidget.isNullOrEmpty(userAgent)){
			userAgent=request.getHeader("User-Agent");
		}
		ClientOsInfo info= HeaderUtil.getMobilOS(userAgent);
        if (null == info) {
//            logger.error("userAgent:" + userAgent);
        }
        if (ValueWidget.isNullOrEmpty(info.getOsType())) {
            info.setOsType(request.getParameter("osType"));//HeaderUtil 中有常量
        }
        //设备标示（device token or clientid） 用于消息推送时,定位设备
        String deviceId = request.getParameter("deviceId");
        info.setUserAgent(userAgent);
        info.setDeviceId(deviceId);

        //request header
        Map headers = getHeaderMap(request, new String[]{"deviceId", "osVersion", "osType", "deviceInfo", "appId"});
        info.setRequestHeaderStr(HWJacksonUtils.getJsonP(headers));
        return info;
	}
	/***
	 * spring MVC下载文件设置的Content-Disposition
	 * @param isInline
	 * @param fileName
	 * @return
	 */
	public static String getContentDisposition(boolean isInline,String fileName){
		String downloadType=null;
        downloadType = Constant2.CONTENT_DISPOSITION_ATTACHMENT;
        if(isInline){
			downloadType=Constant2.CONTENT_DISPOSITION_INLINE;
		}
		if(ValueWidget.isNullOrEmpty(fileName)){
			fileName="name_not_specified";
		}
		String format=downloadType+";filename=\""+fileName+"\"";
		return format;
	}
	/***
	 * 下载文件(或内联显示)时设置Content-Disposition
	 * @param isInline
	 * @param fileName
	 * @param response
	 */
	public static void setDownloadContentDisposition(boolean isInline,String fileName, HttpServletResponse response){
		response.addHeader(Constant2.CONTENT_DISPOSITION, WebServletUtil.getContentDisposition(isInline, fileName));
	}
	
	public static String dealWithJsessionid(String password){
		if(!ValueWidget.isNullOrEmpty(password)){
			password = password.replaceAll(";jsessionid=[\\w]+$", "");
		}
		return password;
	}
	
	public static String getRequestBodyFromMap(Map parameterMap) {
		if(ValueWidget.isNullOrEmpty(parameterMap)){
			return null;
		}
        StringBuffer sbuffer = new StringBuffer();
        for(Object obj:parameterMap.keySet()){
        	Object valObj=parameterMap.get(obj);
            String key = (String) obj;
            if (null != valObj && (valObj instanceof String)) {
                try {
                    valObj = HttpSocketUtil.decryptHttpParameter((String) valObj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                key = HttpSocketUtil.decryptHttpParameter(key);
            } catch (Exception e) {
                e.printStackTrace();
            }
            sbuffer.append(key).append("=").append(valObj == null ? SystemHWUtil.EMPTY : valObj).append("&");
        }
        String result=sbuffer.toString();
        return result.replaceAll("(.*)&[\\s]*$", "$1");
    }
	/***
	 * 用于转化request.getParameterMap()
	 * @param requestParams
	 * @return
	 */
	public static Map getParamMap(Map requestParams) {

        Map<String, String> params = new HashMap<String, String>();

        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {

            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }
        return params;
    }
	/***
	 * eg:http://127.0.0.1:8080/tv_mobile/
	 * @param request
	 * @return : http://127.0.0.1:8080/tv_mobile/
	 */
	public static String getBasePath(HttpServletRequest request){
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://"
				+ request.getServerName() + SystemHWUtil.COLON + request.getServerPort()
				+ path + "/";
		return basePath;
	}
	/***
	 * 服务器是否是本机
	 * @param request
	 * @return
	 */
	public static boolean isLocalIp(HttpServletRequest request){
        if (null == request) {
            return false;
        }
		String ip=WebServletUtil.getClientIpAddr(request);
		return ip.equals("127.0.0.1")||ip.equals("localhost")||ip.equals("0:0:0:0:0:0:0:1");
	}

    /***
     * 是否需要URL编码<br>
     * http请求参数中含有& 或者空格,或者中文字符,则必须URL编码
     *
     * @param value
     * @return
     */
    public static boolean isShouldURLEncode(String value) {
        return !ValueWidget.isNullOrEmpty(value) && (value.contains("&") || RegexUtil.contain2(value, "[\\s]")
                || SystemHWUtil.isHasChinses(value));
    }

    /***
     * 全局替换
     * @param realPath2
     * @return
     */
    public static String escapePath(String realPath2) {
        return realPath2.replace("\\", "\\\\");
    }

    public static String getFullUrl(HttpServletRequest request, String relativePath, String finalFileName) {
        String fullUrl;
        String prefixPath = request.getRequestURL().toString().replaceAll(request.getServletPath(), "");
        if (!prefixPath.endsWith("/") && (!relativePath.startsWith("/"))) {
            prefixPath = prefixPath + "/";
        }
        if (relativePath.endsWith("/") && !ValueWidget.isNullOrEmpty(finalFileName)) {
            relativePath = relativePath + finalFileName;//upload/image/20150329170823_2122015-03-23_01-42-03.jpg
        }
        fullUrl = prefixPath + relativePath;
        return fullUrl;
    }

    /***
     * @param request
     * @param relativePath
     * @param finalFileName
     * @return
     */
    public static String getRelativeUrl(HttpServletRequest request, String relativePath, String finalFileName) {
        String rootPath = request.getContextPath();//有可能为空字符串
        if (!ValueWidget.isNullOrEmpty(rootPath) && !rootPath.endsWith("/")) {
            rootPath = rootPath + "/";
        }
        if (relativePath.endsWith("/")) {
            relativePath = getRelativePath(relativePath, finalFileName);
        }
        return rootPath + relativePath;
    }

    public static String getRelativePath(String relativePath, String finalFileName) {
        if (!relativePath.endsWith("/")) {
            relativePath = relativePath + "/";
        }
        if (relativePath.endsWith("/")) {
            relativePath = relativePath + finalFileName;//upload/image/20150329170823_2122015-03-23_01-42-03.jpg
        }
        return relativePath;
    }


    /***
     * 组装http应答response
     * @param header
     * @param body
     * @return
     */
    public static String assembleResponsePackets(String header, String body) {
        return header + SystemHWUtil.CRLF_WINDOW +
                SystemHWUtil.CRLF_WINDOW +
                body + SystemHWUtil.CRLF_LINUX;
    }

    public static void writeResponse(HttpServletResponse response, Object content) throws IOException {
        if (content instanceof String) {
            writeResponse(response, (String) content);
        } else {
            writeResponse(response, HWJacksonUtils.getJsonP(content));
        }
    }

    public static void writeResponse(HttpServletResponse response, String content) throws IOException {
        response.setCharacterEncoding(SystemHWUtil.CHARSET_UTF);
        response.setContentType(SystemHWUtil.RESPONSE_CONTENTTYPE_JSON_UTF);
        PrintWriter out = response.getWriter();
        out.print(content);
        out.flush();
        out.close();
    }

    public static void noCache(HttpServletResponse response) {
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
    }

    /***
     * 如果请求特别耗时,请求响应时间超过4 (时间可配置DecideUseCacheWhenOvertimeFilter.secondLimitOvertime) 秒,则设置缓存<br />
     *  see com/common/web/filter/RequestbodyFilter.java 中 cacheWhenOvertime <br />
     *  目的:接口访问特别慢时自动切换到缓存,但是缓存也只能使用 RequestbodyFilter.timesCanUseCacheWhenOvertime 次 <br />
     *  如果结果是false,则不缓存
     * @param body
     * @param httpServletRequest
     * @param servletPath
     */
    public static void backUpResponseResult(Object body, HttpServletRequest httpServletRequest, String servletPath) {
        /*try { // 必须注释掉,否则RequestRetrySendFilter 中没法实现重试
            BaseResponseDto baseResponseDto = BaseResponseDto.parseObject((String) body);
            if (null != baseResponseDto && (!baseResponseDto.result)) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        /*if (null != body && httpServletRequest instanceof HttpPutFormContentRequestWrapper) {
            HttpPutFormContentRequestWrapper requestWrapper = (HttpPutFormContentRequestWrapper) httpServletRequest;
            requestWrapper.put(servletPath, body);
        }*/
    }
}
