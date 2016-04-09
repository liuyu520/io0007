package com.string.widget.util;

public class XSSUtil {
    /***
     * 没有对& 处理,因为参数值可能是:<br >
     * http://hbjltv.com/info/request?username=huang&password=admin<br>
     * 用于过滤请求参数
     *
     * @param input
     * @return
     */
    public static String cleanXSS(String input) {
        if (ValueWidget.isNullOrEmpty(input)) {
            return input;
        }
        input = input.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
//        input = input.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");//HTML标准中没有转码
        input = input.replaceAll("'", "&#39;");
        input = input.replaceAll("\"", "&quot;");
//        input=input.replaceAll("alert","");
        return input;
    }
}
