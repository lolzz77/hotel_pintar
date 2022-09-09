package com.example.hotelpintar;

import android.widget.TextView;

public class DataHolder {
    private String email;
    private String nickname;
    private TextView textViewName;
    private String time1, time2;

    public String getEmail() {return email;}
    public void setEmail (String s) {this.email = s;}

    public String getNickname() {return nickname;}
    public void setNickname (String s) {this.nickname = s;}

    public TextView getTextViewName() {return textViewName;}
    public void storeTextViewObject (TextView s) {this.textViewName = s;}

    public String getTime1() {return time1;}
    public void setTime1 (String s) {this.time1 = s;}

    public String getTime2() {return time2;}
    public void setTime2 (String s) {this.time2 = s;}

    private static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() {return holder;}
}
