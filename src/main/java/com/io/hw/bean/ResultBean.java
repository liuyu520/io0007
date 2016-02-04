package com.io.hw.bean;

import java.io.File;
import java.util.ArrayList;

public class ResultBean
{
    private File file;
    private String fullContent;
    private boolean isChanged ;
    private int changedRow;//the row number found the first time ��һ�η��ֵ��к�
    private ArrayList<Integer> rows=new ArrayList<Integer>();
    
    public void addRow(int rownum){
    	this.getRows().add(rownum);
    }
    public ArrayList<Integer> getRows() {
		return rows;
	}
	public void setRows(ArrayList<Integer> rows) {
		this.rows = rows;
	}
	public int getChangedRow()
    {
        return changedRow;
    }
    public void setChangedRow(int changedRow)
    {
        this.changedRow = changedRow;
    }
    public File getFile()
    {
        return file;
    }
    public void setFile(File file)
    {
        this.file = file;
    }
    public String getFullContent()
    {
        return fullContent;
    }
    public void setFullContent(String fullContent)
    {
        this.fullContent = fullContent;
    }
    public boolean isChanged()
    {
        return isChanged;
    }
    public void setChanged(boolean isChanged)
    {
        this.isChanged = isChanged;
    }
    

}
