package com.ashish.mymall;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ProductdetaisAdapter extends FragmentPagerAdapter {

    private int totalTabs;

    public ProductdetaisAdapter(FragmentManager fm,int totalTabs) {
        super(fm);
        this.totalTabs=totalTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new ProductDescriptionFragment();
            case 1:
                return new ProductSpecificationFragment();
            case 2:
                return new ProductDescriptionFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
