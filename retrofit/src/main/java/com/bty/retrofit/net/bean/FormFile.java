package com.bty.retrofit.net.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 */
public class FormFile implements Serializable {
    private byte[] data;
    private InputStream inStream;
    private File file;

    private int fileSize;
    private String filname;
    private String parameterName;
    //private String contentType = "application/octet-stream";
    private String contentType = "Content-Type: image/jpeg";

    public FormFile(String filname, byte[] data, String parameterName, String contentType) {
        this.data = data;
        this.filname = filname;
        this.parameterName = parameterName;
        if (contentType != null) this.contentType = contentType;
    }

    public FormFile(String filname, File file, String parameterName, String contentType) {
        this.filname = filname;
        this.parameterName = parameterName;
        this.file = file;
        try {
            this.inStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (contentType != null) this.contentType = contentType;
    }


    public FormFile(InputStream inStream, int fileSize, String filname,
                    String parameterName, String contentType) {
        super();
        this.inStream = inStream;
        this.fileSize = fileSize;
        this.filname = filname;
        this.parameterName = parameterName;
        this.contentType = contentType;
    }


    public int getFileSize() {
        return fileSize;
    }

    public File getFile() {
        return file;
    }

    public InputStream getInStream() {
        if (inStream == null && file != null) {
            try {
                this.inStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return inStream;
    }

    public void releaseInputStream() {
        if (inStream != null) {
            try {
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            inStream = null;
        }
    }

    public byte[] getData() {
        return data;
    }

    public String getFilname() {
        return filname;
    }

    public void setFilname(String filname) {
        this.filname = filname;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

}
