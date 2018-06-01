package com.example.edison.x_fit;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;

import github.chenupt.springindicator.SpringIndicator;

public class NutritionActivity extends AppCompatActivity implements NutritionData.OnFragmentInteractionListener, NutritionGraph.OnFragmentInteractionListener {
    private ViewPager viewPager;
    private SpringIndicator springIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);
        viewPager = findViewById(R.id.pager);
        springIndicator = findViewById(R.id.indicator);



        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Data"));
        tabLayout.addTab(tabLayout.newTab().setText("Graph"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getChildCount());
        viewPager.setAdapter(adapter);
        springIndicator.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
