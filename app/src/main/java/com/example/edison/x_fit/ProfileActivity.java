package com.example.edison.x_fit;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.bumptech.glide.Glide;
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.widgets.ProgressDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karan.churi.PermissionManager.PermissionManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;

public class ProfileActivity extends AppCompatActivity {
    private DatabaseReference databaseRef;
    private FirebaseAuth mAuth;
    private String currentUserUid;
    private FirebaseUser firebaseUser;
    private TextView accountUsername, accountCustomWorkouts, accountFollowers, accountFollowing, accountName;
    private TextView accountAge, accountGender, accountWeight, accountHeight;
    private ImageView userProfilePicture, accountGenderIcon;
    private ButtonFloat setNewProfile;
    private Uri resultUri;
    private StorageReference mStorage;
    private MaterialDialog materialDialog;
    private ProgressDialog dialog;
    private String profileImageUrl;
    PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        databaseRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference();
        setNewProfile = findViewById(R.id.buttonFloat);
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
        dialog = new ProgressDialog(this, "Uploading");

        currentUserUid = mAuth.getCurrentUser().getUid();

        permissionManager = new PermissionManager() {};
        permissionManager.checkAndRequestPermissions(this);

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
                String currProfilePicUrl = dataSnapshot.child("ProfileImage").getValue(String.class);
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

                Glide.clear(userProfilePicture);
                if(currProfilePicUrl != null){


                            Glide.with(getApplication()).load(currProfilePicUrl).centerCrop().into(userProfilePicture);


                }

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


        setNewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            try {
                dialog.show();
                Uri imageUri = data.getData();
                final Map userImage = new HashMap(), userImages = new HashMap();
                resultUri = imageUri;
                SimpleDateFormat dtf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                SimpleDateFormat dtfRef = new SimpleDateFormat("MM dd yyyy");
                final Date date = new Date();

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));

                final String seconds = String.valueOf(calendar.get(Calendar.SECOND));
                final String theDate = String.valueOf(calendar.get(Calendar.MONTH));
                final String dateRef = String.valueOf(dtfRef.format(date));


                final String compatDate = String.valueOf(dtf.format(date)).replace('/', '-');
                StorageReference filepath = mStorage.child("Users").child(currentUserUid).child("Profile Images").child(String.valueOf(compatDate + "_" + System.currentTimeMillis() + "." + getImageExt(resultUri)));

                filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        userProfilePicture.setImageURI(resultUri);
                        Uri downloadUri = taskSnapshot.getDownloadUrl();
                        userImage.put("ProfileImage", downloadUri.toString());
                        databaseRef.child("Users").child(currentUserUid).updateChildren(userImage);




                        userImages.put((theDate.length() == 1 ? "0"+theDate: theDate)
                                + "" + dateRef.substring(2, dateRef.length()) +" "+  (seconds.length()== 1 ? "0" + seconds : seconds), downloadUri.toString());
                        databaseRef.child("Users").child(currentUserUid).child("Social").child("Images").updateChildren(userImages);
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, "Failed to change profile picture", Toast.LENGTH_SHORT).show();
                    }
                });




            }
            catch (Exception e){
                dialog.dismiss();
                Toast.makeText(this, "Error: Please check permissions", Toast.LENGTH_SHORT).show();

            }
        }
    }


    public String getImageExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.checkResult(requestCode, permissions, grantResults);

    }
    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }

}
