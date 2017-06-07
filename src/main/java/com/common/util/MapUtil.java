package com.common.util;

import com.string.widget.util.ValueWidget;

import java.util.*;

/**
 * Created by whuanghkl on 17/5/10.
 */
public class MapUtil {
    /***
     * 比较两个Map 是否相同
     * @param map1
     * @param map2
     * @return
     */
    public static boolean compareMapByEntrySet(Map<String, Object> map1, Map<String, Object> map2) {

        if (map1.size() != map2.size()) {
            return false;
        }
        if (map1.size() == map2.size()
                && map1.size() == 0) {//两个Map 都为空
            return true;
        }
        Object tmp1;
        Object tmp2;
        boolean b = false;

        for (Map.Entry<String, Object> entry : map1.entrySet()) {
            if (!map2.containsKey(entry.getKey())) {
                b = false;
                break;
            }
            tmp1 = entry.getValue();
            tmp2 = map2.get(entry.getKey());

            if (null != tmp1 && null != tmp2) {   //都不为null
                if (tmp1.equals(tmp2) || ReflectHWUtils.isSameBySimpleTypes(tmp1, tmp2)) {
                    b = true;
                } else {
                    b = false;
                    break;
                }
                continue;
            }
            if (null == tmp1 && null == tmp2) {  //都为null
                b = true;
                continue;
            }
            b = false;
            break;
        }

        return b;
    }

    /***
     * reverse map Note : value in oldMap must be unique. rever
     *
     * @param oldMap
     * @return
     */
    public static Map reverseMap(Map oldMap) {
        Map newMap = new HashMap<Object, Object>();
        for (Iterator it = oldMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Object, String> entry = (Map.Entry<Object, String>) it
                    .next();
            newMap.put(entry.getValue(), entry.getKey());
        }
        return newMap;
    }

    /***
     * convert Map<String, Object> to Map<String, String>
     *
     * @param oldMap
     * @return
     */
    public static Map<String, String> convertMap(Map<String, Object> oldMap) {
        Map<String, String> newMap = new HashMap<String, String>();
        for (Iterator it = oldMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it
                    .next();
            newMap.put(entry.getKey(), (String) entry.getValue());
        }
        return newMap;
    }

    /***
     * 判断arrayList 中是否包含 apkId
     *
     * @param arrayList
     * @param propertyValue
     * @return
     */
    public static boolean isContainMap(List<HashMap<String, String>> arrayList,
                                       String propertyName, String propertyValue) {
        if (ValueWidget.isNullOrEmpty(arrayList)) {
            return false;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            HashMap<String, String> map = arrayList.get(i);
            String apkTmp = map.get(propertyName);
            if (apkTmp.equalsIgnoreCase(propertyValue)) {
                return true;
            }
        }
        return false;
    }

    /***
     * 去掉了List<Map>中重复的Map
     *
     * @param list
     * @param propertyName
     * @return
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static List<?> uniqueMap(List<?> list, String propertyName)
            throws SecurityException, IllegalArgumentException,
            NoSuchFieldException, IllegalAccessException {
        if (ValueWidget.isNullOrEmpty(list)) {
            return list;
        }
        List resultList = new ArrayList();
        resultList.add(list.get(0));
        for (int i = 1; i < list.size(); i++) {
            Map obj = (Map) list.get(i);
            if (!SystemHWUtil.isContainObject(resultList, propertyName,
                    (String) obj.get(propertyName))) {
                resultList.add(obj);
            }
        }
        return resultList;
    }

    public static Map<Object, Object> updateReverseMap(Object value, Object key, Map indexAttributeValMap) {
        Map reverseMap = SystemHWUtil.reverseMap(indexAttributeValMap);
        reverseMap.put(key, value);
        return SystemHWUtil.reverseMap(reverseMap);
    }

}
