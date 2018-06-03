package com.example.edison.x_fit;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.felipecsl.asymmetricgridview.library.Utils;
import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vstechlab.easyfonts.EasyFonts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.jar.Attributes;

public class UserImageActivity extends AppCompatActivity {
    private ArrayList<String> mData = new ArrayList<>(), mKeys = new ArrayList<>();
    private  RecyclerView recyclerView;
    CustomRecyclerAdapter customRecyclerAdapter;
    private DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    TextView title;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_images_layout);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView = findViewById(R.id.recycler_viewImage);
        customRecyclerAdapter = new CustomRecyclerAdapter(this, mData, mKeys);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        title = findViewById(R.id.title);
        title.setTypeface(EasyFonts.caviarDreams(this));
        String userUid = firebaseUser.getUid();
        recyclerView.setAdapter(customRecyclerAdapter);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").child(userUid).child("Social").child("Images").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue(String.class);
                String keys = dataSnapshot.getKey();
                mData.add(value);


                int month = Integer.valueOf(keys.substring(0, 2));
                int day = Integer.valueOf(keys.substring(3, 5));
                int year = Integer.valueOf(keys.substring(6, 10));
                int seconds = Integer.valueOf(keys.substring(11, 13));
                Calendar date = new GregorianCalendar(year, month, day);
                String dateOfValue = new SimpleDateFormat("MMM").format(date.getTime()) +
                        " " + new SimpleDateFormat("dd").format(date.getTime()) + " " +
                        new SimpleDateFormat("yyyy").format(date.getTime()) + " " + seconds;
                Log.d("KEY", keys);
                mKeys.add(dateOfValue);
                customRecyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                int index = mData.indexOf(value);
//                mData.remove(index);
//                mKeys.remove(index);
//
//                customRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}