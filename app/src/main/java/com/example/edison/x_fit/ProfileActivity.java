package com.example.edison.x_fit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {
    DatabaseReference databaseRef;
    FirebaseAuth mAuth;
    Calendar calendar;
    FirebaseUser firebaseUser;
    TextView accountUsername, accountCustomWorkouts, accountFollowers, accountFollowing, accountName;
    TextView accountAge, accountGender, accountWeight, accountHeight;
    ImageView userProfilePicture, accountGenderIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        databaseRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();


        accountUsername = findViewById(R.id.accountUsername);
        accountCustomWorkouts = findViewById(R.id.customWorkouts);
        accountFollowers = findViewById(R.id.followers);
        accountFollowing =  findViewById(R.id.following);
        accountName = findViewById(R.id.accountName);
        accountAge = findViewById(R.id.accountAge);
        accountGender = findViewById(R.id.accountGender);
        accountWeight = findViewById(R.id.accountWeight);
        accountHeight = findViewById(R.id.accountHeight);
        userProfilePicture = findViewById(R.id.userProfilePicture);
        accountGenderIcon = findViewById(R.id.accountGenderIcon);

        String currentUserUid = mAuth.getCurrentUser().getUid();

        databaseRef.child("Users").child(currentUserUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String age = String.valueOf(dataSnapshot.child("Age").getValue(int.class));
                String gender = dataSnapshot.child("Gender").getValue(String.class);
                String heightFt = String.valueOf(dataSnapshot.child("Height Ft").getValue(int.class));
                String heightIn = String.valueOf(dataSnapshot.child("Height In").getValue(int.class));
                String weight =  String.valueOf(dataSnapshot.child("Weight").getValue(int.class));
                String weightUnit = dataSnapshot.child("WeightUnit").getValue(String.class);
                String name = dataSnapshot.child("Name").getValue(String.class);
                String username = dataSnapshot.child("Username").getValue(String.class);
                String currCustomWorkouts = String.valueOf(dataSnapshot.child("Social").child("Custom Workouts").getChildrenCount());
                String currFollowers = String.valueOf(dataSnapshot.child("Social").child("Followers").getChildrenCount());
                String currFollowing = String.valueOf(dataSnapshot.child("Social").child("Following").getChildrenCount());
                switch (gender){
                    case "Male":
                        accountGenderIcon.setBackgroundResource(R.drawable.male_2);
                        break;
                    case "Female":
                        accountGenderIcon.setBackgroundResource(R.drawable.female_2);
                        break;
                    case "Other":
                        accountGenderIcon.setBackgroundResource(R.drawable.ic_panorama_fish_eye_black_24dp);
                        break;
                }

                accountCustomWorkouts.setText(currCustomWorkouts.equals(null) ? "0" : currCustomWorkouts);
                accountFollowers.setText(currFollowers.equals(null) ? "0" : currFollowers);
                accountFollowing.setText(currFollowing.equals(null) ? "0" : currFollowing);


                accountAge.setText(age);
                accountUsername.setText(username);
                accountName.setText(name);
                accountWeight.setText(weight + " " + weightUnit);
                accountHeight.setText(heightFt+"'"+heightIn+'"');
                accountGender.setText(gender);





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
