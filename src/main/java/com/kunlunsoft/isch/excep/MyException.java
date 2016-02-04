package com.kunlunsoft.isch.excep;

public class MyException extends Exception
{
    private static final long serialVersionUID = 8521287067557230478L;

    public MyException()
    {
        super();
    }

    public MyException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public MyException(String message)
    {
        super(message);
    }

    public MyException(Throwable cause)
    {
        super(cause);
    }
}
