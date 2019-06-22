package com.dolphintechno.misscallalert.model;

public class MissCallInfo {

    int id;
    String name;
    String number;
    String date_time;
    String msg;

    public MissCallInfo() {

    }

    public MissCallInfo(int id, String name, String number, String date_time, String msg) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.date_time = date_time;
        this.msg = msg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
