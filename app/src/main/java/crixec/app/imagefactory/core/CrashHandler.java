package crixec.app.imagefactory.core;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.widget.Toast;

public class CrashHandler implements UncaughtExceptionHandler {

    private static Context mContext;

    public CrashHandler(Context context) {
        // TODO Auto-generated constructor stub
        mContext = context;
    }

    public void init() {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // TODO Auto-generated method stub
        Toast.makeText(mContext, "程序出现异常", Toast.LENGTH_SHORT).show();
        ;
        StringBuilder mStringBuilder = new StringBuilder();
        mStringBuilder.append(ExceptionHandler.getSystemInfo());
        mStringBuilder.append(ex.getMessage() + "\n");
        StringWriter mStringWriter = new StringWriter();
        PrintWriter pw = new PrintWriter(mStringWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.close();
        mStringBuilder.append(mStringWriter.toString());
        ExceptionHandler.handle(mStringBuilder.toString());
        System.exit(1);
    }

}
