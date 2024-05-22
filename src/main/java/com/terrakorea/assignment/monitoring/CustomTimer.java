package com.terrakorea.assignment.monitoring;

import org.springframework.stereotype.Component;

@Component
public class CustomTimer {

    public static Long aSecond = 1000L;
    public static Long aMinute = 1000L * 60;
    public static Long aHour = 1000L * 60 * 60;
    public static Long aDay = 1000L * 60 * 60 * 24;
    public static String seoul = "Asia/Seoul";

}
