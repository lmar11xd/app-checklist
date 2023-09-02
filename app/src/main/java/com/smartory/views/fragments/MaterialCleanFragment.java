package com.smartory.views.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class MaterialCleanFragment extends Fragment {
    public final String FRAGMENT_SECTION_ID = "7";
    RecyclerView recyclerView;
    ArrayList<ItemCheckListModel> itemsSum;
    Context context;
    ItemCheckListAdapter adapter;
    IChecklistProgress callbackProgress;

    public MaterialCleanFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_check_list, container, false);
        context=getActivity();
        recyclerView = view.findViewById(R.id.recycler_view_suministro);
        SharedPreferences sharedPref = context.getSharedPreferences("check_list", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("fragment", FRAGMENT_SECTION_ID);
        editor.apply();
        itemsSum = SqliteClass.getInstance(getActivity()).databasehelp.itemSql.getItemsBySection("7",sharedPref.getString("id_check_list_sql",""));
        getList(itemsSum);
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
        itemsSum = SqliteClass.getInstance(getActivity()).databasehelp.itemSql.getItemsBySection("7",sharedPref.getString("id_check_list_sql",""));
        getList(itemsSum);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void updateItemState() {
        adapter.notifyDataSetChanged();
    }
}