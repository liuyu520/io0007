package com.file.hw.filter.impl;

import com.file.hw.filter.infac.IFileFilter;

import java.io.File;

public class JavaFileFilter implements IFileFilter
{

    @Override
    public boolean isRight(File file)
    {
        return file.getName().endsWith(".java");
    }

}
