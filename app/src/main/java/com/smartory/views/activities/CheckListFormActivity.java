package com.smartory.views.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.smartory.BuildConfig;
import com.smartory.R;
import com.smartory.adapters.TabLayoutAdapter;
import com.smartory.config.ConstValue;
import com.smartory.db.SqliteClass;
import com.smartory.enums.ETypeChecklist;
import com.smartory.interfaces.IChecklistProgress;
import com.smartory.models.ItemCheckListModel;
import com.smartory.resource.HorizontalValues;
import com.smartory.resource.OnIntentReceived;
import com.smartory.utils.PictureUtils;
import com.smartory.utils.Util;
import com.smartory.views.fragments.CameraSecurityFragment;
import com.smartory.views.fragments.DocumentDriverFragment;
import com.smartory.views.fragments.DocumentUnitFragment;
import com.smartory.views.fragments.EPPFragment;
import com.smartory.views.fragments.KitEmergencyFragment;
import com.smartory.views.fragments.MedicalKITCabinFragment;
import com.smartory.views.fragments.ObservationStructureFragment;
import com.smartory.views.fragments.RevisionCisternFragment;
import com.smartory.views.fragments.RevisionTractoFragment;
import com.smartory.views.fragments.MaterialCleanFragment;
import com.smartory.views.fragments.SystemLoadingFragment;
import com.smartory.views.fragments.VerificationTireFragment;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.smartory.resource.HorizontalValues.imageView_preview_on_dialog;
import static com.smartory.resource.HorizontalValues.itemCheckListAdapter;
import static com.smartory.resource.HorizontalValues.verifyTireFragment;
import static com.smartory.views.activities.FormNewCheckListActivity.MY_PERMISSIONS_REQUEST_CAMARA;

public class CheckListFormActivity extends AppCompatActivity implements IChecklistProgress {

    @SuppressLint("StaticFieldLeak")
    static Context context;

    List<String> fragmentsTitle = Arrays.asList(
            "DOCUMENTOS DEL CONDUCTOR",
            "EQUIPOS DE PROTECCIÓN PERSONAL",
            "DOCUMENTOS DE LA UNIDAD",
            "REVISIÓN DEL TRACTO",
            "CAMARAS DE SEGURIDAD EN TRACTO",
            "BOTIQUIN EN CABINA",
            "SUMINISTROS Y/O ELEMENTOS DE LIMPIEZA",
            "REVISIÓN DE LA CISTERNA",
            "SISTEMA BOTTOM LOADING",
            "OBSERVACIONES ESTRUCTURALES",
            "KIT ANTIDERRAME DE LA CISTERNA",
            "VERIFICACIÓN DE LLANTAS"
    );

    TextView labelProgress;
    ProgressBar checklistProgressBar;
    Button btnTodosCumple, btnTodosNoAplica;
    LinearLayout grupoTodos;

    ViewPager viewPager;
    TabLayout tabs;
    TabLayoutAdapter sectionsPageAdapter;
    FloatingActionButton fltn_btn_check;
    DocumentDriverFragment fragmentDocumentDriver = new DocumentDriverFragment();
    DocumentUnitFragment fragmentDocumentUnit = new DocumentUnitFragment();
    RevisionTractoFragment fragmentRevisionTracto = new RevisionTractoFragment();
    RevisionCisternFragment fragmentRevisionCistern = new RevisionCisternFragment();
    KitEmergencyFragment fragmentKitEmergency = new KitEmergencyFragment();
    EPPFragment fragmentEPP = new EPPFragment();
    MaterialCleanFragment fragmentMaterialClean = new MaterialCleanFragment();
    MedicalKITCabinFragment fragmentMedicalKITCabin = new MedicalKITCabinFragment();
    CameraSecurityFragment fragmentCameraSecurity = new CameraSecurityFragment();
    VerificationTireFragment fragmentVerificationTire = new VerificationTireFragment();
    ObservationStructureFragment fragmentObservationStructure = new ObservationStructureFragment();
    SystemLoadingFragment fragmentSystemLoading = new SystemLoadingFragment();
    // StructuresFragment fragmentStructures = new StructuresFragment();


    List<Fragment> fragments = Arrays.asList(
            fragmentDocumentDriver,
            fragmentEPP,
            fragmentDocumentUnit,
            fragmentRevisionTracto,
            fragmentCameraSecurity,
            fragmentMedicalKITCabin,
            fragmentMaterialClean,
            fragmentRevisionCistern,
            fragmentSystemLoading,
            fragmentObservationStructure,
            fragmentKitEmergency,
            fragmentVerificationTire
    );

    String[] fragmentIds = {
        fragmentDocumentDriver.FRAGMENT_SECTION_ID,
        fragmentEPP.FRAGMENT_SECTION_ID,
        fragmentDocumentUnit.FRAGMENT_SECTION_ID,
        fragmentRevisionTracto.FRAGMENT_SECTION_ID,
        fragmentCameraSecurity.FRAGMENT_SECTION_ID,
        fragmentMedicalKITCabin.FRAGMENT_SECTION_ID,
        fragmentMaterialClean.FRAGMENT_SECTION_ID,
        fragmentRevisionCistern.FRAGMENT_SECTION_ID,
        fragmentSystemLoading.FRAGMENT_SECTION_ID,
        fragmentObservationStructure.FRAGMENT_SECTION_ID,
        fragmentKitEmergency.FRAGMENT_SECTION_ID,
        fragmentVerificationTire.FRAGMENT_SECTION_ID
    };

    private int selectedFragmentPosition = 0;

    ArrayList<String> nameSection;
    private SqliteClass.DatabaseHelper SQL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list_form);
        SharedPreferences sharedPrefCheck = getSharedPreferences("check_list", Context.MODE_PRIVATE);
        Objects.requireNonNull(getSupportActionBar()).setTitle(sharedPrefCheck.getString("number_check_list", ""));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        ConstValue.enablePermission(this, context);
        nameSection = new ArrayList<>();
        SQL = SqliteClass.getInstance(context).databasehelp;
        labelProgress = findViewById(R.id.labelProgress);
        checklistProgressBar = findViewById(R.id.checklistProgressBar);
        btnTodosCumple = findViewById(R.id.todosCumple);
        btnTodosNoAplica = findViewById(R.id.todosNoAplica);
        grupoTodos = findViewById(R.id.grupoTodos);
        tabs = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.view_pager);

        addFragments();

        SharedPreferences sharedPref = getSharedPreferences("check_list", Context.MODE_PRIVATE);
        fltn_btn_check = findViewById(R.id.fltn_btn_validate);
        if (sharedPref.getString("opcion", "").equals(ConstValue.VIEW)) {
            fltn_btn_check.setVisibility(View.GONE);
            grupoTodos.setVisibility(View.GONE);
        }
        fltn_btn_check.setOnClickListener(v -> {
            if (!sharedPref.getString("opcion", "").equals(ConstValue.VIEW)) {
                Intent intent = new Intent(CheckListFormActivity.this, CheckListValidationActivity.class);
                SharedPreferences sharedPrefCheckAux = context.getSharedPreferences("check_list", Context.MODE_PRIVATE);
                startActivity(intent);
                SQL.checkListSql.updateValueCheckListById(
                        SqliteClass.KEY_CHECDAT,
                        Util.getCurrentDate(),
                        sharedPrefCheckAux.getString("id_check_list_sql", ""));
            }
        });

        btnTodosCumple.setOnClickListener(v -> {
            changeAllTypeSection(ETypeChecklist.C);
        });

        btnTodosNoAplica.setOnClickListener(v -> {
            changeAllTypeSection(ETypeChecklist.NA);
        });

        updateProgress(sharedPref.getString("id_check_list_sql", ""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_action_checklist, menu);
        menu.findItem(R.id.action_version).setTitle(BuildConfig.VERSION_NAME);

        MenuItem action = menu.findItem(R.id.action_checklist);

        SearchView searchView = (SearchView) action.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    selectedTabItem(query);
                    searchView.setQuery("", false);
                    searchView.setIconified(true);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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

    @Override
    public void onBackPressed() {
        finish();
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
        if (requestCode == 80) {
            OnIntentReceived mIntentListener2 = verifyTireFragment;
            if (mIntentListener2 != null) {
                mIntentListener2.onIntent(data, resultCode);
            }
        }
        if (requestCode == 78) {
            OnIntentReceived mIntentListener = itemCheckListAdapter;
            if (mIntentListener != null) {
                mIntentListener.onIntent(data, resultCode);
            }
        }
        if (requestCode == 79) {
            if (imageView_preview_on_dialog != null) {
                if (resultCode == RESULT_OK) {
                    Bitmap imageBitmap = null;
                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(HorizontalValues.photoCompliance));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    assert imageBitmap != null;
                    int origWidth = imageBitmap.getWidth();
                    int origHeight = imageBitmap.getHeight();
                    int targetWidth = 400; // your arbitrary fixed limit
                    int targetHeight = 600; // your arbitrary fixed limit
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
                    imageView_preview_on_dialog.setImageBitmap(imageBitmap);
                    SharedPreferences sharedPref = context.getSharedPreferences("check_list", Context.MODE_PRIVATE);
                    String idItem = sharedPref.getString("idItem", "");
                    switch (sharedPref.getString("photo_type", "c")) {
                        case "nc":
                            SQL.itemSql.updateValueItemById(
                                    SqliteClass.KEY_ITEMNONCOMIMA,
                                    PictureUtils.getBase64String(imageBitmap),
                                    idItem);
                            break;
                        case "c":
                            SQL.itemSql.updateValueItemById(
                                    SqliteClass.KEY_ITEMCOMIMA,
                                    PictureUtils.getBase64String(imageBitmap),
                                    idItem);
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof DocumentDriverFragment) {
            DocumentDriverFragment fragmentAux = (DocumentDriverFragment) fragment;
            fragmentAux.setCheckProgressListener(this);
        } else if (fragment instanceof EPPFragment) {
            EPPFragment fragmentAux = (EPPFragment) fragment;
            fragmentAux.setCheckProgressListener(this);
        } else if (fragment instanceof CameraSecurityFragment) {
            CameraSecurityFragment fragmentAux = (CameraSecurityFragment) fragment;
            fragmentAux.setCheckProgressListener(this);
        } else if (fragment instanceof DocumentUnitFragment) {
            DocumentUnitFragment fragmentAux = (DocumentUnitFragment) fragment;
            fragmentAux.setCheckProgressListener(this);
        } else if (fragment instanceof KitEmergencyFragment) {
            KitEmergencyFragment fragmentAux = (KitEmergencyFragment) fragment;
            fragmentAux.setCheckProgressListener(this);
        } else if (fragment instanceof MaterialCleanFragment) {
            MaterialCleanFragment fragmentAux = (MaterialCleanFragment) fragment;
            fragmentAux.setCheckProgressListener(this);
        } else if (fragment instanceof MedicalKITCabinFragment) {
            MedicalKITCabinFragment fragmentAux = (MedicalKITCabinFragment) fragment;
            fragmentAux.setCheckProgressListener(this);
        } else if (fragment instanceof RevisionCisternFragment) {
            RevisionCisternFragment fragmentAux = (RevisionCisternFragment) fragment;
            fragmentAux.setCheckProgressListener(this);
        } else if (fragment instanceof RevisionTractoFragment) {
            RevisionTractoFragment fragmentAux = (RevisionTractoFragment) fragment;
            fragmentAux.setCheckProgressListener(this);
        } else if (fragment instanceof SystemLoadingFragment) {
            SystemLoadingFragment fragmentAux = (SystemLoadingFragment) fragment;
            fragmentAux.setCheckProgressListener(this);
        } else if (fragment instanceof VerificationTireFragment) {
            VerificationTireFragment fragmentAux = (VerificationTireFragment) fragment;
            fragmentAux.setCheckProgressListener(this);
        } else if (fragment instanceof ObservationStructureFragment) {
            ObservationStructureFragment fragmentAux = (ObservationStructureFragment) fragment;
            fragmentAux.setCheckProgressListener(this);
        }
    }

    @Override
    public void updateProgress(String idChecklist) {
        ArrayList<ItemCheckListModel> items = SQL.itemSql.getAllItemsByCheck(idChecklist);
        int total = items.size();
        int countReviewed = 0;
        int progress = 0;

        for (ItemCheckListModel item : items) {
            if (item.getReviewed()) {
                countReviewed++;
            }
        }

        if (total == 0) {
            progress = 0;
        } else {
            progress = (100 * countReviewed) / total;
        }

        labelProgress.setText(progress + "%");
        checklistProgressBar.setProgress(progress);

        SqliteClass.getInstance(context).databasehelp.checkListSql
                .updateValueCheckListById(SqliteClass.KEY_CHECPROGRESS, "" + progress, idChecklist);
    }

    public void selectedTabItem(String query) {
        int posicion = -1;
        //Buscar Titulos
        for (int i = 0; i < fragmentsTitle.size(); i++) {
            String title = fragmentsTitle.get(i).toUpperCase();
            if (title.contains(query.toUpperCase())) {
                posicion = i;
                break;
            }
        }

        //Buscar Items
        if (posicion == -1) {
            posicion = searchAllItems(query);
        }

        TabLayout.Tab compartmentTab;
        if (posicion > -1) {
            compartmentTab = tabs.getTabAt(posicion);
            Objects.requireNonNull(compartmentTab).select();
        }
    }

    public int searchAllItems(String query) {
        int position = -1;
        SharedPreferences sharedPref = context.getSharedPreferences("check_list", Context.MODE_PRIVATE);
        String idChecklist = sharedPref.getString("id_check_list_sql", "");

        ArrayList<ItemCheckListModel> allItems = SqliteClass.getInstance(context).databasehelp.itemSql.getAllItemsByCheck(idChecklist);

        ItemCheckListModel itemFound = null;
        for (ItemCheckListModel item : allItems) {
            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                itemFound = item;
                break;
            }
        }

        String idSection = "";
        if(itemFound != null) {
            idSection = itemFound.getId_section();
            position = Arrays.asList(fragmentIds).indexOf(idSection);
        }

        return position;
    }

    public void changeAllTypeSection(ETypeChecklist type) {
        SharedPreferences sharedPref = context.getSharedPreferences("check_list", Context.MODE_PRIVATE);
        String idChecklist = sharedPref.getString("id_check_list_sql", "");
        String idSection = sharedPref.getString("currentIdSection", "1");

        //Toast.makeText(getApplicationContext(), type.toString() + "---> Id: " + idChecklist + " -- Section: " + idSection, Toast.LENGTH_SHORT).show();

        ArrayList<ItemCheckListModel> items = SqliteClass.getInstance(context).databasehelp.itemSql.getItemsBySection(idSection, idChecklist);

        for (ItemCheckListModel item : items) {
            SqliteClass.getInstance(context).databasehelp.itemSql.updateValueItemById(
                    SqliteClass.KEY_ITEMEDI,
                    "si",
                    String.valueOf(item.getIdSql()));
            SqliteClass.getInstance(context).databasehelp.itemSql.updateValueItemById(
                    SqliteClass.KEY_ITEMREVIEWED,
                    "1",
                    String.valueOf(item.getIdSql()));

            SqliteClass.getInstance(context).databasehelp.itemSql.updateValueItemById(
                    SqliteClass.KEY_ITEMSTA,
                    type.toString(),
                    String.valueOf(item.getIdSql()));
        }

        updateFragment();
        updateProgress(idChecklist);
    }

    private void updateFragment() {
        Fragment fragment = fragments.get(selectedFragmentPosition);
        if (fragment instanceof DocumentDriverFragment) {
            ((DocumentDriverFragment) fragment).updateItemState();
        } else if (fragment instanceof DocumentUnitFragment) {
            ((DocumentUnitFragment) fragment).updateItemState();
        } else if (fragment instanceof RevisionTractoFragment) {
            ((RevisionTractoFragment) fragment).updateItemState();
        } else if (fragment instanceof RevisionCisternFragment) {
            ((RevisionCisternFragment) fragment).updateItemState();
        } else if (fragment instanceof KitEmergencyFragment) {
            ((KitEmergencyFragment) fragment).updateItemState();
        } else if (fragment instanceof EPPFragment) {
            ((EPPFragment) fragment).updateItemState();
        } else if (fragment instanceof MaterialCleanFragment) {
            ((MaterialCleanFragment) fragment).updateItemState();
        } else if (fragment instanceof MedicalKITCabinFragment) {
            ((MedicalKITCabinFragment) fragment).updateItemState();
        } else if (fragment instanceof CameraSecurityFragment) {
            ((CameraSecurityFragment) fragment).updateItemState();
        } else if (fragment instanceof VerificationTireFragment) {
            ((VerificationTireFragment) fragment).updateItemState();
        } else if (fragment instanceof ObservationStructureFragment) {
            ((ObservationStructureFragment) fragment).updateItemState();
        } else if (fragment instanceof SystemLoadingFragment) {
            ((SystemLoadingFragment) fragment).updateItemState();
        }
    }

    private void addFragments() {
        sectionsPageAdapter = new TabLayoutAdapter(getSupportFragmentManager());

        int i = 0;
        for (Fragment fragment : fragments) {
            sectionsPageAdapter.addFragment(fragment, fragmentsTitle.get(i));
            i++;
        }

        SharedPreferences sharedPref = context.getSharedPreferences("check_list", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("currentIdSection", "1");
        editor.apply();

        viewPager.setAdapter(sectionsPageAdapter);
        tabs.setupWithViewPager(viewPager);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedFragmentPosition = tab.getPosition();
                Fragment fragment = fragments.get(selectedFragmentPosition);
                String idSection = "";

                if (fragment instanceof DocumentDriverFragment) {
                    DocumentDriverFragment selectedFragment = (DocumentDriverFragment) fragment;
                    idSection = selectedFragment.FRAGMENT_SECTION_ID;
                } else if (fragment instanceof DocumentUnitFragment) {
                    DocumentUnitFragment selectedFragment = (DocumentUnitFragment) fragment;
                    idSection = selectedFragment.FRAGMENT_SECTION_ID;
                } else if (fragment instanceof RevisionTractoFragment) {
                    RevisionTractoFragment selectedFragment = (RevisionTractoFragment) fragment;
                    idSection = selectedFragment.FRAGMENT_SECTION_ID;
                } else if (fragment instanceof RevisionCisternFragment) {
                    RevisionCisternFragment selectedFragment = (RevisionCisternFragment) fragment;
                    idSection = selectedFragment.FRAGMENT_SECTION_ID;
                } else if (fragment instanceof KitEmergencyFragment) {
                    KitEmergencyFragment selectedFragment = (KitEmergencyFragment) fragment;
                    idSection = selectedFragment.FRAGMENT_SECTION_ID;
                } else if (fragment instanceof EPPFragment) {
                    EPPFragment selectedFragment = (EPPFragment) fragment;
                    idSection = selectedFragment.FRAGMENT_SECTION_ID;
                } else if (fragment instanceof MaterialCleanFragment) {
                    MaterialCleanFragment selectedFragment = (MaterialCleanFragment) fragment;
                    idSection = selectedFragment.FRAGMENT_SECTION_ID;
                } else if (fragment instanceof MedicalKITCabinFragment) {
                    MedicalKITCabinFragment selectedFragment = (MedicalKITCabinFragment) fragment;
                    idSection = selectedFragment.FRAGMENT_SECTION_ID;
                } else if (fragment instanceof CameraSecurityFragment) {
                    CameraSecurityFragment selectedFragment = (CameraSecurityFragment) fragment;
                    idSection = selectedFragment.FRAGMENT_SECTION_ID;
                } else if (fragment instanceof VerificationTireFragment) {
                    VerificationTireFragment selectedFragment = (VerificationTireFragment) fragment;
                    idSection = selectedFragment.FRAGMENT_SECTION_ID;
                } else if (fragment instanceof ObservationStructureFragment) {
                    ObservationStructureFragment selectedFragment = (ObservationStructureFragment) fragment;
                    idSection = selectedFragment.FRAGMENT_SECTION_ID;
                } else if (fragment instanceof SystemLoadingFragment) {
                    SystemLoadingFragment selectedFragment = (SystemLoadingFragment) fragment;
                    idSection = selectedFragment.FRAGMENT_SECTION_ID;
                }

                SharedPreferences sharedPref = context.getSharedPreferences("check_list", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("currentIdSection", idSection);
                editor.apply();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
