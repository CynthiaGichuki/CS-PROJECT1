package com.example.qrcode;

public class Screening {
    private  String gender;
    private String drycough;
    private String Fever;
    private String Fatigue;
    private String Headache;
    private String Sorethroat;
    private String Taste;
    private String Aches;
    private String Contact;
    private String Date;
    private String Time;


    public Screening() {
    }


    public String getDrycough() {
        return drycough;
    }

    public void setDrycough(String drycough) {
        this.drycough = drycough;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFever() {
        return Fever;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setFever(String fever) {
        Fever = fever;
    }

    public String getFatigue() {
        return Fatigue;
    }

    public void setFatigue(String fatigue) {
        Fatigue = fatigue;
    }

    public String getHeadache() {
        return Headache;
    }

    public void setHeadache(String headache) {
        Headache = headache;
    }

    public String getSorethroat() {
        return Sorethroat;
    }

    public void setSorethroat(String sorethroat) {
        Sorethroat = sorethroat;
    }

    public String getTaste() {
        return Taste;
    }

    public void setTaste(String taste) {
        Taste = taste;
    }

    public String getAches() {
        return Aches;
    }

    public void setAches(String aches) {
        Aches = aches;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }
}
