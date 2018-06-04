package com.example.edison.x_fit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class CustomViewPagerAdapter extends FragmentStatePagerAdapter {
    int numOfTabs;
    public CustomViewPagerAdapter (FragmentManager fm, int NumberOfTabs){
        super(fm);
        this.numOfTabs = NumberOfTabs;

    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case R.id.browseWorkout:
                WorkoutBrowse workoutBrowse = new WorkoutBrowse();
                return workoutBrowse;

            case R.id.customWorkout:
                NutritionGraph nutritionGraph = new NutritionGraph();
                return  nutritionGraph;

            case R.id.myWorkouts:
                WorkoutMyWorkout workoutMyWorkout = new WorkoutMyWorkout();
                return  workoutMyWorkout;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
