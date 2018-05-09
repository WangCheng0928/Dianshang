package com.mmall.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateTimeUtil {
    //joda-time来进行转换
    //str->date
    public static final String STANDARD_FORMAT="yyyy-MM-dd HH:mm:ss";

    //使用任意格式
    public static Date strToDate(String dateTimestr,String formatStr){
        DateTimeFormatter dateTimeFormat=DateTimeFormat.forPattern(formatStr);
        DateTime dateTime=dateTimeFormat.parseDateTime(dateTimestr);
        return dateTime.toDate();
    }
    //date->str
    public static String dateToStr(Date date,String formatStr){
        if(date==null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime=new DateTime(date);
        return dateTime.toString(formatStr);
    }
    //重载使用固定格式
    public static Date strToDate(String dateTimestr){
        DateTimeFormatter dateTimeFormat=DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime=dateTimeFormat.parseDateTime(dateTimestr);
        return dateTime.toDate();
    }
    //date->str
    public static String dateToStr(Date date){
        if(date==null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime=new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }
}
