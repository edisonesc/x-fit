package com.example.edison.x_fit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    private LinearLayout mAccountSettings, mNameSetting, mUsernameSetting
            ,mEditAgeSetting, mWeightSetting, mHeightSetting;
    private MaterialEditText value;
    private MaterialDialog materialDialog;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private TextView nameShow, usernameShow, ageShow, weightShow, heightShow;
    private FirebaseUser mUser;
    private String previousUsername,convert = null;
    ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        final String uid = mUser.getUid();

        nameShow = findViewById(R.id.nameMain);
        usernameShow = findViewById(R.id.usernameMain);
        ageShow = findViewById(R.id.ageMain);
        weightShow = findViewById(R.id.weightMain);
        heightShow = findViewById(R.id.heightMain);

        mHeightSetting = findViewById(R.id.mHeightSetting);
        mWeightSetting = findViewById(R.id.editWeight);
        mEditAgeSetting = findViewById(R.id.editAge);
        mAccountSettings = findViewById(R.id.editAccountSetting);
        mUsernameSetting = findViewById(R.id.editUsername);

        mNameSetting = findViewById(R.id.editName);

        databaseReference.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Name").getValue(String.class);
                String username = dataSnapshot.child("Username").getValue(String.class);
                String age = String.valueOf(dataSnapshot.child("Age").getValue(Integer.class));
                String weightNow = String.valueOf(dataSnapshot.child("Weight").getValue(Integer.class));
                String weightUnitNow = dataSnapshot.child("WeightUnit").getValue(String.class);
                String heightFtNow = String.valueOf(dataSnapshot.child("Height Ft").getValue(Integer.class));
                String heightInNow = String.valueOf(dataSnapshot.child("Height In").getValue(Integer.class));



                nameShow.setText(name);
                usernameShow.setText(username);
                weightShow.setText(weightNow + " " + weightUnitNow);
                heightShow.setText(heightFtNow +"'"+ heightInNow + '"');
                ageShow.setText(age);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditProfileActivity.this, AccountSettingActivity.class);
                startActivity(i);

            }
        });
        mNameSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wrapInScrollView = true;

                materialDialog =
                        new MaterialDialog.Builder(EditProfileActivity.this)
                                .backgroundColorRes(R.color.colorPrimaryDark)
                                .customView(R.layout.custom_name_view, wrapInScrollView)
                                .positiveText("Apply")
                                .negativeText("Back").onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                value = materialDialog.getCustomView().findViewById(R.id.editTextName);
                                Map name = new HashMap();
                                name.put("Name", value.getText().toString());
                                databaseReference.child("Users").child(uid).updateChildren(name).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Toast.makeText(EditProfileActivity.this, "Name changed succesfully", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                materialDialog.dismiss();
                            }
                        })
                                .build();
                value = materialDialog.getCustomView().findViewById(R.id.editTextName);
                databaseReference.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("Name").getValue(String.class);
                        value.setHelperText("( " + name + " )");


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                materialDialog.show();

            }
        });


        mUsernameSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wrapInScrollView = true;
                materialDialog =
                        new MaterialDialog.Builder(EditProfileActivity.this)
                                .backgroundColorRes(R.color.colorPrimaryDark)
                                .customView(R.layout.custom_name_view, wrapInScrollView)
                                .positiveText("Apply")
                                .negativeText("Back").onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                value = materialDialog.getCustomView().findViewById(R.id.editTextName);
                                final Map name = new HashMap(), nameForUsername = new HashMap();
                                name.put("Username", value.getText().toString());

                                nameForUsername.put("Email", mUser.getEmail());
                                final String previousValue = value.getHelperText().substring(1, value.getHelperText().length() - 1).trim();
                                databaseReference.child("Users").child(uid).updateChildren(name).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Toast.makeText(EditProfileActivity.this, "Username changed succesfully", Toast.LENGTH_SHORT).show();

                                        databaseReference.child("Usernames").child(value.getText().toString()).updateChildren(nameForUsername).addOnSuccessListener(new OnSuccessListener() {
                                            @Override
                                            public void onSuccess(Object o) {

                                            }
                                        });
                                        databaseReference.child("Usernames").child(previousValue).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(EditProfileActivity.this, previousValue, Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                    }
                                });
                            }
                        }).onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                materialDialog.dismiss();
                            }
                        })
                                .build();
                value = materialDialog.getCustomView().findViewById(R.id.editTextName);
                databaseReference.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("Username").getValue(String.class);
                        value.setHint("Enter Username");
                        value.setFloatingLabelText("New username");
                        value.setHelperText("( " + name + " )");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                materialDialog.show();
            }
        });

        mWeightSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wrapInScrollView = true;
                materialDialog =
                        new MaterialDialog.Builder(EditProfileActivity.this)
                                .backgroundColorRes(R.color.colorPrimaryDark)
                                .customView(R.layout.custom_weight_view, wrapInScrollView)
                                .positiveText("Apply")
                                .negativeText("Back").onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                MaterialEditText value = materialDialog.getCustomView().findViewById(R.id.editTextWeight);
                                Spinner weightUnit = materialDialog.getCustomView().findViewById(R.id.spinnerGenderEdit);
                                String[] units = {"Kg", "Lbs"};
                                String selectedUnit = units[weightUnit.getSelectedItemPosition()];
                                Map name = new HashMap();
                                name.put("Weight", Double.valueOf(value.getText().toString()));
                                name.put("WeightUnit", selectedUnit);
                                databaseReference.child("Users").child(uid).updateChildren(name).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Toast.makeText(EditProfileActivity.this, "Weight changed succesfully", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                materialDialog.dismiss();
                            }
                        })
                                .build();
                final MaterialEditText value = materialDialog.getCustomView().findViewById(R.id.editTextWeight);
                databaseReference.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String weight = String.valueOf(dataSnapshot.child("Weight").getValue(Double.class));
                        String weightUnit = dataSnapshot.child("WeightUnit").getValue(String.class);

                        value.setHelperText("Current  " + weight + " " + weightUnit);

//                        value.setHint("Enter age");
//                        value.setInputType(InputType.TYPE_CLASS_NUMBER);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                materialDialog.show();

            }
        });

        mEditAgeSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wrapInScrollView = true;
                materialDialog =
                        new MaterialDialog.Builder(EditProfileActivity.this)
                                .backgroundColorRes(R.color.colorPrimaryDark)
                                .customView(R.layout.custom_name_view, wrapInScrollView)
                                .positiveText("Apply")
                                .negativeText("Back").onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                value = materialDialog.getCustomView().findViewById(R.id.editTextName);

                                Map name = new HashMap();
                                name.put("Age", Integer.valueOf(value.getText().toString()));
                                databaseReference.child("Users").child(uid).updateChildren(name).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Toast.makeText(EditProfileActivity.this, "Age changed succesfully", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                materialDialog.dismiss();
                            }
                        })
                                .build();
                value = materialDialog.getCustomView().findViewById(R.id.editTextName);
                databaseReference.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = String.valueOf(dataSnapshot.child("Age").getValue(Integer.class));
                        value.setHelperText("Current ( " + name + " )");

                        value.setHint("Enter age");
                        value.setInputType(InputType.TYPE_CLASS_NUMBER);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                materialDialog.show();

            }
        });



//
//
//
///
        mHeightSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wrapInScrollView = true;

                materialDialog =
                        new MaterialDialog.Builder(EditProfileActivity.this)
                                .backgroundColorRes(R.color.colorPrimaryDark)
                                .customView(R.layout.custom_height_view, wrapInScrollView)
                                .positiveText("Apply")
                                .negativeText("Back").onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                MaterialEditText heightFt = materialDialog.getCustomView().findViewById(R.id.editHeightFt);
                                MaterialEditText heightIn = materialDialog.getCustomView().findViewById(R.id.editHeightIn);
                                MaterialEditText heightCM = materialDialog.getCustomView().findViewById(R.id.editHeightCm);
                                Spinner heightUnit = materialDialog.getCustomView().findViewById(R.id.spinnerHeightEdit);

                                String[] units = {"ft", "cm"};
                                String selectedUnit = units[heightUnit.getSelectedItemPosition()];
                                String ft = null, in = null;

                                if (selectedUnit.equals("cm")){
                                   convert = String.valueOf(round(Double.valueOf(heightCM.getText().toString()) / 30.48, 1));
                                    ft = convert.substring(0, convert.indexOf("."));
                                    in = convert.substring(convert.indexOf('.') + 1);
                                }
                                else if(selectedUnit.equals("ft")){
                                    ft = heightFt.getText().toString();
                                    in = heightIn.getText().toString();

                                }



                                Map name = new HashMap();
                                name.put("Height Ft", Integer.valueOf(ft));
                                name.put("Height In", Integer.valueOf(in));
                                databaseReference.child("Users").child(uid).updateChildren(name).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Toast.makeText(EditProfileActivity.this, "Weight changed succesfully", Toast.LENGTH_SHORT).show();
                                    }
                                });}


                        }).onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                materialDialog.dismiss();
                            }
                        })
                                .build();
                final MaterialEditText heightFt1 = materialDialog.getCustomView().findViewById(R.id.editHeightFt);
                final MaterialEditText heightIn1 = materialDialog.getCustomView().findViewById(R.id.editHeightIn);
                final MaterialEditText heightCM1 = materialDialog.getCustomView().findViewById(R.id.editHeightCm);
                final Spinner heightUnit = materialDialog.getCustomView().findViewById(R.id.spinnerHeightEdit);
                String[] units = {"ft", "cm"};
                final String selectedUnit = units[heightUnit.getSelectedItemPosition()];


                heightUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 1){
                            heightFt1.setVisibility(View.GONE);
                            heightIn1.setVisibility(View.GONE);
                            heightCM1.setVisibility(View.VISIBLE);
                        }
                        else if(position == 0){
                            heightCM1.setVisibility(View.GONE);
                            heightFt1.setVisibility(View.VISIBLE);
                            heightIn1.setVisibility(View.VISIBLE);

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                databaseReference.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String heightFt = String.valueOf(dataSnapshot.child("Height Ft").getValue(Integer.class));
                        String heightIn = String.valueOf(dataSnapshot.child("Height In").getValue(Integer.class));


                        if(selectedUnit.equals("ft")){
                        heightFt1.setHelperText(heightFt);
                        heightIn1.setHelperText(heightIn);}
                        else if(heightUnit.getSelectedItem().equals("cm")){
                            heightCM1.setHelperText("dasda");
                            Toast.makeText(EditProfileActivity.this, "pota", Toast.LENGTH_SHORT).show();

                        }
//                        value.setHint("Enter age");

//                        value.setInputType(InputType.TYPE_CLASS_NUMBER);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                materialDialog.show();

            }
        });





    }
    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}
