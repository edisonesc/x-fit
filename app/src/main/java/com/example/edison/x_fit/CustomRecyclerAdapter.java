package com.example.edison.x_fit;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.vstechlab.easyfonts.EasyFonts;

import org.w3c.dom.Text;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CustomRecyclerAdapter extends RecyclerSwipeAdapter<CustomRecyclerAdapter.MyViewHolder> {
    LayoutInflater inflater;
    Context myContext;
    FirebaseStorage firebaseStorage;
    ArrayList<String> mDataset, mKeys;
    String finalResult;
    String coverImageNameForDel;
    private  long mClicks = 0;
    public CustomRecyclerAdapter(Context context, ArrayList<String> mData, ArrayList<String> keys) {
        this.myContext = context;
        this.mDataset = mData;
        this.mKeys = keys;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public CustomRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        //MyViewHolder was changed to ^ inorder to use class MyViewHolder of the Class CustomRecycler

        View view = inflater.inflate(R.layout.grid_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final String data = mDataset.get(position);

        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        holder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {

            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {

            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });


        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mClicks < 3000) {
                    return;
                }
                mClicks = SystemClock.elapsedRealtime();

                Intent view = new Intent(myContext, SingleImageDisplayActivity.class);
                view.putExtra("ImageLink", mDataset.get(position));
                view.putExtra("Info", mKeys.get(position));
                myContext.startActivity(view);
                Toast.makeText(myContext, "touched", Toast.LENGTH_SHORT).show();

            }
        });




        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser mUser = mAuth.getCurrentUser();
                String uid = mUser.getUid();
                final String selectedForDel = mKeys.get(position);

                final String month = selectedForDel.substring(0, 3); // jun
//                int day = Integer.valueOf(selectedForDel.substring(4, 6)); // 01
//                int year = Integer.valueOf(selectedForDel.substring(8, 11)); //2034
//                int seconds;
//                try{
//                seconds = Integer.valueOf(selectedForDel.substring(12,14));}
//                catch (StringIndexOutOfBoundsException e){
//                    seconds = Integer.valueOf(selectedForDel.substring(12,13));
//
//
//                }
                Date date = null;
                try {
                    date = new SimpleDateFormat("MMM").parse(month);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                firebaseStorage = FirebaseStorage.getInstance();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                final int monthNum = calendar.get(Calendar.MONTH);
                final String monthNumConv = String.valueOf(monthNum).length() == 1 ? "0" + monthNum : String.valueOf( monthNum);

                finalResult = selectedForDel.replace(getMonth(monthNum).substring(0,3), monthNumConv);

                databaseReference.child("Users").child(uid).child("Social").child("Images").child(finalResult).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String url = dataSnapshot.getValue(String.class);
                        try {
                        StorageReference storageReference = firebaseStorage.getReferenceFromUrl(url);
                            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(myContext, "Totally deleted " + finalResult, Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(myContext, "Failed deletion of " + finalResult, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        catch (IllegalArgumentException e){
                            Toast.makeText(myContext, "Data does not exist " + finalResult + " " + url, Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                databaseReference.child("Users").child(uid).child("Social").child("Images").child(finalResult).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mItemManger.removeShownLayouts(holder.swipeLayout);
                        mDataset.remove(position);
                        mKeys.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mDataset.size());
                        Toast.makeText(myContext, String.valueOf(
                                selectedForDel.replace(getMonth(monthNum).substring(0,3), monthNumConv) +

                                        getMonth(monthNum).substring(0,3) + ' ' +selectedForDel.substring(0,3)

                        ), Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(myContext, "Problem deleting", Toast.LENGTH_SHORT).show();

                    }
                });



            }
        });



        mItemManger.bindView(holder.itemView,position);
        holder.position.setText((position + 1) + ".");

        Glide.clear(holder.image);
        if(!mDataset.isEmpty()){
                Glide.with(myContext).load(mDataset.get(position)).centerCrop().into(holder.image);
                    holder.titleDate.setText(mKeys.get(position));
        }



    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipeGrid;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        SwipeLayout swipeLayout;
        Button delete;
        TextView position, titleDate;
        ImageView image;
        CardView card;
        public MyViewHolder(View itemView) {
            super(itemView);
            swipeLayout =  itemView.findViewById(R.id.swipeGrid);
            delete = itemView.findViewById(R.id.trash);
            card = itemView.findViewById(R.id.cardClick);
            image = itemView.findViewById(R.id.grid_item_image);
            position = itemView.findViewById(R.id.position);
            titleDate = itemView.findViewById(R.id.title);
            titleDate.setTypeface(EasyFonts.walkwayBlack(myContext));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(getClass().getSimpleName(), "onItemSelected: " + position.getText().toString());
                    Toast.makeText(view.getContext(), "onItemSelected: " + position.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }
}
