package com.example.anuj.e_co.Coupons;


import android.support.v7.widget.CardView;

public interface CouponsCardAdapter {

    int MAX_ELEVATION_FACTOR = 8;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();


}
