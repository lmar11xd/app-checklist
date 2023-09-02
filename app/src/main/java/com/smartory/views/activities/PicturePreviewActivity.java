package com.smartory.views.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import com.otaliastudios.cameraview.CameraUtils;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.size.AspectRatio;
import com.smartory.R;
import com.smartory.db.SqliteClass;
import com.smartory.resource.HorizontalValues;
import com.smartory.utils.Util;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PicturePreviewActivity extends AppCompatActivity {
    private static PictureResult picture;
    String imageFileName = "";
    String mCurrentPhotoPath = "";
    //ImageModel imageModel;

    public static void setPictureResult(@Nullable PictureResult pictureResult) {
        picture = pictureResult;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_preview);

        getSupportActionBar().setTitle("Vista Previa");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final PictureResult result = picture;
        if (result == null) {
            finish();
            return;
        }

        final ImageView imageView = findViewById(R.id.image);

        final long delay = getIntent().getLongExtra("delay", 0);
        AspectRatio ratio = AspectRatio.of(result.getSize());
        try {
            result.toBitmap(1000, 1000, bitmap -> imageView.setImageBitmap(bitmap));
        } catch (UnsupportedOperationException e) {
            imageView.setImageDrawable(new ColorDrawable(Color.GREEN));
            Toast.makeText(this, "Can't preview this format: " + picture.getFormat(),
                    Toast.LENGTH_LONG).show();
        }

        if (result.isSnapshot()) {
            // Log the real size for debugging reason.
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(result.getData(), 0, result.getData().length, options);
            if (result.getRotation() % 180 != 0) {
                Log.e("PicturePreview", "The picture full size is " + result.getSize().getHeight() + "x" + result.getSize().getWidth());
            } else {
                Log.e("PicturePreview", "The picture full size is " + result.getSize().getWidth() + "x" + result.getSize().getHeight());
            }
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isChangingConfigurations()) {
            setPictureResult(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
        }
        else if (item.getItemId() == R.id.action_save) {
            Toast.makeText(this, "Guardando imagen ...", Toast.LENGTH_SHORT).show();
            String extension;
            switch (picture.getFormat()) {
                case JPEG: extension = "jpg"; break;
                case DNG: extension = "dng"; break;
                default: throw new RuntimeException("Unknown format.");
            }

            //File file = new File(getFilesDir(), "picture." + extension);
            File file = null;
            try { file = createImageFile();
            } catch (IOException e) { e.printStackTrace(); }

            CameraUtils.writeToFile(picture.getData(), file, file1 -> {
                if (file1 != null) {

                    Context context = PicturePreviewActivity.this;
                    //ConstValue.setActualPhotoPath(mCurrentPhotoPath);
                    SharedPreferences sharedPref = context.getSharedPreferences("check_list",Context.MODE_PRIVATE);
                    final SharedPreferences.Editor editor = sharedPref.edit();

                    String photo_type="";
                    if(sharedPref.getString("photo_type","c").equals("nc")){
                        photo_type = SqliteClass.KEY_ITEMNONCOMIMA;
                    }else if(sharedPref.getString("photo_type","c").equals("c")){
                        photo_type = SqliteClass.KEY_ITEMCOMIMA;
                    }else if(sharedPref.getString("photo_type","c").equals("")){
                        photo_type = SqliteClass.KEY_ITEMOBSLIFIMA;
                    }

                    Bundle extras = getIntent().getExtras();
                    String type_car = "";
                    if(extras != null){
                        type_car = extras.getString("type_car");
                    }

                    Log.i("ID-ITEM", sharedPref.getString("idItem",""));
                    Log.i("PARAM-ITEM", photo_type);
                    String photoBase64 = Util.getFileToByte(mCurrentPhotoPath);

                    final SharedPreferences sharedPref1 = context.getSharedPreferences("lev_obs",Context.MODE_PRIVATE);
                    final SharedPreferences.Editor editor1 = sharedPref1.edit();
                    if(!type_car.equals("")){
                        if(type_car.equals("tracto")){
                            editor.putString("tracto_image",photoBase64);
                            editor.apply();

                        }else if(type_car.equals("cistern")){
                            editor.putString("cistern_image",photoBase64);
                            editor.apply();
                        }
                    }else{
                        editor1.putString("imagen64",photoBase64);
                        editor1.apply();
                    }
                    String idItem = sharedPref.getString("idItem","");
                    SqliteClass.getInstance(context).databasehelp.itemSql.updateValueItemById(
                            photo_type,
                            photoBase64,
                            idItem);
                    if(!type_car.equals("")){
                        if(type_car.equals("tracto")){
                            HorizontalValues.formNewCheckListActivity.reload_btn_tracto_Activity();
                        }else if(type_car.equals("cistern")){
                            HorizontalValues.formNewCheckListActivity.reload_btn_cister_Activity();
                        }
                    }
                    finish();
                } else {
                    Toast.makeText(PicturePreviewActivity.this, "Error while writing file.", Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir );

        mCurrentPhotoPath = image.getAbsolutePath();
        Log.i("PATH", mCurrentPhotoPath);
        return image;
    }

}
