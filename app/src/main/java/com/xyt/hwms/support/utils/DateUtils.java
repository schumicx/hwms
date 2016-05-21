package com.xyt.hwms.support.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Date and time utils.
 *
 * @author peilongwu
 */
public class DateUtils {
    public static final int MILLISECONDS_PER_SECOND = 1000;
    private static String currentTime;
    private static String currentDate;

    /**
     * 转换Date类型的数据为yyyy/MM/dd格式的时间字符串
     */
    public static String convertDateToStr1(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String strdate = "";
        try {
            strdate = formatter.format(date);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return strdate;
    }

    /**
     * 转换Date类型的数据为yyyy-MM-dd格式的时间字符串
     */
    public static String convertDateToStr2(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String strdate = "";
        try {
            strdate = formatter.format(date);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return strdate;
    }

    /**
     * 转换格式为 MM/dd/yyyy 字符串类型的数据为Date类型
     */
    public static Date convertStrToDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        try {
            return formatter.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取时间格式为MM/dd/yyyy的字符串
     */
    public static String getStr(String strDate) {
        String year = strDate.substring(0, 4);
        String month = strDate.substring(5, 7);
        String day = strDate.substring(8, 10);
        strDate = month + "/" + day + "/" + year;
        return strDate;
    }

    /**
     * 获取日期字符串，格式为：yyyyMMddHHmmss
     */
    public static String getStr(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String strdate = "";
        try {
            strdate = formatter.format(date);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return strdate;
    }

    public static Date calcDate(Date date, int year) {
        Calendar rili = Calendar.getInstance();
        rili.setTime(date);
        rili.add(Calendar.YEAR, year);
        date = rili.getTime();
        return date;
    }

    public static Date calcDateDay(Date date, int day) {
        Calendar rili = Calendar.getInstance();
        rili.setTime(date);
        rili.add(Calendar.DAY_OF_YEAR, day);
        date = rili.getTime();
        return date;
    }

    public static Date convertDate(String strdate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        ParsePosition pos = new ParsePosition(1);
        try {
            date = formatter.parse(strdate, pos);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getCurtDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        try {
            date = formatter.parse(formatter.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 格式：yyyy-MM-dd HH:mm:ss
     */
    public static Date convertDateToStandard(String strdate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(strdate);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 格式：yyyy/MM/dd
     */
    public static Date convertDateToStandard2(String strdate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date date = null;
        try {
            date = formatter.parse(strdate);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 格式：MM/dd/yyyy
     */
    public static String getStrDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String strdate = "";
        try {
            strdate = formatter.format(date);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return strdate;
    }

    /**
     * 格式：yyyy-MM-dd
     */
    public static String getCurrentDate1() {
        Date nowDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = formatter.format(nowDate);
        return currentDate;
    }

    /**
     * 格式：MM/dd/yyyy
     */
    public static Date getCurrentDate2() {
        Date nowDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
        try {
            date = formatter.parse(formatter.format(nowDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getCurrentDate3() {
        Date nowDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        currentDate = formatter.format(nowDate);
        return currentDate;
    }

    /**
     * 格式：yyyyMMddHHmmss
     */
    public static String getCt() {
        Date nowDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        currentTime = formatter.format(nowDate);
        return currentTime;
    }

    /**
     * 格式：yyyy-MM-dd HH:mm:ss
     */
    public static Date getCurrentTime() {
        Date nowDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(formatter.format(nowDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 格式：yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentTime1() {
        Date nowDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = null;
        time = formatter.format(nowDate);
        return time;
    }


    /**
     * Returns the Compare Result if equals return "0". if before return "1". if
     * after return "2". if error return "4".
     *
     * @return String
     */
    public static String compareDate(String strDate1, String strDate2) {
        Date date1 = null;
        Date date2 = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date1 = formatter.parse(strDate1);
            date2 = formatter.parse(strDate2);
            if (date1.equals(strDate2)) {
                return "0";
            } else if (date1.before(date2)) {
                return "1";
            } else {
                return "2";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "4";
        }

    }

    public static String getCurrentYear() {
        Date nowDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        return formatter.format(nowDate);
    }

    public static String getCurrentYear(Long date) {
        Date nowDate = new Date(date);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        return formatter.format(nowDate);
    }

    public static String getCurrentMonth() {
        Date nowDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM");
        return formatter.format(nowDate);
    }

    public static String getCurrentMonth(Long date) {
        Date nowDate = new Date(date);
        SimpleDateFormat formatter = new SimpleDateFormat("MM");
        return formatter.format(nowDate);
    }

    public static String getCurrentDay() {
        Date nowDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        return formatter.format(nowDate);
    }

    public static String getCurrentDay(Long date) {
        Date nowDate = new Date(date);
        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        return formatter.format(nowDate);
    }

    /**
     * 格式：yyyy?MM?dd
     */
    public static String getYear(String date) {
        return date.substring(0, 4);
    }

    /**
     * 格式：yyyy?MM?dd
     */
    public static String getMonth(String date) {
        return date.substring(5, 7);
    }

    /**
     * 格式：yyyyMM
     */
    public static String getMonth() {
        Date nowDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        currentDate = formatter.format(nowDate);
        return currentDate;
    }

    /**
     * 格式：yyyy-MM
     */
    public static String getMonth2() {
        Date nowDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        currentDate = formatter.format(nowDate);
        return currentDate;
    }

    /**
     * 获取：yyyy-MM-01 00:00:00 时间戳
     */
    public static Long getMonth3() {
        String month = DateUtils.getMonth2() + "-01 00:00:00";
        Date date = DateUtils.convertDateToStandard(month);
        return date.getTime();
    }

    /**
     * 获取每年第一个月
     */
    public static Long getFirstMonth(String year) {
        String month = year + "-01-01 00:00:00";
        Date date = DateUtils.convertDateToStandard(month);
        return date.getTime();
    }


    /**
     * 获取：yyyy-MM-01
     */
    public static String getMonthFirst() {
        return DateUtils.getMonth2() + "-01";
    }

    /**
     * 获取下一月第一天：yyyy-MM-01
     */
    public static String getMonthLast() {
        Calendar cale = null;
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        return getDate2(cale.getTime().getTime());
    }

    /**
     * 获取距离当前时间第N天：yyyy-MM-01
     */
    public static String getDifferDays(int days) {
        Calendar cale = null;
        cale = Calendar.getInstance();
        cale.setTime(new Date());
        cale.set(Calendar.DATE, cale.get(Calendar.DATE) + days);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(cale.getTime());
    }

    /**
     * 获取距离当前时间第N天：yyyy-MM-01
     */
    public static String getDifferDays(Date date, int days) {
        Calendar cale = null;
        cale = Calendar.getInstance();
        cale.setTime(date);
        cale.set(Calendar.DATE, cale.get(Calendar.DATE) + days);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(cale.getTime());
    }

    /**
     * 获取距离当前时间第几个小时
     */
    public static String getDifferHours(Date date, int hours) {
        Calendar cale = null;
        cale = Calendar.getInstance();
        cale.setTime(date);
        cale.set(Calendar.DATE, cale.get(Calendar.HOUR) + hours);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(cale.getTime());
    }

    /**
     * 获取距离当前时间第几分钟
     */
    public static String getDifferMinutes(Date date, int minutes) {
        Calendar cale = null;
        cale = Calendar.getInstance();
        cale.setTime(date);
        cale.set(Calendar.DATE, cale.get(Calendar.MINUTE) + minutes);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(cale.getTime());
    }

    /**
     * 获取距离当前时间第几个小时
     */
    public static String getDifferSeconds(Date date, int seconds) {
        Calendar cale = null;
        cale = Calendar.getInstance();
        cale.setTime(date);
        cale.set(Calendar.DATE, cale.get(Calendar.SECOND) + seconds);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(cale.getTime());
    }

    /**
     * 获取距离当前时间第N天：yyyy-MM-01
     */
    public static String getDifferMonths(Date date, int month) {
        Calendar cale = null;
        cale = Calendar.getInstance();
        cale.setTime(date);
        cale.set(Calendar.DATE, cale.get(Calendar.MONTH) + month);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(cale.getTime());
    }

    /**
     * 获取距离当前月份的月份
     */
    public static Long getDifferMonthsTime(Long time, int month) {
        Calendar cale = null;
        cale = Calendar.getInstance();
        cale.setTime(new Date(time));
        cale.set(Calendar.MONTH, cale.get(Calendar.MONTH) + month);
        return cale.getTime().getTime();
    }

    /**
     * 格式：yyyy?MM?dd
     */
    public static String getDay(String date) {
        return date.substring(8, 10);
    }

    /**
     * 格式：yyyy?MM?dd
     */
    public static String getOnlyDate(String time) {
        return time.substring(0, 10);
    }

    public static Timestamp getcurTime() {
        Date date = new Date();
        return (new Timestamp(date.getTime()));
    }

    public static Date strToDate(String mask, String strDate) {
        SimpleDateFormat format = new SimpleDateFormat(mask);
        Date date = null;
        try {
            date = format.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 字符串转换为日期型：
     * 1、若只为年份，如：2014，自动补为：2014-01-01
     * 2、若为年月，如：2014-01，自动补为：2014-01-01
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        strDate = strDate.trim();
        if (strDate.length() == 4) {
            strDate = strDate + "-01-01";
            return strToDate("yyyy-MM-dd", strDate);
        } else if (strDate.length() == 7) {
            strDate = strDate + "-01";
            return strToDate("yyyy-MM-dd", strDate);
        } else if (strDate.length() == 10) {
            return strToDate("yyyy-MM-dd", strDate);
        } else {
            throw new RuntimeException("传入的日期格式有误");
        }
    }

    public static String strToDateString(String strDate) {
        strDate = strDate.trim();
        if (strDate.length() == 4) {
            strDate = strDate + "-01-01";
            return strDate;
        } else if (strDate.length() == 7) {
            strDate = strDate + "-01";
            return strDate;
        } else if (strDate.length() == 10) {
            return strDate;
        } else {
            throw new RuntimeException("传入的日期格式有误");
        }
    }

    /**
     * 转换字符串为：yyyy-MM-dd HH:mm:ss 为Date
     *
     * @param strTime
     * @return
     */
    public static Date strToTime(String strTime) {
        strTime = strTime.trim();
        if (strTime.length() == 4) {
            strTime = strTime + "-01-01 00:00:00";
            return strToDate("yyyy-MM-dd HH:mm:ss", strTime);
        } else if (strTime.length() == 7) {
            strTime = strTime + "-01 00:00:00";
            return strToDate("yyyy-MM-dd HH:mm:ss", strTime);
        } else if (strTime.length() == 10) {
            strTime = strTime + " 00:00:00";
            return strToDate("yyyy-MM-dd HH:mm:ss", strTime);
        } else if (strTime.length() == 13) {
            strTime = strTime + ":00:00";
            return strToDate("yyyy-MM-dd HH:mm:ss", strTime);
        } else if (strTime.length() == 16) {
            strTime = strTime + ":00";
            return strToDate("yyyy-MM-dd HH:mm:ss", strTime);
        } else if (strTime.length() == 19) {
            return strToDate("yyyy-MM-dd HH:mm:ss", strTime);
        } else {
            throw new RuntimeException("传入的日期格式有误");
        }
    }

    public static String strToTimeString(String strTime) {
        strTime = strTime.trim();
        if (strTime.length() == 4) {
            strTime = strTime + "-01-01 00:00:00";
        } else if (strTime.length() == 7) {
            strTime = strTime + "-01 00:00:00";
        } else if (strTime.length() == 10) {
            strTime = strTime + " 00:00:00";
        } else if (strTime.length() == 13) {
            strTime = strTime + ":00:00";
        } else if (strTime.length() == 16) {
            strTime = strTime + ":00";
        } else {
            throw new RuntimeException("传入的日期格式有误");
        }
        return strTime;
    }

    public static String strToLastTimeString(String strTime) {
        strTime = strTime.trim();
        Date date = strToTime(strTime);
        if (strTime.length() == 4) {
            date = calcDate(date, 1);
            String dateString = convertDateToStr2(date);
            strTime = getYear(dateString) + "-01-01 00:00:00";
        } else if (strTime.length() == 7) {
            String dateString = getDifferMonths(date, 1);
            strTime = dateString + " 00:00:00";
        } else if (strTime.length() == 10) {
            String dateString = getDifferDays(date, 1);
            strTime = dateString + " 00:00:00";
        } else if (strTime.length() == 13) {
            strTime = getDifferHours(date, 1);
        } else if (strTime.length() == 16) {
            strTime = getDifferMinutes(date, 1);
        } else {
            throw new RuntimeException("传入的日期格式有误");
        }
        return strTime;
    }


    public static long sub(String mask, String strBeginDate, String strEndDate) {
        long dateRange = 0;
        Date beginDate = strToDate(mask, strBeginDate);
        Date endDate = strToDate(mask, strEndDate);
        dateRange = endDate.getTime() - beginDate.getTime();
        return dateRange;
    }

    public static long sub(String strBeginDate, String strEndDate) {
        long dateRange = 0;
        Date beginDate = strToDate(strBeginDate);
        Date endDate = strToDate(strEndDate);
        dateRange = endDate.getTime() - beginDate.getTime();
        return dateRange;
    }

    public static int subToSecond(String strBeginDate, String strEndDate) {
        String secNum = "";
        long dateRange = sub("yyyy-MM-dd HH:mm:ss", strBeginDate, strEndDate);
        secNum = "" + (dateRange / MILLISECONDS_PER_SECOND);
        return Integer.parseInt(secNum);
    }

    public static int subToSecondyMd(String strBeginDate, String strEndDate) {
        String secNum = "";
        long dateRange = sub("yyyyMMddHHmmss", strBeginDate, strEndDate);
        secNum = "" + (dateRange / MILLISECONDS_PER_SECOND);
        return Math.abs(Integer.parseInt(secNum));
    }

    public static boolean checkCurrentTime(String strdate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setLenient(false);
        try {
            formatter.parse(strdate);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static String currentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public static String getCtf() {
        Date nowDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MMddHHmmss");
        currentTime = formatter.format(nowDate);
        return currentTime;
    }

    public static Date getDateTimeByString(String date, String hour, String min) {
        String tmpTime = date + " " + hour + ":" + min + ":00";// 拼凑时间格式
        return convertDateToStandard(tmpTime);// 经过转换后将时间返回
    }

    public static Long getTime() {
        Date nowDate = new Date();
        return nowDate.getTime();
    }

    /**
     * 格式：yyyy-MM-dd HH:mm:ss
     */
    public static String getTime(Long time) {
        return getDate(time, "yyyy-MM-dd HH:mm:ss");
    }


    public static Long getDate() {
        try {
            String nowDate = getCurrentDate1();
            nowDate = nowDate + " 00:00:00";
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currentTime;
            currentTime = formatter.parse(nowDate);
            return currentTime.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据传入的格式格式化时间
     */
    public static String getDate(Long time, String mask) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat(mask);
        currentDate = formatter.format(date);
        return currentDate;
    }

    /**
     * 格式：yyyyMMdd
     */
    public static String getDate(Long time) {
        return getDate(time, "yyyyMMdd");
    }

    /**
     * 格式：yyyyMMdd
     */
    public static Date getDate1(Long time) {
        return new Date(time);
    }

    /**
     * 格式：yyyy-MM-dd
     */
    public static String getDate2(Long time) {
        return getDate(time, "yyyy-MM-dd");
    }


    /**
     * 获取当前月的日期范围，即最后一天
     */
    public static int getMonthRange() {
        Calendar cale = null;
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return Integer.parseInt(getDay(format.format(cale.getTime())));
    }

    /**
     * 获取当前月的日期范围
     */
    public static String getMonthRangeString() {
        String monthFirst = getMonthFirst() + "," + getMonthLast();
        return monthFirst;
    }


    public static String getCnDate() {
        String year = getCurrentYear();
        String month = getCurrentMonth();
        String day = getCurrentDay();
        return year + "年" + month + "月" + day + "日";
    }

    public static String getCnDate(Long date) {
        String year = getCurrentYear(date);
        String month = getCurrentMonth(date);
        String day = getCurrentDay(date);
        return year + "年" + month + "月" + day + "日";
    }

    public static String getEnDate(Long date) {
        Date nowDate = new Date(date);
        Locale locale = new Locale("en", "US");
        SimpleDateFormat format = new SimpleDateFormat("d & MMM. (EEE.), yyyy", locale);
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return format.format(nowDate).replace("&", "of");
    }

    public static String getEnDate() {
        Date date = new Date();
        Locale locale = new Locale("en", "US");
        SimpleDateFormat format = new SimpleDateFormat("d & MMM. (EEE.), yyyy", locale);
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return format.format(date).replace("&", "of");
    }

    public static String getEnDate2(Long date) {
        Date nowDate = new Date(date);
        Locale locale = new Locale("en", "US");
        SimpleDateFormat format = new SimpleDateFormat("MMM d&, yyyy", locale);
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return format.format(nowDate).replace("&", "th");
    }

    public static String getEnDate2() {
        Date date = new Date();
        Locale locale = new Locale("en", "US");
        SimpleDateFormat format = new SimpleDateFormat("MMM d&, yyyy", locale);
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return format.format(date).replace("&", "th");
    }

    public static String getGmtDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String dateString = dateFormat.format(date);
        return dateString;
    }

    public static Date getGmtDate(Date date) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String dateString = dateFormat.format(date);
        return dateFormat.parse(dateString);
    }

    public static Date getGmtDate(String dateString) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.parse(dateString);
    }

}
