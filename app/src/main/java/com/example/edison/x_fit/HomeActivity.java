package com.example.edison.x_fit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import me.drakeet.materialdialog.MaterialDialog;

public class HomeActivity extends AppCompatActivity {
    private CardView mLogout, mWorkout, mNutrition, mSleep, mProfile, mSettings ;
    private FirebaseAuth mAuth;
    private MaterialDialog materialDialog, mdExit;
    RelativeLayout relativeLayout;
    GridLayout gridLayout;
    Animation uptodown, downtoup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_home);
        mLogout = findViewById(R.id.buttonLogout);
        uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this, R.anim.downtoup);
        relativeLayout = findViewById(R.id.relativeLayout);
        gridLayout = findViewById(R.id.gridLayout);
        mWorkout = findViewById(R.id.buttonWorkout);
        mNutrition = findViewById(R.id.buttonNutrition);
        mSleep = findViewById(R.id.buttonSleep);
        mProfile = findViewById(R.id.buttonProfile);
        mSettings = findViewById(R.id.buttonSettings);
        relativeLayout.setAnimation(uptodown);
        gridLayout.setAnimation(downtoup);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                       materialDialog = new MaterialDialog(HomeActivity.this)

                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                mAuth.signOut();
                                updateUI();
                            }
                        }).setNegativeButton("Cancel", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                materialDialog.dismiss();
                            }
                        });
               materialDialog.show();

            }
        });

        mWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, WorkoutActivity.class);
                startActivity(intent);
            }
        });

        mSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, UserImageActivity.class);
                startActivity(intent);
            }
        });
        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            updateUI();
        }

    }

    private void updateUI() {
        Toast.makeText(this, "User is logged out", Toast.LENGTH_SHORT).show();
        Intent accIntent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(accIntent);
        finish();
    }

    @Override
    public void onBackPressed() {


        mdExit = new MaterialDialog(HomeActivity.this)

                .setMessage("Do you want to exit??")
                .setPositiveButton("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        finish();
                    }
                }).setNegativeButton("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mdExit.dismiss();

                    }
                });
        mdExit.show();

    }
}
