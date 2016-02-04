package com.io.hw.chain;

import java.util.ArrayList;
import java.util.List;

import com.io.hw.impl.ContentFilterImpl;
import com.io.hw.infac.IContentFilter;

public class ContentFilterChain implements IContentFilter
{
    List<IContentFilter> filters   = new ArrayList<IContentFilter>();
    private boolean      isChanged = false;
    
    public int getSize(){
        return this.filters.size();
    }
    public boolean isChanged()
    {
        return isChanged;
    }

    public void setChanged(boolean isChanged)
    {
        this.isChanged = isChanged;
    }


    @Override
    public String replaceFilter(String content)
    {
        IContentFilter replaceFilter = null;
        for (int i = 0; i < this.filters.size(); i++)
        {
            replaceFilter = (IContentFilter) filters.get(i);
            content = replaceFilter.replaceFilter(content);
        }
        if (!this.isChanged && ContentFilterImpl.isChanged)
        {
            isChanged = ContentFilterImpl.isChanged;
        }
        return content;
    }

    public ContentFilterChain add(IContentFilter contentFilter)
    {
        this.filters.add(contentFilter);
        return this;
    }
    @Override
    public boolean isContains(String content)
    {
        boolean isContains=false;
        IContentFilter replaceFilter = null;
        for (int i = 0; i < this.filters.size(); i++)
        {
            replaceFilter = (IContentFilter) filters.get(i);
            isContains = replaceFilter.isContains(content);
            if(isContains){
                break;
            }
        }
        return isContains;
    }
    public IContentFilter getFirstFilter(){
    	if(null==this.filters||this.filters.size()==0){
    		return null;
    	}
    	return this.filters.get(0);
    }
	@Override
	public String getOldRegex() {
		return getFirstFilter().getOldRegex();
	}
}
