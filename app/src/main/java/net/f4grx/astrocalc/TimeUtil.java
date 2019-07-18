package net.f4grx.astrocalc;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    public static double JulianDay(Calendar d) {

        int date   = d.get(Calendar.DAY_OF_MONTH);
        int month  = d.get(Calendar.MONTH) + 1;
        int year   = d.get(Calendar.YEAR);
        double UT  = d.get(Calendar.HOUR) + d.get(Calendar.MINUTE)/60.0 + d.get(Calendar.SECOND)/3600.0 + d.get(Calendar.MILLISECOND)/3600000.0;

        if (month<=2) {
            month=month+12;
            year=year-1;
        }

        return (int)(365.25*year) + (int)(30.6001*(month+1)) - 15 + 1720996.5 + date + UT/24.0;
    }

    //sidereal angle in degrees
    public static double siderealGreenwich(double JD) {
        double T = (JD - 2451545.0 ) / 36525;

        double theta0 = 280.46061837 + 360.98564736629*(JD-2451545.0) + 0.000387933*T*T - T*T*T/38710000.0;

        return (theta0) % 360.0;
    }

    /*fractional hours to hours,minutes,seconds*/
    public static int[] dms(double time) {
        int h = (int)time;
        time = (time - h) * 60;
        int m = (int)time;
        time = (time - m) * 60;
        int s = (int)time;
        time = (time - s) * 1000;
        return new int[] {h,m,s,(int)time};
    }
}
