/*********************************************************************
 * Copyright (C) 2013 Tendyron company.                                  *
 * All rights reserved.                           *
 *********************************************************************/
/**
 * @file   JsonParser.java
 *
 * @author chenlong (chenlong@tendyron.com)
 *
 * 负责对通讯数据的解析类
 */

package com.io.hw.json;

import com.string.widget.util.ValueWidget;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class JsonParser {
	public static final String TRADESTATUS = "01"; // 通讯成功的状态值
	public static final String TRADESATUSFAILRUE = "02";// 通讯失败的状态值
	/**
	 * 转输入流为字符串
	 * 
	 * @param input
	 * @return时
	 * @throws Exception
	 */
	public static String formatStreamToString(InputStream input)
			throws Exception {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		String va = "";
		if (input != null) {
			try {
				br = new BufferedReader(new InputStreamReader(input, "utf-8"));
				while ((va = br.readLine()) != null) {
					sb.append(va);
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new Exception(e.getMessage());
			}
		}
		return sb.toString();
	}

	/****
	 * 对任意复杂的json格式数据的解析方法
	 * 
	 * @param target
	 * @return 一个HashMap/ArrayList 混合的复杂对象
	 * @throws JSONException
	 * @throws UnsupportedEncodingException
	 */
	public static Object parserRandomJsonFormat(String target)
			throws JSONException {
		HashMap<String, Object> map = null;
		ArrayList<Object> list = null;
		if (!ValueWidget.isNullOrEmpty(target)) {
			target = target.trim();
			if(!ValueWidget.isNullOrEmpty(target)){
				if ("[".equals(String.valueOf(target.charAt(0)))) {// jsonArray对象
					list = new ArrayList<Object>();
					JSONArray jArray = new JSONArray(target);
					parseStartJSONArrayFormat(jArray, list);
					return list;
				} else if ("{".equals(String.valueOf(target.charAt(0)))) {// jsonObject对象
					JSONObject object = new JSONObject(target);
					map = new HashMap<String, Object>();
					parseMutilJSONObjectFormat(object, map);
					return map;
				}
			}
		}
		return null;
	}

	/***
	 * 解析数组中包含数组、对象、基本数据的格式 如 [[]] 、[{}]、[1]
	 * 
	 * @param jsonArray
	 * @param list
	 * @throws JSONException
	 */
	public static void parseStartJSONArrayFormat(JSONArray jsonArray,
			List<Object> list) throws JSONException {
		HashMap<String, Object> map = null;
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONArray array = jsonArray.getJSONArray(i);
				ArrayList<Object> sublist = new ArrayList<Object>();
				parseStartJSONArrayFormat(array, sublist);
				list.add(sublist);
			} catch (Exception e) {
				try {
					map = new HashMap<String, Object>();
					JSONObject object = jsonArray.getJSONObject(i);
					parseMutilJSONObjectFormat(object, map);
					list.add(map);
				} catch (Exception e1) {
					list.add(jsonArray.get(i));
				}
			}
		}
	}

	/**
	 * 解析json对象中包含的json对象 如{"name":{}}、{"name"：[]}、{"name":"1"}
	 * 
	 * @param jsonObject
	 * @param hashMap
	 * @throws JSONException
	 */
	public static void parseMutilJSONObjectFormat(JSONObject jsonObject,
			HashMap<String, Object> hashMap) throws JSONException {
		JSONArray nameArray = jsonObject.names();
		if(nameArray==null){
			return;
		}
		for (int k = 0; k < nameArray.length(); k++) {
			String attrName = nameArray.getString(k);
			try {
				JSONArray objarray2 = jsonObject.getJSONArray(attrName);
				ArrayList<Object> sublist = new ArrayList<Object>();
				parseStartJSONArrayFormat(objarray2, sublist);
				hashMap.put(attrName, sublist);
			} catch (JSONException e) {
				try {
					JSONObject objson2 = jsonObject.getJSONObject(attrName);
					HashMap<String, Object> submap = new HashMap<String, Object>();
					try {
						parseMutilJSONObjectFormat(objson2, submap);
					} catch (NullPointerException e1) {
						e1.printStackTrace();
					}
					hashMap.put(attrName, submap);
				} catch (Exception e1) {
					hashMap.put(attrName, jsonObject.get(attrName));
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		File file = new File("test.txt");
		InputStream inputStream;
		String va = "[\"adb\",\"cvd\",\"weg\",\"rty\",\"qwe\",\"tyu\",\"poi\"]";
		String vas = "[{\"name\":\"jakchen\",\"sex\":\"man\"},{\"name\":\"shaoyang\",\"sex\":\"longss\"},[{\"teche\":\"java\",\"yuyan\":\"chinese\"},[\"ggggg\",{\"vb\":\"yyuu\"}],\"aaa\",\"ccc\"]]";
		String vasr = "{\"name\":\"jakchen\",\"sex\":\"man\",\"course\":[\"yuwen\",\"shuxue\",\"english\",\"wuli\"]}";
		try {
			Object ob = parserRandomJsonFormat(vasr);
			if (ob instanceof ArrayList) {
				iterateArrayList((ArrayList) ob);
			} else if (ob instanceof HashMap) {
				iterateHashMap((HashMap) ob);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static void iterateArrayList(ArrayList list) {
		if (list != null) {
			for (Object oo : list) {
				if (oo instanceof ArrayList) {
					iterateArrayList((ArrayList) oo);
				} else if (oo instanceof HashMap) {
					iterateHashMap((HashMap) oo);
				}
			}
		}
	}

	public static void iterateHashMap(HashMap<String, Object> hashmap) {
		if (hashmap != null) {
			Set<Entry<String, Object>> set = hashmap.entrySet();
			for (Entry<String, Object> entry : set) {
				Object oo = entry.getValue();
				if (oo instanceof ArrayList) {
					iterateArrayList((ArrayList) oo);
				} else if (oo instanceof HashMap) {
					iterateHashMap((HashMap) oo);
				}
			}
		}
	}
}
