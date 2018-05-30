package com.example.edison.x_fit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class RegisterLastStep extends AppCompatActivity {
    private String name, email,gender, weightUnit;
    private double heightIn;
    private int weight, age, heightFt;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ActionProcessButton mRegister;
    private MaterialEditText mUsername, mPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_last_step);
        mUsername = findViewById(R.id.editTextUsername);
        mPassword = findViewById(R.id.editTextPassword);
        mRegister = findViewById(R.id.button3);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    //Intent of the user
                    Toast.makeText(RegisterLastStep.this, "Logged In", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }
                else{
                    Toast.makeText(RegisterLastStep.this, "Logged Out", Toast.LENGTH_SHORT).show();
                }

            }
        };
        Bundle clientInfo = getIntent().getExtras();
        if(clientInfo != null){
        name = clientInfo.getString("Name");
        email = clientInfo.getString("Email");
        gender = clientInfo.getString("Gender");
        weightUnit = clientInfo.getString("WeightUnit");
        age = clientInfo.getInt("Age");
        heightFt = clientInfo.getInt("HeightFt");
        heightIn = clientInfo.getDouble("HeightIn");
        weight = clientInfo.getInt("Weight");
        }
//        Toast.makeText(this, name + email + gender + weight + weightUnit + age + heightIn + heightFt , Toast.LENGTH_SHORT).show();
        mRegister.setMode(ActionProcessButton.Mode.PROGRESS);
        mRegister.setProgress(0);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRegister.setProgress(0);
                final String client_email = email;
                final String client_password = mPassword.getText().toString();
                final String client_username = mUsername.getText().toString();
                if(!client_username.isEmpty() && !client_password.isEmpty()){
                    mRegister.setProgress(50);
                    mAuth.createUserWithEmailAndPassword(client_email, client_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = mAuth.getCurrentUser();
                                String userId = mAuth.getCurrentUser().getUid();
                                DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                                Map userInfo = new HashMap();
                                userInfo.put("Username", client_username);
                                userInfo.put("Email", client_email);
                                userInfo.put("Name", name);
                                userInfo.put("Gender", gender);
                                userInfo.put("Age", age);
                                userInfo.put("Weight", weight);
                                userInfo.put("WeightUnit", weightUnit);
                                userInfo.put("Height Ft", heightFt);
                                userInfo.put("Height In", heightIn);
                                currentUserDb.updateChildren(userInfo);
                                Toast.makeText(RegisterLastStep.this, "Account register successful", Toast.LENGTH_SHORT).show();
                                mRegister.setProgress(75);
                                DatabaseReference storeEmail = FirebaseDatabase.getInstance().getReference().child("Usernames").child(client_username);
                                Map userEmail = new HashMap();
                                userEmail.put("Email", client_email);
                                storeEmail.updateChildren(userEmail);
                                mRegister.setProgress(100);
                                updateUI(user);

                            }
                            else{
                                Toast.makeText(RegisterLastStep.this, "Register Failed", Toast.LENGTH_SHORT).show();
                                mRegister.setProgress(-1);
                            }
                        }
                    });

                }
                else
                {
                    Toast.makeText(RegisterLastStep.this, "Please check fields", Toast.LENGTH_SHORT).show();
                        mRegister.setProgress(-1);
                }
            }
        });
    }
    private void updateUI(FirebaseUser arg) {
        Toast.makeText(this, arg.toString() +" is logged in", Toast.LENGTH_SHORT).show();
        Intent accIntent = new Intent(RegisterLastStep.this, MainActivity.class);
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

    @Override
    protected void onResume() {
        super.onResume();
        mRegister.setProgress(0);
    }
}
