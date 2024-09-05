package cn.xiangstudy.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author zhangxiang
 * @date 2024-08-21 16:06
 */
public class DateUtils {

    /**
     * @description: 获取当前时间
     * @author: zhangxiang
     * @date: 2024/9/5 11:41
     * @param: []
     * @return: java.util.Date
     */
    public static Date nowDate(){
        return new Date();
    }

    /**
     * @description: 获取当前时间
     * @author: zhangxiang
     * @date: 2024/9/5 11:43
     * @param: []
     * @return: java.time.LocalDateTime
     */
    public static LocalDateTime nowLocalDateTime(){
        return LocalDateTime.now();
    }

    /**
     * @description: date 转 localDateTime
     * @author: zhangxiang
     * @date: 2024/9/5 11:47
     * @param: [date]
     * @return: java.time.LocalDateTime
     */
    public static LocalDateTime dateToLocalDateTime(Date date){
//        Instant instant = date.toInstant();
//        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        ZonedDateTime zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault());
        return zonedDateTime.toLocalDateTime();
    }

    /**
     * @description: date 转 localDate
     * @author: zhangxiang
     * @date: 2024/9/5 16:16
     * @param: [date]
     * @return: java.time.LocalDate
     */
    public static LocalDate dateToLocalDate(Date date){
        ZonedDateTime zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault());
        return zonedDateTime.toLocalDate();
    }

    /**
     * @description: localDateTime 转 date
     * @author: zhangxiang
     * @date: 2024/9/5 11:59
     * @param: [localDateTime]
     * @return: java.util.Date
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime){
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    /**
     * @description: localDate 转 date
     * @author: zhangxiang
     * @date: 2024/9/5 16:21
     * @param: [localDate]
     * @return: java.util.Date
     */
    public static Date localDateToDate(LocalDate localDate){
        LocalDateTime localDateTime = localDate.atTime(0, 0, 0);
        return localDateTimeToDate(localDateTime);
    }

    /**
     * @description: 增加 xx 秒
     * @author: zhangxiang
     * @date: 2024/9/5 14:33
     * @param: [date, second]
     * @return: java.util.Date
     */
    public static Date plusSecond(Date date, Long second){
        Instant instant = date.toInstant().plusSeconds(second);
        return Date.from(instant);
    }

    /**
     * @description: 增加 xx 分钟
     * @author: zhangxiang
     * @date: 2024/9/5 14:30
     * @param: [date, minute]
     * @return: java.util.Date
     */
    public static Date plusMinute(Date date, Long minute){
        ZonedDateTime zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault());
        ZonedDateTime resultZone = zonedDateTime.plusMinutes(minute);
        return Date.from(resultZone.toInstant());
    }

    /**
     * @description: 增加 xx 小时
     * @author: zhangxiang
     * @date: 2024/9/5 14:41
     * @param: [date, hour]
     * @return: java.util.Date
     */
    public static Date plusHour(Date date, Long hour){
        ZonedDateTime zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault());
        ZonedDateTime resultZone = zonedDateTime.plusHours(hour);
        return Date.from(resultZone.toInstant());
    }

    /**
     * @description: 增加 xx 天
     * @author: zhangxiang
     * @date: 2024/9/5 14:59
     * @param: [date, day]
     * @return: java.util.Date
     */
    public static Date plusDay(Date date, Long day){
        ZonedDateTime zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault());
        ZonedDateTime resultZone = zonedDateTime.plusDays(day);
        return Date.from(resultZone.toInstant());
    }

    /**
     * @description: 增加 xx 星期
     * @author: zhangxiang
     * @date: 2024/9/5 15:03
     * @param: [date, week]
     * @return: java.util.Date
     */
    public static Date plusWeek(Date date, Long week){
        ZonedDateTime zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault());
        ZonedDateTime resultZone = zonedDateTime.plusWeeks(week);
        return Date.from(resultZone.toInstant());
    }

    /**
     * @description: 增加 xx 月
     * @author: zhangxiang
     * @date: 2024/9/5 15:05
     * @param: [date, month]
     * @return: java.util.Date
     */
    public static Date plusMonth(Date date, Long month){
        ZonedDateTime zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault());
        ZonedDateTime resultZone = zonedDateTime.plusMonths(month);
        return Date.from(resultZone.toInstant());
    }

    /**
     * @description: 减少 xx 秒
     * @author: zhangxiang
     * @date: 2024/9/5 14:45
     * @param: [date, second]
     * @return: java.util.Date
     */
    public static Date minusSecond(Date date, Long second){
        Instant instant = date.toInstant().minusSeconds(second);
        return Date.from(instant);
    }

    /**
     * @description: 减少 xx 分钟
     * @author: zhangxiang
     * @date: 2024/9/5 14:48
     * @param: [date, minute]
     * @return: java.util.Date
     */
    public static Date minusMinute(Date date, Long minute){
        ZonedDateTime zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault());
        ZonedDateTime resultZone = zonedDateTime.minusMinutes(minute);
        return Date.from(resultZone.toInstant());
    }

    /**
     * @description: 减少 xx 小时
     * @author: zhangxiang
     * @date: 2024/9/5 14:52
     * @param: [date, hour]
     * @return: java.util.Date
     */
    public static Date minusHour(Date date, Long hour){
        ZonedDateTime zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault());
        ZonedDateTime resultZone = zonedDateTime.minusHours(hour);
        return Date.from(resultZone.toInstant());
    }

    /**
     * @description: 减少 xx 天
     * @author: zhangxiang
     * @date: 2024/9/5 14:56
     * @param: [date, day]
     * @return: java.util.Date
     */
    public static Date minusDay(Date date, Long day){
        ZonedDateTime zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault());
        ZonedDateTime resultZone = zonedDateTime.minusDays(day);
        return Date.from(resultZone.toInstant());
    }

    /**
     * @description: 减少 xx 周
     * @author: zhangxiang
     * @date: 2024/9/5 15:41
     * @param: [date, week]
     * @return: java.util.Date
     */
    public static Date minusWeek(Date date, Long week){
        ZonedDateTime zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault());
        ZonedDateTime resultZone = zonedDateTime.minusWeeks(week);
        return Date.from(resultZone.toInstant());
    }

    /**
     * @description: 减少 xx 月
     * @author: zhangxiang
     * @date: 2024/9/5 15:44
     * @param: [date, month]
     * @return: java.util.Date
     */
    public static Date minusMonth(Date date, Long month){
        ZonedDateTime zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault());
        ZonedDateTime resultZone = zonedDateTime.minusMonths(month);
        return Date.from(resultZone.toInstant());
    }

    /**
     * @description: date 转 字符串
     * @author: zhangxiang
     * @date: 2024/9/5 15:52
     * @param: [date, pattern] [date,"yyyy-MM-dd HH:mm:ss"]
     * @return: java.lang.String
     */
    public static String dateToStr(Date date, String pattern){
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(dateTimeFormatter);
    }

    /**
     * @description: 字符串 转 date; 字符串必须有时间
     * @author: zhangxiang
     * @date: 2024/9/5 16:07
     * @param: [strDate, pattern]; ["2024-09-05 16:00:00", "yyyy-MM-dd HH:mm:ss"]
     * @return: java.util.Date
     */
    public static Date haveTimeStrToDate(String strDate, String pattern){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime parse = LocalDateTime.parse(strDate, dateTimeFormatter);
        return localDateTimeToDate(parse);
    }

    /**
     * @description:  字符串 转 date; 字符串不能有时间
     * @author: zhangxiang
     * @date: 2024/9/5 16:26
     * @param: [strDate, pattern]; ["2024-10-05","yyyy-MM-dd"]
     * @return: java.util.Date
     */
    public static Date noTimeStrToDate(String strDate, String pattern){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate parse = LocalDate.parse(strDate, dateTimeFormatter);
        return localDateToDate(parse);
    }

    /** 
     * @description: 获取月份的第一天; 时间为 00:00:00
     * @author: zhangxiang
     * @date: 2024/9/5 17:21
     * @param: [date]
     * @return: java.util.Date
     */
    public static Date startOfMonth(Date date){
        LocalDate localDate = dateToLocalDate(date);
        LocalDate firstLocalDate = localDate.withDayOfMonth(1);
        return localDateToDate(firstLocalDate);
    }

    /**
     * @description: 获取月份的最后一天; 时间为 23:59:59
     * @author: zhangxiang
     * @date: 2024/9/5 17:35
     * @param: [date]
     * @return: java.util.Date
     */
    public static Date endOfMonth(Date date){
        LocalDate localDate = dateToLocalDate(date);
        LocalDate lastLocalDay = localDate.withDayOfMonth(localDate.lengthOfMonth());
        LocalDateTime lastDay = LocalDateTime.of(lastLocalDay, LocalTime.MAX);
        return localDateTimeToDate(lastDay);
    }

}

