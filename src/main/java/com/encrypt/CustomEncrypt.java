package com.encrypt;

import com.common.util.SystemHWUtil;
import com.io.hw.file.util.FileUtils;
import com.string.widget.util.ValueWidget;

import java.io.File;
import java.io.InputStream;

/***
 * 自定义加密算法<br>
 * 算法:<br>
 * 加密:<br>
 * 1,最高位(第八位)取反;<br>
 * 2,求原字节数组的MD5,并追加到最后面;<br>
 * 3,DES加密<br>
 * 解密:<br>
 * 1,DES解密;<br>
 * 2,去掉后面的MD5字节数组;<br>
 * 3,最高位(第八位)取反;
 * 4,校验MD5,目的是检验完整性
 * @author huangwei
 * @since 2014年12月28日
 */
public class CustomEncrypt {
	/***
	 * 加密
	 * @param source
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] source,byte[]key) throws Exception{
		if(ValueWidget.isNullOrEmpty(source)||ValueWidget.isNullOrEmpty(key)){
			return null;
		}
		byte []data=highRevers(source);
		byte[]md5= SystemHWUtil.getMD5Bytes(source);//原字节数组的md5
		return SystemHWUtil.encryptDES(SystemHWUtil.mergeArray(data,md5), key);
	}
	/***
	 * 加密
	 * @param source
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] source,String key) throws Exception{
		if(ValueWidget.isNullOrEmpty(source)||ValueWidget.isNullOrEmpty(key)){
			return null;
		}
		byte[]key2=key.getBytes(SystemHWUtil.CHARSET_UTF);
		return encrypt(source, key2);
	}
	/***
	 * 没有关闭inputStream
	 * @param inputStream : 执行完之后,没有关闭
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(InputStream inputStream,String key) throws Exception{
		byte[]source=FileUtils.readBytes3(inputStream);
		return encrypt(source, key);
	}
	public static byte[]encrypt(File file,String key) throws Exception{
		if(!file.exists()){
			System.out.println("[byte[]encrypt(File file,String key)]file does not exist");
			return null;
		}
		return encrypt(FileUtils.getBytes4File(file), key);
	}
	public static byte[]encrypt(String filePath,String key) throws Exception{
		File file2=new File(filePath);
		return encrypt(FileUtils.getBytes4File(file2), key);
	}
	public static void encrypt2File(File file,String key,File destFile) throws Exception{
		if(!file.exists()){
			System.out.println("[void encrypt2File(File file,String key,File destFile)]file does not exist");
			return ;
		}
		byte[]encryptedData=encrypt(file, key);
		
		FileUtils.writeBytesToFile(encryptedData, destFile);
	}
	public static void encrypt2File(String filePath,String key,File destFile) throws Exception{
		byte[]encryptedData=encrypt(filePath, key);
		FileUtils.writeBytesToFile(encryptedData, destFile);
	}
	/***
	 * 解密 :<br>
	 * 1,DES解密;<br>
	 * 2,去掉后面的MD5字节数组;<br>
	 * 3,最高位(第八位)取反;<br>
	 * 4,校验MD5,目的是检验完整性
	 * @param encryptData
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] encryptData,byte[]key) throws Exception{
		if(ValueWidget.isNullOrEmpty(encryptData)||ValueWidget.isNullOrEmpty(key)){
			return null;
		}
		byte []data=SystemHWUtil.decryptDES(encryptData, key) ;
		byte[]source2=SystemHWUtil.getFrontBytes(data, data.length-16);//去掉后面的MD5字节数组
		byte[]md5Bytes=SystemHWUtil.getAfterBytes(data, 16);//获取MD5字节数组
		byte[]source=highRevers(source2);//最高位取反
		byte[]md5= SystemHWUtil.getMD5Bytes(source);//原字节数组的md5
		if(!SystemHWUtil.isSame(md5Bytes, md5)){//MD5校验失败,则返回null
			String tempDir=System.getProperty("user.home");
			if(!tempDir.endsWith(File.separator)){
				tempDir+=File.separator;
			}
			String destFilepath=tempDir+"AppData\\Local\\Temp\\http"+File.separator+"old.dat";
			FileUtils.writeBytesToFile(encryptData, destFilepath);
			System.out.println("[decrypt]MD5校验失败,保存至:"+destFilepath);
			return null;
		}
		return source;
	}
	/***
	 * 解密
	 * @param encryptData
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] encryptData,String key) throws Exception{
		if(ValueWidget.isNullOrEmpty(encryptData)||ValueWidget.isNullOrEmpty(key)){
			return null;
		}
		byte[]key2=key.getBytes(SystemHWUtil.CHARSET_UTF);
		return decrypt(encryptData, key2);
	}
	/***
	 * 
	 * @param inputStream : 执行完之后没有关闭
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(InputStream  inputStream,String key) throws Exception{
		byte[] encryptData=FileUtils.readBytes3(inputStream);
		return decrypt(encryptData, key);
	}
	public static byte[]decrypt(File file,String key) throws Exception{
		if(!file.exists()){
			System.out.println("[byte[]decrypt(File file,String key)]file does not exist");
			return null;
		}
		return decrypt(FileUtils.getBytes4File(file), key);
	}
	public static byte[]decrypt(String filePath,String key) throws Exception{
		File file=new File(filePath);
		return decrypt(FileUtils.getBytes4File(file), key);
	}
	public static void decrypt2File(File file,String key,File destFile) throws Exception{
		if(!file.exists()){
			System.out.println("[void decrypt2File(File file,String key,File destFile)]file does not exist");
			return ;
		}
		byte[]encryptedData=decrypt(file, key);
		FileUtils.writeBytesToFile(encryptedData, destFile);
	}
	public static void decrypt2File(String filePath,String key,File destFile) throws Exception{
		byte[]encryptedData=decrypt(filePath, key);
		if(encryptedData==null||encryptedData.length==0){
			System.out.println("encryptedData is null");
			return;
		}
		FileUtils.writeBytesToFile(encryptedData, destFile);
	}
	/***
	 * 最高位(第八位)取反;
	 * @param b
	 * @return
	 */
	public static byte highRevers(byte b){
		if ((int) b > 0) {
			return (byte) ((int) b - 128);
		}else{
			return (byte) ((int) b + 128);
		}
	}
	/***
	 * 每个字节的最高位(第八位)取反;
	 * @param input
	 * @return
	 */
	public static byte[] highRevers(byte[] input){
		if(ValueWidget.isNullOrEmpty(input)){
			System.out.println("[byte[] highRevers(byte[] input)]input is null or empty.");
			return null;
		}
		int length=input.length;
		byte[]c=new byte[length];
		for(int i=0;i<length;i++){
			c[i]=highRevers(input[i]);
		}
		return c;
	}
}
