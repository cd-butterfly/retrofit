package com.bty.retrofit.net.bean;

import com.bty.retrofit.net.body.ProgressResponseBody;

import java.io.Serializable;

/**
 * Created by duo.chen on 2017/4/21.
 */

public class FileDownloadRequest implements Serializable {

    private String savPath;
    private String fileName;

    ProgressResponseBody.ProgressResponseListener progressResponseListener;

    public String getSavPath() {
        return savPath;
    }

    public void setSavPath(String savPath) {
        this.savPath = savPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ProgressResponseBody.ProgressResponseListener getProgressResponseListener() {
        return progressResponseListener;
    }

    public void setProgressResponseListener(ProgressResponseBody.ProgressResponseListener progressResponseListener) {
        this.progressResponseListener = progressResponseListener;
    }
}
