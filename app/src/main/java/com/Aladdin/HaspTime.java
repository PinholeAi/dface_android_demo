/*
 *  Copyright (C) 2013, SafeNet, Inc. All rights reserved.
 *  Use is subject to license terms.
 */

package com.Aladdin;

import com.Aladdin.HaspStatus;

public class HaspTime
{
    private long time[] = {0};
    private int day[] = {0};
    private int month[] = {0};
    private int year[] = {0};
    private int hour[] = {0};
    private int minute[] = {0};
    private int second[] = {0};
    private int status;

    /*
     * private native functions
     *
     */
    private static native int DatetimeToHasptime(int day, int month, int year, int hour, int minute, int second, long time[]);
    private static native int HasptimeToDatetime(long time, int day[], int month[], int year[],int hour[], int minute[], int second[]);

    /**
     * IA 64 not considered yet
     */
    static
    {
        HaspStatus.Init();
    }

    /**
     * HaspTime constructor.
     *
     * @param      year         input year
     * @param      month        input month
     * @param      day          input day
     * @param      hour         input hour
     * @param      minute       input minute
     * @param      second       input second
     *
     */
    public HaspTime(int year, int month, int day, int hour,
                    int minute, int second)
    {
        status = DatetimeToHasptime(day, month, year, hour, minute, second, time);
    }

    public HaspTime(long hasptime)
    {
        time[0] = hasptime;
        status = HasptimeToDatetime(hasptime,day,month,year,hour,minute,second);
    }

    /**
     * Returns the error that occurred in the last function call.
     */
    public int getLastError()
    {
        return status;
    }

    /**
     * Returns the HASP Time value in UTC format.
     */
    public long getHaspTime()
    {
        return time[0];
    }

    /**
     * Returns the month value of the time.
     */
    public int getMonth()
    {
        return month[0];
    }

    /**
     * Returns the year value of the time.
     */
    public int getYear()
    {
        return year[0];
    }

    /**
     * Returns the day value of the time.
     */
    public int getDay()
    {
        return day[0];
    }

    /**
     * Returns the hour value of the time.
     */
    public int getHour()
    {
        return hour[0];
    }

    /**
     * Returns the minute value of the time.
     */
    public int getMinute()
    {
        return minute[0];
    }

    /**
     * Returns the second value of the time.
     */
    public int getSecond()
    {
        return second[0];
    }
}

