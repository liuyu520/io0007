package com.kunlunsoft.isch.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.kunlunsoft.isch.excep.MyException;

public class IsChineseUtil
{
    /**
     * 字节数大于1，则返回true
     * @param c
     * @return
     */
    public static boolean isChinese(char c)
    {
        return String.valueOf(c).getBytes().length > 1;
    }

    public static void main(String[] args)
    {
        String input = "，。，";
        System.out.println(isAllChinese(input));
    }

    /**
     * 判断str 中的所有字符是否全部是中文字符（包括中文的标点符号）
     * @param str
     * @return
     */
    public static boolean isAllChinese(String str)
    {
        char[] cs = null;
        if (str.length() == 1)
        {
            cs = new char[1];
            cs[0] = str.charAt(0);
        }
        else
        {
            cs = str.toCharArray();
        }
        for (int i = 0; i < cs.length; i++)
        {
            char c = cs[i];
            if (!isChinese(c))
            {
                return false;
            }
        }
        return true;
    }

    public static boolean isHasChinses(String str)
    {
        String encodeName = "UTF-8";
        for (int i = 0; i < str.length(); i++)
        {
            try
            {
                String singleStr = str.substring(i, i + 1);
                int leng = getEncodeLength(singleStr, encodeName);
                //                System.out.println(singleStr + "\t" + leng);
                if (leng == 9)//表示是中文字符
                {
//                    System.out.println("有中文");
                    return true;
                }
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            catch (MyException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean isHasChinses2(String str)
    {
        String encodeName = "UTF-8";
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++)
        {
            try
            {
                char c = chars[i];
                int leng = getEncodeLength(c, encodeName);
                //                System.out.println(singleStr + "\t" + leng);
                if (leng == 9)//表示是中文字符
                {
//                    System.out.println("有中文");
                    return true;
                }
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            catch (MyException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static int getEncodeLength(String str, String encodeName)
            throws MyException, UnsupportedEncodingException
    {//返回值为9 的话，则说明有中文。
        if (str.length() != 1)
        {
            throw new MyException("超过一个字符");
        }
        String encod = URLEncoder.encode(str, "UTF-8");
        return encod.length();
    }

    public static int getEncodeLength(char c, String encodeName)
            throws MyException, UnsupportedEncodingException
    {//返回值为9 的话，则说明有中文。
        return getEncodeLength(String.valueOf(c), encodeName);
    }

}
