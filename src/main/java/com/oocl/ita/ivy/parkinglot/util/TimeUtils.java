package com.oocl.ita.ivy.parkinglot.util;

import java.util.Date;

public class TimeUtils {
    static int millisecond_to_second_unit = 1000;
    static int second_to_minute_unit = 60;
    static int minute_to_hour_unit = 60;
    public static long getTwoDateDiffHours(Date startDate, Date endDate){
        long time = endDate.getTime() - startDate.getTime();
        long second = time / millisecond_to_second_unit;
        long minute = second / second_to_minute_unit;
        return minute / minute_to_hour_unit;
    }
}
