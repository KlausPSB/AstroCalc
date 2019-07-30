package net.f4grx.astrocalc;

import android.util.Log;

public class Locator {

    public static String fromCoords(double lat, double lon) {
        // https://ham.stackexchange.com/questions/221/how-can-one-convert-from-lat-long-to-grid-square
        double adjLat,adjLon;
        char GLat,GLon;
        String nLat,nLon;
        char gLat,gLon;
        double rLat,rLon;
        String U = "ABCDEFGHIJKLMNOPQRSTUVWX";
        String L = U.toLowerCase();

        // support Chris Veness 2002-2012 LatLon library and
        // other objects with lat/lon properties
        // properties could be getter functions, numbers, or strings


        if (Double.isNaN(lat)) {
            Log.d("LatLon", "lat is NaN");
            return null;
        }
        if (Double.isNaN(lon)) {
            Log.d("LatLon", "lon is NaN");
            return null;
        }
        if (Math.abs(lat) == 90.0) {
            Log.d("LatLon", "grid squares invalid at N/S poles");
            return null;
        }
        if (Math.abs(lat) > 90) {
            Log.d("LatLon", "invalid latitude: "+lat);
            return null;
        }
        if (Math.abs(lon) > 180) {
            Log.d("LatLon", "invalid longitude: "+lon);
            return null;
        }

        adjLat = lat + 90;
        adjLon = lon + 180;
        GLat = U.charAt((int) (adjLat/10));
        GLon = U.charAt((int) (adjLon/20));
        nLat = ""+(int)(adjLat % 10);
        nLon = ""+(int)((adjLon/2) % 10);
        rLat = (adjLat - (int)(adjLat)) * 60;
        rLon = (adjLon - 2*(int)(adjLon/2)) *60;
        gLat = L.charAt((int)(rLat/2.5));
        gLon = L.charAt((int)(rLon/5));
        String locator = ""+GLon+GLat+nLon+nLat+gLon+gLat;
        return locator;
    }

    /* Returns lat, lon of the center of the square, in that order */
    public static double[] decode(String str) {
        // view-source:https://www.egloff.eu/googlemap_v3/mhtolatlng.js
        double[] d = new double[2];
        int[] loca = new int[10];

        str = str.toUpperCase();

        for(int i=0;i<str.length();i++) {
            loca[i] = str.charAt(i) - 65;
        }
        loca[2] += 17; loca[3] += 17;
        loca[6] += 17; loca[7] += 17;

        d[0] = 0;
        d[1] = 0;
        if(str.length() >= 2) {
            d[0] += loca[1] * 10;
            d[1] += loca[0] * 20;
        }
        if(str.length() == 2) {
            d[0] += 5;
            d[1] += 10;
        }
        if(str.length() >= 4) {
            d[0] += loca[3];
            d[1] += loca[2] * 2;
        }
        if(str.length() == 4) {
            d[0] += 0.5;
            d[1] += 1;
        }
        if(str.length() >= 6) {
            d[0] += loca[5] / 24.0d;
            d[1] += loca[4] / 12.0d;
        }
        if(str.length() == 6) {
            d[0] += 1/48.0d;
            d[1] += 1/24.0d;
        }
        if(str.length() >= 8) {
            d[0] += loca[7]/240.0d ;
            d[1] += loca[6]/120.0d;
        }
        if(str.length() == 8) {
            d[0] += 1/480.0d;
            d[1] += 1/240.0d;
        }

        d[0] -= 90;
        d[1] -= 180;

        return d;
    }
}
