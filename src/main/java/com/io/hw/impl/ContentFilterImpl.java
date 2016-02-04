package com.io.hw.impl;

import com.io.hw.exception.NoFilterException;
import com.io.hw.infac.IContentFilter;
import com.string.widget.util.ValueWidget;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentFilterImpl implements IContentFilter
{
    public static boolean isChanged = false;
    private String        oldRegex;
    private String        replacement;

    public ContentFilterImpl()
    {
    }

    public ContentFilterImpl(String oldRegex, String replacement) throws NoFilterException
    {
    	if(ValueWidget.isMargin(oldRegex)){
    		throw new NoFilterException();
    	}
        this.oldRegex = oldRegex;
        this.replacement = replacement;
    }

    @Override
    public String replaceFilter(String content)
    {
        Pattern p = Pattern.compile(oldRegex);
        Matcher m = p.matcher(content);
        boolean bl=m.find();
        if (!isChanged && bl)
        {
            isChanged = bl;
        }
        return oldRegex.equals(replacement)? content:content.replaceAll(oldRegex, replacement);
    }

    @Override
    public boolean isContains(String content)
    {
        Pattern p=Pattern.compile(this.oldRegex);
        Matcher m=p.matcher(content);
        return m.find();
    }
    @Override
    public String getOldRegex(){
    	return oldRegex;
    }

}
