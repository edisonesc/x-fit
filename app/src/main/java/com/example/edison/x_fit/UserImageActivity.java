package com.example.edison.x_fit;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.felipecsl.asymmetricgridview.library.Utils;
import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

public class UserImageActivity extends AppCompatActivity {
    private ArrayList<String> mData = new ArrayList<>();
    private  RecyclerView recyclerView;
    CustomRecyclerAdapter customRecyclerAdapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_images_layout);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView = findViewById(R.id.recycler_viewImage);
        customRecyclerAdapter = new CustomRecyclerAdapter(this, mData);

        mData.add("adasd");
        mData.add("adasd");
        mData.add("adasd");
        mData.add("adasd");

        recyclerView.setAdapter(customRecyclerAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);

    }
}