package com.smartory.views.activities;

import static java.lang.Integer.parseInt;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.smartory.BuildConfig;
import com.smartory.R;
import com.smartory.db.SqliteClass;
import com.smartory.models.CarrierModel;
import com.smartory.models.CheckListModel;
import com.smartory.models.DriverModel;
import com.smartory.models.GetCheckListModel;
import com.smartory.models.InspectorModel;
import com.smartory.models.ItemCheckListModel;
import com.smartory.models.LocationModel;
import com.smartory.models.OperacionModel;
import com.smartory.models.PaginationModel;
import com.smartory.models.RouteModel;
import com.smartory.models.SupervisorModel;
import com.smartory.models.TractoModel;
import com.smartory.models.TransportUnitModel;
import com.smartory.network.InterfaceAPI;
import com.smartory.network.RetrofitClientInstance;
import com.smartory.utils.ConnectionDetector;
import com.smartory.utils.PictureUtils;
import com.smartory.utils.Util;
import com.smartory.views.dialogs.CommonDialogs;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import io.sentry.Sentry;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FormNewCheckListActivity extends AppCompatActivity {
    static Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
    static final InterfaceAPI api = retrofit.create(InterfaceAPI.class);
    Spinner spnr_type_checklist, spnr_unit_configuration, spnr_type_operation, spnr_company, spnr_supervisor,
            spnr_inspector, spnr_driver, spnr_unit, spnr_type_route;
    EditText et_date, et_hora, et_mileage, et_manufactoring_year, et_last_checklist;
    TextView edittext_supervisor, edittext_inspector;
    Button btn_create, btn_cancel, btn_tracto_image, btn_cistern_image;
    Context context = this;
    Activity activity = this;
    ArrayList<String> type_checklist;
    ArrayList<String> type_unit_configuration;
    ArrayList<OperacionModel> operationModels = new ArrayList<>();
    ArrayList<SupervisorModel> supervisorModels = new ArrayList<>();
    ArrayList<CarrierModel> carrierModels = new ArrayList<>();
    ArrayList<InspectorModel> inspectorModels = new ArrayList<>();
    ArrayList<LocationModel> locationModels = new ArrayList<>();
    ArrayList<DriverModel> driverModels = new ArrayList<>();
    ArrayList<RouteModel> routeModels = new ArrayList<>();
    ArrayList<TransportUnitModel> unitModels = new ArrayList<>();
    ArrayList<ItemCheckListModel> itemCheckModels = new ArrayList<>();
    public ArrayList<String> nameSupervisor = new ArrayList<>();
    public ArrayList<String> nameInspector = new ArrayList<>();
    public ArrayList<String> nameLocation = new ArrayList<>();
    public ArrayList<String> nameDrivers = new ArrayList<>();
    public ArrayList<String> nameRoutes = new ArrayList<>();
    public ArrayList<String> nameUnits = new ArrayList<>();
    public ArrayList<String> nameCompanies = new ArrayList<>();
    ArrayList<String> nameOperations = new ArrayList<>();
    ArrayAdapter<String> adapterSupervisor;
    ArrayAdapter<String> adapterInspector;
    ArrayAdapter<String> adapterDrivers;
    ArrayAdapter<String> adapterRoutes;
    ArrayAdapter<String> adapterUnits;
    ArrayAdapter<String> adapterCompanies;
    String pkOperation = "";
    String operationString = "0", company = "0", sup = "0", inspectorString = "0", dri = "0", unit = "0", rout = "0";
    CheckListModel checkListModel = new CheckListModel();
    TextView image_no_available;
    ProgressDialog dialog;
    ConnectionDetector cd;
    String hourString, dateString;
    static final int REQUEST_IMAGE_CAPTURE_TRACTO = 1;
    static final int REQUEST_IMAGE_CAPTURE_CISTERN = 2;
    public static final int MY_PERMISSIONS_REQUEST_CAMARA = 0;
    String type_car = "";
    ImageView image_tracto_view;
    ImageView image_cistern_view;
    String employee_name, type, company_name;
    Boolean is_staff, is_superuser;
    Boolean is_group_supervisor, is_group_inspector, is_group_sub_employee, is_group_staff, is_group_sub_staff, is_group_driver;
    int company_id;
    String mCurrentPhotoPath;
    LocationManager locationManager;
    double longitudeBest, latitudeBest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_new_check_list);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences sharedPref1 = getSharedPreferences("check_list", Context.MODE_PRIVATE);
        SharedPreferences sharedPref = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        toggleBestUpdates();
        employee_name = sharedPref.getString("employee_name", "");
        type = sharedPref.getString("type_user", "");
        is_staff = sharedPref.getBoolean("is_staff", false);
        is_superuser = sharedPref.getBoolean("is_superuser", false);
        company_name = sharedPref.getString("company_name", "");
        company_id = sharedPref.getInt("company_id", 0);
        is_group_supervisor = sharedPref.getBoolean("is_group_supervisor", false);
        is_group_inspector = sharedPref.getBoolean("is_group_inspector", false);
        is_group_sub_employee = sharedPref.getBoolean("is_group_sub_employee", false);
        is_group_staff = sharedPref.getBoolean("is_group_staff", false);
        is_group_sub_staff = sharedPref.getBoolean("is_group_sub_staff", false);
        is_group_driver = sharedPref.getBoolean("is_group_driver", false);
        SharedPreferences.Editor editor = sharedPref.edit();
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor1 = sharedPref1.edit();
        cd = new ConnectionDetector(context);
        image_no_available = findViewById(R.id.image_no_available);
        image_tracto_view = findViewById(R.id.image_tracto_view); //AREA HOUR Y DATE
        image_cistern_view = findViewById(R.id.image_cistern_view);
        edittext_supervisor = findViewById(R.id.edittext_supervisor);
        edittext_inspector = findViewById(R.id.edittext_inspector);
        et_date = findViewById(R.id.et_fecha);
        et_date.setText(dateString);
        et_hora = findViewById(R.id.et_hora);
        et_hora.setText(hourString);
        et_mileage = findViewById(R.id.et_kilometraje);
        et_manufactoring_year = findViewById(R.id.et_manufactoring_year);
        et_last_checklist = findViewById(R.id.et_last_checklist);
        checkListModel.setDatetime("");
        checkListModel.setTracto_image("");
        checkListModel.setCistern_image("");
        et_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkListModel.setDatetime(s.toString() + " / " + et_hora.getText().toString());
            }
        });
        et_date.setOnClickListener(v -> CommonDialogs.dialogPickerFecha(activity, et_date));
        et_hora.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkListModel.setDatetime(et_date.getText().toString() + " / " + s.toString());
            }
        });
        et_hora.setOnClickListener(v -> CommonDialogs.dialogPickerHoraFormat24h(activity, et_hora));
        //AREA KILOMETERS
        et_mileage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    checkListModel.setMileage(s.toString());
                }
            }
        });
        spnr_unit_configuration = findViewById(R.id.spnr_unit_configuration);
        spnr_type_checklist = findViewById(R.id.spnr_tipe_check);
        spnr_supervisor = findViewById(R.id.spnr_supervisor);
        spnr_inspector = findViewById(R.id.spnr_inspector);
        spnr_type_operation = findViewById(R.id.spnr_tipe_operation);
        spnr_driver = findViewById(R.id.spnr_driver);
        spnr_unit = findViewById(R.id.spnr_unit);
        spnr_company = findViewById(R.id.spnr_company);
        spnr_type_route = findViewById(R.id.spnr_tipe_route);
        nameOperations.add("Seleccione");
        nameSupervisor.add("Seleccione");
        nameInspector.add("Seleccione");
        nameLocation.add("Seleccione");
        nameDrivers.add("Seleccione");
        nameUnits.add("Seleccione");
        nameRoutes.add("Seleccione");
        nameCompanies.add("Seleccione");
        type_checklist = new ArrayList<>(); //AREA TYPE CHECKLIST
        type_checklist.add("PRECARGA");
        type_checklist.add("DESCARGA");
        operationModels = SqliteClass.getInstance(context).databasehelp.operacionSql.getAllOperations();
        if (is_superuser || is_staff || is_group_staff) {
            type_checklist.add("INSPECCIÓN");
        }
        spnr_type_checklist.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, type_checklist));
        spnr_type_checklist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkListModel.setType_check(type_checklist.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        type_unit_configuration = new ArrayList<>(); //AREA TYPE CHECKLIST
        type_unit_configuration.add("T3S3");
        type_unit_configuration.add("C2");
        type_unit_configuration.add("C3");
        spnr_unit_configuration.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, type_unit_configuration));
        spnr_unit_configuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkListModel.setUnit_configuration(type_unit_configuration.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        for (int i = 0; i < operationModels.size(); i++) { //AREA OPERATIONS
            nameOperations.add(operationModels.get(i).getName());
        }
        ArrayAdapter<String> adapterOperations = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, nameOperations);
        spnr_type_operation.setAdapter(adapterOperations);
        spnr_type_operation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                supervisorModels.clear();
                nameSupervisor.clear();
                inspectorModels.clear();
                nameInspector.clear();
                driverModels.clear();
                nameDrivers.clear();
                routeModels.clear();
                unitModels.clear();
                carrierModels.clear();
                nameUnits.clear();
                nameCompanies.clear();
                nameRoutes.clear();
                locationModels.clear();
                nameLocation.clear();
                nameSupervisor.add("Seleccione");
                nameInspector.add("Seleccione");
                nameDrivers.add("Seleccione");
                nameUnits.add("Seleccione");
                nameCompanies.add("Seleccione");
                nameLocation.add("Seleccione");
                nameRoutes.add("Seleccione");
                if (position != 0) {
                    operationString = "1";
                    checkListModel.setOperation(operationModels.get(position - 1).pk);
                    checkListModel.operation_id_check = operationModels.get(position - 1).getPk();
                    checkListModel.setName_operation(operationModels.get(position - 1).getName());
                    pkOperation = String.valueOf(operationModels.get(position - 1).getPk());
                    ArrayList<LocationModel> ListLocationsByOperation = SqliteClass.getInstance(context).databasehelp.locationSql.getAllLocation(pkOperation);
                    for (LocationModel location : ListLocationsByOperation) {
                        if (location.pk_operation == checkListModel.operation_id_check) {
                            locationModels.add(location);
                            nameLocation.add(location.getName());
                        }
                    }
                    ArrayList<CarrierModel> ListCarriersByOperation = SqliteClass.getInstance(context).databasehelp.carrierSql.getAllCarrierByPkCompanyAndPkOperation(pkOperation);

                    for (CarrierModel carriers : ListCarriersByOperation) {
                        if (carriers.pkOperation.equals(checkListModel.operation_id_check + "")) {
                            if (!is_staff && !is_superuser && !is_group_staff) {
                                if (company_name.equalsIgnoreCase(carriers.getParty())) {
                                    carrierModels.add(carriers);
                                    nameCompanies.add(carriers.getParty());
                                }
                            } else {
                                carrierModels.add(carriers);
                                nameCompanies.add(carriers.getParty());
                            }
                        }
                    }
                    ArrayList<InspectorModel> InspectorAll = SqliteClass.getInstance(context).databasehelp.inspectorSql.
                            getAllInspectorByPkCompanyAndPkOperation(pkOperation);
                    for (InspectorModel inspector : InspectorAll) {
                        if (inspector.pkOperation.equals(checkListModel.operation_id_check + "")) {
                            if (is_group_inspector && !is_superuser) {
                                if (employee_name.equalsIgnoreCase(inspector.getParty())) {
                                    inspectorModels.add(inspector);
                                    nameInspector.add(inspector.getParty());
                                }
                            } else {
                                inspectorModels.add(inspector);
                                nameInspector.add(inspector.getParty());
                            }
                        }
                    }

                    ArrayList<SupervisorModel> ListSupervisorByOperation =
                            SqliteClass.getInstance(context).databasehelp.supervisorSql.
                                    getAllInspectorByPkCompanyAndPkOperation(pkOperation);
                    for (SupervisorModel supervise : ListSupervisorByOperation) {
                        if (is_group_supervisor && !is_superuser) {
                            if (employee_name.equalsIgnoreCase(supervise.getParty())) {
                                supervisorModels.add(supervise);
                                nameSupervisor.add(supervise.getParty());
                            }
                        } else {
                            supervisorModels.add(supervise);
                            nameSupervisor.add(supervise.getParty());
                        }
                    }
                    ArrayList<RouteModel> ListRoutesByOperation =
                            SqliteClass.getInstance(context).databasehelp.routeSql.
                                    getRouteByOpe(pkOperation);
                    for (RouteModel route : ListRoutesByOperation) {
                        routeModels.add(route);
                        nameRoutes.add(route.getName_route());
                    }
                } else {
                    operationString = "0";
                    checkListModel.setOperation(0);
                    checkListModel.setName_operation("");
                }
                if (sharedPref1.getString("estado", "create").equals("create")) {
                    adapterSupervisor = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, nameSupervisor);
                    spnr_supervisor.setAdapter(adapterSupervisor);
                    adapterInspector = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, nameInspector);
                    spnr_inspector.setAdapter(adapterInspector);
                    adapterCompanies = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, nameCompanies);
                    spnr_company.setAdapter(adapterCompanies);
                    adapterDrivers = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, nameDrivers);
                    spnr_driver.setAdapter(adapterDrivers);
                    adapterUnits = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, nameUnits);
                    spnr_unit.setAdapter(adapterUnits);
                    adapterRoutes = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, nameRoutes);
                    spnr_type_route.setAdapter(adapterRoutes);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //AREA CARRIER COMPANY
        spnr_company.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (carrierModels.size() != 0) {
                    driverModels.clear();
                    nameDrivers.clear();
                    inspectorModels.clear();
                    nameInspector.clear();
                    unitModels.clear();
                    nameUnits.clear();
                    supervisorModels.clear();
                    nameSupervisor.clear();
                    nameSupervisor.add("Seleccione");
                    nameInspector.add("Seleccione");
                    nameDrivers.add("Seleccione");
                    nameUnits.add("Seleccione");
                    if (position != 0) {
                        company = "1";
                        checkListModel.setCompany_shipment(carrierModels.get(position - 1).getPk());
                        checkListModel.setName_company_shipment(carrierModels.get(position - 1).getParty());
                        ArrayList<DriverModel> ListDriversByCompany = SqliteClass.getInstance(context).databasehelp.driverSql.getAllDriverByComAndOpe(pkOperation);
                        for (DriverModel driver : ListDriversByCompany) {
                            if (driver.pk_company.equals(carrierModels.get(position - 1).pkCompany)) {
                                driverModels.add(driver);
                                nameDrivers.add(driver.getName_driver());
                            }
                        }
                        ArrayList<TransportUnitModel> ListTransportByOperation = SqliteClass.getInstance(context).databasehelp.tranportUnitSql.getAllUnitsByComAndOpe(pkOperation);
                        for (TransportUnitModel transport : ListTransportByOperation) {
                            if (transport.pk_company.equals(carrierModels.get(position - 1).pkCompany)) {
                                unitModels.add(transport);
                                nameUnits.add(transport.getPlate_tracto() + " / " + transport.getPlate_cistern());
                            }
                        }
                        ArrayList<InspectorModel> InspectorAll = SqliteClass.getInstance(context).databasehelp.inspectorSql.
                                getAllInspectorByPkCompanyAndPkOperation(pkOperation);
                        for (InspectorModel inspector : InspectorAll) {
                            if (is_superuser) {
                                if (inspector.pkCompany.equals(carrierModels.get(position - 1).pkCompany)
                                        || company_name.equalsIgnoreCase(inspector.getName_company())
                                ) {
                                    inspectorModels.add(inspector);
                                    nameInspector.add(inspector.getParty());
                                }
                            } else if (is_staff || is_group_staff) {
                                if (is_group_inspector) {
                                    if (employee_name.equalsIgnoreCase(inspector.getParty())) {
                                        inspectorModels.add(inspector);
                                        nameInspector.add(inspector.getParty());
                                    }
                                } else {
                                    if (inspector.pkCompany.equals(carrierModels.get(position - 1).pkCompany)) {
                                        inspectorModels.add(inspector);
                                        nameInspector.add(inspector.getParty());
                                    }
                                }
                            } else {
                                if (inspector.pkCompany.equals(carrierModels.get(position - 1).pkCompany)) {
                                    if (is_group_inspector) {
                                        if (employee_name.equalsIgnoreCase(inspector.getParty())) {
                                            inspectorModels.add(inspector);
                                            nameInspector.add(inspector.getParty());
                                        }
                                    } else {
                                        inspectorModels.add(inspector);
                                        nameInspector.add(inspector.getParty());
                                    }
                                }
                            }
                        }
                        ArrayList<SupervisorModel> ListSupervisorByOperation = SqliteClass.getInstance(context).databasehelp.supervisorSql.getAllInspectorByPkCompanyAndPkOperation(pkOperation);
                        for (SupervisorModel supervise : ListSupervisorByOperation) {
                            if (is_superuser) {
                                if (supervise.pkCompany.equals(carrierModels.get(position - 1).pkCompany)
                                        || company_id == parseInt(supervise.pkCompany)) {
                                    supervisorModels.add(supervise);
                                    nameSupervisor.add(supervise.getParty());
                                }
                            } else if (is_staff || is_group_staff) {
                                if (is_group_supervisor) {
                                    if (employee_name.equalsIgnoreCase(supervise.getParty())) {
                                        supervisorModels.add(supervise);
                                        nameSupervisor.add(supervise.getParty());
                                    }
                                } else {
                                    if (supervise.pkCompany.equals(carrierModels.get(position - 1).pkCompany)) {
                                        supervisorModels.add(supervise);
                                        nameSupervisor.add(supervise.getParty());
                                    }
                                }
                            } else {
                                if (supervise.pkCompany.equals(carrierModels.get(position - 1).pkCompany)) {
                                    if (is_group_supervisor) {
                                        if (employee_name.equalsIgnoreCase(supervise.getParty())) {
                                            supervisorModels.add(supervise);
                                            nameSupervisor.add(supervise.getParty());
                                        }
                                    } else {
                                        supervisorModels.add(supervise);
                                        nameSupervisor.add(supervise.getParty());
                                    }
                                }
                            }
                        }
                        if (sharedPref1.getString("estado", "create").equals("create")) {
                            adapterDrivers = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, nameDrivers);
                            spnr_driver.setAdapter(adapterDrivers);
                            adapterUnits = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, nameUnits);
                            spnr_unit.setAdapter(adapterUnits);
                            adapterSupervisor = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, nameSupervisor);
                            spnr_supervisor.setAdapter(adapterSupervisor);
                            adapterInspector = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, nameInspector);
                            spnr_inspector.setAdapter(adapterInspector);
                        }
                    } else {
                        company = "0";
                        checkListModel.setCompany_shipment(0);
                        checkListModel.setName_company_shipment("");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //AREA SUPERVISOR
        spnr_supervisor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    sup = "1";
                    checkListModel.setSupervisor(supervisorModels.get(position - 1).getPk());
                    checkListModel.setName_supervisor(supervisorModels.get(position - 1).getParty());
                } else {
                    sup = "0";
                    checkListModel.setSupervisor(0);
                    checkListModel.setName_supervisor("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //AREA ROUTE
        spnr_type_route.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    rout = "1";
                    checkListModel.setRoute(routeModels.get(position - 1).getId_api());
                    checkListModel.setName_route(routeModels.get(position - 1).getName_route());
                } else {
                    rout = "0";
                    checkListModel.setRoute(0);
                    checkListModel.setName_route("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (is_group_supervisor && !is_superuser) {
            edittext_supervisor.setVisibility(View.VISIBLE);
            spnr_supervisor.setVisibility(View.GONE);
            edittext_supervisor.setText(employee_name.toUpperCase());
        } else {
            edittext_supervisor.setVisibility(View.GONE);
            spnr_supervisor.setVisibility(View.VISIBLE);
        }

        //AREA INSPECTOR
        spnr_inspector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    inspectorString = "1";
                    checkListModel.setInspector(inspectorModels.get(position - 1).getPk());
                    checkListModel.setName_inspector(inspectorModels.get(position - 1).getParty());
                } else {
                    inspectorString = "0";
                    checkListModel.setInspector(0);
                    checkListModel.setName_inspector("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (is_group_inspector && !is_superuser) {
            edittext_inspector.setVisibility(View.VISIBLE);
            spnr_inspector.setVisibility(View.GONE);
            edittext_inspector.setText(employee_name.toUpperCase());
        } else {
            edittext_inspector.setVisibility(View.GONE);
            spnr_inspector.setVisibility(View.VISIBLE);
        }

        //AREA DRIVER
        spnr_driver.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (driverModels.size() > 0) {
                    if (position != 0) {
                        dri = "1";
                        checkListModel.setDriver(driverModels.get(position - 1).getId());
                        checkListModel.setName_driver(driverModels.get(position - 1).getName_driver());
                    } else {
                        dri = "0";
                        checkListModel.setDriver(0);
                        checkListModel.setName_driver("");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //AREA UNIT TRANSPORT
        spnr_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    unit = "1";
                    if (unitModels.size() != 0 && (position - 1) < unitModels.size()) {
                        checkListModel.setTransport_unit(unitModels.get(position - 1).getId());
                        checkListModel.setPlate_cistern(unitModels.get(position - 1).getPlate_cistern());
                        checkListModel.setPlate_tracto(unitModels.get(position - 1).getPlate_tracto());

                        //Obtener Datos de la Unidad de Transporte
                        String unitTransportId = "" + unitModels.get(position - 1).getId();
                        new GetUnitTransportTask().execute(unitTransportId);
                    }
                } else {
                    unit = "0";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //AREA IMAGE BUTTON
        btn_tracto_image = findViewById(R.id.btn_tracto_image);
        btn_tracto_image.setOnClickListener(view -> callCamera("tracto"));
        btn_cistern_image = findViewById(R.id.btn_cistern_image);
        btn_cistern_image.setOnClickListener(view -> callCamera("cistern"));

        //AREA BUTTONS
        btn_create = findViewById(R.id.btn_create);

        btn_create.setOnClickListener(v -> {
            checkListModel.setState("posted");
            checkListModel.setDriver_signature("");
            checkListModel.setSupervisor_signature("");
            checkListModel.setType("local");
            checkListModel.setDatetime("");
            checkListModel.setNumber("Sin Enviar");
            if (sharedPref1.getString("estado", "create").equals("update")) {
                Intent intent = new Intent(context, CheckListFormActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            } else {
                if (is_group_supervisor && !is_superuser) {
                    if (supervisorModels.size() > 0) {
                        sup = "1";
                        checkListModel.setSupervisor(supervisorModels.get(0).getPk());
                        checkListModel.setName_supervisor(supervisorModels.get(0).getParty());
                        if (!supervisorModels.get(0).getParty().equalsIgnoreCase(employee_name)) {
                            Toast.makeText(context, "El usuario no pertenece a la operación", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        Toast.makeText(context, "El usuario no pertenece a la operación", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (is_group_inspector && !is_superuser) {
                    if (inspectorModels.size() > 0) {
                        inspectorString = "1";
                        checkListModel.setInspector(inspectorModels.get(0).getPk());
                        checkListModel.setName_inspector(inspectorModels.get(0).getParty());
                        if (!inspectorModels.get(0).getParty().equalsIgnoreCase(employee_name)) {
                            Toast.makeText(context, "El usuario no pertenece a la operación", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        Toast.makeText(context, "El usuario no pertenece a la operación", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (et_mileage.length() > 0 && !sup.equals("0") && !operationString.equals("0")
                        && !dri.equals("0") && !inspectorString.equals("0") && !rout.equals("0")
                        && !unit.equals("0") && !checkListModel.getCistern_image().equals("")
                        && !checkListModel.getTracto_image().equals("")) {
                    editor1.putString("estado", "create");
                    editor.apply();
                    if (Util.isCorrectFormatPoint(checkListModel.getMileage())) {
                        if (isLocationEnabled() && longitudeBest != 0 && latitudeBest != 0) {
                            checkListModel.setMileage(Util.only2Steps(checkListModel.getMileage()));
                            checkListModel.setLatitude(String.valueOf(Math.round(latitudeBest * 10000000000.0) / 10000000000.0));
                            checkListModel.setLongitude(String.valueOf(Math.round(longitudeBest * 10000000000.0) / 10000000000.0));
                            checkListModel.setLocation(null);
                            checkListModel.setName_location("Sin ubicación");
                            new ItemTask().execute(checkListModel);
                        } else {
                            //Toast.makeText(context, "DEBE ACTIVAR SU GPS, RECUERDE QUE LA CALIBRACIÓN PUEDE TOMAR UNOS SEGUNDOS", Toast.LENGTH_LONG).show();
                            new ItemTask().execute(checkListModel);
                        }
                    } else {
                        Toast.makeText(context, "El kilometraje no es un número válido", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Faltan completar y/o seleccionar datos", Toast.LENGTH_LONG).show();
                }
            }
        });
        btn_cancel = findViewById(R.id.btn_cancelar);
        btn_cancel.setOnClickListener(view -> {
            Intent intent = new Intent(FormNewCheckListActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
        if (sharedPref1.getString("estado", "create").equals("create")) { // Here set edited data
            dateString = Util.getFecha();
            hourString = Util.getHora();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class ItemTask extends AsyncTask<CheckListModel, Void, String> {
        @Override
        protected String doInBackground(CheckListModel... checkListModels) {
            String result;
            itemCheckModels = SqliteClass.getInstance(context).databasehelp.itemSql.getAllItemsByCheck("plantilla");
            ItemCheckListModel itemModel = new ItemCheckListModel();
            String idCheckList = SqliteClass.getInstance(context).databasehelp.checkListSql.addcheckList(checkListModels[0]);
            SharedPreferences sharedPrefCheck = getSharedPreferences("check_list", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefCheck.edit();
            editor.putString("id_check_list", String.valueOf(checkListModels[0].getId()));
            editor.putString("id_check_list_sql", idCheckList);
            editor.putString("number_check_list", "Sin enviar");
            editor.putString("type", "local");
            editor.putString("opcion", "");
            editor.apply();
            for (int i = 0; i < itemCheckModels.size(); i++) {
                if (itemCheckModels.get(i).getOperations_text() != null) {
                    if (itemCheckModels.get(i).getOperations_text().equals("")) {
                        itemModel.setId(itemCheckModels.get(i).getId());
                        itemModel.setName(itemCheckModels.get(i).getName());
                        itemModel.setId_section(itemCheckModels.get(i).getId_section());
                        itemModel.setId_check_list(idCheckList);
                        itemModel.setCompliance_image("");
                        itemModel.setNon_compliance_image("");
                        itemModel.setObservation_lifting_image("");
                        itemModel.setState("C");
                        SqliteClass.getInstance(context).databasehelp.itemSql.addItem(itemModel);
                    } else {
                        if (itemCheckModels.get(i).getOperations_text().contains(checkListModels[0].getName_operation())) {
                            itemModel.setId(itemCheckModels.get(i).getId());
                            itemModel.setName(itemCheckModels.get(i).getName());
                            itemModel.setId_section(itemCheckModels.get(i).getId_section());
                            itemModel.setId_check_list(idCheckList);
                            itemModel.setCompliance_image("");
                            itemModel.setNon_compliance_image("");
                            itemModel.setObservation_lifting_image("");
                            itemModel.setState("C");
                            SqliteClass.getInstance(context).databasehelp.itemSql.addItem(itemModel);
                        }
                    }
                }
            }
            result = "ok";
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(context, "", "Creando CheckList", true);
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if (s.equals("ok")) {
                Toast.makeText(context, "Listo!!, Información guardada", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, CheckListFormActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(context, "Encontramos un error, no se pudo completar el guardado", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(s);
        }

    }

    @Override
    public void onBackPressed() {
        if (et_mileage.length() > 0 ||
                !operationString.equals("0") || !dri.equals("0") || !inspectorString.equals("0") || !rout.equals("0") ||
                !unit.equals("0") || !checkListModel.getCistern_image().equals("") || !checkListModel.getTracto_image().equals("")) {
            new MaterialAlertDialogBuilder(context)
                    .setTitle("Cerrar")
                    .setMessage("Esta seguro que quiere salir ? , sus cambios se perderan")
                    .setNegativeButton("Cancelar", null)
                    .setPositiveButton("Ok", (dialog, which) -> super.onBackPressed()).show();
        } else {
            super.onBackPressed();
        }
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
            if (et_mileage.length() > 0 ||
                    !operationString.equals("0") || !dri.equals("0") || !inspectorString.equals("0") || !rout.equals("0") ||
                    !unit.equals("0") || !checkListModel.getCistern_image().equals("") || !checkListModel.getTracto_image().equals("")) {
                new MaterialAlertDialogBuilder(context)
                        .setTitle("Cerrar")
                        .setMessage("Esta seguro que quiere salir ? , sus cambios se perderan")
                        .setNegativeButton("Cancelar", null)
                        .setPositiveButton("Ok", (dialog, which) -> finish()).show();
            } else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void reload_btn_tracto_Activity() {
        btn_tracto_image.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_a_photo_black_24dp, 0, R.drawable.ic_baseline_check_circle_24, 0);
    }

    public void reload_btn_cister_Activity() {
        btn_cistern_image.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_a_photo_black_24dp, 0, R.drawable.ic_baseline_check_circle_24, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap imageBitmap = null;
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(context, "SE CANCELO LA CAPTURA DE IMAGEN", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE_TRACTO && resultCode == RESULT_OK) {
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            int origWidth = Objects.requireNonNull(imageBitmap).getWidth();
            int origHeight = imageBitmap.getHeight();
            int targetWidth = 400; // your arbitrary fixed limit
            int targetHeight = 600; // your arbitrary fixed limit
            Bitmap bitmapImageResult = imageBitmap;
            if (origWidth <= origHeight) {
                if (origWidth > targetWidth) {
                    int destHeight = origHeight / (origWidth / targetWidth);
                    bitmapImageResult = Bitmap.createScaledBitmap(imageBitmap, targetWidth, destHeight, false);
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    bitmapImageResult.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
                    checkListModel.setTracto_image(PictureUtils.getBase64String(bitmapImageResult));
                } else {
                    checkListModel.setTracto_image(PictureUtils.getBase64String(imageBitmap));
                }
            } else {
                if (origHeight > targetHeight) {
                    int destWidth = origWidth / (origHeight / targetHeight);
                    bitmapImageResult = Bitmap.createScaledBitmap(imageBitmap, destWidth, targetHeight, false);
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    bitmapImageResult.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
                    checkListModel.setTracto_image(PictureUtils.getBase64String(bitmapImageResult));
                } else {
                    checkListModel.setTracto_image(PictureUtils.getBase64String(imageBitmap));
                }

            }
            image_tracto_view.setImageBitmap(bitmapImageResult);
        } else if (requestCode == REQUEST_IMAGE_CAPTURE_CISTERN && resultCode == RESULT_OK) {
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
            } catch (IOException e) {
                Sentry.captureMessage(e + "error-sentry");
                e.printStackTrace();
            }
            int origWidth = Objects.requireNonNull(imageBitmap).getWidth();
            int origHeight = imageBitmap.getHeight();
            int targetWidth = 400; // your arbitrary fixed limit
            int targetHeight = 600; // your arbitrary fixed limit
            Bitmap bitmapImageResult = imageBitmap;
            if (origWidth <= origHeight) {
                if (origWidth > targetWidth) {
                    int destHeight = origHeight / (origWidth / targetWidth);
                    bitmapImageResult = Bitmap.createScaledBitmap(imageBitmap, targetWidth, destHeight, false);
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    bitmapImageResult.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
                    checkListModel.setCistern_image(PictureUtils.getBase64String(bitmapImageResult));
                } else {
                    checkListModel.setCistern_image(PictureUtils.getBase64String(imageBitmap));
                }
            } else {
                if (origHeight > targetHeight) {
                    int destWidth = origWidth / (origHeight / targetHeight);
                    bitmapImageResult = Bitmap.createScaledBitmap(imageBitmap, destWidth, targetHeight, false);
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    bitmapImageResult.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
                    checkListModel.setCistern_image(PictureUtils.getBase64String(bitmapImageResult));
                } else {
                    checkListModel.setCistern_image(PictureUtils.getBase64String(imageBitmap));
                }
            }
            image_cistern_view.setImageBitmap(bitmapImageResult);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMARA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(context, "EL PERMISO ES NECESARIO", Toast.LENGTH_SHORT).show();
            }
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
                i.putExtra(MediaStore.EXTRA_OUTPUT,
                        FileProvider.getUriForFile(this,
                                context.getApplicationContext().getPackageName() + ".provider", photoFile));
            }
            if (type_car.equals("tracto")) {
                startActivityForResult(i, REQUEST_IMAGE_CAPTURE_TRACTO);
            } else if (type_car.equals("cistern")) {
                startActivityForResult(i, REQUEST_IMAGE_CAPTURE_CISTERN);
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "NO HAY CÁMARAS DISPONIBLES", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void callCamera(String type_car) {
        this.type_car = type_car;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMARA);
            } else {
                openCamera();
            }
        } else {
            openCamera();
        }
    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Active GPS")
                .setMessage("Su ubicación esta desactivada.\npor favor active su ubicación " +
                        "usa esta app")
                .setPositiveButton("Configuración de ubicación", (paramDialogInterface, paramInt) -> {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                })
                .setNegativeButton("Cancelar", (paramDialogInterface, paramInt) -> {
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void toggleBestUpdates() {
        if (!checkLocation())
            return;
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"Sin permisos para acceder al GPS", Toast.LENGTH_SHORT).show();
                return;
            }
            locationManager.requestLocationUpdates(provider,2 * 20 * 1000,10, locationListenerBest);
        }

    }

    private final LocationListener locationListenerBest = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeBest = location.getLongitude();
            latitudeBest = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };

    @SuppressLint("StaticFieldLeak")
    class GetTractoTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... plate) {
            SharedPreferences sharedPref = context.getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
            try {
                checkListModel.setLastInspection("");
                checkListModel.setYearFabrication("");
                Response<List<TractoModel>> tractoResp = api.getTractoByPlate(sharedPref.getString("token", ""), plate[0]).execute();
                if(tractoResp.isSuccessful()) {
                    List<TractoModel> tractos = tractoResp.body();
                    if(tractos != null && tractos.size() > 0) {
                        checkListModel.setLastInspection(tractos.get(0).getLastChecklist());
                        checkListModel.setYearFabrication(tractos.get(0).getYearFabrication());
                        return "ok";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
            return "";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(context, "", "Obteniendo información del Tracto", true);
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if(s.equals("ok")) {
                et_last_checklist.setText(checkListModel.getLastInspection());
                et_manufactoring_year.setText(checkListModel.getYearFabrication());
            }
            super.onPostExecute(s);
        }

    }

    @SuppressLint("StaticFieldLeak")
    class GetUnitTransportTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... ids) {
            SharedPreferences sharedPref = context.getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
            try {
                checkListModel.setLastInspection("");
                checkListModel.setYearFabrication("");
                Response<PaginationModel> unitResp = api.getLastCheckListByTransportUnit(sharedPref.getString("token", ""), parseInt(ids[0]), 1).execute();
                if(unitResp.isSuccessful()) {
                    List<GetCheckListModel> checklists = unitResp.body().getResults();
                    if(checklists != null && checklists.size() > 0) {
                        String lastInspection = "" + checklists.get(0).getLastInspection();
                        lastInspection = Util.toNumeroChecklistFecha(lastInspection);
                        checkListModel.setLastInspection(lastInspection);
                        checkListModel.setYearFabrication(checklists.get(0).getYearFabrication());
                        return "ok";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
            return "";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(context, "", "Obteniendo información del Tracto", true);
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if(s.equals("ok")) {
                et_last_checklist.setText(checkListModel.getLastInspection());
                et_manufactoring_year.setText(checkListModel.getYearFabrication());
            }
            super.onPostExecute(s);
        }

    }
}

