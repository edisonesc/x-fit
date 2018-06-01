package com.example.edison.x_fit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.vstechlab.easyfonts.EasyFonts;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomRecyclerAdapter extends RecyclerSwipeAdapter<CustomRecyclerAdapter.MyViewHolder> {
    LayoutInflater inflater;
    Context myContext;
    ArrayList<String> mDataset, mKeys;


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
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        String data = mDataset.get(position);

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
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemManger.removeShownLayouts(holder.swipeLayout);
                Toast.makeText(myContext, "delete", Toast.LENGTH_SHORT).show();
            }
        });
        mItemManger.bindView(holder.itemView,position);
        holder.position.setText((position + 1) + ".");
        holder.titleDate.setText(mKeys.get(position));
        Glide.clear(holder.image);
        if(!mDataset.isEmpty()){
                Glide.with(myContext).load(mDataset.get(position)).centerCrop().into(holder.image);
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

        public MyViewHolder(View itemView) {
            super(itemView);
            swipeLayout =  itemView.findViewById(R.id.swipeGrid);
            delete = itemView.findViewById(R.id.trash);
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
}
