package com.nadim.csedashboard.dataset;

/**
 * Created by d3stR0y3r on 11/11/2018.
 */
public class ClassModel {
    String ctime,ccode,ctname,cname,ctimestamp;

    public ClassModel() {
    }

    public ClassModel(String ctime, String ccode, String ctname) {
        this.ctime = ctime;
        this.ccode = ccode;
        this.ctname = ctname;
    }

    public ClassModel(String ctime, String ccode, String ctname, String cname, String ctimestamp) {
        this.ctime = ctime;
        this.ccode = ccode;
        this.ctname = ctname;
        this.cname = cname;
        this.ctimestamp = ctimestamp;
    }



    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getCcode() {
        return ccode;
    }

    public void setCcode(String ccode) {
        this.ccode = ccode;
    }

    public String getCtname() {
        return ctname;
    }

    public void setCtname(String ctname) {
        this.ctname = ctname;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCtimestamp() {
        return ctimestamp;
    }

    public void setCtimestamp(String ctimestamp) {
        this.ctimestamp = ctimestamp;
    }
}
