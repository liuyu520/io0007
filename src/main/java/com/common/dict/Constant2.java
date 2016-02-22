package com.common.dict;

import org.apache.commons.collections.map.ListOrderedMap;

public class Constant2 {
	/***
	 * TLV 中表示长度的那个字节的上限
	 */
	public static final byte TLV_LENGTH_LIMIT_BYTE=(byte) 0x80;
	/***
	 * TLV 中表示长度的那个字节的上限
	 */
	public static final int TLV_LENGTH_LIMIT_INT=(int) 0x80;
	/***
	 * 群发
	 */
	public static final String PUSH_MESSAGE_MODE_BULK="bulk";
	/***
	 * 定点推送
	 */
	public static final String PUSH_MESSAGE_MODE_POINT2POINT="point";
	/***
	 * 手机操作系统类型
	 */
	public static final String APP_OS_TYPE_ANDROID="android";
	/***
	 * 手机操作系统类型
	 */
	public static final String APP_OS_TYPE_IOS="ios";
	public static final String FIX_PREFIX_COMMAND = "cmd /c ";
	/***
	 * 不是查询,而是点击[上一页]或[下一页],
	 */
	public static final String PAGEFLAG_NOT_QUERY="not_query";
	/***
	 * spring MVC 的跳转
	 */
	public static final String SPRINGMVC_REDIRECT_PREFIX="redirect:/";
	public static final String FLAG_LOGIN_SUCCESS="success";
	
	
	public static final String SRC_MAIN_WEBAPP="src\\main\\webapp\\";
	/***
	 * PC端
	 */
	public static final int DEVICE_TYPE_PC=2;
	/***
	 * 手机端
	 */
	public static final int DEVICE_TYPE_MOBILE=3;
	/***
	 * pad
	 */
	public static final int DEVICE_TYPE_PAD=4;
	/***
	 * 全局设置
	 */
	public static final String DICTIONARY_GROUP_GLOBAL_SETTING="global_setting";
	/***
	 * 登录成功的标识
	 */
	public static final String SESSION_KEY_LOGINED_FLAG="logined";
	public static final String COOKIE_KEY_USEREMAIL="userEmail";//记住用户名
	/***
	 * cookie中密码的key
	 */
	public static final String COOKIE_KEY_PASSWORD="password23";
	/***
	 * cookie中"自动登录"的key
	 */
	public static final String COOKIE_KEY_ISAUTO_LOGIN="isauto22";
	/***
	 * cookie中"自动登录"的value
	 */
	public static final String COOKIE_VALUE_ISAUTO_LOGIN="auto";
	public static final String JAR_META_INF = "META-INF";
	public static final String JAR_META_INF_SLASH = "/META-INF";
	public static final String SHAREDPICDIVISION=";;";
	/***
	 * http的请求方式:GET
	 */
	public static final String HTTP_REQUESTMETHOD_GET="GET";
	/***
	 * http的请求方式:POST
	 */
	public static final String HTTP_REQUESTMETHOD_POST="POST";
	/***
	 * 内联显示,内嵌显示
	 */
	public static final String CONTENT_DISPOSITION_INLINE="inline";
	/***
	 * 作为单独的附件下载
	 */
	public static final String CONTENT_DISPOSITION_ATTACHMENT="attachment";
	public static final String CONTENT_DISPOSITION="Content-Disposition";
	public static final String JSON_RETURN_START="start";
	/***
	 * 本次查询返回多少条数据
	 */
	public static final String JSON_RETURN_LENGTH="length2";
	/***
	 * 总共有多少条
	 */
	public static final String JSON_RETURN_SUM="sum2";
	/***
	 * 是否返回完<br>true:返回完;false:没有返回完
	 */
	public static final String JSON_RETURN_OVER="over";
	/***
	 * 当前是第几页
	 */
	public static final String JSON_RETURN_CURRENTPAGE="currentpage";
	/***
	 * 每页最多显示多少条
	 */
	public static final String JSON_RETURN_RECORDSPERPAGE="records_perpage";
	/***
	 * 放置上传的文件的目录
	 */
	public static final String UPLOAD_FOLDER_NAME="upload";
	/***
	 * 请选择文件.
	 */
	public static final String ERROR_UPLOAD_FILE_NO_SELECTED_FILE="请选择文件.";
	public static final long UPLOAD_SIZE_DEFAULT=1000000L;
	public static final String EDIT_FLAG="isEdit";
	public static final String REQUEST_HEADER_CONTENT_TYPE="Content-Type";
	public static final String REPLY_AT="@";
	public static final String HOSTS_PATH="C:\\Windows\\System32\\drivers\\etc\\hosts";
	/***
	 * 新闻的状态:打开
	 */
	public static final int NEWS_STATUS_ON=1;
	/***
	 * 新闻的状态:关闭
	 */
	public static final int NEWS_STATUS_OFF=2;
	/***
	 * 新闻的状态:打开
	 */
	public static final int STATUS_ON=NEWS_STATUS_ON;
	/***
	 * 新闻的状态:关闭
	 */
	public static final int STATUS_OFF=NEWS_STATUS_OFF;
	/***
	 * 登录结果的json中的key
	 */
	public static final String LOGIN_RESULT_KEY="result";
	public static final String RESPONSE_KEY_ERROR_MESSAGE="errorMessage";
	public static final String RESPONSE_RIGHT_RESULT="{\"result\":true}";
	public static final String RESPONSE_WRONG_RESULT="{\"result\":false}";
	public static final String KEY_REQUESTTARGET = "requestTarget";
	/***
	 * 记录条数
	 */
	public static final String RECORD_TOTAL_SUM="记录条数:%s";
	public static final int STATISTICS_TYPE_DAY=1;
	public static final int STATISTICS_TYPE_WEEK=2;
	public static final int STATISTICS_TYPE_MONTH=3;
	/***
	 * 进入页面
	 */
	public static final int LOGS_ACCESS_TYPE_INTO=1;
	/***
	 * 离开页面
	 */
	public static final int LOGS_ACCESS_TYPE_LEAVE=2;
	/***
	 * 1:访问页面;<br>2:离开页面 ;<br>3,上传文件;<br>4,下载文件<br>5,删除记录<br>6,修改记录
	 */
	public static final int LOGS_ACCESS_TYPE_UPLOAD_FILE=3;
	/***
	 * 1:访问页面;<br>2:离开页面 ;<br>3,上传文件;<br>4,下载文件<br>5,删除记录<br>6,修改记录
	 */
	public static final int LOGS_ACCESS_TYPE_DOWNLOAD_FILE=4;
	/***
	 * 1:访问页面;<br>2:离开页面 ;<br>3,上传文件;<br>4,下载文件<br>5,删除记录<br>6,修改记录
	 */
	public static final int LOGS_ACCESS_TYPE_DELETE=5;
	/***
	 * 1:访问页面;<br>2:离开页面 ;<br>3,上传文件;<br>4,下载文件<br>5,删除记录<br>6,修改记录<br>7,增加记录
	 * <br />8,登录<br />9,注销/退出
	 */
	public static final int LOGS_ACCESS_TYPE_UPDATE=6;
	/***
	 * 1:访问页面;<br>2:离开页面 ;<br>3,上传文件;<br>4,下载文件<br>5,删除记录<br>6,修改记录<br>7,增加记录
	 * <br />8,登录<br />9,注销/退出
	 */
	public static final int LOGS_ACCESS_TYPE_ADD = 7;
	/**
	 * 1:访问页面;<br>2:离开页面 ;<br>3,上传文件;<br>4,下载文件<br>5,删除记录<br>6,修改记录
	 * <br />8,登录<br />9,注销/退出
	 */
	public static final int LOGS_ACCESS_TYPE_LOGIN = 8;
	/**
	 * 1:访问页面;<br>2:离开页面 ;<br>3,上传文件;<br>4,下载文件<br>5,删除记录<br>6,修改记录
	 * <br />8,登录<br />9,注销/退出
	 */
	public static final int LOGS_ACCESS_TYPE_LOGOUT = 9;
	public static final String SESSION_KEY_LOGINED_USER="user";
	/***
	 * HTTP请求方式:GET
	 */
	public static final int REQUEST_METHOD_GET=0;
	/***
	 * HTTP请求方式:POST
	 */
	public static final int REQUEST_METHOD_POST=2;
	public static final int REQUEST_METHOD_PUT=3;
	public static final int REQUEST_METHOD_DELETE=4;
	public static final int REQUEST_METHOD_OPTIONS=5;
	public static final int REQUEST_METHOD_HEAD=6;
	public static final ListOrderedMap REQUEST_METHOD_MAP=new ListOrderedMap();
	/***
	 * 删除CIA返回的无用的前缀
	 */
	public static final String REGEX_DELETE_FRONT_OF_CIA_RESPONSE_STRICT="^.*<<[\\s]*\"[\\s]*(.*)\"[\\s]*$";
	public static final String REGEX_DELETE_FRONT_OF_CIA_REQUEST_STRICT ="^.*>>[\\s]*\"[\\s]*(.*)\"[\\s]*$";
	public static final String REGEX_DELETE_FRONT_OF_CIA_REQUEST_LOOSE ="^.*>>[\\s]*(.*)[\\s]*$";
	public static final String REGEX_DELETE_FRONT_OF_CIA_RESPONSE_LOOSE="^.*<<[\\s]*(.*)[\\s]*$";
	/***
	 * 修改当前目录
	 */
	public static final String COMMAND_CWD = "cwd";
	/***
	 * 静态资源在本地(ipa文件内部)
	 */
	public static final String OS_STATIC_RES_LOC_LOCAL = "local";
	/***
	 * 静态资源在远程服务器
	 */
	public static final String OS_STATIC_RES_LOC_REMOTE = "remote";
	/***
	 * 电脑任务栏的高度
	 */
	public static final int SWING_TASK_BAR_HEIGHT=40;
	/***
	 * 登录成功
	 */
	public static final int LOGIN_RESULT_SUCCESS = 1;
	public static final String FINDTXTRESULTBEAN_FIELD = "findTxtResultBean";
	public static final String DAO = "Dao";
	public static final String KEY_MODEL_ATTRIBUTE = "modelAttribute";
	public static final String DB_ID = "id";
	public static final String YES = "yes";
	public static final String KEY_LABEL = "label";
	public static final String KEY_CURRENT_TIME = "currentTime";
	public static final String Slash = "/";
	public static final String stub_folder_no_Slash = "stub";
    /***
     * stub/
     */
    public static final String stub_folder = stub_folder_no_Slash + Constant2.Slash;
    public static final String stub_file_Suffix = ".json";
	/***
	 * 用户名不存在
	 */
	public static final int LOGIN_RESULT_USERNAME_INVALID = 2;
	/***
	 * 用户名为空
	 */
	public static final int LOGIN_RESULT_USERNAME_EMPTY = 3;
	/***
	 * 密码为空
	 */
	public static final int LOGIN_RESULT_PASSWORD_EMPTY = 4;
	/***
	 * 登录失败
	 */
	public static final int LOGIN_RESULT_FAILED = 5;
	/***
	 * 缺少参数
	 */
	public static final int ERROR_CODE_NEED_ARGUMENT = 7;
	/***
	 * 注册时,用户名已经存在
	 */
	public static final int REGISTER_RESULT_USERNAME_EXIST = 10;
	/***
	 * 注册时邮箱不能为空
	 */
	public static final int REGISTER_RESULT_EMAIL_EMPTY = 11;
	/***
	 * 用户还没有登录
	 */
	public static final int MODIFY_PASS_RESULT_NOT_LOGINED_YET = 21;
	/***
	 * 修改密码时,两次密码不能相同
	 */
	public static final int MODIFY_PASS_RESULT_PASS_SAME = 22;
	/***
	 * 新密码为空
	 */
	public static final int MODIFY_PASS_RESULT_NEW_PASS_EMPTY = 23;
	/***
	 * 旧密码不对
	 */
	public static final int MODIFY_PASS_RESULT_OLD_PASS_WRONG = 24;
	/***
	 * 需要更新手机端的密码
	 */
	public static final int GET_PASS_RESULT_NEED_REFRESH = 30;
	/***
	 * 不需要更新手机端的密码
	 */
	public static final int GET_PASS_RESULT_ALREADY_NEWEST = 31;
	public static final String COOKIE_KEY_JSESSIONID = "JSESSIONID";

	static {
		REQUEST_METHOD_MAP.put("GET", REQUEST_METHOD_GET);
		REQUEST_METHOD_MAP.put("POST", REQUEST_METHOD_POST);
		REQUEST_METHOD_MAP.put("PUT", REQUEST_METHOD_PUT);
		REQUEST_METHOD_MAP.put("DELETE", REQUEST_METHOD_DELETE);
		REQUEST_METHOD_MAP.put("OPTIONS", REQUEST_METHOD_OPTIONS);
		REQUEST_METHOD_MAP.put("HEAD", REQUEST_METHOD_HEAD);
	}
}
