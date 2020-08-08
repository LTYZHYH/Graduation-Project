package com.example.travelapplication.infrastructure.utils;

import com.example.travelapplication.exception.ThrowableException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by peng on 2018/1/11.
 */

public class TimeFormatUtils {
    public static String getFormatDatetime(Date date) {
        if (date == null) {
            return "无";
        } else {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        }
    }

    public static String getFormatDate(Date date) {
        if (date == null) {
            return "无";
        } else {
            return new SimpleDateFormat("yyyy-MM-dd").format(date);
        }
    }

    public static Date getFormatDatetime(String date) {
        return format("yyyy-MM-dd HH:mm:ss", date);
    }

    public static Date getFormatDate(String date) {
        return format("yyyy-MM-dd", date);
    }

    private static Date format(String formation, String date) {
        if (date == null || date.isEmpty() || date.equals("无")) {
            return null;
        }
        Date formatDate;
        DateFormat dateFormat = new SimpleDateFormat(formation);

        try {
            formatDate = dateFormat.parse(date);
        } catch (ParseException e) {
            switch (formation) {
                case "yyyy-MM-dd HH:mm:ss":
                    throw new ThrowableException(new UnsupportedOperationException("时间格式错误，应为yyyy-MM-dd HH:mm:ss"));
                default:
                    throw new ThrowableException(new UnsupportedOperationException("时间格式错误，应为yyyy-MM-dd"));
            }
        }
        return formatDate;
    }
}
