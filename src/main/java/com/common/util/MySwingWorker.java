package com.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.swing.SwingWorker;

public final class MySwingWorker extends SwingWorker<Boolean, Character>
{
    private BufferedReader br_right             = null;
    private BufferedReader br_error             = null;
    private MyProcess      myprocess      = null;
    private boolean        isPrintVerbose = true;
    private StringBuffer stringbuf=new StringBuffer();

    public MySwingWorker(MyProcess myprocess, BufferedReader br)
    {
        this.br_right = br;
        this.myprocess = myprocess;
        
    }
    public MySwingWorker(MyProcess myprocess)
    {
        this.myprocess = myprocess;
        br_right = new BufferedReader(new InputStreamReader(
            myprocess.getInputStream()), 4096);
        br_error = new BufferedReader(new InputStreamReader(
            myprocess.getErrorStream()), 4096);
    }

    @Override
    protected Boolean doInBackground() throws Exception
    {
        char word = ' ';
        int tmp = 0;
        while ((tmp = br_right.read()) != -1)
        {
            word = (char) tmp;
            publish(word);
        }
        while ((tmp = br_error.read()) != -1)
        {
            word = (char) tmp;
            publish(word);
        }
        if (isPrintVerbose)
        {
            System.out.println("doInBackground() over");
        }
        return true;
    }

    @Override
    protected void process(List<Character> chunks)
    {
        for (char temp : chunks)
        {
            {
                System.out.print(temp);
                this.stringbuf.append(temp);
            }
        }
    }

    
    
    public StringBuffer getStringbuf()
    {
        return stringbuf;
    }
    /***
     * main thread can't execute next command(below waitFor()) 
     * until done() is executed
     */
    @Override
    protected void done()
    {
        if (isPrintVerbose)
        {
            System.out.println("done() is finish");
        }
        try
        {
            br_right.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        this.myprocess.stopLoop();
    }

}
