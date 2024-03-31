package com.example.researchradar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.geeksforgeeks.databinding.ItemsRowBinding;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.example.researchradar.DetailsActivity;
import com.example.researchradar.Papers;
import com.example.researchradar.R;

import java.util.ArrayList;
import java.util.List;

//public class PaperAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
public class PaperAdapter extends RecyclerView.Adapter<PaperAdapter.MyViewHolder> {
    //create a list to pass our Model class
    List<Papers> modelList;
    Context context;
    public PaperAdapter(List<Papers> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate our custom view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_paper,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //bind all custom views by its position
        //to get the positions we call our Model class
        final Papers model = modelList.get(position);
        holder.name.setText(model.getName());
        holder.tag.setText(model.getTag());
//        holder.imageView.setImageDrawable(context.getResources().getDrawable(model.getImage()));
        //click listener
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
//                intent.putExtra("image",model.getImage());
                intent.putExtra("name",model.getName());
                intent.putExtra("tag",model.getTag());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return modelList.size();
    }
    //all the custom view will be hold here or initialize here inside MyViewHolder
    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name, tag;
        RelativeLayout relativeLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
//            imageView = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            tag = itemView.findViewById(R.id.tag);
            relativeLayout = itemView.findViewById(R.id.item);
        }
    }
}