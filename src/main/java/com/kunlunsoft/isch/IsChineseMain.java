package com.kunlunsoft.isch;

import java.util.Scanner;

import com.kunlunsoft.isch.util.IsChineseUtil;

public class IsChineseMain
{
    public static void main(String[] args)
    {
//        char input='，';
//        System.out.println(IsChineseUtil.isChinese(input));
        main1();
    }
    public static void main1(){
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        System.out.println("input: " + input);
        boolean bl=IsChineseUtil.isHasChinses2(input);
        if(!bl){
            System.out.println("没有中文");
        }
    }

}
