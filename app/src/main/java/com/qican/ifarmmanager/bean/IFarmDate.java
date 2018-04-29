package com.qican.ifarmmanager.bean;

import android.util.Log;

public class IFarmDate {

    boolean isLeapYear;
    int year;
    int month;
    int day;

    int hour;
    int min;
    int sec;
    int daysOfMon[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    String time;

    public IFarmDate(String time) {
        this.time = time;
        parseTime();
    }

    private void parseTime() {
        String temp[] = time.split(" ");
        if (temp.length != 2) {
            Log.i("IFarmDate", "解析时间错误！！");
            return;
        }
        String date1[] = temp[0].split("-");
        String date2[] = temp[1].split(":");

        setYear(Integer.valueOf(date1[0]));
        setMonth(Integer.valueOf(date1[1]));
        setDay(Integer.valueOf(date1[2]));

        setHour(Integer.valueOf(date2[0]));
        setMin(Integer.valueOf(date2[1]));
        setSec(Integer.valueOf(date2[2]));

        int tempYear = (getYear() % 100 == 0) ? getYear() % 100 : getYear();

        if (tempYear % 4 == 0) setLeapYear(true);
        else setLeapYear(false);

        if (isLeapYear()) {
            daysOfMon[1] = 29;//2月闰月
        }
    }

    public boolean isLeapYear(int year) {

        int tempYear = (year % 100 == 0) ? year % 100 : year;

        if (tempYear % 4 == 0)
            return true;
        else
            return false;
    }

    public int getDaysOfMonByMonth(int month) {
        if (month <= 0 || month >= 13) {
            Log.i("IFarmDate", "输入月数有误，month=[ " + month + " ]");
            return 0;
        }
        return daysOfMon[month - 1];
    }

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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
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

    public int getSec() {
        return sec;
    }

    public void setSec(int sec) {
        this.sec = sec;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isLeapYear() {
        return isLeapYear;
    }

    public void setLeapYear(boolean leapYear) {
        isLeapYear = leapYear;
    }

    //相差几天 this - otherTime
    public int differ(IFarmDate otherTime) {
        //不同年的
        boolean isFirst = true;
        int sumDay = 0;

        if (this.isSmallThan(otherTime))
            return -otherTime.differ(this);

        for (int year = otherTime.getYear(); year < this.getYear(); year++) {
            int daysOfLeapMon = 28;
            if (isLeapYear(year)) {
                daysOfLeapMon = 29;
            }
            int month = 1;
            if (isFirst) {
                month = otherTime.getMonth();
                isFirst = false;
            }
            for (; month <= 12; month++) {
                //判断闰月否
                if (month == 2)
                    sumDay = sumDay + daysOfLeapMon;
                else
                    sumDay = sumDay + getDaysOfMonByMonth(month);
            }
        }
        return sumDay;
    }

    private boolean isSmallThan(IFarmDate otherTime) {
        if (getYear() < otherTime.getYear())
            return true;
        if (getYear() == otherTime.getYear()) {
            if (getMonth() < otherTime.getMonth())
                return true;
            else if (getMonth() == otherTime.getMonth() && getDay() < otherTime.getDay())
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "IFarmDate{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", hour=" + hour +
                ", min=" + min +
                ", sec=" + sec +
                ", time='" + time + '\'' +
                '}';
    }
}
