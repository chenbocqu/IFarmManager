/**
 * 时间工具
 */
package com.qican.ifarmmanager.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

    public static void WaitMs(long ms) {
        long time = System.currentTimeMillis();
        while (System.currentTimeMillis() - time < ms)
            ;
    }

    public static String getTimeFromNum(int num) {
        int hour = num / 3600;
        int min = (num % 3600) / 60;
        int sec = num % 60;

        String ret =
                (hour != 0 ? hour + "小时" : "") +
                        (min != 0 ? min + "分钟" : "") +
                        (sec != 0 ? sec + "秒" : "");
        if (hour == 0 && min == 0 && sec == 0) ret = "0秒";

        return ret;
    }


    // 2017-01-06 15:01:44.0
    public static String getDate(String timeStamp) {
        String[] time = timeStamp.split(" ");
        if (time.length != 2) {
            throw new IllegalArgumentException("timeStamp 格式有问题，请检查！");
        }
        return time[0];
    }

    public static String parseTime(String time) {

        if (time == null) return "";

        String ret = time;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {

        }

        if (date != null) {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            ret = format.format(date);
        }
        return ret;
    }

    /**
     * 格式化时间
     *
     * @param time
     * @return
     */
    public static String formatDateTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (time == null || "".equals(time)) {
            return "";
        }

        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {

        }
        if (date == null) return "";

        Calendar current = Calendar.getInstance();
        current.setTime(date);

        // 计算显示值

        String min = "";
        if (current.get(Calendar.MINUTE) < 10) min = "0" + current.get(Calendar.MINUTE);
        else min = current.get(Calendar.MINUTE) + "";

        String hourAndMin = current.get(Calendar.HOUR_OF_DAY) + ":" + min;

        if (current.before(getDateByOffset(-2)) && current.after(getDateByOffset(-3))) {
            return "上前天 " + hourAndMin;
        } else if (current.before(getDateByOffset(-1)) && current.after(getDateByOffset(-2))) {
            return "前天 " + hourAndMin;
        } else if (current.before(getDateByOffset(0)) && current.after(getDateByOffset(-1))) {
            return "昨天 " + hourAndMin;
        } else if (current.before(getDateByOffset(1)) && current.after(getDateByOffset(0))) {
            return "今天 " + hourAndMin;
        } else if (current.before(getDateByOffset(2)) && current.after(getDateByOffset(1))) {
            return "明天 " + hourAndMin;
        } else if (current.before(getDateByOffset(3)) && current.after(getDateByOffset(2))) {
            return "后天 " + hourAndMin;
        } else if (current.before(getDateByOffset(4)) && current.after(getDateByOffset(3))) {
            return "大后天 " + hourAndMin;
        } else {
            return (current.get(Calendar.MONTH) + 1) + "-" + current.get(Calendar.DAY_OF_MONTH) + " " + hourAndMin;
        }
    }

    private static Calendar getDateByOffset(int offset) {

        Calendar current = Calendar.getInstance();
        Calendar d = Calendar.getInstance();    // 后天 offset = 2

        d.set(Calendar.YEAR, current.get(Calendar.YEAR));
        d.set(Calendar.MONTH, current.get(Calendar.MONTH));
        d.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) + offset);
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        d.set(Calendar.HOUR_OF_DAY, 0);
        d.set(Calendar.MINUTE, 0);
        d.set(Calendar.SECOND, 0);

        return d;
    }


    /**
     * 格式化时间
     *
     * @param time
     * @return
     */
    public static String formatTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (time == null || "".equals(time)) {
            return "";
        }

        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {

        }
        if (date == null) return "";

        Calendar current = Calendar.getInstance();

        Calendar today = Calendar.getInstance();    //今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar yesterday = Calendar.getInstance();    //昨天

        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);

        Calendar tomorrow = Calendar.getInstance();    // 明天

        tomorrow.set(Calendar.YEAR, current.get(Calendar.YEAR));
        tomorrow.set(Calendar.MONTH, current.get(Calendar.MONTH));
        tomorrow.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) + 1);
        tomorrow.set(Calendar.HOUR_OF_DAY, 0);
        tomorrow.set(Calendar.MINUTE, 0);
        tomorrow.set(Calendar.SECOND, 0);

        Calendar houtian = Calendar.getInstance();    // 后天

        houtian.set(Calendar.YEAR, current.get(Calendar.YEAR));
        houtian.set(Calendar.MONTH, current.get(Calendar.MONTH));
        houtian.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) + 2);
        houtian.set(Calendar.HOUR_OF_DAY, 0);
        houtian.set(Calendar.MINUTE, 0);
        houtian.set(Calendar.SECOND, 0);

        current.setTime(date);

        // 计算显示值

        String min = "";
        if (current.get(Calendar.MINUTE) < 10) min = "0" + current.get(Calendar.MINUTE);
        else min = current.get(Calendar.MINUTE) + "";

        String hourAndMin = current.get(Calendar.HOUR_OF_DAY) + ":" + min;

        if (current.before(tomorrow) && current.after(today)) {
            return "今天 " + hourAndMin;
        } else if (current.before(today) && current.after(yesterday)) {
            return "昨天 " + hourAndMin;
        } else if (current.before(houtian) && current.after(tomorrow)) {
            return "明天 " + hourAndMin;
        } else {
            return (current.get(Calendar.MONTH) + 1) + "-" + current.get(Calendar.DAY_OF_MONTH) + " " + hourAndMin;
        }
    }

    public static String formatTimeYear(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (time == null || "".equals(time)) {
            return "";
        }

        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {

        }
        if (date == null) return "";

        Calendar current = Calendar.getInstance();

        current.setTime(date);

        // 计算显示值

        String min = "";
        if (current.get(Calendar.MINUTE) < 10) min = "0" + current.get(Calendar.MINUTE);
        else min = current.get(Calendar.MINUTE) + "";

        String hourAndMin = current.get(Calendar.HOUR_OF_DAY) + ":" + min;

        return current.get(Calendar.YEAR) + "年" + (current.get(Calendar.MONTH) + 1) + "月" + current.get(Calendar.DAY_OF_MONTH) + "日"; // + hourAndMin
    }

    // 903 秒 转化成 15 分 3 秒
    public static String formatSecondTime(String time) {

        int second = 0;
        try {
            second = Integer.parseInt(time);
        } catch (Exception e) {
            return "- -";
        }

        return formatSecondTime(second);
    }

    public static String formatSecondTime(int timeInSecond) {

        int min = timeInSecond / 60;
        int second = timeInSecond % 60;

        if (min <= 0) return second + " 秒";
        else if (second == 0) return min + " 分 ";
        else return min + " 分 " + second + " 秒";
    }
}
