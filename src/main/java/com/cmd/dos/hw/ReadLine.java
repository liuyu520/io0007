
package com.cmd.dos.hw;

/*从键盘上读取字节，输出字节，遇到Byte时退出
 .构造方法:
 .String(byt[] bytes,int offset,int length)
 .equalsIgnoreCase方法,不区分大小写
 .indexOf(int ch)方法，用于返回字符首次出现的位置
 */
class ReadLine {
    public static void main(String[] args)
    {
        byte[] buf = new byte[1024];     //字节当中输入字符最大为1024个字节
        String strInfo = null;                  //字符串对象
        int pos = 0;                                 //显示初始化未知
        int ch = 0;                                   //每次读取的字节
        Conversion conv = new Conversion();    
        System.out.println("请输入汉字:"); 
        while (true)                                   //不停的读取键盘输入
        {
            try
            {
                ch = System.in.read();         //从键盘上读取字节
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            switch (ch)
            {
                case '\r' :                                     //回车时不处理
                    break;
                case '\n' :                                     //换行                   
                
                strInfo = new String(buf, 0, pos);                 //转换字节为字符从buff的0个开始到pos个转换
                if (strInfo.equalsIgnoreCase("bye"))
                {
                    return;                                     //返回主函数
                }
                else
                {
                                                                        //输出unicode编码
                    //conv.unicodeToChinese(strInfo);
                    System.out.println(conv.chinaToUnicode(strInfo).substring(4));
                    pos = 0;                                      //清零
                    break;                                          //跳出来
                }
                default :
                    buf[pos] = (byte) ch;             //CH附值给buf的pos
                    pos++;
            }
        }
    }
}

