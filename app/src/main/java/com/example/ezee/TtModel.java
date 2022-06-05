package com.example.ezee;

public class TtModel {
    String filename;
    String fileurl;
    String year,dept,div;

    public TtModel(String filename, String fileurl, String year, String dept, String div) {
        this.filename = filename;
        this.fileurl = fileurl;
        this.year = year;
        this.dept = dept;
        this.div = div;
    }

    public TtModel() {
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getDiv() {
        return div;
    }

    public void setDiv(String div) {
        this.div = div;
    }
}
