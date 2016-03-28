package com.test;

import com.common.bean.Woman;
import com.common.dict.Constant2;
import com.common.util.*;
import com.http.util.HttpSocketUtil;
import com.io.hw.file.util.FileUtils;
import com.string.widget.util.ValueWidget;
import com.time.util.TimeHWUtil;
import junit.framework.Assert;
import net.sf.json.JSONObject;
import org.apache.commons.compress.archivers.ArchiveException;
import org.junit.Test;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnitTest2 {

    /***
     * 解析TLV格式中表示长度的第一个字节
     *
     * @param length : [0,128]
     * @return
     */
    public static int parseLengthFirstByte(byte length) {
        int byteLeng = (int) length;
        if (byteLeng < 0) {
            byteLeng = 257 + byteLeng;
        }
        return byteLeng;
    }

    /***
     * 修改StringBuffer 的内容
     *
     * @param sb
     * @param drift
     */
    public static void modifyStringBuffer(StringBuffer sb, int drift) {
        sb.setLength(0);//清空StringBuffer
        sb.append(drift);
    }

    public static String randomType(List<Object[]> typeTotals) {
        if (typeTotals == null || typeTotals.size() == 0) {
            return null;
        }
        int sum = 0;
        for (int i = 0; i < typeTotals.size(); i++) {
            Object[] o = typeTotals.get(i);
            sum += (Long) o[1];
            if (i > 0) {
                o[1] = (Long) o[1] + (Long) typeTotals.get(i - 1)[1];
            }
        }
        Random random = new Random();
        int temp = random.nextInt(sum);
        System.out.println("temp:" + temp);
        String res = null;
        for (Object[] o : typeTotals) {
            if (temp <= (Long) o[1]) {
                res = (String) o[0];
                break;
            }
        }

        return res;
    }

    @Test
    public void test_formatBytes() {
        String input = "a1b2";
        System.out.println(HexByteHWUtil.formatBytes(SystemHWUtil
                .hexStrToBytes(input)));
    }

    @Test
    public void test_getFullContent3() {
        String filepath = "D:\\bin\\config\\conf_passwd.properties";
        try {
            System.out.print(FileUtils.getFullContent3(filepath, "UTF-8"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_readBytes3() {
        String filepath = "D:\\bin\\config\\conf_passwd.properties";
        try {
            InputStream in = new FileInputStream(filepath);
            byte[] bytes = FileUtils.readBytes3(in);
            System.out.print(new String(bytes, "UTF-8"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_parse() {
        String input = "0700010360201001 0A000000012345D4B4F6D0".replaceAll(" ", "");
        int index = 0;
        String leng1 = input.substring(index, 2);
        index += 2;
        int byteLenInt1 = Integer.parseInt(leng1, 16);
        String data1 = ValueWidget.subSpecifiedStr(input, 2, byteLenInt1 * 2);
        index += byteLenInt1 * 2;
        System.out.println(data1);
        System.out.println(leng1);
        String leng2 = ValueWidget.subSpecifiedStr(input, index, 2);
        index += 2;
        int byteLenInt2 = Integer.parseInt(leng2, 16);
        String data2 = ValueWidget.subSpecifiedStr(input, index, byteLenInt2 * 2);
        System.out.println(leng2);
        System.out.println(data2.substring(0, 12));
    }

    @Test
    public void test_integer() {
        Integer a = (Integer) 2;
        Integer b = (Integer) 2;
        System.out.println(a == b);
        System.out.println(a == (int) b);
        System.out.println(a.intValue() == b.intValue());
    }

    @Test
    public void test03() {
        long size = 1000001000L;//单位是Byte
        System.out.println(FileUtils.formatFileSize2(size));
    }

    @Test
    public void test_getCron() {
        String cron = TimeHWUtil.getCron(new Date());
        System.out.println(cron);
    }

    @Test
    public void test_isDuplicate() {
        List<String> list3 = new ArrayList<String>();
        list3.add("a");
        list3.add("b");
        list3.add("c");
        list3.add("b");
        Object[] result = SystemHWUtil.isDuplicate(list3);
        System.out.println(result[0]);
        System.out.println(result[1]);
    }

    @Test
    public void test_isAbsolutePath() {
        String path = "d:\\bin";
        System.out.println(SystemHWUtil.isAbsolutePath(path));
    }

    @Test
    public void test_getListFilesNoRecursion() {
        List<File> files = FileUtils.getListFilesNoRecursion(new File("D:\\Temp\\a\\a\\socket_chat_20140122"));
        for (int i = 0; i < files.size(); i++) {
            System.out.println(files.get(i).getName());
        }
    }

    @Test
    public void test_getFileSuffixName() {
        String fileSimpleName = "a.b.c.png";
        System.out.println(SystemHWUtil.getFileSuffixName(fileSimpleName));
    }

    @Test
    public void test_tlv() {
        long len = 130;
        String hex = TLVUtil.getTLVLengthHex(len);
        System.out.println(hex);
        byte byteLenPre = SystemHWUtil.hexStrToBytes(hex)[0];
        System.out.println("byteLenPre:" + byteLenPre);
        System.out.println("byte:" + parseLengthFirstByte(byteLenPre));
        Assert.assertEquals(true, parseLengthFirstByte(byteLenPre) == len);
        int byteLenInt = Integer.parseInt(hex, 16);
        System.out.println("byteLenInt:" + byteLenInt);
    }

    @Test
    public void test_tlv2() {
        long len = 129;
        String hex = TLVUtil.getTLVLengthHex(len);
        System.out.println(hex);
        int byteLenInt = Integer.parseInt(hex, 16);
        System.out.println("byteLenInt:" + byteLenInt);

    }


//	public static void cpFile(File sourceFile ,File targetFile) throws FileNotFoundException, IOException{
//		byte[]bytes=FileUtils.readBytesFromInputStream(new BufferedInputStream(new FileInputStream(sourceFile)), (int)sourceFile.length());
//		FileOutputStream fout=new FileOutputStream(targetFile);
//		fout.write(bytes);
//		fout.flush();
//		fout.close();
//		
//	}

    @Test
    public void test_tlv3() {
        String source = "ff81";
        int loc = 0;
        String byteLen = TLVUtil.subSpecifiedStr(source, loc, 2);

        byte byteLenPre = SystemHWUtil.hexStrToBytes(byteLen)[0];
        String data = null;
        // 下一次递归时，从哪儿开始,注意：单位是字节个数
        int newIndex = 0;
        // 表示data长度的那个字节(一个字节)
        int byteLenInt = Integer.parseInt(byteLen, 16);
        if (byteLenInt <= (int) Constant2.TLV_LENGTH_LIMIT_INT) {
            data = TLVUtil.subSpecifiedStr(source, loc + 2, byteLenInt * 2);
            newIndex = Integer.parseInt(byteLen, 16);
        } else {// >0x80
            // 保存长度的位串包含多少个字节，比如2
            int lengthLen = ((byte) byteLenPre)
                    ^ (Constant2.TLV_LENGTH_LIMIT_BYTE);
            System.out.println(lengthLen);
            // 数据的真实长度
//			int sumLeng = Integer.parseInt(
//					TLVUtil.subSpecifiedStr(source, loc + 2, lengthLen * 2), 16);
//			System.out.println("sumLeng:"+sumLeng);
        }
    }

    @Test
    public void test_longParse() {
//		long length=1295;
//		String hex=Long.toHexString(length);
//		System.out.println("hex:"+hex);


    }

    @Test
    public void test_cpFile() {
//		File sourceFile=new File("E:\\software\\java\\android-studio-bundle-130.737825-windows.exe");
//		File targetFile=new File("E:\\software\\java\\android-studio-bundle-130.737825-windows_bak.exe");
//		try {
//			cpFile(sourceFile, targetFile);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
    }

    @Test
    public void test_same_file() {
        File file1 = new File("D:\\Temp\\a\\a\\b\\log4j.xml");
        File file2 = new File("D:\\Temp\\a\\a\\b\\..\\b\\log4j.xml");
        boolean isSame = FileUtils.isSameFile(file1, file2);
        System.out.println(isSame);
        Assert.assertEquals(true, isSame);
    }

    @Test
    public void test_replaceY() {
        String source = "\\u6211\\u4eec";
        System.out.println("source:" + source);
        System.out.println("a:" + source.replaceAll("()", "[A-F]"));
        System.out.println("source:" + ValueWidget.replaceY(source, "abcdef", "ABCDEF"));


        String regex = "[abcdef]";
        Pattern pattern = Pattern.compile(regex, Pattern.LITERAL);
        Matcher matcher = pattern.matcher(source);
        String result = matcher.replaceAll("[ABCDEF]");
        System.out.println(result);

    }

    @Test
    public void test_getTimeDelta() {
        String dateStr1 = "2014-02-27 20:12:57";
        String dateStr2 = "2014-02-27 21:12:54";
        System.out.println(TimeHWUtil.getSecondDelta(dateStr1, dateStr2));
    }

    @Test
    public void test_isContain() {
        List<Woman> list = new ArrayList<Woman>();
        Woman w = null;
        w = new Woman();
        w.setName("aa");
        list.add(w);

        w = new Woman();
        w.setName("bb");
        list.add(w);

        w = new Woman();
        w.setName("cc");
        list.add(w);

        w = new Woman();
        w.setName("dd");
        list.add(w);

        try {
            System.out.println(SystemHWUtil.isContainObject(list, "name", "dd2"));
            Assert.assertEquals(true, SystemHWUtil.isContainObject(list, "name", "dd"));
            Assert.assertEquals(false, SystemHWUtil.isContainObject(list, "name", "dd2"));
            Assert.assertEquals(true, SystemHWUtil.isContainObject(list, "name", "aa"));
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    //	@Test
    public void test_isContain22() {
        List<Woman> list = new ArrayList<Woman>();
        Woman w = null;
        w = new Woman();
        w.setName("aa");
        list.add(w);

        w = new Woman();
        w.setName("bb");
        list.add(w);

        w = new Woman();
        w.setName("cc");
        list.add(w);

        w = new Woman();
        w.setName("cc");
        list.add(w);

        w = new Woman();
        w.setName("dd");
        list.add(w);

        w = new Woman();
        w.setName("dd");
        list.add(w);

        System.out.println(list.size());

        try {
            List resultList = SystemHWUtil.uniqueObject(list, "name");
            System.out.println(resultList.size());
            System.out.println(resultList);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

	/*@Test
	public void test_modifyString(){
		String string=new String("abc");
		System.out.println("old value:\t"+string.toString());
		modifyString(string, 3);
		System.out.println("new value:\t"+string.toString());
	}*/

    /***
     * 修改String的内容
     *
     * @param string
     * @param drift
     *//*
	public static void modifyString(String string,int drift){
		string =String.valueOf(drift);
	}*/
    @Test
    public void test_modifyStringBuffer() {
        StringBuffer sb = new StringBuffer();
        sb.append("whuang");
        System.out.println("old value:\t" + sb.toString());
        modifyStringBuffer(sb, 3);
        System.out.println("new value:\t" + sb.toString());
    }

    @Test
    public void test_isHasSuffix() {
        String filepath = "a/a/ca.txt";
        Assert.assertEquals(true, SystemHWUtil.isHasSuffix(filepath));
    }

    @Test
    public void test_isZip() {
        String fileapth1 = "d:\\bin\\a111.txt";
        String fileapth2 = "d:\\bin\\path_tools-0.0.1-SNAPSHOT.jar";
//		System.out.println();
        Assert.assertEquals(false, CompressZipUtil.isZip(fileapth1));
        Assert.assertEquals(true, CompressZipUtil.isZip(fileapth2));
    }

    @Test
    public void test_isContains() {
        List<String> list = new ArrayList<String>();
        Assert.assertEquals(false, SystemHWUtil.isContains(list, new String("aa")));
        list.add("aa");
        System.out.println(SystemHWUtil.isContains(list, new String("aa")));
        Assert.assertEquals(true, SystemHWUtil.isContains(list, new String("aa")));
        Assert.assertEquals(true, SystemHWUtil.isContains(list, "aa"));
    }

    //	@Test
    public void test_deCompressSingleFile() {
        String zipFile = "D:\\doc\\android\\app\\package\\2014-4-9\\2014-4-9\\editor_360llq.zip";
        try {
            byte[] bytes = CompressZipUtil.deCompressSingleFile(new File(zipFile), "editor_360llq/score.txt", SystemHWUtil.CHARSET_UTF);
            System.out.println(new String(bytes, SystemHWUtil.CHARSET_UTF));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ArchiveException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void test_isNullOrEmpty() {
        String input1 = null;
        String input3 = "";
        String input4 = " ";
        String input5 = "a";
        String input6 = "   ";
        String[] strs1 = new String[]{"a"};
        String[] strs2 = new String[]{"", ""};
        String[] strs3 = new String[]{"", "c"};
        String[] strs4 = new String[]{};
        org.junit.Assert.assertEquals(true, ValueWidget.isNullOrEmpty(input1));
        org.junit.Assert.assertEquals(true, ValueWidget.isNullOrEmpty(input3));
        org.junit.Assert.assertEquals(false, ValueWidget.isNullOrEmpty(input4));
        org.junit.Assert.assertEquals(false, ValueWidget.isNullOrEmpty(input5));
        org.junit.Assert.assertEquals(false, ValueWidget.isNullOrEmpty(input6));

        org.junit.Assert.assertEquals(false, ValueWidget.isNullOrEmpty(strs1));
        org.junit.Assert.assertEquals(true, ValueWidget.isNullOrEmpty(strs2));
        org.junit.Assert.assertEquals(false, ValueWidget.isNullOrEmpty(strs3));
        org.junit.Assert.assertEquals(true, ValueWidget.isNullOrEmpty(strs4));
    }

    //	@Test
    public void test_findStr2() {
        System.out.println(SystemHWUtil.findStr2("../public", "../"));
    }

    @Test
    public void test_unPackage() {
        Integer i = Integer.valueOf(444);
        System.out.println(DigitUtil.unPackage(i));
        org.junit.Assert.assertEquals(444, DigitUtil.unPackage(i));
    }

    @Test
    public void test_unPackage22() {
        Long i = Long.valueOf(444);
        System.out.println(DigitUtil.unPackage(i));
        org.junit.Assert.assertEquals(444, DigitUtil.unPackage(i));
    }

    @Test
    public void test_isEqual() {
        Integer i1 = Integer.valueOf(1444);
        Long i2 = Long.valueOf(1444);
//		System.out.println(i1==i2);
        System.out.println(DigitUtil.isEqualNumber(i1, i2));
    }

    @Test
    public void test_delDoubleQuotation() {
//		String input="abc";
//		System.out.println(input);
//		System.out.println(SystemHWUtil.delDoubleQuotation(input));
        String input = "abc\ncc";
        input = input.replaceAll("\n", "\r\n");
        System.out.println(input);
    }

    @Test
    public void test002() {
        List<Object[]> typeTotals = new ArrayList<Object[]>(10);
        Object[] objs = new Object[2];
        objs[0] = "004";
        objs[1] = new Long(29);
        typeTotals.add(objs);

        objs = new Object[2];
        objs[0] = "001";
        objs[1] = new Long(133);
        typeTotals.add(objs);

        objs = new Object[2];
        objs[0] = "005";
        objs[1] = new Long(177);
        typeTotals.add(objs);

        System.out.println(randomType(typeTotals));
    }

    //	@Test
    public void test_guolv2() {
        List<String> list = new ArrayList<String>();
        list.add("abc");
        list.add("bbb");
        list.add("abc");
        list.add(new String("abc"));
        List<String> list2 = SystemHWUtil.guolv2(list);
        for (Iterator iterator = list2.iterator(); iterator.hasNext(); ) {
            String string = (String) iterator.next();
            System.out.println(string);
        }
    }

    //	@Test
    public void test_upload() throws Exception {
        FileInputStream ins = new FileInputStream("C:\\Users\\huangwei\\Pictures\\201406\\2014-06-05_18-01-45.jpg");
        String result = HttpSocketUtil.uploadFile("http://192.168.1.112:80/upload_app.php", false, ins, "form-data; name=\"pic\"; filename=\"2014-06-05_18-01-45.jpg\"", null, null, -1, -1);
        System.out.println(result);
    }

    //	@Test
    public void test_deleteFrontSlash() {
        String realpath = "D:\\software\\eclipse\\workspace2\\springMVC_upload\\attached";
        String uploadFolderName = "attached/";
        String webappPath = "src\\main\\webapp\\";
        String result = realpath.replaceAll("(attached[/]?)$", webappPath.replace("\\", "\\\\") + "$1");
        System.out.println("result:\n" + result);
    }

    //	@Test
    public void test_metadata() {
        String input = "D:\\xxx\\eclipse\\workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core";
        System.out.println(input);
        String reg = ".metadata\\.plugins";
        System.out.println(input.contains(reg));
    }

    @Test
    public void test_parseObjectList() {
        List list = new ArrayList<Woman>();
        Woman w = new Woman();
        w.setAge(23);
        w.setHobby("xiaochi");
        w.setName("yj");
        list.add(w);

        w = new Woman();
        w.setAge(24);
        w.setHobby("xiaochi2");
        w.setName("yj2");
        list.add(w);

        Map<String, Object> map = ReflectHWUtils.parseObjectList(list, Woman.class, "hobby", "name");
//		System.out.println(map.size());
        org.junit.Assert.assertEquals(2, map.size());
//		System.out.println(map);
    }

    //	@Test
    public void test003() {
        String jsonReq = "{}";
        JSONObject jObj = JSONObject.fromObject(jsonReq);
        System.out.println(jObj.isEmpty());
        jObj.put("key1", "va   lue1");
        jObj.put("key2", "value2");
        String json = jObj.toString();
        System.out.println(json);
    }

    @Test
    public void test_indexOf() {
        String[] arr = new String[]{"a", "b", "c", "d"};
//		System.out.println(SystemHWUtil.indexOfArr(arr, "d"));
        org.junit.Assert.assertEquals(3, SystemHWUtil.indexOfArr(arr, "d"));
    }

}


