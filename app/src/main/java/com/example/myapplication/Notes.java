package com.example.myapplication;


import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Notes implements Serializable ,Comparable<Notes>{

    private String title;
    private String desc;
    private Date updateDate;
    String dateTime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    @SuppressLint("SimpleDateFormat")
    public void setUpdateDate(long updateDate) {
        //updateDate = Long.parseLong(dateTime);
        this.updateDate = new Date(updateDate);
    }
    public Notes(String title1, String desc1) {
        this.title = title1;
        this.desc = desc1;
        this.updateDate = new Date();
    }
    public int compareTo(Notes note){
        if(updateDate.after(note.updateDate)){
            return -1;
        }
        else if(updateDate.before(note.updateDate)){
            return 1;
        }
        return 0;
    }
}
