package com.example.edison.x_fit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManagerNonConfig;
import android.support.v4.app.FragmentStatePagerAdapter;



public class PagerAdapter extends FragmentStatePagerAdapter {
    int numOfTabs;
    public PagerAdapter (FragmentManager fm, int NumberOfTabs){
        super(fm);
        this.numOfTabs = NumberOfTabs;

    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                NutritionGraph nutritionGraph = new NutritionGraph();
                return  nutritionGraph;

            case 1:
                NutritionData nutritionData = new NutritionData();
                return nutritionData;
                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
