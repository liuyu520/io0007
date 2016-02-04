package com.io.hw.exception;

public class NoFilterException extends Exception
{
    private static final long serialVersionUID = 8841391708858959048L;

    public NoFilterException()
    {
        super("FullContentReplace has no content filter!");
    }

    public NoFilterException(String arg0)
    {
        super(arg0);
    }
    
}
