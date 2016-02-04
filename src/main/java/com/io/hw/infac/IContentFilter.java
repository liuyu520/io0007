package com.io.hw.infac;

public interface IContentFilter
{
    
    String replaceFilter(String content);
    boolean isContains(String content);
    String getOldRegex();
}
