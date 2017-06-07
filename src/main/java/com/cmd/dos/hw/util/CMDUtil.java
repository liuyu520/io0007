package com.cmd.dos.hw.util;

import com.common.util.SystemHWUtil;
import com.io.hw.file.util.FileUtils;
import com.string.widget.util.ValueWidget;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class CMDUtil {
	private static final String filePathshort = "c:" + File.separator + "test";
	private static final String filename = "a.txt";
	private static final String filePath_name = filePathshort + File.separator
			+ filename;
	
	
 public static Process executeCmd(String command)
    {
        Process p = null;
        try
        {
            p = Runtime.getRuntime().exec(SystemHWUtil.CMD_SHORT + command);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return p;
    }

    public static BufferedReader cmdCmdreReader(String command, String charset) throws IOException
    {
    	if(ValueWidget.isNullOrEmpty(charset)){
    		charset=SystemHWUtil.CURR_ENCODING;
    	}
        Process p = null;
        BufferedReader reader = null;
        try
        {
			String command2 = null;
			if (SystemHWUtil.isWindows) {
				command2 = SystemHWUtil.CMD_SHORT + command;
			} else {
				command2 = command;
			}

            p = Runtime.getRuntime().exec(command2);
            System.out.println(command2);
            reader = new BufferedReader(new InputStreamReader(p.getInputStream(),charset));
            byte[] bytes = FileUtils.readBytes3(p.getErrorStream());
            if (!ValueWidget.isNullOrEmpty(bytes)) {
                System.out.println(new String(bytes, charset));
                if (SystemHWUtil.isWindows) {
                    System.out.println(new String(bytes, SystemHWUtil.CHARSET_GBK));
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw e;
        }
        return reader;
    }
	

	public static String cmdCmdreStr(String command,String charset) throws IOException 
	{
		BufferedReader reader = cmdCmdreReader(command,charset);
		return FileUtils.getFullContent3(reader).toString();
	}

	/**
	 *  Unicode -->\u4e2d\u56fd
	 *
	 * @param str
	 * @return
	 */
	/*
	 * public static String native2Unicode(String nativeStr){
	 * FileUtils.writeToFile(filePath_name, nativeStr); return
	 * cmdCmdreStr("native2ascii "+filePath_name); }
	 */
	public static String toUnicode(String str) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			int chr1 = (char) str.charAt(i);
			result.append("\\u" + Integer.toHexString(chr1));

		}
		return result.toString();
	}

	/**
	 *  Unicode  \u4e2d\u56fd-->
	 * <br>要执行操作系统本地命令
	 * @param nativeStr
	 * @return
	 * @throws IOException 
	 */
	public static String unicode2Native(String nativeStr) throws IOException {
        FileUtils.writeToFile(filePath_name, nativeStr, false);
        return cmdCmdreStr("native2ascii -reverse " + filePath_name,null);
	}

	public static String resolveUnicode(String fileContent, boolean isToUnicode) {
		if (!isToUnicode) {
			try {
				return unicode2Native(fileContent);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		} else {
			return toUnicode(fileContent);
		}
	}
	public static String getResult4cmd(String cmd) throws IOException{
		return getResult4cmd(cmd, null);
	}
	public static String getResult4cmd(String cmd,String charset) throws IOException{
		if(ValueWidget.isNullOrEmpty(charset)){
			charset=SystemHWUtil.CURR_ENCODING;
		}
		BufferedReader reader =CMDUtil.cmdCmdreReader(cmd,charset);
        StringBuffer stringBuf = FileUtils.getFullContent3(reader);
        if (null == stringBuf) {
            return null;
        }
        return stringBuf.toString();
    }
	public static void main(String[] args) {
//		String filePath="e:\\test\\a.txt";
		String filePath="e:\\Java\\jdk\\jdk-6u27-windows-x64.exe";
		String content;
		try {
			content = CMDUtil.getResult4cmd("dir "+filePath,SystemHWUtil.CURR_ENCODING);
			System.out.println(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/***
	 * 
	 * @param command
	 * @param cmdFolder : 命令的路径
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String execute(String[]command,String cmdFolder,String charset) throws IOException{
		 BufferedReader reader = null;
		 Process p = null;
		 String errorInputStr = null;
        String content = null;
        try {
//	        	String commandFolder=;
	        	if(ValueWidget.isNullOrEmpty(charset)){
	        		charset=SystemHWUtil.CURR_ENCODING;
	        	}
	        	if(ValueWidget.isNullOrEmpty(cmdFolder)){
	        		p = Runtime.getRuntime().exec(command);
	        	}else{
	        		File cmdFolder2=null;
	        		if(!ValueWidget.isNullOrEmpty(cmdFolder)){
	        			cmdFolder2=new File(cmdFolder/*命令的所在目录*/);
	        			p = Runtime.getRuntime().exec(command, null,cmdFolder2/*命令的路径*/);//)
	        		}else{
	        			p = Runtime.getRuntime().exec(command, null);//)
	        		}
	        		
	        		
	        	}
	            reader = new BufferedReader(new InputStreamReader(p
	                    .getInputStream(),charset));
	            errorInputStr=FileUtils.getFullContent4(p.getErrorStream(),charset);
	            if(!ValueWidget.isNullOrEmpty(errorInputStr)){
	            	System.out.println("error:"+errorInputStr);
	            }
	            StringBuilder sb = new StringBuilder();
	            String readedLine = null;
	            try
	            {
	                while ((readedLine = reader.readLine()) != null)
	                {
	                    sb.append(readedLine);
	                    sb.append("\r\n");
	                }
	            }
	            catch (IOException e)
	            {
	                e.printStackTrace();
	            }
	            finally
	            {
	                try
	                {
	                    reader.close();
	                    p.destroy();
	                }
	                catch (IOException e)
	                {
	                    e.printStackTrace();
	                }
	            }
                content = sb.toString();
            System.out.println(content);
	        }
	        catch (IOException e)
	        {
	            e.printStackTrace();
	            throw e;
	        }
        if (ValueWidget.isNullOrEmpty(content)) {
            return errorInputStr;
        } else {
            return errorInputStr + SystemHWUtil.CRLF + content;
        }
    }
	/***
	 * 仅适用于windows 系统,会调用本地命令<br>
	 * hide:attrib ".mqtt_client.properties" +H<br>
	 * show:attrib ".mqtt_client.properties" -H
	 * @param filePath
	 * @return
	 */
	public static int hide(String filePath){
		return modifyAttrib(filePath, "+H");

	}

	public static int modifyAttrib(String filePath, String flags) {
		if (SystemHWUtil.isWindows && new File(filePath).exists()) {
			Process p = CMDUtil.executeCmd("attrib " + filePath + " " + flags);
			try {
				p.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return SystemHWUtil.NEGATIVE_ONE;
			}
			return p.exitValue();
		} else {
			return SystemHWUtil.NEGATIVE_ONE;
		}
	}

	public static int hide(File filePath){
		return hide(filePath.getAbsolutePath());
	}
	/***
	 * 仅适用于windows 系统,会调用本地命令<br>
	 * hide:attrib ".mqtt_client.properties" +H<br>
	 * show:attrib ".mqtt_client.properties" -H
	 * @param filePath
	 * @return
	 */
	public static int show(String filePath){
		return modifyAttrib(filePath, "-H");
	}
	public static int show(File filePath){
		return show(filePath.getAbsolutePath());
	}
	
	public static void startTomcatAction(String cmdFolder){
		try {
			CMDUtil.execute(new String[]{"cmd"," /c ","startup.bat"}, cmdFolder, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void shutdownTomcatAction(String cmdFolder){
		try {
			CMDUtil.execute(new String[]{"cmd"," /c ","shutdown.bat"}, cmdFolder, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/***
	 * 设置为只读
	 * @param filePath
	 * @return
	 */
	public static int readOnly(String filePath){
		return modifyAttrib(filePath, "+R");
	}
	
	/***
	 * 设置为可写
	 * @param filePath
	 * @return
	 */
	public static int removeReadOnly(String filePath){
		return modifyAttrib(filePath, "-R");
	}
	public static String getRealMessage(String message, String type) {
		message = message.replaceAll("^" + type + "\\b[\\s]*([^\\s].*)$", "$1");
		return message;
	}
}
