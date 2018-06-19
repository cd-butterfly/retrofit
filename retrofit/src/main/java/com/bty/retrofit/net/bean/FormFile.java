package com.bty.retrofit.net.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 *
 */
public class FormFile implements Serializable {
    private byte[] data;
    private InputStream inStream;
    private File file;

    private int fileSize;
    private String filename;
    private String parameterName;
    /**
     *
     * Http ContentType @link {http://tool.oschina.net/commons}
     */
    private String contentType = "application/octet-stream";

    public FormFile(String filename, byte[] data, String parameterName, String contentType) {
        this.data = data;
        this.filename = filename;
        this.parameterName = parameterName;
        if (contentType != null) this.contentType = contentType;
    }

    public FormFile(String filename, File file, String parameterName, String contentType) {
        this.filename = filename;
        this.parameterName = parameterName;
        this.file = file;
        try {
            this.inStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (contentType != null) this.contentType = contentType;
    }


    public FormFile(InputStream inStream, int fileSize, String filename,
                    String parameterName, String contentType) {
        super();
        this.inStream = inStream;
        this.fileSize = fileSize;
        this.filename = filename;
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
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
