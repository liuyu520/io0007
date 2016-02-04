package com.common.util;

import java.io.IOException;

public final class ProcessUtil
{
    private static Process process = null;
    static
    {

    }
    private ProcessUtil()
    {
        throw new Error("Don't let anyone instantiate this class.");
    }

    public static Process getPro(String command) throws IOException
    {
        process = Runtime.getRuntime().exec(command);
        return process;
    }

    public static Process getPro(String[] command) throws IOException
    {
        process = Runtime.getRuntime().exec(command);
        return process;
    }
}
