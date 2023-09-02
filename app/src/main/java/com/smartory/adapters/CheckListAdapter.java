package com.smartory.adapters;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.smartory.R;
import com.smartory.config.ConstValue;
import com.smartory.db.SqliteClass;
import com.smartory.models.CheckListModel;
import com.smartory.models.ItemCheckListModel;
import com.smartory.network.InterfaceAPI;
import com.smartory.network.RetrofitClientInstance;
import com.smartory.utils.ConnectionDetector;
import com.smartory.utils.Util;
import com.smartory.views.activities.CheckListFormActivity;
import com.smartory.views.activities.CheckListValidationActivity;
import com.smartory.views.activities.ListChecksActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.ViewHolder> {
    Context context;
    List<CheckListModel> items;
    ConnectionDetector cd;

    public CheckListAdapter(Context context, ArrayList<CheckListModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.row_list, parent, false);
        CheckListAdapter.ViewHolder viewHolder = new CheckListAdapter.ViewHolder(contactView);
        cd = new ConnectionDetector(context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CheckListAdapter.ViewHolder holder, int position) {
        holder.bind(items.get(position), position);
    }

    @Override
    public int getItemCount() {
        if (items != null)
            return items.size();
        else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_inspector, tv_supervisor, tv_conductor, tv_numero,
                tv_date, tv_operation, tv_company, tv_type, tv_plate_tracto,
                tv_plate_cistern, tv_state, tv_route, tv_last_checklist, tv_manufactoring_year, tv_progress_percent, tv_unidad_transporte;
        CardView crd_check_list;
        SharedPreferences sharedPref = context.getSharedPreferences("check_list", Context.MODE_PRIVATE);
        SharedPreferences sharedPrefLogin = context.getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Boolean is_staff, is_superuser;
        Boolean is_group_supervisor, is_group_inspector, is_group_sub_employee, is_group_staff, is_group_sub_staff, is_group_driver;
        Boolean can_create_checklist, can_validate_checklist, can_edit_checklist, can_edit_validated_checklist, can_extend_noncompliance_checklist;
        String employee_name, type, company_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_numero = itemView.findViewById(R.id.text_number_checkList);
            tv_inspector = itemView.findViewById(R.id.tv_inspector);
            tv_supervisor = itemView.findViewById(R.id.tv_supervisor);
            tv_conductor = itemView.findViewById(R.id.tv_driver);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_operation = itemView.findViewById(R.id.tv_operation);
            tv_company = itemView.findViewById(R.id.tv_company);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_state = itemView.findViewById(R.id.tv_state);
            tv_route = itemView.findViewById(R.id.tv_route);
            //tv_plate_tracto = itemView.findViewById(R.id.tv_plate_tracto);
            //tv_plate_cistern = itemView.findViewById(R.id.tv_plate_cistern);
            tv_unidad_transporte = itemView.findViewById(R.id.tv_unidad_transporte);
            crd_check_list = itemView.findViewById(R.id.ideaCard_check_list);
            tv_last_checklist = itemView.findViewById(R.id.tv_last_checklist);
            tv_manufactoring_year = itemView.findViewById(R.id.tv_manufactoring_year);
            tv_progress_percent = itemView.findViewById(R.id.tv_progress_percent);
        }

        @SuppressLint("SetTextI18n")
        public void bind(final CheckListModel checkListModel, int position) {
            if (checkListModel.getNumber() == null) {
                tv_numero.setText("Sin enviar");
            } else {
                tv_numero.setText(String.valueOf(checkListModel.getNumber()));
            }
            AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

            editor.putString("id_check_list", String.valueOf(checkListModel.getId()));
            is_staff = sharedPrefLogin.getBoolean("is_staff", false);
            is_superuser = sharedPrefLogin.getBoolean("is_superuser", false);
            company_name = sharedPrefLogin.getString("company_name", "");
            is_group_supervisor = sharedPrefLogin.getBoolean("is_group_supervisor", false);
            is_group_inspector = sharedPrefLogin.getBoolean("is_group_inspector", false);
            is_group_sub_employee = sharedPrefLogin.getBoolean("is_group_sub_employee", false);
            is_group_staff = sharedPrefLogin.getBoolean("is_group_staff", false);
            is_group_sub_staff = sharedPrefLogin.getBoolean("is_group_sub_staff", false);
            is_group_driver = sharedPrefLogin.getBoolean("is_group_driver", false);


            can_create_checklist = sharedPrefLogin.getBoolean("can_create_checklist", false);
            can_validate_checklist = sharedPrefLogin.getBoolean("can_validate_checklist", false);
            can_edit_checklist = sharedPrefLogin.getBoolean("can_edit_checklist", false);
            can_edit_validated_checklist = sharedPrefLogin.getBoolean("can_edit_validated_checklist", false);
            can_extend_noncompliance_checklist = sharedPrefLogin.getBoolean("can_extend_noncompliance_checklist", false);

            employee_name = sharedPrefLogin.getString("employee_name", "");
            tv_inspector.setText(String.valueOf(checkListModel.getName_inspector()));
            tv_supervisor.setText(String.valueOf(checkListModel.getName_supervisor()));
            tv_conductor.setText(String.valueOf(checkListModel.getName_driver()));
            tv_date.setText(String.valueOf(checkListModel.getDatetime()));
            tv_operation.setText(String.valueOf(checkListModel.getName_operation()));
            tv_company.setText(String.valueOf(checkListModel.getName_company_shipment()));
            tv_type.setText(String.valueOf(checkListModel.getType_check()));
            if (checkListModel.getState().equals("posted") && !checkListModel.getType().equals("local")) {
                tv_state.setText("Registrado");
            } else if (checkListModel.getState().equals("posted") && checkListModel.getType().equals("local")) {
                tv_state.setText("Sin Registrar");
            } else if (checkListModel.getState().equals("Validado con observaciones")) {
                tv_state.setText("Validado con Obs.");
            } else if (checkListModel.getState().equals("review")) {
                tv_state.setText("Validado");
            } else {
                tv_state.setText(String.valueOf(checkListModel.getState()));
            }
            tv_route.setText(String.valueOf(checkListModel.getName_route()));
            if (checkListModel.getName_route() == null) {
                tv_route.setText("-");
            } else {
                tv_route.setText(String.valueOf(checkListModel.getName_route()));
            }

            String last_inspection = "" + checkListModel.getLastInspection();
            last_inspection = Util.toNumeroChecklistFecha(last_inspection);
            //tv_plate_tracto.setText(String.valueOf(checkListModel.getPlate_tracto()));
            //tv_plate_cistern.setText(String.valueOf(checkListModel.getPlate_cistern()));
            tv_unidad_transporte.setText(checkListModel.getPlate_tracto() + "/" + checkListModel.getPlate_cistern());
            tv_last_checklist.setText(last_inspection);
            tv_manufactoring_year.setText(checkListModel.getYearFabrication());
            tv_progress_percent.setText(checkListModel.getProgress() + "%");
            crd_check_list.setOnClickListener(v -> {
                v.startAnimation(buttonClick);
                Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
                final InterfaceAPI api = retrofit.create(InterfaceAPI.class);
                final AlertDialog alertDialog;
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.dialog_options_checklist, null);
                builder.setView(view);
                alertDialog = builder.create();
                ConstraintLayout btn_donwload = view.findViewById(R.id.event_donwload_pdf);
                ConstraintLayout btn_edit = view.findViewById(R.id.event_edit);
                ConstraintLayout btn_view = view.findViewById(R.id.event_view);
                ConstraintLayout btn_delete = view.findViewById(R.id.event_delete);
                ConstraintLayout btn_validate = view.findViewById(R.id.event_lev_obs);
                ConstraintLayout btn_publicar = view.findViewById(R.id.event_publicar);

                btn_edit.setVisibility(View.GONE);
                btn_view.setVisibility(View.VISIBLE);
                btn_validate.setVisibility(View.GONE);
                btn_donwload.setVisibility(View.VISIBLE);
                btn_delete.setVisibility(View.GONE);
                btn_publicar.setVisibility(View.GONE);

                editor.putString("id_check_list_sql", String.valueOf(checkListModel.getIdSql()));
                editor.putString("id_check_list_api", String.valueOf(checkListModel.getId()));
                editor.putString("number_check_list", String.valueOf(checkListModel.getNumber()));
                editor.putString("type", String.valueOf(checkListModel.getType()));
                editor.apply();

                if (can_create_checklist && checkListModel.getNumber().equals("Sin Enviar")) {
                    btn_publicar.setVisibility(View.VISIBLE);
                }
                if (can_validate_checklist) {
                    btn_validate.setVisibility(View.VISIBLE);
                }
                if (checkListModel.getState().equals("Validado") || checkListModel.getState().equals("review") ||
                        checkListModel.getState().equals("Validado con observaciones") || checkListModel.getState().equals("review_w_obs")) {
                    btn_validate.setVisibility(View.GONE);
                    if (can_edit_validated_checklist) {
                        btn_edit.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (can_edit_checklist) {
                        btn_edit.setVisibility(View.VISIBLE);
                    }
                }
                btn_donwload.setOnClickListener(v1 -> {
                    Toast.makeText(context, "Iniciando Descarga ...", Toast.LENGTH_SHORT).show();
                    v1.startAnimation(buttonClick);
                    Call<ResponseBody> call = api.pdfCheckList(sharedPrefLogin.getString("token", ""), String.valueOf(checkListModel.getId()));
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                File arch = Util.writeResponseBodyToDisk(response.body(), checkListModel.getNumber());
                                if (arch != null) {
                                    Toast.makeText(context, "CheckList descargado", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", arch), "application/pdf");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    try {
                                        alertDialog.dismiss();
                                        context.startActivity(intent);
                                    } catch (ActivityNotFoundException e) {
                                        Toast.makeText(context, "No tiene una aplicación para abrir PDFs, busque en sus archivos", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(context, "Error al guardar", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(context, "Error al descargar, intente sincronizar sus datos", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                            Toast.makeText(context, "Error al descargar, revise su conexión a internet", Toast.LENGTH_LONG).show();
                        }
                    });
                });
                btn_validate.setOnClickListener(v12 -> {
                    v12.startAnimation(buttonClick);
                    editor.putString("id_check_list", String.valueOf(checkListModel.getId()));
                    editor.putString("opcion", ConstValue.VALIDATE);
                    editor.putString("estado", "validate");
                    editor.apply();
                    alertDialog.dismiss();
                    Intent intent = new Intent(context, CheckListValidationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(intent);
                });
                btn_publicar.setOnClickListener(v13 -> {
                    v13.startAnimation(buttonClick);
                    editor.putString("id_check_list", String.valueOf(checkListModel.getId()));
                    editor.putString("opcion", "");
                    editor.putString("estado", "");
                    editor.apply();
                    alertDialog.dismiss();
                    Intent intent = new Intent(context, CheckListValidationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(intent);
                });

                btn_edit.setOnClickListener(v13 -> {
                    v13.startAnimation(buttonClick);
                    editor.putString("estado", "update");
                    editor.putString("opcion", ConstValue.EDIT);
                    editor.apply();
                    alertDialog.dismiss();
                    Intent intent = new Intent(context, CheckListFormActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(intent);
                });

                btn_view.setOnClickListener(v14 -> {
                    v14.startAnimation(buttonClick);
                    editor.putString("opcion", ConstValue.VIEW);
                    editor.putString("estado", "view");
                    editor.apply();
                    alertDialog.dismiss();
                    Intent intent = new Intent(context, CheckListFormActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(intent);
                });
                btn_delete.setOnClickListener(view1 -> {
                    alertDialog.dismiss();
                    //if(checkListModel.getType().equals("local")) {
                    new MaterialAlertDialogBuilder(context)
                            .setTitle("Check List")
                            .setMessage("Esta apunto de eliminar un CheckList ¿Esta seguro?")
                            .setNegativeButton("Cancelar", null)
                            .setPositiveButton("Ok", (dialog, which) -> {
                                Toast.makeText(context, "Eliminando ..", Toast.LENGTH_SHORT).show();
                                ArrayList<ItemCheckListModel> items;
                                items = SqliteClass.getInstance(context).databasehelp.itemSql.getAllItemsByCheck(String.valueOf(checkListModel.getIdSql()));
                                for (int i = 0; i < items.size(); i++) {
                                    SqliteClass.getInstance(context).databasehelp.itemSql.deleteItem(items.get(i).getIdSql());
                                }
                                SqliteClass.getInstance(context).databasehelp.checkListSql.deleteCheckList(checkListModel.getIdSql());
                                //items.remove(position);
                                //notifyDataSetChanged();
                                Intent intent = new Intent(context, ListChecksActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                context.startActivity(intent);

                            }).show();
                    //}
                });
                alertDialog.show();
            });

            // ELIMINAR CHECKLIST
            /*crd_check_list.setOnLongClickListener(v -> {
                //if(checkListModel.getType().equals("local")) {
                    new MaterialAlertDialogBuilder(context)
                            .setTitle("Check List")
                            .setMessage("Esta apunto de eliminar un CheckList ¿Esta seguro de realizar esta acción?")
                            .setNegativeButton("Cancelar", null)
                            .setPositiveButton("Ok", (dialog, which) -> {
                                ArrayList<ItemCheckListModel> items = new ArrayList<ItemCheckListModel>();
                                items = SqliteClass.getInstance(context).databasehelp.itemSql.getAllItemsByCheck(String.valueOf(checkListModel.getIdSql()));
                                for (int i = 0; i < items.size(); i++) {
                                    SqliteClass.getInstance(context).databasehelp.itemSql.deleteItem(items.get(i).getIdSql());
                                }
                                SqliteClass.getInstance(context).databasehelp.checkListSql.deleteCheckList(checkListModel.getIdSql());

                                Intent intent = new Intent(context, ListChecksActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                context.startActivity(intent);
                            }).show();
                //}
                return false;
            });*/
        }
    }
}
