package com.smartory.views.fragments;

import static android.app.Activity.RESULT_OK;
import static com.smartory.adapters.ItemCheckListAdapter.isLocationEnabled;
import static com.smartory.db.SqliteClass.KEY_IMAORDNUM;
import static com.smartory.resource.HorizontalValues.LABEL_CHECK_IMAGE_ITEM;
import static com.smartory.resource.HorizontalValues.verifyTireFragment;
import static com.smartory.views.activities.FormNewCheckListActivity.MY_PERMISSIONS_REQUEST_CAMARA;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.smartory.R;
import com.smartory.adapters.PhotoAdapter;
import com.smartory.config.ConstValue;
import com.smartory.db.SqliteClass;
import com.smartory.interfaces.IChecklistProgress;
import com.smartory.models.CheckListModel;
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


public class VerificationTireFragment extends Fragment implements OnIntentReceived {
    public final String FRAGMENT_SECTION_ID = "8";
    EditText et_1, et_2, et_3, et_4, et_5, et_6, et_7, et_8, et_9, et_10, et_11, et_12, et_13, et_14, et_15, et_16, et_17, et_18, et_19, et_20, et_21, et_22, et_resp1, et_resp2;
    TextInputLayout text_input_1, text_input_2, text_input_3, text_input_4, text_input_5, text_input_6, text_input_7, text_input_8,
            text_input_9, text_input_10, text_input_11, text_input_12, text_input_13, text_input_14, text_input_15, text_input_16, text_input_17,
            text_input_18, text_input_19, text_input_20, text_input_21, text_input_22;
    ArrayList<ItemCheckListModel> itemsLlantas;
    LinearLayout layout_tire_c2c3;
    Context context;
    RecyclerView recyclerView;
    PhotoAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    List<PhotoEvidence> list;
    IChecklistProgress callbackProgress;

    public VerificationTireFragment() {
    }

    public void setCheckProgressListener(IChecklistProgress callbackProgress) {
        this.callbackProgress = callbackProgress;
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
            int targetWidth = 400; // your arbitrary fixed limit
            int targetHeight = 600; // your arbitrary fixed limit
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
                    location.getLongitude() + "",
                    location.getLatitude() + "",
                    new Date() + "",
                    sharedPref.getString("photo_type", "c")
            );
            adapter.addElement(photoEvidence);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verification_tire, container, false);
        context = getActivity();
        assert context != null;
        initialComponentsUI(view);
        updateComponentUI();
        return view;
    }

    private void initialComponentsUI(View view) {
        layout_tire_c2c3 = view.findViewById(R.id.layout_tire_c2c3);
        text_input_1 = view.findViewById(R.id.text_input_1);
        text_input_2 = view.findViewById(R.id.text_input_2);
        text_input_3 = view.findViewById(R.id.text_input_3);
        text_input_4 = view.findViewById(R.id.text_input_4);
        text_input_5 = view.findViewById(R.id.text_input_5);
        text_input_6 = view.findViewById(R.id.text_input_6);
        text_input_7 = view.findViewById(R.id.text_input_7);
        text_input_8 = view.findViewById(R.id.text_input_8);
        text_input_9 = view.findViewById(R.id.text_input_9);
        text_input_10 = view.findViewById(R.id.text_input_10);
        text_input_11 = view.findViewById(R.id.text_input_11);
        text_input_12 = view.findViewById(R.id.text_input_12);
        text_input_13 = view.findViewById(R.id.text_input_13);
        text_input_14 = view.findViewById(R.id.text_input_14);
        text_input_15 = view.findViewById(R.id.text_input_15);
        text_input_16 = view.findViewById(R.id.text_input_16);
        text_input_17 = view.findViewById(R.id.text_input_17);
        text_input_18 = view.findViewById(R.id.text_input_18);
        text_input_19 = view.findViewById(R.id.text_input_19);
        text_input_20 = view.findViewById(R.id.text_input_20);
        text_input_21 = view.findViewById(R.id.text_input_21);
        text_input_22 = view.findViewById(R.id.text_input_22);
        et_1 = view.findViewById(R.id.et_1);
        et_2 = view.findViewById(R.id.et_2);
        et_3 = view.findViewById(R.id.et_3);
        et_4 = view.findViewById(R.id.et_4);
        et_5 = view.findViewById(R.id.et_5);
        et_6 = view.findViewById(R.id.et_6);
        et_7 = view.findViewById(R.id.et_7);
        et_8 = view.findViewById(R.id.et_8);
        et_9 = view.findViewById(R.id.et_9);
        et_10 = view.findViewById(R.id.et_10);
        et_11 = view.findViewById(R.id.et_11);
        et_12 = view.findViewById(R.id.et_12);
        et_13 = view.findViewById(R.id.et_13);
        et_14 = view.findViewById(R.id.et_14);
        et_15 = view.findViewById(R.id.et_15);
        et_16 = view.findViewById(R.id.et_16);
        et_17 = view.findViewById(R.id.et_17);
        et_18 = view.findViewById(R.id.et_18);
        et_19 = view.findViewById(R.id.et_19);
        et_20 = view.findViewById(R.id.et_20);
        et_21 = view.findViewById(R.id.et_21);
        et_22 = view.findViewById(R.id.et_22);
        et_resp1 = view.findViewById(R.id.et_rep_1);
        et_resp2 = view.findViewById(R.id.et_rep_2);
    }

    private void updateComponentUI() {
        SharedPreferences sharedPref = context.getSharedPreferences("check_list", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("fragment", FRAGMENT_SECTION_ID);
        editor.apply();

        final CheckListModel checkListModel = SqliteClass.getInstance(context).databasehelp.checkListSql.getCheck(sharedPref.getString("id_check_list_sql", ""));
        itemsLlantas = SqliteClass.getInstance(getActivity()).databasehelp.itemSql.getItemsBySection(FRAGMENT_SECTION_ID, sharedPref.getString("id_check_list_sql", ""));

        if (itemsLlantas.size() > 0) {
            text_input_1.setVisibility(View.VISIBLE);
            et_1.setText(itemsLlantas.get(0).getState());
            et_1.setOnClickListener(v -> openDialog(itemsLlantas.get(0), et_1));
            text_input_2.setVisibility(View.VISIBLE);
            et_2.setText(itemsLlantas.get(1).getState());
            et_2.setOnClickListener(v -> openDialog(itemsLlantas.get(1), et_2));
            text_input_3.setVisibility(View.VISIBLE);
            et_3.setText(itemsLlantas.get(2).getState());
            et_3.setOnClickListener(v -> openDialog(itemsLlantas.get(2), et_3));
            text_input_4.setVisibility(View.VISIBLE);
            et_4.setText(itemsLlantas.get(3).getState());
            et_4.setOnClickListener(v -> openDialog(itemsLlantas.get(3), et_4));
            if (checkListModel.getUnit_configuration().equals("C3") ||
                    checkListModel.getUnit_configuration().equals("T3S3")) {
                text_input_5.setVisibility(View.VISIBLE);
                et_5.setText(itemsLlantas.get(4).getState());
                et_5.setOnClickListener(v -> openDialog(itemsLlantas.get(4), et_5));
                text_input_6.setVisibility(View.VISIBLE);
                et_6.setText(itemsLlantas.get(5).getState());
                et_6.setOnClickListener(v -> openDialog(itemsLlantas.get(5), et_6));
            }
            if (checkListModel.getUnit_configuration().equals("T3S3")) {
                layout_tire_c2c3.setVisibility(View.GONE);
                text_input_7.setVisibility(View.VISIBLE);
                et_7.setText(itemsLlantas.get(6).getState());
                et_7.setOnClickListener(v -> openDialog(itemsLlantas.get(6), et_7));
                text_input_8.setVisibility(View.VISIBLE);
                et_8.setText(itemsLlantas.get(7).getState());
                et_8.setOnClickListener(v -> openDialog(itemsLlantas.get(7), et_8));
                text_input_9.setVisibility(View.VISIBLE);
                et_9.setText(itemsLlantas.get(8).getState());
                et_9.setOnClickListener(v -> openDialog(itemsLlantas.get(8), et_9));
                text_input_10.setVisibility(View.VISIBLE);
                et_10.setText(itemsLlantas.get(9).getState());
                et_10.setOnClickListener(v -> openDialog(itemsLlantas.get(9), et_10));
                text_input_11.setVisibility(View.VISIBLE);
                et_11.setText(itemsLlantas.get(10).getState());
                et_11.setOnClickListener(v -> openDialog(itemsLlantas.get(10), et_11));
                text_input_12.setVisibility(View.VISIBLE);
                et_12.setText(itemsLlantas.get(11).getState());
                et_12.setOnClickListener(v -> openDialog(itemsLlantas.get(11), et_12));
                text_input_13.setVisibility(View.VISIBLE);
                et_13.setText(itemsLlantas.get(12).getState());
                et_13.setOnClickListener(v -> openDialog(itemsLlantas.get(12), et_13));
                text_input_14.setVisibility(View.VISIBLE);
                et_14.setText(itemsLlantas.get(13).getState());
                et_14.setOnClickListener(v -> openDialog(itemsLlantas.get(13), et_14));
                text_input_15.setVisibility(View.VISIBLE);
                et_15.setText(itemsLlantas.get(14).getState());
                et_15.setOnClickListener(v -> openDialog(itemsLlantas.get(14), et_15));
                text_input_16.setVisibility(View.VISIBLE);
                et_16.setText(itemsLlantas.get(15).getState());
                et_16.setOnClickListener(v -> openDialog(itemsLlantas.get(15), et_16));
                text_input_17.setVisibility(View.VISIBLE);
                et_17.setText(itemsLlantas.get(16).getState());
                et_17.setOnClickListener(v -> openDialog(itemsLlantas.get(16), et_17));
                text_input_18.setVisibility(View.VISIBLE);
                et_18.setText(itemsLlantas.get(17).getState());
                et_18.setOnClickListener(v -> openDialog(itemsLlantas.get(17), et_18));
                text_input_19.setVisibility(View.VISIBLE);
                et_19.setText(itemsLlantas.get(18).getState());
                et_19.setOnClickListener(v -> openDialog(itemsLlantas.get(18), et_19));
                text_input_20.setVisibility(View.VISIBLE);
                et_20.setText(itemsLlantas.get(19).getState());
                et_20.setOnClickListener(v -> openDialog(itemsLlantas.get(19), et_20));
                text_input_21.setVisibility(View.VISIBLE);
                et_21.setText(itemsLlantas.get(20).getState());
                et_21.setOnClickListener(v -> openDialog(itemsLlantas.get(20), et_21));
                text_input_22.setVisibility(View.VISIBLE);
                et_22.setText(itemsLlantas.get(21).getState());
                et_22.setOnClickListener(v -> openDialog(itemsLlantas.get(21), et_22));
            }
            et_resp1.setText(itemsLlantas.get(22).getState());
            et_resp1.setOnClickListener(v -> openDialog(itemsLlantas.get(22), et_resp1));
            et_resp2.setText(itemsLlantas.get(23).getState());
            et_resp2.setOnClickListener(v -> openDialog(itemsLlantas.get(23), et_resp2));
        }
    }

    private void callCamera() {
        if (!isLocationEnabled(context)) {
            Toast.makeText(context, "EL GPS NO ESTA HABILITADO", Toast.LENGTH_LONG).show();
            Location location = Position.getLastBestLocation(context);
            if (location == null) {
                Toast.makeText(context, "EL GPS NO ESTA HABILITADO", Toast.LENGTH_LONG).show();
            }
            return;
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMARA);
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
            verifyTireFragment = this;
            Objects.requireNonNull(getActivity()).startActivityForResult(i, 80);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "NO HAY CÁMARAS DISPONIBLES", Toast.LENGTH_SHORT).show();
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
                    adapter.addElement(photoEvidence);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        File storageDir = Objects.requireNonNull(context).getExternalFilesDir(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        HorizontalValues.photoCompliance = "file:" + image.getAbsolutePath();
        return image;
    }

    @SuppressLint("SetTextI18n")
    private void openDialog(ItemCheckListModel itemCheckListModel, TextView tv_state) {
        AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_check_item, null);
        Button btnFire = view.findViewById(R.id.btn_ok);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        EditText et_obs = view.findViewById(R.id.et_observaciones);
        LinearLayout layout_register_photo = view.findViewById(R.id.layout_register_photo);
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
        RadioButton checkbox_c, checkbox_n_c, checkbox_n_a;
        Button btn_compliace_image = view.findViewById(R.id.take_photo);
        SharedPreferences sharedPref = context.getSharedPreferences("check_list", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("idItem", String.valueOf(itemCheckListModel.getIdSql()));
        editor.apply();
        checkbox_c = view.findViewById(R.id.checkbox_c);
        checkbox_n_c = view.findViewById(R.id.checkbox_n_c);
        checkbox_n_a = view.findViewById(R.id.checkbox_n_a);
        if (sharedPref.getString("opcion", "").equals(ConstValue.VIEW)) {
            btnFire.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            et_obs.setEnabled(false);
            checkbox_c.setEnabled(false);
            checkbox_n_c.setEnabled(false);
            checkbox_n_a.setEnabled(false);
            btn_compliace_image.setEnabled(false);
        } else {
            btnFire.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
            et_obs.setEnabled(true);
            checkbox_c.setEnabled(true);
            checkbox_n_c.setEnabled(true);
            checkbox_n_a.setEnabled(true);
            btn_compliace_image.setEnabled(true);
        }
        ItemCheckListModel item = SqliteClass.getInstance(context).databasehelp.itemSql.getItemById(String.valueOf(itemCheckListModel.getIdSql()));
        switch (item.getState()) {
            case "C":
                checkbox_c.setChecked(true);
                break;
            case "NC":
                checkbox_n_c.setChecked(true);
                break;
            case "NA":
                checkbox_n_a.setChecked(true);
                btn_compliace_image.setEnabled(false);
                break;
        }
        checkbox_c.setOnCheckedChangeListener((buttonView, isChecked) -> { // change radio buttons
            if (isChecked) {
                checkbox_n_c.setChecked(false);
                checkbox_n_a.setChecked(false);
                btn_compliace_image.setEnabled(true);
                layout_register_photo.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
        checkbox_n_c.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkbox_c.setChecked(false);
                checkbox_n_a.setChecked(false);
                btn_compliace_image.setEnabled(true);
                layout_register_photo.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
        checkbox_n_a.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkbox_n_c.setChecked(false);
                checkbox_c.setChecked(false);
                btn_compliace_image.setEnabled(false);
                layout_register_photo.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }
        });
        btn_compliace_image.setOnClickListener(v1 -> callCamera());
        et_obs.setText(item.getObservation_description());
        final String[] observation = {""};
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
                    observation[0] = "";
                } else {
                    observation[0] = s.toString();
                }
            }
        });
        builder.setView(view);
        alertDialog = builder.create();
        if (sharedPref.getString("type", "").equals("cloud")) { // Add action buttons
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
                        tv_state.setText("C");
                    } else if (checkbox_n_c.isChecked()) {
                        SqliteClass.getInstance(context).databasehelp.itemSql.updateValueItemById(
                                SqliteClass.KEY_ITEMSTA,
                                "NC",
                                String.valueOf(itemCheckListModel.getIdSql()));
                        tv_state.setText("NC");
                    } else if (checkbox_n_a.isChecked()) {
                        SqliteClass.getInstance(context).databasehelp.itemSql.updateValueItemById(
                                SqliteClass.KEY_ITEMSTA,
                                "NA",
                                String.valueOf(itemCheckListModel.getIdSql()));
                        tv_state.setText("NA");
                        list.clear();
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
                    if(callbackProgress != null) {
                        callbackProgress.updateProgress(itemCheckListModel.getId_check_list());
                    }
                    alertDialog.dismiss();
                    Intent intent = new Intent(getActivity(), CheckListFormActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("fragment", "4");
                    intent.putExtra("response", "check_adapter");
                    Objects.requireNonNull(getActivity()).startActivity(intent);
                }
        );
        btnCancel.setOnClickListener(v13 -> alertDialog.dismiss());
        alertDialog.show();
    }

    public void updateItemState() {
        updateComponentUI();
    }
}
