package com.example.anuj.e_co.BottomSheet;

/**
 * Created by abhi on 21/10/17.
 */


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anuj.e_co.CarAct;
import com.example.anuj.e_co.Carpool_act;
import com.example.anuj.e_co.HomeAccount;
import com.example.anuj.e_co.R;
import com.example.anuj.e_co.Recycler_act;

public class BottomSheetRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    Context context;
    public TextView countryName;
    public ImageView countryPhoto;

    public BottomSheetRecyclerViewHolder(View itemView) {

        super(itemView);
        context=itemView.getContext();
        itemView.setOnClickListener(this);
        countryName = (TextView)itemView.findViewById(R.id.country_name);
        countryPhoto = (ImageView)itemView.findViewById(R.id.country_photo);
    }

    
    @Override
    public void onClick(View view) {
        switch(getPosition())
        {
            case 0 :
                context.startActivity(new Intent(context,Carpool_act.class));
                break;
            case 1 :
                context.startActivity(new Intent(context,Recycler_act.class));
                break;
            case 2 :
                context.startActivity(new Intent(context,HomeAccount.class));
                break;
            case 3 :
                context.startActivity(new Intent(context,CarAct.class));
                break;


        }

    }
}