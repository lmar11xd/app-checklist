package com.smartory.db;

import static com.smartory.resource.HorizontalValues.LABEL_OBSERVATION_ITEM;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.smartory.models.CarrierModel;
import com.smartory.models.CheckListModel;
import com.smartory.models.DriverModel;
import com.smartory.models.ImageModel;
import com.smartory.models.InspectorModel;
import com.smartory.models.ItemCheckListModel;
import com.smartory.models.LevObsModel;
import com.smartory.models.LocationModel;
import com.smartory.models.OperacionModel;
import com.smartory.models.ResponseImageItemChecklist;
import com.smartory.models.RouteModel;
import com.smartory.models.SupervisorModel;
import com.smartory.models.TransportUnitModel;

import java.util.ArrayList;
import java.util.List;

public class SqliteClass {

    /* @TABLE_APP_OPERATION  */
    public static final String TABLE_APP_OPERATION = "app_operation";
    public static final String KEY_OPEID = "operation_id";
    public static final String KEY_OPEPK = "operation_pk";
    public static final String KEY_OPENAM = "operation_name";
    public static final String KEY_OPECHECKID = "operation_check_pk";

    /* @TABLE_APP_COMPANY */
    public static final String TABLE_APP_COMPANY = "app_companie";
    public static final String KEY_COMID = "supervisor_id";
    public static final String KEY_COMPK = "supervisor_id_api";
    public static final String KEY_COMNAM = "supervisor_name";

    /* @TABLE_APP_CARRIER */
    public static final String TABLE_APP_CARRIER = "app_carrier";
    public static final String KEY_CARRID = "carrier_id";
    public static final String KEY_CARRPK = "carrier_pk";
    public static final String KEY_CARRNAM = "carrier_name";
    public static final String KEY_CARRCOM = "carrier_pk_company";
    public static final String KEY_CARROPE = "carrier_pk_operation";

    /* @TABLE_APP_CARRIER_BY_OPERATION  */
    public static final String TABLE_APP_CARRIER_BY_OPERATION = "app_operation_carrier";
    public static final String KEY_CARRBYOPEID = "company_carrier_id";
    public static final String KEY_CARRBYOPEPKCOM = "company_carrier_pk_company";
    public static final String KEY_CARRBYOPERPKSUP = "company_carrier_pk_carrier";

    /* @TABLE_APP_COMPANY_BY_OPERATION  */
    public static final String TABLE_APP_COMPANY_BY_OPERATION = "app_company_operation";
    public static final String KEY_COMPBYOPERID = "company_operation_id";
    public static final String KEY_COMPBYOPERPKCOM = "company_operation_pk_company";
    public static final String KEY_COMPBYOPERPKOPE = "company_operation_pk_operation";

    /* @TABLE_APP_SUPERVISOR */
    public static final String TABLE_APP_SUPERVISOR = "app_supervisor";
    public static final String KEY_SUPID = "supervisor_id";
    public static final String KEY_SUPPK = "supervisor_pk";
    public static final String KEY_SUPNAM = "supervisor_name";
    public static final String KEY_SUPCOM = "supervisor_pk_company";
    public static final String KEY_SUPCOPE = "supervisor_pk_operation";

    /* @TABLE_APP_COMPANY_BY_SUPERVISOR  */
    public static final String TABLE_APP_COMPANY_BY_SUPERVISOR = "app_company_supervisor";
    public static final String KEY_COMPBYSUPERID = "company_supervisor_id";
    public static final String KEY_COMPBYSUPERPKCOM = "company_supervisor_pk_company";
    public static final String KEY_COMPBYSUPERPKSUP = "company_supervisor_pk_supervisor";

    /* @TABLE_APP_INSPECTOR */
    public static final String TABLE_APP_INSPECTOR = "app_inspector";
    public static final String KEY_INSID = "inspector_id";
    public static final String KEY_INSPK = "inspector_pk";
    public static final String KEY_INSNAM = "inspector_name";
    public static final String KEY_INSCOM = "inspector_pk_company";
    public static final String KEY_INSOPE = "inspector_pk_operation";
    public static final String KEY_INSNAMCOMPANY = "inspector_name_company";

    /* @TABLE_APP_COMPANY_BY_INSPECTOR  */
    public static final String TABLE_APP_COMPANY_BY_INSPECTOR = "app_company_inspector";
    public static final String KEY_COMPBYINSERID = "company_inspector_id";
    public static final String KEY_COMPBYINSPKCOM = "company_inspector_pk_company";
    public static final String KEY_COMPBYINSPKINS = "company_inspector_pk_inspector";

    /* @TABLE_APP_DRIVER */
    public static final String TABLE_APP_DRIVER = "app_driver";
    public static final String KEY_DRIID = "driv_id";
    public static final String KEY_DRIVIDAPI = "driv_id_api";
    public static final String KEY_DRIVNAM = "driv_name";
    public static final String KEY_DRIVPKCOMP = "driv_pk_company";
    public static final String KEY_DRIVPKOPE = "driv_pk_operation";


    /* @TABLE_APP_ROUTE */
    public static final String TABLE_APP_ROUTE = "app_route";
    public static final String KEY_ROUTE_ID = "app_route_id";
    public static final String KEY_ROUTE_API = "app_route_id_api";
    public static final String KEY_ROUTE_NAM = "app_route_name";
    public static final String KEY_ROUTE_PK_OPE = "app_route_pk_operation";

    /* @TABLE_APP_COMPANY_BY_DRIVER  */
    public static final String TABLE_APP_COMPANY_BY_DRIVER = "app_company_driver";
    public static final String KEY_COMPBYDRIID = "company_driver_id";
    public static final String KEY_COMPBYDRIPKCOM = "company_driver_pk_company";
    public static final String KEY_COMPBYDRIIDDRI = "company_driver_id_driver";
    public static final String KEY_COMPBYDRIPKOPE = "company_driver_pk_operation";

    /* @TABLE_APP_TRANSPORT_UNIT  */
    public static final String TABLE_APP_TRANSPORT_UNIT = "app_tranport_unit";
    public static final String KEY_TRAPORID = "trans_unit_id";
    public static final String KEY_TRAPORPK = "trans_unit_id_api";
    public static final String KEY_TRAPORPLATCIS = "trans_unit_plate_cistern";
    public static final String KEY_TRAPORPLATTRA = "trans_unit_plate_tracto";
    public static final String KEY_TRAPORCOMPK = "trans_unit_transport_company_pk";
    public static final String KEY_TRAPOROPEPK = "trans_unit_transport_operation_pk";

    /* @TABLE_APP_COMPANY_BY_TRANSPORTUNIT  */
    public static final String TABLE_APP_COMPANY_BY_TRANSPORTUNIT = "app_company_transportunit";
    public static final String KEY_TRANSUNIBYDRIID = "company_transportunit_id";
    public static final String KEY_TRANSUNIBYDRIPKCOM = "company_transportunit_pk_company";
    public static final String KEY_TRANSUNIBYDRIPKOPE = "company_transportunit_pk_operation";
    public static final String KEY_TRANSUNIBYDRIPKTRAN = "company_transportunit_id_transportunit";

    /* @TABLE_APP_LOCATION */
    public static final String TABLE_APP_LOCATION = "app_location";
    public static final String KEY_LOCAID = "location_id";
    public static final String KEY_LOCIDAPI = "location_id_api";
    public static final String KEY_LOCNAM = "location_name";
    public static final String KEY_LOCLATITUDE = "location_latitude";
    public static final String KEY_LOCLONGITUDE = "location_longitude";
    public static final String KEY_LOCPKOPER = "location_pk_operation";
    public static final String KEY_LOCALTITUDE = "location_altitude";

    /* @TABLE_APP_SECTION  */
    public static final String TABLE_APP_SECTION = "app_section";
    public static final String KEY_SECID = "section_id";
    public static final String KEY_SECIDAPI = "section_id_api";
    public static final String KEY_SECNAM = "section_name";

    /* @TABLE_APP_CHECK  */
    public static final String TABLE_APP_CHECKLIST = "app_check";
    public static final String KEY_CHECID = "check_id";
    public static final String KEY_CHECIDAPI = "check_id_api";
    public static final String KEY_CHECNUM = "check_number";
    public static final String KEY_CHECMIL = "check_mileage";
    public static final String KEY_CHECTYP = "check_type";
    public static final String KEY_CHECUNITCONFIG = "check_unit_configuration";
    public static final String KEY_CHECTYPCHE = "check_type_check";
    public static final String KEY_CHECDAT = "check_date";
    public static final String KEY_CHECSTA = "check_state";
    public static final String KEY_CHECROUTE = "check_route";
    public static final String KEY_CHECNAMROUTE = "check_route_name";
    public static final String KEY_CHECSIGDRIV = "check_signature_drive";
    public static final String KEY_CHECSIGSUPE = "check_signature_supervisor";
    public static final String KEY_CHECDRI = "check_driver";
    public static final String KEY_CHECNAMTRA = "check_name_transport";
    public static final String KEY_CHECTRA = "check_transport";
    public static final String KEY_CHECNAMDRI = "check_name_driver";
    public static final String KEY_CHECSUP = "check_supervisor";
    public static final String KEY_CHECNAMSUP = "check_name_supervisor";
    public static final String KEY_CHECINS = "check_inspector";
    public static final String KEY_CHECNAMINS = "check_name_inspector";
    public static final String KEY_CHECLOC = "check_location";
    public static final String KEY_CHECIDSQLLOC = "check_id_sql_location";
    public static final String KEY_CHECNAMLOC = "check_name_location";
    public static final String KEY_CHECK_LONGITUDE = "check_name_longitude";
    public static final String KEY_CHECK_LATITUDE = "check_name_latitude";
    public static final String KEY_CHECOPE = "check_operation";
    public static final String KEY_CHECNAMOPE = "check_name_operation";
    public static final String KEY_CHECENV = "check_enviado";
    public static final String KEY_CHECCOMSHI = "check_company_shipment";
    public static final String KEY_CHECNAMCOMSHI = "check_name_company_shipment";
    public static final String KEY_CHECPLATRAC = "check_plate_tracto";
    public static final String KEY_CHECPLACIST = "check_plate_cistern";
    public static final String KEY_CHECTRACIMA = "check_tracto_image";
    public static final String KEY_CHECCISIMA = "check_cistern_image";
    public static final String KEY_CHECLASCHE = "check_last_checklist";
    public static final String KEY_CHECMANYEA = "check_manufactoring_year";
    public static final String KEY_CHECPROGRESS = "check_progress";

    /* @TABLE_APP_ITEM  */
    //GET
    public static final String TABLE_APP_ITEM = "app_item";
    public static final String KEY_ITEMID = "item_id";
    public static final String KEY_ITEMIDAPI = "item_id_api";
    public static final String KEY_ITEMIDSEC = "item_id_section";//FK
    public static final String KEY_ITEMNAM = "item_name";
    public static final String KEY_ITEMIDCHEK = "item_ic_checklist";
    //POST
    public static final String KEY_ITEMDUEDAT = "item_due_date";
    public static final String KEY_ITEMOBSDES = "item_observation_description";
    public static final String KEY_ITEMOBSLIF = "item_observation_lifting";
    public static final String KEY_ITEMOBSLIFDAT = "item_observation_lifting_date";
    public static final String KEY_ITEMCOMIMA = "item_compliance_image";
    public static final String KEY_ITEMNONCOMIMA = "item_non_compliance_image";
    public static final String KEY_ITEMOBSLIFIMA = "item_observation_lifting_image";
    public static final String KEY_ITEMSTA = "item_state";
    public static final String KEY_ITEMTRACTIMA = "item_tracto_image";
    public static final String KEY_ITEMCISIMA = "item_cistern_image";
    public static final String KEY_ITEMCHELIS = "item_check_list";
    public static final String KEY_ITEMCHELISITE = "item_check_list_item";
    public static final String KEY_ITEMEDI = "item_editado";
    public static final String KEY_ITEM_OPERATIONS = "item_checklist_operation_item";
    public static final String KEY_ITEMREVIEWED = "item_reviewed";

    /* @TABLE_LEV_OBS  */
    public static final String TABLE_APP_LEB_OBS = "app_lev_obs";
    public static final String KEY_LEVOBSID = "lev_obs_id";
    public static final String KEY_LEVOBSPKAPI = "lev_obs_pk_api";
    public static final String KEY_LEVOBSIDITEM = "lev_obs_id_item";
    public static final String KEY_LEVOBSCHELIS = "lev_obs_check_list";
    public static final String KEY_LEVOBSCHELISDATE = "lev_obs_check_list_date";
    public static final String KEY_LEVOBSTRACOM = "lev_obs_transport_company";
    public static final String KEY_LEVOBSTRAUNI = "lev_obs_transport_unit";
    public static final String KEY_LEVOBSSEC = "lev_obs_section";
    public static final String KEY_LEVOBSCHELISITE = "lev_obs_check_list_item";
    public static final String KEY_LEVOBSOBSDES = "lev_obs_observation_description";
    public static final String KEY_LEVOBSSEV = "lev_obs_severity";
    public static final String KEY_LEVOBSDUEDAT = "lev_obs_due_date";
    public static final String KEY_LEVOBSSTATE = "lev_obs_state";
    public static final String KEY_LEV_OBS_OBS_LIFTING = "lev_obs_observation_ligthing";


    /* @TABLE_APP_IMAGE*/
    public static final String TABLE_APP_IMAGE = "app_image";
    public static final String KEY_IMAID = "app_image_id";
    public static final String KEY_IMANAM = "app_image_name";
    public static final String KEY_IMAORDNUM = "app_image_documentIdApi";
    public static final String KEY_IMASTA = "app_image_state";
    public static final String KEY_IMATYP = "app_image_type";
    public static final String KEY_IMAOWN = "app_image_owner";
    public static final String KEY_IMA_LATITUDE = "app_image__latitude";
    public static final String KEY_IMA_LONGITUDE = "app_image__longitude";
    public static final String KEY_IMA_DATETIME = "app_image__datetime";
    public static final String KEY_IMA_LABEL = "app_image__label";
    public static final String KEY_IMA_URL = "app_image__url";
    public static final String KEY_IMA_COMMENT = "app_image__comment";

    @SuppressLint("StaticFieldLeak")
    public static DatabaseHelper databasehelp;
    private static SqliteClass SqliteInstance = null;

    private SqliteClass(Context context) {
        databasehelp = DatabaseHelper.getInstance(context);
    }

    public static SqliteClass getInstance(Context context) {
        if (SqliteInstance == null) {
            SqliteInstance = new SqliteClass(context);
        }
        return SqliteInstance;
    }

    /**
     * create custom DatabaseHelper class that extends SQLiteOpenHelper
     */
    public static class DatabaseHelper extends SQLiteOpenHelper {
        public Context context;
        @SuppressLint("StaticFieldLeak")
        private static DatabaseHelper sInstance;
        private static final int DATABASE_VERSION = 2;
        private static final String DATABASE_NAME = "app_checklist.db";
        public OperationSql operacionSql;
        public SupervisorSql supervisorSql;
        public InspectorSql inspectorSql;
        public TranportUnitSql tranportUnitSql;
        public DriverSql driverSql;
        public RouteSql routeSql;
        public AppImageSql imageSql;
        public LocationSql locationSql;
        public ItemSql itemSql;
        public CheckListSql checkListSql;
        public LevObsSql levObsSql;
        public CarrierSql carrierSql;

        public static synchronized DatabaseHelper getInstance(Context context) {
            if (sInstance == null) {
                sInstance = new DatabaseHelper(context.getApplicationContext());
            }
            return sInstance;
        }

        private DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
            operacionSql = new OperationSql();
            supervisorSql = new SupervisorSql();
            imageSql = new AppImageSql();
            inspectorSql = new InspectorSql();
            routeSql = new RouteSql();
            tranportUnitSql = new TranportUnitSql();
            driverSql = new DriverSql();
            locationSql = new LocationSql();
            itemSql = new ItemSql();
            checkListSql = new CheckListSql();
            levObsSql = new LevObsSql();
            carrierSql = new CarrierSql();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            /* @TABLE_OPERATION */
            String CREATE_TABLE_OPERATION = "CREATE TABLE " + TABLE_APP_OPERATION + "("
                    + KEY_OPEID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_OPEPK + " TEXT," + KEY_OPECHECKID + " TEXT," + KEY_OPENAM + " TEXT )";

            /* @TABLE_COMPANY */
            String CREATE_TABLE_COMPANY = "CREATE TABLE " + TABLE_APP_COMPANY + "("
                    + KEY_COMID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_COMPK + " TEXT," + KEY_COMNAM + " TEXT )";

            /* @TABLE_COMPANY_OPERATION */
            String CREATE_TABLE_COM_OPE = "CREATE TABLE " + TABLE_APP_COMPANY_BY_OPERATION + "("
                    + KEY_COMPBYOPERID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_COMPBYOPERPKCOM + " TEXT," + KEY_COMPBYOPERPKOPE + " TEXT )";

            /* @TABLE_SUPERVISOR */
            String CREATE_TABLE_SUPERVISOR = "CREATE TABLE " + TABLE_APP_SUPERVISOR + "("
                    + KEY_SUPID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_SUPPK + " TEXT," + KEY_SUPCOM + " TEXT," + KEY_SUPCOPE + " TEXT," + KEY_SUPNAM + " TEXT )";

            /* @TABLE_COMPANY_SUPERVISOR */
            String CREATE_TABLE_COM_SUP = "CREATE TABLE " + TABLE_APP_COMPANY_BY_SUPERVISOR + "("
                    + KEY_COMPBYSUPERID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_COMPBYSUPERPKCOM + " TEXT," + KEY_COMPBYSUPERPKSUP + " TEXT )";

            /* @TABLE_INSPECTOR */
            String CREATE_TABLE_INSPECTOR = "CREATE TABLE " + TABLE_APP_INSPECTOR + "("
                    + KEY_INSID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_INSPK + " TEXT," + KEY_INSCOM + " TEXT," + KEY_INSNAMCOMPANY + " TEXT," + KEY_INSOPE + " TEXT," + KEY_INSNAM + " TEXT )";

            /* @TABLE_COMPANY_INSPECTOR */
            String CREATE_TABLE_COM_INS = "CREATE TABLE " + TABLE_APP_COMPANY_BY_INSPECTOR + "("
                    + KEY_COMPBYINSERID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_COMPBYINSPKCOM + " TEXT," + KEY_COMPBYINSPKINS + " TEXT )";

            /* @TABLE_DRIVER */
            String CREATE_TABLE_DRIVER = "CREATE TABLE " + TABLE_APP_DRIVER + "("
                    + KEY_DRIID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DRIVIDAPI + " TEXT," + KEY_DRIVNAM + " TEXT," + KEY_DRIVPKCOMP + " TEXT," + KEY_DRIVPKOPE + " TEXT )";

            /* @TABLE_COMPANY_DRIVER */
            String CREATE_TABLE_COM_DRI = "CREATE TABLE " + TABLE_APP_COMPANY_BY_DRIVER + "("
                    + KEY_COMPBYDRIID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_COMPBYDRIPKCOM + " TEXT," + KEY_COMPBYDRIPKOPE + " TEXT," + KEY_COMPBYDRIIDDRI + " TEXT )";

            /* @TABLE_TRANSPORT_UNIT */
            String CREATE_TABLETRANPORT_UNIT = "CREATE TABLE " + TABLE_APP_TRANSPORT_UNIT + "("
                    + KEY_TRAPORID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TRAPORPK + " TEXT," + KEY_TRAPORCOMPK + " TEXT," + KEY_TRAPOROPEPK + " TEXT,"
                    + KEY_TRAPORPLATCIS + " TEXT," + KEY_TRAPORPLATTRA + " TEXT )";

            /* @TABLE_COMPANY_TRANSPORT */
            String CREATE_TABLE_COM_TRANS = "CREATE TABLE " + TABLE_APP_COMPANY_BY_TRANSPORTUNIT + "("
                    + KEY_TRANSUNIBYDRIID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TRANSUNIBYDRIPKCOM + " TEXT," + KEY_TRANSUNIBYDRIPKOPE + " TEXT," + KEY_TRANSUNIBYDRIPKTRAN + " TEXT )";

            /* @TABLE_LOCATION */
            String CREATE_TABLE_LOCATION = "CREATE TABLE " + TABLE_APP_LOCATION + "("
                    + KEY_LOCAID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_LOCIDAPI + " TEXT,"
                    + KEY_LOCPKOPER + " TEXT," + KEY_LOCNAM + " TEXT, " + KEY_LOCLATITUDE + " NUMERIC," + KEY_LOCALTITUDE + " NUMERIC," + KEY_LOCLONGITUDE + " NUMERIC )";

            /* @TABLE_SECTION */
            String CREATE_TABLE_SECTION = "CREATE TABLE " + TABLE_APP_SECTION + "("
                    + KEY_SECID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_SECIDAPI + " TEXT," + KEY_SECNAM + " TEXT )";


            /* @TABLE_CHECKLIST */
            String CREATE_CHECKLIST = "CREATE TABLE " + TABLE_APP_CHECKLIST + "("
                    + KEY_CHECID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_CHECIDAPI + " TEXT,"
                    + KEY_CHECNUM + " TEXT,"
                    + KEY_CHECMIL + " TEXT,"
                    + KEY_CHECSIGDRIV + " TEXT,"
                    + KEY_CHECSIGSUPE + " TEXT,"
                    + KEY_CHECOPE + " TEXT,"
                    + KEY_CHECNAMOPE + " TEXT,"
                    + KEY_CHECROUTE + " INTEGER,"
                    + KEY_CHECNAMROUTE + " TEXT,"
                    + KEY_CHECTYPCHE + " TEXT,"
                    + KEY_CHECTYP + " TEXT,"
                    + KEY_CHECUNITCONFIG + " TEXT,"
                    + KEY_CHECDAT + " TEXT,"
                    + KEY_CHECSTA + " TEXT,"
                    + KEY_CHECK_LONGITUDE + " TEXT,"
                    + KEY_CHECK_LATITUDE + " TEXT,"
                    + KEY_CHECDRI + " TEXT,"
                    + KEY_CHECSUP + " TEXT,"
                    + KEY_CHECTRA + " TEXT,"
                    + KEY_CHECNAMDRI + " TEXT,"
                    + KEY_CHECNAMINS + " TEXT,"
                    + KEY_CHECNAMLOC + " TEXT,"
                    + KEY_CHECNAMSUP + " TEXT,"
                    + KEY_CHECNAMTRA + " TEXT,"
                    + KEY_CHECCISIMA + " TEXT,"
                    + KEY_CHECCOMSHI + " TEXT,"
                    + KEY_CHECNAMCOMSHI + " TEXT,"
                    + KEY_CHECINS + " TEXT,"
                    + KEY_CHECLOC + " TEXT,"
                    + KEY_CHECIDSQLLOC + " TEXT,"
                    + KEY_CHECENV + " TEXT,"
                    + KEY_CHECPLATRAC + " TEXT,"
                    + KEY_CHECTRACIMA + " TEXT,"
                    + KEY_CHECPLACIST + " TEXT,"
                    + KEY_CHECLASCHE + " TEXT,"
                    + KEY_CHECMANYEA + " TEXT,"
                    + KEY_CHECPROGRESS + " TEXT)";

            /* @TABLE_ITEM */
            String CREATE_TABLE_ITEM = "CREATE TABLE " + TABLE_APP_ITEM + "("
                    + KEY_ITEMID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ITEMIDAPI + " TEXT,"
                    + KEY_ITEMDUEDAT + " TEXT," + KEY_ITEMOBSDES + " TEXT," + KEY_ITEMOBSLIF + " TEXT," + KEY_ITEMOBSLIFDAT + " TEXT,"
                    + KEY_ITEMCOMIMA + " TEXT," + KEY_ITEMNONCOMIMA + " TEXT," + KEY_ITEMOBSLIFIMA + " TEXT," + KEY_ITEMSTA + " TEXT,"
                    + KEY_ITEMCHELIS + " TEXT," +
                    KEY_ITEMCHELISITE + " TEXT," +
                    KEY_ITEMIDCHEK + " TEXT," +
                    KEY_ITEM_OPERATIONS + " TEXT," +
                    KEY_ITEMEDI + " TEXT,"
                    + KEY_ITEMIDSEC + " TEXT," +
                    KEY_ITEMNAM + " TEXT," +
                    KEY_ITEMTRACTIMA + " TEXT," +
                    KEY_ITEMCISIMA + " TEXT, " +
                    KEY_ITEMREVIEWED + " TEXT )";

            /* @TABLE_LEV_OBS */
            String CREATE_TABLE_LEVOBS = "CREATE TABLE " + TABLE_APP_LEB_OBS + "("
                    + KEY_LEVOBSID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_LEVOBSPKAPI + " TEXT, " + KEY_LEVOBSIDITEM + " TEXT, " + KEY_LEVOBSCHELIS + " TEXT," + KEY_LEVOBSCHELISDATE
                    + " TEXT," + KEY_LEVOBSDUEDAT +
                    " TEXT," + KEY_LEVOBSTRACOM +
                    " TEXT," + KEY_LEVOBSTRAUNI +
                    " TEXT," + KEY_LEVOBSOBSDES +
                    " TEXT," + KEY_LEVOBSSEC +
                    " TEXT," + KEY_LEVOBSSEV +
                    " TEXT," + KEY_LEV_OBS_OBS_LIFTING +
                    " TEXT," + KEY_LEVOBSCHELISITE +
                    " TEXT," + KEY_LEVOBSSTATE +
                    " TEXT )";


            /* @TABLE_CARRIER */
            String CREATE_TABLE_CARRIER = "CREATE TABLE " + TABLE_APP_CARRIER + "("
                    + KEY_CARRID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_CARRPK + " TEXT," + KEY_CARRNAM + " TEXT," + KEY_CARRCOM + " TEXT," + KEY_CARROPE + " TEXT )";

            /* @TABLE_CARRIER_OPERATION */
            String CREATE_TABLE_CARR_OPE = "CREATE TABLE " + TABLE_APP_CARRIER_BY_OPERATION + "("
                    + KEY_CARRBYOPEID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_CARRBYOPEPKCOM + " TEXT," + KEY_CARRBYOPERPKSUP + " TEXT )";


            /* @TABLE_ROUTE_OPERATION */
            String CREATE_TABLE_ROUTE_OPERATION = "CREATE TABLE " + TABLE_APP_ROUTE + "("
                    + KEY_ROUTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ROUTE_API + " TEXT," + KEY_ROUTE_PK_OPE + " TEXT," + KEY_ROUTE_NAM + " TEXT )";


            String CREATE_TABLE_IMAGE = "CREATE TABLE " + TABLE_APP_IMAGE + "("
                    + KEY_IMAID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_IMANAM + " TEXT,"
                    + KEY_IMAORDNUM + " TEXT,"
                    + KEY_IMASTA + " TEXT,"
                    + KEY_IMA_LATITUDE + " TEXT,"
                    + KEY_IMA_LONGITUDE + " TEXT,"
                    + KEY_IMA_DATETIME + " TEXT,"
                    + KEY_IMA_LABEL + " TEXT,"
                    + KEY_IMA_URL + " TEXT,"
                    + KEY_IMA_COMMENT + " TEXT,"
                    + KEY_IMATYP + " TEXT,"
                    + KEY_IMAOWN + " TEXT )";

            /* @EXECSQL_CREATE */
            db.execSQL(CREATE_TABLE_OPERATION);
            db.execSQL(CREATE_TABLE_COMPANY);
            db.execSQL(CREATE_TABLE_COM_OPE);
            db.execSQL(CREATE_TABLE_SUPERVISOR);
            db.execSQL(CREATE_TABLE_COM_SUP);
            db.execSQL(CREATE_TABLE_INSPECTOR);
            db.execSQL(CREATE_TABLE_DRIVER);
            db.execSQL(CREATE_TABLE_COM_DRI);
            db.execSQL(CREATE_TABLETRANPORT_UNIT);
            db.execSQL(CREATE_TABLE_COM_TRANS);
            db.execSQL(CREATE_TABLE_LOCATION);
            db.execSQL(CREATE_TABLE_SECTION);
            db.execSQL(CREATE_TABLE_ITEM);
            db.execSQL(CREATE_CHECKLIST);
            db.execSQL(CREATE_TABLE_LEVOBS);
            db.execSQL(CREATE_TABLE_COM_INS);
            db.execSQL(CREATE_TABLE_CARRIER);
            db.execSQL(CREATE_TABLE_CARR_OPE);
            db.execSQL(CREATE_TABLE_ROUTE_OPERATION);
            db.execSQL(CREATE_TABLE_IMAGE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            /* @EXECSQL_DROP */
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_OPERATION);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_COMPANY);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_COMPANY_BY_OPERATION);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_SUPERVISOR);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_COMPANY_BY_SUPERVISOR);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_INSPECTOR);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_COMPANY_BY_INSPECTOR);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_DRIVER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_ROUTE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_COMPANY_BY_DRIVER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_TRANSPORT_UNIT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_COMPANY_BY_TRANSPORTUNIT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_LOCATION);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_SECTION);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_ITEM);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_CHECKLIST);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_LEB_OBS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_CARRIER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_CARRIER_BY_OPERATION);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_IMAGE);
            onCreate(db);
        }


        /* @CLASS_OPERATION_SQL */
        public class OperationSql {
            public OperationSql() {
            }

            public void deleteOperationTable() {
                SQLiteDatabase db = databasehelp.getReadableDatabase();
                db.delete(TABLE_APP_OPERATION, null, null);
            }

            public void deleteOperationIdApi(int id) {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                db.delete(TABLE_APP_OPERATION, KEY_OPEPK + " = ?",
                        new String[]{String.valueOf(id)});
            }

            public void addOperation(String pk, String name) {

                SQLiteDatabase db = databasehelp.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(KEY_OPEPK, pk);
                values.put(KEY_OPENAM, name);
                db.insert(TABLE_APP_OPERATION, null, values);
            }

            public ArrayList<OperacionModel> getAllOperations() {
                ArrayList<OperacionModel> models = new ArrayList<>();

                String selectQuery = "SELECT * FROM " + TABLE_APP_OPERATION;
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);

                if (cursor.moveToFirst()) {
                    do {
                        OperacionModel model = new OperacionModel();
                        model.setId_sql(cursor.getInt(cursor.getColumnIndex(KEY_OPEID)));
                        model.setPk(cursor.getInt(cursor.getColumnIndex(KEY_OPEPK)));
                        model.setName(cursor.getString(cursor.getColumnIndex(KEY_OPENAM)));
                        model.operation_id = cursor.getString(cursor.getColumnIndex(KEY_OPECHECKID));
                        models.add(model);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return models;
            }

        }

        /* @CLASS_SUPERVISOR */
        public class SupervisorSql {

            public SupervisorSql() {
            }

            public void deleteSupervisorSqlTable() {
                SQLiteDatabase db = databasehelp.getReadableDatabase();
                db.delete(TABLE_APP_SUPERVISOR, null, null);
            }

            public void deleteSupervisorIdApi(int id) {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                db.delete(TABLE_APP_SUPERVISOR, KEY_SUPPK + " = ?",
                        new String[]{String.valueOf(id)});
            }

            public void addSupervisorSql(String pk, String name, String company, String operation) {

                SQLiteDatabase db = databasehelp.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(KEY_SUPPK, pk);
                values.put(KEY_SUPNAM, name);
                values.put(KEY_SUPCOM, company);
                values.put(KEY_SUPCOPE, operation);
                db.insert(TABLE_APP_SUPERVISOR, null, values);
            }

            public ArrayList<SupervisorModel> getAllInspectorByPkCompanyAndPkOperation(String pkOperation) {
                ArrayList<SupervisorModel> models = new ArrayList<>();

                String selectQuery = "SELECT * FROM " + TABLE_APP_SUPERVISOR + " WHERE " + KEY_SUPCOPE + " ='" + pkOperation + "'";
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);

                if (cursor.moveToFirst()) {
                    do {
                        SupervisorModel model = new SupervisorModel();
                        model.setId_sql(cursor.getInt(cursor.getColumnIndex(KEY_SUPID)));
                        model.setPk(cursor.getInt(cursor.getColumnIndex(KEY_SUPPK)));
                        model.setParty(cursor.getString(cursor.getColumnIndex(KEY_SUPNAM)));
                        model.setPkCompany(cursor.getString(cursor.getColumnIndex(KEY_SUPCOM)));
                        model.setPkOperation(cursor.getString(cursor.getColumnIndex(KEY_SUPCOPE)));
                        models.add(model);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return models;
            }
        }

        /* @CLASS_INSPECTOR */
        public class InspectorSql {

            public InspectorSql() {
            }

            public void deleteInspectoTable() {
                SQLiteDatabase db = databasehelp.getReadableDatabase();
                db.delete(TABLE_APP_INSPECTOR, null, null);
            }

            public void deleteInspectorIdApi(int id) {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                db.delete(TABLE_APP_INSPECTOR, KEY_INSPK + " = ?",
                        new String[]{String.valueOf(id)});
            }

            public void addInspectorSql(String pk, String name, String operation, String company, String name_company) {

                SQLiteDatabase db = databasehelp.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(KEY_INSPK, pk);
                values.put(KEY_INSNAM, name);
                values.put(KEY_INSOPE, operation);
                values.put(KEY_INSCOM, company);
                values.put(KEY_INSNAMCOMPANY, name_company);
                db.isOpen();
                db.insert(TABLE_APP_INSPECTOR, null, values);
            }

            public ArrayList<InspectorModel> getAllInspectorByPkCompanyAndPkOperation(String pkOperation) {
                ArrayList<InspectorModel> models = new ArrayList<>();
                String selectQuery = "SELECT * FROM " + TABLE_APP_INSPECTOR + " WHERE " + KEY_INSOPE + "='" + pkOperation + "'";
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do {
                        InspectorModel model = new InspectorModel();
                        model.setId_sql(cursor.getInt(cursor.getColumnIndex(KEY_INSID)));
                        model.setPk(cursor.getInt(cursor.getColumnIndex(KEY_INSPK)));
                        model.setParty(cursor.getString(cursor.getColumnIndex(KEY_INSNAM)));
                        model.setPkCompany(cursor.getString(cursor.getColumnIndex(KEY_INSCOM)));
                        model.setPkOperation(cursor.getString(cursor.getColumnIndex(KEY_INSOPE)));
                        model.setName_company(cursor.getString(cursor.getColumnIndex(KEY_INSNAMCOMPANY)));
                        models.add(model);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return models;
            }

        }

        /* @CLASS_DRIVER */
        public class DriverSql {

            public DriverSql() {
            }

            public void deleteDriverTable() {
                SQLiteDatabase db = databasehelp.getReadableDatabase();
                db.delete(TABLE_APP_DRIVER, null, null);
            }

            public void deleteDriverIdApi(int id) {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                db.delete(TABLE_APP_DRIVER, KEY_DRIVIDAPI + " = ?",
                        new String[]{String.valueOf(id)});
            }

            public void addDriverSql(String pk, String name, String company, String operation) {

                SQLiteDatabase db = databasehelp.getReadableDatabase();
                ContentValues values = new ContentValues();
                values.put(KEY_DRIVIDAPI, pk);
                values.put(KEY_DRIVNAM, name);
                values.put(KEY_DRIVPKCOMP, company);
                values.put(KEY_DRIVPKOPE, operation);
                db.insert(TABLE_APP_DRIVER, null, values);

            }

            public ArrayList<DriverModel> getAllDriverByComAndOpe(String pkOperation) {
                ArrayList<DriverModel> models = new ArrayList<>();

                String selectQuery = "SELECT * FROM " + TABLE_APP_DRIVER + " WHERE " + KEY_DRIVPKOPE + "='" + pkOperation + "'";
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);

                if (cursor.moveToFirst()) {
                    do {
                        DriverModel model = new DriverModel();
                        model.setId_sql(cursor.getInt(cursor.getColumnIndex(KEY_DRIID)));
                        model.setId(cursor.getInt(cursor.getColumnIndex(KEY_DRIVIDAPI)));
                        model.setName_driver(cursor.getString(cursor.getColumnIndex(KEY_DRIVNAM)));
                        model.setPk_company(cursor.getString(cursor.getColumnIndex(KEY_DRIVPKCOMP)));
                        model.setPk_operation(cursor.getString(cursor.getColumnIndex(KEY_DRIVPKOPE)));

                        models.add(model);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return models;
            }
        }

        /* @CLASS_DRIVER */
        public class RouteSql {

            public RouteSql() {
            }

            public void deleteRouteTable() {
                SQLiteDatabase db = databasehelp.getReadableDatabase();
                db.delete(TABLE_APP_ROUTE, null, null);
            }

            public void addRouteSql(String pk, String name, String operation) {
                SQLiteDatabase db = databasehelp.getReadableDatabase();
                ContentValues values = new ContentValues();
                values.put(KEY_ROUTE_API, pk);
                values.put(KEY_ROUTE_NAM, name);
                values.put(KEY_ROUTE_PK_OPE, operation);
                db.insert(TABLE_APP_ROUTE, null, values);
            }

            public ArrayList<RouteModel> getRouteByOpe(String pkOperation) {
                ArrayList<RouteModel> models = new ArrayList<>();

                String selectQuery = "SELECT * FROM " + TABLE_APP_ROUTE + " WHERE " + KEY_ROUTE_PK_OPE + "='" + pkOperation + "'";
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);

                if (cursor.moveToFirst()) {
                    do {
                        RouteModel model = new RouteModel();
                        model.setId_sql(cursor.getInt(cursor.getColumnIndex(KEY_ROUTE_ID)));
                        model.setId_api(cursor.getInt(cursor.getColumnIndex(KEY_ROUTE_API)));
                        model.setName_route(cursor.getString(cursor.getColumnIndex(KEY_ROUTE_NAM)));
                        model.setPk_operation(cursor.getInt(cursor.getColumnIndex(KEY_ROUTE_PK_OPE)));

                        models.add(model);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return models;
            }
        }

        /* @CLASS_TRANPORT_UNITSQL */
        public class TranportUnitSql {
            public TranportUnitSql() {
            }

            public void deleteUnitTable() {
                SQLiteDatabase db = databasehelp.getReadableDatabase();
                db.delete(TABLE_APP_TRANSPORT_UNIT, null, null);
            }

            public void deleteUnitIdApi(int id) {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                db.delete(TABLE_APP_TRANSPORT_UNIT, KEY_TRAPORPK + " = ?",
                        new String[]{String.valueOf(id)});
            }

            public void addUnit(TransportUnitModel model) {

                SQLiteDatabase db = databasehelp.getReadableDatabase();
                ContentValues values = new ContentValues();
                values.put(KEY_TRAPORPK, model.getId());
                values.put(KEY_TRAPORPLATCIS, model.getPlate_cistern());
                values.put(KEY_TRAPORPLATTRA, model.getPlate_tracto());
                values.put(KEY_TRAPORCOMPK, model.getPk_company());
                values.put(KEY_TRAPOROPEPK, model.getPk_operation());
                db.insert(TABLE_APP_TRANSPORT_UNIT, null, values);
            }

            public ArrayList<TransportUnitModel> getAllUnitsByComAndOpe(String operation) {
                ArrayList<TransportUnitModel> models = new ArrayList<>();
                String selectQuery = "SELECT * FROM " + TABLE_APP_TRANSPORT_UNIT + " WHERE " + KEY_TRAPOROPEPK + "='" + operation + "'";
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);

                if (cursor.moveToFirst()) {
                    do {
                        TransportUnitModel model = new TransportUnitModel();
                        model.setId_Sql(cursor.getInt(cursor.getColumnIndex(KEY_TRAPORID)));
                        model.setId(cursor.getInt(cursor.getColumnIndex(KEY_TRAPORPK)));
                        model.setPlate_cistern(cursor.getString(cursor.getColumnIndex(KEY_TRAPORPLATCIS)));
                        model.setPlate_tracto(cursor.getString(cursor.getColumnIndex(KEY_TRAPORPLATTRA)));
                        model.setPk_company(cursor.getString(cursor.getColumnIndex(KEY_TRAPORCOMPK)));
                        model.setPk_operation(cursor.getString(cursor.getColumnIndex(KEY_TRAPOROPEPK)));
                        models.add(model);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return models;
            }
        }

        /* @CLASS_LOCATION */
        public class LocationSql {

            public LocationSql() {
            }

            public void deleteLocationTable() {
                SQLiteDatabase db = databasehelp.getReadableDatabase();
                db.delete(TABLE_APP_LOCATION, null, null);
            }

            public String addLocationSql(LocationModel model) {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(KEY_LOCIDAPI, model.getId());
                values.put(KEY_LOCNAM, model.getName());
                values.put(KEY_LOCPKOPER, model.getPk_operation());
                values.put(KEY_LOCLATITUDE, model.latitude);
                values.put(KEY_LOCLONGITUDE, model.longitude);
                values.put(KEY_LOCALTITUDE, model.altitude);
                long id = db.insert(TABLE_APP_LOCATION, null, values);
                return String.valueOf(id);
            }

            public ArrayList<LocationModel> getAllLocation(String pkOperation) {
                ArrayList<LocationModel> models = new ArrayList<>();

                String selectQuery = "SELECT * FROM " + TABLE_APP_LOCATION + " WHERE " + KEY_LOCPKOPER + " ='" + pkOperation + "'";
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);

                if (cursor.moveToFirst()) {
                    do {
                        LocationModel model = new LocationModel();
                        model.setId_Sql(cursor.getInt(cursor.getColumnIndex(KEY_LOCAID)));
                        model.setId(cursor.getInt(cursor.getColumnIndex(KEY_LOCIDAPI)));
                        model.setName(cursor.getString(cursor.getColumnIndex(KEY_LOCNAM)));
                        model.setPk_operation(cursor.getInt(cursor.getColumnIndex(KEY_LOCPKOPER)));
                        model.setLatitude(cursor.getDouble(cursor.getColumnIndex(KEY_LOCLATITUDE)));
                        model.setLongitude(cursor.getDouble(cursor.getColumnIndex(KEY_LOCLONGITUDE)));
                        model.setAltitude(cursor.getDouble(cursor.getColumnIndex(KEY_LOCALTITUDE)));
                        models.add(model);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return models;
            }

            public ArrayList<LocationModel> getLocationById(String idsql) {
                ArrayList<LocationModel> models = new ArrayList<>();

                String selectQuery = "SELECT * FROM " + TABLE_APP_LOCATION + " WHERE " + KEY_LOCAID + " ='" + idsql + "'";
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);

                if (cursor.moveToFirst()) {
                    do {
                        LocationModel model = new LocationModel();
                        model.setId_Sql(cursor.getInt(cursor.getColumnIndex(KEY_LOCAID)));
                        model.setId(cursor.getInt(cursor.getColumnIndex(KEY_LOCIDAPI)));
                        model.setName(cursor.getString(cursor.getColumnIndex(KEY_LOCNAM)));
                        model.setPk_operation(cursor.getInt(cursor.getColumnIndex(KEY_LOCPKOPER)));
                        model.setLatitude(cursor.getDouble(cursor.getColumnIndex(KEY_LOCLATITUDE)));
                        model.setLongitude(cursor.getDouble(cursor.getColumnIndex(KEY_LOCLONGITUDE)));
                        model.setAltitude(cursor.getDouble(cursor.getColumnIndex(KEY_LOCALTITUDE)));
                        models.add(model);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return models;
            }
        }

        /* @CLASS_ITEMSQL */
        public class ItemSql {
            public ItemSql() {
            }

            public void deleteItemTable() {
                SQLiteDatabase db = databasehelp.getReadableDatabase();
                db.delete(TABLE_APP_ITEM, null, null);
            }

            public void deleteItem(int id) {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                db.delete(TABLE_APP_ITEM, KEY_ITEMID + " = ?",
                        new String[]{String.valueOf(id)});
            }

            public void deleteItemChecklistTemplate() {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                db.delete(TABLE_APP_ITEM, KEY_ITEMIDCHEK + " = ?",
                        new String[]{"plantilla"});
            }

            public void deleteItemApi(int id) {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                db.delete(TABLE_APP_ITEM, KEY_ITEMIDAPI + " = ?",
                        new String[]{String.valueOf(id)});
            }

            public void deleteItemCheckListNoCloud(String number) {
                SQLiteDatabase db = databasehelp.getReadableDatabase();
                db.execSQL("DELETE FROM " + TABLE_APP_ITEM + " WHERE EXISTS (SELECT " + KEY_CHECNUM + " FROM " + TABLE_APP_CHECKLIST + " WHERE '" + number + "' != " + TABLE_APP_CHECKLIST + "." + KEY_CHECNUM + ")");
            }

            public void addItems(List<ItemCheckListModel> list) {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                db.beginTransaction();
                try {
                    ContentValues values = new ContentValues();
                    for (ItemCheckListModel model : list) {
                        values.put(KEY_ITEMIDAPI, model.getId());
                        values.put(KEY_ITEMNAM, model.getName());
                        values.put(KEY_ITEMIDCHEK, model.getId_check_list());
                        values.put(KEY_ITEMIDSEC, model.getId_section());
                        values.put(KEY_ITEMCHELIS, model.getCheck_list());
                        values.put(KEY_ITEMCHELISITE, model.getCheck_list_item());
                        values.put(KEY_ITEMSTA, model.getState());
                        values.put(KEY_ITEMOBSDES, model.getObservation_description());
                        values.put(KEY_ITEMOBSLIF, model.getObservation_lifting());
                        values.put(KEY_ITEMOBSLIFDAT, model.getObservation_lifting_date());
                        values.put(KEY_ITEMOBSLIFIMA, model.getObservation_lifting_image());
                        values.put(KEY_ITEMNONCOMIMA, model.getNon_compliance_image());
                        values.put(KEY_ITEMCOMIMA, model.getCompliance_image());
                        if (model.getOperations() != null) {
                            String operations = "";
                            for (int i = 0; i < model.getOperations().size(); i++) {
                                operations = operations + model.getOperations().get(i) + "***";
                            }
                            values.put(KEY_ITEM_OPERATIONS, operations);
                        }
                        values.put(KEY_ITEMEDI, "no");
                        values.put(KEY_ITEMREVIEWED, model.getReviewed() ? 1 : 0);
                        db.insert(TABLE_APP_ITEM, null, values);
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }

            public void addItem(ItemCheckListModel model) {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(KEY_ITEMIDAPI, model.getId());
                values.put(KEY_ITEMNAM, model.getName());
                values.put(KEY_ITEMIDCHEK, model.getId_check_list());
                values.put(KEY_ITEMIDSEC, model.getId_section());
                values.put(KEY_ITEMCHELIS, model.getCheck_list());
                values.put(KEY_ITEMCHELISITE, model.getCheck_list_item());
                values.put(KEY_ITEMSTA, model.getState());
                values.put(KEY_ITEMOBSDES, model.getObservation_description());
                values.put(KEY_ITEMOBSLIF, model.getObservation_lifting());
                values.put(KEY_ITEMOBSLIFDAT, model.getObservation_lifting_date());
                values.put(KEY_ITEMOBSLIFIMA, model.getObservation_lifting_image());
                values.put(KEY_ITEMNONCOMIMA, model.getNon_compliance_image());
                values.put(KEY_ITEMCOMIMA, model.getCompliance_image());
                if (model.getOperations() != null) {
                    String operations = "";
                    for (int i = 0; i < model.getOperations().size(); i++) {
                        operations = operations + model.getOperations().get(i) + "***";
                    }
                    values.put(KEY_ITEM_OPERATIONS, operations);
                }
                values.put(KEY_ITEMEDI, "no");
                values.put(KEY_ITEMREVIEWED, model.getReviewed() ? 1 : 0);
                db.insert(TABLE_APP_ITEM, null, values);
            }

            public ArrayList<ItemCheckListModel> getAllItemsByCheck(String idCheck) {
                ArrayList<ItemCheckListModel> models = new ArrayList<>();

                String selectQuery = "SELECT * FROM " + TABLE_APP_ITEM + " WHERE " + KEY_ITEMIDCHEK + " ='" + idCheck + "'";
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);

                if (cursor.moveToFirst()) {
                    do {
                        ItemCheckListModel model = new ItemCheckListModel();
                        model.setIdSql(cursor.getInt(cursor.getColumnIndex(KEY_ITEMID)));
                        model.setId(cursor.getInt(cursor.getColumnIndex(KEY_ITEMIDAPI)));
                        model.setName(cursor.getString(cursor.getColumnIndex(KEY_ITEMNAM)));
                        model.setId_section(cursor.getString(cursor.getColumnIndex(KEY_ITEMIDSEC)));
                        model.setId_check_list(cursor.getString(cursor.getColumnIndex(KEY_ITEMIDCHEK)));
                        model.setDue_date(cursor.getString(cursor.getColumnIndex(KEY_ITEMDUEDAT)));
                        model.setObservation_description(cursor.getString(cursor.getColumnIndex(KEY_ITEMOBSDES)));
                        model.setObservation_lifting(cursor.getString(cursor.getColumnIndex(KEY_ITEMOBSLIF)));
                        model.setObservation_lifting_date(cursor.getString(cursor.getColumnIndex(KEY_ITEMOBSLIFDAT)));
                        model.setCompliance_image(cursor.getString(cursor.getColumnIndex(KEY_ITEMCOMIMA)));
                        model.setNon_compliance_image(cursor.getString(cursor.getColumnIndex(KEY_ITEMNONCOMIMA)));
                        model.setObservation_lifting_image(cursor.getString(cursor.getColumnIndex(KEY_ITEMOBSLIFIMA)));
                        model.setState(cursor.getString(cursor.getColumnIndex(KEY_ITEMSTA)));
                        model.setOperations_text(cursor.getString(cursor.getColumnIndex(KEY_ITEM_OPERATIONS)));
                        model.setCheck_list(cursor.getInt(cursor.getColumnIndex(KEY_ITEMCHELIS)));
                        model.setCheck_list_item(cursor.getInt(cursor.getColumnIndex(KEY_ITEMCHELISITE)));
                        model.setEditado(cursor.getString(cursor.getColumnIndex(KEY_ITEMEDI)));
                        model.setReviewed(cursor.getInt(cursor.getColumnIndex(KEY_ITEMREVIEWED)) == 1);
                        models.add(model);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return models;
            }

            public ArrayList<ItemCheckListModel> getAllItemsByCheckEdit(String idCheck) {
                ArrayList<ItemCheckListModel> models = new ArrayList<>();

                String selectQuery = "SELECT * FROM " + TABLE_APP_ITEM + " WHERE " + KEY_ITEMIDCHEK + " ='" + idCheck + "' AND " + KEY_ITEMEDI + " = 'si'";
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);

                if (cursor.moveToFirst()) {
                    do {
                        ItemCheckListModel model = new ItemCheckListModel();
                        model.setIdSql(cursor.getInt(cursor.getColumnIndex(KEY_ITEMID)));
                        model.setId(cursor.getInt(cursor.getColumnIndex(KEY_ITEMIDAPI)));
                        model.setName(cursor.getString(cursor.getColumnIndex(KEY_ITEMNAM)));
                        model.setId_section(cursor.getString(cursor.getColumnIndex(KEY_ITEMIDSEC)));
                        model.setId_check_list(cursor.getString(cursor.getColumnIndex(KEY_ITEMIDCHEK)));
                        model.setDue_date(cursor.getString(cursor.getColumnIndex(KEY_ITEMDUEDAT)));
                        model.setObservation_description(cursor.getString(cursor.getColumnIndex(KEY_ITEMOBSDES)));
                        model.setObservation_lifting(cursor.getString(cursor.getColumnIndex(KEY_ITEMOBSLIF)));
                        model.setObservation_lifting_date(cursor.getString(cursor.getColumnIndex(KEY_ITEMOBSLIFDAT)));
                        model.setCompliance_image(cursor.getString(cursor.getColumnIndex(KEY_ITEMCOMIMA)));
                        model.setNon_compliance_image(cursor.getString(cursor.getColumnIndex(KEY_ITEMNONCOMIMA)));
                        model.setObservation_lifting_image(cursor.getString(cursor.getColumnIndex(KEY_ITEMOBSLIFIMA)));
                        model.setState(cursor.getString(cursor.getColumnIndex(KEY_ITEMSTA)));
                        model.setOperations_text(cursor.getString(cursor.getColumnIndex(KEY_ITEM_OPERATIONS)));
                        model.setCheck_list(cursor.getInt(cursor.getColumnIndex(KEY_ITEMCHELIS)));
                        model.setCheck_list_item(cursor.getInt(cursor.getColumnIndex(KEY_ITEMCHELISITE)));
                        model.setEditado(cursor.getString(cursor.getColumnIndex(KEY_ITEMEDI)));
                        model.setReviewed(cursor.getInt(cursor.getColumnIndex(KEY_ITEMREVIEWED)) == 1);
                        models.add(model);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return models;
            }

            public ArrayList<ItemCheckListModel> getItemsBySection(String idSection, String idCheck) {
                ArrayList<ItemCheckListModel> models = new ArrayList<>();

                String selectQuery = "SELECT * FROM " + TABLE_APP_ITEM + " WHERE " + KEY_ITEMIDSEC + " ='" + idSection + "' AND " + KEY_ITEMIDCHEK + " ='" + idCheck + "'";
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);

                if (cursor.moveToFirst()) {
                    do {
                        ItemCheckListModel model = new ItemCheckListModel();
                        model.setIdSql(cursor.getInt(cursor.getColumnIndex(KEY_ITEMID)));
                        model.setId(cursor.getInt(cursor.getColumnIndex(KEY_ITEMIDAPI)));
                        model.setName(cursor.getString(cursor.getColumnIndex(KEY_ITEMNAM)));
                        model.setId_section(cursor.getString(cursor.getColumnIndex(KEY_ITEMIDSEC)));
                        model.setId_check_list(cursor.getString(cursor.getColumnIndex(KEY_ITEMIDCHEK)));
                        model.setOperations_text(cursor.getString(cursor.getColumnIndex(KEY_ITEM_OPERATIONS)));

                        model.setDue_date(cursor.getString(cursor.getColumnIndex(KEY_ITEMDUEDAT)));
                        model.setObservation_description(cursor.getString(cursor.getColumnIndex(KEY_ITEMOBSDES)));
                        model.setObservation_lifting(cursor.getString(cursor.getColumnIndex(KEY_ITEMOBSLIF)));
                        model.setObservation_lifting_date(cursor.getString(cursor.getColumnIndex(KEY_ITEMOBSLIFDAT)));
                        model.setCompliance_image(cursor.getString(cursor.getColumnIndex(KEY_ITEMCOMIMA)));
                        model.setNon_compliance_image(cursor.getString(cursor.getColumnIndex(KEY_ITEMNONCOMIMA)));
                        model.setObservation_lifting_image(cursor.getString(cursor.getColumnIndex(KEY_ITEMOBSLIFIMA)));
                        model.setState(cursor.getString(cursor.getColumnIndex(KEY_ITEMSTA)));
                        model.setCheck_list(cursor.getInt(cursor.getColumnIndex(KEY_ITEMCHELIS)));
                        model.setCheck_list_item(cursor.getInt(cursor.getColumnIndex(KEY_ITEMCHELISITE)));
                        model.setEditado(cursor.getString(cursor.getColumnIndex(KEY_ITEMEDI)));
                        model.setReviewed(cursor.getInt(cursor.getColumnIndex(KEY_ITEMREVIEWED)) == 1);

                        models.add(model);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return models;
            }

            public void updateValueItemById(String param, String value, String idItem) {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(param, value);
                db.update(TABLE_APP_ITEM, values, KEY_ITEMID + " = ?", new String[]{idItem});
            }

            public void updateValueResponseItemById(String param, String id, String param2, String checklist, String idItem) {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(param, id);
                values.put(param2, checklist);
                db.update(TABLE_APP_ITEM, values, KEY_ITEMID + " = ?", new String[]{idItem});
            }

            public ItemCheckListModel getItemById(String idItem) {
                ItemCheckListModel model = new ItemCheckListModel();

                String selectQuery = "SELECT * FROM " + TABLE_APP_ITEM + " WHERE " + KEY_ITEMID + "='" + idItem + "'";
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);

                if (cursor.moveToFirst()) {
                    do {
                        model.setIdSql(cursor.getInt(cursor.getColumnIndex(KEY_ITEMID)));
                        model.setId(cursor.getInt(cursor.getColumnIndex(KEY_ITEMIDAPI)));
                        model.setName(cursor.getString(cursor.getColumnIndex(KEY_ITEMNAM)));
                        model.setId_section(cursor.getString(cursor.getColumnIndex(KEY_ITEMIDSEC)));
                        model.setId_check_list(cursor.getString(cursor.getColumnIndex(KEY_ITEMIDCHEK)));
                        model.setOperations_text(cursor.getString(cursor.getColumnIndex(KEY_ITEM_OPERATIONS)));

                        model.setDue_date(cursor.getString(cursor.getColumnIndex(KEY_ITEMDUEDAT)));
                        model.setObservation_description(cursor.getString(cursor.getColumnIndex(KEY_ITEMOBSDES)));
                        model.setObservation_lifting(cursor.getString(cursor.getColumnIndex(KEY_ITEMOBSLIF)));
                        model.setObservation_lifting_date(cursor.getString(cursor.getColumnIndex(KEY_ITEMOBSLIFDAT)));
                        model.setCompliance_image(cursor.getString(cursor.getColumnIndex(KEY_ITEMCOMIMA)));
                        model.setNon_compliance_image(cursor.getString(cursor.getColumnIndex(KEY_ITEMNONCOMIMA)));
                        model.setObservation_lifting_image(cursor.getString(cursor.getColumnIndex(KEY_ITEMOBSLIFIMA)));
                        model.setState(cursor.getString(cursor.getColumnIndex(KEY_ITEMSTA)));
                        model.setCheck_list(cursor.getInt(cursor.getColumnIndex(KEY_ITEMCHELIS)));
                        model.setCheck_list_item(cursor.getInt(cursor.getColumnIndex(KEY_ITEMCHELISITE)));
                        model.setEditado(cursor.getString(cursor.getColumnIndex(KEY_ITEMEDI)));
                        model.setReviewed(cursor.getInt(cursor.getColumnIndex(KEY_ITEMREVIEWED)) == 1);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return model;
            }

        }

        /* @CLASS_CHECKLIST */
        public class CheckListSql {
            public CheckListSql() {
            }

            public void deleteCheckListNoCloud(String number) {
                SQLiteDatabase db = databasehelp.getReadableDatabase();
                db.execSQL("DELETE FROM " + TABLE_APP_CHECKLIST + " WHERE " + KEY_CHECNUM + "!='" + number + "'");
            }

            public void deleteCheckListTable() {
                SQLiteDatabase db = databasehelp.getReadableDatabase();
                db.delete(TABLE_APP_CHECKLIST, null, null);
            }

            public void deleteCheckList(int id) {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                db.delete(TABLE_APP_CHECKLIST, KEY_CHECID + " = ?",
                        new String[]{String.valueOf(id)});
            }

            public void deleteCheckListIdApi(int id) {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                db.delete(TABLE_APP_CHECKLIST, KEY_CHECIDAPI + " = ?",
                        new String[]{String.valueOf(id)});
            }

            public String addcheckList(CheckListModel model) {

                SQLiteDatabase db = databasehelp.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(KEY_CHECIDAPI, model.getId());
                if (model.getType() != null) {
                    values.put(KEY_CHECTYP, model.getType());
                } else {
                    values.put(KEY_CHECTYP, "");
                }
                if (model.getUnit_configuration() != null) {
                    values.put(KEY_CHECUNITCONFIG, model.getUnit_configuration());
                } else {
                    values.put(KEY_CHECUNITCONFIG, "T3S3");
                }
                values.put(KEY_CHECTYPCHE, model.getType_check());
                values.put(KEY_CHECNUM, model.getNumber());
                values.put(KEY_CHECMIL, model.getMileage());
                values.put(KEY_CHECSIGDRIV, model.getDriver_signature());
                values.put(KEY_CHECSIGSUPE, model.getSupervisor_signature());
                values.put(KEY_CHECDAT, model.getDatetime());
                values.put(KEY_CHECSTA, model.getState());
                values.put(KEY_CHECROUTE, model.getRoute());
                values.put(KEY_CHECNAMROUTE, model.getName_route());
                values.put(KEY_CHECDRI, model.getDriver());
                values.put(KEY_CHECNAMDRI, model.getName_driver());
                values.put(KEY_CHECSUP, model.getSupervisor());
                values.put(KEY_CHECNAMSUP, model.getName_supervisor());
                values.put(KEY_CHECINS, model.getInspector());
                values.put(KEY_CHECNAMINS, model.getName_inspector());
                values.put(KEY_CHECLOC, model.getLocation());
                values.put(KEY_CHECIDSQLLOC, model.getId_location_sql());

                values.put(KEY_CHECK_LONGITUDE, model.getLongitude());
                values.put(KEY_CHECK_LATITUDE, model.getLatitude());

                values.put(KEY_CHECNAMLOC, model.getName_location());
                values.put(KEY_CHECTRA, model.getTransport_unit());
                values.put(KEY_CHECNAMOPE, model.getName_operation());
                values.put(KEY_CHECOPE, model.getOperation());
                values.put(KEY_CHECCOMSHI, model.getCompany_shipment());
                values.put(KEY_CHECNAMCOMSHI, model.getName_company_shipment());
                values.put(KEY_CHECENV, model.getEnviado());
                values.put(KEY_CHECPLATRAC, model.getPlate_tracto());
                values.put(KEY_CHECPLACIST, model.getPlate_cistern());
                values.put(KEY_CHECCISIMA, model.getCistern_image());
                values.put(KEY_CHECTRACIMA, model.getTracto_image());
                values.put(KEY_CHECLASCHE, model.getLastInspection());
                values.put(KEY_CHECMANYEA, model.getYearFabrication());
                values.put(KEY_CHECPROGRESS, model.getProgress());

                long id = db.insert(TABLE_APP_CHECKLIST, null, values);
                return String.valueOf(id);

            }

            public ArrayList<CheckListModel> getAllChecks() {
                ArrayList<CheckListModel> models = new ArrayList<>();
                String selectQuery = "SELECT * FROM " + TABLE_APP_CHECKLIST;
                SQLiteDatabase db = databasehelp.getWritableDatabase();

                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do {
                        CheckListModel model = new CheckListModel();
                        model.setIdSql(cursor.getInt(cursor.getColumnIndex(KEY_CHECID)));
                        model.setId(cursor.getInt(cursor.getColumnIndex(KEY_CHECIDAPI)));
                        model.setNumber(cursor.getString(cursor.getColumnIndex(KEY_CHECNUM)));
                        model.setDriver_signature(cursor.getString(cursor.getColumnIndex(KEY_CHECSIGDRIV)));
                        model.setSupervisor_signature(cursor.getString(cursor.getColumnIndex(KEY_CHECSIGSUPE)));
                        model.setMileage(cursor.getString(cursor.getColumnIndex(KEY_CHECMIL)));
                        model.setType_check(cursor.getString(cursor.getColumnIndex(KEY_CHECTYPCHE)));
                        model.setType(cursor.getString(cursor.getColumnIndex(KEY_CHECTYP)));
                        model.setUnit_configuration(cursor.getString(cursor.getColumnIndex(KEY_CHECUNITCONFIG)));
                        model.setDatetime(cursor.getString(cursor.getColumnIndex(KEY_CHECDAT)));
                        model.setState(cursor.getString(cursor.getColumnIndex(KEY_CHECSTA)));
                        model.setDriver(cursor.getInt(cursor.getColumnIndex(KEY_CHECDRI)));
                        model.setName_driver(cursor.getString(cursor.getColumnIndex(KEY_CHECNAMDRI)));
                        model.setRoute(cursor.getInt(cursor.getColumnIndex(KEY_CHECROUTE)));
                        model.setName_route(cursor.getString(cursor.getColumnIndex(KEY_CHECNAMROUTE)));
                        model.setSupervisor(cursor.getInt(cursor.getColumnIndex(KEY_CHECSUP)));
                        model.setName_supervisor(cursor.getString(cursor.getColumnIndex(KEY_CHECNAMSUP)));
                        model.setInspector(cursor.getInt(cursor.getColumnIndex(KEY_CHECINS)));
                        model.setName_inspector(cursor.getString(cursor.getColumnIndex(KEY_CHECNAMINS)));
                        model.setLocation(cursor.getInt(cursor.getColumnIndex(KEY_CHECLOC)));

                        model.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_CHECK_LATITUDE)));
                        model.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_CHECK_LONGITUDE)));

                        model.setId_location_sql(cursor.getString(cursor.getColumnIndex(KEY_CHECIDSQLLOC)));
                        model.setName_location(cursor.getString(cursor.getColumnIndex(KEY_CHECNAMLOC)));
                        model.setTransport_unit(cursor.getInt(cursor.getColumnIndex(KEY_CHECTRA)));
                        model.setName_operation(cursor.getString(cursor.getColumnIndex(KEY_CHECNAMOPE)));
                        model.setOperation(cursor.getInt(cursor.getColumnIndex(KEY_CHECOPE)));
                        model.setName_company_shipment(cursor.getString(cursor.getColumnIndex(KEY_CHECNAMCOMSHI)));
                        model.setCompany_shipment(cursor.getInt(cursor.getColumnIndex(KEY_CHECCOMSHI)));
                        model.setEnviado(cursor.getString(cursor.getColumnIndex(KEY_CHECENV)));
                        model.setPlate_tracto(cursor.getString(cursor.getColumnIndex(KEY_CHECPLATRAC)));
                        model.setPlate_cistern(cursor.getString(cursor.getColumnIndex(KEY_CHECPLACIST)));
                        model.setCistern_image(cursor.getString(cursor.getColumnIndex(KEY_CHECCISIMA)));
                        model.setTracto_image(cursor.getString(cursor.getColumnIndex(KEY_CHECTRACIMA)));
                        model.setLastInspection(cursor.getString(cursor.getColumnIndex(KEY_CHECLASCHE)));
                        model.setYearFabrication(cursor.getString(cursor.getColumnIndex(KEY_CHECMANYEA)));
                        model.setProgress(cursor.getInt(cursor.getColumnIndex(KEY_CHECPROGRESS)));
                        models.add(model);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return models;
            }

            public ArrayList<CheckListModel> getAllChecksByParam(String param, String value) {
                ArrayList<CheckListModel> models = new ArrayList<>();

                String selectQuery = "SELECT * FROM " + TABLE_APP_CHECKLIST + " WHERE " + param + " ='" + value + "'";
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);

                if (cursor.moveToFirst()) {
                    do {
                        CheckListModel model = new CheckListModel();
                        model.setIdSql(cursor.getInt(cursor.getColumnIndex(KEY_CHECID)));
                        model.setId(cursor.getInt(cursor.getColumnIndex(KEY_CHECIDAPI)));
                        model.setNumber(cursor.getString(cursor.getColumnIndex(KEY_CHECNUM)));
                        model.setDriver_signature(cursor.getString(cursor.getColumnIndex(KEY_CHECSIGDRIV)));
                        model.setSupervisor_signature(cursor.getString(cursor.getColumnIndex(KEY_CHECSIGSUPE)));
                        model.setMileage(cursor.getString(cursor.getColumnIndex(KEY_CHECMIL)));
                        model.setType_check(cursor.getString(cursor.getColumnIndex(KEY_CHECTYPCHE)));
                        model.setType(cursor.getString(cursor.getColumnIndex(KEY_CHECTYP)));
                        model.setUnit_configuration(cursor.getString(cursor.getColumnIndex(KEY_CHECUNITCONFIG)));
                        model.setDatetime(cursor.getString(cursor.getColumnIndex(KEY_CHECDAT)));
                        model.setState(cursor.getString(cursor.getColumnIndex(KEY_CHECSTA)));
                        model.setRoute(cursor.getInt(cursor.getColumnIndex(KEY_CHECROUTE)));
                        model.setName_route(cursor.getString(cursor.getColumnIndex(KEY_CHECNAMROUTE)));
                        model.setDriver(cursor.getInt(cursor.getColumnIndex(KEY_CHECDRI)));
                        model.setName_driver(cursor.getString(cursor.getColumnIndex(KEY_CHECNAMDRI)));
                        model.setSupervisor(cursor.getInt(cursor.getColumnIndex(KEY_CHECSUP)));
                        model.setName_supervisor(cursor.getString(cursor.getColumnIndex(KEY_CHECNAMSUP)));
                        model.setInspector(cursor.getInt(cursor.getColumnIndex(KEY_CHECINS)));
                        model.setName_inspector(cursor.getString(cursor.getColumnIndex(KEY_CHECNAMINS)));
                        model.setLocation(cursor.getInt(cursor.getColumnIndex(KEY_CHECLOC)));
                        model.setId_location_sql(cursor.getString(cursor.getColumnIndex(KEY_CHECIDSQLLOC)));
                        model.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_CHECK_LATITUDE)));
                        model.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_CHECK_LONGITUDE)));
                        model.setName_location(cursor.getString(cursor.getColumnIndex(KEY_CHECNAMLOC)));
                        model.setTransport_unit(cursor.getInt(cursor.getColumnIndex(KEY_CHECTRA)));
                        model.setName_operation(cursor.getString(cursor.getColumnIndex(KEY_CHECNAMOPE)));
                        model.setOperation(cursor.getInt(cursor.getColumnIndex(KEY_CHECOPE)));
                        model.setName_company_shipment(cursor.getString(cursor.getColumnIndex(KEY_CHECNAMCOMSHI)));
                        model.setCompany_shipment(cursor.getInt(cursor.getColumnIndex(KEY_CHECCOMSHI)));
                        model.setEnviado(cursor.getString(cursor.getColumnIndex(KEY_CHECENV)));
                        model.setPlate_tracto(cursor.getString(cursor.getColumnIndex(KEY_CHECPLATRAC)));
                        model.setPlate_cistern(cursor.getString(cursor.getColumnIndex(KEY_CHECPLACIST)));
                        model.setCistern_image(cursor.getString(cursor.getColumnIndex(KEY_CHECCISIMA)));
                        model.setTracto_image(cursor.getString(cursor.getColumnIndex(KEY_CHECTRACIMA)));

                        models.add(model);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return models;
            }

            public CheckListModel getCheck(String idCheckListSql) {
                CheckListModel model = new CheckListModel();

                String selectQuery = "SELECT * FROM " + TABLE_APP_CHECKLIST + " WHERE " + KEY_CHECID + " ='" + idCheckListSql + "'";
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);

                if (cursor.moveToFirst()) {
                    do {
                        model.setIdSql(cursor.getInt(cursor.getColumnIndex(KEY_CHECID)));
                        model.setId(cursor.getInt(cursor.getColumnIndex(KEY_CHECIDAPI)));
                        model.setNumber(cursor.getString(cursor.getColumnIndex(KEY_CHECNUM)));
                        model.setDriver_signature(cursor.getString(cursor.getColumnIndex(KEY_CHECSIGDRIV)));
                        model.setSupervisor_signature(cursor.getString(cursor.getColumnIndex(KEY_CHECSIGSUPE)));
                        model.setMileage(cursor.getString(cursor.getColumnIndex(KEY_CHECMIL)));
                        model.setType_check(cursor.getString(cursor.getColumnIndex(KEY_CHECTYPCHE)));
                        model.setType(cursor.getString(cursor.getColumnIndex(KEY_CHECTYP)));
                        model.setUnit_configuration(cursor.getString(cursor.getColumnIndex(KEY_CHECUNITCONFIG)));
                        model.setDatetime(cursor.getString(cursor.getColumnIndex(KEY_CHECDAT)));
                        model.setState(cursor.getString(cursor.getColumnIndex(KEY_CHECSTA)));
                        model.setDriver(cursor.getInt(cursor.getColumnIndex(KEY_CHECDRI)));
                        model.setName_driver(cursor.getString(cursor.getColumnIndex(KEY_CHECNAMDRI)));
                        model.setRoute(cursor.getInt(cursor.getColumnIndex(KEY_CHECROUTE)));
                        model.setName_route(cursor.getString(cursor.getColumnIndex(KEY_CHECNAMROUTE)));
                        model.setSupervisor(cursor.getInt(cursor.getColumnIndex(KEY_CHECSUP)));
                        model.setName_supervisor(cursor.getString(cursor.getColumnIndex(KEY_CHECNAMSUP)));
                        model.setInspector(cursor.getInt(cursor.getColumnIndex(KEY_CHECINS)));
                        model.setName_inspector(cursor.getString(cursor.getColumnIndex(KEY_CHECNAMINS)));
                        model.setId_location_sql(cursor.getString(cursor.getColumnIndex(KEY_CHECIDSQLLOC)));
                        model.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_CHECK_LATITUDE)));
                        model.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_CHECK_LONGITUDE)));
                        model.setLocation(cursor.getInt(cursor.getColumnIndex(KEY_CHECLOC)));
                        model.setName_location(cursor.getString(cursor.getColumnIndex(KEY_CHECNAMLOC)));
                        model.setTransport_unit(cursor.getInt(cursor.getColumnIndex(KEY_CHECTRA)));
                        model.setName_operation(cursor.getString(cursor.getColumnIndex(KEY_CHECNAMOPE)));
                        model.setOperation(cursor.getInt(cursor.getColumnIndex(KEY_CHECOPE)));
                        model.setName_company_shipment(cursor.getString(cursor.getColumnIndex(KEY_CHECNAMCOMSHI)));
                        model.setCompany_shipment(cursor.getInt(cursor.getColumnIndex(KEY_CHECCOMSHI)));
                        model.setEnviado(cursor.getString(cursor.getColumnIndex(KEY_CHECENV)));
                        model.setPlate_tracto(cursor.getString(cursor.getColumnIndex(KEY_CHECPLATRAC)));
                        model.setPlate_cistern(cursor.getString(cursor.getColumnIndex(KEY_CHECPLACIST)));
                        model.setCistern_image(cursor.getString(cursor.getColumnIndex(KEY_CHECCISIMA)));
                        model.setTracto_image(cursor.getString(cursor.getColumnIndex(KEY_CHECTRACIMA)));
                        model.setLastInspection(cursor.getString(cursor.getColumnIndex(KEY_CHECLASCHE)));
                        model.setYearFabrication(cursor.getString(cursor.getColumnIndex(KEY_CHECMANYEA)));
                        model.setProgress(cursor.getInt(cursor.getColumnIndex(KEY_CHECPROGRESS)));
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return model;
            }

            public void updateValueCheckListById(String param, String value, String idCheck) {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(param, value);
                db.update(TABLE_APP_CHECKLIST, values, KEY_CHECID + " = ?", new String[]{idCheck});
            }
        }

        /* @CLASS_LEV_OBS */
        public class LevObsSql {

            public LevObsSql() {
            }

            public void deleteLevObsTable() {
                SQLiteDatabase db = databasehelp.getReadableDatabase();
                db.delete(TABLE_APP_LEB_OBS, null, null);
            }

            public void deleteLevObsIdApi(int id) {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                db.delete(TABLE_APP_LEB_OBS, KEY_LEVOBSPKAPI + " = ?",
                        new String[]{String.valueOf(id)});
            }

            public void addLevObs(LevObsModel model) {

                SQLiteDatabase db = databasehelp.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(KEY_LEVOBSPKAPI, model.getPk());
                values.put(KEY_LEVOBSIDITEM, model.getId_item());
                values.put(KEY_LEVOBSCHELIS, model.getCheck_list());
                values.put(KEY_LEVOBSCHELISDATE, model.getCheck_list_date());
                values.put(KEY_LEVOBSCHELISITE, model.getCheck_list_item());
                values.put(KEY_LEVOBSTRACOM, model.getTransport_company());
                values.put(KEY_LEVOBSTRAUNI, model.getTransport_unit());
                values.put(KEY_LEVOBSSEC, model.getSection());
                values.put(KEY_LEVOBSSEV, model.getSeverity());
                values.put(KEY_LEVOBSOBSDES, model.getObservation_description());
                values.put(KEY_LEV_OBS_OBS_LIFTING, model.getObservation_lifting());
                values.put(KEY_LEVOBSDUEDAT, model.getDue_date());
                values.put(KEY_LEVOBSSTATE, model.getState());

                db.insert(TABLE_APP_LEB_OBS, null, values);

            }

            public ArrayList<LevObsModel> getAllLevOs() {
                ArrayList<LevObsModel> models = new ArrayList<>();

                String selectQuery = "SELECT * FROM " + TABLE_APP_LEB_OBS;
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);

                if (cursor.moveToFirst()) {
                    do {
                        LevObsModel model = new LevObsModel();
                        model.setIdSql(cursor.getInt(cursor.getColumnIndex(KEY_LEVOBSID)));
                        model.setPk(cursor.getInt(cursor.getColumnIndex(KEY_LEVOBSPKAPI)));
                        model.setId_item(cursor.getInt(cursor.getColumnIndex(KEY_LEVOBSIDITEM)));
                        model.setCheck_list(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSCHELIS)));
                        model.setCheck_list_date(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSCHELISDATE)));
                        model.setCheck_list_item(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSCHELISITE)));
                        model.setTransport_company(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSTRACOM)));
                        model.setTransport_unit(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSTRAUNI)));
                        model.setSection(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSSEC)));
                        model.setSeverity(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSSEV)));
                        model.setObservation_description(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSOBSDES)));
                        model.setObservation_lifting(cursor.getString(cursor.getColumnIndex(KEY_LEV_OBS_OBS_LIFTING)));
                        model.setDue_date(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSDUEDAT)));
                        model.setState(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSSTATE)));
                        models.add(model);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return models;
            }

            public LevObsModel getLevOs(String idSQL) {
                LevObsModel model = new LevObsModel();

                String selectQuery = "SELECT * FROM " + TABLE_APP_LEB_OBS + " WHERE " + KEY_LEVOBSID + " ='" + idSQL + "'";
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);

                if (cursor.moveToFirst()) {
                    do {
                        model.setIdSql(cursor.getInt(cursor.getColumnIndex(KEY_LEVOBSID)));
                        model.setPk(cursor.getInt(cursor.getColumnIndex(KEY_LEVOBSPKAPI)));
                        model.setId_item(cursor.getInt(cursor.getColumnIndex(KEY_LEVOBSIDITEM)));
                        model.setCheck_list(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSCHELIS)));
                        model.setCheck_list_date(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSCHELISDATE)));
                        model.setCheck_list_item(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSCHELISITE)));
                        model.setTransport_company(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSTRACOM)));
                        model.setTransport_unit(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSTRAUNI)));
                        model.setSection(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSSEC)));
                        model.setSeverity(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSSEV)));
                        model.setObservation_description(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSOBSDES)));
                        model.setObservation_lifting(cursor.getString(cursor.getColumnIndex(KEY_LEV_OBS_OBS_LIFTING)));
                        model.setDue_date(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSDUEDAT)));
                        model.setState(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSSTATE)));
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return model;
            }

            public void updateValueLevObs(String param, String value, String idSQL) {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(param, value);
                db.update(TABLE_APP_LEB_OBS, values, KEY_LEVOBSID + " = ?", new String[]{idSQL});
            }

            public ArrayList<LevObsModel> getAllLevOsByNumber(String number) {
                ArrayList<LevObsModel> models = new ArrayList<>();

                String selectQuery = "SELECT * FROM " + TABLE_APP_LEB_OBS + " WHERE " + KEY_LEVOBSCHELIS + " ='" + number + "'";
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);

                if (cursor.moveToFirst()) {
                    do {
                        LevObsModel model = new LevObsModel();
                        model.setIdSql(cursor.getInt(cursor.getColumnIndex(KEY_LEVOBSID)));
                        model.setPk(cursor.getInt(cursor.getColumnIndex(KEY_LEVOBSPKAPI)));
                        model.setId_item(cursor.getInt(cursor.getColumnIndex(KEY_LEVOBSIDITEM)));
                        model.setCheck_list(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSCHELIS)));
                        model.setCheck_list_date(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSCHELISDATE)));
                        model.setCheck_list_item(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSCHELISITE)));
                        model.setTransport_company(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSTRACOM)));
                        model.setTransport_unit(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSTRAUNI)));
                        model.setSection(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSSEC)));
                        model.setSeverity(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSSEV)));
                        model.setObservation_description(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSOBSDES)));
                        model.setObservation_lifting(cursor.getString(cursor.getColumnIndex(KEY_LEV_OBS_OBS_LIFTING)));
                        model.setDue_date(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSDUEDAT)));
                        model.setState(cursor.getString(cursor.getColumnIndex(KEY_LEVOBSSTATE)));
                        models.add(model);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return models;
            }

        }

        /* @CLASS_SUPERVISOR */
        public class CarrierSql {

            public CarrierSql() {
            }

            public void deleteCarrierSqlTable() {
                SQLiteDatabase db = databasehelp.getReadableDatabase();
                db.delete(TABLE_APP_CARRIER, null, null);
            }

            public void deleteCarrierIdApi(int id) {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                db.delete(TABLE_APP_CARRIER, KEY_CARRPK + " = ?",
                        new String[]{String.valueOf(id)});
            }

            public void addCarrierSql(String pk, String name, String company, String operation) {

                SQLiteDatabase db = databasehelp.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(KEY_CARRPK, pk);
                values.put(KEY_CARRNAM, name);
                values.put(KEY_CARRCOM, company);
                values.put(KEY_CARROPE, operation);
                db.insert(TABLE_APP_CARRIER, null, values);

            }

            public ArrayList<CarrierModel> getAllCarrierByPkCompanyAndPkOperation(String pkOperation) {
                ArrayList<CarrierModel> models = new ArrayList<>();

                String selectQuery = "SELECT * FROM " + TABLE_APP_CARRIER + " WHERE " + KEY_CARROPE + " ='" + pkOperation + "'";
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);

                if (cursor.moveToFirst()) {
                    do {
                        CarrierModel model = new CarrierModel();
                        model.setId_sql(cursor.getInt(cursor.getColumnIndex(KEY_CARRID)));
                        model.setPk(cursor.getInt(cursor.getColumnIndex(KEY_CARRPK)));
                        model.setParty(cursor.getString(cursor.getColumnIndex(KEY_CARRNAM)));
                        model.setPkCompany(cursor.getString(cursor.getColumnIndex(KEY_CARRCOM)));
                        model.setPkOperation(cursor.getString(cursor.getColumnIndex(KEY_CARROPE)));
                        models.add(model);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return models;
            }

        }

        /* @CLASS_IMAGE_SQL */
        public class AppImageSql {
            public AppImageSql() {
            }

            public void deleteImageTable() {
                SQLiteDatabase db = databasehelp.getReadableDatabase();
                db.delete(TABLE_APP_IMAGE, null, null);
            }

            public void addImages(List<ResponseImageItemChecklist> list, int type) {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                db.beginTransaction();
                try {
                    ContentValues values = new ContentValues();
                    for (ResponseImageItemChecklist item : list) {
                        ImageModel model = new ImageModel();
                        if(type == 1) {
                            model.setDocumentIdApi(item.getChecklist_section_item() + "");
                            model.setImage_url(item.getImage());
                            model.setComment(item.getComment());
                            model.setLabel(item.getLabel());
                            model.setState("server");
                        } else {
                            model.setDocumentIdApi(item.getNon_compliance() + "");
                            model.setImage_url(item.getImage());
                            model.setComment("");
                            model.setLabel(LABEL_OBSERVATION_ITEM);
                            model.setState("server");
                            model.setType(item.getState_lifting());
                        }

                        values.put(KEY_IMANAM, model.getName()); //ruta a la imagen local
                        values.put(KEY_IMAORDNUM, model.getDocumentIdApi());
                        values.put(KEY_IMASTA, model.getState());
                        values.put(KEY_IMATYP, model.getType());
                        values.put(KEY_IMAOWN, model.getOwner());
                        values.put(KEY_IMA_LATITUDE, model.getGps_latitude());
                        values.put(KEY_IMA_LONGITUDE, model.getGps_longitude());
                        values.put(KEY_IMA_DATETIME, model.getDatetime());
                        values.put(KEY_IMA_LABEL, model.getLabel());
                        values.put(KEY_IMA_URL, model.getImage_url());
                        values.put(KEY_IMA_COMMENT, model.getComment());
                        db.insert(TABLE_APP_IMAGE, null, values);
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }

            public void addImage(ImageModel model) {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(KEY_IMANAM, model.getName()); //ruta a la imagen local
                values.put(KEY_IMAORDNUM, model.getDocumentIdApi());
                values.put(KEY_IMASTA, model.getState());
                values.put(KEY_IMATYP, model.getType());
                values.put(KEY_IMAOWN, model.getOwner());
                values.put(KEY_IMA_LATITUDE, model.getGps_latitude());
                values.put(KEY_IMA_LONGITUDE, model.getGps_longitude());
                values.put(KEY_IMA_DATETIME, model.getDatetime());
                values.put(KEY_IMA_LABEL, model.getLabel());
                values.put(KEY_IMA_URL, model.getImage_url());
                values.put(KEY_IMA_COMMENT, model.getComment());
                db.insert(TABLE_APP_IMAGE, null, values);
            }

            public void deleteImageForLabelAndDocument(String orderNumber, String label) {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                db.delete(TABLE_APP_IMAGE, KEY_IMAORDNUM + " = ? AND " + KEY_IMA_LABEL + " = ?",
                        new String[]{String.valueOf(orderNumber), String.valueOf(label)});
            }

            public void deleteImageLabel(String label) {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                db.delete(TABLE_APP_IMAGE, KEY_IMA_LABEL + " = ?",
                        new String[]{String.valueOf(label)});
            }

            public void deleteImageSql(String id) {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                db.delete(TABLE_APP_IMAGE, KEY_IMAID + " = ?",
                        new String[]{String.valueOf(id)});
            }

            public ArrayList<ImageModel> getImagesByField(String value, String param) {
                ArrayList<ImageModel> models = new ArrayList<>();
                String selectQuery = "SELECT * FROM " + TABLE_APP_IMAGE + " WHERE " + param + "='" + value + "'";
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do {
                        ImageModel model = new ImageModel();
                        model.setId(cursor.getInt(cursor.getColumnIndex(KEY_IMAID)));
                        model.setName(cursor.getString(cursor.getColumnIndex(KEY_IMANAM)));
                        model.setDocumentIdApi(cursor.getString(cursor.getColumnIndex(KEY_IMAORDNUM)));
                        model.setState(cursor.getString(cursor.getColumnIndex(KEY_IMASTA)));
                        model.setType(cursor.getString(cursor.getColumnIndex(KEY_IMATYP)));
                        model.setOwner(cursor.getString(cursor.getColumnIndex(KEY_IMAOWN)));
                        model.setGps_latitude(cursor.getString(cursor.getColumnIndex(KEY_IMA_LATITUDE)));
                        model.setGps_longitude(cursor.getString(cursor.getColumnIndex(KEY_IMA_LONGITUDE)));
                        model.setDatetime(cursor.getString(cursor.getColumnIndex(KEY_IMA_DATETIME)));
                        model.setLabel(cursor.getString(cursor.getColumnIndex(KEY_IMA_LABEL)));
                        model.setImage_url(cursor.getString(cursor.getColumnIndex(KEY_IMA_URL)));
                        model.setComment(cursor.getString(cursor.getColumnIndex(KEY_IMA_COMMENT)));
                        models.add(model);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return models;
            }

            public void updateValue(String param, String value, String idSql) {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(param, value);
                db.update(TABLE_APP_IMAGE, values, KEY_IMAID + " = ?", new String[]{idSql});
            }

        }
    }

}
