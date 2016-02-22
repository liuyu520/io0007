package com.io.hw.file.util;

import com.cmd.dos.hw.util.CMDUtil;
import com.common.thread.SharedMemoryArea;
import com.common.util.SystemHWUtil;
import com.string.widget.util.ValueWidget;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public final class FileUtils {
	public static final int BUFFSIZE_1024 = 1024;
	private static boolean isDetail = false;

	/***
	 * 不允许实例化
	 */
	private FileUtils() {
		throw new Error("Don't let anyone instantiate this class.");
	}

	/***
	 * 
	 * @param filename
	 * @param charset
	 * @return
	 * @throws FileNotFoundException
	 */
	public static BufferedReader getBufferReaderFromFile(String filename,
			String charset) throws FileNotFoundException {
		InputStream ins = new FileInputStream(filename);
		InputStreamReader ireader = null;
		try {
			if (charset == null) {
				charset = SystemHWUtil.CHARSET_ISO88591;
				if (isDetail) {
					System.out.println("FileUtils.BufferedReader 采用编码:"
							+ charset);
				}
			}
			ireader = new InputStreamReader(ins, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (ireader == null) {
			return null;
		}
		return new BufferedReader(ireader);
	}

	/***
	 * convert file to BufferedReader,must specify charset(encoding)
	 * 
	 * @param file
	 * @param charset
	 * @return
	 * @throws FileNotFoundException
	 */
	public static BufferedReader getBufferReaderFromFile(File file,
			String charset) throws FileNotFoundException {
		InputStream ss = new FileInputStream(file);
		InputStreamReader ireader;
		BufferedReader reader = null;
		try {
			if (charset == null) {
				charset = SystemHWUtil.CHARSET_ISO88591;
			}
			ireader = new InputStreamReader(ss, charset);
			reader = new BufferedReader(ireader);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return reader;
	}

	/***
	 * convert file to BufferedInputStream
	 * 
	 * @param filename
	 * @return
	 * @throws FileNotFoundException
	 */
	public static BufferedInputStream getBufferedInputStreamFromFile(
			String filename) throws FileNotFoundException {
		InputStream ins = new FileInputStream(filename);
		return new BufferedInputStream(ins);
	}
	/***
	 * convert file to BufferedInputStream
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static BufferedInputStream getBufferedInputStreamFromFile(
			File file) throws FileNotFoundException {
		if(!file.exists()){
			return null;
		}
		InputStream ins = new FileInputStream(file);
		return new BufferedInputStream(ins);
	}

	/***
	 * convert byte[] to ByteArrayInputStream
	 * 
	 * @param bytes
	 * @return ByteArrayInputStream
	 */
	public static ByteArrayInputStream getByteArrayInputSreamFromByteArr(
			byte[] bytes) {
		return new ByteArrayInputStream(bytes);
	}

	/***
	 * convert hex(16) bit string to ByteArrayInputStream
	 * 
	 * @param hex
	 * @return ByteArrayInputStream
	 */
	public static ByteArrayInputStream getByteArrayInputSream2hexString(
			String hex) {
		return getByteArrayInputSreamFromByteArr(SystemHWUtil
				.hexStrToBytes(hex));
	}

	public static int isFileContains(File file, String regex, String charset) {
		BufferedReader reader;
		if (charset == null) {
			charset = SystemHWUtil.CHARSET_ISO88591;
		}
		try {
			reader = getBufferReaderFromFile(file, charset);
			return isFileContains(reader, regex);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/***
	 * Note : reader has not close.
	 * 
	 * @param file
	 * @param regex
	 * @param charset
	 * @return
	 */
	public static int isFileContains(String file, String regex, String charset) {
		if (charset == null) {
			charset = SystemHWUtil.CHARSET_ISO88591;
		}
		BufferedReader reader;
		try {
			reader = getBufferReaderFromFile(file, charset);
			return isFileContains(reader, regex);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 在第几行发现key word
	 * 
	 * @param reader
	 *            : has closed
	 * 
	 * @param regex
	 * @return
	 */
	public static int isFileContains(Reader reader, String regex) {
		String readedLine = null;
		BufferedReader br = null;
		try {
			int changedRow = 0;// 行号
			br = new BufferedReader(reader);
			while ((readedLine = br.readLine()) != null) {
				changedRow++;
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(readedLine);
				if (m.find()) {
					return changedRow;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}


	/***
	 * 
	 * @param is
	 * @return
	 */
	@Deprecated
	public static String getFullContent(InputStream is) {
		InputStreamReader inputReader = new InputStreamReader(is);
		BufferedReader bureader = new BufferedReader(inputReader);

		return getFullContent(bureader);
	}

	@Deprecated
	public static String getFullContent(InputStreamReader inputReader) {
		BufferedReader bur = new BufferedReader(inputReader);
		return getFullContent(bur);
	}

	/***
	 * 
	 * @param reader
	 * @return the content of reader
	 */
	@Deprecated
	public static String getFullContent(Reader reader) {
		BufferedReader br = new BufferedReader(reader);
		return getFullContent(br);
	}

	/**
	 * 
	 * @param reader
	 * @return
	 */
	@Deprecated
	public static String getFullContent(CharArrayReader reader) {
		BufferedReader bfreader = new BufferedReader(reader);
		return getFullContent(bfreader);
	}

	/***
	 * 
	 * @param byteb
	 * @return
	 */
	public static String getByteBufferContent4array(ByteBuffer byteb) {
		if (null == byteb) {
			return null;
		}
		byteb.flip();
		return new String(byteb.array());
	}

	/***
	 * read file ,convert file to byte array
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static byte[] readBytes4file(File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		return readBytes3(in);

	}

	/***
	 * Has been tested ok
	 * 
	 * @param in
	 *            : 会关闭
	 * @return
	 * @throws IOException
	 */
	public static byte[] readBytes3(InputStream in) throws IOException {
		BufferedInputStream bufin = new BufferedInputStream(in);
		int buffSize = BUFFSIZE_1024;
		ByteArrayOutputStream out = new ByteArrayOutputStream(buffSize);

		// System.out.println("Available bytes:" + in.available());

		byte[] temp = new byte[buffSize];
		int size = 0;
		while ((size = bufin.read(temp)) != -1) {
			out.write(temp, 0, size);
		}
		bufin.close();
		in.close();
		out.flush();
		byte[] content = out.toByteArray();
		out.close();
		return content;
	}

	/***
	 * get byte[] from <code>InputStream</code> Low efficiency
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	@Deprecated
	public static byte[] readBytes2(InputStream in) throws IOException {
		byte[] temp = new byte[BUFFSIZE_1024];
		byte[] result = new byte[0];
		int size = 0;
		while ((size = in.read(temp)) != -1) {
			byte[] readBytes = new byte[size];
			System.arraycopy(temp, 0, readBytes, 0, size);
			result = SystemHWUtil.mergeArray(result, readBytes);
		}
		return result;
	}

	/***
	 * Has been tested
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	@Deprecated
	public static byte[] readBytes(InputStream in) throws IOException {
		byte[] temp = new byte[in.available()];
		byte[] result = new byte[0];
		int size = 0;
		while ((size = in.read(temp)) != -1) {
			byte[] readBytes = new byte[size];
			System.arraycopy(temp, 0, readBytes, 0, size);
			result = SystemHWUtil.mergeArray(result, readBytes);
		}
		return result;
	}

	/***
	 * 
	 * @param filepath
	 * @return
	 * @throws IOException
	 */
	public static byte[] readBytes4file(String filepath) throws IOException {
		File file = new File(filepath);
		return readBytes4file(file);
	}

	/***
	 * 从文件中读取指定长度的字节
	 * 
	 * @param inputFile
	 * @param length2
	 * @return
	 * @throws IOException
	 */
	public static byte[] readBytesFromFile(File inputFile, int length2,
			boolean isCloseInput) throws IOException {
		InputStream ins = new FileInputStream(inputFile);
		return readBytesFromInputStream(ins, length2, isCloseInput);
	}

	/***
	 * 从输入流获取指定长度的字节数组,当文件很大时，报java.lang.OutOfMemoryError: Java heap space
	 * 注意：没有关闭inputstream
	 * 
	 * @param ins
	 * @param length2
	 * @return
	 * @throws IOException
	 */
	public static byte[] readBytesFromInputStream(InputStream ins, int length2)
			throws IOException {
		return readBytesFromInputStream(ins, length2, false/* isCloseInput */);
	}

	/***
	 * 从输入流获取指定长度字节数组,当文件很大时，报java.lang.OutOfMemoryError: Java heap space
	 * 
	 * @since 2014-02-19
	 * @param ins
	 * @param length2
	 * @return
	 * @throws IOException
	 */
	public static byte[] readBytesFromInputStream(InputStream ins,
			long length2, boolean isCloseInput) throws IOException {
		int readSize;
		byte[] bytes = null;
		bytes = new byte[(int) length2];

		long length_tmp = length2;// 还需要读取的字节数
		long index = 0;// start from zero
		while ((readSize = ins.read(bytes, (int) index, (int) length_tmp)) != -1) {
			length_tmp -= readSize;
			if (length_tmp == 0) {
				break;
			}
			index = index + readSize;
		}
		if (isCloseInput) {
			ins.close();
		}
		return bytes;
	}

	/***
	 * 读取指定长度的字符（不是字节）。
	 * 
	 * @param br
	 * @param length2
	 * @param isCloseReader
	 * @return
	 * @throws IOException
	 */
	public static char[] readCharsFromInputStream(BufferedReader br,
			long length2, boolean isCloseReader) throws IOException {
		int readSize;
		char[] chars = null;
		chars = new char[(int) length2];

		long length_tmp = length2;
		long index = 0;// start from zero
		while ((readSize = br.read(chars, (int) index, (int) length_tmp)) != -1) {
			length_tmp -= readSize;
			if (length_tmp == 0) {
				break;
			}
			index = index + readSize;
		}
		if (isCloseReader) {
			br.close();
		}
		return chars;
	}

	/***
	 * 从文件中读取指定长度的字节
	 * 
	 * @param file
	 * @param length2
	 * @return
	 * @throws IOException
	 */
	public static byte[] readBytesFromFile(File file, int length2)
			throws IOException {
		if (file == null || (!file.exists())) {
			return null;
		}
		InputStream ins = new FileInputStream(file);
		return readBytesFromInputStream(ins, length2, true/* isCloseInput */);
	}

	/***
	 * 从文件中读取指定长度的字符（不是字节哦）
	 * 
	 * @param file
	 * @param length2
	 * @return
	 * @throws IOException
	 */
	public static char[] readCharsFromFile(File file, int length2)
			throws IOException {
		if (file == null || (!file.exists())) {
			return null;
		}
		BufferedReader br = getBufferReaderFromFile(file, null/* charset */);
		return readCharsFromInputStream(br, length2, true/* isCloseReader */);
	}

	/***
	 * 读取指定长度的字节
	 * 
	 * @since 2014-02-27
	 * @param ins
	 * @param sumLeng
	 *            : 要读取的字节数
	 * @return
	 * @throws IOException
	 */
	public static byte[] readBytesFromGzipInputStream(GZIPInputStream ins,
			long sumLeng) throws IOException {
		byte[] fileNameBytes = new byte[(int) sumLeng];
		int fileNameReadLength = 0;
		int hasReadLength = 0;// 已经读取的字节数
		while ((fileNameReadLength = ins.read(fileNameBytes, hasReadLength,
				(int) sumLeng - hasReadLength)) > 0) {
			hasReadLength = hasReadLength + fileNameReadLength;
		}
		return fileNameBytes;
	}

	/***
	 * 从inputstream 中读取字节数组,没有关闭br_right
	 * 
	 * @param br_right
	 *            : 没有关闭
	 * @return
	 * @throws IOException
	 */
	public static byte[] readBytesFromInputStream(InputStream br_right)
			throws IOException {
		return readBytesFromInputStream(br_right, br_right.available());
	}

	/***
	 * read char array from inputstream according to specified length.
	 * 没有关闭br_right
	 * 
	 * @param file
	 * @param ins
	 * @param length2
	 *            :要读取的字符总数
	 * @throws IOException
	 */
	public static char[] getCharsFromReader(BufferedReader br, int length2,
			boolean isCloseReader) throws IOException {
		int readSize;
		char[] chars = null;
		chars = new char[length2];

		long length_tmp = length2;
		long index = 0;// start from zero
		while ((readSize = br.read(chars, (int) index, (int) length_tmp)) != -1) {
			length_tmp -= readSize;
			if (length_tmp == 0) {
				break;
			}
			index = index + readSize;// 写入字符数组的offset（偏移量）
		}
		if (isCloseReader) {
			br.close();
		}
		return chars;
	}

	/***
	 * 从文件中读取指定长度的字符（注意：不是字节）
	 * 
	 * @param file
	 * @param length2
	 * @return
	 * @throws IOException
	 */
	public static char[] getCharsFromFile(File file, int length2)
			throws IOException {
		if (file == null || (!file.exists())) {
			if (isDetail) {
				System.out
						.println("FileUtils.getCharsFromFile " + file == null ? ""
								: file.getAbsolutePath() + "不存在");
			}
			return null;
		}
		BufferedReader br = getBufferReaderFromFile(file, null);
		return getCharsFromReader(br, length2, true/* isCloseReader */);
	}

	/***
	 * get byte[] from file
	 * 
	 * @param filepath
	 *            : String
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytes4File(String filepath) throws IOException {
		return readBytes4file(filepath);
	}

	/***
	 * get byte[] from file
	 * 
	 * @param filepath
	 *            : java.io.File
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytes4File(File file) throws IOException {
		if (file == null || (!file.exists())) {
			if (isDetail) {
				System.out
						.println("FileUtils.getBytes4File " + file == null ? ""
								: file.getAbsolutePath() + "不存在");
			}
			return null;
		}
		return readBytes4file(file);
	}

	/***
	 * convert ByteBuffer to String .
	 * 
	 * @param bytebuffer
	 * @param charset
	 * @return
	 */
	public static String getByteBufferContent(ByteBuffer bytebuffer,
			String charset) {
		bytebuffer.flip();
		byte[] content = new byte[bytebuffer.limit()];
		bytebuffer.get(content);
		if (ValueWidget.isNullOrEmpty(charset)) {
			return (new String(content));
		} else {
			try {
				return (new String(content, charset));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return (new String(content));
		}

	}

	/**
	 * have closed reader
	 * 
	 * @param reader
	 * @return
	 */
	@Deprecated
	public static String getFullContent(BufferedReader reader) {
		StringBuilder sb = new StringBuilder();
		String readedLine = null;
		try {
			while ((readedLine = reader.readLine()) != null) {
				sb.append(readedLine);
				sb.append(SystemHWUtil.CRLF);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String content = sb.toString();
		int length_CRLF = SystemHWUtil.CRLF.length();
		if (content.length() <= length_CRLF) {
			return content;
		}
		return content.substring(0, content.length() - length_CRLF);//
	}

	@Deprecated
	public static ArrayList<String> getFullContentArr(BufferedReader reader) {
		ArrayList<String> chat = new ArrayList<String>();
		String readedLine = null;
		try {
			while ((readedLine = reader.readLine()) != null) {
				chat.add(readedLine.replaceAll(SystemHWUtil.CRLF,
						SystemHWUtil.CRLF + SystemHWUtil.CRLF));
				// sb.append(readedLine);
				// sb.append(SystemUtil.CRLF);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return chat;
	}

	/***
	 * 
	 * @param chats
	 * @param file
	 * @throws IOException
	 */
	public static void write2File(List<String> chats, File file, String charset)
			throws IOException {
		OutputStream in = new FileOutputStream(file);
		BufferedWriter reader = new BufferedWriter(new OutputStreamWriter(in,
				charset));
		int length = chats.size();
		for (int i = length - 1; i >= 0; i--) {
			reader.write(chats.get(i));
			reader.write(SystemHWUtil.CRLF);
		}
		reader.flush();
		reader.close();
		in.close();

	}

	public static String getFullContent2(File file, String charset)
			throws IOException {
		return getFullContent2(file, charset, true);
	}

	public static String getFullContent2(File file, String charset, boolean isCloseStream)
            throws IOException {
		if (file.exists()) {
			FileInputStream fis = new FileInputStream(file);
            return getFullContent2(fis, charset, isCloseStream);
        } else {
			if (isDetail) {
				System.out.println("FileUtils.getFullContent2 File \""
						+ file.getAbsolutePath() + "\" not exist!");
			}
			return null;
		}
	}

	/***
	 * 指定字符编码，无损地读取文本文件.推荐!
	 * 
	 * @param in
	 *            : 输入流，会关闭
	 * @param charset
	 *            : 字符编码
	 * @return
	 * @throws IOException
	 */
	public static StringBuffer getFullContent3(InputStream in, String charset)
			throws IOException {
		StringBuffer sbuffer = new StringBuffer();
		InputStreamReader inReader;
		// 设置字符编码
		if (ValueWidget.isNullOrEmpty(charset)) {
			charset = SystemHWUtil.CURR_ENCODING;
		}
		inReader = new InputStreamReader(in, charset);
		char[] ch = new char[SystemHWUtil.BUFF_SIZE_1024];
		int readCount = 0;
		while ((readCount = inReader.read(ch)) != -1) {
			sbuffer.append(ch, 0, readCount);
		}
		inReader.close();
		in.close();
		return sbuffer;
	}
	/***
	 * 
	 * @param inReader
	 * @param charset
	 * @param isCloseReader : 是否关闭Reader
	 * @return
	 * @throws IOException
	 */
	public static StringBuffer getFullContent3(Reader inReader,boolean isCloseReader)
			throws IOException {
		StringBuffer sbuffer = new StringBuffer();
		char[] ch = new char[SystemHWUtil.BUFF_SIZE_1024];
		int readCount = 0;
		while ((readCount = inReader.read(ch)) != -1) {
			sbuffer.append(ch, 0, readCount);
		}
		if(isCloseReader){
		inReader.close();}
		return sbuffer;
	}

	/***
	 * 没有关闭inReader
	 * @param inReader : 最后没有关闭
	 * @return
	 * @throws IOException
	 */
	public static StringBuffer getFullContent3(Reader inReader) throws IOException
	{
		return getFullContent3(inReader, false);
	}
	/***
	 * 无损读取.推荐!
	 * 
	 * @param file
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static StringBuffer getFullContent3(File file, String charset)
			throws IOException {
		InputStream in = new FileInputStream(file);
		return getFullContent3(in, charset);
	}
	/***
	 * 无损读取.推荐!
	 * 
	 * @param file
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static StringBuffer getFullContent3(File file) throws IOException{
		return getFullContent3(file, SystemHWUtil.CURR_ENCODING);
}
	/***
	 * 推荐!
	 * 
	 * @param file
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static StringBuffer getFullContent3(String file, String charset)
			throws IOException {
		System.out.println("[getFullContent3]charset:"+charset);
		return getFullContent3(new File(file), charset);
	}

	/***
	 * 无损读取,推荐
	 * 
	 * @param in
	 *            : 会关闭
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String getFullContent4(InputStream in, String charset)
			throws IOException {
		System.out.println("[getFullContent4]charset:"+charset);
		byte[] bytes = FileUtils.readBytes3(in);
		// 设置字符编码
		if (ValueWidget.isNullOrEmpty(charset)) {
			charset = SystemHWUtil.CURR_ENCODING;
		}
		return new String(bytes, charset);
	}

	/***
	 * 无损读取,推荐
	 * 
	 * @param in
	 *            : 会关闭
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String getFullContent4(InputStream in) throws IOException
	{
		return getFullContent4(in, SystemHWUtil.CURR_ENCODING);
	}
	/***
	 * 先读取出来字节数组，然后在包装成为字符串;效率不高，因为有拷贝操作（System.arraycopy）
	 * 
	 * @param in
	 * @param charset
	 * @return
	 * @throws IOException
	 */
    public static String getFullContent2(InputStream in, String charset, boolean isCloseStream)
            throws IOException {
		// 并不是要读取的字节的长度
		int step = BUFFSIZE_1024;
		BufferedInputStream bis = new BufferedInputStream(in);

		// Data's byte array
		byte[] receData = new byte[step];

		// data length read from the stream
		int readLength = 0;

		// data Array offset
		int offset = 0;

		// Data array length
		int byteLength = step;

		while ((readLength = bis.read(receData, offset, byteLength - offset)) != -1) {
			// Calculate the current length of the data
			offset += readLength;
			// Determine whether you need to copy data , when the remaining
			// space is less than step / 2, copy the data
			if (byteLength - offset <= step / 2) {
				byte[] tempData = new byte[receData.length + step];
				System.arraycopy(receData, 0, tempData, 0, offset);
				receData = tempData;
				byteLength = receData.length;
			}
		}
        if (isCloseStream) {
            bis.close();
            in.close();
        }
        if(ValueWidget.isNullOrEmpty(charset)){
			charset="UTF-8";
		}
		return new String(receData, 0, offset, charset);
	}

	/***
	 * 
	 * @param fileName
	 * @param charset
	 * @return
	 */
	@Deprecated
	public static String getFullContent(String fileName, String charset) {
			File file = new File(fileName);

			return charset == null ? getFullContent(file) : getFullContent(
					file, charset);
	}

	/***
	 * Note: use <code>SystemUtil.CHARSET_ISO88591</code>
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFullContent(String fileName) {
		return getFullContent(fileName, SystemHWUtil.CHARSET_ISO88591);
	}

	@Deprecated
	public static String getFullContent(File file) {
		return getFullContent(file, null);
	}

	@Deprecated
	public static String getFullContent(File file, String charset) {
		BufferedReader reader = null;
		if (!file.exists()) {
			System.out.println("getFullContent: file(" + file.getAbsolutePath()
					+ ") does not exist.");
			return null;
		}
		if (charset == null) {
			charset = SystemHWUtil.CHARSET_ISO88591;
		}
		try {
			reader = getBufferReaderFromFile(file, charset);
			return getFullContent(reader);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	/***
	 * 读取普通的文本文件,返回List,每一行就是一个元素,并且忽略空行和首尾的空格
	 * @param file
	 * @param charset
	 * @return List<String>
	 */
	public static List<String>getListStr4File(File file,String charset){
		List<String>strList=new ArrayList<String>();
		BufferedReader reader = null;
		if (!file.exists()) {
			System.out.println("getFullContent: file(" + file.getAbsolutePath()
					+ ") does not exist.");
			return null;
		}
		if (charset == null) {
			charset = SystemHWUtil.CHARSET_ISO88591;
		}
		try {
			reader = getBufferReaderFromFile(file, charset);
			String readedLine = null;
			try {
				while ((readedLine = reader.readLine()) != null) {
					if(!ValueWidget.isNullOrEmpty(readedLine)){
						strList.add(readedLine.trim());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return strList;
	}

	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public static BufferedWriter getBufferedWriter(String fileName) {
		FileWriter fileWrite;
		File file = new File(fileName);
		if (!file.exists()) {
			if (isDetail) {
				System.out.println("FileUtils.getBufferedWriter: file("
						+ file.getAbsolutePath() + ") does not exist.");
			}
			return null;
		}
		try {
			fileWrite = new FileWriter(file);
			return new BufferedWriter(fileWrite);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 无法指定编码
	 * 
	 * @param file
	 * @return
	 */
	public static BufferedWriter getBufferedWriter(File file) {
		if (!file.exists()) {
			if (isDetail) {
				System.out.println("FileUtils.getFullContent: file("
						+ file.getAbsolutePath() + ") does not exist.");
			}
			return null;
		}
		FileWriter fileWrite;
		try {
			fileWrite = new FileWriter(file);// 无法指定编码
			return new BufferedWriter(fileWrite);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 把字符串写入指定文件
	 * 
	 * @param fileName
	 * @param fileContent
	 * @param willCreateIfNotExist
	 */
	public static void writeToFile(String fileName, String fileContent,
			boolean willCreateIfNotExist) {
		File file = new File(fileName);
		writeToFile(file, fileContent, willCreateIfNotExist);
	}
	/***
	 * <br>将关闭输出流
	 * @param file
	 * @param fileContent
	 * @param willCreateIfNotExist : 若文件不存在是否创建
	 */
	public static void writeStrToFile(File file, String fileContent,
			boolean willCreateIfNotExist){
		writeToFile(file, fileContent, willCreateIfNotExist);
	}
	/**
	 * first clear up this file ,and then write to it.
	 * <br>无法指定编码,默认按照系统编码(windows 是GBK,linux 是UTF-8)
	 * <br>将关闭输出流
	 * @param fileName
	 * @param fileContent
	 * @param willCreateIfNotExist : 若文件不存在是否创建
	 */
	public static void writeToFile(File file, String fileContent,
			boolean willCreateIfNotExist) {
		if ((!file.exists()) && willCreateIfNotExist) {
			try {
				file.createNewFile();
				if(isDetail){
					System.out.println("FileUtils.writeToFile create new file:"+file.getAbsolutePath());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		BufferedWriter bufferedWriter = getBufferedWriter(file);
		if (bufferedWriter == null) {
			if(isDetail){
			System.out.println("FileUtils.writeToFile : bufferedWriter is null");}
			return;
		}
		try {
			bufferedWriter.write(fileContent);
			bufferedWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void writeToFile(File fileName, String fileContent) {
		writeToFile(fileName, fileContent, true/* willCreateIfNotExist */);
	}

	public static void writeToFile(String fileName, String fileContent) {
		writeToFile(fileName, fileContent, true/* willCreateIfNotExist */);
	}

	public static void writeToFile(File file, StringBuffer stringbuf,
			String charset) {
		if (!file.exists()) {
			if(isDetail){
			System.out.println("writeToFile: file(" + file.getAbsolutePath()
					+ ") does not exist.");}
			return;
		}
		writeToFile(file, stringbuf.toString(), charset);
	}

	public static void writeToFile(String fileName, StringBuffer fileContent) {
		writeToFile(fileName, fileContent.toString());
	}

	/***
	 * 把字符串写入文件，并且按照指定的编码
	 * <br>关闭文件流
	 * <br>可以指定编码
	 * @param file
	 * @param fileContent
	 * @param charset : 编码
	 */
	public static void writeToFile(File file, String fileContent, String charset) {
		if (!file.exists()) {
			if(isDetail){
			System.out.println("writeToFile: file(" + file.getAbsolutePath()
					+ ") does not exist.");}
			return;
		}
		OutputStreamWriter osw = null;
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			if(ValueWidget.isNullOrEmpty(charset)){
				charset=SystemHWUtil.CURR_ENCODING;
			}
			if(isDetail){
				System.out.println("FileUtils.writeToFile charset is :"+charset);
			}
			osw = new OutputStreamWriter(os, charset);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(osw==null){
			return;
		}
		BufferedWriter bufferedWriter = new BufferedWriter(osw);
		try {
			bufferedWriter.write(fileContent);
			bufferedWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedWriter != null)
					bufferedWriter.close();
//				if (osw != null)
					osw.close();
				if (os != null)
					os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/***
	 * write inputstream into outputStream .
	 * 
	 * @param ins
	 * @param outs
	 */
	public static void writeIn2Output(InputStream ins, OutputStream outs,
			boolean isCloseOut, boolean isCloseInput) {
		int resultInt = SystemHWUtil.NEGATIVE_ONE;
		byte[] bytes = null;
		bytes = new byte[SystemHWUtil.BUFF_SIZE];

		try {
			while ((resultInt = ins.read(bytes)) != -1) {
				outs.write(bytes, 0, resultInt);
			}
			outs.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (isCloseOut) {
				try {
					outs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (isCloseInput) {
				try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void writeIn2OutputCloseAll(InputStream ins, OutputStream outs) {
		writeIn2Output(ins, outs, true/*isCloseOut*/, true/*isCloseInput*/);
	}

	/***
	 * close outputstream,and donot close inputstream
	 * 
	 * @param ins
	 * @param outs
	 */
	public static void writeIn2Output(InputStream ins, OutputStream outs) {
		writeIn2Output(ins, outs, true/* isCloseOut */, false/* isCloseInput */);
	}

	/***
	 * write inputstream into outputStream ,haven't close stream. slowly
	 * 
	 * @param ins
	 * @param outs
	 */
	@Deprecated
	public static void writeIn2Output2(InputStream ins, OutputStream outs) {
		int resultInt = -1;
		try {
			while ((resultInt = ins.read()) != -1) {
				outs.write(resultInt);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/***
	 * write inputstream into file according to specified length.
	 * 
	 * @param file
	 * @param ins
	 *            : not closed
	 * @param length2
	 * @throws IOException
	 */
	public static FileOutputStream writeInputStream2File(File file,
			InputStream ins, long length2, boolean isCloseOutputStream)
			throws IOException {
		String parentDir = SystemHWUtil.getParentDir(file.getAbsolutePath());
		File fatherFile = new File(parentDir);
		if (!fatherFile.exists()) {
			fatherFile.mkdirs();
		}
		FileOutputStream outs = new FileOutputStream(file);
		int readSize;
		byte[] bytes = null;
		bytes = new byte[(int) length2];

		long length_tmp = length2;
		while ((readSize = ins.read(bytes)) != SystemHWUtil.NEGATIVE_ONE/*-1*/) {
			length_tmp -= readSize;

			outs.write(bytes, 0, readSize);
			if (length_tmp == 0) {
				break;
			}
			// 非常重要，千万不能去掉!!!
			if (length_tmp < SystemHWUtil.BUFF_SIZE/* 4096 */) {
				bytes = new byte[(int) length_tmp];
			}
		}
		outs.flush();
		if (isCloseOutputStream) {
			outs.close();
		}
		return outs;
	}

	/***
	 * haven't close inputstream.
	 * 
	 * @param fileObj
	 * @param outs
	 * @throws FileNotFoundException
	 */
	public static InputStream writeFile2OutputStream(Object fileObj,
			OutputStream outs, boolean isCloseOut) throws FileNotFoundException {
		FileInputStream ins = null;
		if (fileObj instanceof String) {
			ins = new FileInputStream((String) fileObj);
		} else {
			ins = new FileInputStream((File) fileObj);
		}
		writeIn2Output(ins, outs, isCloseOut, false/* isCloseInput */);
		return ins;
	}

	public static OutputStream writeToFile(File file, InputStream ins)
			throws FileNotFoundException {
		if (!file.exists()) {
			if(isDetail){
			System.out.println("writeToFile: file(" + file.getAbsolutePath()
					+ ") does not exist.");}
			return null;
		}
		FileOutputStream fileouts = new FileOutputStream(file);
		writeIn2Output(ins, fileouts);
		return fileouts;
	}

	/***
	 * 是一个一个字节读，效率不高
	 * 
	 * @param file
	 * @param insr
	 */
	@Deprecated
	public static void writeToFile(File file, InputStreamReader insr) {
		if (!file.exists()) {
			System.out.println("writeToFile: file(" + file.getAbsolutePath()
					+ ") does not exist.");
			return;
		}
		try {
			FileOutputStream fileouts = new FileOutputStream(file);
			int resultInt = -1;
			readAndWriteIO(insr, fileouts);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void readAndWriteIO(InputStreamReader insr, FileOutputStream fileouts) {
		int resultInt;
		try {
			while ((resultInt = insr.read()) != -1) {
				fileouts.write(resultInt);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileouts.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/***
	 * write byte[] to file
	 * 
	 * @param bytes
	 * @param destFile
	 * @throws IOException
	 */
	public static void writeBytesToFile(byte[] bytes, File destFile)
			throws IOException {
		if(bytes==null||bytes.length==0){
			System.out.println("bytes is null");
			return;
		}
		FileOutputStream out = new FileOutputStream(destFile);
		write2File(bytes, out);
	}

	/**
	 * Has been tested
	 * 
	 * @param bytes
	 * @param destFilepath
	 * @throws IOException
	 */
	public static void writeBytesToFile(byte[] bytes, String destFilepath)
			throws IOException {
		File destFile = new File(destFilepath);
		writeBytesToFile(bytes, destFile);
	}

	/***
	 * 关闭了FileOutputStream
	 * 
	 * @param bytes
	 * @param out
	 *            :最后会关闭
	 * @throws IOException
	 */
	public static void write2File(byte[] bytes, FileOutputStream out)
			throws IOException {
		out.write(bytes);
		out.close();
	}

	/**
	 * save Object to file
	 * 
	 * @param obj
	 *            Object to save
	 * @param fileName
	 * @throws Exception
	 */
	public static ObjectOutputStream writeObjectToFile(String fileName,
			Object obj, boolean isCloseOutput) throws Exception {
		ObjectOutputStream output = new ObjectOutputStream(
				new FileOutputStream(fileName));
		output.writeObject(obj);
		if (isCloseOutput) {
			output.close();
		}
		return output;
	}

	public static ObjectOutputStream writeObjectToFile(String fileName,
			Object obj) throws Exception {
		return writeObjectToFile(fileName, obj, true);
	}

	/***
	 * 把对象写入输出流
	 * @param outs
	 * @param obj
	 * @param isCloseOutput
	 * @throws IOException
	 */
	public static void writeObjectToOutputStream(OutputStream outs, Object obj,
			boolean isCloseOutput) throws IOException {
		ObjectOutputStream output = new ObjectOutputStream(outs);
		output.writeObject(obj);
		if (isCloseOutput) {
			output.close();
		}
	}

	/***
	 * 插入回车
	 * @param outs
	 * @param count
	 * @param isCloseOutput
	 * @throws IOException
	 */
	public static void writeCRLFToOutputStream(OutputStream outs,int count,
			boolean isCloseOutput) throws IOException {
		if(count<1){
			count=1;
		}
		for(int i=0;i<count;i++){
			outs.write(SystemHWUtil.CRLF.getBytes());
		}
		if (isCloseOutput) {
			outs.close();
		}
	}
	/***
	 * 插入字符串
	 * @param outs
	 * @param str
	 * @param charset
	 * @param isCloseOutput
	 * @throws IOException
	 */
	public static void writeStrToOutputStream(OutputStream outs,String str,String charset,
			boolean isCloseOutput) throws IOException {
		if(ValueWidget.isNullOrEmpty(charset)){
			outs.write(str.getBytes());
		}else{
			outs.write(str.getBytes(charset));
		}
		
		if (isCloseOutput) {
			outs.close();
		}
	}

	/***
	 * 追加字符串到文件末尾
	 * @param file
	 * @param str
	 * @param charset
	 * @param isCloseOutput
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public static void appendStr2File(File file,String str,String charset,boolean isCloseOutput) throws IOException{
		FileOutputStream outs=new FileOutputStream(file, true);
		if(ValueWidget.isNullOrEmpty(charset)){
			outs.write(str.getBytes(SystemHWUtil.CHARSET_UTF));
		}else{
			outs.write(str.getBytes(charset));
		}
		if (isCloseOutput) {
			outs.close();
		}
	}
	
	/***
	 * 没有关闭输出流
	 * 
	 * @param outs : 没有关闭
	 * @param obj
	 * @throws IOException
	 */
	public static void writeObjectToOutputStream(OutputStream outs, Object obj)
			throws IOException {
		writeObjectToOutputStream(outs, obj, false/* isCloseOutput */);
	}

	/**
	 * read object from file
	 * 
	 * @param fileName
	 * @return Object
	 * @throws Exception
	 */
	public static Object readObjectFromFile(String fileName,
			boolean isCloseInput) throws Exception {
		FileInputStream fileInputStream = new FileInputStream(
				fileName);
		ObjectInputStream input = new ObjectInputStream(fileInputStream);
		Object obj = input.readObject();
		if (isCloseInput) {
			input.close();
			fileInputStream.close();
		}
		return obj;
	}

	/**
	 * whether this file exist.
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean isFile(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}

	/**
	 * 
	 * @param files
	 */
	public static void printFileList(ArrayList<File> files) {
		if (null == files || files.size() == 0) {
			System.out.println("files is empty ");
			return ;
		}
		for (int i = 0; i < files.size(); i++) {
			String fileName = files.get(i).getName();
			System.out.println(fileName);
		}
	}

	public static void main(String[] args) {
		// String file="e:/test/a.txt";
		// // System.out.println(isFile(file));
		// open_directory("e:\\test\\cc.txt");
		try {
			System.out.println(sizeOfFile("e:\\test\\cc.txt",null));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/***
	 * 
	 * @param folder
	 *            : directory
	 */
	public static void open_directory(Object folderObj) {
		if (ValueWidget.isNullOrEmpty(folderObj)) {
			return;
		}
		File file = null;
		/*if (folderObj instanceof JTextField) {
			JTextField tf = (JTextField) folderObj;
			file = new File(tf.getText());
		} else */if (folderObj instanceof String) {
			file = new File((String) folderObj);
		} else {
			file = (File) folderObj;
		}
		if (!file.exists()) {
			return;
		}
		Runtime runtime = null;
		try {
			runtime = Runtime.getRuntime();
			if (SystemHWUtil.isWindows) {
				runtime.exec("cmd /c start explorer " + file.getAbsolutePath());
			} else {
				runtime.exec("nautilus " + file.getAbsolutePath());

			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (null != runtime) {
				runtime.runFinalization();
			}
		}
	}

	/***
	 * 
	 * @param filePath
	 *            : only regular file
	 */
	public static boolean open_file(Object folderObj) {
		if (ValueWidget.isNullOrEmpty(folderObj)) {
			return false;
		}
		File file = null;
		/*if (folderObj instanceof JTextField) {
			JTextField tf = (JTextField) folderObj;
			file = new File(tf.getText());
		} else*/ if (folderObj instanceof String) {
			file = new File((String) folderObj);
		} else {
			file = (File) folderObj;
		}
		if (!file.exists()) {
			return false;
		}
		Runtime runtime = null;
		try {
			runtime = Runtime.getRuntime();
			if (SystemHWUtil.isWindows) {
				runtime.exec("cmd /c start explorer /select,/e, "
						+ file.getAbsolutePath());

			} else {
				runtime.exec("nautilus " + file.getAbsolutePath());
				// System.out.println("is linux");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		} finally {
			if (null != runtime) {
				runtime.runFinalization();
			}
		}
		return true;
	}

	/**
	 * 仅限于window 系统
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException 
	 */
	@Deprecated
	public static long sizeOfFile(String filePath,String charset) throws IOException {
		if(ValueWidget.isNullOrEmpty(charset)){
			charset=SystemHWUtil.CURR_ENCODING;
		}
		// BufferedReader reader =CMDUtil.cmdCmdreReader("dir "+filePath);
		String content = CMDUtil.getResult4cmd("dir " + filePath,charset);
		content = content.replaceAll("\r", " ").replaceAll("\n", " ");
		int index = filePath.lastIndexOf('\\');
		String regex = "\\s*((\\d+[,])*\\d+)\\s"
				+ filePath.substring(index + 1);
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		long sizeLong = 0l;
		if (m.find()) {
			String sizeStr = m.group(1);
			if (null != sizeStr && !"".equals(sizeStr)) {
				sizeLong = Long.parseLong(sizeStr);
			}
		}
		return sizeLong;
	}

	/***
	 * Get size of file,byte
	 * 
	 * @param file
	 * @return
	 */
	public static long getFileSize2(File file) {
		return file.length();
	}

	/***
	 * Get size of file,byte
	 * 
	 * @param fileStr
	 * @return
	 */
	public static long getFileSize2(String fileStr) {
		if (ValueWidget.isNullOrEmpty(fileStr)) {
			return 0L;
		}
		File file = new File(fileStr);
		if (file.exists()) {
			return getFileSize2(file);
		} else {
			return -1L;
		}
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String getStringKeyboard() throws IOException {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		return br.readLine();
	}

	public static int getIntKeyboard() {
		Scanner sca = new Scanner(System.in);
		return sca.nextInt();
	}

	// public static String getWebAppAbPath_win(HttpSession session){
	// String appName = session.getServletContext().getServletContextName();
	// String contextPath = session.getServletContext().getRealPath("");
	// int startIndex = contextPath.indexOf(".");
	// int endIndex = contextPath.indexOf(appName);
	// StringBuffer sb = new StringBuffer();
	// sb.append(contextPath.substring(0, startIndex));
	// sb.append(contextPath.substring(endIndex));
	// return sb.toString();
	// }
	// public static String getWebAppAbPath_linux(HttpSession session){
	// String filePath=getWebAppAbPath_win(session);
	// return PathUtils.linuxPath(filePath);
	// }
	/**
	 * make directory recursely
	 * 
	 * @param file
	 * @return
	 */
	public static File mkdirRecurse(File file) {
		int count = countChar(file.getAbsolutePath(), File.separatorChar);
		for (int i = 0; i < count - 1; i++) {
			File parentFile = getParentFile(file, (count - i - 1));
			if (!parentFile.isDirectory()) {
				parentFile.mkdir();
			}
		}
		return file;
	}

	/**
	 * count times the given char appear in string
	 * 
	 * @param s
	 * @param c
	 * @return
	 */
	public static int countChar(String s, char c) {
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == c) {
				count++;
			}
		}
		return count;
	}

	public static File getParentFile(File leafFile, int deep) {
		File parentFile = leafFile;
		for (int i = 0; i < deep; i++) {
			parentFile = parentFile.getParentFile();
		}
		return parentFile;
	}

	/***
	 * 
	 * @param path
	 * @param prefixStr
	 *            :前缀名
	 * @return
	 */
	public static File[] getFilesByPathPrefix(File path, final String prefixStr) {
		return path.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				// System.out.println("prefixStr:"+prefixStr);
				return (ValueWidget.isNullOrEmpty(prefixStr) || (dir.isDirectory() && name
						.startsWith(prefixStr)));
			}
		});

	}
	
	/***
	 * 获取jpg图片文件,根据后缀名
	 * @param path
	 * @return
	 */
	public static File[] getJPGImageFiles(File path) {
		if(!path.exists()){
			return null;
		}
		if(!path.isDirectory()){//如果不是目录
			return new File[]{path};
		}
		//如果是目录
		return path.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return dir.isDirectory() && isJPGImageFile(name);
			}
		});

	}
	/***
	 * 判断是否是图片,根据后缀名,注意:不包含png
	 * @param filePath
	 * @return
	 */
	public static boolean isImageFile(String filePath){
		if(filePath.length()<4){//目录
			return false;
		}
		filePath=filePath.trim();
		String suffixName=filePath.substring(filePath.length()-3);
		System.out.println(suffixName);
		String suffixName4=filePath.substring(filePath.length()-4);
		if(suffixName.equalsIgnoreCase("jpg")||/*suffixName.equalsIgnoreCase("png")||*/suffixName.equalsIgnoreCase("gif")){
			return true;
		}else if(suffixName4.equalsIgnoreCase("jpeg")){
			return true;
		}
		return false;	
	}

	/***
	 * 判断是否是JPG
	 * @param filePath
	 * @return
	 */
	public static boolean isJPGImageFile(String filePath){
		if(filePath.length()<4){//目录
			return false;
		}
		filePath=filePath.trim();
		String suffixName=filePath.substring(filePath.length()-3);
		System.out.println(suffixName);
		String suffixName4=filePath.substring(filePath.length()-4);
		if(suffixName.equalsIgnoreCase("jpg")){
			return true;
		}else if(suffixName4.equalsIgnoreCase("jpeg")){
			return true;
		}
		return false;	
	}

	/***
	 * 判断是否是图片,根据后缀名
	 * @param filePath
	 * @return
	 */
	public static boolean isImageFile(File filePath)
	{
		return isImageFile(filePath.getName());
	}
	/***
	 * 
	 * @param path
	 * @param prefixStr
	 *            :后缀名
	 * @return
	 */
	public static File[] getFilesByPathAndSuffix(File path,
			final String sufixStr) {
		return path.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				// System.out.println("prefixStr:"+prefixStr);
				return (ValueWidget.isNullOrEmpty(sufixStr) || (dir.isDirectory() && name
						.endsWith(sufixStr)));
			}
		});

	}

	/***
	 * 前缀名
	 * 
	 * @param pathStr
	 * @param prefixStr
	 * @return
	 */
	public static File[] getFilesByPathAndPrefix(String pathStr,
			final String prefixStr) {
		File path = new File(pathStr);
		return getFilesByPathPrefix(path, prefixStr);
	}

	/***
	 * 后缀名
	 * 
	 * @param pathStr
	 * @param sufixStr
	 * @return
	 */
	public static File[] getFilesByPathAndSuffix(String pathStr,
			final String sufixStr) {
		File path = new File(pathStr);
		return getFilesByPathAndSuffix(path, sufixStr);
	}

	/***
	 * 获取指定文件夹下的文件，不包含文件夹。未采用递归
	 * 
	 * @param folderFile
	 * @return
	 */
	public static ArrayList<File> getListFilesNoRecursion(File folderFile) {
		ArrayList<File> files = new ArrayList<File>();
		File[] fileArr = folderFile.listFiles();
		if(!ValueWidget.isNullOrEmpty(fileArr)){
			for (int i = 0; i < fileArr.length; i++) {
				File fileOne = fileArr[i];
				if (!fileOne.isDirectory()) {
					files.add(fileOne);
				}
			}
		}
		return files;
	}

	public static List<File> getListFiles(Object obj) {
		return getListFiles(obj, null);
	}
	/***
	 * 获取指定目录下的所有的文件（不包括文件夹），采用了递归
	 * 
	 * @param obj
	 * @return
	 */
	public static List<File> getListFiles(Object obj,String suffix) {
		File directory = null;
		if (obj instanceof File) {
			directory = (File) obj;
		} else {
			directory = new File(obj.toString());
		}
		ArrayList<File> files = new ArrayList<File>();
		if (directory.isFile()&&(suffix==null||directory.getName().endsWith(suffix))) {
			files.add(directory);
			return files;
		} else if (directory.isDirectory()) {
			File[] fileArr = directory.listFiles();
			if(!ValueWidget.isNullOrEmpty(fileArr)){
				for (int i = 0; i < fileArr.length; i++) {
					File fileOne = fileArr[i];
					files.addAll(getListFiles(fileOne,suffix));
				}
			}
		}
		return files;
	}

	/***
	 * delete a directory/folder<br>
	 * 采用了递归
	 * @param someFile
	 *            :directory
	 */
	public static boolean deleteDir(File someFile) {
		if (!someFile.exists()) {
			System.out.println("[deleteDir]File " + someFile.getAbsolutePath()
					+ " does not exist.");
			return false;
		}
		if (someFile.isDirectory()) {// is a folder
			File[] files = someFile.listFiles();
			if (!ValueWidget.isNullOrEmpty(files)) {
				for (File subFile : files) {
					boolean isSuccess = deleteDir(subFile);
					if (!isSuccess) {
						return isSuccess;
					}
				}
			}

		} else {// is a regular file
			boolean isSuccess = someFile.delete();
			if (!isSuccess) {
				return isSuccess;
			}
		}
		return !someFile.isDirectory() || someFile.delete();
	}

	/***
	 * Formatted file size:1,000,001,000,000
	 * 
	 * @param size
	 * @return
	 */
	public static String formatFileSize(long size) {
		DecimalFormat df1 = (DecimalFormat) DecimalFormat.getInstance();
		df1.setGroupingSize(3);
		return df1.format(size);

	}
	/***
	 * "1234567"->"1,234,567"
	 * @param input
	 * @return
	 */
	public static String formatFileSize(String input) {
		String regx="(?<=\\d)(\\d{3})";
//		System.out.println(input.replaceAll(regx, ",$1"));
		Pattern pattern=Pattern.compile(regx);
		Matcher matcher=pattern.matcher(input);
		StringBuffer sb = new StringBuffer();
		 while(matcher.find()){
	        String mStr=matcher.group(1);
//              System.out.println(mStr);
	            matcher.appendReplacement(sb, ","+mStr);
        }
		 matcher.appendTail(sb);
		 return sb.toString();
	}
	/***
	 * 读取指定长度的字节，存放到数组中.
	 * 
	 * @param fin
	 * @param bytes
	 * @param start
	 *            : 数组前start 个元素已经有值了
	 * @param length2
	 *            : 还要读取的字节数
	 * @param isCloseInputStream
	 * @throws IOException
	 */
	public static void readBytes(InputStream fin, byte[] bytes, int start,
			int length2, boolean isCloseInputStream) throws IOException {
		int length_tmp = length2;
		int readSize = 0;
		while ((readSize = fin.read(bytes, start, length_tmp)) != SystemHWUtil.NEGATIVE_ONE) {
			start = start + readSize;
			length_tmp -= readSize;
			if (length_tmp == 0) {
				break;
			}
		}
		if (isCloseInputStream) {
			fin.close();
		}
	}

	/***
	 * Not responsible for closing the output and input stream 写入指定长度的字节到输出流
	 * 
	 * @param fin
	 * @param fout
	 *            : The divided file
	 * @param length2
	 * @throws IOException
	 */
	public static void writeFromFile2File(InputStream fin, OutputStream fout,
			long length2) throws IOException {
		if (SharedMemoryArea.isWillStop()) {
			return;
		}
		if (length2 == 0) {// want to write zero bytes
			// if (fout != null) {
			// fout.close();
			// }
			return;
		}
		int readSize;
		byte[] bytes = null;
		if (length2 >= SystemHWUtil.BUFF_SIZE) {
			bytes = new byte[SystemHWUtil.BUFF_SIZE];
		} else {
			bytes = new byte[(int) length2];
		}

		long length_tmp = length2;
		while ((readSize = fin.read(bytes)) != SystemHWUtil.NEGATIVE_ONE) {
			if (SharedMemoryArea.isWillStop()) {
				break;
			}
			length_tmp -= readSize;
			fout.write(bytes, 0, readSize);
			if (length_tmp == 0) {
				break;
			}
			// 非常重要，千万不能删除
			if (length_tmp < SystemHWUtil.BUFF_SIZE) {
				bytes = new byte[(int) length_tmp];
			}
		}

	}

	/***
	 * 从上面的方法（writeFromFile2File）抄过来的
	 * 
	 * @param fin
	 * @param fout
	 * @param length2
	 * @throws IOException
	 */
	public static void writeFromIn2Out(InputStream fin, OutputStream fout,
			long length2, boolean isCloseInputStream,
			boolean isCloseOutputStream) throws IOException {
		if (length2 == 0) {// want to write zero bytes
			// if (fout != null) {
			// fout.close();
			// }
			return;
		}
		int readSize;
		byte[] bytes = null;
		if (length2 >= SystemHWUtil.BUFF_SIZE) {
			bytes = new byte[SystemHWUtil.BUFF_SIZE];
		} else {
			bytes = new byte[(int) length2];
		}

		long length_tmp = length2;
		while ((readSize = fin.read(bytes)) != SystemHWUtil.NEGATIVE_ONE) {
			length_tmp -= readSize;
			fout.write(bytes, 0, readSize);
			if (length_tmp == 0) {
				break;
			}
			// 非常重要，千万不能删除
			if (length_tmp < SystemHWUtil.BUFF_SIZE) {
				bytes = new byte[(int) length_tmp];
			}
		}
		fout.flush();
		if (isCloseOutputStream) {
			fout.close();
		}
		if (isCloseInputStream) {
			fin.close();
		}

	}

	/***
	 * Responsible for closing the output stream
	 * 
	 * @param fin
	 * @param outPutFile
	 * @param length2
	 *            :The number of bytes to be written
	 * @param append
	 *            : Whether additional
	 * @throws IOException
	 */
	public static void writeFromFile2File(InputStream fin, File outPutFile,
			long length2, boolean append) throws IOException {
		if (SharedMemoryArea.isWillStop()) {
			return;
		}
		if (length2 == 0) {// want to write zero bytes
			return;
		}
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(outPutFile, append/* 追加 */);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			writeFromFile2File(fin, fout, length2);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fout!=null){
				fout.flush();
				fout.close();// Close the stream
				fout=null;
			}
			
		}
	}

	/***
	 * 深拷贝对象
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T clone(T obj) {
		// 拷贝产生的对象
		T cloneObj = null;

		// 读取对象字节数据
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(baos);
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out
					.println("[FileUtils.clone]create ObjectOutputStream failed.");
		}
		if(oos!=null){
			try {
				oos.writeObject(obj);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("[FileUtils.clone]writeObject " + obj
						+ " failed.");
			}
		}
		
		try {
			if(oos!=null){
				oos.close();
				oos=null;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 生成新对象
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(bais);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(ois!=null){
			try {
				cloneObj = (T) ois.readObject();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		try {
			if(ois!=null){
				ois.close();
				ois=null;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cloneObj;

	}

	/***
	 * 从URL中读取输入流.
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static byte[] readBytes(URL url) throws IOException {
		InputStream in = url.openStream();
		if(isDetail){
			System.out.println("FileUtils.readBytes url:"+url);
		}
		return readBytes3(in);
	}

	/***
	 * 
	 * @param url
	 * @param charset
	 *            : 字符编码
	 * @return
	 * @throws IOException
	 */
    public static String getFullContent(URL url, String charset, boolean isCloseStream)
            throws IOException {
		if(isDetail){
			System.out.println("FileUtils.readBytes url:"+url);
		}
		InputStream in = url.openStream();
        return getFullContent2(in, charset, isCloseStream);
    }

	/***
	 * 格式化 文件的大小，更具人性化.
	 *
     * @param size2
     * @return
	 */
	@Deprecated
	public static String formatSize(long size2) {
		double size = (double) size2;
		long abc = 1024;
		int b = 10;
		String unit2 = "B";
		int result = 0;

		long mod2 = abc * abc;
		long mod3 = mod2 * abc;
		if (size > mod3 * b) {
			result = (int) Math.round((size / mod3));
			unit2 = "GB";
		} else if (size > mod2 * b) {
			result = (int) Math.round(size / mod2);
			unit2 = "MB";
		} else if (size > abc * b) {
			result = (int) Math.round(size / (abc));
			unit2 = "KB";
		} else {
			result = (int) size;
		}
		return (new StringBuffer().append(result).append(" ").append(unit2)
				.toString());
	}
	public static String formatFileSize2(File file) {
		return formatFileSize2(file,true);
	}
	public static String formatFileSize2(File file,boolean append) {
		return formatFileSize2(file, 3,append);
	}
	public static String formatFileSize2(File file,int numAfterDot,boolean append) {
		if(ValueWidget.isNullOrEmpty(file)){
			return String.valueOf(SystemHWUtil.NEGATIVE_ONE);
		}
		if(file.exists()){
			return formatFileSize2(file.length(),numAfterDot,append);
		}else{
			return null;
		}
	}
	public static String formatFileSize2(String file) {
		return formatFileSize2(file,true);
	}
	public static String formatFileSize2(String file,boolean append) {
		return formatFileSize2(file,3,append);
	}
	public static String formatFileSize2(String file,int numAfterDot,boolean append) {
		if(ValueWidget.isNullOrEmpty(file)){
			return String.valueOf(SystemHWUtil.NEGATIVE_ONE);
		}
		return formatFileSize2(new File(file),numAfterDot,append);
	}
	public static String formatFileSize2(long size){
		return formatFileSize2(size,true);
	}
	public static String formatFileSize2(long size,boolean append){
		return formatFileSize2(size,3,append);
	}
	public static String formatFileSize2(long size,int numAfterDot,boolean append){
		return formatFileSize2(size,2,numAfterDot,append);
	}
	public static String formatFileSize3(long size,boolean append){
		return formatFileSize3(size,3,append);
	}
	public static String formatFileSize3(long size,int numAfterDot,boolean append){
		return formatFileSize2(size,3,numAfterDot,append);
	}
	public static String formatFileSize5(long size,boolean append){
		return formatFileSize5(size,3,append);
	}
	public static String formatFileSize5(long size,int numAfterDot,boolean append){
		return formatFileSize2(size,5,numAfterDot,append);
	}
	/***
	 * example:1,000.001GB 小数点后保留3位，大于10000，才升一级单位, 注意：采用四舍五入，相当于Math.round
	 * 
	 * @param size
	 * @return
	 */
	public static String formatFileSize2(long size,int fineness,int numAfterDot,boolean append) {
		if(size<0){
			return String.valueOf(SystemHWUtil.NEGATIVE_ONE);
		}
		float shang = (float) size;// 商
		int seq = 0;
		while (shang >= (fineness*512/*1024/2*/)) {
			shang = shang / 1024L;
			seq++;
		}
		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
		// 小数点后面必须是两位，987.3 是不对的；987.30 是对的。
		df.setMinimumFractionDigits(numAfterDot);//3
		df.setMaximumFractionDigits(numAfterDot);//3
		String sizeStr = df.format(shang).replaceAll("[0]+$$", "0");/*
																	 * 若后面都是零，
																	 * 则只显示一个零。
																	 */
		String result=sizeStr + SystemHWUtil.UNIT_SIZE[seq];
		if(append){
			result+= "("+ formatFileSize(size) + " Byte)";
		}
		return  result;
	}

	/***
	 * 判断两个文件是否是相同的文件，但是不精确
	 * 
	 * @param file1
	 * @param file2
	 * @return
	 */
	public static boolean isSameFile(File file1, File file2) {
		return file1 == file2 || file1.getAbsolutePath().equals(file2.getAbsolutePath()) || file1.length() == file2.length();
	}

	public static void setDetail(boolean isDetail) {
		FileUtils.isDetail = isDetail;
	}
	/***
	 * 比较两个文件的MD5是否相同(先比较文件长度)
	 * @param srcfile
	 * @param targfile
	 * @return
	 */
	public static boolean isSameMd5(File srcfile,File targfile){
		long size_of_targfile = targfile.length();
		long size_of_srcfile = srcfile.length();
		if (size_of_targfile != size_of_srcfile) {
			System.out.println("by size");
//			GUIUtil23.errorDialog(MESG_DIFF);
			return false;
		}
		return SystemHWUtil.isSameMd5(targfile, srcfile);
	}

	/***
	 * "D:\\picture\\scan\\美文摘抄.pdf" <br>-->"D:\picture\scan\美文摘抄_aaa.pdf"
	 * @param oldPath
	 * @param append
	 * @return
	 */
	public static File modifyFilePath(String oldPath,String append){
		String filePath2=SystemHWUtil.getParentDir(oldPath);
		String simpleName=SystemHWUtil.getFileSimpleName(oldPath);
		String nameOnly=simpleName.replaceAll("\\.[a-zA-Z0-9_]+$", "");
		String suffix222=SystemHWUtil.getFileSuffixName(oldPath);
		String new_name=nameOnly+"_"+append;
		return new File(filePath2,new_name+SystemHWUtil.ENGLISH_PERIOD+suffix222);
	}
	public static File modifyFilePath(File oldPath,String append){
		return modifyFilePath(oldPath.getAbsolutePath(), append);
	}
	/***
	 * 判断父目录parentFolderStr 是否有文件subFileStr(也可以是目录)
	 * @param parentFolderStr
	 * @param subFileStr
	 * @return : 返回null,说明不存在
	 */
	public static File subFileExist(String parentFolderStr,String subFileStr)
	{
		if(!parentFolderStr.endsWith(File.separator)){
			parentFolderStr+=File.separator;
		}
		File subFolder=new File(parentFolderStr+subFileStr);
		if(subFolder.exists()){
			return subFolder;
		}else{
			return null;
		}
	}
	/***
	 * 根据文件的后缀名判断文件是否是PDF文件<br>
	 * test ok!
	 * @param file
	 * @return
	 */
	public static boolean isPDF(String file){
		String suffix=SystemHWUtil.getFileSuffixName(file);
		System.out.println(suffix);
		return suffix.equalsIgnoreCase("pdf");
	}
	/***
	 * 判断文件是否被占用<br>
	 * 
	 * @param file
	 * @return : true:被占用;<br>
	 * false:没有被占用
	 */
	public static boolean isOccupy(File file){
		if(file.renameTo(file)){   
		  System.out.println("文件未被操作");
		  return false;
		}else{   
			System.out.println("文件正在被操作");
			return true;
		}
	}
	/***
	 * 
	 * @param file
	 * @return : 最后是否可写
	 */
	public static boolean makeWritable(File file) {
		return file.canWrite() || file.setWritable(true);
	}
}
