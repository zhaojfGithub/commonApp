package com.zhao.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * @author zjf
 * @date 2023/7/11
 * @effect
 */
public class DateUtil {

    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final String DATE_FORMAT_MM_DD = "MM-dd";
    public static final String DATETIME_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATETIME_FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String DATETIME_FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";


    public static final String DATETIME_FORMAT_MM_DD_HH_MM = "MM-dd HH:mm";
    public static final String DATETIME_FORMAT_HH_MM = "HH:mm";

    public static final String DATE_DAY = "DAY";
    public static final String DATE_HOURS = "HOURS";
    public static final String DATE_MINUTES = "MINUTES";
    public static final String DATE_SECONDS = "SECONDS";

    /**
     * @return 当前时间
     */
    public static Date getNowDate() {
        return new Date();
    }

    public static String getNowStrDate(String format) {
        return getDateFormat(getNowDate(), format);
    }

    /**
     * @param date   需要转换格式的date
     * @param format 目标格式
     * @return 结果
     */
    public static String getDateFormat(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.CHINA);
        return formatter.format(date);
    }

    /**
     * @param date   需要转换格式的date
     * @param format 目标格式
     * @return 结果
     */
    public static Date getDateFormat(String date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.CHINA);
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param date       需要转换格式的date
     * @param formFormat 当前格式
     * @param toFormat   目标格式
     * @return 结果
     */
    public static String getDateFormat(String date, String formFormat, String toFormat) {
        SimpleDateFormat formMatter = new SimpleDateFormat(formFormat, Locale.CHINA);
        SimpleDateFormat toMatter = new SimpleDateFormat(toFormat, Locale.CHINA);
        Date date1;
        try {
            date1 = formMatter.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        assert date1 != null;
        return toMatter.format(date1);
    }

    /**
     * 获取未来[days]天
     *
     * @param days   需要加减的天数
     * @param format 目标格式
     * @return 结果
     */
    public static String getFutureDayDate(int days, String format) {
        return getDateFormat(getFutureDayDate(days), format);
    }

    /**
     * 获取未来的[day]天
     *
     * @param days 需要加减的天数
     * @return 结果
     */
    public static Date getFutureDayDate(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getNowDate());
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    /**
     * 获取未来的[day]天
     *
     * @param days 需要加减的天数
     * @return 结果 精确到Day
     */
    public static String getFuturePreciseDayDate(int days, String format) {
        String str = getFutureDayDate(days, DATE_FORMAT_YYYY_MM_DD);
        return getDateFormat(str, DATE_FORMAT_YYYY_MM_DD, format);
    }

    /**
     * 获取两个时间的差值
     *
     * @param startDate 起始时间
     * @param endDate   结束时间
     * @param type      单位格式
     * @return 插值
     */
    public static long getDateDifference(Date startDate, Date endDate, String type) {
        long time = 0;
        switch (type) {
            case DATE_DAY:
                time = getDateDayDifference(startDate, endDate);
                break;
            case DATE_HOURS:
                time = getDateHoursDifference(startDate, endDate);
                break;
            case DATE_MINUTES:
                time = getDateMinutesDifference(startDate, endDate);
                break;
            default:
                time = getDateSecondsDifference(startDate, endDate);
        }
        return time;
    }

    /**
     * 获取两个时间的差值，以天为单位
     *
     * @param startDate 起始时间
     * @param endDate   结束时间
     */
    public static long getDateDayDifference(Date startDate, Date endDate) {
        long durationInMillis = endDate.getTime() - startDate.getTime();
        return TimeUnit.MILLISECONDS.toDays(durationInMillis);
    }

    /**
     * 获取两个时间的差值，以小时为单位
     *
     * @param startDate 起始时间
     * @param endDate   结束时间
     */
    public static long getDateHoursDifference(Date startDate, Date endDate) {
        long durationInMillis = endDate.getTime() - startDate.getTime();
        return TimeUnit.MILLISECONDS.toHours(durationInMillis);
    }

    /**
     * 获取两个时间的差值，以分钟为单位
     *
     * @param startDate 起始时间
     * @param endDate   结束时间
     */
    public static long getDateMinutesDifference(Date startDate, Date endDate) {
        long durationInMillis = endDate.getTime() - startDate.getTime();
        return TimeUnit.MILLISECONDS.toMinutes(durationInMillis);
    }

    /**
     * 获取两个时间的差值，以秒为单位
     *
     * @param startDate 起始时间
     * @param endDate   结束时间
     */
    public static long getDateSecondsDifference(Date startDate, Date endDate) {
        long durationInMillis = endDate.getTime() - startDate.getTime();
        return TimeUnit.MILLISECONDS.toSeconds(durationInMillis);
    }
}
