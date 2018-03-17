package com.example.anuj.e_co.Coupons;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class CouponsCardFragmentPagerAdapter extends FragmentStatePagerAdapter implements CouponsCardAdapter {

    private List<CouponsCardFragment> mFragments;
    private float mBaseElevation;
    public int pos;


    public CouponsCardFragmentPagerAdapter(FragmentManager fm, float baseElevation) {
        super(fm);
        mFragments = new ArrayList<>();
        mBaseElevation = baseElevation;

        for(int i = 0; i< 8; i++){
            addCardFragment(new CouponsCardFragment());
        }

    }

    @Override
    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {

        return mFragments.get(position).getCardView();

    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        mFragments.set(position, (CouponsCardFragment) fragment);
        return fragment;
    }

    public void addCardFragment(CouponsCardFragment fragment) {
        mFragments.add(fragment);
    }

}
