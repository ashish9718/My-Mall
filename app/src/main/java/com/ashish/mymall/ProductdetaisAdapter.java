package com.ashish.mymall;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class ProductdetaisAdapter extends FragmentPagerAdapter {

    private int totalTabs;
    private String productDesc,productOtherDetails;
    private List<ProductSpecificationModel> productSpecificationModelList;

    public ProductdetaisAdapter(FragmentManager fm, int totalTabs, String productDesc, String productOtherDetails, List<ProductSpecificationModel> productSpecificationModelList) {
        super(fm);
        this.totalTabs = totalTabs;
        this.productDesc = productDesc;
        this.productOtherDetails = productOtherDetails;
        this.productSpecificationModelList = productSpecificationModelList;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                ProductDescriptionFragment productDescriptionFragment1=new ProductDescriptionFragment();
                productDescriptionFragment1.body=productDesc;
                return productDescriptionFragment1;
            case 1:
                ProductSpecificationFragment productSpecificationFragment=new ProductSpecificationFragment();
                productSpecificationFragment.productSpecificationModelList=productSpecificationModelList;
                return productSpecificationFragment;
            case 2:
                ProductDescriptionFragment productDescriptionFragment2=new ProductDescriptionFragment();
                productDescriptionFragment2.body=productOtherDetails;
                return productDescriptionFragment2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
