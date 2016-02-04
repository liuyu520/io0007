package com.file.hw.props;

import com.string.widget.util.ValueWidget;

import java.io.*;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropUtil
{
    private ResourceBundle rb;
    private InputStream    in;

    //    public PropUtil(Properties props)
    //    {
    //        this.props = props;
    //    }

    public PropUtil(String filename)
    {
        this(filename, false);
    }

    public PropUtil(String filename, boolean isWebProject)
    {

        try
        {
            //            System.out.println(this.getClass().getClassLoader()
            //                    .getResource("./"));
            if (isWebProject)
            {
                in = new BufferedInputStream(this.getClass().getClassLoader()
                        .getResourceAsStream(filename));
            }
            else
            {
            	FileInputStream fin=new FileInputStream(filename);
            	if(null!=fin){
            		in = new BufferedInputStream(fin);
            	}
            }
            //String rootPath=Thread.currentThread().getContextClassLoader().getResource("" ).getPath();
            //            in = new BufferedInputStream(new FileInputStream(rootPath+this.fileName));
            if(null!=in){
            	this.rb = new PropertyResourceBundle(in);
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /***
     * any use ?
     *
     * @param input
     * @param key
     * @return
     */
    public static String getValue(String input, String key) {
        String regex = "#[ \\s]*" + key + "[ \\s]*=[ \\s]*(.+)[ \\s]*$";
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return (matcher.group(1));
        }
        return null;
    }

    /***
     * 保存properties文件
     *
     * @param props
     * @param out
     * @param comments
     * @throws IOException
     */
    public static void save(Properties props, OutputStream out, String comments) throws IOException {
        props.store(out, comments);
    }

    public boolean isHasKey(String key) {
        return !ValueWidget.isNullOrEmpty(rb) && rb.containsKey(key);
    }

    /**
     * @param key
     * @return
     */
    public String getStr(String key)
    {
        if (!isHasKey(key))
        {
            return null;
        }
        if(ValueWidget.isNullOrEmpty(rb)){
        	return null;
        }
        return rb.getString(key);
    }

    /***
     *
     * @param key
     * @return int
     */
    public int getInt(String key)
    {
        String valueStr = getStr(key);
        if (valueStr == null || !Pattern.matches("[\\d]+", valueStr))
        {
            return 0;
        }
        return Integer.parseInt(valueStr);
    }

    /***
     *
     * @param key
     * @return boolean
     */
    public boolean getBoolean(String key)
    {
        String valueStr = getStr(key);
        if (ValueWidget.isNullOrEmpty(valueStr))
        {
            return false;
        }
        return valueStr.equalsIgnoreCase("true") || valueStr.equalsIgnoreCase("t");
    }

    public void close()
    {
        if (this.in != null)
        {
            try {
                this.in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
