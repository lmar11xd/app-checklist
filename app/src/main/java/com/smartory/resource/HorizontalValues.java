package com.smartory.resource;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.widget.ImageView;

import com.smartory.BuildConfig;
import com.smartory.adapters.CabLevObsAdapter;
import com.smartory.adapters.ItemCheckListAdapter;
import com.smartory.adapters.ItemLevAdapter;
import com.smartory.views.activities.FormNewCheckListActivity;
import com.smartory.views.fragments.VerificationTireFragment;

public class HorizontalValues {

    public static  boolean IS_PRODUCTION = true;
    public static String BASE_URL = "https://unpaperwork.petroperu.net.pe/api/";
    //public static String BASE_URL = "https://unpaperwork.dev.smartory.pe/api/";
    //public static String BASE_URL = "https://unpaperwork.qa.smartory.pe/api/";
    public static final String BASE_URL_SANDBOX = "https://unpaperwork.sandbox.smartory.pe/api/";
    public static final String BASE_URL_DEV = "https://unpaperwork.dev.smartory.pe/api/";
    public static final String BASE_URL_QA = "https://unpaperwork.qa.smartory.pe/api/";
    public static final String BASE_URL_PROD = "https://unpaperwork.petroperu.net.pe/api/";

    @SuppressLint("StaticFieldLeak")
    public static FormNewCheckListActivity formNewCheckListActivity = null;
    @SuppressLint("StaticFieldLeak")
    public static ItemCheckListAdapter itemCheckListAdapter = null;
    @SuppressLint("StaticFieldLeak")
    public static ItemLevAdapter itemLevAdapter = null;
    @SuppressLint("StaticFieldLeak")
    public static VerificationTireFragment verifyTireFragment = null;
    @SuppressLint("StaticFieldLeak")
    public static ImageView imageView_preview_on_dialog = null;
    @SuppressLint("StaticFieldLeak")
    public static CabLevObsAdapter.ViewHolder cabLevObsAdapter =  null;
    public static String photoCompliance = null;
    public static String LABEL_CHECK_IMAGE_ITEM ="IMAGEN DE EVIDENCIA - CHECK";
    public static String LABEL_OBSERVATION_ITEM ="IMAGEN DE LEVANTAMIENTO";
    public static String URL_TO_DOWNLOAD = "";
    public static Boolean isRunning = false;
    public static Handler ha;
}
