package com.smartory.utils;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import java.util.List;

public class Position {
    private static LocationManager locManager;
    public static boolean gps_enabled = false;
    public static boolean network_enabled = false;
    Context context;
    protected static LocationListener locationListener;
    public Position(Context context) {
        this.context = context;
    }

    @SuppressLint("MissingPermission")
    public static Location getLastBestLocation(Context _context) {
        if (locManager == null) {
            locManager = (LocationManager) _context.getSystemService(LOCATION_SERVICE);
            gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        Location bestResult = null;
        float bestAccuracy = Float.MAX_VALUE;
        List<String> matchingProviders = locManager.getAllProviders();
        for (String provider : matchingProviders) {
            @SuppressLint("MissingPermission") Location location = locManager.getLastKnownLocation(provider);
            if (location != null) {
                float accuracy = location.getAccuracy();
                if (accuracy < bestAccuracy) {
                    bestResult = location;
                    bestAccuracy = accuracy;
                }
            }
        }
        return bestResult;
    }

    public static void actualizarPosicion(Context context,boolean start) {
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
         locationListener = new LocationListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onLocationChanged(Location location) {
                SharedPreferences sharedPref = context.getSharedPreferences("values", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                if(location !=null){
                    if(start){
                        editor.putString("lastLatitud",String.valueOf(location.getLatitude()));
                        editor.putString("lastLongitud",String.valueOf(location.getLongitude()));
                        editor.apply();
                    }
                }
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {}
        };
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        } else if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }

    }

}