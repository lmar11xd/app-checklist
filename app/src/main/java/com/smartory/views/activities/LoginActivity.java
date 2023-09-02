package com.smartory.views.activities;

import static com.smartory.resource.HorizontalValues.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.smartory.BuildConfig;
import com.smartory.R;
import com.smartory.config.ConstValue;
import com.smartory.db.SqliteClass;
import com.smartory.models.Credentials;
import com.smartory.models.CredentialsPinCode;
import com.smartory.network.InterfaceAPI;
import com.smartory.network.ReloadData;
import com.smartory.network.RetrofitClientInstance;
import com.smartory.resource.HorizontalValues;
import com.smartory.utils.ConnectionDetector;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    static TextView txtBranch;
    @SuppressLint("StaticFieldLeak")
    static EditText et_user;
    @SuppressLint("StaticFieldLeak")
    static EditText et_pass;
    @SuppressLint("StaticFieldLeak")
    static EditText et_pin;
    TextView name_version;
    @SuppressLint("StaticFieldLeak")
    static TextView text_pass;
    @SuppressLint("StaticFieldLeak")
    static TextView text_username;
    @SuppressLint("StaticFieldLeak")
    static TextView test_with_other;
    @SuppressLint("StaticFieldLeak")
    static TextView text_pin;
    String pattern = "yyyy-MM-dd'T'HH:mm:ss";
    static SimpleDateFormat simpleDateFormat;
    static ProgressDialog dialog;
    Handler handler = new Handler();
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    @SuppressLint("StaticFieldLeak")
    private static SqliteClass.DatabaseHelper SQL;
    @SuppressLint("StaticFieldLeak")
    public static Activity activity;
    public static String nameFileUpload = "";
    ImageView settings_env;
    ConnectionDetector cd;
    static String token;
    Retrofit retrofit;
    static boolean need_2factor = false;
    static int id_user = -1;
    static InterfaceAPI api;
    static ReloadData reloadData;
    private ImageView showHideBtn;
    boolean flag_show = false;

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
        setContentView(R.layout.activity_login_new);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Objects.requireNonNull(getSupportActionBar()).hide();
        SharedPreferences sharedPref_env = getSharedPreferences("ev_settings", Context.MODE_PRIVATE);
        simpleDateFormat = new SimpleDateFormat(pattern);
        RetrofitClientInstance.setBaseUrl(BASE_URL);
        retrofit = RetrofitClientInstance.getRetrofitInstance();
        SQL = SqliteClass.getInstance(context).databasehelp;
        api = retrofit.create(InterfaceAPI.class);
        text_pass = findViewById(R.id.text_pass);
        text_username = findViewById(R.id.text_username);
        text_pin = findViewById(R.id.text_pin);
        settings_env = findViewById(R.id.settings_env);
        test_with_other = findViewById(R.id.test_with_other);
        cd = new ConnectionDetector(context);
        ConstValue.enablePermission(this, context);
        txtBranch = findViewById(R.id.txtBranch);
        name_version = findViewById(R.id.name_version);
        name_version.setText("VERSIÓN: " + BuildConfig.VERSION_NAME);
        et_user = findViewById(R.id.username);
        et_pass = findViewById(R.id.password);
        et_pin = findViewById(R.id.pincode);
        showHideBtn = findViewById(R.id.showHideBtn);
        txtBranch.setText("");

        showHideBtn.setOnClickListener(view -> {
            if (!flag_show) {
                et_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                showHideBtn.setBackgroundResource(R.drawable.ic_baseline_visibility_24);
            } else {
                et_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                showHideBtn.setBackgroundResource(R.drawable.ic_baseline_visibility_off_24);
            }
            flag_show = !flag_show;
        });
        // OPEN SESSION
        SharedPreferences sharedPref = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        String logueado = sharedPref.getString("logueado", "inactive");
        String oldAppVersion = sharedPref.getString("app_version", "");
        String currentAppVersion = BuildConfig.VERSION_NAME;

        if (currentAppVersion.equals(oldAppVersion) && logueado.equals("active")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
        Button btLogin = findViewById(R.id.bt_login);
        btLogin.setOnClickListener(v -> {
            if (cd.isConnectingToInternet()) {
                if (need_2factor) {
                    if (et_pin.getText().toString().length() != 6) {
                        et_pin.setError("El PIN tiene 6 digitos");
                        return;
                    }
                    new login2AuthTask().execute(true);
                    return;
                }
                if (et_user.getText().toString().length() == 0) {
                    et_user.setError("Ingrese Usuario");
                    return;
                }
                if (et_pass.getText().toString().length() == 0) {
                    et_pass.setError("Ingrese Contraseña");
                    return;
                }
                setUrlApi(et_user.getText().toString());
                new loginTask().execute(true);
            } else {
                Toast.makeText(context, "El Equipo no tiene conexion a internet", Toast.LENGTH_LONG).show();
            }
        });
        test_with_other.setOnClickListener(v -> {
            need_2factor = false;
            et_pin.setVisibility(View.GONE);
            text_pin.setVisibility(View.GONE);
            text_pass.setVisibility(View.VISIBLE);
            text_username.setVisibility(View.VISIBLE);
            et_user.setVisibility(View.VISIBLE);
            et_pass.setVisibility(View.VISIBLE);
            showHideBtn.setVisibility(View.VISIBLE);
            test_with_other.setVisibility(View.GONE);
        });
        settings_env.setOnClickListener(view -> {
            AlertDialog alertDialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view_modal = inflater.inflate(R.layout.dialog_setting_env, null);
            Button btnFire = view_modal.findViewById(R.id.btn_ok);
            Button btnCancel = view_modal.findViewById(R.id.btn_cancel);
            EditText et_obs = view_modal.findViewById(R.id.et_env);
            SharedPreferences sharedPref1 = getSharedPreferences("ev_settings", Context.MODE_PRIVATE);
            String url = sharedPref1.getString("baseurl", BASE_URL);
            et_obs.setText(url);
            builder.setView(view_modal);
            alertDialog = builder.create();
            btnFire.setOnClickListener(view1 -> {
                SharedPreferences.Editor editor_env = sharedPref_env.edit();
                editor_env.putString("baseurl", et_obs.getText().toString());
                editor_env.apply();
                BASE_URL = et_obs.getText().toString();
                RetrofitClientInstance.setBaseUrl(BASE_URL);
                switch (BASE_URL.substring(8, 11)) {
                    case "dev":
                        txtBranch.setText("DEVELOPMENT");
                        break;
                    case "stg":
                        txtBranch.setText("STAYING");
                        break;
                    default:
                        txtBranch.setText("");
                        break;
                }
                alertDialog.dismiss();
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            });
            btnCancel.setOnClickListener(view12 -> alertDialog.dismiss());
            alertDialog.show();
        });
        try {
            String URL_TO_DOWNLOAD = getIntent().getStringExtra("URL_TO_DOWNLOAD");
            executeDownload(URL_TO_DOWNLOAD);
        } catch (Exception ignored) {
        }
    }

    public void setUrlApi(String username) {
        String prefix = username.split("\\.")[0];
        switch (prefix) {
            case "sandbox":
                HorizontalValues.BASE_URL = HorizontalValues.BASE_URL_SANDBOX;
                txtBranch.setText("SANDBOX");
                break;
            case "dev":
                HorizontalValues.BASE_URL = HorizontalValues.BASE_URL_DEV;
                txtBranch.setText("DEVELOPMENT");
                break;
            case "qa":
                HorizontalValues.BASE_URL = HorizontalValues.BASE_URL_QA;
                txtBranch.setText("TESTING");
                break;
            default:
                if (HorizontalValues.IS_PRODUCTION) {
                    HorizontalValues.BASE_URL = HorizontalValues.BASE_URL_PROD;
                    txtBranch.setText("");
                }
                break;
        }
        RetrofitClientInstance.setBaseUrl(HorizontalValues.BASE_URL);
        et_user.setText(username.replace(prefix + ".", ""));
    }

    private static void nextActivity() {
        SharedPreferences sharedPref = activity.getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("logueado", "active");
        editor.apply();
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
        activity.finish();
    }

    @SuppressLint("StaticFieldLeak")
    class login2AuthTask extends AsyncTask<Boolean, Void, String> {
        String pin;

        @Override
        protected void onPreExecute() {
            SQL.onUpgrade(SQL.getReadableDatabase(), 1, 2);
            RetrofitClientInstance.setBaseUrl(BASE_URL);
            pin = et_pin.getText().toString();
            if (!activity.isFinishing()) {
                dialog = ProgressDialog.show(activity, "", "Cargando..., espere un momento por favor", true);
            }
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("ok")) {
                Toast.makeText(context, "Inicio de sesion exitoso", Toast.LENGTH_LONG).show();
                SharedPreferences sharedPref = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("logueado", "active");
                editor.putString("app_version", BuildConfig.VERSION_NAME);
                editor.apply();
                dialog.dismiss();
                nextActivity();
            } else if (s.equals("nouser")) {
                Toast.makeText(context, "Este Usuario no tiene permiso para acceder", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            } else {
                Toast.makeText(context, "Problemas para acceder al servidor, revise sus credenciales", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
            super.onPostExecute(s);
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(Boolean... booleans) {
            String type;
            Call<Credentials> login = api.loginsecondStage(new CredentialsPinCode(pin), id_user);
            SharedPreferences sharedPref = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            try {
                Response<Credentials> response = login.execute();
                Credentials apiResponse = response.body();
                if (apiResponse == null) {
                    return "";
                } else {
                    if (apiResponse.user_permissions == null) {
                        return "nouser";
                    }
                    if (!apiResponse.user_permissions.contains("authentication.checklist-enter_checklist_app")) {
                        return "nouser";
                    }
                    token = "token " + apiResponse.getToken();
                    editor.putString("token", token);
                    editor.putBoolean("is_staff", apiResponse.getUser().is_staff);
                    editor.putBoolean("can_create_checklist", apiResponse.user_permissions.contains("authentication.checklist-create_checklist"));
                    editor.putBoolean("can_validate_checklist", apiResponse.user_permissions.contains("authentication.checklist-validate_checklist"));
                    editor.putBoolean("can_edit_checklist", apiResponse.user_permissions.contains("authentication.checklist-edit_checklist"));
                    editor.putBoolean("can_edit_validated_checklist", apiResponse.user_permissions.contains("authentication.checklist-edit_validated_checklist"));
                    editor.putBoolean("can_extend_noncompliance_checklist", apiResponse.user_permissions.contains("authentication.checklist-raise_obs_noncompliance"));
                    editor.putBoolean("can_accept_observation_lifting", apiResponse.user_permissions.contains("authentication.checklist-accept_observation_lifting"));
                    editor.putBoolean("is_staff", apiResponse.getUser().is_staff);
                    editor.putBoolean("is_staff", apiResponse.getUser().is_staff);
                    editor.putBoolean("is_superuser", apiResponse.getUser().is_superuser);
                    editor.putString("employee_name", apiResponse.getUser().employee_name);
                    editor.putInt("company_id", apiResponse.getUser().getCompany().id);
                    editor.putString("company_name", apiResponse.getUser().getCompany().name);
                    editor.putString("company_type", apiResponse.getUser().getCompany().type);
                    editor.putBoolean("is_group_supervisor", apiResponse.user_group.contains("supervisor"));
                    editor.putBoolean("is_group_inspector", apiResponse.user_group.contains("inspector"));
                    editor.putBoolean("is_group_sub_employee", apiResponse.user_group.contains("sub_employee"));
                    editor.putBoolean("is_group_staff", apiResponse.user_group.contains("staff"));
                    editor.putBoolean("is_group_sub_staff", apiResponse.user_group.contains("sub_staff"));
                    editor.putBoolean("is_group_driver", apiResponse.user_group.contains("driver"));
                    editor.putString("datetime_sync", simpleDateFormat.format(new Date()));
                    editor.apply();
                }
                Call<Credentials> getTypeUser = api.getType(token);
                Response<Credentials> responseType = getTypeUser.execute();
                Credentials apiResponseType = responseType.body();
                if (apiResponseType != null) {
                    if (apiResponseType.getType() == null) {
                        return "nouser";
                    } else {
                        type = apiResponseType.getType();
                        editor.putString("type_user", type);
                        editor.apply();
                    }
                } else {
                    return "nouser";
                }
                reloadData = new ReloadData(context, api);
                reloadData.startReloadAll(null);
                while (true) {
                    if (reloadData.isFinish()) {
                        return "ok";
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "error" + e;
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    class loginTask extends AsyncTask<Boolean, Void, String> {
        String username, password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            RetrofitClientInstance.setBaseUrl(BASE_URL);
            username = et_user.getText().toString();
            password = et_pass.getText().toString();
            SQL.onUpgrade(SQL.getReadableDatabase(), 1, 2);
            if (!activity.isFinishing()) {
                dialog = ProgressDialog.show(activity, "", "Cargando..., espere un momento por favor", true);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            switch (s) {
                case "ok":
                    Toast.makeText(context, "Inicio de sesion exitoso", Toast.LENGTH_LONG).show();
                    SharedPreferences sharedPref = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("logueado", "active");
                    editor.putString("app_version", BuildConfig.VERSION_NAME);
                    editor.apply();
                    nextActivity();
                    break;
                case "2factor":
                    et_pin.setVisibility(View.VISIBLE);
                    text_pin.setVisibility(View.VISIBLE);
                    text_pass.setVisibility(View.GONE);
                    text_username.setVisibility(View.GONE);
                    et_user.setVisibility(View.GONE);
                    et_pass.setVisibility(View.GONE);
                    showHideBtn.setVisibility(View.GONE);
                    test_with_other.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                    break;
                case "nouser":
                    Toast.makeText(context, "Este usuario no tiene permiso para acceder", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    break;
                case "406":
                    Toast.makeText(context, "App con versión desactualizada.", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    break;
                case "400":
                    Toast.makeText(context, "Usuario y/o contraseña incorrectos.", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    break;
                default:
                    dialog.dismiss();
                    break;
            }
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(Boolean... booleans) {
            String type;
            String app_version = BuildConfig.VERSION_NAME;
            Call<Credentials> login = api.loginfirstStage(new Credentials(username, password, app_version));
            SharedPreferences sharedPref = activity.getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            try {
                Response<Credentials> response = login.execute();
                int responseCode = response.code();
                if (responseCode == 406) {
                    return "" + responseCode;
                } else if (responseCode == 400) {
                    return "" + responseCode;
                }

                Credentials apiResponse = response.body();
                if (apiResponse == null) {
                    return "";
                } else {
                    if (apiResponse.is_tfa_active) {
                        id_user = apiResponse.user_id;
                        need_2factor = true;
                        return "2factor";
                    }
                    if (apiResponse.user_permissions == null) {
                        return "nouser";
                    }
                    if (!apiResponse.user_permissions.contains("authentication.checklist-enter_checklist_app")) {
                        return "nouser";
                    }
                    token = "token " + apiResponse.getToken();
                    editor.putString("token", token);
                    editor.putBoolean("is_staff", apiResponse.getUser().is_staff);
                    editor.putBoolean("can_create_checklist", apiResponse.user_permissions.contains("authentication.checklist-create_checklist"));
                    editor.putBoolean("can_validate_checklist", apiResponse.user_permissions.contains("authentication.checklist-validate_checklist"));
                    editor.putBoolean("can_edit_checklist", apiResponse.user_permissions.contains("authentication.checklist-edit_checklist"));
                    editor.putBoolean("can_edit_validated_checklist", apiResponse.user_permissions.contains("authentication.checklist-edit_validated_checklist"));
                    editor.putBoolean("can_extend_noncompliance_checklist", apiResponse.user_permissions.contains("authentication.checklist-raise_obs_noncompliance"));
                    editor.putBoolean("can_accept_observation_lifting", apiResponse.user_permissions.contains("authentication.checklist-accept_observation_lifting"));
                    editor.putBoolean("is_superuser", apiResponse.getUser().is_superuser);
                    editor.putString("employee_name", apiResponse.getUser().employee_name);
                    editor.putInt("company_id", apiResponse.getUser().getCompany().id);
                    editor.putString("company_name", apiResponse.getUser().getCompany().name);
                    editor.putString("company_type", apiResponse.getUser().getCompany().type);
                    editor.putBoolean("is_group_supervisor", apiResponse.user_group.contains("supervisor"));
                    editor.putBoolean("is_group_inspector", apiResponse.user_group.contains("inspector"));
                    editor.putBoolean("is_group_sub_employee", apiResponse.user_group.contains("sub_employee"));
                    editor.putBoolean("is_group_staff", apiResponse.user_group.contains("staff"));
                    editor.putBoolean("is_group_sub_staff", apiResponse.user_group.contains("sub_staff"));
                    editor.putBoolean("is_group_driver", apiResponse.user_group.contains("driver"));
                    editor.putString("datetime_sync", simpleDateFormat.format(new Date()));
                    editor.apply();
                }
                Call<Credentials> getTypeUser = api.getType(token);
                Response<Credentials> responseType = getTypeUser.execute();
                Credentials apiResponseType = responseType.body();
                if (apiResponseType != null) {
                    if (apiResponseType.getType() == null) {
                        return "nouser";
                    } else {
                        type = apiResponseType.getType();
                        editor.putString("type_user", type);
                        editor.apply();
                    }
                } else {
                    return "nouser";
                }
                reloadData = new ReloadData(context, api);
                reloadData.startReloadAll(null);
                while (true) {
                    if (reloadData.isFinish()) {
                        return "ok";
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "error" + e;
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private void executeDownload(String URL_TO_DOWNLOAD) {
        registerReceiver(new DonwloadCompleteReceiver(), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(URL_TO_DOWNLOAD));
        request.setDescription("Descargando archivo");
        request.setTitle("Descargando");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        nameFileUpload = "checklist_" + new Date() + ".apk";
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nameFileUpload);
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    public class DonwloadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                File mFile;
                mFile = new File(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + nameFileUpload);
                if (mFile.exists()) {
                    Toast.makeText(context, "Descarga completa, instale el apk e inicie sesión nuevamente", Toast.LENGTH_LONG).show();
                    Uri fileUri = Uri.fromFile(mFile);
                    if (Build.VERSION.SDK_INT >= 24) {
                        fileUri = FileProvider.getUriForFile(LoginActivity.this, BuildConfig.APPLICATION_ID + ".provider", mFile);
                    }
                    Intent mIntent = new Intent(Intent.ACTION_VIEW, fileUri);
                    mIntent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                    mIntent.setDataAndType(fileUri, "application/vnd.android.package-archive");
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    mIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(mIntent);
                }
            }
        }
    }
}