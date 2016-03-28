package com.test;

import com.common.util.ProcessUtil;
import com.io.hw.file.util.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class TestCmd {

    //	@Test
    public void test_001() {
        String cmd = "runas /env /user:Administrator \"notepad C:\\Windows\\System32\\drivers\\etc\\hosts\"";
        try {
            final Process p = ProcessUtil.getPro("cmd /K " + cmd);
            String result;
            InputStream ins = p.getInputStream();
//			String result=FileUtils.getFullContent2(ins,"GBK");
//			System.out.println(result);
//			p.waitFor();
//			outs.flush();
            ins = p.getInputStream();
            result = FileUtils.getFullContent2(ins, "GBK", false);
            System.out.println(result);
            p.waitFor();
            /*new Thread() {
	            public void run() {
	            	InputStream ins=p.getInputStream();
//	    			String result=FileUtils.getFullContent2(ins,"GBK");
//	    			System.out.println(result);
	    			
//	    			outs.flush();
	    			ins=p.getInputStream();
	    			String result = null;
					try {
						result = FileUtils.getFullContent2(ins,"GBK");
					} catch (IOException e) {
						e.printStackTrace();
					}
	    			System.out.println(result);
	            }
	        }.start();
	        */


//			Thread.sleep(3000);
//			new Thread() {
//	            public void run() {
            OutputStream out = p.getOutputStream();
            PrintWriter writer = new PrintWriter(out);
            writer.write("yunma\n\r");
            writer.flush();
            writer.close();
            System.out.println(p.waitFor());
	                /*Scanner scanner = new Scanner(System.in);
	                String str = null;
	                while ((str = scanner.nextLine()) != null) {
	                    writer.println(str);
	                    writer.flush();
	                }*/
//	            }
//	        }.start();
            Thread.sleep(1000);
            ins = p.getErrorStream();
            result = FileUtils.getFullContent2(ins, "GBK", false);
            System.out.println(result);
//			OutputStream outs=p.getOutputStream();
//			FileUtils.writeStrToOutputStream(outs, "yunma", SystemHWUtil.CURR_ENCODING, true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
