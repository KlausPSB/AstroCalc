package net.f4grx.astrocalc;

import java.util.Calendar;

public class TimeUtil {

    //confirmed by http://www.csgnetwork.com/juliandatetodaycalc.html
    //https://www.rgagnon.com/javadetails/java-0506.html

    // Gregorian Calendar adopted Oct. 15, 1582 (2299161)
    private static int JGREG = 15 + 31*(10+12*1582);

    public static double JulianDay(Calendar d) {

        int day    = d.get(Calendar.DAY_OF_MONTH);
        int month  = d.get(Calendar.MONTH) + 1; //we require one based, calendar gives zero based.
        int year   = d.get(Calendar.YEAR);
        double UT  = d.get(Calendar.HOUR) + d.get(Calendar.MINUTE)/60.0 + d.get(Calendar.SECOND)/3600.0 + d.get(Calendar.MILLISECOND)/3600000.0;

        if(year < 0) {
            year += 1;
        }

        if (month<=2) {
            month = month + 12;
            year = year - 1;
        } else {
            month = month + 1;
        }

        double julian = 0;

        if (day + 31 * (month + 12 * year) >= JGREG) {
            // change over to Gregorian calendar
            int ja = (int)(0.01 * year);
            julian += 2 - ja + (0.25 * ja);
        }

        julian += (int)(365.25*year);
        julian += (int)(30.6001*month);
        julian += day;
        julian += 1720995.0;
        julian += UT/24.0;

        return julian;

        //return Math.floor(365.25*(year+4716)) + Math.floor(30.6001*(month+1)) + date - 13 -1524.5 + (UT)/24.0;
    }

    //siderealTime angle in hours
    public static double siderealTimeGreenwich(double JD) {
        /*
        double T = (JD - 2451545.0 ) / 36525;
        double theta0 = 280.46061837 + 360.98564736629*(JD-2451545.0) + 0.000387933*T*T - T*T*T/38710000.0;
        return theta0;
        */
        double MJD = JD - 2400000.5;
        double MJD0 = (int)(MJD);
        double ut = (MJD - MJD0)*24.0;
        double t_eph  = (MJD0-51544.5)/36525.0;
        double sidtime =  6.697374558 + 1.0027379093*ut + (8640184.812866 + (0.093104 - 0.0000062*t_eph)*t_eph)*t_eph/3600.0;
        return sidtime;
    }

    private static double frac(double X) {
        X = X - (int)(X);
        if (X<0) X = X + 1.0;
        return X;
    }

    // in hours
    public static double siderealTime(double JD, double lon_deg) {
        double GMST = siderealTimeGreenwich(JD);
        return 24.0*frac((GMST + lon_deg/15.0)/24.0);
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
