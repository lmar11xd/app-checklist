package com.smartory.views.activities;

import static com.smartory.db.SqliteClass.KEY_IMAORDNUM;
import static com.smartory.resource.HorizontalValues.LABEL_CHECK_IMAGE_ITEM;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gis.drawingview.DrawingView;
import com.smartory.BuildConfig;
import com.smartory.R;
import com.smartory.config.ConstValue;
import com.smartory.db.SqliteClass;
import com.smartory.models.CheckListModel;
import com.smartory.models.CreateLocationModel;
import com.smartory.models.GetLocationCreateModel;
import com.smartory.models.GetPatchValidateModel;
import com.smartory.models.ImageModel;
import com.smartory.models.ItemCheckListModel;
import com.smartory.models.LocationModel;
import com.smartory.models.PatchItemCheckListModel;
import com.smartory.models.PatchValidarModel;
import com.smartory.models.PostItemCheckListModel;
import com.smartory.models.PostItemImageCheckList;
import com.smartory.models.ResponseImageItemChecklist;
import com.smartory.utils.ConnectionDetector;
import com.smartory.network.InterfaceAPI;
import com.smartory.network.RetrofitClientInstance;
import com.smartory.utils.Util;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.sentry.Sentry;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CheckListValidationActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    static Context context;
    DrawingView dv_smcv;
    Button btClearSmcv, btValide;
    String imageFileName = "";
    static String mCurrentPhotoPath = "";
    LinearLayout btn_back;
    public static ProgressDialog dialog;
    ConnectionDetector cd;
    static Response<CheckListModel> responseTransport;
    static Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
    static final InterfaceAPI api = retrofit.create(InterfaceAPI.class);
    static ContentResolver contentResolver;
    static final Executor THREAD_POOL_EXECUTOR =
            new ThreadPoolExecutor(50, Integer.MAX_VALUE, 1,
                    TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list_validation);
        context = this;
        contentResolver = this.getContentResolver();
        cd = new ConnectionDetector(context);
        Objects.requireNonNull(getSupportActionBar()).setTitle("CheckList");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dv_smcv = findViewById(R.id.drawing_view_smcv);
        btClearSmcv = findViewById(R.id.btn_clear_smcv);
        btn_back = findViewById(R.id.btn_back);
        btClearSmcv.setOnClickListener(v -> dv_smcv.undo());
        final SharedPreferences sharedPrefChec = getSharedPreferences("check_list", Context.MODE_PRIVATE);
        final CheckListModel checkListModel = SqliteClass.getInstance(context).databasehelp.checkListSql.getCheck(sharedPrefChec.getString("id_check_list_sql", ""));

        btValide = findViewById(R.id.btn_send);
        if (sharedPrefChec.getString("opcion", "").equals(ConstValue.VALIDATE)) {
            btValide.setText("VALIDAR CHECKLIST");
        } else if (sharedPrefChec.getString("opcion", "").equals("")) {
            btValide.setText("REGISTRAR CHECKLIST");
        } else if (sharedPrefChec.getString("opcion", "").equals(ConstValue.EDIT)) {
            btValide.setText("REGISTRAR CHECKLIST");
        }

        btn_back.setOnClickListener(v -> {
            if (!sharedPrefChec.getString("opcion", "").equals(ConstValue.VALIDATE)) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
            finish();
        });
        btValide.setOnClickListener(v -> {
            String finalMessg = "";
            if (sharedPrefChec.getString("opcion", "").equals("")) {
                finalMessg = "Esta apunto de registrar un CheckList, si cuenta con internet se registrara inmediatamente ¿Esta seguro de realizar esta acción?";
            } else if (sharedPrefChec.getString("opcion", "").equals(ConstValue.EDIT)) {
                finalMessg = "Esta apunto de registrar el Check List " + sharedPrefChec.getString("number_check_list", "") + " . ¿Esta seguro de realizar esta acción?";
            } else if (!checkListModel.getType().equals("local") && sharedPrefChec.getString("opcion", "").equals(ConstValue.VALIDATE)) {
                finalMessg = "Esta apunto de validar el CheckList " + sharedPrefChec.getString("number_check_list", "") + " . ¿Esta seguro de realizar esta acción?";
            } else if (checkListModel.getType().equals("local") && sharedPrefChec.getString("opcion", "").equals(ConstValue.VALIDATE)) {
                finalMessg = "Esta apunto de validar el CheckList, si cuenta con internet se registrara inmediatamente ¿Esta seguro de realizar esta acción?";
            }
            new MaterialAlertDialogBuilder(context)
                    .setTitle("Check List")
                    .setMessage(finalMessg)
                    .setNegativeButton("Cancelar", null)
                    .setPositiveButton("Ok", (dialog, which) -> {
                        String signatureIsEmpty = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAMCAgMCAgMDAwMEAwMEBQgFBQQEBQoHBwYIDAoMDAsK\nCwsNDhIQDQ4RDgsLEBYQERMUFRUVDA8XGBYUGBIUFRT/2wBDAQMEBAUEBQkFBQkUDQsNFBQUFBQU\nFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBT/wAARCAGQAooDASIA\nAhEBAxEB/8QAFQABAQAAAAAAAAAAAAAAAAAAAAn/xAAUEAEAAAAAAAAAAAAAAAAAAAAA/8QAFAEB\nAAAAAAAAAAAAAAAAAAAAAP/EABQRAQAAAAAAAAAAAAAAAAAAAAD/2gAMAwEAAhEDEQA/AKpgAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//Z\n";
                        try {
                            mCurrentPhotoPath = "";
                            saveEditedImage(dv_smcv.getBitmap());
                            if (sharedPrefChec.getString("opcion", "").equals(ConstValue.VALIDATE)) {
                                checkListModel.setSupervisor_signature(Util.getFileToByte(mCurrentPhotoPath));
                            } else if (sharedPrefChec.getString("opcion", "").equals("")) {
                                checkListModel.setDriver_signature(Util.getFileToByte(mCurrentPhotoPath));
                            }
                            if (cd.isConnectingToInternet()) {
                                if (checkListModel.getType() != null && checkListModel.getType().equals("local") && sharedPrefChec.getString("opcion", "").equals("")) {
                                    if (!Util.getFileToByte(mCurrentPhotoPath).equals(signatureIsEmpty)) {
                                        System.out.println("CREATE");
                                        new createCheckListTask(this).execute(checkListModel);
                                    } else {
                                        Toast.makeText(context, "Coloca tu firma digital para poder registrar este CheckList!", Toast.LENGTH_SHORT).show();
                                    }
                                } else if (sharedPrefChec.getString("opcion", "").equals(ConstValue.EDIT)) {
                                    if (checkListModel.getType().equals("local")) {
                                        if (!Util.getFileToByte(mCurrentPhotoPath).equals(signatureIsEmpty)) {
                                            new createCheckListTask(this).execute(checkListModel);
                                        } else {
                                            Toast.makeText(context, "Coloca tu firma digital para poder registrar este CheckList!", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        new putItemTask().execute(checkListModel);
                                    }
                                } else if (sharedPrefChec.getString("opcion", "").equals(ConstValue.VALIDATE)) {
                                    if (!Util.getFileToByte(mCurrentPhotoPath).equals(signatureIsEmpty)) {
                                        Toast.makeText(context, "Iniciando Validación!", Toast.LENGTH_SHORT).show();
                                        new validateCheckListTask(this).execute(checkListModel);
                                    } else {
                                        Toast.makeText(context, "Coloca tu firma digital para poder validar este CheckList!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                if (sharedPrefChec.getString("opcion", "").equals(ConstValue.VALIDATE)) {
                                    if (checkListModel.getType().equals("local")) {
                                        SqliteClass.getInstance(context).databasehelp.checkListSql.updateValueCheckListById(
                                                SqliteClass.KEY_CHECSTA, "review", sharedPrefChec.getString("id_check_list_sql", ""));
                                        SqliteClass.getInstance(context).databasehelp.checkListSql.updateValueCheckListById(
                                                SqliteClass.KEY_CHECENV, "NO", sharedPrefChec.getString("id_check_list_sql", ""));
                                        Toast.makeText(getApplicationContext(), "CheckList - Su dispositivo no cuenta con conexión a internet. \n Los datos seran guardados para ser enviados una ves el equipo tenga conexión", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(context, MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Para validar un checklist ya registrado, se necesita internet", Toast.LENGTH_LONG).show();
                                    }
                                } else { // No internet
                                    if (checkListModel.getType().equals("local")) {
                                        if (sharedPrefChec.getString("opcion", "").equals(ConstValue.EDIT)) {
                                            Toast.makeText(getApplicationContext(), "CheckList editado con exito", Toast.LENGTH_LONG).show();
                                            Intent i = new Intent(context, MainActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            startActivity(i);
                                        } else {
                                            SqliteClass.getInstance(context).databasehelp.checkListSql.updateValueCheckListById(
                                                    SqliteClass.KEY_CHECENV, "NO", sharedPrefChec.getString("id_check_list_sql", ""));
                                            Toast.makeText(getApplicationContext(), "CheckList - Su dispositivo no cuenta con conexión a internet. \n Los datos seran guardados para ser enviados una ves el equipo tenga conexión", Toast.LENGTH_LONG).show();
                                            Intent i = new Intent(context, MainActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            startActivity(i);
                                        }
                                        finish();
                                    } else {
                                        SqliteClass.getInstance(context).databasehelp.checkListSql.updateValueCheckListById(
                                                SqliteClass.KEY_CHECENV, "NO-EDIT", sharedPrefChec.getString("id_check_list_sql", ""));
                                        Toast.makeText(getApplicationContext(), "CheckList - Su dispositivo no cuenta con conexión a internet. \n Los datos seran guardados para ser enviados una ves el equipo tenga conexión", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(context, MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(i);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).show();
        });
    }

    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void saveEditedImage(Bitmap bitmap) {
        File file;
        try {
            file = createImageFile();
            OutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream); // Uri to OutputStream
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Bitmap getBitmapFromUri(String url_path) {
        Bitmap imageBitmap;
        try {
            imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(url_path));
            int origWidth = Objects.requireNonNull(imageBitmap).getWidth();
            int origHeight = imageBitmap.getHeight();
            int targetWidth = 400;
            int targetHeight = 600;
            if (origWidth <= origHeight) {
                if (origWidth > targetWidth) {
                    int destHeight = origHeight / (origWidth / targetWidth);
                    Bitmap b2 = Bitmap.createScaledBitmap(imageBitmap, targetWidth, destHeight, false);
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    b2.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
                    return b2;
                } else {
                    return imageBitmap;
                }
            } else {
                if (origHeight > targetHeight) {
                    int destWidth = origWidth / (origHeight / targetHeight);
                    Bitmap b2 = Bitmap.createScaledBitmap(imageBitmap, destWidth, targetHeight, false);
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    b2.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
                    return b2;
                } else {
                    return imageBitmap;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static public class createCheckListTask extends AsyncTask<CheckListModel, Void, String> {

        private final WeakReference<CheckListValidationActivity> activityReference;

        createCheckListTask(CheckListValidationActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(context, "", "Registrando CheckList", true);
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(CheckListModel... checkListModels) {
            SharedPreferences sharedPref = context.getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
            SharedPreferences sharedPrefCheck = context.getSharedPreferences("check_list", Context.MODE_PRIVATE);
            SharedPreferences.Editor editorPrefCheck = sharedPrefCheck.edit();
            editorPrefCheck.apply();
            if (checkListModels[0].getDatetime() == null || checkListModels[0].getDatetime().equals("")) {
                checkListModels[0].setDatetime(Util.getCurrentDate());
            }
            CheckListModel newCheckList;
            checkListModels[0].location = null;
            Call<CheckListModel> postCheckListHead = api.postCheckList(sharedPref.getString("token", ""), checkListModels[0]);
            try {
                responseTransport = postCheckListHead.execute();
                if (responseTransport.isSuccessful()) {
                    newCheckList = responseTransport.body();
                    assert newCheckList != null;
                    String idSql = String.valueOf(checkListModels[0].getIdSql());
                    SqliteClass.getInstance(context).databasehelp.checkListSql.updateValueCheckListById(
                            SqliteClass.KEY_CHECNUM, newCheckList.getNumber(), idSql);
                    SqliteClass.getInstance(context).databasehelp.checkListSql.updateValueCheckListById(
                            SqliteClass.KEY_CHECIDAPI, String.valueOf(newCheckList.getId()), idSql);
                    SqliteClass.getInstance(context).databasehelp.checkListSql.updateValueCheckListById(
                            SqliteClass.KEY_CHECTYP, "cloud", idSql);
                    SqliteClass.getInstance(context).databasehelp.checkListSql.updateValueCheckListById(
                            SqliteClass.KEY_CHECDAT, newCheckList.getDatetime().replace("-05:00", ""), idSql);
                    SqliteClass.getInstance(context).databasehelp.checkListSql.updateValueCheckListById(
                            SqliteClass.KEY_CHECPROGRESS, String.valueOf(newCheckList.getProgress()), idSql);
                    checkListModels[0].setType("cloud");
                    ArrayList<ItemCheckListModel> itemCheckListModels =
                            SqliteClass.getInstance(context).databasehelp.itemSql.getAllItemsByCheck(
                                    sharedPrefCheck.getString("id_check_list_sql", ""));
                    PostItemCheckListModel postItemCheckListModel;
                    for (int i = 0; i < itemCheckListModels.size(); i++) {
                        postItemCheckListModel = new PostItemCheckListModel(
                                itemCheckListModels.get(i).getDue_date(),
                                itemCheckListModels.get(i).getObservation_description(),
                                itemCheckListModels.get(i).getObservation_lifting(),
                                itemCheckListModels.get(i).getObservation_lifting_date(),
                                "",
                                "",
                                itemCheckListModels.get(i).getObservation_lifting_image(),
                                itemCheckListModels.get(i).getState(),
                                newCheckList.getId(),
                                itemCheckListModels.get(i).getId()
                        );
                        postItemCheckListModel.id_sql = itemCheckListModels.get(i).getIdSql();
                        new registerItem().executeOnExecutor(THREAD_POOL_EXECUTOR, postItemCheckListModel);
                    }
                    SqliteClass.getInstance(context).databasehelp.checkListSql.updateValueCheckListById(
                            SqliteClass.KEY_CHECENV, "SI", String.valueOf(checkListModels[0].getIdSql()));
                    return "ok";
                } else {
                    System.out.println(responseTransport.errorBody().string());
                    Sentry.captureMessage("Error, " + responseTransport.errorBody().string());
                    return "";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if (!s.equals("")) {
                Toast.makeText(context, "CheckLis creado con exito", Toast.LENGTH_LONG).show();
                CheckListValidationActivity activity = activityReference.get();
                if (activity == null || activity.isFinishing()) {
                    return;
                } else {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(intent);
                    activity.finish();
                }
            } else {
                Toast.makeText(context, "CheckLis creado, guardado localmente", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(s);
        }
    }

    static public class validateCheckListTask extends AsyncTask<CheckListModel, Void, String> {

        private final WeakReference<CheckListValidationActivity> activityReference;

        validateCheckListTask(CheckListValidationActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(context, "", "Validando CheckList", true);
            super.onPreExecute();
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(CheckListModel... checkListModels) {
            SharedPreferences sharedPref = context.getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
            SharedPreferences sharedPrefChec = context.getSharedPreferences("check_list", Context.MODE_PRIVATE);
            SharedPreferences.Editor cleanEditor = sharedPrefChec.edit();
            String result = "";
            PatchValidarModel patchValidarModel = new PatchValidarModel();
            patchValidarModel.setSupervisor_signature(Util.getFileToByte(mCurrentPhotoPath));
            patchValidarModel.setState("review");
            try {
                if (checkListModels[0].getDatetime() == null || checkListModels[0].getDatetime().equals("")) {
                    checkListModels[0].setDatetime(Util.getCurrentDate());
                }
                if (checkListModels[0].getNumber().equals("Sin Enviar")) {
                    Call<CheckListModel> postCheckListHead = api.postCheckList(sharedPref.getString("token", ""), checkListModels[0]);
                    try {
                        Response<CheckListModel> responseTransport = postCheckListHead.execute();
                        if (responseTransport.isSuccessful()) {
                            String idSql = String.valueOf(checkListModels[0].getIdSql());
                            result = Objects.requireNonNull(responseTransport.body()).getNumber();
                            SqliteClass.getInstance(context).databasehelp.checkListSql.updateValueCheckListById(SqliteClass.KEY_CHECNUM, result, idSql);
                            SqliteClass.getInstance(context).databasehelp.checkListSql.updateValueCheckListById(SqliteClass.KEY_CHECTYP, "cloud", idSql);
                            SqliteClass.getInstance(context).databasehelp.checkListSql.updateValueCheckListById(
                                    SqliteClass.KEY_CHECIDAPI, String.valueOf(responseTransport.body().getId()), String.valueOf(checkListModels[0].getIdSql()));
                            SqliteClass.getInstance(context).databasehelp.checkListSql.updateValueCheckListById(
                                    SqliteClass.KEY_CHECPROGRESS, String.valueOf(checkListModels[0].getProgress()), idSql);
                            ArrayList<ItemCheckListModel> itemCheckListModels = SqliteClass.getInstance(context).databasehelp.itemSql.getAllItemsByCheck(checkListModels[0].getIdSql() + "");
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
                                        responseTransport.body().getId(),
                                        itemCheckListModels.get(i).getId()
                                );
                                postItemCheckListModel.id_sql = itemCheckListModels.get(i).getIdSql();
                                new registerItem().executeOnExecutor(THREAD_POOL_EXECUTOR, postItemCheckListModel);
                            }
                            if (api.patchValidate(sharedPref.getString("token", ""),
                                    responseTransport.body().getId(),
                                    patchValidarModel).execute().isSuccessful()) {
                                cleanEditor.putString("opcion", "");
                                cleanEditor.apply();
                            } else {
                                result = "";
                            }
                        } else {
                            result = "";
                        }
                    } catch (IOException e) {
                        result = "";
                        e.printStackTrace();
                    }
                } else { // EDIT
                    patchValidarModel.supervisor_signature = "FIRMA";
                    Call<GetPatchValidateModel> callPtach = api.patchValidate(sharedPref.getString("token", ""),
                            checkListModels[0].getId(),
                            patchValidarModel);
                    Response<GetPatchValidateModel> response = callPtach.execute();
                    if (response.isSuccessful()) {
                        SqliteClass.getInstance(context).databasehelp.checkListSql.updateValueCheckListById(
                                SqliteClass.KEY_CHECSTA, "review", sharedPrefChec.getString("id_check_list_sql", ""));
                        cleanEditor.putString("opcion", "");
                        cleanEditor.apply();
                        result = "ok-edit";
                    }
                }
            } catch (Exception e) {
                result = "";
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.equals("")) { //success
                if (s.equals("ok-edit")) {
                    Toast.makeText(context, "Check List Validado con éxito", Toast.LENGTH_LONG).show();
                    CheckListValidationActivity activity = activityReference.get();
                    if (activity == null || activity.isFinishing()) {
                        return;
                    } else {
                        activity.finish();
                    }
                } else {
                    Toast.makeText(context, "Check List Validado con éxito", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(context, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(i);
                }
            } else { //error
                Toast.makeText(context, "Error al validar Check List", Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        }
    }

    public static class putItemTask extends AsyncTask<CheckListModel, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(context, "", "Editando CheckList", true);
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(CheckListModel... checkListModels) {
            String result = "ok";
            SharedPreferences sharedPref = context.getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
            SharedPreferences sharedPrefCheck = context.getSharedPreferences("check_list", Context.MODE_PRIVATE);
            PatchItemCheckListModel checkListModel;
            ArrayList<ItemCheckListModel> itemCheckListModels = SqliteClass.getInstance(context).databasehelp.itemSql.getAllItemsByCheckEdit(sharedPrefCheck.getString("id_check_list_sql", ""));
            for (int i = 0; itemCheckListModels.size() > i; i++) {
                checkListModel = new PatchItemCheckListModel(
                        itemCheckListModels.get(i).getObservation_description(),
                        itemCheckListModels.get(i).getObservation_lifting(),
                        "",
                        "",
                        itemCheckListModels.get(i).getObservation_lifting_image(),
                        itemCheckListModels.get(i).getState(),
                        itemCheckListModels.get(i).getReviewed()
                );
                Response<PostItemCheckListModel> putItem;
                try {
                    putItem = api.patchItem(
                            sharedPref.getString("token", ""),
                            String.valueOf(itemCheckListModels.get(i).getId()),
                            checkListModel).execute();
                    if (!putItem.isSuccessful()) {
                        result = "";
                    } else {
                        ArrayList<ImageModel> imageModels = SqliteClass.getInstance(context).databasehelp.imageSql.getImagesByField(String.valueOf(itemCheckListModels.get(i).getIdSql()), KEY_IMAORDNUM);
                        for (int j = 0; j < imageModels.size(); j++) {
                            if (!imageModels.get(j).getState().equals("server") && !imageModels.get(j).getState().equals("serverAndLocal")
                                    && (imageModels.get(j).getLabel().equals(LABEL_CHECK_IMAGE_ITEM))) {
                                PostItemImageCheckList postItemImageCheckListModel = new PostItemImageCheckList(
                                        imageModels.get(j).getId(),
                                        imageModels.get(j).getLabel(),
                                        String.valueOf(itemCheckListModels.get(i).getId()),
                                        getBitmapFromUri(imageModels.get(j).getName()),
                                        imageModels.get(j).getComment(),
                                        imageModels.get(j).getGps_longitude(),
                                        imageModels.get(j).getGps_latitude(),
                                        imageModels.get(j).getDatetime()
                                );
                                new registerItemImage().executeOnExecutor(THREAD_POOL_EXECUTOR, postItemImageCheckListModel);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    result = "";
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if (s.equals("ok")) {
                SharedPreferences sharedPrefCheck = context.getSharedPreferences("check_list", Context.MODE_PRIVATE);
                CheckListModel checkListModel = SqliteClass.getInstance(context).databasehelp.checkListSql.getCheck(sharedPrefCheck.getString("id_check_list_sql", ""));
                if (checkListModel.getState().equals("Validado") ||
                        checkListModel.getState().equals("review") ||
                        checkListModel.getState().equals("Validado con observaciones") ||
                        checkListModel.getState().equals("review_w_obs")) {
                    SqliteClass.getInstance(context).databasehelp.checkListSql.updateValueCheckListById(
                            SqliteClass.KEY_CHECSTA, "posted", sharedPrefCheck.getString("id_check_list_sql", ""));
                }
                Toast.makeText(context, "Envio éxitoso", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Edición Fallida", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(s);
        }
    }

    public static class registerItem extends AsyncTask<PostItemCheckListModel, Void, String> {
        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(PostItemCheckListModel... postItemCheckListModels) {
            SharedPreferences sharedPref = context.getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
            Call<PostItemCheckListModel> postCheckListItem = api.postItemCheckList(
                    sharedPref.getString("token", ""), postItemCheckListModels[0]);
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
                ArrayList<ImageModel> imageModels = SqliteClass.getInstance(context).databasehelp.imageSql.getImagesByField(String.valueOf(postItemCheckListModels[0].id_sql), KEY_IMAORDNUM);
                for (int j = 0; j < imageModels.size(); j++) {
                    if (!imageModels.get(j).getState().equals("server") && !imageModels.get(j).getState().equals("serverAndLocal")
                            && (imageModels.get(j).getLabel().equals(LABEL_CHECK_IMAGE_ITEM))) {
                        PostItemImageCheckList postItemImageCheckListModel = new PostItemImageCheckList(
                                imageModels.get(j).getId(),
                                imageModels.get(j).getLabel(),
                                String.valueOf(response.body().id),
                                getBitmapFromUri(imageModels.get(j).getName()),
                                imageModels.get(j).getComment(),
                                imageModels.get(j).getGps_longitude(),
                                imageModels.get(j).getGps_latitude(),
                                imageModels.get(j).getDatetime()
                        );
                        new registerItemImage().executeOnExecutor(THREAD_POOL_EXECUTOR, postItemImageCheckListModel);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class registerItemImage extends AsyncTask<PostItemImageCheckList, Void, String> {
        @Override
        protected String doInBackground(PostItemImageCheckList... postItemImageCheckList) {
            SharedPreferences sharedPref = context.getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

            MultipartBody.Part imagen = null;
            if (postItemImageCheckList[0].getImage() != null) {
                File filesDir = context.getFilesDir();
                File imageFile = new File(filesDir, "temp_file_" + postItemImageCheckList[0].getDatetime() + ".jpg");
                OutputStream os;
                try {
                    os = new FileOutputStream(imageFile);
                    postItemImageCheckList[0].getImage().compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.flush();
                    os.close();
                } catch (Exception ignored) {
                }
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
                imagen = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);
            }

            Call<ResponseImageItemChecklist> postCheckListItemImage = api.postItemImageCheckList(
                    sharedPref.getString("token", ""),
                    imagen,
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), postItemImageCheckList[0].getLabel()),
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), postItemImageCheckList[0].getComment()),
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), String.valueOf(Math.round(
                                    Double.parseDouble(postItemImageCheckList[0].getGps_longitude()) * 10000000000.0) / 10000000000.0)),
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), String.valueOf(Math.round(
                                    Double.parseDouble(postItemImageCheckList[0].getGps_latitude()) * 10000000000.0) / 10000000000.0)),
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), postItemImageCheckList[0].getDatetime()),
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), postItemImageCheckList[0].getCheck_list_item())
            );
            try {
                Response<ResponseImageItemChecklist> response = postCheckListItemImage.execute();
                if (response.isSuccessful()) {
                    System.out.println("*******************+");
                    System.out.println(response.body());
                    assert response.body() != null;
                    SqliteClass.getInstance(context).databasehelp.imageSql.updateValue(
                            SqliteClass.KEY_IMASTA,
                            "serverAndLocal",
                            String.valueOf(postItemImageCheckList[0].getId()));
                } else {
                    System.out.println("*******************+");
                    System.out.println(response.errorBody().toString());
                    System.out.println(response.errorBody().string());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_checklist, menu);
        menu.findItem(R.id.action_checklist).setVisible(false);
        menu.findItem(R.id.action_version).setTitle(BuildConfig.VERSION_NAME);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
