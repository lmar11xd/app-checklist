package com.smartory.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.ResponseBody;

public class Util {

    public static double maxDigits(double number, int max_digits){
        String string = String.valueOf(number);
        if (string.length() > max_digits) {
            string = string.substring(0,11);
        }
        return Double.parseDouble(string);
    }

    public static String getLast30Date(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -30);
        String pattern = "yyyy-MM-dd";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(c.getTime()) + "T00:00:00";
    }

    public static boolean isCorrectFormatPoint(String cad){
        boolean hayPunto=false;
        int posicionDelPunto=0;
        int i=0;
        for( i=0;i<cad.length(); i++ )
            if ( cad.charAt(i) == '.'){
                hayPunto=true;
            }
        if(hayPunto){
            posicionDelPunto=cad.indexOf('.');
            return posicionDelPunto != cad.length() - 1 && posicionDelPunto != 0;
        }
        return true;
    }

    public static String only2Steps(String number){
        boolean hayPunto = false;
        for(int i=0;i<number.length(); i++ )
            if ( number.charAt(i) == '.'){
                hayPunto=true;
            }
        if(!hayPunto){
            return number;
        } else {
            int posicionDelPunto = number.indexOf('.');
            String parteEntera = "";
            String parteDecimal = "";
            for (int i = 0; i < posicionDelPunto; i++) {
                parteEntera = parteEntera + number.charAt(i);
            }
            for (int i = 0; i < parteEntera.length(); i++)
                for (i = posicionDelPunto + 1; i < number.length(); i++)
                    parteDecimal = parteDecimal + number.charAt(i);
            if (parteDecimal.length() > 2) {
                return parteEntera + "." + parteDecimal.substring(0,2);
            } else {
                if (parteDecimal.length() ==1){
                    return parteEntera + "." + parteDecimal + "0";
                }
                return number;
            }
        }
    }

    public static String getFecha(){
        String strMonth = "", strDay = "";
        final Calendar calendario = Calendar.getInstance();
        int yy = calendario.get(Calendar.YEAR);
        int mm = calendario.get(Calendar.MONTH);
        int dd = calendario.get(Calendar.DAY_OF_MONTH);
        mm++;

        if(mm < 10) strMonth = "0" + mm;
        else strMonth = "" + mm;

        if (dd < 10) strDay = "0" + dd;
        else strDay = "" + dd;

        return  yy + "-" + strMonth + "-" + strDay;
    }

    public static String getHora(){
        String strHoras = "", strMinutos = "", strSegundos = "";
        final Calendar calendario = Calendar.getInstance();
        int horas, minutos, segundos;
        horas = calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE);
        segundos = calendario.get(Calendar.SECOND);

        if (horas < 10) strHoras = "0" + horas;
        else strHoras = "" + horas;

        if (minutos < 10) strMinutos = "0" + minutos;
        else strMinutos = "" + minutos;

        if (segundos < 10) strSegundos = "0" + segundos;
        else strSegundos = "" + segundos;

        return  strHoras + ":" + strMinutos + ":" + strSegundos;
    }

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd / hh:mm:ss");
        return format.format(calendar.getTime()).replace(" / ", "T");
    }

    public static String getFileToByte(String filePath){
        Bitmap bmInitial = BitmapFactory.decodeFile(filePath);
        Bitmap bm = Bitmap.createScaledBitmap(bmInitial, 650, 400, false);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();
        return Base64.encodeToString(ba, Base64.DEFAULT);
    }
    private static String createName (String number){
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return "CheckList_" + number + "_"+ timeStamp + ".pdf";
    }
    public static File writeResponseBodyToDisk(ResponseBody body, String number) {
        File result;
        try {
            File path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS);
            File file = new File(path, createName(number));
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                if (!path.exists()) {
                    path.mkdirs();
                }
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                }
                outputStream.flush();
                result = file;
            } catch (IOException e) {
                result = null;
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    public static  String toNumeroChecklistFecha(String input) {
        //Input Format: 0000860 / 2023-01-11 01:22:00+00:00;
        try {
            if(input.contains(",")) return input;

            String[] inputs = input.split("/");
            String numeroChecklist = inputs [0];
            String fecha = inputs[1];

            return numeroChecklist.trim() + ", " + toFormatDate(fecha);
        } catch (Exception ex) {
            return input;
        }
    }

    public static String toFormatDate(String dateTimeString) {
        //Input Format 2023-01-11 01:22:00+00:00

        // Formato de entrada
        SimpleDateFormat inputFormat = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssXXX");
        } else {
            dateTimeString = dateTimeString.replace("+00:00", "");
            inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        // Formato de salida
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd, HH:mm");

        try {
            // Parsear la fecha y hora de entrada
            Date dateTime = inputFormat.parse(dateTimeString);

            // Formatear la fecha y hora en el formato deseado
            String formattedDateTime = outputFormat.format(dateTime);

            return formattedDateTime;
        } catch (Exception e) {
            return dateTimeString;
        }
    }
}
