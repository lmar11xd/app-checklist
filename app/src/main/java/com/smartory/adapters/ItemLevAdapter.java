package com.smartory.adapters;

import static android.app.Activity.RESULT_OK;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.smartory.R;
import com.smartory.db.SqliteClass;
import com.smartory.models.ImageModel;
import com.smartory.models.LevObsModel;
import com.smartory.models.NonComplianceLifting;
import com.smartory.models.PatchLevItemModel;
import com.smartory.models.PhotoEvidence;
import com.smartory.models.ResponseImageLigthNoCompliance;
import com.smartory.models.ResponseLevItemModel;
import com.smartory.models.ResponsePostLigth;
import com.smartory.models.SendState;
import com.smartory.models.SendStaterAproved;
import com.smartory.models.SendStaterRejected;
import com.smartory.network.InterfaceAPI;
import com.smartory.network.RetrofitClientInstance;
import com.smartory.resource.HorizontalValues;
import com.smartory.resource.OnIntentReceived;
import com.smartory.utils.Position;
import org.jetbrains.annotations.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import static com.smartory.adapters.ItemCheckListAdapter.isLocationEnabled;
import static com.smartory.db.SqliteClass.KEY_IMAORDNUM;
import static com.smartory.db.SqliteClass.KEY_LEVOBSSTATE;
import static com.smartory.db.SqliteClass.KEY_LEV_OBS_OBS_LIFTING;
import static com.smartory.resource.HorizontalValues.LABEL_OBSERVATION_ITEM;
import static com.smartory.resource.HorizontalValues.itemLevAdapter;
import static com.smartory.views.activities.FormNewCheckListActivity.MY_PERMISSIONS_REQUEST_CAMARA;

public class ItemLevAdapter extends RecyclerView.Adapter<ItemLevAdapter.ViewHolder> implements OnIntentReceived {
    Context context;
    List<LevObsModel> items;
    Activity activity;
    RecyclerView recyclerView;
    PhotoAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    List<PhotoEvidence> list;
    CabLevObsAdapter fatherAdapter;

    public ItemLevAdapter(Activity activity, Context context, List<LevObsModel> items, CabLevObsAdapter fatherAdapter) {
        this.fatherAdapter = fatherAdapter;
        this.context = context;
        this.items = items;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.row_item_lev, parent, false);
        return new ViewHolder(contactView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ItemLevAdapter.ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onIntent(Intent i, int resultCode) {
        if (resultCode == RESULT_OK) {
            Bitmap imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(HorizontalValues.photoCompliance));
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert imageBitmap != null;
            int origWidth = imageBitmap.getWidth();
            int origHeight = imageBitmap.getHeight();
            int targetWidth = 400;
            int targetHeight = 600;
            Location location = Position.getLastBestLocation(context);
            if (origWidth<=origHeight) {
                if (origWidth > targetWidth) {
                    int destHeight = origHeight / (origWidth / targetWidth);
                    imageBitmap = Bitmap.createScaledBitmap(imageBitmap, targetWidth, destHeight, false);
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
                }
            } else {
                if (origHeight > targetHeight) {
                    int destWidth = origWidth / (origHeight / targetHeight);
                    imageBitmap = Bitmap.createScaledBitmap(imageBitmap, destWidth, targetHeight, false);
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
                }
            }
            SharedPreferences sharedPref = context.getSharedPreferences("check_list",Context.MODE_PRIVATE);
            String idItem = sharedPref.getString("idItem","");
            PhotoEvidence photoEvidence = new PhotoEvidence(
                    imageBitmap,
                    LABEL_OBSERVATION_ITEM,
                    HorizontalValues.photoCompliance,
                    "",
                    idItem,
                    location.getLongitude()+"",
                    location.getLatitude()+"",
                    new Date() + "",
                    ""
            );
            //photoEvidence.setState("draft");
            adapter.addElement(photoEvidence);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_section,tv_item;
        LinearLayout lnyt_item_lev;
        LinearLayout layout_register_photo;
        TextView title_dialog;

        @SuppressLint("WrongViewCast")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_section = itemView.findViewById(R.id.tv_section_item);
            tv_item = itemView.findViewById(R.id.tv_name_item);
            lnyt_item_lev = itemView.findViewById(R.id.lnyt_item_lev);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void bind (final LevObsModel levObsModel){
            reloadComponentBind(levObsModel);
        }

        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.N)
        private void reloadComponentBind(final LevObsModel levObsModel){
            tv_section.setText(levObsModel.getSection());
            tv_item.setText(levObsModel.getCheck_list_item());
            Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
            final InterfaceAPI api = retrofit.create(InterfaceAPI.class);
            switch (levObsModel.getState()) {
                case "Levantado":
                case "L":
                    lnyt_item_lev.setBackgroundColor(Color.parseColor("#BDECB6"));
                    break;
                case "Vencido":
                case "V":
                    lnyt_item_lev.setBackgroundColor(Color.parseColor("#F2A89E"));
                    break;
                case "Aceptado":
                case "AC":
                    lnyt_item_lev.setBackgroundColor(Color.parseColor("#9ef1f2"));
                    break;
            }

            lnyt_item_lev.setOnClickListener(v -> {
                if (levObsModel.state.equals("Vencido")||levObsModel.state.equals("V")){
                    Toast.makeText(context,"La observacion vencio", Toast.LENGTH_LONG).show();
                    return;
                }
                final AlertDialog alertDialog;
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.dialog_check_lev_item, null);
                layout_register_photo =  view.findViewById(R.id.layout_register_photo);
                title_dialog = view.findViewById(R.id.title_dialog);
                recyclerView = view.findViewById(R.id.recicler_photos);
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);
                recyclerView.setLayoutManager(layoutManager);
                list = new ArrayList();
                adapter = new PhotoAdapter(list, context, true);
                recyclerView.setAdapter(adapter);

                ArrayList<ImageModel> imageModels = SqliteClass.getInstance(context).databasehelp.
                        imageSql.getImagesByField(levObsModel.getPk()+"",KEY_IMAORDNUM);
                imageModels.removeIf(
                        item-> item.getState().equals("server")||!item.getLabel().equals(LABEL_OBSERVATION_ITEM)
                );
                ArrayList<ImageModel> imageModelsServer = SqliteClass.getInstance(context).databasehelp.imageSql.getImagesByField(levObsModel.getPk()+"",KEY_IMAORDNUM);
                imageModelsServer.removeIf(
                        item-> item.getState().equals("local")||!item.getLabel().equals(LABEL_OBSERVATION_ITEM)
                );

                Button btnFire = view.findViewById(R.id.btn_ok);
                Button btnCancel = view.findViewById(R.id.btn_cancel);
                Button btnRejected = view.findViewById(R.id.btn_rejected);

                final Button btn_compliance_image = view.findViewById(R.id.take_photo);
                final String[] obs_description = new String[2];
                obs_description[0] = "";
                final String[] obs_comment_accepted = new String[2];
                obs_comment_accepted[0] = "";
                final EditText et_lev_obs = view.findViewById(R.id.et_lev);
                final EditText et_lev_accepted_rejected = view.findViewById(R.id.et_lev_accepted_rejected);
                final TextInputLayout text_et_lev_accepted_rejected = view.findViewById(R.id.text_et_lev_accepted_rejected);

                SharedPreferences sharedPrefLogin = context.getSharedPreferences("login_preferences",Context.MODE_PRIVATE);
                switch (levObsModel.getState()) {
                    case "Levantado":
                    case "L":
                        title_dialog.setText("DETALLE DE LEVANTAMIENTO");
                        et_lev_obs.setText(levObsModel.getObservation_lifting());
                        et_lev_obs.setEnabled(false);
                        createGallery(imageModels,levObsModel.getPk()+"");
                        createGalleryServer(imageModelsServer,levObsModel.getPk()+"");
                        btn_compliance_image.setVisibility(View.INVISIBLE);
                        btnCancel.setText("CERRAR");
                        btnFire.setText("ACEPTAR");
                        text_et_lev_accepted_rejected.setVisibility(View.VISIBLE);
                        if(sharedPrefLogin.getBoolean("can_accept_observation_lifting",false)){
                            btnFire.setVisibility(View.VISIBLE);
                            btnRejected.setVisibility(View.VISIBLE);
                        } else {
                            btnFire.setVisibility(View.GONE);
                            btnRejected.setVisibility(View.GONE);
                        }
                        break;
                    case "Aceptado":
                    case "AC":
                        title_dialog.setText("DETALLE DE LEVANTAMIENTO");
                        et_lev_obs.setText(levObsModel.getObservation_lifting());
                        et_lev_obs.setEnabled(false);
                        createGallery(imageModels,levObsModel.getPk()+"");
                        createGalleryServer(imageModelsServer,levObsModel.getPk()+"");
                        btn_compliance_image.setVisibility(View.INVISIBLE);
                        text_et_lev_accepted_rejected.setVisibility(View.GONE);
                        btnCancel.setText("CERRAR");
                        btnFire.setVisibility(View.GONE);
                        btnRejected.setVisibility(View.GONE);
                        break;
                    case "Pendiente":
                    case "P":
                    case "Por vencer":
                    case "PV":
                        title_dialog.setText("REALIZAR LEVANTAMIENTO");
                        et_lev_obs.setEnabled(true);
                        et_lev_obs.setText("");
                        btn_compliance_image.setVisibility(View.VISIBLE);
                        text_et_lev_accepted_rejected.setVisibility(View.GONE);
                        btnCancel.setText("CANCELAR");
                        btnFire.setText("GUARDAR");

                        if(sharedPrefLogin.getBoolean("can_extend_noncompliance_checklist",false)){
                            btnFire.setVisibility(View.VISIBLE);
                        } else {
                            btnFire.setVisibility(View.GONE);
                        }

                        btnRejected.setVisibility(View.GONE);
                        break;
                }
                final SharedPreferences sharedPref = context.getSharedPreferences("check_list", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = sharedPref.edit();

                editor.putString("idItem",String.valueOf(levObsModel.getPk()));
                editor.putString("idItemSql",String.valueOf(levObsModel.getIdSql()));
                editor.apply();

                btn_compliance_image.setOnClickListener(v1 -> callCamera());

                et_lev_obs.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        if(s.length()>0){
                            obs_description[0] =s.toString();
                        } else  {
                            obs_description[0] ="";
                        }
                    }
                });
                et_lev_accepted_rejected.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        if(s.length()>0){
                            obs_comment_accepted[0] =s.toString();
                        } else  {
                            obs_comment_accepted[0] ="";
                        }
                    }
                });
                builder.setView(view);
                alertDialog = builder.create();

                btnFire.setOnClickListener(v1 -> {
                    SharedPreferences sharedPref12 = context.getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
                    String token = sharedPref12.getString("token", "");
                    String id = String.valueOf(levObsModel.getPk());
                    if(levObsModel.getState().equals("Pendiente")||levObsModel.getState().equals("Por vencer")||
                            levObsModel.getState().equals("P")||levObsModel.getState().equals("PV")) {
                        if(list.size()==0){
                            Toast.makeText(context,"NECESITA AL MENOS UNA FOTO", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        enableOrDisableBtn(btnFire,btnCancel,btnRejected,false);
                        PatchLevItemModel patchLevItemModel = new PatchLevItemModel();
                        patchLevItemModel.setState("C");
                        patchLevItemModel.setObservation_lifting(obs_description[0]);
                        patchLevItemModel.setObservation_lifting_image("");

                        List<PhotoEvidence> auxList = new ArrayList<>(list);

                        NonComplianceLifting nonComplianceLifting = new NonComplianceLifting();
                        nonComplianceLifting.setState("pending");
                        nonComplianceLifting.setObservation_lifting(obs_description[0]);
                        nonComplianceLifting.setRejection_comment("");
                        nonComplianceLifting.setNon_compliance(id);
                        api.postNonComplianceLifting(token, nonComplianceLifting).enqueue(new Callback<ResponsePostLigth>() {
                            @Override
                            public void onResponse(@NotNull Call<ResponsePostLigth> call, @NotNull Response<ResponsePostLigth> response) {
                                lnyt_item_lev.setBackgroundColor(Color.parseColor("#BDECB6"));
                                levObsModel.setState("L");
                                levObsModel.setObservation_lifting(obs_description[0]);
                                SqliteClass.getInstance(context).databasehelp.levObsSql.updateValueLevObs(
                                        KEY_LEVOBSSTATE,
                                        "L",
                                        levObsModel.getIdSql()+""
                                );
                                SqliteClass.getInstance(context).databasehelp.levObsSql.updateValueLevObs(
                                        KEY_LEV_OBS_OBS_LIFTING,
                                        obs_description[0],
                                        levObsModel.getIdSql()+""
                                );
                                for (int i = 0; i < auxList.size(); i++) {
                                    if (auxList.get(i).getState().equals("local")) {
                                        SqliteClass.getInstance(context).databasehelp.imageSql.addImage(new ImageModel(
                                                0,
                                                auxList.get(i).getImage_url(),
                                                auxList.get(i).getDocument(),
                                                "local",
                                                "pending",
                                                "",
                                                auxList.get(i).getGps_longitude(),
                                                auxList.get(i).getGps_latitude(),
                                                auxList.get(i).getDatetime(),
                                                LABEL_OBSERVATION_ITEM,
                                                "",
                                                ""
                                        ));
                                        Bitmap postImage = getBitmapFromUri(auxList.get(i).getImage_url());
                                        MultipartBody.Part imagen = null;
                                        if (postImage != null) {
                                            File filesDir = context.getFilesDir();
                                            File imageFile = new File(filesDir, "temp_file_" + auxList.get(i).getDatetime() + ".jpg");
                                            OutputStream os;
                                            try {
                                                os = new FileOutputStream(imageFile);
                                                postImage.compress(Bitmap.CompressFormat.JPEG, 100, os);
                                                os.flush();
                                                os.close();
                                            } catch (Exception ignored) { }
                                            RequestBody requestFile =
                                                    RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
                                            imagen = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);
                                        }


                                        assert response.body() != null;
                                        api.postItemImageLifting(
                                                token, imagen,
                                                RequestBody.create(
                                                        MediaType.parse("multipart/form-data"), auxList.get(i).getLabel()),
                                                RequestBody.create(
                                                        MediaType.parse("multipart/form-data"), auxList.get(i).getComment()),
                                                RequestBody.create(
                                                        MediaType.parse("multipart/form-data"), String.valueOf(Math.round(
                                                                Double.parseDouble(auxList.get(i).getGps_longitude())*10000000000.0)/10000000000.0)),
                                                RequestBody.create(
                                                        MediaType.parse("multipart/form-data"), String.valueOf(Math.round(
                                                                Double.parseDouble(auxList.get(i).getGps_latitude())*10000000000.0)/10000000000.0)),
                                                RequestBody.create(
                                                        MediaType.parse("multipart/form-data"), auxList.get(i).getDatetime()),
                                                RequestBody.create(
                                                        MediaType.parse("multipart/form-data"), response.body().getPk())
                                        ).enqueue(new Callback<ResponseImageLigthNoCompliance>() {
                                            @Override
                                            public void onResponse(@NotNull Call<ResponseImageLigthNoCompliance> call,
                                                                   @NotNull Response<ResponseImageLigthNoCompliance> response) {
                                                System.out.println("***********");
                                                if (response.isSuccessful()){
                                                    System.out.println(response.body());
                                                    System.out.println("success");
                                                }else {

                                                    System.out.println("no success");
                                                }
                                            }
                                            @Override
                                            public void onFailure(@NotNull Call<ResponseImageLigthNoCompliance> call, @NotNull Throwable t) {
                                                System.out.println("***********");
                                                System.out.println("FAILURE");
                                                System.out.println(t.toString());
                                                System.out.println(call.request().toString());
                                            }
                                        });
                                    }
                                }
                                enableOrDisableBtn(btnFire,btnCancel,btnRejected,true);
                                alertDialog.dismiss();
                                Toast.makeText(context, "Observación Levantada con Exito", Toast.LENGTH_LONG).show();
                            }
                            @Override
                            public void onFailure(@NotNull Call<ResponsePostLigth> call, @NotNull Throwable t) {
                                enableOrDisableBtn(btnFire,btnCancel,btnRejected,true);
                                Toast.makeText(context, "Problemas al enviar", Toast.LENGTH_LONG).show();
                            }
                        });
                        api.patchItem(token,id,patchLevItemModel).enqueue(new Callback<ResponseLevItemModel>() {
                            @Override
                            public void onResponse(@NotNull Call<ResponseLevItemModel> call,
                                                   @NotNull Response<ResponseLevItemModel> response) {
                            }
                            @Override
                            public void onFailure(@NotNull Call<ResponseLevItemModel> call, @NotNull Throwable t) {
                            }
                        });
                    } else if (levObsModel.getState().equals("Levantado")||levObsModel.getState().equals("L")) {
                        SendState patchLevItemModel = new SendState();
                        patchLevItemModel.setState("AC");
                        api.patchSectionItem(token,id,patchLevItemModel).enqueue(new Callback<ResponseLevItemModel>() {
                            @Override
                            public void onResponse(@NotNull Call<ResponseLevItemModel> call, @NotNull Response<ResponseLevItemModel> response) {
                                lnyt_item_lev.setBackgroundColor(Color.parseColor("#9ef1f2"));
                                levObsModel.setState("AC");
                                SqliteClass.getInstance(context).databasehelp.levObsSql.updateValueLevObs(
                                        KEY_LEVOBSSTATE,
                                        "AC",
                                        levObsModel.getIdSql()+""
                                );
                                enableOrDisableBtn(btnFire,btnCancel,btnRejected,true);
                                alertDialog.dismiss();
                                Toast.makeText(context, "Levantamiento Aceptado con Exito", Toast.LENGTH_LONG).show();
                            }
                            @Override
                            public void onFailure(@NotNull Call<ResponseLevItemModel> call, @NotNull Throwable t) {
                                enableOrDisableBtn(btnFire,btnCancel,btnRejected,true);
                                Toast.makeText(context, "Problemas al enviar", Toast.LENGTH_LONG).show();
                            }
                        });
                        api.getNonComplianceLifting(token,levObsModel.getPk()+"","pending").enqueue(new Callback<List<ResponsePostLigth>>() {
                            @Override
                            public void onResponse(@NotNull Call<List<ResponsePostLigth>> call, @NotNull Response<List<ResponsePostLigth>> response) {
                                if (response.body() != null && response.body().size() > 0) {
                                    SendStaterAproved patchLevItemModel = new SendStaterAproved();
                                    patchLevItemModel.setState("accepted");
                                    patchLevItemModel.setObservation_lifting(obs_comment_accepted[0]);
                                    api.patchNonComplianceLifting(token, response.body().get(0).getPk(), patchLevItemModel).enqueue(
                                        new Callback<ResponsePostLigth>() {
                                            @Override
                                            public void onResponse(@NotNull Call<ResponsePostLigth> call, @NotNull Response<ResponsePostLigth> response) {}
                                            @Override
                                            public void onFailure(@NotNull Call<ResponsePostLigth> call, @NotNull Throwable t) {}
                                        }
                                    );
                                }
                            }
                            @Override
                            public void onFailure(@NotNull Call<List<ResponsePostLigth>> call, @NotNull Throwable t) {}
                        });
                    }
                });
                btnCancel.setOnClickListener(v12 -> alertDialog.dismiss());
                btnRejected.setOnClickListener(v12 -> {
                    if (obs_comment_accepted[0]==null||obs_comment_accepted[0].equals("")){
                        Toast.makeText(context,"INGRESE UNA RAZÓN DE RECHAZO", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    enableOrDisableBtn(btnFire,btnCancel,btnRejected,false);
                    SharedPreferences sharedPref12 = context.getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
                    String token = sharedPref12.getString("token", "");
                    String id = String.valueOf(levObsModel.getPk());
                    SendState patchLevItemModel = new SendState();
                    patchLevItemModel.setState("P");
                    api.patchSectionItem(token,id,patchLevItemModel).enqueue(new Callback<ResponseLevItemModel>() {
                        @Override
                        public void onResponse(@NotNull Call<ResponseLevItemModel> call, @NotNull Response<ResponseLevItemModel> response) {
                            lnyt_item_lev.setBackgroundColor(Color.parseColor("#ffffff"));
                            levObsModel.setState("P");
                            SqliteClass.getInstance(context).databasehelp.levObsSql.updateValueLevObs(
                                    KEY_LEVOBSSTATE,
                                    "P",
                                    levObsModel.getIdSql()+""
                            );
                            SqliteClass.getInstance(context).databasehelp.imageSql.deleteImageForLabelAndDocument(
                                    levObsModel.getPk()+"",LABEL_OBSERVATION_ITEM
                            );
                            enableOrDisableBtn(btnFire,btnCancel,btnRejected,true);
                            alertDialog.dismiss();
                            Toast.makeText(context, "Levantamiento Rechazado", Toast.LENGTH_LONG).show();
                        }
                        @Override
                        public void onFailure(@NotNull Call<ResponseLevItemModel> call, @NotNull Throwable t) {
                            enableOrDisableBtn(btnFire,btnCancel,btnRejected,true);
                            Toast.makeText(context, "Problemas al enviar", Toast.LENGTH_LONG).show();
                        }
                    });

                    api.getNonComplianceLifting(token,levObsModel.getPk()+"","pending").
                            enqueue(new Callback<List<ResponsePostLigth>>() {
                        @Override
                        public void onResponse(@NotNull Call<List<ResponsePostLigth>> call, @NotNull Response<List<ResponsePostLigth>> response) {
                            if (response.body() != null && response.body().size() > 0) {
                                SendStaterRejected patchLevItemModel = new SendStaterRejected();
                                patchLevItemModel.setState("rejected");
                                patchLevItemModel.setRejection_comment(obs_comment_accepted[0]);
                                api.patchNonComplianceLifting(token, response.body().get(0).getPk(), patchLevItemModel).enqueue(
                                    new Callback<ResponsePostLigth>() {
                                        @Override
                                        public void onResponse(@NotNull Call<ResponsePostLigth> call,
                                                                   @NotNull Response<ResponsePostLigth> response) {}
                                        @Override
                                        public void onFailure(@NotNull Call<ResponsePostLigth> call, @NotNull Throwable t) {}
                                    }
                                );
                            }
                        }
                        @Override
                        public void onFailure(@NotNull Call<List<ResponsePostLigth>> call, @NotNull Throwable t) {}
                    });
                });
                alertDialog.show();
            });
        }
        private void enableOrDisableBtn(Button ok, Button cancel, Button rejected, boolean option){
            ok.setEnabled(option);
            cancel.setEnabled(option);
            rejected.setEnabled(option);
        }
    }

    private void callCamera () {
        if(!isLocationEnabled(activity)){
            Toast.makeText(activity, "EL GPS NO ESTA HABILITADO", Toast.LENGTH_LONG).show();
            Location location = Position.getLastBestLocation(activity);
            if(location==null){
                Toast.makeText(activity, "EL GPS NO ESTA HABILITADO", Toast.LENGTH_LONG).show();
            }
            return;
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMARA );
            } else {
                openCamera();
            }
        } else {
            openCamera();
        }
    }
    public void openCamera() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                i.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", photoFile));

            }
            itemLevAdapter = this;
            activity.startActivityForResult(i, 781);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context,"NO HAY CÁMARAS DISPONIBLES",Toast.LENGTH_SHORT).show();
        }
    }
    private Bitmap getBitmapFromUri(String url_path){
        Bitmap imageBitmap;
        try {
            imageBitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), Uri.parse(url_path));
            int origWidth = Objects.requireNonNull(imageBitmap).getWidth();
            int origHeight = imageBitmap.getHeight();
            int targetWidth = 400;
            int targetHeight = 600;
            if (origWidth<=origHeight) {
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
    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        File storageDir = Objects.requireNonNull(activity).getExternalFilesDir(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        HorizontalValues.photoCompliance = "file:" + image.getAbsolutePath();
        return image;
    }
    private void createGalleryServer(ArrayList<ImageModel> gallery, String idItem){
        for (int i = 0; i< gallery.size(); i++) {
            PhotoEvidence photoEvidence = new PhotoEvidence(
                    null,
                    gallery.get(i).getLabel(),
                    gallery.get(i).getImage_url(),
                    gallery.get(i).getComment(),
                    idItem,
                    gallery.get(i).getGps_longitude(),
                    gallery.get(i).getGps_latitude(),
                    gallery.get(i).getDatetime(),
                    ""
            );
            photoEvidence.setState("server");
            adapter.addElement(photoEvidence);
        }
    }
    private void createGallery(ArrayList<ImageModel> gallery, String idItem){
        for (int i = 0; i < gallery.size();i++){
            Bitmap imageBitmap;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(gallery.get(i).getName()));
                int origWidth = Objects.requireNonNull(imageBitmap).getWidth();
                int origHeight = imageBitmap.getHeight();
                int targetWidth = 400;
                int targetHeight = 600;
                PhotoEvidence photoEvidence;
                if (origWidth<=origHeight) {
                    if (origWidth > targetWidth) {
                        int destHeight = origHeight / (origWidth / targetWidth);
                        Bitmap b2 = Bitmap.createScaledBitmap(imageBitmap, targetWidth, destHeight, false);
                        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                        b2.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
                        photoEvidence = new PhotoEvidence(
                                b2,
                                gallery.get(i).getLabel(),
                                "",
                                gallery.get(i).getComment(),
                                idItem+"",
                                gallery.get(i).getGps_longitude(),
                                gallery.get(i).getGps_latitude(),
                                gallery.get(i).getDatetime(),
                                ""
                        );
                    } else {
                        photoEvidence = new PhotoEvidence(
                                imageBitmap,
                                gallery.get(i).getLabel(),
                                "",
                                gallery.get(i).getComment(),
                                idItem,
                                gallery.get(i).getGps_longitude(),
                                gallery.get(i).getGps_latitude(),
                                gallery.get(i).getDatetime(),
                                ""
                        );
                    }
                } else {
                    if (origHeight > targetHeight) {
                        int destWidth = origWidth / (origHeight / targetHeight);
                        Bitmap b2 = Bitmap.createScaledBitmap(imageBitmap, destWidth, targetHeight, false);
                        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                        b2.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
                        photoEvidence = new PhotoEvidence(
                                b2,
                                gallery.get(i).getLabel(),
                                "",
                                gallery.get(i).getComment(),
                                idItem,
                                gallery.get(i).getGps_longitude(),
                                gallery.get(i).getGps_latitude(),
                                gallery.get(i).getDatetime(),
                                ""
                        );
                    } else {
                        photoEvidence = new PhotoEvidence(
                                imageBitmap,
                                gallery.get(i).getLabel(),
                                "",
                                gallery.get(i).getComment(),
                                idItem,
                                gallery.get(i).getGps_longitude(),
                                gallery.get(i).getGps_latitude(),
                                gallery.get(i).getDatetime(),
                                ""
                        );
                    }
                }
                photoEvidence.setState(gallery.get(i).getState());
                photoEvidence.setIdSql(gallery.get(i).getId()+"");
                adapter.addElement(photoEvidence);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

