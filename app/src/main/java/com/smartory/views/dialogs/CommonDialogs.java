package com.smartory.views.dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.EditText;
import com.smartory.db.SQLTables;
import com.smartory.views.activities.LoginActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.Calendar;

public class CommonDialogs {

    public static void showLogoutDialog(final Activity activity, final Context context){
        new MaterialAlertDialogBuilder(activity)
                .setTitle("Salir")
                .setMessage("¿Está seguro de salir de la aplicación? Todos los datos no enviados serán eliminados.")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Salir", (dialog, which) -> {
                    SharedPreferences sharedPref = context.getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
                    SQLTables DataTables = new SQLTables();
                    DataTables.Delete(context);
                    // SharedPreferences.Editor editor = sharedPref.edit();
                    sharedPref.edit().clear().apply();
                    //editor.putString("logueado", "inactive");
                    // editor.apply();
                    activity.finish();
                    Intent i = new Intent(activity, LoginActivity.class);
                    activity.startActivity(i);
                })
                .show();
    }

    public static void dialogPickerFecha (Activity activity, final EditText et_fecha){
        final Calendar calendario = Calendar.getInstance();
        int yy = calendario.get(Calendar.YEAR);
        int mm = calendario.get(Calendar.MONTH);
        int dd = calendario.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(activity, (view, year, monthOfYear, dayOfMonth) -> {
            String fecha = year+"-"+ (monthOfYear+1)+"-"+ dayOfMonth;
            et_fecha.setText(fecha);
        }, yy, mm, dd);
        datePicker.show();
    }

    public static void dialogPickerHoraFormat24h (Activity activity, final EditText et_hora){
        final Calendar c = Calendar.getInstance();
        final int hora = c.get(Calendar.HOUR_OF_DAY);
        final int minuto = c.get(Calendar.MINUTE);
        TimePickerDialog recogerHora = new TimePickerDialog(activity, (view, hourOfDay, minute) -> {
            String min;
            if(minute<10) min="0"+minute;
            else min= String.valueOf(minute);
            String hora1 = hourOfDay+":"+min;
            et_hora.setText(hora1); //Muestro la hora con el formato deseado
        }, hora, minuto, false);

        recogerHora.show();
    }

}
