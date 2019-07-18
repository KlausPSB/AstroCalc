package net.f4grx.astrocalc;

import android.util.Log;

import java.util.Calendar;

public class Sun {
    // http://www.geoastro.de/elevaz/basics/meeus.htm#solar

    // get [ra dec] of sun at given julian date
    public static double[] pos(double JD) {

        // number of Julian centuries since Jan 1, 2000, 12 UT
        double T = (JD-2451545.0) / 36525;

        // Solar Ecliptic Coordinates (according to: Jean Meeus: Astronomical Algorithms), accuracy of 0.01 degree
        double k = Math.PI/180;

        //mean anomaly, degree
        double M = 357.52910 + 35999.05030*T - 0.0001559*T*T - 0.00000048*T*T*T;

        // mean longitude, degree
        double L0 = 280.46645 + 36000.76983*T + 0.0003032*T*T;

        // Sun's equation of center
        double DL = (1.914600 - 0.004817*T - 0.000014*T*T)*Math.sin(k*M)
                + (0.019993 - 0.000101*T)*Math.sin(k*2*M) + 0.000290*Math.sin(k*3*M);

        // true longitude, degree
        double L = L0 + DL;

        // convert ecliptic longitude L to right ascension RA and declination delta
        //(the ecliptic latitude of the Sun is assumed to be zero):

        // obliquity eps of ecliptic:
        double eps = 23.0 + 26.0/60.0 + 21.448/3600.0 - (46.8150*T + 0.00059*T*T - 0.001813*T*T*T)/3600;

        double X = Math.cos(k*L);
        double Y = Math.cos(k*eps)*Math.sin(k*L);
        double Z = Math.sin(k*eps)*Math.sin(k*L);
        double R = Math.sqrt(1.0-Z*Z);

        double delta = Math.atan2(Z,R)/k; // in degrees
        double RA = 2.0*Math.atan2(Y,(X+R))/k; // in degrees

        return new double[] {RA, delta};
    }
}
