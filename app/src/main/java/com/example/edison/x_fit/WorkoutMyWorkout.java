package com.example.edison.x_fit;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WorkoutMyWorkout.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WorkoutMyWorkout#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkoutMyWorkout extends Fragment {
    private DatabaseReference databaseReference;
    private ArrayList<String> mWorkout = new ArrayList<>();
    private TextView showEmptyList, title, mUsername;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Button mAddWorkout;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    ConstraintLayout constraintLayout;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public WorkoutMyWorkout() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkoutMyWorkout.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkoutMyWorkout newInstance(String param1, String param2) {
        WorkoutMyWorkout fragment = new WorkoutMyWorkout();
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
        View rootView = inflater.inflate(R.layout.fragment_workout_my_workout, container, false);
        Toast.makeText(getContext(), "MY WORKOUTS IS ON", Toast.LENGTH_SHORT).show();

        recyclerView = rootView.findViewById(R.id.recycler_view);

        showEmptyList = rootView.findViewById(R.id.emptyWorkout);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAddWorkout = rootView.findViewById(R.id.button8);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        final String user = mUser.getUid();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new RecyclerViewAdapter(getContext(), mWorkout);
        recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));
        recyclerView.setAdapter(mAdapter);

        databaseReference.child("Users").child(user).child("Workouts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Object value = dataSnapshot.getKey();
                mWorkout.add(String.valueOf(value));
                int index = mWorkout.indexOf(value);
                if(mWorkout.isEmpty()){
                    showEmptyList.setVisibility(View.VISIBLE);
                }
                else {
                    showEmptyList.setVisibility(View.INVISIBLE);
                }
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getKey();
                int index = mWorkout.indexOf(value);
                mWorkout.set(index, value);
                // mAdapter.notifyItemChanged(index);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                String value = dataSnapshot.getKey();
//                int index = mWorkout.indexOf(value);
//                mWorkout.remove(index );
//                mAdapter.notifyDataSetChanged();
//                mAdapter.notifyItemRemoved(index);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        databaseReference.child("Users").child(user).child("Workouts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    showEmptyList.setVisibility(View.VISIBLE);
                }
                else{
                    showEmptyList.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        recyclerView.setOnScrollListener(onScrollListener);




        mAddWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "button clicked", Toast.LENGTH_SHORT).show();
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                String random = "1234567890ABCDEFGHIJKLMNOPQR";
                Random r = new Random();
                final String  charS = String.valueOf(random.charAt(r.nextInt(random.length())) + " SECT");
                Map workoutAdd = new HashMap();
                workoutAdd.put(charS, true);
                dbRef.child("Users").child(user).child("Workouts").updateChildren(workoutAdd).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            mAdapter.notifyDataSetChanged();
                            Toast.makeText(getContext(), charS, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        return  rootView;
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

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
