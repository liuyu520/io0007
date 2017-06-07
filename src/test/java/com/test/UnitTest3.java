package com.test;

import com.cmd.dos.hw.util.CMDUtil;
import com.common.bean.ClientOsInfo;
import com.common.bean.Son;
import com.common.bean.Student;
import com.common.bean.Woman;
import com.common.dict.Constant2;
import com.common.util.*;
import com.encrypt.CustomEncrypt;
import com.http.util.HttpSocketUtil;
import com.io.hw.file.util.FileUtils;
import com.io.hw.json.HWJacksonUtils;
import com.string.widget.util.RegexUtil;
import com.string.widget.util.ValueWidget;
import com.test.bean.Person2;
import com.test.bean.Student2;
import com.time.util.TimeHWUtil;
import junit.framework.Assert;
import org.junit.Test;

import java.io.*;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class UnitTest3 {
    public static final String name;

    static {
        name = "whuang";
    }

    public static String encodeMD5(byte[] sourceBytes) {
        if (null == sourceBytes) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest code = MessageDigest.getInstance("MD5");
            code.update(sourceBytes);
            byte[] bs = code.digest();
            for (int i = 0; i < bs.length; i++) {
                int v = bs[i] & 0xFF;
                if (v < 16) {
                    sb.append(0);
                }
                sb.append(Integer.toHexString(v));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String encodeMD5(String str) {
        if (null == str) {
            return null;
        }
        try {
            return encodeMD5(str.getBytes("GBK"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public final static String MD5Encoder(String s, String charset) {
        try {
            byte[] btInput = s.getBytes(charset);
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < md.length; i++) {
                int val = ((int) md[i]) & 0xff;
                if (val < 16) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(val));
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static Map parseQueryString(String notifyUrl) {
        String extra_common = notifyUrl.split("\\?")[1];
        String[] strs = extra_common.split("&");
        Map map = new HashMap();
        for (int i = 0; i < strs.length; i++) {
            String[] keyVal = strs[i].split("=");
            String val = null;
            if (keyVal.length > 1) {
                val = keyVal[1];
            } else {
                val = "";
            }
            map.put(keyVal[0], val);
        }
        return map;
    }

    //	@Test
    public void test_isContains() {
        String[] input = null;// new String[]{"a","baa","c"};
        // System.out.println(SystemHWUtil.isContains(input,"ba"));
        Assert.assertEquals(false, SystemHWUtil.isContains(input, "ba"));
        Assert.assertEquals(false, SystemHWUtil.isContains(input, "b"));
        Assert.assertEquals(true, SystemHWUtil.isContains(input, "baa"));
        Assert.assertEquals(true, SystemHWUtil.isContains(input, "c"));
    }

    //	@Test
    public void test_isContains22() {
        String[] input = new String[]{"a", "baa", "c", "c"};
        // System.out.println(SystemHWUtil.isContains(input,"ba"));
        Assert.assertEquals(true, SystemHWUtil.isContains(input, "ba", 0));
        Assert.assertEquals(true, SystemHWUtil.isContains(input, "b", 0));
        Assert.assertEquals(true, SystemHWUtil.isContains(input, "baa", 1));
        Assert.assertEquals(true, SystemHWUtil.isContains(input, "c", 2));
    }

    //	@Test
    public void test_uniqueStr() {
        String[] input = new String[]{"a", "baa", "c", "c"};
        // System.out.println(SystemHWUtil.uniqueStr(input));
        Assert.assertEquals(false, SystemHWUtil.uniqueStr(input));

        input = new String[]{"a", "baa", "c"};
        Assert.assertEquals(true, SystemHWUtil.uniqueStr(input));
        input = new String[]{"a", "baa", "c", "c "};
        Assert.assertEquals(true, SystemHWUtil.uniqueStr(input));
    }

    // @Test
    public void test_guolv() {
        String[] input = new String[]{"c", "a", "baa", "c", "c1", "c"};
        System.out.println(SystemHWUtil.formatArr(SystemHWUtil.guolv(input),
                " "));
    }

    // @Test
    public void test_mergeArray2() {
        String[] str2 = new String[]{"111", "222"};
        String[] input = new String[]{"c", "a", "baa", "c", "c1", "c"};
        String[] totalArr = SystemHWUtil.mergeArray2(str2, input);
        System.out.println(SystemHWUtil.formatArr(totalArr, " , "));
        System.out.println(totalArr.length);
    }

    @Test
    public void test_findReadLength() {
        String srcText = "abc";
        // System.out.println(SystemHWUtil.findReadLength(srcText, "abc", 0));
        Assert.assertEquals(3, SystemHWUtil.findReadLength(srcText, "abc", 0));

        srcText = " abc";
        Assert.assertEquals(4, SystemHWUtil.findReadLength(srcText, "abc", 0));

        srcText = " abcabc";
        Assert.assertEquals(4, SystemHWUtil.findReadLength(srcText, "abc", 0));

        srcText = " ab c";
        Assert.assertEquals(-1, SystemHWUtil.findReadLength(srcText, "abc", 0));

        srcText = "aababbabc";
        Assert.assertEquals(9, SystemHWUtil.findReadLength(srcText, "abc", 0));
    }

    @Test
    public void test_findReadLength2() {
        String srcText = "abc";
        // System.out.println(SystemHWUtil.findReadLength2(srcText, "abc", 0));
        Assert.assertEquals(3, SystemHWUtil.findReadLength(srcText, "abc", 0));

        srcText = " abc";
        Assert.assertEquals(4, SystemHWUtil.findReadLength2(srcText, "abc", 0));

    }

    @Test
    public void ttest_deleteJavaComment() {
        String input = "/*aabbcc*/ */aa";
        Assert.assertEquals(" */aa", ValueWidget.deleteJavaComment(input));
        System.out.println(ValueWidget.deleteJavaComment(input));

        input = "/*aabbcc* */aa";
        Assert.assertEquals("aa", ValueWidget.deleteJavaComment(input));

        input = "/*aabbcc/ */aa";
        Assert.assertEquals("aa", ValueWidget.deleteJavaComment(input));

        input = "/*aabbcc/ */aa/**/c";
        Assert.assertEquals("aac", ValueWidget.deleteJavaComment(input));

        input = "/*aabb" + SystemHWUtil.CRLF + "cc/ */aa";
        System.out.println(input);
        Assert.assertEquals("aa", ValueWidget.deleteJavaComment(input));
    }

    @Test
    public void test_replaceAll2() {
        String input = "I like Java,jAva is very easy and jaVa is so popular.";
        String regex = "java";
        String replacement = "cccc";

        String sb = ValueWidget.replaceAll3(input, regex, replacement);
//		System.out.println(input);
//		System.out.println(sb);
        Assert.assertEquals("I like cccc,cccc is very easy and cccc is so popular.", sb);

    }

    @Test
    public void test_replaceAll3() {
        String input = "I like Java,jAva is very easy and jaVa is so popular.";
        String regex = "java";
        String replacement = "cccc";

        String sb = ValueWidget.replaceAll3(input, regex, replacement);
//		System.out.println(input);
//		System.out.println(sb);
        Assert.assertEquals("I like cccc,cccc is very easy and cccc is so popular.", sb);

    }

    @Test
    /***
     * 方式一
     */
    public void test_replaceAll33() {
        String input = "I like Java,jAva is very easy and jaVa is so popular.";
        String replacement = "cccc";

//		System.out.println(input);
        String result = (input.replaceAll("(?i)java", replacement));
        Assert.assertEquals("I like cccc,cccc is very easy and cccc is so popular.", result);
    }

    //	@Test
    public void test_otherwise() {
        String input = "/* aa java bb */ \r\n /*jav*/";
        System.out.println(input);
        String regex = "/\\*[^java]*\\*/";
        System.out.println("-------------------------");
        System.out.println(input.replaceAll(regex, "good"));

    }

    //	@Test
    public void test_otherwise2() {
        String input = "/* aa java bb */ \r\n /*jav*/";
        System.out.println(input);
        String regex = "/\\*([^j][^a][^v][^a])*\\*/";
        System.out.println("-------------------------");
        System.out.println(input.replaceAll(regex, "good"));

    }

    //	@Test
    public void test_otherwise3() {
        String input = "/* aa java bb */ \r\n /*javcaaaaaaaaaaa*/";
        System.out.println(input);
        String regex = "/\\*" + ValueWidget.otherwise22("java") + "\\*/";
        System.out.println("-------------------------");
        System.out.println(regex);
        System.out.println("-------------------------");
        System.out.println(input.replaceAll(regex, "good"));

    }

    //	@Test
    public void test_compressImg() throws IOException {
        File file = new File("D:\\Temp\\mini 设备 副本新.JPG");
        ImageHWUtil.compressImg(file, null, new File("D:\\Temp\\mini2222.JPG"));
    }

    //	@Test
    public void test_thumbnail() throws IOException {
//		ImageHWUtil.thumbnail("D:\\software\\apache-tomcat-7.0.53-windows-x64\\apache-tomcat-7.0.53\\webapps\\yhyc\\upload\\image\\20141020\\20141020111710_400.jpg", 80,
//				new File("D:\\software\\apache-tomcat-7.0.53-windows-x64\\apache-tomcat-7.0.53\\webapps\\yhyc\\upload\\image\\20141020\\20141020111710_400_thumbnail.jpg"));
//		try {
//			Thread.sleep(20000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        String savePath = "abc/";
        savePath = savePath.substring(0, savePath.length() - 1);
        System.out.println(savePath);
    }

    //	@Test
    public void test_isJPGImageFile() {
//		String fileName="abc.png";
//		System.out.println(FileUtils.isJPGImageFile(fileName));
        String srcImgPath = "a.bc.jpeg";
        int index = srcImgPath.lastIndexOf('.');
        String thumbnail = "_thumbnail";
        System.out.println(srcImgPath.substring(0, index) + thumbnail + srcImgPath.substring(index));
    }

    //	@Test
    public void test_isword() {
        String input = "123456";
        System.out.println(ValueWidget.isWord(input, false));
    }

    //	@Test
    public void test_byte() {
        byte b = 12;
        System.out.println(Integer.toBinaryString(b));//1100
        String input = "00001100";
        System.out.println(Byte.parseByte(input, 2));//12

        System.out.println("-----------------");
        b = -116;//-12
        System.out.println(Integer.toBinaryString(b));//10001100
        input = "01110100";
        System.out.println(Byte.parseByte(input, 2));

        System.out.println("-----------------");
        b = -12;
        System.out.println(Integer.toBinaryString(b));//01110100
        input = "01110100";
        System.out.println(Byte.parseByte(input, 2));
    }

    //	@Test
    public void test_byte2() {
//		byte b=0;
//		System.out.println(Integer.toBinaryString(b));//1100
//
//		byte c=highRevers(b);
//		System.out.println(Integer.toBinaryString(c));//1100
    }

    //	@Test
    public void test_byte3() {
//		byte b=-127;
//		System.out.println(b);
//		byte c=highRevers(b);
//		System.out.println(c);
//		System.out.println(highRevers(c));
    }

    //	@Test
    public void test_CustomEncrypt() throws Exception {
        byte[] data = "黄威fsdfsdfdsf123".getBytes("GBK");
        byte[] key = "huangwei!@#$%".getBytes();
        byte[] encryptData = CustomEncrypt.encrypt(data, key);
        System.out.println(data.length);
        System.out.println("----------------------");
        byte[] data2 = CustomEncrypt.decrypt(encryptData, key);
        System.out.println(data2.length);
        System.out.println(SystemHWUtil.isSame(data, data2));
    }

    //	@Test
    public void test_CustomEncrypt2() throws Exception {
        String filePath = "D:\\doc\\个人信息_20131106.pdf";
        File destFile = new File("d:\\Temp\\ccc.dd");
        try {
            CustomEncrypt.encrypt2File(filePath, "黄威fsdfsdfdsf123", destFile);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    //	@Test
    public void test_CustomEncrypt3() throws Exception {
        String filePath = "d:\\Temp\\ccc.pdf";
        File destFile = new File("d:\\Temp\\ccc2.pdf");
        try {
            CustomEncrypt.decrypt2File(filePath, "黄威fsdfsdfdsf123", destFile);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    //	@Test
    public void test_createParentFolder() {
        String file = "d:\\Temp\\aa\\bb\\cc.java";
        SystemHWUtil.createParentFolder(file);
    }

    @Test
    public void test_trimeObject() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Woman w = new Woman();
        w.setHobby("  abc  ");
//		ReflectHWUtils.trimObject(w);
//		System.out.println(w.getHobby());
        SystemHWUtil.printObject(w);

    }

    @Test
    public void unique_stringarr() {
        String[] strs = new String[]
                {"a", "b", "c", "b", "c"};
        System.out.println(SystemHWUtil.formatArr(strs, ","));
        System.out.println("---------------");
        String[] strs_new = SystemHWUtil.unique(strs);
        String result = SystemHWUtil.formatArr(strs_new, ",");
        System.out.println(result);
        Assert.assertEquals("a,b,c", result);
    }

    /***
     * device type:	Phone
     * type version:	Android_4.1.1
     * version:	4.1.1
     */
//	@Test
    public void test_getMobilOS() {
        String userAgent = "Mozilla/5.0 (Linux; U; Android 4.1.1; en-cn; HTC T528d) AppleWebKit/53" +
                "4.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 MicroMessenger/6.0.2.5" +
                "8_r984381.520 NetType/WIFI";
//		String userAgent="Mozilla/5.0 (Linux; U; Android 2.3.4; zh-cn; XT531 Build/GRJ22) AppleW"+
//"ebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
//		String userAgent="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Ge"+
//"cko) Chrome/36.0.1985.143 Safari/537.36";
//		String userAgent="Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0";
        ClientOsInfo info = HeaderUtil.getMobilOS(userAgent);
        System.out.println("device type:\t" + info.getDeviceType());
        System.out.println("type version:\t" + info.getOsTypeVersion());
        System.out.println("version:\t" + info.getVersion());
        System.out.println("是移动设备吗:" + info.isMobile());
    }

    //	@Test
    public void test_blank() {
        String input = " \n		";
        System.out.println("a" + input + "b");
        input = input.replaceAll("[\\s]", "");
        System.out.println("a" + input + "b");
    }

    //	@Test
    public void test_thumbnail22() {
        File descFile = new File("D:\\Temp\\cc.jpg");
        try {
            InputStream is = HttpSocketUtil.getInputStream("http://img03.taobaocdn.com/bao/uploaded/i3/TB1nr9zHXXXXXaHXFXXXXXXXXXX_!!0-item_pic.jpg_430x430.jpg", null);
            ImageHWUtil.thumbnail(is, 90, 100, descFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //	@Test
    public void test_isPDF() {
        String file = "1.PDF2";
        System.out.println(FileUtils.isPDF(file));
    }

    //	@Test
    public void test_createThumbnail() {
        String sourceFile = "d:\\Temp\\TB1nr9zHXXXXXaHXFXXXXXXXXXX_!!0-item_pic.jpg_430x430.jpg";
        String target = "d:\\Temp\\result.jpg";
        try {
            ImageHWUtil.createThumbnail(sourceFile, 200, 100, 100, target);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //	@Test
    public void test_thumbSize() {
//		ImageHWUtil.thumbSize(200, 100, 800, 500);
        System.out.println(Integer.class.getSimpleName());
    }

    //	@Test
    public void test_isEqualNumber() {
        System.out.println(DigitUtil.isEqualNumber(null, 1));
    }
    /*@Test
	public void test_getCurrentTimeLong(){
		System.out.println(TimeHWUtil.getCurrentTimeLong());
	}*/

    //	@Test
    public void test_isLeapYear() {
        System.out.println(DigitUtil.isLeapYear("2004-02-02"));
    }

    //	@Test
    public void test_formBankCard() {
        String input = "6225880137706868";
        System.out.println("\"" + DigitUtil.formBankCard(input) + "\"");
//		System.out.println("\""+input.replaceAll("([\\d]{4})", "$1 ")+"\"");
    }

    //	@Test
    public void test_digit() {
        String input = "1234567";
        String result = FileUtils.formatFileSize(input);
        System.out.println(result);
    }

    @Test
    public void test_formatFileSize() {
        DecimalFormat df1 = (DecimalFormat) DecimalFormat.getInstance();
        df1.setGroupingSize(3);
        String result = df1.format(1234567);
//		String result=FileUtils.formatFileSize(1234567);
        System.out.println(result);
    }

	/*@Test
	public void test_equal(){
		String a="1";
		int b=1;
		boolean result=a.equals(String.valueOf(b));
		System.out.println(result);
	}*/
	/*@Test
	public void test_equal2(){
		Long a=229L;
		Long b=229L;
		System.out.println((a.intValue()==b.intValue()));
	}*/
	
	/*@Test
	public void test_userAgent()
	{
		String userAgent="Mozilla/5.0 (Linux; U; Android 4.1.1; en-cn; HTC T528d) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 MicroMessenger/6.0.2.58_r984381.520 NetType/WIFI";
		System.out.println(HeaderUtil.isMobile(userAgent));
		
		userAgent="Opera/9.80 (Android 2.3.7; Linux; Opera Mobi/46154) Presto/2.11.355 Version/12.10";
		System.out.println(HeaderUtil.isMobile(userAgent));
	}*/

    //	@Test
    public void test_setNull4specified() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        List list = new ArrayList();


        Student2 stu = new Student2();
        stu.setAddress("湖北");
        stu.setAge(12);
        stu.setIdentitify("422202198810066555");
        list.add(stu);

        stu = new Student2();
        stu.setAddress("湖北2");
        stu.setAge(13);
        stu.setIdentitify("a422202198810066555");
        list.add(stu);

        stu = new Student2();
        stu.setAddress("湖北3");
        stu.setAge(14);
        stu.setIdentitify("b422202198810066555");
        list.add(stu);
        System.out.println(SystemHWUtil.DIVIDING_LINE);
        ReflectHWUtils.setNull4specified(list, "identitify");
        System.out.println(list.size());
        for (int i = 0; i < list.size(); i++) {
            Student2 student = (Student2) list.get(i);
            System.out.println(student.getAddress() + "\t" + student.getAge() + "\t" + student.getIdentitify());
        }

    }

    //	@Test
    public void test_deleteNullEle4List() {
        List list = new ArrayList<String>();
//		list.add("aa");
//		list.add("cc");
//		list.add("b");
//		list.add("d");
        list.add("a");
//		list.add("cc");

        System.out.println(list);
        System.out.println(SystemHWUtil.DIVIDING_LINE);
        try {
            ReflectHWUtils.deleteNullEle4List(list, null);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(list);
    }

    @Test
    public void test_sed() {
        try {
            String content = FileUtils.getFullContent2(new File("E:\\tmp\\aa.txt"), "", true);
            System.out.println(content);
            String sb = RegexUtil.deleteComment(content, "staticapp.chanjet.com");
            System.out.println(SystemHWUtil.DIVIDING_LINE);
            System.out.println(sb);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void test_isMobileNumber() {
        String phone = "13718486139";
        org.junit.Assert.assertTrue(ValueWidget.isMobileNumber(phone));

        phone = "15102775755";
        org.junit.Assert.assertTrue(ValueWidget.isMobileNumber(phone));


        phone = "137184861391";
        org.junit.Assert.assertFalse(ValueWidget.isMobileNumber(phone));

        phone = "1371848613";
        org.junit.Assert.assertFalse(ValueWidget.isMobileNumber(phone));

        phone = "21718486139";
        org.junit.Assert.assertFalse(ValueWidget.isMobileNumber(phone));
        System.out.println(name);
    }

    @Test
    public void test_contain() {
        String source = "aava is my best";
        org.junit.Assert.assertFalse(RegexUtil.contain2(source, "Java"));
        org.junit.Assert.assertFalse(RegexUtil.contain2(source, "java"));
        org.junit.Assert.assertFalse(RegexUtil.contain2("jaava is my best", "java"));
        org.junit.Assert.assertTrue(RegexUtil.contain2("ajava is my best", "java"));
        org.junit.Assert.assertTrue(RegexUtil.contain2("aJava is my best", "java"));
    }

    //	@Test
    public void test_convertMap2Obj() throws InstantiationException, IllegalAccessException, SecurityException, NoSuchFieldException, IllegalArgumentException {
        Map map = new HashMap();
        map.put("personName", "黄威");
        map.put("birthdate", new Timestamp(1000255));
        map.put("identitify", "422203158895255555");
        map.put("age", 21);
        Object obj = ReflectHWUtils.convertMap2Obj(map, Person2.class);
        Person2 person = (Person2) obj;
//		System.out.println(obj);
        System.out.println(person.getPersonName());
        System.out.println(person.getAge());
        System.out.println(person.getBirthdate());
    }

    @Test
    public void test_equal() {
        String source = "ab.d.c";
//		System.out.println(regex);
        org.junit.Assert.assertFalse(RegexUtil.equalsWildcard(source, ".b.d.c"));
        org.junit.Assert.assertFalse(RegexUtil.equalsWildcard(source, "a..d.c"));
        org.junit.Assert.assertFalse(RegexUtil.equalsWildcard(source, "a.*.d.c"));
        org.junit.Assert.assertFalse(RegexUtil.equalsWildcard(source, "abad.c"));
        org.junit.Assert.assertFalse(RegexUtil.equalsWildcard(source, "a*ad.c"));
        org.junit.Assert.assertFalse(RegexUtil.equalsWildcard(source, "ab.d.."));
        org.junit.Assert.assertTrue(RegexUtil.equalsWildcard(source, "ab.d.c"));
        org.junit.Assert.assertTrue(RegexUtil.equalsWildcard(source, source));
        org.junit.Assert.assertTrue(RegexUtil.equalsWildcard(source, "a*.d.c"));
        org.junit.Assert.assertTrue(RegexUtil.equalsWildcard(source, "ab.*.c"));
        org.junit.Assert.assertTrue(RegexUtil.equalsWildcard(source, "ab*.c"));
        org.junit.Assert.assertTrue(RegexUtil.equalsWildcard(source, "ab*.d*c"));

    }

    //	@Test
    public void test_md5() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String plain = "bgUrl=&notifyUrl=&businessId=00000000001&platIdtfy=t3&merchantId=0044098&orderId=2015070200009&orderDate=20150702&bankType=&payeeBankAccount=&payeeBankType=&payeeBankName=&payeeName=&deviceId=&payerName=&payerCardType=&payerContactMbl=&payerContactMal=&orderAmount=1&amtType=01&orderTime=2015-07-02 21:35:03&expireTime=2015-07-03 21:35:03&goodsId=45&productName=好会计&productNum=1&productDesc=&redoFlag=0&merPriv=&expand=&expand2=&key=12356780Poi)(*";
        String plain2 = SystemHWUtil.convertUTF2GBK(plain);
        System.out.println(plain2);
        System.out.println(plain2.length());
        System.out.println(MD5Encoder(plain2, "gbk"));



		/*plain2=SystemHWUtil.convertUTF2ISO(plain);
		System.out.println(plain2);
		System.out.println(plain2.length());
		System.out.println(encodeMD5(plain2));



		plain2=SystemHWUtil.convertGBK2UTF(plain);
		System.out.println(plain2);
		System.out.println(plain2.length());
		System.out.println(encodeMD5(plain2));



		plain2=SystemHWUtil.convertGBK2ISO(plain);
		System.out.println(plain2);
		System.out.println(plain2.length());
		System.out.println(encodeMD5(plain2));*/
    }

    //	@Test
    public void test_096() {
        String selectContent = "{\"name\":23}";
        System.out.println(selectContent);
        selectContent = selectContent.replace("\"", "\\\"");
        System.out.println(selectContent);
    }

    //	@Test
    public void test_002() {
        String now = "2014-02-25";
        String endTime = "2014-01-25";
        if (now.compareTo(endTime) == 1) {//大于
            System.out.println("过期");
            ;
        } else {
            System.out.println("还没有过期");
        }
    }

    //	@Test
    public void test_formatDateTime() {
        System.out.println(TimeHWUtil.formatDateTime());

    }

    //	@Test
    public void test_getDateBeforeMinute() {
        Date date = new Date();
        System.out.println(TimeHWUtil.formatDateTime(date));
        Date a = TimeHWUtil.getDateBeforeMinute(date, 2);
        System.out.println(TimeHWUtil.formatDateTime(a));
    }

    //	@Test
    public void tst_getListStr4File() {
        File file = new File("E:\\tmp\\list.txt");
        List<String> strList = FileUtils.getListStr4File(file, "utf-8");
        System.out.println(strList.size());
        SystemHWUtil.printList(strList, true, null, null);
        System.out.println(strList);
    }
	/*public static void main(String[] args) {
    	String str = "123你好aaa";
    	String repickStr=RegexUtil.replaceChinese(str, "_");
    	System.out.println("去中文后:"+repickStr);
	}*/

    //	@Test
    public void test_china() {
        String content = "{    \"error\": {        \"code\": 0,        \"hint\": \"\",        \"msg\": \"OK\"    },    \"result\": true,    \"value\": {        \"category\": \"invoice\",        \"userId\": 60000740772,        \"value\": \"{\"title\":\"武汉云马科技有限公司\",\"type\":\"1\",\"personal\":false,\"address\":\"湖北省武汉市\",\"receiver\":\"黄昆仑\",\"phone\":\"phone\"}\"    }}";
        content = content.replaceAll("([\u4E00-\u9FA5]*)+", "aa");
        System.out.println(content);
    }

    //	@Test
    public void test_mutipleBlank() {
        String input = "a     b  c d";
        input = input.replaceAll("[\\s]{2,}", " ");
        System.out.println(input);
    }

    /*@Test
    public void test_replaceN(){
        String input=" http-outgoing-76 << \"		\"needInvoice\":false,[\\n]\"";
        System.out.println(input);
        System.out.println(input.replace("[\\n]", SystemHWUtil.EMPTY));
    }*/
//	@Test
    public void test_replace22() {
        String input = " http-outgoing-76 << \"		\"needInvoice\":false,[\\n]\"";
        System.out.println(input);
        System.out.println(RegexUtil.dealCiaResponse(input, true));
    }
//	@Test
	/*public void test_json(){
		String input="\"normalPrice\": \"{\"storagePrice\":66,\"ud1Price\":1,\"userPeriodPrice\":99}\",";
		System.out.println(input);
		String result=RegexUtil.getJsonFromQuotes(input, true); 
		System.out.println(result);
	}*/

    //	@Test
    public void test_0002() throws IOException {
        String filePATH = "E:\\tmp\\testCia.txt";
        String content = FileUtils.getFullContent3(filePATH, null).toString();
//		System.out.println(content);
        String result = RegexUtil.dealCiaResponseRequest(content, true);
        System.out.println(result);

    }

    //	@Test
    public void test_array() {
        List list = new ArrayList<String>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        list.add(0, "111");
        ValueWidget.prepend(list, "444");
        System.out.println(list);

    }

    //	@Test
    public void test_omitTooLongString() {
        String input = "11223344";
        System.out.println(ValueWidget.omitTooLongString(input, 14));
    }

    public Long getOrderTip(String dateString) {

        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateTrans = null;
        try {
            dateTrans = format1.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (dateTrans == null) {
            return 0L;
        }

        Calendar inputDateCalendarPlusOffset = Calendar.getInstance();
        inputDateCalendarPlusOffset.setTime(dateTrans);
        inputDateCalendarPlusOffset.add(Calendar.YEAR, 1);

        return new Long(inputDateCalendarPlusOffset.getTimeInMillis());

    }

    //	@Test
    public void test_getOrderTip() {
		/*String dateStr="2016-08-01 00:00:00";
		Long l=getOrderTip(dateStr);
		Date date=new Date();
		date.setTime(l);
		System.out.println(date);*/
//		String url="http://localhost:8081/SSLServer/addUser.security";
//		System.out.println(url.replaceAll("(https?://[^/]+)/.*$", "$1"));
        String input = "{\"result\":false,\"param\":\"(20095)\",\"errorCode\":\"20095\",\"errorMessage\":\"骞冲.?ュ.璋..澶辫触(20095)\"}";
        System.out.println(input.startsWith("{"));
    }

    //	@Test
    public void test_deleteDigit() {
        String input;
        try {
            input = FileUtils.getFullContent3(new File("e:\\tmp\\testdigit.txt"), SystemHWUtil.CHARSET_CURR).toString();
            System.out.println(RegexUtil.deleteDigit(input));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void test_getRealMessage() {
        String result = CMDUtil.getRealMessage("cwd /home", Constant2.COMMAND_CWD);
        Assert.assertEquals("/home", result);
    }

    //	@Test
    public void test_divide() {
        String payAmountStr = "1011";
        BigDecimal chanpayAcount = new BigDecimal(payAmountStr);
        BigDecimal bssAcount = chanpayAcount.divide(new BigDecimal(100));
        System.out.println("使用BigDecimal");
        System.out.println(String.valueOf(bssAcount.multiply(new BigDecimal(100))));
    }

    //	@Test
    public void test_divide2() {
//		String payAmountStr="1011";
//		float f=Float.parseFloat(payAmountStr);
//		float result=f/100f;
//		System.out.println(String.valueOf(result*100));
        String input = "[\"123\"]";
        System.out.println(input.replaceAll("\\[[\\s]*\"([\\w]+)[\\s]*\"\\]", "$1"));
    }

    @Test
    public void test_sign() {
        String signPlainTemplate = "businessId=%s"
                + "&platIdtfy=%s"
                + "&merchantId=%s"
                + "&orderId=%s"
                + "&orderDate=%s"
                + "&orderAmount=%s"
                + "&orderTime=%s"
                + "&expireTime=%s"
                + "&payeeBankAccount=%s"
                + "&payeeBankType=%s"
                + "&payeeBankName=%s"
                + "&payeeName=%s"
                + "&deviceId=%s"
                + "&detailId=%s"
                + "&detailTime=%s"
                + "&bankId=%s"
                + "&bankDealId=%s"
                + "&amount=%s"
                + "&amtType=%s"
                + "&payResult=%s"
                + "&errCode=%s"
                + "&errMsg=%s"
                + "&payeeBankAccount=%s"
                + "&payeeBankType=%s"
                + "&payeeBankName=%s"
                + "&payeeName=%s"
                + "&key=%s";
        String signPlain = String.format(signPlainTemplate
                , "00WGFK210016"
                , "t3"/*platIdtfy*/
                , "CP008030"/*merchantId*/
                , "2015081400061"/*orderId*/
                , "2015-09-02 00:00:00"/*orderDate*/
                , "1"/*orderAmount*/
                , "2015-09-02 12:59:03"/*orderTime*/
                , "2015-09-03 12:59:03"/*expireTime*/
                , ""/*payeeBankAccount*/
                , ""/*payeeBankType*/
                , ""/*payeeBankName*/
                , ""/*payeeName*/
                , ""/*deviceId*/
                , "10000000000269042"/*detailId*/
                , "2015-09-02 13:26:08"/*detailTime*/
                , ""/*bankId*/
                , ""/*bankDealId*/
                , "1"/*amount*/
                , "01"/*amtType*/
                , "00"/*payResult*/
                , ""/*errCode*/
                , ""/*errMsg*/
                , ""/*payeeBankAccount*/
                , ""/*payeeBankType*/
                , ""/*payeeBankName*/
                , "60000746055"/*payeeName*/
                , "EGFSDFG23DSTEWTW"/*key*/);
        String md5;
        System.out.println(signPlain);
        md5 = SystemHWUtil.getMD5(signPlain, SystemHWUtil.CHARSET_UTF);
        System.out.println(md5);


		/*String orderDate="2015-08-14 00:00:00";
		orderDate=orderDate.split(" ")[0].replaceAll("-", "");
		System.out.println(orderDate);*/
    }

    //	@Test
    public void test_signNew() {
        String signPlainTemplate =
                "businessId=%s"
                        + "&platIdtfy=%s"
                        + "&merchantId=%s"
                        + "&orderId=%s"
                        + "&orderDate=%s"
                        + "&orderAmount=%s"
                        + "&orderTime=%s"
                        + "&expireTime=%s"
                        + "&payeeBankAccount=%s"
                        + "&payeeBankType=%s"
                        + "&payeeBankName=%s"
                        + "&payeeName=%s"
                        + "&deviceId=%s"
                        + "&detailId=%s"
                        + "&detailTime=%s"
                        + "&bankId=%s"
                        + "&bankDealId=%s"
                        + "&amount=%s"
                        + "&amtType=%s"
                        + "&payResult=%s"
                        + "&errCode=%s"
                        + "&errMsg=%s"
                        + "&payeeBankAccount=%s"
                        + "&payeeBankType=%s"
                        + "&payeeBankName=%s"
                        + "&payeeName=%s"
                        + "&key=%s";
        String signPlain = String.format(signPlainTemplate
                , ""/*businessId*/
                , ""/*platIdtfy*/
                , "CP008030"/*merchantId*/
                , "2015081400071"/*orderId*/
                , "2015-09-02 00:00:00"/*orderDate*/
                , "1"/*orderAmount*/
                , ""/*orderTime*/
                , ""/*expireTime*/
                , ""/*payeeBankAccount*/
                , ""/*payeeBankType*/
                , ""/*payeeBankName*/
                , ""/*payeeName*/
                , ""/*deviceId*/
                , "10000000000269045"/*detailId*/
                , "2015-09-02 14:14:11"/*detailTime*/
                , ""/*bankId*/
                , ""/*bankDealId*/
                , "1"/*amount*/
                , ""/*amtType*/
                , "00"/*payResult*/
                , ""/*errCode*/
                , ""/*errMsg*/
                , ""/*payeeBankAccount*/
                , ""/*payeeBankType*/
                , ""/*payeeBankName*/
                , ""/*payeeName*/
                , "EGFSDFG23DSTEWTW"/*key*/);
        String md5;
        System.out.println(signPlain);
        md5 = SystemHWUtil.getMD5(signPlain, SystemHWUtil.CHARSET_UTF);
        System.out.println(md5);


		/*String orderDate="2015-08-14 00:00:00";
		orderDate=orderDate.split(" ")[0].replaceAll("-", "");
		System.out.println(orderDate);*/
    }

    //	@Test
    public void test_getQueryString() {
		/*Map map=new HashMap();
		map.put("username", "whuang");
		map.put("address", "huilongguan");
		map.put("age", 27);
		System.out.println(map);
		System.out.println(HttpSocketUtil.getQueryString(map));*/

//		BigDecimal chanpayAcount=new BigDecimal("1.12");
//        BigDecimal bssAcount=  chanpayAcount.multiply(new BigDecimal(100));//畅捷支付的单位是分
//        long orderTotal=bssAcount.longValue();
//        System.out.println("orderTotal:"+orderTotal);
//		String input="businessId=&platIdtfy=&merchantId=0044098&orderId=808087&orderDate=2015-08-04 00:00:00&orderAmount=100&orderTime=&expireTime=&payeeBankAccount=&payeeBankType=&payeeBankName=&payeeName=&deviceId=&detailId=10000000001441238&detailTime=2015-08-04 08:54:40&bankId=&bankDealId=&amount=100&amtType=&payResult=00&errCode=&errMsg=&payeeBankAccount=&payeeBankType=&payeeBankName=&payeeName=&key=12356780Poi)(*";
//		System.out.println(input.replaceAll("&", "&\r"));
//		String input="http://hbjltv.com/info/request?username=sss";
//		System.out.println(input.split("\\?")[1]);
//		BigDecimal chanpayAcount = new BigDecimal("1");
//		BigDecimal bssAcount = chanpayAcount.divide(new BigDecimal(100));
//		System.out.println(bssAcount.toString());
        String input = "singnType=&version=&businessId=00WGFKB20012&platIdtfy=&merchantId=CP008030&orderId=2015081400074&orderDate=2015-09-06&payDate=2015-09-06&orderAmount=1&payTime=2015-09-06+11%3A48%3A16.000000&orderTime=&expireTime=&payeeBankAccount=&payeeBankType=&payeeBankName=&payeeName=&deviceId=&detailTime=&bankId=&detailId=10000000000269305&bankDealId=&amount=1&amtType=01&payResult=00&errCode=&errMsg=&respCode=000000&reason=%EF%BE%9F&signMsg=d8a126382b383d3c34f3de962ffd5339";
        System.out.println(HttpSocketUtil.getPostForm(input, "http://127.0.0.1:8080/ROOT/pay/payResult", "gbk"));
//		String notifyUrl="http://hbjltv.com/info/request?cc=aaa:bbb&c=c";
//		Map map =parseQueryString(notifyUrl);
//				System.out.println(map);
    }

    //	@Test
    public void test_02() {
//		String input="\"a\"aa\"\n\r\n--------";
        String input;
        try {
            input = FileUtils.getFullContent2(new File("d:\\Temp\\a.txt"), "utf-8", true);
            System.out.println(input);
            System.out.println(RegexUtil.deleteTwoQuote(input));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //	@Test
    public void test_deleteEveryThingBeforeBrace() {
        String input = "fsdf--\n\r-{1234\n\r56";
        System.out.println(input);
        System.out.println(RegexUtil.deleteEveryThingBeforeBrace(input));
    }

    //	@Test
    public void test_getRequestBodyFromList() {
//		Map map=new HashMap();
//		map.put("username", "whuang");
//		map.put("age", "23");
//		map.put("address", null);
//		System.out.println(WebServletUtil.getRequestBodyFromMap(map));
        String tempDir = "aa";
        System.out.println(tempDir);
        if (!tempDir.endsWith(File.separator)) {
            tempDir += File.separator;
        }
        System.out.println(tempDir);
    }

    //	@Test
    public void test_isSamePropertyValue() {
        Woman w = new Woman();
        w.setAge(22);
        w.setHobby("足球1");
        w.setName("中国");
        Son s = new Son();
        s.setName("zhangsan");
        s.setId(2);
        s.setWife(w);

        Woman w2 = new Woman();
        w2.setAge(22);
        w2.setHobby("足球1");
        w2.setName("中国");
        Son s2 = new Son();
        s2.setName("zhangsan");
        s2.setId(2);
        s2.setWife(w2);

        try {
            System.out.println(ReflectHWUtils.isSamePropertyValue(s, s2));
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    //	@Test
    public void test_regFind() {
        String input = null;
        try {
            input = FileUtils.getFullContent2(new File("E:\\tmp\\0930\\a.txt"), "GBK");
            System.out.println(input);
            System.out.println();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //	@Test
    public void test_json2queryString() {
        String input = "{\"username\":\"whuang\"}";
        Map map = (Map) HWJacksonUtils.deSerialize(input, Map.class);
        System.out.println(input);
        System.out.println(map);
    }

    //	@Test
    public void test_ListComparator() {

        List<Student> students = new ArrayList<Student>();
        Student stu = null;
        stu = new Student();
        stu.setName("whuang");
        stu.setAge(12);
        stu.setScore(80);
        students.add(stu);

        stu = new Student();
        stu.setName("rong");
        stu.setAge(11);
        stu.setScore(90);
        students.add(stu);

        stu = new Student();
        stu.setName("zhu");
        stu.setAge(15);
        stu.setScore(100);
        students.add(stu);
        SortList<Student> sortList = new SortList<Student>();
        sortList.sort(students, "getAge", "asc");
//		Collections.sort(students,new SystemHWUtil. ListComparator(true,"age"));
        System.out.println(students);


    }

    //	@Test
    public void test_deleteNotes() {
        String input = FileUtils.getFullContent(new File("E:\\tmp\\1022\\a.txt"), "GBK");
        System.out.println(input);
        System.out.println(RegexUtil.deleteNotes(input));
    }

    //	@Test
    public void test_further() {
        String input = "\"[\"aa\"]\"   ,";
        System.out.println(input);
        input = input.replaceAll("\"(\\[.+\\])\"([\\s]*[,}])", "$1$2");
        //jsonStr=jsonStr.replaceAll("\"(\\{[^\\\\]+\\})\"", "$1");
        System.out.println(input);
    }

    //	@Test
    public void test_unicode() {
        String str = "aaa\nbbb\nccc\nddd\n";

        System.out.println(str.replaceAll("(\n)[\\s]*([\\w]+)", "\" +$1\"$2"));
    }

    //	@Test
    public void test_unicode2() {
        String str;
        try {
            str = FileUtils.getFullContent3("E:\\tmp\\1025\\a.txt", null).toString();
            System.out.println(str.replaceAll("(\r\n)[\\s]*([^\\s]+.*)", "\" +$1\"$2"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    //	@Test
    public void test_join() {
        String[] strs = new String[]{"a", "b", "c", "dd"};
        System.out.println(ValueWidget.join(strs, "-"));
    }

    //	@Test
    public void test_zip() {
        List list_key = new ArrayList();
        list_key.add("name");
        list_key.add("age");
        List list_val = new ArrayList();
        list_val.add("黄威");
        list_val.add(26);
        Map map = ValueWidget.zip(list_key, list_val);
        System.out.println(map);
    }

    //	@Test
    public void test_sed2() {
        String input;
        try {
            input = FileUtils.getFullContent2(new File("E:\\tmp\\1030\\a.txt"), SystemHWUtil.CHARSET_UTF);
            System.out.println(ValueWidget.sed(input, "(__isLogin=[\\s]*)True", "$1False"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//	@Test
	/*public void test_sed3() throws IOException{
		System.out.println(RegexUtil.sed(new File("E:\\tmp\\1030\\a.txt"), "(__isLogin=[\\s]*)true", "$1False2"));
		
	}*/

    @Test
    public void test_isHTMLWebPage() {
//		String input="a<html><head><title>";
        String input;
        try {
            input = FileUtils.getFullContent2(new File("E:\\tmp\\1120\\index.html"), null);
            System.out.println();
            org.junit.Assert.assertTrue(RegexUtil.isHTMLWebPage(input));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //	@Test
    public void test_json() {
        List intList = new ArrayList();
        intList.add(4200);
        intList.add(4200);
        intList.add(4200);
        intList.add(4200);
        intList.add(4200);
        intList.add(4200);

        Map map = new HashMap();
        map.put("name", "铟");
        map.put("data", intList);


        System.out.println(HWJacksonUtils.getJsonP(map));
    }

    //	@Test
    public void test_escapeHTML() {
        String input = "<script>";
        System.out.println(ValueWidget.escapeHTML(input));
    }
}
