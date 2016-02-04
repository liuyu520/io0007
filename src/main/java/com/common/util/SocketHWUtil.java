package com.common.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.string.widget.util.ValueWidget;

public final class SocketHWUtil {
	/***
	 * read from socket InputStream.
	 * 
	 * @param ins  : 最后没有关闭
	 * @param charset : 字符编码
	 * @return
	 * @throws IOException
	 */
	public static String readSocket(InputStream ins,String charset) throws IOException {
		if(ValueWidget.isNullOrEmpty(charset)){
			charset=SystemHWUtil.CURR_ENCODING;
		}
		int step = SystemHWUtil.BUFF_SIZE_1024 ;
		InputStreamReader bis = new InputStreamReader(ins,charset);
		// Data's byte array
		char[] receData = new char[step];

		// data length read from the stream
		int readLength = 0;

		// data Array offset
		int offset = 0;

		// Data array length
		int byteLength = step;

		while (bis.ready()
				&& (readLength = bis
						.read(receData, offset, byteLength - offset)) != -1) {
			// Calculate the current length of the data
			offset += readLength;
			// Determine whether you need to copy data , when the remaining
			// space is less than step / 2, copy the data
			if (byteLength - offset <= step / 2) {
				char[] tempData = new char[receData.length + step];
				System.arraycopy(receData, 0, tempData, 0, offset);
				receData = tempData;
				byteLength = receData.length;
			}
		}
		return new String(receData, 0, offset);
	}

	private static void bindPort(String host, int port) throws Exception {
		Socket s = new Socket();
		s.bind(new InetSocketAddress(host, port));
		s.close();
	}

	public static boolean isOcupy(String ip, int port) {
		try {
			bindPort(ip, port);
			bindPort(InetAddress.getLocalHost().getHostAddress(), port);

			return false;
		} catch (Exception e) {
			// e.printStackTrace();
			return true;
		}
	}
	
	public static boolean isOcupy(int port) {
		return isOcupy("0.0.0.0", port);
	}
	/***
	 * 
	 * @param s : Socket
	 * @param send : 发送到socket的字节数组
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String readSocket(Socket s,byte[]send,String charset) throws IOException{
		if(ValueWidget.isNullOrEmpty(send)){
			return null;
		}
		OutputStream out=s.getOutputStream();
		BufferedOutputStream bout=new BufferedOutputStream(out);
		bout.write(send);
		bout.flush();
//		bout.close();
		InputStream ins=s.getInputStream();
		if(ValueWidget.isNullOrEmpty(charset)){
			charset=SystemHWUtil.CHARSET_UTF;
		}
		String result=readSocket(ins, charset);
		
		return result;
	}

}
