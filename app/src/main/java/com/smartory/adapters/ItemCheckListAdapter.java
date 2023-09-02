package com.smartory.adapters;

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
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.smartory.R;
import com.smartory.config.ConstValue;
import com.smartory.db.SqliteClass;
import com.smartory.interfaces.IChecklistProgress;
import com.smartory.models.ImageModel;
import com.smartory.models.ItemCheckListModel;
import com.smartory.models.PhotoEvidence;
import com.smartory.resource.HorizontalValues;
import com.smartory.resource.OnIntentReceived;
import com.smartory.utils.Position;
import com.smartory.views.activities.CheckListFormActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.smartory.db.SqliteClass.KEY_IMAORDNUM;
import static com.smartory.resource.HorizontalValues.LABEL_CHECK_IMAGE_ITEM;
import static com.smartory.resource.HorizontalValues.itemCheckListAdapter;
import static com.smartory.views.activities.FormNewCheckListActivity.MY_PERMISSIONS_REQUEST_CAMARA;

public class ItemCheckListAdapter extends RecyclerView.Adapter<ItemCheckListAdapter.ViewHolder> implements OnIntentReceived {

    Context context;
    Activity activity;
    List<ItemCheckListModel> items;
    RecyclerView recyclerView;
    PhotoAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    List<PhotoEvidence> list;

    IChecklistProgress callback;

    public static String HINT_OBSERVACION = "DESCRIPCIÓN DE LA OBSERVACIÓN";
    public static String HINT_COMENTARIO = "COMENTARIO";

    public ItemCheckListAdapter(Activity activity, Context context, ArrayList<ItemCheckListModel> items, IChecklistProgress callback) {
        this.context = context;
        this.items = items;
        this.activity = activity;
        this.callback = callback;
    }

    public ItemCheckListAdapter(Activity activity, Context context, ArrayList<ItemCheckListModel> items) {
        this.context = context;
        this.items = items;
        this.activity = activity;
        this.callback = null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.row_item_checklist, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
            int origWidth = imageBitmap.getWidth();
            int origHeight = imageBitmap.getHeight();
            int targetWidth = 400;
            int targetHeight = 600;
            Location location = Position.getLastBestLocation(context);
            if (origWidth <= origHeight) {
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
            SharedPreferences sharedPref = context.getSharedPreferences("check_list", Context.MODE_PRIVATE);
            String idItem = sharedPref.getString("idItem", "");
            PhotoEvidence photoEvidence = new PhotoEvidence(
                    imageBitmap,
                    LABEL_CHECK_IMAGE_ITEM,
                    HorizontalValues.photoCompliance,
                    "",
                    idItem,
                    location != null ? location.getLongitude() + "" : "0",
                    location != null ? location.getLatitude() + "" : "0",
                    new Date() + "",
                    sharedPref.getString("photo_type", "c")
            );
            photoEvidence.setState("draft");
            adapter.addElement(photoEvidence);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name_item, tv_state_item;
        public LinearLayout lnyt_observtion, lnyt_item, lnyt_item_container;
        LinearLayout layout_register_photo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name_item = itemView.findViewById(R.id.tv_name_item);
            tv_state_item = itemView.findViewById(R.id.tv_state_item);
            lnyt_observtion = itemView.findViewById(R.id.lnyt_observacion);
            lnyt_item = itemView.findViewById(R.id.lnyt_item);
            lnyt_item_container = itemView.findViewById(R.id.lnyt_item_container);
        }

        @SuppressLint("SetTextI18n")
        public void bind(ItemCheckListModel itemCheckListModel) {
            ItemCheckListModel item = SqliteClass.getInstance(context).databasehelp.itemSql.getItemById(String.valueOf(itemCheckListModel.getIdSql()));
            if (item.getReviewed()) {
                lnyt_item_container.setBackground(context.getApplicationContext().getDrawable(R.drawable.border_round_checked));
            }
            tv_name_item.setText(itemCheckListModel.getName());
            switch (item.getState()) {
                case "C":
                    tv_state_item.setText("Cumple");
                    break;
                case "NC":
                    tv_state_item.setText("No Cumple");
                    break;
                case "NA":
                    tv_state_item.setText("No Aplica");
                    break;
                case "NV":
                    tv_state_item.setText("No se puede Verificar");
                    break;
            }
            if (itemCheckListModel.getState().equals("NC")) { // Change color
                lnyt_observtion.setBackgroundColor(Color.RED);
            } else {
                lnyt_observtion.setBackgroundColor(Color.GRAY);
            }
            lnyt_item.setOnClickListener(v -> {
                AlertDialog alertDialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.dialog_check_item, null);
                Button btnFire = view.findViewById(R.id.btn_ok);
                Button btnCancel = view.findViewById(R.id.btn_cancel);
                EditText et_obs = view.findViewById(R.id.et_observaciones);
                TextInputLayout hint_obs = view.findViewById(R.id.ti_observacion);
                layout_register_photo = view.findViewById(R.id.layout_register_photo);
                recyclerView = view.findViewById(R.id.recicler_photos);
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager);
                list = new ArrayList();
                adapter = new PhotoAdapter(list, context);
                recyclerView.setAdapter(adapter);

                ArrayList<ImageModel> imageModels = SqliteClass.getInstance(context).databasehelp.imageSql.getImagesByField(itemCheckListModel.getIdSql() + "", KEY_IMAORDNUM);
                ArrayList<ImageModel> imageModelsServer = SqliteClass.getInstance(context).databasehelp.imageSql.getImagesByField(itemCheckListModel.getId() + "", KEY_IMAORDNUM);
                createGallery(imageModels, itemCheckListModel.getIdSql() + "");
                createGalleryServer(imageModelsServer, itemCheckListModel.getIdSql() + "");
                RadioButton checkbox_c, checkbox_n_c, checkbox_n_a, checkbox_n_v;
                Button btn_compliace_image = view.findViewById(R.id.take_photo);
                SharedPreferences sharedPref = context.getSharedPreferences("check_list", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("idItem", String.valueOf(itemCheckListModel.getIdSql()));
                editor.apply();

                checkbox_c = view.findViewById(R.id.checkbox_c);
                checkbox_n_c = view.findViewById(R.id.checkbox_n_c);
                checkbox_n_a = view.findViewById(R.id.checkbox_n_a);
                checkbox_n_v = view.findViewById(R.id.checkbox_n_v);

                if (sharedPref.getString("opcion", "").equals(ConstValue.VIEW)) {
                    btnFire.setVisibility(View.GONE);
                    btnCancel.setVisibility(View.GONE);
                    et_obs.setEnabled(false);
                    checkbox_c.setEnabled(false);
                    checkbox_n_c.setEnabled(false);
                    checkbox_n_a.setEnabled(false);
                    checkbox_n_v.setEnabled(false);
                    btn_compliace_image.setEnabled(false);
                } else {
                    btnFire.setVisibility(View.VISIBLE);
                    btnCancel.setVisibility(View.VISIBLE);
                    et_obs.setEnabled(true);
                    checkbox_c.setEnabled(true);
                    checkbox_n_c.setEnabled(true);
                    checkbox_n_a.setEnabled(true);
                    checkbox_n_v.setEnabled(true);
                    btn_compliace_image.setEnabled(true);
                }
                switch (item.getState()) {
                    case "C":
                        checkbox_c.setChecked(true);
                        hint_obs.setHint(HINT_OBSERVACION);
                        break;
                    case "NC":
                        checkbox_n_c.setChecked(true);
                        hint_obs.setHint(HINT_OBSERVACION);
                        break;
                    case "NA":
                        checkbox_n_a.setChecked(true);
                        btn_compliace_image.setEnabled(false);
                        hint_obs.setHint(HINT_OBSERVACION);
                        break;
                    case "NV":
                        checkbox_n_v.setChecked(true);
                        btn_compliace_image.setEnabled(true);
                        hint_obs.setHint(HINT_COMENTARIO);
                        break;
                }

                checkbox_c.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        checkbox_n_c.setChecked(false);
                        checkbox_n_a.setChecked(false);
                        checkbox_n_v.setChecked(false);
                        btn_compliace_image.setEnabled(true);
                        layout_register_photo.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        hint_obs.setHint(HINT_OBSERVACION);
                    }
                });
                checkbox_n_c.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        checkbox_c.setChecked(false);
                        checkbox_n_a.setChecked(false);
                        checkbox_n_v.setChecked(false);
                        btn_compliace_image.setEnabled(true);
                        layout_register_photo.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        hint_obs.setHint(HINT_OBSERVACION);
                    }
                });
                checkbox_n_a.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        checkbox_c.setChecked(false);
                        checkbox_n_c.setChecked(false);
                        checkbox_n_v.setChecked(false);
                        btn_compliace_image.setEnabled(false);
                        layout_register_photo.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        hint_obs.setHint(HINT_OBSERVACION);
                    }
                });
                checkbox_n_v.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        checkbox_c.setChecked(false);
                        checkbox_n_c.setChecked(false);
                        checkbox_n_a.setChecked(false);
                        btn_compliace_image.setEnabled(true);
                        layout_register_photo.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        hint_obs.setHint(HINT_COMENTARIO);
                    }
                });

                btn_compliace_image.setOnClickListener(v1 -> callCamera());
                et_obs.setText(item.getObservation_description());
                final String[] commentItem = {""};
                et_obs.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() > 0) {
                            commentItem[0] = "";
                        } else {
                            commentItem[0] = s.toString();
                        }
                    }
                });
                builder.setView(view);
                alertDialog = builder.create();
                if (sharedPref.getString("type", "").equals("cloud")) {
                    btnFire.setText("Actualizar");
                } else {
                    btnFire.setText("OK");
                }
                btnFire.setOnClickListener(
                        v12 -> {
                            if (checkbox_n_c.isChecked() && (et_obs.length() == 0 || list.size() == 0)) {
                                Toast.makeText(context, " Necesita al menos una foto de evidencia y/o describir la observación sobre la falta ", Toast.LENGTH_LONG).show();
                                return;
                            }
                            SqliteClass.getInstance(context).databasehelp.itemSql.updateValueItemById(
                                    SqliteClass.KEY_ITEMEDI,
                                    "si",
                                    String.valueOf(itemCheckListModel.getIdSql()));
                            SqliteClass.getInstance(context).databasehelp.itemSql.updateValueItemById(
                                    SqliteClass.KEY_ITEMREVIEWED,
                                    "1",
                                    String.valueOf(itemCheckListModel.getIdSql()));
                            if (checkbox_c.isChecked()) {
                                SqliteClass.getInstance(context).databasehelp.itemSql.updateValueItemById(
                                        SqliteClass.KEY_ITEMSTA,
                                        "C",
                                        String.valueOf(itemCheckListModel.getIdSql()));
                                tv_state_item.setText("Cumple");
                                lnyt_observtion.setBackgroundColor(Color.GRAY);
                            } else if (checkbox_n_c.isChecked()) {
                                SqliteClass.getInstance(context).databasehelp.itemSql.updateValueItemById(
                                        SqliteClass.KEY_ITEMSTA,
                                        "NC",
                                        String.valueOf(itemCheckListModel.getIdSql()));
                                tv_state_item.setText("No Cumple");
                                lnyt_observtion.setBackgroundColor(Color.RED);
                            } else if (checkbox_n_a.isChecked()) {
                                SqliteClass.getInstance(context).databasehelp.itemSql.updateValueItemById(
                                        SqliteClass.KEY_ITEMSTA,
                                        "NA",
                                        String.valueOf(itemCheckListModel.getIdSql()));
                                tv_state_item.setText("No Aplica");
                                //SqliteClass.getInstance(context).databasehelp.imageSql.deleteImage(itemCheckListModel.getIdSql()+"");
                                list.clear();
                                lnyt_observtion.setBackgroundColor(Color.GRAY);
                            } else if (checkbox_n_v.isChecked()) {
                                SqliteClass.getInstance(context).databasehelp.itemSql.updateValueItemById(
                                        SqliteClass.KEY_ITEMSTA,
                                        "NV",
                                        String.valueOf(itemCheckListModel.getIdSql()));
                                tv_state_item.setText("No se puede Verificar");
                                //SqliteClass.getInstance(context).databasehelp.imageSql.deleteImage(itemCheckListModel.getIdSql()+"");
                                list.clear();
                                lnyt_observtion.setBackgroundColor(Color.GRAY);
                            }
                            for (int i = 0; i < list.size(); i++) {
                                if (!list.get(i).getState().equals("server") && !list.get(i).getState().equals("serverAndLocal")) {
                                    ImageModel imageModel = new ImageModel(
                                            0,
                                            list.get(i).getImage_url(),
                                            list.get(i).getDocument(),
                                            "local",
                                            checkbox_c.isChecked() ? "C" : "NC",
                                            "",
                                            list.get(i).getGps_longitude(),
                                            list.get(i).getGps_latitude(),
                                            list.get(i).getDatetime(),
                                            LABEL_CHECK_IMAGE_ITEM,
                                            "",
                                            ""
                                    );
                                    SqliteClass.getInstance(context).databasehelp.imageSql.addImage(imageModel);
                                }
                            }
                            String valueObservation = "";
                            if (et_obs.length() > 0)
                                valueObservation = et_obs.getText().toString();

                            SqliteClass.getInstance(context).databasehelp.itemSql.updateValueItemById(
                                    SqliteClass.KEY_ITEMOBSDES,
                                    valueObservation,
                                    String.valueOf(itemCheckListModel.getIdSql()));

                            if (callback != null) {
                                callback.updateProgress(itemCheckListModel.getId_check_list());
                            }

                            alertDialog.dismiss();
                            Intent intent = new Intent(activity, CheckListFormActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra("fragment", "4");
                            intent.putExtra("response", "check_adapter");
                            activity.startActivity(intent);
                        }
                );
                btnCancel.setOnClickListener(v13 -> alertDialog.dismiss());
                alertDialog.show();
            });
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
            itemCheckListAdapter = this;
            activity.startActivityForResult(i, 78);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "NO HAY CÁMARAS DISPONIBLES", Toast.LENGTH_SHORT).show();
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

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    private void callCamera() {
        if (!isLocationEnabled(context)) {
            Toast.makeText(context, "EL GPS NO ESTA HABILITADO", Toast.LENGTH_LONG).show();
            Location location = Position.getLastBestLocation(context);
            if (location == null) {
                Toast.makeText(activity, "EL GPS NO ESTA HABILITADO", Toast.LENGTH_LONG).show();
            }
            return;
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMARA);
            } else {
                openCamera();
            }
        } else {
            openCamera();
        }
    }

    private void createGalleryServer(ArrayList<ImageModel> gallery, String idItem) {
        for (int i = 0; i < gallery.size(); i++) {
            if (gallery.get(i).getLabel().equals(LABEL_CHECK_IMAGE_ITEM)) {
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
    }

    private void createGallery(ArrayList<ImageModel> gallery, String idItem) {
        for (int i = 0; i < gallery.size(); i++) {
            if (gallery.get(i).getLabel().equals(LABEL_CHECK_IMAGE_ITEM)) {
                Bitmap imageBitmap;
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(gallery.get(i).getName()));
                    int origWidth = Objects.requireNonNull(imageBitmap).getWidth();
                    int origHeight = imageBitmap.getHeight();
                    int targetWidth = 400;
                    int targetHeight = 600;
                    PhotoEvidence photoEvidence;
                    if (origWidth <= origHeight) {
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
                                    idItem + "",
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
                    photoEvidence.setIdSql(gallery.get(i).getId() + "");
                    adapter.addElement(photoEvidence);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}