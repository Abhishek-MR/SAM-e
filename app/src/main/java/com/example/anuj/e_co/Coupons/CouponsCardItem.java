package com.example.anuj.e_co.Coupons;


public class CouponsCardItem {

    private int mTextResource;
    public int mTitleResource;

    public CouponsCardItem(int title, int text) {
        mTitleResource = title;
        mTextResource = text;
    }

    public int getText() {
        return mTextResource;
    }

    public int getTitle() {
        return mTitleResource;
    }
}
