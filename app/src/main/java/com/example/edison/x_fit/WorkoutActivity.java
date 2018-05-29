package com.example.edison.x_fit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.ArraySwipeAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.jar.Attributes;

public class WorkoutActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private ArrayList<String> mWorkout = new ArrayList<>();
    private TextView showEmptyList;
    private Context mContext = this;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private ListView mListView;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ConstraintLayout constraintLayout;
    private Button mAddWorkout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        constraintLayout = findViewById(R.id.constraintLayout);
        recyclerView = findViewById(R.id.recycler_view);
//        mListView = findViewById(R.id.listview);
        showEmptyList = findViewById(R.id.emptyWorkout);
        mAddWorkout = findViewById(R.id.button8);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));
        mUser = mAuth.getCurrentUser();

        final String user = mUser.getUid();
        mAdapter = new RecyclerViewAdapter(mContext, mWorkout);

        recyclerView.setAdapter(mAdapter);
        databaseReference.child("Users").child(user).child("Workouts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Object value = dataSnapshot.getKey();
                mWorkout.add(String.valueOf(value));

                if(mWorkout.isEmpty()){
                    showEmptyList.setVisibility(View.VISIBLE);
                }
                else {
                    showEmptyList.setVisibility(View.INVISIBLE);
                }
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        databaseReference.child("Users").child(user).child("Workouts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    showEmptyList.setVisibility(View.VISIBLE);
                }
                else{
                    showEmptyList.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        recyclerView.setOnScrollListener(onScrollListener);
        //mAdapter.setMode(Attributes.);




//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ((SwipeLayout) (mListView.getChildAt(position - mListView.getFirstVisiblePosition()))).open(true);
//            }
//        });


        mAddWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "button clicked", Toast.LENGTH_SHORT).show();
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                String random = "1234567890ABCDEFGHIJKLMNOPQR";
                Random r = new Random();
                final String  charS = String.valueOf(random.charAt(r.nextInt(random.length())) + " SECT");
                Map workoutAdd = new HashMap();
                workoutAdd.put(charS, true);
                dbRef.child("Users").child(user).child("Workouts").updateChildren(workoutAdd).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            mAdapter.notifyDataSetChanged();
                            Toast.makeText(mContext, charS, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }


    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

}
