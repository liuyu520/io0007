package com.path.hw.test;

import com.path.hw.PathUtils;

public class PathTest
{
    public static void main(String[] args)
    {
//        String input="E:\\kingbase\\��������\\V7\\src\\doc\\kingbase\\doc\\src";
//        System.out.println(input);
//        System.out.println(PathUtils.windowsPath(input));
//        System.out.println(PathUtils.linuxPath(input));
//    	PropUtil propu=new PropUtil("com/path/hw/conf.properties");
//    	System.out.println(propu.getStr("oldregex"));
//    	System.out.println(Thread.currentThread().getContextClassLoader().getResource("" ));  
//    	  
        System.out.println(PathUtils.class .getClassLoader().getResource("" ));  
//  
        System.out.println(ClassLoader.getSystemResource("" ));  
//        System.out.println(PathUtils.class .getResource("" ));  
//        System.out.println("path: "+PathUtils.class .getResource("/" ));
//        System.out.println(new  File("/" ).getAbsolutePath());  
        System.out.println(System.getProperty("user.dir" ));  
    }
}
