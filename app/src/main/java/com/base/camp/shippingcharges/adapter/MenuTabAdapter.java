package com.base.camp.shippingcharges.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.base.camp.shippingcharges.fragment.HargaFragment;

public class MenuTabAdapter extends FragmentStatePagerAdapter {

    public MenuTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HargaFragment();
            case 1:
                return new HargaFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}

