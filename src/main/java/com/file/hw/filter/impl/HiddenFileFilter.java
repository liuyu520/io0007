package com.file.hw.filter.impl;

import com.file.hw.filter.infac.IFileFilter;

import java.io.File;
/**
 * �ж��ļ��Ƿ��������ļ�
 * @author Administrator
 *
 */
public class HiddenFileFilter implements IFileFilter
{

    public static void main(String args[]) {
    	HiddenFileFilter hff=new HiddenFileFilter();
    	System.out.println(hff.isRight(new File("e:/test/b.txt")));

	}

	/**
	 * �������ļ����򷵻�true
	 * ���������ļ����򷵻�false
	 */
    @Override
    public boolean isRight(File file)//����Ҫ���򷵻�false ,��Ҫ���򷵻� true.
    {
        return file.isHidden();
    }

}
