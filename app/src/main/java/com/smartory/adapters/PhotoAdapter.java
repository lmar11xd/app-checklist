package com.smartory.adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.smartory.R;
import com.smartory.db.SqliteClass;
import com.smartory.models.PhotoEvidence;
import com.smartory.utils.ConnectionDetector;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ProjectViewHolder>{
    private List<PhotoEvidence> items;
    private Context context;
    private ConnectionDetector cf;
    private boolean onlyView = false;
    public static class ProjectViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageEvidence;
        public ImageView deleteImage, downloadImage, zoomImage;
        public TextView typePhoto;
        public LinearLayout boxImage;
        public LinearLayout option_delete, option_download, option_zoom;
        public ProjectViewHolder(View v){
            super(v);
            boxImage = v.findViewById(R.id.boxImage);
            imageEvidence = v.findViewById(R.id.imageEvidence);
            deleteImage = v.findViewById(R.id.deleteImage);
            zoomImage = v.findViewById(R.id.zoomImage);
            downloadImage = v.findViewById(R.id.downloadImage);
            typePhoto = v.findViewById(R.id.typePhoto);
            option_delete = v.findViewById(R.id.option_delete);
            option_download = v.findViewById(R.id.option_download);
            option_zoom = v.findViewById(R.id.option_zoom);
        }
    }
    public PhotoAdapter(List<PhotoEvidence> items, Context context){
        this.context = context;
        this.items = items;
        cf = new ConnectionDetector(context);
    }
    public PhotoAdapter(List<PhotoEvidence> items, Context context, boolean options){
        this.onlyView = options;
        this.context = context;
        this.items = items;
        cf = new ConnectionDetector(context);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addElement(PhotoEvidence project){
        items.add(project);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void deleteElement(int position){
        if (items.get(position).getState().equals("local")
                &&items.get(position).getIdSql()!=null
                &&!items.get(position).getIdSql().equals("")
        ){
            SqliteClass.getInstance(context).databasehelp.imageSql.deleteImageSql(items.get(position).getIdSql());
        }
        items.remove(position);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount(){
        return items.size();
    }
    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.grid_layout_item_photo_evidence,viewGroup,false);
        return new ProjectViewHolder(v);
    }
    @Override
    public void onBindViewHolder(final ProjectViewHolder projectViewHolder, @SuppressLint("RecyclerView") final int i){
        try {
            projectViewHolder.typePhoto.setText(items.get(i).getLabel());
            if(items.get(i).getState().equals("server")) {
                projectViewHolder.imageEvidence.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.preview));
                projectViewHolder.option_delete.setVisibility(View.GONE);
                projectViewHolder.option_zoom.setVisibility(View.GONE);
            }else if (items.get(i).getState().equals("serverAndLocal")){
                projectViewHolder.option_delete.setVisibility(View.GONE);
                projectViewHolder.option_download.setVisibility(View.GONE);
                projectViewHolder.imageEvidence.setImageBitmap(items.get(i).getImage());
            } else {
                projectViewHolder.option_download.setVisibility(View.GONE);
                projectViewHolder.imageEvidence.setImageBitmap(items.get(i).getImage());
            }
            if (onlyView){
                projectViewHolder.option_delete.setVisibility(View.GONE);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        projectViewHolder.deleteImage.setOnClickListener(v -> showAlertDialogDelete(i));
        projectViewHolder.downloadImage.setOnClickListener(v-> {
            if (cf.isConnectingToInternet()){
                new DownloadImageTask(projectViewHolder.imageEvidence,items.get(i),projectViewHolder.option_zoom).execute(items.get(i).getImage_url());
            } else {
                Toast.makeText(context,"NECESITA UNA CONEXION A INTERNET", Toast.LENGTH_SHORT).show();
            }
        });
        projectViewHolder.zoomImage.setOnClickListener(v -> {
            final android.app.AlertDialog alertDialog;
            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_photo_preview, null);
            ImageView imageView =  view.findViewById(R.id.imageView);

            if (items.get(i).getState().equals("serverAndLocal")){
                imageView.setImageBitmap(items.get(i).getImage());
            } else {
                imageView.setImageBitmap(items.get(i).getImage());
            }


            Button btn_cancel = view.findViewById(R.id.btn_cancel);
            builder.setView(view);
            alertDialog = builder.create();
            btn_cancel.setOnClickListener(v1 -> alertDialog.dismiss());
            alertDialog.show();
        });
    }

    private void showAlertDialogDelete(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Eliminar Foto");
        builder.setMessage("Esta seguro que desea eliminar esta foto ?" );
        builder.setCancelable(false);
        builder.setPositiveButton("Eliminar", (dialog, which) -> deleteElement(position));
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog= builder.create();
        dialog.show();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        private ProgressDialog mDialog;
        private ImageView bmImage;
        private PhotoEvidence photoEvidence;
        private LinearLayout zoomOption;

        public DownloadImageTask(ImageView bmImage, PhotoEvidence photoEvidence, LinearLayout zoomOption) {
            this.bmImage = bmImage;
            this.photoEvidence = photoEvidence;
            this.zoomOption = zoomOption;
        }

        protected void onPreExecute() {
            mDialog = ProgressDialog.show(context,"Espere un momento...", "Descargando vista previa de imagen ...", true);
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            photoEvidence.setImage(result);
            photoEvidence.setState("serverAndLocal");
            bmImage.setImageBitmap(result);
            mDialog.dismiss();
            zoomOption.setVisibility(View.VISIBLE);
        }
    }
}
