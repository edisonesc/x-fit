package com.example.edison.x_fit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.impl.LinearProgressButton;
import com.dd.processbutton.FlatButton;
import com.dd.processbutton.iml.ActionProcessButton;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.widgets.ProgressDialog;
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
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vstechlab.easyfonts.EasyFonts;

public class MainActivity extends AppCompatActivity {
    private ActionProcessButton mLogin;
    private MaterialEditText mUsername, mPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        TextView title = findViewById(R.id.textViewTitle), subtitle = findViewById(R.id.textViewSubtitle);
        title.setTypeface(EasyFonts.captureIt(this));
        subtitle.setTypeface(EasyFonts.caviarDreams(this));
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

        mLogin.setMode(ActionProcessButton.Mode.ENDLESS);
        mLogin.setProgress(0);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!mUsername.getText().toString().isEmpty() || !mPassword.getText().toString().isEmpty() ||
                        !mUsername.getText().toString().isEmpty() &&
                        !mPassword.getText().toString().isEmpty()){
                    mLogin.setProgress(60);
                final String username = mUsername.getText().toString();
                final String password = mPassword.getText().toString();
                    mLogin.setProgress(75);
                    mDatabase.child("Usernames").child(username).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mLogin.setProgress(85);
                            final String email = dataSnapshot.child("Email").getValue(String.class);

                            if (email != null) {
                                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(MainActivity.this, "Login problem", Toast.LENGTH_SHORT).show();
                                            mLogin.setProgress(-1);
                                        }
                                        else{mLogin.setProgress(100);}

                                    }
                                });
                            } else {
                                mLogin.setProgress(-1);
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
