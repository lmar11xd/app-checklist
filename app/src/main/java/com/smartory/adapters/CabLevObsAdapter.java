package com.smartory.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartory.R;
import com.smartory.db.SqliteClass;
import com.google.android.material.card.MaterialCardView;
import com.smartory.models.LevObsModel;

import java.util.ArrayList;
import java.util.List;

import static com.smartory.resource.HorizontalValues.cabLevObsAdapter;

public class CabLevObsAdapter extends RecyclerView.Adapter<CabLevObsAdapter.ViewHolder> {
    Context context;
    Activity activity;
    List<LevObsModel> items;
    CabLevObsAdapter inter = null;

    public CabLevObsAdapter(Activity activity, Context context, ArrayList<LevObsModel> items) {
        this.context = context;
        inter = this;
        this.items = items;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.row_lev_obs, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView text_number_checkList,tv_cant_checkList,tv_date,tv_name_company;
        public MaterialCardView ideaCard_cab_lev_obs;
        public RecyclerView rcv_items;
        public ImageView img_detail;
        @SuppressLint("WrongViewCast")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_number_checkList = (TextView) itemView.findViewById(R.id.tv_number_checkList);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_name_company = (TextView) itemView.findViewById(R.id.tv_name_company);
            tv_cant_checkList = (TextView) itemView.findViewById(R.id.tv_cant_checkList);
            ideaCard_cab_lev_obs = (MaterialCardView) itemView.findViewById(R.id.ideaCard_cab_lev_obs);
            img_detail =(ImageView) itemView.findViewById(R.id.img_detail);
            rcv_items = (RecyclerView) itemView.findViewById(R.id.recycler_view_items_obse);
        }

        @SuppressLint("SetTextI18n")
        public void bind (final LevObsModel levObsModel){
            text_number_checkList.setText(levObsModel.getCheck_list());
            tv_name_company.setText(levObsModel.getTransport_company());
            tv_date.setText(levObsModel.getCheck_list_date());
            tv_cant_checkList.setText("Cantidad : "+SqliteClass.getInstance(context).databasehelp.levObsSql.getAllLevOsByNumber(levObsModel.check_list).size());
            getList(SqliteClass.getInstance(context).databasehelp.levObsSql.getAllLevOsByNumber(String.valueOf(levObsModel.check_list)));
            ideaCard_cab_lev_obs.setOnClickListener(v -> {
                cabLevObsAdapter = this;
                if(rcv_items.getVisibility()== View.VISIBLE){
                    rcv_items.setVisibility(View.GONE);
                    img_detail.setImageResource(R.drawable.ic_expand_more_black_24dp);
                } else {
                    rcv_items.setVisibility(View.VISIBLE);
                    img_detail.setImageResource(R.drawable.ic_expand_less_black_24dp);
                }
            });
        }

        public void getList(ArrayList<LevObsModel> list){
            if(list.size()>0){
                ItemLevAdapter adapter = new ItemLevAdapter(activity, context, list, inter);
                rcv_items.setAdapter(adapter);
                rcv_items.setLayoutManager(new LinearLayoutManager(context));
            }
        }
    }
}