package com.example.anuj.e_co.BottomSheet;

/**
 * Created by abhi on 21/10/17.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anuj.e_co.R;

import java.util.List;

public class BottomSheetRecyclerViewAdapter extends RecyclerView.Adapter<BottomSheetRecyclerViewHolder> {

    private List<BottomSheetItemObject> itemList;
    private Context context;

    public BottomSheetRecyclerViewAdapter(Context context, List<BottomSheetItemObject> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public BottomSheetRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottomsheet_card_view_list, null);
        BottomSheetRecyclerViewHolder rcv = new BottomSheetRecyclerViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(BottomSheetRecyclerViewHolder holder, int position) {
        holder.countryName.setText(itemList.get(position).getName());
        holder.countryPhoto.setImageResource(itemList.get(position).getPhoto());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}