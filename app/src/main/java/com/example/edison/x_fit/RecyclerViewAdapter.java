package com.example.edison.x_fit;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.view.View.Y;

public class RecyclerViewAdapter extends RecyclerSwipeAdapter<RecyclerViewAdapter.SimpleViewHolder> {


    public static class  SimpleViewHolder extends RecyclerView.ViewHolder{
        public SwipeLayout swipeLayout;
        private TextView textViewPos;
        private TextView textViewData;
        private Button buttonDelete;

        public View button;


        public SimpleViewHolder (View itemView){
            super(itemView);
            swipeLayout =  itemView.findViewById(R.id.swipe);
            textViewPos = itemView.findViewById(R.id.position);
            textViewData = itemView.findViewById(R.id.text_data);
            buttonDelete = itemView.findViewById(R.id.delete);
            button = itemView.findViewById(R.id.trash);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(getClass().getSimpleName(), "onItemSelected: " + textViewData.getText().toString());
                    Toast.makeText(view.getContext(), "onItemSelected: " + textViewData.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private Context mContext;
    private ArrayList<String> mDataset;
    public RecyclerViewAdapter(Context context, ArrayList<String> objects){
        this.mContext = context;
        this.mDataset = objects;
    }
    @Override
    public  SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new SimpleViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position){
        final String data = mDataset.get(position);
        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        mItemManger.bindView(viewHolder.itemView, position);
        viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {
                Toast.makeText(mContext, "ON START OPEN", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.

            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
                Toast.makeText(mContext, "Released", Toast.LENGTH_SHORT).show();

            }
        });

        viewHolder.swipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "clicked.", Toast.LENGTH_SHORT).show();

            }
        });
        viewHolder.swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser mUser = mAuth.getCurrentUser();
                String uid = mUser.getUid();
                String selectedForDel = mDataset.get(position);

                mItemManger.removeShownLayouts(viewHolder.swipeLayout);

                databaseReference.child("Users").child(uid).child("Workouts").child(selectedForDel).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mDataset.remove(position);
                            notifyItemRemoved(position);

                            notifyItemRangeChanged(position , mDataset.size());
//                            Toast.makeText(view.getContext(), "Deleted " + viewHolder.textViewData.getText().toString() + "!", Toast.LENGTH_SHORT).show();
                        }
                        else {
//                            Toast.makeText(view.getContext(), "Problem Deleting", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                mItemManger.closeAllItems();

            }
        });
        viewHolder.textViewPos.setText((position + 1) + ".");
        viewHolder.textViewData.setText(data);


    }
    @Override
    public int getItemCount(){
        return mDataset.size();
    }
    @Override
    public int getSwipeLayoutResourceId(int position){
        return R.id.swipe;
    }


}
