package com.common.util;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public final class WindowUtil
{
    private WindowUtil()
    {
        throw new Error("Don't let anyone instantiate this class.");
    }
    public static String getSysClipboardText()
    {
        String ret = "";
        Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable clipTf=null;
        try
        {
            clipTf = sysClip.getContents(null);
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }

        if (clipTf != null)
        {
            if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor))
            {
                try
                {
                    ret = (String) clipTf
                            .getTransferData(DataFlavor.stringFlavor);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        return ret;
    }

    /**
     * ���ַ��Ƶ����а塣
     */
    public static void setSysClipboardText(String writeMe)
    {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(writeMe);
        clip.setContents(tText, null);
    }
    /**
     * minus Set
     * @param oldList
     * @param list
     * @return
     */
    public static List getMinusSet(List oldList, List list)
    {
        return getMinusSetStr(oldList, list);
    }

    public static List<File> getMinusSetFile(List oldList, List list)
    {
        List selectedList = null;
        if (oldList != null)
        {
            selectedList = new ArrayList<File>();
            int leng = oldList.size();
            if (leng != 0)
            {
                for (int i = 0; i < leng; i++)
                {
                    Object obj = oldList.get(i);
                    if (!contains(list, obj))
                    {
                        selectedList.add(obj);
                    }
                }
            }
        }
        return selectedList;
    }

    public static List getMinusSetStr(List oldList, List list)
    {
        List selectedList = null;
        if (oldList != null)
        {
            selectedList = new ArrayList<Object>();
            int leng = oldList.size();
            if (leng != 0)
            {
                for (int i = 0; i < leng; i++)
                {
                    Object obj = oldList.get(i);
                    if (!contains(list, obj))
                    {
                        selectedList.add(obj);
                    }
                }
            }
        }
        return selectedList;
    }
    public static boolean contains(List<Object> list, Object value)
    {
        if (list == null || list.size() == 0)
        {
            return false;
        }
        else
        {
            for (int i = 0; i < list.size(); i++)
            {
                String valueStr;
                if (value instanceof File)
                {
                    valueStr = ((File) value).getName();
                }
                else
                {
                    valueStr = value.toString();
                }
                Object obj = list.get(i);
                if (obj instanceof File)
                {
                    if (list.contains(valueStr)
                        || ((File) obj).getName().toString().equals(valueStr))
                    {
                        return true;
                    }
                }
                else
                {
                    if (list.contains(valueStr)
                        || list.get(i).toString().equals(valueStr))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public static String getCmdOutput(String[] command) throws IOException
    {
        Process process = null;
        process = ProcessUtil.getPro(command);
        try
        {
            process.waitFor();
        }
        catch (InterruptedException e)
        {
        }
        BufferedReader reader = null;
        InputStreamReader ireader = new InputStreamReader(process
                .getInputStream());
        reader = new BufferedReader(ireader);
        String readedLine = null;
        StringBuffer strb = new StringBuffer();
        while ((readedLine = reader.readLine()) != null)
        {
            strb.append(readedLine);
        }
        reader.close();
        process.destroy();
        return strb.toString();
    }
    
}
