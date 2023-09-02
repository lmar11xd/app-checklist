package com.smartory.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;


import java.text.DecimalFormat;

public class LocationService extends Service {

    LocationManager locManager;
    LocationListener locListener;
    Context context;
    LocationService activity;
    //FusedLocationProviderClient fusedLocationClient;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        context = this;
        //fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // Toast.makeText(context,"Servicio creado",Toast.LENGTH_LONG).show();
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locListener = new LocationListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onLocationChanged(Location location) {
                SharedPreferences sharedPref = getSharedPreferences("location_values", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                DecimalFormat df = new DecimalFormat("#.0000000");
                DecimalFormat dfaltitu = new DecimalFormat("#.0");
                if(location !=null){
                    editor.putString("latitud",String.valueOf(df.format(location.getLatitude())));
                    editor.putString("longitud",String.valueOf(df.format(location.getLongitude())));
                    editor.putString("altitud",String.valueOf(dfaltitu.format(location.getAltitude())));
                    editor.apply();
                }
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {}
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if(locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, locListener);

        } else if(locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0, locListener);
        }
    }

    @Override
    public void onDestroy() {
        if (locManager!=null)
            locManager.removeUpdates(locListener);
        super.onDestroy();
    }
}
