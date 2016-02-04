package com.cmd.dos.hw;

import com.kunlunsoft.isch.util.IsChineseUtil;

public class Conversion
{
    /**
     * chinese to unicode
    * @param str
     * @return unicode
    */
    public static String chinaToUnicode(String str)
    {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++)
        {
            char chr1 =  str.charAt(i);
            if (IsChineseUtil.isChinese( chr1))
            {
                result.append("\\u" + Integer.toHexString((char)chr1));
            }else{
                result.append(chr1);
            }
        }
        return result.toString();
    }

    /**
    * unicode to Chinese
    * @param  str
    * @return  Chinese
    */
    public static String unicodeToChinese(String str)
    {
        StringBuilder sb = new StringBuilder();
        char[] charArray=str.toCharArray();
        for (char c : charArray)
        {
            sb.append(c);
        }
        return sb.toString();
    }

    public static void main(String args[])
    {
        String str = "\u4e2d\u56fd";

        System.out.println(unicodeToChinese(str));
        //for(char c : str.toCharArray())         
        //System.out.print(c); 

    }

}
