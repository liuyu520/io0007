package com.io.hw.json;

import com.common.util.SystemHWUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.string.widget.util.ValueWidget;
import com.swing.component.ComponentUtil;
import net.sf.json.JSONException;
import org.apache.commons.lang.StringEscapeUtils;

import javax.swing.text.JTextComponent;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class JSONHWUtil {
	public static String addQuotationMarks(String input){
		String result="\""+input.replaceAll("\"", "\\\\\"")+"\"";
		return result;
	}

	public static String jsonFormatter(String uglyJSONString) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(uglyJSONString);
		String prettyJsonString = gson.toJson(je);
		return prettyJsonString;
	}
	
	/***
	 * 格式化返回的json字符串
	 *
	 * @param ta
	 */
	public static void formatJson(JTextComponent ta, boolean isFurther, String oldJson) {
		String json=ta.getText();
		if(ValueWidget.isNullOrEmpty(json)){
			return ;
		}
		if (null == oldJson) {//oldJson用于解析json报错时恢复原始json的
			oldJson = json;
		}
		JsonElement jsonEle = null;
		try {
			JsonParser parser = new JsonParser();
			jsonEle = parser.parse(json);
			if (jsonEle != null && !jsonEle.isJsonNull()) {
				GsonBuilder gb = new GsonBuilder();
				gb.setPrettyPrinting();
				gb.serializeNulls();
				Gson gson = gb.create();
				String jsonStr = gson.toJson(jsonEle);
				if (jsonStr != null) {
					jsonStr = StringEscapeUtils.unescapeJava(jsonStr);
					if(isFurther){
						jsonStr = optimizationJson(jsonStr);
					}

//					jsonStr = gson.toJson(StringEscapeUtils.escapeJava(jsonStr));
					// System.out.println(jsonStr);
					ta.setText(jsonStr);
					if(isFurther){
						formatJson(ta, false, oldJson);
					}
				}
			} else {
				String message="是否缺少开始“{”或结束“}”？";
//				GUIUtil23.warningDialog(message);
				ComponentUtil.appendResult(ta, message, true);
			}
		} catch (Exception ex) {
//			GUIUtil23.warningDialog("非法JSON字符串！");
//			GUIUtil23.errorDialog(ex.getMessage());
			if (!ValueWidget.isNullOrEmpty(oldJson)) {
				ta.setText(oldJson);
				formatJson(ta, false, null);
				ComponentUtil.appendResult(ta, null, false, false);
				ComponentUtil.appendResult(ta, "历史错误信息", true, false);
			}
			
			ComponentUtil.appendResult(ta, null, false,true);
			ComponentUtil.appendResult(ta, ex.getMessage(), true);
		}
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

	public static String[]parse(net.sf.json.JSONArray array){
		int length=array.size();
		String []strs=new String[length];
		for(int i=0;i<length;i++){
			strs[i]=array.getString(i);
		}
		return strs;
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

}
