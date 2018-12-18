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
    private File file;

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
        if (contentType != null) this.contentType = contentType;
    }

    public byte[] getData() {
        return data;
    }

    public File getFile() {
        return file;
    }

    public String getFilename() {
        return filename;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getContentType() {
        return contentType;
    }
}
