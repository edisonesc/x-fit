package com.example.edison.x_fit;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.ArraySwipeAdapter;
import com.flaviofaria.kenburnsview.KenBurnsView;
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
import com.vstechlab.easyfonts.EasyFonts;
import com.yalantis.guillotine.animation.GuillotineAnimation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.jar.Attributes;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WorkoutActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private ArrayList<String> mWorkout = new ArrayList<>();
    private TextView showEmptyList, title, mUsername;
    private Context mContext = this;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    private ImageView userProfilePic;
    private ListView mListView;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Button mAddWorkout;
    boolean isShow = false;
    ImageView hamburger;
    ConstraintLayout constraintLayout;
    ActionBarDrawerToggle mToggle;
    DrawerLayout drawerLayout;
    private static final long RIPPLE_DURATION = 250;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        ButterKnife.bind(this);
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

        title = getWindow().getDecorView().findViewById(R.id.titleWorkout);

        title.setTypeface(EasyFonts.ostrichBlack(this));
//        animator = new SlideInLeftAnimator(new OvershootInterpolator(1f));
//        recyclerView.setItemAnimator(animator);
        final String user = mUser.getUid();
        mAdapter = new RecyclerViewAdapter(mContext, mWorkout);
        drawerLayout = findViewById(R.id.drawerlayout);



        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open,R.string.close){
            @Override
            public void onDrawerOpened(View drawerView) {
                Animation rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
                hamburger.startAnimation(rotate);
                mUsername = findViewById(R.id.currentUsername);
                userProfilePic = getWindow().getDecorView().findViewById(R.id.currentUserPicture);
                KenBurnsView coverPhoto = findViewById(R.id.image);
                databaseReference.child("Users").child(user).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String userPicture = dataSnapshot.child("ProfileImage").getValue(String.class);
                        String userUsername = dataSnapshot.child("Username").getValue(String.class);

                        Glide.clear(userProfilePic);
                        if(userPicture != null){
                            Glide.with(getApplication()).load(userPicture).centerCrop().into(userProfilePic);
                        }
                        mUsername.setText(userUsername);
                        mUsername.setTypeface(EasyFonts.caviarDreams(getApplicationContext()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                userProfilePic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent);

                    }
                });
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if(slideOffset == 0.000005f){
                    Toast.makeText(mContext, "3333333", Toast.LENGTH_SHORT).show();
                }
                else if(slideOffset == 10){

                    Toast.makeText(mContext, "sadasdasdasd", Toast.LENGTH_SHORT).show();
                }
                super.onDrawerSlide(drawerView, slideOffset);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Animation rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);
                hamburger.startAnimation(rotate);
                super.onDrawerClosed(drawerView);

            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {

                switch (item.getItemId()){

                    case R.id.browseWorkout:
                        Toast.makeText(mContext, "hjfiwsdjf", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return super.onOptionsItemSelected(item);
                }





            }
        };
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        hamburger = findViewById(R.id.content_ham);

        hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isShow == false){

                drawerLayout.openDrawer(Gravity.LEFT);
                }
                else{
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
                }

        });
        recyclerView.setAdapter(mAdapter);
        databaseReference.child("Users").child(user).child("Workouts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Object value = dataSnapshot.getKey();
                mWorkout.add(String.valueOf(value));
                int index = mWorkout.indexOf(value);
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
                String value = dataSnapshot.getKey();
                int index = mWorkout.indexOf(value);
                mWorkout.set(index, value);
               // mAdapter.notifyItemChanged(index);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                String value = dataSnapshot.getKey();
//                int index = mWorkout.indexOf(value);
//                mWorkout.remove(index );
//                mAdapter.notifyDataSetChanged();
//                mAdapter.notifyItemRemoved(index);

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

//    public void browseWorkoutClicked(){
//        Toast.makeText(mContext, "jasodj", Toast.LENGTH_SHORT).show();
//    }
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

}
