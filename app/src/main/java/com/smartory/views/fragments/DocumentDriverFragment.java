package com.smartory.views.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.smartory.R;
import com.smartory.adapters.ItemCheckListAdapter;
import com.smartory.db.SqliteClass;
import com.smartory.interfaces.IChecklistProgress;
import com.smartory.models.ItemCheckListModel;
import com.smartory.utils.ComparadorItemsChecklist;

import java.util.ArrayList;
import java.util.Collections;

public class DocumentDriverFragment extends Fragment {
    public final String FRAGMENT_SECTION_ID = "1";
    RecyclerView recyclerView;
    TextView tv_state;
    ArrayList<ItemCheckListModel> itemsDriver;
    Context context;
    ItemCheckListAdapter adapter;
    IChecklistProgress callbackProgress;

    public DocumentDriverFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_document_driver, container, false);
        context=getActivity();
        recyclerView = view.findViewById(R.id.recycler_view_driver);
        @SuppressLint("InflateParams") View contactView = inflater.inflate(R.layout.row_item_checklist, null);
        tv_state = contactView.findViewById(R.id.tv_state_item);

        SharedPreferences sharedPref = context.getSharedPreferences("check_list", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("fragment", FRAGMENT_SECTION_ID);
        editor.apply();
        String id_check = sharedPref.getString("id_check_list_sql","");
        itemsDriver = SqliteClass.getInstance(getActivity()).databasehelp.itemSql.getItemsBySection("1", id_check);
        getList(itemsDriver);
        return view;
    }

    public void setCheckProgressListener(IChecklistProgress callbackProgress) {
        this.callbackProgress = callbackProgress;
    }

    public void getList(ArrayList<ItemCheckListModel> list){
        Collections.sort(list, new ComparadorItemsChecklist());
        adapter = new ItemCheckListAdapter(getActivity(),getActivity(), list, callbackProgress);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPref = context.getSharedPreferences("check_list", Context.MODE_PRIVATE);
        itemsDriver = SqliteClass.getInstance(getActivity()).databasehelp.itemSql.getItemsBySection("1",sharedPref.getString("id_check_list_sql",""));
        getList(itemsDriver);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void updateItemState() {
        adapter.notifyDataSetChanged();
    }
}