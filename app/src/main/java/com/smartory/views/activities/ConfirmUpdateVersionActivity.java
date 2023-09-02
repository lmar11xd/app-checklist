package com.smartory.views.activities;

import static com.smartory.resource.HorizontalValues.URL_TO_DOWNLOAD;
import static com.smartory.resource.HorizontalValues.ha;
import static com.smartory.resource.HorizontalValues.isRunning;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.smartory.BuildConfig;
import com.smartory.R;
import com.smartory.db.SQLTables;

public class ConfirmUpdateVersionActivity extends AppCompatActivity {

    Button okUpdate;
    Context context;

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_confirm_update_version);
        context = this;
        okUpdate = findViewById(R.id.okUpdate);
        okUpdate.setOnClickListener(v -> {
            isRunning = false;
            if (ha != null) {
                ha.removeCallbacksAndMessages(null);
                ha = null;
            }
            SharedPreferences sharedPref = context.getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
            SQLTables DataTables = new SQLTables();
            DataTables.Delete(context);
            sharedPref.edit().clear().apply();
            Intent intent = new Intent(ConfirmUpdateVersionActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("URL_TO_DOWNLOAD", URL_TO_DOWNLOAD);
            startActivity(intent);
            finish();
            cancelNotification(context,13);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_checklist, menu);
        menu.findItem(R.id.action_checklist).setVisible(false);
        menu.findItem(R.id.action_version).setTitle(BuildConfig.VERSION_NAME);
        return true;
    }

    public static void cancelNotification(Context ctx, int notifyId) {
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(notifyId);
    }
}