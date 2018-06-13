package com.example.jorch.svmovil.ModulosYHelpers;

/**
 * Created by infraestructurasorteostec on 09/05/18.
 */


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

public class CustomizedExceptionHandler implements UncaughtExceptionHandler {

    private UncaughtExceptionHandler defaultUEH;
    private String localPath;
    public CustomizedExceptionHandler(String localPath) {
        this.localPath = localPath;
        //Getting the the default exception handler
        //that's executed when uncaught exception terminates a thread
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }

    public void uncaughtException(Thread t, Throwable e) {

        //Write a printable representation of this Throwable
        //The StringWriter gives the lock used to synchronize access to this writer.
        final Writer stringBuffSync = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringBuffSync);
        e.printStackTrace(printWriter);
        String stacktrace = stringBuffSync.toString();
        printWriter.close();

        if (localPath != null) {
            writeToFile(stacktrace);
        }

         defaultUEH.uncaughtException(t, e);
    }

    private void writeToFile(String currentStacktrace)
    {
        String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath() + "/Crash_Reports/";
        File Directory = new File(dir);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date date = new Date();
        String filename = "Crash_rep" + dateFormat.format(date) + ".txt";
        File file = new File(dir, filename);

        if (!Directory.exists())
        {
            file.getParentFile().mkdirs();
        }
        try
        {
            file.createNewFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        try (FileWriter fileWriter = new FileWriter(file))
        {
            fileWriter.append(currentStacktrace);
            fileWriter.flush();
            fileWriter.close();
        }
        catch (IOException e)
        {
                Log.e("ExceptionHandler", e.getMessage());
        }
    }
}