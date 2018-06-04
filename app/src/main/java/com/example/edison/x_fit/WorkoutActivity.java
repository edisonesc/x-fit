package com.example.edison.x_fit;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
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


public class WorkoutActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, WorkoutBrowse.OnFragmentInteractionListener
        , WorkoutMyWorkout.OnFragmentInteractionListener, WorkoutCustom.OnFragmentInteractionListener,
        WorkoutProgress.OnFragmentInteractionListener



{
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
    KenBurnsView coverPhoto;
    ImageView hamburger;
    ConstraintLayout constraintLayout;
    ActionBarDrawerToggle mToggle;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    private static final long RIPPLE_DURATION = 250;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        ButterKnife.bind(this);
        constraintLayout = findViewById(R.id.constraintLayout);
//        recyclerView = findViewById(R.id.recycler_view);
//        mListView = findViewById(R.id.listview);
//        showEmptyList = findViewById(R.id.emptyWorkout);
//        mAddWorkout = findViewById(R.id.button8);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
//        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));
        mUser = mAuth.getCurrentUser();
        title = getWindow().getDecorView().findViewById(R.id.titleWorkout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        title.setTypeface(EasyFonts.ostrichBlack(this));
//        animator = new SlideInLeftAnimator(new OvershootInterpolator(1f));
//        recyclerView.setItemAnimator(animator);
        final String user = mUser.getUid();
//        mAdapter = new RecyclerViewAdapter(mContext, mWorkout);
        drawerLayout = findViewById(R.id.drawerlayout);
        android.support.v4.app.Fragment fragment = new WorkoutMyWorkout();

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.my_pager, fragment);
        fragmentTransaction.commit();

        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open,R.string.close){
            @Override
            public void onDrawerOpened(View drawerView) {

                navigationView.bringToFront();
                navigationView.requestLayout();
                Animation rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
                hamburger.startAnimation(rotate);
                mUsername = findViewById(R.id.currentUsername);
                userProfilePic = getWindow().getDecorView().findViewById(R.id.currentUserPicture);
                coverPhoto = findViewById(R.id.image);
                databaseReference.child("Users").child(user).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String userPicture = dataSnapshot.child("ProfileImage").getValue(String.class);
                        String userUsername = dataSnapshot.child("Username").getValue(String.class);
                        String coverPhotoUrl = dataSnapshot.child("Cover Photo").getValue(String.class);
                        Glide.clear(userProfilePic);
                        if(userPicture != null){
                            Glide.with(getApplication()).load(userPicture).centerCrop().into(userProfilePic);

                        }
                        if(coverPhoto != null){
                            Glide.with(getApplication()).load(coverPhotoUrl).into(coverPhoto);
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

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        drawerLayout.closeDrawers();
        android.support.v4.app.Fragment fragment = null;

        switch (id){
            case R.id.browseWorkout:

            case R.id.customWorkout:

            case R.id.progressWorkout:

            case R.id.myWorkouts:


        }
        if(id == R.id.browseWorkout ){
            fragment = new WorkoutBrowse();
        }
        else if(id == R.id.customWorkout){
            fragment = new WorkoutCustom();

        }
        else if(id == R.id.progressWorkout){
            fragment = new WorkoutProgress();
        }
        else if(id == R.id.myWorkouts){
            fragment = new WorkoutMyWorkout();
        }
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.my_pager, fragment);
        fragmentTransaction.commit();


        DrawerLayout drawerLayout = findViewById(R.id.drawerlayout);
        drawerLayout.closeDrawer(Gravity.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
