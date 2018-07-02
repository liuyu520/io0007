package com.common.util;

import com.string.widget.util.ValueWidget;

import java.util.HashMap;
import java.util.Map;

/***
 * 枚举工具类<br />
 * added at 2018-04-19   中国标准时间 下午3:48:40
 */
public class EnumUtil {
    // 根据 label 获取枚举
    public static <E extends Enum<E>> E getByLabel(Class<E> enumClass, String propertyName, String name) {
//enumClass.getEnumConstants() 将获取枚举类的所有实例
        for (E e : enumClass.getEnumConstants()) {
            String propertyVal = (String) ReflectHWUtils.getGetterVal(e, "get" + ValueWidget.capitalize(propertyName));
            if (propertyVal.equals(name)) {
                return e;
            }
        }
        return null;
    }

    public static <E extends Enum<E>> E getByCode(Class<E> enumClass, String propertyName, int code) {
        //enumClass.getEnumConstants() 将获取枚举类的所有实例
        for (E e : enumClass.getEnumConstants()) {
            Number propertyVal = (Number) ReflectHWUtils.getGetterVal(e, "get" + ValueWidget.capitalize(propertyName));
            if (propertyVal.intValue() == code) {
                return e;
            }
        }
        return null;
    }

    /***
     * {验房=4, 经纪人下架=12, 发出=3, 已评价=8, 请求验房=1, 取消=10, 只剩下视频链接没有上传=7, 接单=2, 验房结束=5, 房主确认=6, 拒单=11}
     * @param enumClass
     * @param codePropName
     * @param labelPropName
     * @param <E>
     * @return
     */
    public static <E extends Enum<E>> Map<String, Object> getLabelCodeMap(Class<E> enumClass, String codePropName, String labelPropName) {
        Map<String, Object> labelCodeMap = new HashMap<>();
        for (E e : enumClass.getEnumConstants()) {
            Object code = ReflectHWUtils.getGetterValNoPrefix(e, codePropName);
            String label = (String) ReflectHWUtils.getGetterValNoPrefix(e, labelPropName);
            labelCodeMap.put(label, code);
        }
        return labelCodeMap;
    }

    /***
     * {1=请求验房, 2=接单, 3=发出, 4=验房, 5=验房结束, 6=房主确认, 7=只剩下视频链接没有上传, 8=已评价, 10=取消, 11=拒单, 12=经纪人下架}
     * @param enumClass
     * @param codePropName
     * @param labelPropName
     * @param <E>
     * @return
     */
    public static <E extends Enum<E>> Map<Object, String> getCodeLabelMap(Class<E> enumClass, String codePropName, String labelPropName) {
        Map<Object, String> codeLabelMap = new HashMap<>();
        for (E e : enumClass.getEnumConstants()) {
            Object code = ReflectHWUtils.getGetterValNoPrefix(e, codePropName);
            String label = (String) ReflectHWUtils.getGetterValNoPrefix(e, labelPropName);
            codeLabelMap.put(code, label);
        }
        return codeLabelMap;
    }

}
