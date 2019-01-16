package com.bty.retrofit.demo.log.FloatingWindow;

import java.util.ArrayList;
import java.util.List;

public class LogManager {

    public List<OkHttpRequestLog> data = new ArrayList<>();

    private LogManager() {
    }

    public static LogManager INSTANCE = new LogManager();

    private LogAddListener logAddListener;

    public void setLogAddListener(LogAddListener logAddListener) {
        this.logAddListener = logAddListener;
    }

    public void log(OkHttpRequestLog log){
        data.add(0,log);
        if (logAddListener!= null){
            logAddListener.onAddLog(log);
        }
    }

    public interface LogAddListener{
        void onAddLog(OkHttpRequestLog log);
    }
}
