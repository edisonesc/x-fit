package com.example.edison.x_fit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private Button mLogin;
    private EditText mUsername, mPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    //Intent of the user
                    Toast.makeText(MainActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }
                else{
                    Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                }

            }
        };
        mLogin = findViewById(R.id.buttonLogin);
        mUsername = findViewById(R.id.editTextUsername);
        mPassword = findViewById(R.id.editText9);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!mUsername.getText().toString().isEmpty() || !mPassword.getText().toString().isEmpty() ||
                        !mUsername.getText().toString().isEmpty() &&
                        !mPassword.getText().toString().isEmpty()){
                final String username = mUsername.getText().toString();
                final String password = mPassword.getText().toString();

                    mDatabase.child("Usernames").child(username).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final String email = dataSnapshot.child("Email").getValue(String.class);

                            if (email != null) {
                                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(MainActivity.this, "Login problem", Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });
                            } else {
                                Toast.makeText(MainActivity.this, "Error ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            else {
                    Toast.makeText(MainActivity.this, "Please check fields", Toast.LENGTH_SHORT).show();
                }

            }


        });
    }


    public void registerRoute(View view) {
        Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(registerIntent);

    }
    private void updateUI(FirebaseUser arg) {
        Toast.makeText(this, arg.toString() +" is logged in", Toast.LENGTH_SHORT).show();
        Intent accIntent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(accIntent);
        finish();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}
