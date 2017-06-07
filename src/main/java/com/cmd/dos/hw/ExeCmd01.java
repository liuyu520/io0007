package com.cmd.dos.hw;

import com.common.util.SystemHWUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ExeCmd01
{
    public static void main(String[] args)
    {
        String cmd = SystemHWUtil.CMD_SHORT+"ping ";//˵���� /C ��ʾ��ִ�н����ʾ�� console �С�
        String ipprefix = "192.168.209.";
        int begin = 100;
        int end = 800;
        Process p = null;
        List<String> ip_avaliable = new ArrayList<String>();
        for (int i = begin; i < end; i++)
        {
            try
            {
                p = Runtime.getRuntime().exec(cmd + ipprefix + i);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            String line = null;
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(p
                        .getInputStream(), SystemHWUtil.CHARSET_UTF));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try
            {
                while ((line = reader.readLine()) != null)
                {
                    //                    System.out.println(line);
                    if (line.startsWith("Reply from"))
                    {
                        ip_avaliable.add(ipprefix + i);
                        System.out.println(ipprefix + i);
                        break;
                    }
                    else if (line.startsWith("Request timed out"))
                    {
                        break;
                    }
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                reader.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            p.destroy();
        }
        for (int i = 0; i < ip_avaliable.size(); i++)
        {
            System.out.println(ip_avaliable.get(i));
        }
    }
    
}
