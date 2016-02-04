package com.file.hw.filter.infac;

import java.io.File;

public interface IFileFilter
{
    boolean isRight(File file);//不合要求 ，则返回false ,合要求则返回 true.
}
