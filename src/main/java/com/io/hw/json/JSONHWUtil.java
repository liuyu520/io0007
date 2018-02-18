package com.io.hw.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.bean.json.ParseJsonInfo;
import com.common.util.RequestUtil;
import com.common.util.SystemHWUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.string.widget.util.ValueWidget;
import com.swing.dialog.toast.ToastMessage;
import net.sf.json.JSONException;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.List;

public class JSONHWUtil {
    protected static final Logger logger = Logger.getLogger(JSONHWUtil.class);

	public static String addQuotationMarks(String input){
		String result="\""+input.replaceAll("\"", "\\\\\"")+"\"";
		return result;
	}

    public static String jsonFormatter(String uglyJSONString) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        try {
            JsonElement je = jp.parse(uglyJSONString);
            String prettyJsonString = gson.toJson(je);
            return prettyJsonString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 最大限度地当做json 字符串解析<br />
     * 方案:"[...]" 转为[...],并且其中的\"-->"<br />
     * "{...}" 转为{...},并且其中的\"-->"<br />
     * 深度格式化json,采用递归
     * @param jsonStr
     * @return
     */
    public static String toJson(String jsonStr) {
        if (ValueWidget.isNullOrEmpty(jsonStr)) {
            return null;
        }
        if (ValueWidget.isHTMLWebPage(jsonStr)) {
            return jsonStr;
        }

        if (!jsonStr.trim().startsWith("{")) {
            return jsonStr;
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONObject.parseObject(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return jsonStr;
        }

        ParseJsonInfo parseJsonInfo = toJsonObjectRecursive(jsonObject);
        if (parseJsonInfo.isHasString()) {
            return jsonObject.toJSONString();
        }

        return jsonStr;
    }

    /**
     * 最大限度地当做json 字符串解析<br />
     * 方案:"[...]" 转为[...],并且其中的\"-->"<br /
     *
     * @param jsonObject
     * @return
     */
    public static ParseJsonInfo toJsonObjectRecursive(JSONObject jsonObject) {
//        JSONObject jsonObject=JSONObject.parseObject(jsonStr);
        Set<Map.Entry<String, Object>> set = jsonObject.entrySet();
//        boolean hasString=false;
        ParseJsonInfo parseJsonInfo = new ParseJsonInfo();
        for (Map.Entry<String, Object> entry : set) {
//            System.out.println("key :" + entry.getKey());
            Object val = entry.getValue();
            String key = entry.getKey();
//            System.out.println("value :" + val);
            if (val instanceof String) {
                String valString = (String) val;
                valString = valString.trim();
                if (valString.contains(":") && valString.contains("\"")) {//判断是否是json字符串,比如{"username":"whuang"}
//                    hasString=true;
                    if (valString.startsWith("[")) {//"[...]"--数组
                        JSONArray jsonArray = JSONObject.parseArray(valString);
                        parseArray(parseJsonInfo, jsonArray);
                        jsonObject.put(key, jsonArray);
                    } else {//--对象
                        try {
                            ParseJsonInfo parseJsonInfoTmp = toJsonObjectRecursive(JSONObject.parseObject(valString));
                            jsonObject.put(key, parseJsonInfoTmp.getJsonObject());
                        } catch (com.alibaba.fastjson.JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    parseJsonInfo.setHasString(true);
                }
            } else if (val instanceof JSONObject) {
                ParseJsonInfo parseJsonInfoTmp = toJsonObjectRecursive((JSONObject) val);
                if (parseJsonInfoTmp.isHasString()) {
                    parseJsonInfo.setHasString(true);
                }
            }
//            System.out.println("value type :" +(val instanceof String));
//            String val2=JSONHWUtil.unescapeJava((String)val);
//            System.out.println("val2 :" + val2);

        }
        parseJsonInfo.setJsonObject(jsonObject);
        return parseJsonInfo;
    }

    public static void parseArray(ParseJsonInfo parseJsonInfoParent, JSONArray jsonArray) {
        int size = jsonArray.size();
        for (int i = 0; i < size; i++) {
            Object object = jsonArray.get(i);
            if (object instanceof JSONObject) {
                ParseJsonInfo parseJsonInfoTmp = toJsonObjectRecursive((JSONObject) object);
                if (parseJsonInfoTmp.isHasString()) {
                    parseJsonInfoParent.setHasString(true);
                    jsonArray.set(i, parseJsonInfoTmp.getJsonObject());
                }

            }
        }
    }
	
	/***
	 * 格式化返回的json字符串
	 *
	 * @param ta
	 */
    public static void formatJson(JTextComponent ta, boolean isFurther, String oldJson, boolean isSuppressWarnings) {
        String json=ta.getText();
		if(ValueWidget.isNullOrEmpty(json)){
			return ;
		}
		if (null == oldJson) {//oldJson用于解析json报错时恢复原始json的
			oldJson = json;
		}
		try {
            String jsonStr = formatJson(json);
            if (null != jsonStr) {
                ta.setText(jsonStr);
                    }
				String message="是否缺少开始“{”或结束“}”？";
//				GUIUtil23.warningDialog(message);
//				ComponentUtil.appendResult(ta, message, true);
                if (!isSuppressWarnings) {
                    ToastMessage.toast(message, 2000, Color.red);
                }
		} catch (Exception ex) {
//			GUIUtil23.warningDialog("非法JSON字符串！");
//			GUIUtil23.errorDialog(ex.getMessage());
            if (isFurther && !ValueWidget.isNullOrEmpty(oldJson)) {
                ta.setText(oldJson);
                formatJson(ta, false, null, isSuppressWarnings);
//				ComponentUtil.appendResult(ta, null, false, false);
//				ComponentUtil.appendResult(ta, "历史错误信息", true, false);
                if (!isSuppressWarnings) {
                    ToastMessage.toast("历史错误信息", 2000, Color.red);
                }
            }

//			ComponentUtil.appendResult(ta, null, false,true);
//			ComponentUtil.appendResult(ta, ex.getMessage(), true);
            if (!isSuppressWarnings) {
                ToastMessage.toast(ex.getMessage(), 2000, Color.red);
            }

		}
	}

    public static String formatJson(String json) {
        return formatJson(json, false);
    }

    public static String formatJson(String json, boolean unescapeJava) {
        JsonElement jsonEle;
        JsonParser parser = new JsonParser();
        jsonEle = parser.parse(json);
        if (jsonEle != null && !jsonEle.isJsonNull()) {
            GsonBuilder gb = new GsonBuilder();
            gb.setPrettyPrinting();
            gb.serializeNulls();
            Gson gson = gb.create();
            String jsonStr = gson.toJson(jsonEle);
            if (jsonStr != null) {
                if (unescapeJava) {
                    jsonStr = StringEscapeUtils.unescapeJava(jsonStr);
                }
/*if(isFurther){
jsonStr = optimizationJson(jsonStr);
                }*/

//					jsonStr = gson.toJson(StringEscapeUtils.escapeJava(jsonStr));
                // System.out.println(jsonStr);
                return (jsonStr);
/*if(isFurther){
formatJson(ta, false, jsonStr, isSuppressWarnings);
}*/
            }
            return json;
        }
        return null;
    }

	/***
	 * 优化json字符串
	 *
	 * @param jsonStr
	 * @return
	 */
	public static String optimizationJson(String jsonStr) {
		jsonStr = jsonStr.replaceAll("\"(\\{.+\\})\"([\\s]*[,}])", "$1$2");
		jsonStr = jsonStr.replaceAll("\"(\\[.+\\])\"([\\s]*[,}])", "$1$2");
		return jsonStr;
	}

   /*

    //会把四个斜杠 当成一个斜杠
//		jsonStr = jsonStr.replaceAll(, "$1$2");
//		String regex="\"(\\[.+\\])\"([\\s]*[,}])";
//        jsonStr=jsonStr.replace("\\","\\\\");//因为正则表达式的替换,本身会减少双引号的层级,
		jsonStr = pureStringToJson(jsonStr, "\"(\\{[^}]+\\})\"([\\s]*[,}])");
        jsonStr=jsonStr.replace("\\","\\\\");//因为正则表达式的替换,本身会减少双引号的层级,
        return pureStringToJson(jsonStr, "\"(\\[.+\\])\"([\\s]*[,}])");
   public static String pureStringToJson(String jsonStr, String regex) {

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(jsonStr);
        StringBuffer sb = new StringBuffer();
        boolean result = m.find();
        while (result) {
            String str = m.group(1);//此时已经减掉了一层双引号
            if(str.contains(":")){
                m.appendReplacement(sb, *//*JSONHWUtil.unescapeJava(*//*str*//*)*//*+m.group(2));
            }

            result = m.find();
        }
        m.appendTail(sb);
        return sb.toString();
    }*/

    /***
     * 功能:减掉一层引号的引用,比如" -->直接去掉<br />
     * \"-->"<br />
     * \\"-->\"<br />
     * "{\"amount\":{\"freeStorage\":\"{\\" username\\":\\"whuang\\"}\",\"period\":12,\"storage\":0},\"priceType\":1,\"trialPeriod\":0}"<br />
     * 改为:<br />
     * {amount:{freeStorage:{\ username\:\whuang\},period:12,storage:0},priceType:1,trialPeriod:0}
     * @param jsonStr
     * @return
     */
    public static String unescapeJava(String jsonStr) {
        jsonStr = StringEscapeUtils.unescapeJava(jsonStr);
        return SystemHWUtil.delDoubleQuotation(jsonStr);//去掉最边上的双引号
    }

	public static String[]parse(net.sf.json.JSONArray array){
		int length=array.size();
		String []strs=new String[length];
		for(int i=0;i<length;i++){
			strs[i]=array.getString(i);
		}
		return strs;
	}

	/***
     * json库是json_lib
     * @param jsonInput
     * @param clazz
     * @return
     */
    public static Object parseObject(String jsonInput, Class clazz) {
        net.sf.json.JSONObject js = net.sf.json.JSONObject.fromObject(jsonInput);
        return net.sf.json.JSONObject.toBean(js, clazz);
    }

    /***
     * convert json string to Map;e.g:{errorMessage=系统异常，请稍后再试！, id=, status=02, errorCode=error_default, method=}
     *
     * @param jsonResult
     * @return
     * @throws UnsupportedEncodingException
     * @throws JSONException
     * @throws org.json.JSONException
     */
    public static Map<String, String> getMap(String jsonResult)
            throws UnsupportedEncodingException, JSONException,
            org.json.JSONException {
        if (ValueWidget.isNullOrEmpty(jsonResult)) {
            return null;
        }
        //callback({"auth_code":"v39hXq"})
        jsonResult = deleteCallback(jsonResult, "callback");
        Map<String, String> resultMap = null;
        if (!jsonResult.trim().startsWith("{")) {
            return null;
        }
        Map obj = (Map) com.io.hw.json.JsonParser.parserRandomJsonFormat(jsonResult);
        if (ValueWidget.isNullOrEmpty(obj)) {
            return null;
        }
        List resultList = (List) obj.get("resultList");
        if (ValueWidget.isNullOrEmpty(resultList)) {
            resultMap = obj;
        } else {
            resultMap = new TreeMap<String, String>();//TreeMap 有固定的顺序
            for (int i = 0; i < resultList.size(); i++) {
                Map mp_tmp = (Map) resultList.get(i);
                parseMap(resultMap, mp_tmp);
            }
        }

        return resultMap;

    }

	/***
	 * convert mp_tmp to Map
	 * @param targetMap
	 * @param mp_tmp
	 *            :Map or List
	 */
	public static void parseMap(Map<String, String> targetMap, Object mp_tmp) {

		if (mp_tmp instanceof Map) {
			Map map_tmp = (Map) mp_tmp;
			for (Iterator it = map_tmp.entrySet().iterator(); it.hasNext(); ) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it
						.next();
				Object value_tmp = entry.getValue();
				String key_tmp = entry.getKey();
				if (value_tmp instanceof String) {
					targetMap.put(key_tmp, (String) value_tmp);
				} /*
				 * else if (value_tmp == JSONObject.NULL) { value_tmp = "";
				 * targetMap.put(key_tmp, (String) value_tmp); }
				 */ else {
					parseMap(targetMap, value_tmp);
				}
			}
		} else if (mp_tmp instanceof List) {
			List list_tmp = (List) mp_tmp;
			for (int i_tmp = 0; i_tmp < list_tmp.size(); i_tmp++) {
				Object tmp = list_tmp.get(i_tmp);
				if (!(tmp instanceof String)) {//if not is String
					parseMap(targetMap, list_tmp.get(i_tmp));
				}
			}
		}
	}

	/***
	 * callback({"auth_code":"v39hXq"}) -->{"auth_code":"v39hXq"}<br>
	 * 删除"\r\n "
	 * @param input
	 * @param callbackName
	 * @return
	 */
	public static String deleteCallback(String input,String callbackName){
//		System.out.println(input);
		String result=input.replaceAll("^"+callbackName + "\\((.*\\})\\);?$", "$1").replace("\\r\\n", SystemHWUtil.EMPTY);
//		System.out.println(result);
		return result;
	}

	public void test_addQuotationMarks() {
		String input = "abc:\"ccc\"";
		System.out.println(input);
//		JSONObject js=JSONObject.
		System.out.println(addQuotationMarks(input));
	}

    public static String parameter2Json(String selectContent) {
        Map requestMap = new HashMap();
        RequestUtil.setArgumentMap(requestMap, selectContent, true, null, null, false, true);
        return HWJacksonUtils.getJsonP(requestMap);
    }

}
