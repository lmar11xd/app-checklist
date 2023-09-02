package com.smartory.views.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartory.BuildConfig;
import com.smartory.R;
import com.smartory.adapters.CheckListAdapter;
import com.smartory.db.SqliteClass;
import com.smartory.models.CheckListModel;
import com.smartory.utils.ComparadorChecks;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ListChecksActivity extends AppCompatActivity {
    ArrayList<CheckListModel> checkListModels,checkListModelsFilter,getShipments;
    RecyclerView recyclerView;
    TextView tv_empty;
    SearchView editsearch;
    Context context;
    String param="";
    Spinner spinner_options,spinner_option_select;
    LinearLayout lnt_filter, lnt_detail_filter;
    ArrayList<String> options_result , options;
    CardView crd_ok,crd_cancel;
    CheckListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_checks);
        context = this;
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_lis_check);
        tv_empty = findViewById(R.id.tv_empty);

        checkListModels = SqliteClass.getInstance(context).databasehelp.checkListSql.getAllChecks();
        checkListModelsFilter = SqliteClass.getInstance(context).databasehelp.checkListSql.getAllChecks();
        if(checkListModels.size()>0){
            getList(checkListModels);
        } else {
            tv_empty.setVisibility(View.VISIBLE);
        }

        editsearch = findViewById(R.id.searchView);
        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText == null || newText.trim().isEmpty()) {
                    resetSearch();
                    return false;
                }
                ArrayList<CheckListModel> filteredValues = new ArrayList<>(checkListModels);
                for (CheckListModel value : checkListModels) {
                    if (!value.getNumber().toLowerCase().contains(newText.toLowerCase()))
                        filteredValues.remove(value);
                }
                getList(filteredValues);
                return false;
            }
        });
        lnt_filter = findViewById(R.id.lnt_filter_shipment);
        lnt_filter.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_filter);
            spinner_options= dialog.findViewById(R.id.spnr_filter_general);
            lnt_detail_filter = dialog.findViewById(R.id.lnt_detail_filter);
            crd_ok = dialog.findViewById(R.id.ideaCard_ok);
            crd_cancel = dialog.findViewById(R.id.ideaCard_cancel);
            options = new ArrayList<>();
            options_result= new ArrayList<>();
            options.add("Seleccione");      //0
            options.add("Mostrar Todo");            //1
            options.add("Empresa de Transporte");
            options.add("Operación");
            options.add("Inspector");
            options.add("Supervisor");
            options.add("Conductor");
            ArrayAdapter<String> adapterFilter= new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, options);
            spinner_options.setAdapter(adapterFilter);
            spinner_option_select= dialog.findViewById(R.id.spnr_filter_detail);
            spinner_options.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    lnt_detail_filter.setVisibility(View.GONE);
                    switch (position){
                        case 0:
                            options_result.clear();
                            lnt_detail_filter.setVisibility(View.GONE);
                            break;
                        case 1:
                            options_result.clear();
                            getList(checkListModels);
                            dialog.dismiss();
                            break;
                        case 2:
                            options_result.clear();
                            param=SqliteClass.KEY_CHECNAMCOMSHI;
                            for (int i = 0; i<checkListModels.size();i++){
                                if(!options_result.contains(checkListModelsFilter.get(i).getName_company_shipment()))
                                    options_result.add(checkListModelsFilter.get(i).getName_company_shipment());
                            }
                            lnt_detail_filter.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            options_result.clear();
                            param=SqliteClass.KEY_CHECNAMOPE;
                            for (int i = 0; i<checkListModels.size();i++){
                                if(!options_result.contains(checkListModelsFilter.get(i).getName_operation()))
                                    options_result.add(checkListModelsFilter.get(i).getName_operation());
                            }
                            lnt_detail_filter.setVisibility(View.VISIBLE);
                            break;

                        case 4:
                            options_result.clear();
                            param=SqliteClass.KEY_CHECNAMINS;
                            for (int i = 0; i<checkListModels.size();i++){
                                if(!options_result.contains(checkListModelsFilter.get(i).getName_inspector()))
                                    options_result.add(checkListModelsFilter.get(i).getName_inspector());
                            }
                            lnt_detail_filter.setVisibility(View.VISIBLE);
                            break;
                        case 5:
                            options_result.clear();
                            param=SqliteClass.KEY_CHECNAMSUP;
                            for (int i = 0; i<checkListModels.size();i++){
                                if(!options_result.contains(checkListModelsFilter.get(i).getName_supervisor()))
                                    options_result.add(checkListModelsFilter.get(i).getName_supervisor());
                            }
                            lnt_detail_filter.setVisibility(View.VISIBLE);
                            break;
                        case 6:
                            options_result.clear();
                            param=SqliteClass.KEY_CHECNAMDRI;
                            for (int i = 0; i<checkListModels.size();i++){
                                if(!options_result.contains(checkListModelsFilter.get(i).getName_driver()))
                                    options_result.add(checkListModelsFilter.get(i).getName_driver());
                            }
                            lnt_detail_filter.setVisibility(View.VISIBLE);
                            break;
                        default:
                            break;
                    }
                    ArrayAdapter<String> adapterFilter_result= new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, options_result);
                    spinner_option_select.setAdapter(adapterFilter_result);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            spinner_option_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    getShipments = SqliteClass.getInstance(context).databasehelp.checkListSql.getAllChecksByParam(param,options_result.get(position));
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            dialog.show();
            crd_ok.setOnClickListener(v12 -> {
                if(param.equals("") || param.equals("Seleccione")){
                    Toast.makeText(context,"Debe seleccionar un filtro", Toast.LENGTH_LONG).show();
                }else {
                    getList(getShipments);
                }
                dialog.dismiss();
            });
            crd_cancel.setOnClickListener(v1 -> dialog.dismiss());
        });
    }
    public void resetSearch() {
        checkListModels = SqliteClass.getInstance(context).databasehelp.checkListSql.getAllChecks();
        getList(checkListModels);
    }

    public void getList(ArrayList<CheckListModel> list){
        adapter = new CheckListAdapter(context, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public void onResume() {
        super.onResume();
        checkListModels = SqliteClass.getInstance(context).databasehelp.checkListSql.getAllChecks();
        Collections.sort(checkListModels, new ComparadorChecks());
        getList(checkListModels);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ListChecksActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
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
        if(id == android.R.id.home){
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
