package com.file.hw.filter.impl;

import com.file.hw.filter.infac.IFileFilter;

import java.io.File;

public class NoHiddenFileFilter implements IFileFilter
{
	/**
	 * �������ļ����򷵻�false
	 * ���������ļ����򷵻�true
	 */
    @Override
    public boolean isRight(File file)//����Ҫ���򷵻�false ,��Ҫ���򷵻� true.
    {
        return !file.isHidden();
    }

}
