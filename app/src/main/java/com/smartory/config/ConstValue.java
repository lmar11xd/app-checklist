package com.smartory.config;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class ConstValue {

    public static String EDIT ="editar" ;
    public static String VIEW ="ver" ;
    public static String VALIDATE ="validar" ;

    public static void enablePermission(Activity activity, Context context){
        int accessFinePermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        int accessCoarsePermission = ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION);
        int accessWritePermission = ActivityCompat.checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int accessCamera = ActivityCompat.checkSelfPermission(context,Manifest.permission.CAMERA);

        if(accessFinePermission!= PackageManager.PERMISSION_GRANTED &&
                accessCoarsePermission!=PackageManager.PERMISSION_GRANTED &&
                accessWritePermission!=PackageManager.PERMISSION_GRANTED &&
                accessCamera!=PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);}
    }

}
