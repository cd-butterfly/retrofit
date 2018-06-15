package com.bty.retrofit.net.bean;

import java.io.Serializable;

/**
 * Created by duo.chen on 2017/4/20.
 */

public class FileUploadRequest implements Serializable {

    private FormFile[] files;

    public FormFile[] getFiles() {
        return files;
    }

    public void setFiles(FormFile[] files) {
        this.files = files;
    }
}
