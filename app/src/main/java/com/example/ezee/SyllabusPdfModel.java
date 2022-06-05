package com.example.ezee;

public class SyllabusPdfModel {

    String filename, fileurl, dept, sem;

    public SyllabusPdfModel() {
    }

    public SyllabusPdfModel(String filename, String fileurl, String dept, String sem) {
        this.filename = filename;
        this.fileurl = fileurl;
        this.dept = dept;
        this.sem = sem;
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

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }
}
