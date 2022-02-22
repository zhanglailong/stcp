package org.jeecg.modules.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间处理工具类
 * @author shx
 */
public class FormatTimeEightUtils {
    /**
     * 时间增加 指定大小
     * @param time 原时间
     * @return 改变后的时间
     */
    public static Date timeReversal(String time,Long TimeStepSize){
        try {
            Date date = null;
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = sd.parse(time);
            long rightTime = (long) (date.getTime() + TimeStepSize);
            Date newDate = sd.parse(sd.format(rightTime));
            return newDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 时间减少 指定大小
     * @param time 原时间
     * @return 改变后时间
     */
    public static Date reduceTime (String time,Long TimeStepSize){
        try {
            Date date = null;
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = sd.parse(time);
            long rightTime = (long) (date.getTime() - TimeStepSize);
            Date newDate = sd.parse(sd.format(rightTime));
            return newDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 时间格式转化 例：（2021-08-26T07:48:30.000000）
     * @param time 原时间格式
     * @return time
     */
    public static String formattingTime(String time){
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
            Date  date = df.parse(time);
            SimpleDateFormat df1 = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
            Date date1 =  df1.parse(date.toString());
            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return df2.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
