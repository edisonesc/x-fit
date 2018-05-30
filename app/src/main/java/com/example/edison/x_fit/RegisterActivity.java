package com.example.edison.x_fit;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.input.InputManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.rengwuxian.materialedittext.MaterialEditText;

public class RegisterActivity extends AppCompatActivity {
    private Spinner mGender, mWeightUnit;
    private MaterialEditText mName, mEmail, mWeight, mAge, mHeightFt, mHeightIn, mHeightCM;
    private String genders[] = {"Male", "Female", "Other"};
    private String weightUnits[] = {"Kg", "Lbs"};
    private int weightUnitIcon[] = {R.drawable.ic_power_input_black_24dp, R.drawable.ic_line_weight_black_24dp};
    private int genderIcon[] = {R.drawable.male_2, R.drawable.female_2, R.drawable.ic_panorama_fish_eye_black_24dp};
    private String isCMorFT;
    private ActionProcessButton btnSignIn;
    private LinearLayout heightFt, heightIn, heightCm;
    private boolean cm_ft;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout_final);
        //mRegister = findViewById(R.id.buttonRegister);
        mGender  = findViewById(R.id.spinnerGender);
        mWeightUnit = findViewById(R.id.spinnerWeightUnit);
        mName = findViewById(R.id.editTextName);
        mEmail = findViewById(R.id.editTextEmail);
        mWeight = findViewById(R.id.editTextWeight);
        mAge = findViewById(R.id.editTextAge);
        mHeightFt = findViewById(R.id.editTextHeightFt);
        mHeightIn = findViewById(R.id.editTextHeightIn);
        com.gc.materialdesign.views.Switch aSwitch = findViewById(R.id.switchView);
        heightCm = findViewById(R.id.layout_Height_Cm);
        heightFt = findViewById(R.id.layout_Height_Ft);
        heightIn = findViewById(R.id.layout_Height_In);
        mHeightCM = findViewById(R.id.editTextHeightCM);


        aSwitch.setOncheckListener(new com.gc.materialdesign.views.Switch.OnCheckListener() {
            @Override
            public void onCheck(com.gc.materialdesign.views.Switch view, boolean check) {
                if(!check){
                    heightCm.setVisibility(View.VISIBLE);
                    heightFt.setVisibility(View.INVISIBLE);
                    heightIn.setVisibility(View.INVISIBLE);
                    cm_ft = true;

                }
                else{
                    heightCm.setVisibility(View.INVISIBLE);
                    heightFt.setVisibility(View.VISIBLE);
                    heightIn.setVisibility(View.VISIBLE);
                    cm_ft = false;
                }
            }
        });


        CustomAdapter customAdapterGender = new CustomAdapter(getApplicationContext(),genderIcon, genders);
        mGender.setAdapter(customAdapterGender);
        CustomAdapter customAdapterWeight = new CustomAdapter(getApplicationContext(), weightUnitIcon, weightUnits);
        mWeightUnit.setAdapter(customAdapterWeight);

        //Performing action onItemSelected and onNothing selected
        btnSignIn = findViewById(R.id.btnSignIn);

        btnSignIn.setMode(ActionProcessButton.Mode.PROGRESS);




        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSignIn.setProgress(0);
                if (!mName.getText().toString().isEmpty() &&
                        !mEmail.getText().toString().isEmpty() &&
                        !mWeight.getText().toString().isEmpty() &&
                        !mAge.getText().toString().isEmpty() &&


                        (cm_ft ? !mHeightCM.getText().toString().isEmpty() : !mHeightFt.getText().toString().isEmpty()) &&
                        (cm_ft ? !mHeightCM.getText().toString().isEmpty() : !mHeightIn.getText().toString().isEmpty()) &&
                        !mWeight.getText().toString().equals(".")



                        ){
                    String name = mName.getText().toString();
                    String email = mEmail.getText().toString();
                    String gender = genders[mGender.getSelectedItemPosition()];
                    int weight = Integer.valueOf(mWeight.getText().toString());
                    int age = Integer.valueOf(mAge.getText().toString());
                    int heightFt = Integer.valueOf(mHeightFt.getText().toString().isEmpty() ? "0" : mHeightFt.getText().toString() );
                    double heightIn = Double.valueOf(mHeightIn.getText().toString().isEmpty() ? "0" : mHeightIn.getText().toString());
                    String weightUnit = weightUnits[mWeightUnit.getSelectedItemPosition()];
                    double mHeightCm = Double.valueOf(mHeightCM.getText().toString());
                    btnSignIn.setProgress(50);

                    if(!name.isEmpty() && email.contains("@") && email.contains(".com") &&
                            !String.valueOf(weight).isEmpty() &&
                            !String.valueOf(age).isEmpty() &&
                            (cm_ft ? !String.valueOf(mHeightCm).isEmpty(): !String.valueOf(heightFt).isEmpty()  )&&
                            (cm_ft ? !String.valueOf(mHeightCm).isEmpty():(!String.valueOf(heightIn).isEmpty() ))&&
                            (cm_ft ? (mHeightCm > 0):(heightFt <= 20 && heightFt > 0)) &&
                            (cm_ft ? String.valueOf(mHeightCm).isEmpty():(heightIn <= 13 && heightIn > 0))



                            ) {



                        if(cm_ft){

                            String  convert = String.valueOf(round(Double.valueOf(mHeightCM.getText().toString()) / 30.48, 1));
                            String ft =  convert.substring(0, convert.indexOf("."));
                            String in = convert.substring('.');
                            heightFt = Integer.valueOf(ft);
                            heightIn = Double.valueOf(in);


                        }
                        btnSignIn.setProgress(75);
                        Intent finalProcedure = new Intent(RegisterActivity.this, RegisterLastStep.class);
                        finalProcedure.putExtra("Name", name);
                        finalProcedure.putExtra("Email", email);
                        finalProcedure.putExtra("Gender", gender);
                        finalProcedure.putExtra("Weight", weight);
                        finalProcedure.putExtra("Age", age);
                        finalProcedure.putExtra("HeightFt", heightFt);
                        finalProcedure.putExtra("HeightIn", heightIn);
                        finalProcedure.putExtra("WeightUnit", weightUnit);
                        btnSignIn.setProgress(100);
                        startActivity(finalProcedure);


                    }
                    else {

                        btnSignIn.setProgress(-1);
                        Toast.makeText(RegisterActivity.this, "Please check fields carefully", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    btnSignIn.setProgress(-1);
                    Toast.makeText(RegisterActivity.this, "Please check the fields", Toast.LENGTH_SHORT).show();
                }



            }
        });
        findViewById(R.id.relativeLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);


                return true;
            }
        });


//        mRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!mName.getText().toString().isEmpty() &&
//                        !mEmail.getText().toString().isEmpty() &&
//                        !mWeight.getText().toString().isEmpty() &&
//                        !mAge.getText().toString().isEmpty() &&
//                        !mHeightFt.getText().toString().isEmpty() &&
//                        !mHeightIn.getText().toString().isEmpty()
//
//
//                        ){
//                    String name = mName.getText().toString();
//                    String email = mEmail.getText().toString();
//                    String gender = genders[mGender.getSelectedItemPosition()];
//                    int weight = Integer.valueOf(mWeight.getText().toString());
//                    int age = Integer.valueOf(mAge.getText().toString());
//                    int heightFt = Integer.valueOf(mHeightFt.getText().toString());
//                    double heightIn = Double.valueOf(mHeightIn.getText().toString());
//                    String weightUnit = weightUnits[mWeightUnit.getSelectedItemPosition()];
//
//
//                    if(!name.isEmpty() && email.contains("@") && email.contains(".com") &&
//                            !String.valueOf(weight).isEmpty() &&
//                            !String.valueOf(age).isEmpty() &&
//                            !String.valueOf(heightFt).isEmpty() &&
//                            !String.valueOf(heightIn).isEmpty() &&
//                            (heightFt <= 20) &&
//                            (heightIn <= 13)
//
//                            ) {
//                        Intent finalProcedure = new Intent(RegisterActivity.this, RegisterLastStep.class);
//                        finalProcedure.putExtra("Name", name);
//                        finalProcedure.putExtra("Email", email);
//                        finalProcedure.putExtra("Gender", gender);
//                        finalProcedure.putExtra("Weight", weight);
//                        finalProcedure.putExtra("Age", age);
//                        finalProcedure.putExtra("HeightFt", heightFt);
//                        finalProcedure.putExtra("HeightIn", heightIn);
//                        finalProcedure.putExtra("WeightUnit", weightUnit);
//                        startActivity(finalProcedure);
//
//                        Toast.makeText(RegisterActivity.this, "info saved", Toast.LENGTH_SHORT).show();
//                    }}
//                else {
//
//                    Toast.makeText(RegisterActivity.this, "Please check the fields", Toast.LENGTH_SHORT).show();
//                }
//
//
//
//
//            }
//        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        btnSignIn.setProgress(0);
    }
    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}
