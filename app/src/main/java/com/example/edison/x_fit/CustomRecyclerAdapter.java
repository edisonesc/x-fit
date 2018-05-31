package com.example.edison.x_fit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomRecyclerAdapter extends RecyclerSwipeAdapter<CustomRecyclerAdapter.MyViewHolder> {
    LayoutInflater inflater;
    Context myContext;
    ArrayList<String> mDataset;
    public CustomRecyclerAdapter(Context context, ArrayList<String> mData) {
        this.myContext = context;
        this.mDataset = mData;
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

        holder.position.setText((position + 1) + ".");

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
        TextView position;

        public MyViewHolder(View itemView) {
            super(itemView);
            swipeLayout =  itemView.findViewById(R.id.swipeGrid);
            delete = itemView.findViewById(R.id.trash);
            position = itemView.findViewById(R.id.position);

        }
    }
}
