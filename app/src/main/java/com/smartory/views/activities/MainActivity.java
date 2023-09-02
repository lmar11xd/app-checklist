package com.smartory.views.activities;

import static com.smartory.resource.HorizontalValues.URL_TO_DOWNLOAD;
import static com.smartory.resource.HorizontalValues.ha;
import static com.smartory.resource.HorizontalValues.isRunning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.smartory.BuildConfig;
import com.smartory.R;
import com.smartory.db.SQLTables;
import com.smartory.db.SqliteClass;
import com.smartory.models.APKVersionControl;
import com.smartory.models.CheckListModel;
import com.smartory.models.CreateLocationModel;
import com.smartory.models.GetLocationCreateModel;
import com.smartory.models.ItemCheckListModel;
import com.smartory.models.LocationModel;
import com.smartory.models.PostItemCheckListModel;
import com.smartory.network.InterfaceAPI;
import com.smartory.network.ReloadData;
import com.smartory.network.RetrofitClientInstance;
import com.smartory.resource.HorizontalValues;
import com.smartory.utils.ConnectionDetector;
import com.smartory.utils.Position;
import com.smartory.utils.Util;
import com.smartory.views.dialogs.CommonDialogs;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "1901";
    private static final String YES_ACTION = "YES_ACTION";
    CardView crd_new_check, crd_list_check, crd_lev;
    @SuppressLint("StaticFieldLeak")
    static Context context;
    ConnectionDetector connectionDetector;
    Position position;
    static ProgressDialog dialog;
    static Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
    static final InterfaceAPI api = retrofit.create(InterfaceAPI.class);
    TextView usuario_text_show, company_text_show, name_version;
    private static final short REQUEST_CODE = 6545;
    private static boolean runnnig = false;
    static ReloadData reloadData;
    static String pattern = "yyyy-MM-dd'T'HH:mm:ss";
    static SimpleDateFormat simpleDateFormat;
    String employee_name;
    String company_name;
    static String token;
    Boolean is_staff, is_superuser;
    Boolean is_group_supervisor, is_group_inspector, is_group_sub_employee, is_group_staff, is_group_sub_staff, is_group_driver;
    Boolean can_create_checklist, can_validate_checklist, can_edit_checklist, can_edit_validated_checklist, can_extend_noncompliance_checklist;
    SharedPreferences.Editor editor_login_preferences;
    static String datetime_sync = "";
    static PendingIntent pendingIntent;

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView txtBranch = findViewById(R.id.txtBranch);
        isRunning = false;

        if(HorizontalValues.BASE_URL.contains(".sandbox.")) {
            txtBranch.setText("SANDBOX");
        } else if(HorizontalValues.BASE_URL.contains(".dev.")) {
            txtBranch.setText("DEVELOPMENT");
        } else if(HorizontalValues.BASE_URL.contains(".qa.")) {
            txtBranch.setText("TESTING");
        }

        context = this;
        crd_new_check = findViewById(R.id.ideaCard_new);
        usuario_text_show = findViewById(R.id.usuario_text_show);
        company_text_show = findViewById(R.id.company_text_show);
        name_version = findViewById(R.id.name_version);
        crd_list_check = findViewById(R.id.ideaCard_lista);
        crd_lev = findViewById(R.id.ideaCard_lev);
        connectionDetector = new ConnectionDetector(context);
        simpleDateFormat = new SimpleDateFormat(pattern);
        position = new Position(context);
        ImageView imagenSinConexion = findViewById(R.id.iv_nointernet);
        imagenSinConexion.setVisibility(View.INVISIBLE);
        SharedPreferences sharedPref = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        editor_login_preferences = sharedPref.edit();
        is_staff = sharedPref.getBoolean("is_staff", false);
        is_superuser = sharedPref.getBoolean("is_superuser", false);
        company_name = sharedPref.getString("company_name", "");
        company_text_show.setText(company_name);
        is_group_supervisor = sharedPref.getBoolean("is_group_supervisor", false);
        is_group_inspector = sharedPref.getBoolean("is_group_inspector", false);
        is_group_sub_employee = sharedPref.getBoolean("is_group_sub_employee", false);
        is_group_staff = sharedPref.getBoolean("is_group_staff", false);
        is_group_sub_staff = sharedPref.getBoolean("is_group_sub_staff", false);
        is_group_driver = sharedPref.getBoolean("is_group_driver", false);
        can_create_checklist = sharedPref.getBoolean("can_create_checklist", false);
        can_validate_checklist = sharedPref.getBoolean("can_validate_checklist", false);
        can_edit_checklist = sharedPref.getBoolean("can_edit_checklist", false);
        can_edit_validated_checklist = sharedPref.getBoolean("can_edit_validated_checklist", false);
        can_extend_noncompliance_checklist = sharedPref.getBoolean("can_extend_noncompliance_checklist", false);
        employee_name = sharedPref.getString("employee_name", "");
        usuario_text_show.setText(employee_name);
        token = sharedPref.getString("token", "");
        name_version.setText("VERSIÓN: " + BuildConfig.VERSION_NAME);
        if (can_extend_noncompliance_checklist || sharedPref.getBoolean("can_accept_observation_lifting", false)) {
            crd_lev.setVisibility(View.VISIBLE);
        } else {
            crd_lev.setVisibility(View.INVISIBLE);
        }

        if (can_create_checklist) {
            crd_new_check.setVisibility(View.VISIBLE);
        } else {
            crd_new_check.setVisibility(View.GONE);
        }
        ha = new Handler();
        crd_new_check.setOnClickListener(v -> {
            SharedPreferences sharedPrefCheckList = context.getSharedPreferences("check_list", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefCheckList.edit();
            editor.putString("estado", "create");
            editor.apply();
            Intent intent = new Intent(MainActivity.this, FormNewCheckListActivity.class);
            startActivity(intent);
        });

        crd_list_check.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListChecksActivity.class);
            startActivity(intent);
        });

        crd_lev.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LevObsActivity.class);
            startActivity(intent);
        });
        createNotificationChannel();
        startChekVersion();
    }

    private final BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = manager.getActiveNetworkInfo();
            onNetworkChange(ni);
        }
    };

    private DialogInterface.OnClickListener startChekVersion() {
        ha = new Handler();
        isRunning = true;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (connectionDetector.isConnectingToInternet()) {
                    new getAPKCurrent().execute(false);
                }
                if (isRunning) {
                    ha.postDelayed(this, 20000);
                }
            }
        };
        ha.postDelayed(runnable, 20000);
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        unregisterReceiver(networkStateReceiver);
        super.onPause();
    }

    private void onNetworkChange(NetworkInfo networkInfo) {
        if (networkInfo != null) {
            if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                ArrayList<CheckListModel> checkListModels = SqliteClass.getInstance(context).databasehelp.checkListSql.getAllChecksByParam(SqliteClass.KEY_CHECENV, "NO");
                ArrayList<CheckListModel> checkListModels2 = SqliteClass.getInstance(context).databasehelp.checkListSql.getAllChecksByParam(SqliteClass.KEY_CHECENV, "NO-EDIT");
                if ((checkListModels.size() > 0 || checkListModels2.size() > 0) && !runnnig) {
                    new MaterialAlertDialogBuilder(context)
                            .setTitle("Sincronizar Checklists")
                            .setMessage("Tiene algunos checklist sin sincronizar ¿ Desea enviarlos ahora ?")
                            .setNegativeButton("Cancelar", null)
                            .setPositiveButton("Ok", (dialog, which) -> new createCheckListTask().execute(true)).show();
                }
            }
            Toast.makeText(context, "Conectado", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Sin conexión", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_global, menu);
        menu.findItem(R.id.action_version).setTitle(BuildConfig.VERSION_NAME);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_update) {
            if (connectionDetector.isConnectingToInternet()) {
                dialog = ProgressDialog.show(context, "", "Cargando... Espere en esta pantalla por favor", true);
                new updateTask().execute(String.valueOf(datetime_sync));
                editor_login_preferences.putString("datetime_sync", simpleDateFormat.format(new Date()));
                editor_login_preferences.apply();
            } else {
                Toast.makeText(context, "El equipo no tiene conexión a internet", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.action_logout) {
            CommonDialogs.showLogoutDialog(this, context);
        } else if (id == R.id.action_new_version) {
            download();
        }
        return super.onOptionsItemSelected(item);
    }

    public void download() {
        if (!connectionDetector.isConnectingToInternet()) {
            Toast.makeText(getApplicationContext(), "El equipo no tiene conexión a internet", Toast.LENGTH_LONG).show();
            return;
        }
        if (!isDownloadManagerAvailable()) {
            Toast.makeText(this, "El metodo de descarga no esta disponible", Toast.LENGTH_LONG).show();
            return;
        }
        checkSelfPermission();
    }

    @SuppressLint("ObsoleteSdkInt")
    private static boolean isDownloadManagerAvailable() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    private void returnLogin() {
        isRunning = false;
        if (ha != null) {
            ha.removeCallbacksAndMessages(null);
            ha = null;
        }
        Intent intent = new Intent(MainActivity.this, ConfirmUpdateVersionActivity.class);
        startActivity(intent);
    }

    private void checkSelfPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            new getAPKCurrent().execute(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new getAPKCurrent().execute(true);
            } else {
                Toast.makeText(this, "Se necesitan permisos", Toast.LENGTH_LONG).show();
            }
        }
    }

    static public class createCheckListTask extends AsyncTask<Boolean, Void, String> {
        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(Boolean... booleans) {
            boolean rpta = true;
            ArrayList<CheckListModel> checkListModels =
                    SqliteClass.getInstance(context).databasehelp.checkListSql.getAllChecksByParam(SqliteClass.KEY_CHECENV, "NO");
            for (int a = 0; a < checkListModels.size(); a++) {
                CheckListModel verification1 = checkListModels.get(a);
                if (verification1.getType().equals("local")) {
                    SharedPreferences sharedPref = context.getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
                    if (checkListModels.get(a).getDatetime() == null || checkListModels.get(a).getDatetime().equals("")) {
                        checkListModels.get(a).setDatetime(Util.getCurrentDate());
                    }
                    // Call<CheckListModel> postCheckListHead = api.postCheckList(sharedPref.getString("token", ""), checkListModels.get(a));
                    CheckListModel newCheckList;
                    SharedPreferences sharedPrefChecK = context.getSharedPreferences("check_list", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPrefChecK.edit();
                    Call<GetLocationCreateModel> postLocation = null;
                    if (checkListModels.get(a).getLocation() == -1) { // TODO: ESTA FUNCIONALIDAD YA NO ESTA EN USO
                        ArrayList<LocationModel> locationModels = SqliteClass.getInstance(context).databasehelp.locationSql.getLocationById(checkListModels.get(a).id_location_sql);
                        if (locationModels.size() > 0) {
                            postLocation = api.postLocation(sharedPref.getString("token", ""),
                                    new CreateLocationModel(
                                            locationModels.get(0).getName(),
                                            locationModels.get(0).getLatitude(),
                                            locationModels.get(0).getLongitude(),
                                            locationModels.get(0).getAltitude(),
                                            String.valueOf(locationModels.get(0).getPk_operation())
                                    ));
                        }
                    }
                    try {
                        if (checkListModels.get(a).getLocation() == -1) {
                            Response<GetLocationCreateModel> responseType = null;
                            if (postLocation != null) {
                                responseType = postLocation.execute();
                            }
                            GetLocationCreateModel apiResponseType = null;
                            if (responseType != null) {
                                apiResponseType = responseType.body();
                            }
                            if (apiResponseType != null) {
                                SqliteClass.getInstance(context).databasehelp.checkListSql.updateValueCheckListById(
                                        SqliteClass.KEY_CHECLOC,
                                        String.valueOf(apiResponseType.getId()),
                                        String.valueOf(checkListModels.get(a).getIdSql()));
                                checkListModels.get(a).setLocation(apiResponseType.getId());
                            }
                        }
                        Response<CheckListModel> responseTranport = api.postCheckList(sharedPref.getString("token", ""), checkListModels.get(a)).execute();
                        if (responseTranport.isSuccessful()) {
                            newCheckList = responseTranport.body();
                            if (newCheckList != null) {
                                SqliteClass.getInstance(context).databasehelp.checkListSql
                                        .updateValueCheckListById(SqliteClass.KEY_CHECNUM,
                                                newCheckList.getNumber(), String.valueOf(checkListModels.get(0).getIdSql()));
                            }
                            SqliteClass.getInstance(context).databasehelp.checkListSql
                                    .updateValueCheckListById(SqliteClass.KEY_CHECTYP, "cloud",
                                            String.valueOf(checkListModels.get(0).getIdSql()));
                            SqliteClass.getInstance(context).databasehelp.checkListSql
                                    .updateValueCheckListById(SqliteClass.KEY_CHECENV, "SI",
                                            sharedPrefChecK.getString("id_check_list_sql", ""));
                            if (newCheckList != null) {
                                SqliteClass.getInstance(context).databasehelp.checkListSql.updateValueCheckListById(
                                        SqliteClass.KEY_CHECIDAPI, String.valueOf(newCheckList.getId()), String.valueOf(newCheckList.getIdSql()));
                                editor.putString("id_check_list", String.valueOf(newCheckList.getId()));
                                editor.putString("estado", "create");
                                editor.apply();
                                ArrayList<ItemCheckListModel> itemCheckListModels;
                                itemCheckListModels = SqliteClass.getInstance(context).databasehelp.itemSql.getAllItemsByCheck(sharedPrefChecK.getString("id_check_list_sql", ""));
                                PostItemCheckListModel postItemCheckListModel;
                                for (int i = 0; i < itemCheckListModels.size(); i++) {
                                    postItemCheckListModel = new PostItemCheckListModel(
                                            itemCheckListModels.get(i).getDue_date(),
                                            itemCheckListModels.get(i).getObservation_description(),
                                            itemCheckListModels.get(i).getObservation_lifting(),
                                            itemCheckListModels.get(i).getObservation_lifting_date(),
                                            itemCheckListModels.get(i).getCompliance_image(),
                                            itemCheckListModels.get(i).getNon_compliance_image(),
                                            itemCheckListModels.get(i).getObservation_lifting_image(),
                                            itemCheckListModels.get(i).getState(),
                                            newCheckList.getId(),
                                            itemCheckListModels.get(i).getId()
                                    );
                                    new registerItem(sharedPref.getString("token", "")).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, postItemCheckListModel);
                                }
                            }
                            rpta = true;
                        } else {
                            rpta = false;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        rpta = false;
                    }
                } else {
                    rpta = true;
                }
            }
            if (rpta)
                return "ok";
            else
                return "";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            runnnig = true;
            dialog = ProgressDialog.show(context, "Sincronizando información", "Enviando CheckLists, espere en esta pantalla por favor", true);
        }

        @Override
        protected void onPostExecute(String s) {
            runnnig = false;
            dialog.dismiss();
            if (!s.equals("")) {
                Toast.makeText(context, "CheckList enviados", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "No pudimos enviar toda la informacion, revise su conexion a internet", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(s);
        }
    }

    public static class registerItem extends AsyncTask<PostItemCheckListModel, Void, String> {
        private final WeakReference<String> token;

        registerItem(String token_) {
            token = new WeakReference<>(token_);
        }

        @Override
        protected String doInBackground(PostItemCheckListModel... postItemCheckListModels) {
            String token_ = token.get();
            Call<PostItemCheckListModel> postCheckListItem = api.postItemCheckList(
                    token_, postItemCheckListModels[0]);
            try {
                Response<PostItemCheckListModel> response = postCheckListItem.execute();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    SqliteClass.getInstance(context).databasehelp.itemSql.updateValueResponseItemById(
                            SqliteClass.KEY_ITEMIDAPI,
                            String.valueOf(response.body().id),
                            SqliteClass.KEY_ITEMCHELIS,
                            String.valueOf(response.body().getCheck_list()),
                            String.valueOf(postItemCheckListModels[0].id_sql));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "UP-CHECKLIST";
            String description = "Actualización de aplicativo";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            Intent intentAction = new Intent(MainActivity.this, ConfirmUpdateVersionActivity.class);
            intentAction.putExtra("action", YES_ACTION);
            pendingIntent = PendingIntent.getActivity(this, 13, intentAction, 0);
        }
    }

    private void createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_check_list)
                .setContentTitle("UP-CHECKLIST")
                .setContentText("Hay una nueva version del aplicativo ...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Hay una nueva version del aplicativo disponible, presiona la opcion de descargar nueva versión en la parte" +
                                " superior del menú"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.ic_baseline_sync_24, "ACTUALIZAR", pendingIntent).
                setOngoing(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
        notificationManager.notify(13, notification);
    }


    @SuppressLint("StaticFieldLeak")
    class getAPKCurrent extends AsyncTask<Boolean, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            if (s == null) {
                Toast.makeText(context, "No hay versiones mas actuales", Toast.LENGTH_LONG).show();
                return;
            }
            if (s.equals("CHECK")) {
                createNotification();
                return;
            }
            if (s.equals("NO-NOTIFY")) {
                return;
            }
            returnLogin();
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Boolean... params) {
            try {
                Response<List<APKVersionControl>> getAPKListItems =
                        api.getAPKChecklist(token, true).execute();
                if (!getAPKListItems.isSuccessful()) {
                    return null;
                }
                if (getAPKListItems.body() == null) {
                    return null;
                }

                List<APKVersionControl> apkVersionControls = getAPKListItems.body();

                if (apkVersionControls.size() == 0) {
                    return null;
                }

                APKVersionControl obj = apkVersionControls.get(0); //Only one item is active
                String versionApk = obj.getVersion();

                if (versionApk.equals(BuildConfig.VERSION_NAME)) {
                    if (params[0]) {
                        return null;
                    } else {
                        return "NO-NOTIFY";
                    }

                }

                URL_TO_DOWNLOAD = obj.getFile();

                if (params[0]) {
                    return URL_TO_DOWNLOAD;
                } else {
                    return "CHECK";
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    @SuppressLint("StaticFieldLeak")
    class updateTask extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            SqliteClass.getInstance(context).databasehelp.imageSql.deleteImageTable();
            SqliteClass.getInstance(context).databasehelp.operacionSql.deleteOperationTable();
            SqliteClass.getInstance(context).databasehelp.supervisorSql.deleteSupervisorSqlTable();
            SqliteClass.getInstance(context).databasehelp.inspectorSql.deleteInspectoTable();
            SqliteClass.getInstance(context).databasehelp.routeSql.deleteRouteTable();
            SqliteClass.getInstance(context).databasehelp.tranportUnitSql.deleteUnitTable();
            SqliteClass.getInstance(context).databasehelp.driverSql.deleteDriverTable();
            SqliteClass.getInstance(context).databasehelp.locationSql.deleteLocationTable();
            SqliteClass.getInstance(context).databasehelp.itemSql.deleteItemTable();
            SqliteClass.getInstance(context).databasehelp.checkListSql.deleteCheckListTable();
            SqliteClass.getInstance(context).databasehelp.levObsSql.deleteLevObsTable();
            SqliteClass.getInstance(context).databasehelp.carrierSql.deleteCarrierSqlTable();
            dialog = ProgressDialog.show(context, "", "Cargando, espere por favor …", true);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            finish();
            startActivity(intent);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                reloadData = new ReloadData(context, api);
                reloadData.startReloadAll(params[0]);
                while (true) {
                    if (reloadData.isFinish()) {
                        return null;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Error al recargar datos";
            }
        }
    }

}
