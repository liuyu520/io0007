package com.test;

import com.common.bean.FindTxtResultBean;
import com.common.bean.ParameterIncludeBean;
import com.common.util.AssembleUtil;
import com.common.util.RequestUtil;
import com.common.util.SystemHWUtil;
import com.io.hw.file.util.FileUtils;
import com.string.widget.util.RegexUtil;
import com.string.widget.util.ValueWidget;
import com.swing.table.TableUtil3;
import com.time.util.TimeHWUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.fail;

public class UnitTest4 {

    /*public static void main(String[] args) {
        char buf[]={'a','b','c'};

        perm(buf,0,buf.length-1);
    }*/
    public static void perm(char[] buf, int start, int end) {
        if (start == end) {//当只要求对数组中一个字母进行全排列时，只要就按该数组输出即可（特殊情况）
            for (int i = 0; i <= end; i++) {
                System.out.print(buf[i]);
            }
            System.out.println();
        } else {//多个字母全排列（普遍情况）
            for (int i = start; i <= end; i++) {//（让指针start分别指向每一个数）
                char temp = buf[start];//交换数组第一个元素与后续的元素
                buf[start] = buf[i];
                buf[i] = temp;

                perm(buf, start + 1, end);//后续元素递归全排列

                temp = buf[start];//将交换后的数组还原
                buf[start] = buf[i];
                buf[i] = temp;
            }
        }
    }

    /*public static int getSum(int n){
        if(n<2){
            return 1;
        }else{
            return n+getSum(n-1);
        }
    }*/
    public static int getSum(int n) {
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum = sum + (i + 1);
        }
        return sum;
    }

    /***
     * 阶乘
     *
     * @param n
     * @return
     */
    public static int arrayArrange(int n) {
        int power = 1;
        for (int i = 0; i < n; i++) {
            power = power * (i + 1);
        }
        return power;
    }

    public static void main(String[] args) {
        up(1);
    }

    public static void up(int n) {
        System.out.println(String.format("rank1 %d", n));
        if (n < 4)
            up(n + 1);
        System.out.println(String.format("rank2 %d", n));
    }

    //	@Test
    public void test() {
        fail("Not yet implemented");
    }

    //	@Test
    public void test_findStr() {
        FindTxtResultBean findTxtResultBean = SystemHWUtil.findStr("abab", "ab", 2);
        System.out.println(findTxtResultBean);
    }

    @Test
    public void test_pid() {
        String string = "   a\nb\n\n\n";
        System.out.println(string);
//		string=string.replaceAll("\n+$","");
//		System.out.println(string);
        System.out.println(RegexUtil.splitPlus(string, (String) "\n", "\""));
    }

    @Test
    public void test_dataFormat() {
        String dataStr = "Dec 17 12:51:02";//Jul 10 15:48:40
        System.out.println(TimeHWUtil.formatDateTime(TimeHWUtil.getUSDate4Str(dataStr)));
    }

    @Test
    public void test_arg() {
        Assert.assertEquals(1, AssembleUtil.getAssembleSum(1));
        Assert.assertEquals(3, AssembleUtil.getAssembleSum(2));
        Assert.assertEquals(7, AssembleUtil.getAssembleSum(3));
        Assert.assertEquals(15, AssembleUtil.getAssembleSum(4));
        Assert.assertEquals(31, AssembleUtil.getAssembleSum(5));
    }

    @Test
    public void test_factorial() {
        System.out.println(SystemHWUtil.factorial(4, 4));
        Assert.assertEquals(4 * 3, SystemHWUtil.factorial(4, 2));
        Assert.assertEquals(4, SystemHWUtil.factorial(4, 1));
        Assert.assertEquals(5 * 4, SystemHWUtil.factorial(5, 2));
    }

    @Test
    public void test_factorialaa() {
        String base[] = new String[]{"a", "b", "c", "d"};
        List<String> result = AssembleUtil.assemble(base, 0, false);

        System.out.println(result.size());
        System.out.println(result);
    }

    @Test
    public void test_factorialaa22() {
        String base[] = new String[]{"a", "b", "c", "d"};
        List<String> result = AssembleUtil.assemble(base, 0, false, "#");

        System.out.println(result.size());
        System.out.println(result);
    }

    @Test
    public void test_sedY() {
        String base[] = new String[]{"a", "bc", "c", "d"};
        String result = RegexUtil.sedY("a", base, new String[]{"1a", "2b", "3c", "4d"});
        System.out.println(result);
    }

    @Test
    public void test_sort() {
        String source = "cda";
        System.out.println(ValueWidget.sortStr(source));
        Assert.assertEquals("acd", ValueWidget.sortStr(source));
        Assert.assertEquals("abc", ValueWidget.sortStr("cba"));
        Assert.assertEquals("abc", ValueWidget.sortStr("cab"));
        Assert.assertEquals("ab", ValueWidget.sortStr("ba"));
        Assert.assertEquals("a", ValueWidget.sortStr("a"));
        Assert.assertEquals("c", ValueWidget.sortStr("c"));
        Assert.assertEquals("abcd", ValueWidget.sortStr("acbd"));
    }

    @Test
    public void test_aheadElement() {
        String base[] = new String[]{"a", "b", "c", "d"};
        String[] now = SystemHWUtil.aheadElement(base, 2);
        System.out.println(now.length);
        Assert.assertArrayEquals(new String[]{"a", "b", "c", "d"}, SystemHWUtil.aheadElement(base, 0));
        Assert.assertArrayEquals(new String[]{"b", "a", "c", "d"}, SystemHWUtil.aheadElement(base, 1));
        Assert.assertArrayEquals(new String[]{"c", "a", "b", "d"}, SystemHWUtil.aheadElement(base, 2));
        Assert.assertArrayEquals(new String[]{"d", "a", "b", "c"}, SystemHWUtil.aheadElement(base, 3));
    }

    @Test
    public void test_arrayArrange() {
        System.out.println(SystemHWUtil.arrayArrange(3));
        Assert.assertEquals(1, SystemHWUtil.arrayArrange(1));
        Assert.assertEquals(2, SystemHWUtil.arrayArrange(2));
        Assert.assertEquals(6, SystemHWUtil.arrayArrange(3));
        Assert.assertEquals(24, SystemHWUtil.arrayArrange(4));
        Assert.assertEquals(120, SystemHWUtil.arrayArrange(5));
    }

    @Test
    public void test_getSum() {
        int n = 10;
        System.out.println(getSum(n));
    }

    @Test
    public void test_arrayArrange2() {
        int n = 4;
        System.out.println(arrayArrange(n));
    }

    //	@Test
    public void test_split() {
        String input = "abc";
        String[] strs = input.split("");
        System.out.println(SystemHWUtil.formatArr(strs, ","));
    }

    @Test
    public void test_getParameterIncludeBean() {
        ParameterIncludeBean parameterIncludeBean = TableUtil3.getParameterIncludeBean("abc");
        Assert.assertEquals("abc", parameterIncludeBean.getKey());
        Assert.assertEquals(null, parameterIncludeBean.getValue());
    }

    /*public void test_interface(){
        Map<String,Object> param=new HashMap<String,Object>();
        param.put("username","whuang");

        HTTPClient httpClient=new HTTPClient();
        httpClient.setUrl("http://login.kunlunsoft.com:80/internal_api/v1/login");
        httpClient.setParam(param);//如果设置了param,则使用POST请求
        httpClient.setMethod();//GET/POST/PUT/DELETE
        httpClient.setAssertion(new Assertion(){
            public boolean assert(ResponseInfo responseInfo){
                return false;
            }
        })
        ResponseInfo responseInfo=httpClient.send();//有一个参数,表示请求方式
        int respCode=responseInfo.getResponseCode();
        responseInfo.getHeaderFields()
        String contentType=responseInfo.getHeaderField("size");


    }*/
    @Test
    public void test_list_all_stub() {
        String rootPath = "E:\\apache-tomcat-7.0.53-windows-x64_3\\apache-tomcat-7.0.53\\webapps\\tv_mobile\\stub";


    }

    @Test
    public void test_byts() {
        byte b = -128;
        String binaryStr = Integer.toBinaryString(b);
        int c = Integer.parseInt("10000001", 2) - 256;
        System.out.println(c);
        System.out.println(binaryStr);

    }


    @Test
    public void test_stub() {

//		System.out.println(ValueWidget. getNginxDispatch(targetUrl,"www.yhskyc.com/"));
        System.out.println(10000000 / (60 * 60 * 24));
    }

    @Test
    public void test_bak() {
        String configFilePath = System.getProperty("user.home") + File.separator + ".share_http.properties";
        System.out.println(configFilePath);
        String parentDir = SystemHWUtil.getParentDir(configFilePath);//C:\Users\Administrator
        String baseName = SystemHWUtil.getFileSimpleName(configFilePath) + "_bak";//.share_http.properties
        System.out.println(parentDir);
        System.out.println(baseName);
        File[] files = FileUtils.getFilesByPathPrefix(new File(parentDir), baseName);
        Arrays.sort(files);
        System.out.println(files.length);
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i]);
        }
        int limit = 5;
        if (files.length > limit) {
            for (int i = 0; i < files.length - limit; i++) {
                File file = files[i];
                System.out.println("delete:" + file.getAbsolutePath());
                file.delete();
            }
        }
        String dateFormat = TimeHWUtil.formatDate(new Date(), TimeHWUtil.YYYYMMDD_NO_LINE);
        String newFileName = baseName + dateFormat;
        File newFile = new File(parentDir, newFileName);
        System.out.println(newFile.exists());
        System.out.println(newFileName);
    }

    @Test
    public void test_uniqueInt() {
        List<Integer> integers = new ArrayList<Integer>();
        integers.add(1);
        integers.add(2);
        integers.add(2);
        integers.add(129);
        integers.add(129);
        integers.add(130);
        integers.add(1);
        System.out.println(integers);
        List<Integer> uniqueIntegers = SystemHWUtil.uniqueInt(integers);
        System.out.println(uniqueIntegers);
    }

    @Test
    public void test_BigDecimal() {
        BigDecimal big = new BigDecimal(4);
        System.out.println(big);
        System.out.println(big.toString());
        System.out.println(big.compareTo(new BigDecimal(0)));

        BigDecimal big2 = new BigDecimal(-4);
        System.out.println(big2);
        System.out.println(big2.toString());
        System.out.println(big2.compareTo(new BigDecimal(0)));

        BigDecimal big3 = new BigDecimal("4.127");
        System.out.println(big3);
        System.out.println(big3.toPlainString());
        System.out.println(big3.compareTo(new BigDecimal(0)));


        BigDecimal big4 = new BigDecimal(0);
        System.out.println(big4);
        System.out.println(big4.toPlainString());
        System.out.println(big4.compareTo(new BigDecimal(0)));
    }

    @Test
    public void test_queryString() {
        String url = "http://store.kunlunsoft.com/order/startPay?orderId=INTE2016031701837=&payType=chanpay&orgId=90001001032&callack= ";
        Map<String, Object> argMap = RequestUtil.parseQueryString(url);
//        Assert.assertEquals(argMap.toString(), "{orgId=90001001032, callack= , payType=chanpay, orderId=INTE2016031701837=}");
        Assert.assertTrue(argMap.get("orgId").equals("90001001032"));
        Assert.assertTrue(argMap.get("orderId").equals("INTE2016031701837="));
        System.out.println(argMap);
    }

    @Test
    public void test_Hump_marking() {
        String input = "aaa_bbb_ccc_";
        Assert.assertEquals(RegexUtil.humpMarking(input), "aaaBbbCcc");
    }
}
