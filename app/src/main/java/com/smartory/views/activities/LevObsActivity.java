package com.smartory.views.activities;

import static com.smartory.resource.HorizontalValues.LABEL_OBSERVATION_ITEM;
import static com.smartory.resource.HorizontalValues.imageView_preview_on_dialog;
import static com.smartory.resource.HorizontalValues.itemCheckListAdapter;
import static com.smartory.resource.HorizontalValues.itemLevAdapter;
import static com.smartory.resource.HorizontalValues.verifyTireFragment;
import static com.smartory.views.activities.FormNewCheckListActivity.MY_PERMISSIONS_REQUEST_CAMARA;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smartory.BuildConfig;
import com.smartory.R;
import com.smartory.adapters.CabLevObsAdapter;
import com.smartory.db.SqliteClass;
import com.smartory.models.GetLevObsModel;
import com.smartory.models.ImageModel;
import com.smartory.models.LevObsModel;
import com.smartory.models.ResponseImageItemChecklist;
import com.smartory.network.InterfaceAPI;
import com.smartory.network.RetrofitClientInstance;
import com.smartory.resource.HorizontalValues;
import com.smartory.resource.OnIntentReceived;
import com.smartory.utils.ComparadorObs;
import com.smartory.utils.PictureUtils;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LevObsActivity extends AppCompatActivity {

    static ArrayList<LevObsModel> levObsModels; //all
    static ArrayList<LevObsModel> levObsModelsfiltrado = new ArrayList<>(); // filtered
    static ArrayList<String> levObsModelsNumber = new ArrayList<>(); // filtered
    static RecyclerView recyclerView;
    FloatingActionButton fltn_btn_sync;
    static Context context;
    static CabLevObsAdapter adapter;
    static ProgressDialog dialog;
    static Activity activity;
    static Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
    static final InterfaceAPI api = retrofit.create(InterfaceAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lev_obs);

        context = this;
        activity = this;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_list_lev_obs);
        fltn_btn_sync = findViewById(R.id.fltn_btn_sync);
        levObsModels = SqliteClass.getInstance(context).databasehelp.levObsSql.getAllLevOs();

        for(int i = levObsModels.size()-1; i>=0;i--){
            if(!levObsModelsNumber.contains(levObsModels.get(i).getCheck_list())){
                levObsModelsfiltrado.add(levObsModels.get(i));
                levObsModelsNumber.add(levObsModels.get(i).getCheck_list());
            }
        }
        Collections.sort(levObsModelsfiltrado, new ComparadorObs());
        getList(levObsModelsfiltrado);
        new UpdateTask().execute(true);
        fltn_btn_sync.setOnClickListener(view -> new UpdateTask().execute(true));

    }

    public static void getList(ArrayList<LevObsModel> list){
        adapter = new CabLevObsAdapter(activity, context, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    static public class UpdateTask extends AsyncTask<Boolean, Void, String> {
        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(context, "", "Cargando... Espere en esta pantalla por favor", true);

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("ok")){
                Toast.makeText(context,"SINCRONIZACIÓN EXITOSA",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context,"LA SINCRONIZACIÓN DE DATOS, REQUIERE CONEXIÓN A INTERNET",Toast.LENGTH_LONG).show();
            }
            levObsModels = SqliteClass.getInstance(context).databasehelp.levObsSql.getAllLevOs();

            for(int i = levObsModels.size()-1; i>=0;i--){
                if(!levObsModelsNumber.contains(levObsModels.get(i).getCheck_list())){
                    levObsModelsfiltrado.add(levObsModels.get(i));
                    levObsModelsNumber.add(levObsModels.get(i).getCheck_list());
                }
            }
            Collections.sort(levObsModelsfiltrado, new ComparadorObs());
            getList(levObsModelsfiltrado);
            dialog.dismiss();
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Boolean... booleans) {
            String token;
            SharedPreferences sharedPref = context.getSharedPreferences("login_preferences",Context.MODE_PRIVATE);
            token = sharedPref.getString("token","");
            Response<List<GetLevObsModel>> getLevObsItems;
            try {
                getLevObsItems = api.getLevObs(token).execute();
                if (getLevObsItems.isSuccessful()) {
                    SqliteClass.getInstance(context).databasehelp.levObsSql.deleteLevObsTable();
                    if (getLevObsItems.body() != null) {
                        for (GetLevObsModel observationItem : getLevObsItems.body()) {
                            LevObsModel levObsModel = new LevObsModel();
                            levObsModel.setPk(observationItem.getPk());
                            levObsModel.setId_item(observationItem.item.getId());
                            levObsModel.setCheck_list(observationItem.item.getCheck_list());
                            levObsModel.setCheck_list_date(observationItem.item.getCheck_list_date());
                            levObsModel.setTransport_company(observationItem.item.getTransport_company());
                            levObsModel.setTransport_unit(observationItem.item.getTransport_unit());
                            levObsModel.setSection(observationItem.item.getSection());
                            levObsModel.setCheck_list_item(observationItem.item.getCheck_list_item());
                            levObsModel.setSeverity(observationItem.item.getSeverity());
                            levObsModel.setObservation_description(observationItem.item.getObservation_description());
                            levObsModel.setObservation_lifting(observationItem.item.getObservation_lifting());
                            levObsModel.setDue_date(observationItem.item.getDue_date());
                            levObsModel.setState(observationItem.getState());
                            SqliteClass.getInstance(context).databasehelp.levObsSql.addLevObs(levObsModel);
                        }
                    }
                } else {
                    return "";
                }

                Response<List<ResponseImageItemChecklist>> getItemImage2 = api.getItemImageLiftingMobile(token).execute();
                if (getItemImage2.isSuccessful()) {
                    SqliteClass.getInstance(context).databasehelp.imageSql.deleteImageLabel(LABEL_OBSERVATION_ITEM);
                    if (getItemImage2.body() != null) {
                        for (ResponseImageItemChecklist liftingImageItem : getItemImage2.body()) {
                            ImageModel imageModel = new ImageModel();
                            imageModel.setDocumentIdApi(liftingImageItem.getNon_compliance()+"");
                            imageModel.setImage_url(liftingImageItem.getImage());
                            imageModel.setComment("");
                            imageModel.setLabel(LABEL_OBSERVATION_ITEM);
                            imageModel.setState("server");
                            imageModel.setType(liftingImageItem.getState_lifting());
                            SqliteClass.getInstance(context).databasehelp.imageSql.addImage(imageModel);
                        }
                    }
                } else {
                    return "";
                }
                return "ok";
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        levObsModels = SqliteClass.getInstance(context).databasehelp.levObsSql.getAllLevOs();
        for(int i = 0; i<levObsModels.size();i++){
            if(!levObsModelsNumber.contains(levObsModels.get(i).getCheck_list())){
                levObsModelsfiltrado.add(levObsModels.get(i));
                levObsModelsNumber.add(levObsModels.get(i).getCheck_list());
            }
        }
        getList(levObsModelsfiltrado);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LevObsActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
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
        if(id == android.R.id.home){
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
            finish();        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMARA) {
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "EL PERMISO ES NECESARIO", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 781) {
            OnIntentReceived mIntentListener2 = itemLevAdapter;
            if (mIntentListener2 != null) {
                mIntentListener2.onIntent(data, resultCode);
            }
        }
    }
}
