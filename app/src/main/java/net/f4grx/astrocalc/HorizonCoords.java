package net.f4grx.astrocalc;

import java.util.Calendar;

public class HorizonCoords {
    private double jd;
    private double lat, lon; // in degs

    private double k = Math.PI/180.0;

    // http://www.geoastro.de/elevaz/basics/index.htm

    public void setDate(Calendar d) {
        jd = TimeUtil.JulianDay(d);
    }

    public double getJulianDate() {
        return jd;
    }

    public void setPos(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    //return alt, az in degrees, input is radec_deg in degrees
    public double[] toHorizontal(double[] radec_deg) {
        double sidtime = TimeUtil.sidereal(jd, lon);
        double sidangle = sidtime * 15.0;

        double tau = (sidangle - radec_deg[0]) * k; // in radians
        double delta = radec_deg[1] * k; // in radians

        double lat_rad = lat * k;

        double sinh = Math.sin(lat_rad) * Math.sin(delta) + Math.cos(lat_rad) * Math.cos(delta) * Math.cos(tau);
        double tanaztop = -Math.sin(tau);
        double tanazbot = Math.cos(lat_rad) * Math.tan(delta) - Math.sin(lat_rad) * Math.cos(tau);

        double az = Math.atan2(tanaztop,tanazbot)/k;
        if(az < 0) az += 360;

        return new double[]{Math.asin(sinh)/k, az};
    }
}
