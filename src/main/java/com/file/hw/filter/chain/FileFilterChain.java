package com.file.hw.filter.chain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.file.hw.filter.infac.IFileFilter;

/**
 * ���ڹ����ļ��ĺ�׺��
 * @author Administrator
 *
 */
public class FileFilterChain implements IFileFilter
{
    private List<IFileFilter> fileFilters = new ArrayList<IFileFilter>();

    @SuppressWarnings("unused")
    private List<IFileFilter> getFileFilters()
    {
        return fileFilters;
    }

    @SuppressWarnings("unused")
    private void setFileFilters(List<IFileFilter> fileFilters)
    {
        this.fileFilters = fileFilters;
    }

    public FileFilterChain add(IFileFilter fileFilter)
    {
    	if(null==this.fileFilters){
    		this.fileFilters=new ArrayList<IFileFilter>();
    	}
        this.fileFilters.add(fileFilter);
        return this;
    }
    public void removeFilter(IFileFilter fileFilter){
    	if(this.fileFilters.contains(fileFilter)){
    		this.fileFilters.remove(fileFilter);
    	}
    }
    /**
     * �ļ�������еĹ����������ŷ���true
     */
    @Override
    public boolean isRight(File file)
    {
        boolean isRight = true;
        for (int j = 0; j < fileFilters.size(); j++)
        {
            isRight = fileFilters.get(j).isRight(file);//����Ҫ�� ���򷵻�false ,��Ҫ���򷵻� true.
            if (!isRight)
            {
                break;
            }

        }
        return isRight;
    }
}
