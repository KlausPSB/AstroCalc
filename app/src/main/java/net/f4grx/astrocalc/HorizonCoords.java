package net.f4grx.astrocalc;

import java.util.Calendar;

public class HorizonCoords {
    private double jd;
    private double rlat, rlon; // in rads

    private double k = Math.PI/180.0;

    // http://www.geoastro.de/elevaz/basics/index.htm

    public void setDate(Calendar d) {
        jd = TimeUtil.JulianDay(d);
    }

    public double getJulianDate() {
        return jd;
    }

    public void setPos(double lat, double lon) {
        rlat = lat*k;
        rlon = lon*k;
    }

    //return alt, az in degrees, input is radec in degrees
    public double[] toHorizontal(double[] radec) {
        double sid = TimeUtil.siderealGreenwich(jd);

        double  tau = (sid - radec[0]) * k;
        double delta = radec[1] * k;

        tau += rlon;

        double sinh = Math.sin(rlat) * Math.sin(delta) + Math.cos(rlat) * Math.cos(delta) * Math.cos(tau);
        double tanaztop = -Math.sin(tau);
        double tanazbot = Math.cos(rlat) * Math.tan(delta) - Math.sin(rlat) * Math.cos(tau);

        return new double[]{Math.asin(sinh)/k, Math.atan2(tanaztop, tanazbot)/k};
    }
}
