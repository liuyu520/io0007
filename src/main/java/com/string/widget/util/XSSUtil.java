package com.string.widget.util;

public class XSSUtil {
    /***
     * 没有对& 和斜杠/进行处理,因为参数值可能是:<br >
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
        System.out.println("before xss:\t" + input);
        input = input.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
//        input = input.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");//HTML标准中没有转码
        input = input.replaceAll("'", "&#39;");
        input = input.replaceAll("\"", "&quot;");
//        input=input.replaceAll("alert","");
        System.out.println("after xss:\t" + input);
        return input;
    }

    /***
     * 没有对& 和斜杠/进行处理,因为参数值可能是:<br >
     * http://hbjltv.com/info/request?username=huang&password=admin<br>
     * 用于过滤请求参数
     *
     * @param input
     * @return
     */
    public static String deleteXSS(String input) {
        if (ValueWidget.isNullOrEmpty(input)) {
            return input;
        }
        System.out.println("before xss:\t" + input);
        input = input.replaceAll("<", "").replaceAll(">", "");
//        input = input.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");//HTML标准中没有转码
        input = input.replaceAll("'", "");
        input = input.replaceAll("\"", "");
//        input=input.replaceAll("alert","");
        System.out.println("after xss:\t" + input);
        return input;
    }
}
