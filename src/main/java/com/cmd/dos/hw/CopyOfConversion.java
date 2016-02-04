package com.cmd.dos.hw;

public class CopyOfConversion
{
    /**
     * ����תunicode
    * @param str
     * @return ����unicode����
    */
    public static String chinaToUnicode(String str)
    {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++)
        {
            int chr1 = (char) str.charAt(i);
            result.append("\\u" + Integer.toHexString(chr1));
        }
        return result.toString();
    }

    /**
    * unicodeת����
    * @param  str
    * @return ����
    */
    public static String unicodeToChinese(String str)
    {
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray())
        {
            sb.append(c);
        }
        return sb.toString();
    }

    public static void main(String args[])
    {
        //unicodeת����
        String str = "\u9ec4\u5a01";
        CopyOfConversion con = new CopyOfConversion();

        System.out.println(con.unicodeToChinese(str));
        //for(char c : str.toCharArray())         
        //System.out.print(c); 

    }

}
