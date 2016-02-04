package com.io.hw.awt;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;

import com.io.hw.awt.custom.SimpleSwing;

public class FindApp
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        
        final BottomPanel prograssThread=new BottomPanel();
        final FindSwing findFrame=new FindSwing(prograssThread);
        
        new Thread(new SimpleSwing()
        {
            @Override
            public void customLayout(Container c)
            {
                c.add(findFrame,BorderLayout.CENTER);
                c.add(prograssThread,BorderLayout.SOUTH);
            }
        }).start();
        new Thread(findFrame).start();
        new Thread(prograssThread).start();
    }
    

}
