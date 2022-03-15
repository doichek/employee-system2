package models;

public class MyCalendar {


    //カレンダーの年
    private int year;
    //カレンダーの月
    private int month;

    //カレンダーの日付を保持する配列
    private String[] date;



    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public String[] getDate() {
        return date;
    }
    public void setDate(String[] date) {
        this.date = date;
    }
}