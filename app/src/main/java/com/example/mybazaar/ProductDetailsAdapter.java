package com.example.mybazaar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ProductDetailsAdapter extends FragmentPagerAdapter {

    private int totalTabs;

    public ProductDetailsAdapter(FragmentManager fm,int totalTabs) {
        super(fm);
        this.totalTabs=totalTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                  ProductDetailsFragment productDetailsFragment=new ProductDetailsFragment();
                  return productDetailsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
