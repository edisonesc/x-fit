package com.example.edison.x_fit;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NutritionData.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NutritionData#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NutritionData extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView bmi, bmr, calorieNeeds, idealWeight;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NutritionData() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NutritionData.
     */
    // TODO: Rename and change types and number of parameters
    public static NutritionData newInstance(String param1, String param2) {
        NutritionData fragment = new NutritionData();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nutrition_data, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();
//        final int age, heighFt, heightIn;
//        double weight;
//        String gender, weightUnit;

            bmi = view.findViewById(R.id.bmiValue);
            bmr = view.findViewById(R.id.bmrValue);
            calorieNeeds = view.findViewById(R.id.calorieNeedsValue);
            idealWeight = view.findViewById(R.id.idealWeightValue);

        databaseReference.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int ageVal = dataSnapshot.child("Age").getValue(Integer.class);
                int heightFt = dataSnapshot.child("Height Ft").getValue(Integer.class);
                int heightIn = dataSnapshot.child("Height In").getValue(Integer.class);
                double weight = dataSnapshot.child("Weight").getValue(Double.class);
                String gender = dataSnapshot.child("Gender").getValue(String.class);
                String weightUnit = dataSnapshot.child("WeightUnit").getValue(String.class);
                double height = Double.parseDouble(String.valueOf(heightFt) + "." + String.valueOf(heightIn));
                Double formulaBmr = 0d, formulaBmi = 0d;
                int idealWeight = 0;

                switch (gender){
                    case "Male":
                        formulaBmr = 66 + (13.75 * (weightUnit.equals("Kg") ? weight : weight * 2.2)) +
                                (5 * (height * 30.48)) - (4.7 * ageVal);


                        break;
                    case "Female":
                        formulaBmr = 655 + (9.6 * (weightUnit.equals("Kg") ? weight : weight * 2.2)) +
                                (1.8 * (height * 30.48)) - (4.7 * ageVal);
                        break;
                    case "Other":
                        break;
                }

                formulaBmi = (weightUnit.equals("Kg") ? weight : weight * 2.2) / height * 0.304;

                bmi.setText(String.valueOf(formulaBmi));
                bmr.setText(String.valueOf(formulaBmr));






            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        bmi = view.findViewById(R.id.bmiValue);
        bmr = view.findViewById(R.id.bmrValue);
        calorieNeeds = view.findViewById(R.id.calorieNeedsValue);
        idealWeight = view.findViewById(R.id.idealWeightValue);





        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
