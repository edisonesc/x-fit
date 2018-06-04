package com.example.edison.x_fit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import me.drakeet.materialdialog.MaterialDialog;

public class SettingsActivity extends AppCompatActivity {
    LinearLayout mLogout, mDeleteAccount;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseRef;
    private  MaterialDialog materialDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mLogout = findViewById(R.id.settingsLogout);
        mAuth = FirebaseAuth.getInstance();
        mDeleteAccount = findViewById(R.id.settingsDelete);
        databaseRef = FirebaseDatabase.getInstance().getReference();
        mUser = mAuth.getCurrentUser();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    //Intent of the user
                    Toast.makeText(SettingsActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI();
                }
                else{
                    Toast.makeText(SettingsActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                }
            }
        };
        mDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog = new MaterialDialog(SettingsActivity.this)
                        .setMessage("Are you sure you want to delete this account?")
                        .setPositiveButton("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                databaseRef.child("Users").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String username = "No Value";
                                        username = dataSnapshot.child("Username").getValue(String.class);

                                        DatabaseReference databaseReferenceUsernames = FirebaseDatabase.getInstance().getReference();
                                        assert username != null;
                                        databaseReferenceUsernames.child("Usernames").child(username).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                databaseRef.child("Users").child(mUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(SettingsActivity.this, "User datas totally deleted", Toast.LENGTH_SHORT).show();
                                                        mUser.delete();
                                                        materialDialog.dismiss();
                                                        updateUI();
                                                    }
                                                }) .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(SettingsActivity.this, "Failed to delete user data", Toast.LENGTH_SHORT).show();
                                                    }
                                                });


                                                Toast.makeText(SettingsActivity.this, "Username removed", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(SettingsActivity.this, "failed to delete username", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


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
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               materialDialog = new MaterialDialog(SettingsActivity.this)

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
    }

    private void updateUI() {
        Toast.makeText(this, "User is logged out", Toast.LENGTH_SHORT).show();
        Intent accIntent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(accIntent);
        finish();
    }
}
