package com.common.util;

import java.io.InputStream;
import java.io.StringReader;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialException;

import com.io.hw.file.util.FileUtils;


public final class TypeUtil
{
    private TypeUtil()
    {
        throw new Error("Don't let anyone instantiate this class.");
    }
    
    public static int bool2int(boolean bool){
        if (bool){
            return 1;
        }else{
            return 0;
        }
    }
    public static String getClobStr(java.sql.Clob clob)
    {
        StringReader reader = null;
        try
        {
            reader = (StringReader) clob.getCharacterStream();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        if(null==reader){
			return null;
		}
        return FileUtils.getFullContent(reader);
    }
    public InputStream getInputStream(java.sql.Blob blob){
    	InputStream in = null;
    	 try
         {
             in = blob.getBinaryStream();
         }
         catch (SQLException e)
         {
             e.printStackTrace();
         }
    	 return in;
    }

    public static java.sql.Clob getClob4Str(String input)
    {
        try
        {
            return new javax.sql.rowset.serial.SerialClob(input.toCharArray());
        }
        catch (SerialException e)
        {
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
