package in.co.softwaresolution.list;

import java.util.Calendar;

public class Info {

    Calendar calendar=Calendar.getInstance();
    private long id;
    private String title;
    private String description;
    private int day=calendar.get(Calendar.DAY_OF_MONTH);
    private int month=calendar.get(Calendar.MONTH);
    private int year=calendar.get(Calendar.YEAR);
    private int hour=calendar.get(Calendar.HOUR_OF_DAY);
    private int min=calendar.get(Calendar.MINUTE);
    private int second=calendar.get(Calendar.SECOND);

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Info()
    {

    }
    public Info(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Info(String title, String description, int day, int month, int year, int min, int hour,int second) {
        this.title = title;
        this.description = description;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.min = min;
        this.second=second;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
