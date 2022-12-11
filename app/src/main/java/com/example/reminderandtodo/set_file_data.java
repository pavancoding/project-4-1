package com.example.reminderandtodo;

public class set_file_data {
    String filename,type,size,extension,path;
    int color;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getFilename() {
        return filename;
    }

    public String getType() {
        return type;
    }

    public String getSize() {
        return size;
    }

    public String getExtension() {
        return extension;
    }

    public int getColor() {
        return color;
    }

    public set_file_data(String filename, String type, String size, String extension, int color,String path) {
        this.filename = filename;
        this.type = type;
        this.size = size;
        this.extension = extension;
        this.color = color;
        this.path=path;
    }
}
