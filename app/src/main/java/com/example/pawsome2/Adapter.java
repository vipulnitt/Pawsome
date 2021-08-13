package com.example.pawsome2;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    LayoutInflater inflater;
    List<Data> data;
    Context context;
    public Adapter(Context ctx, List<Data> data){
        this.inflater =LayoutInflater.from(ctx);
        this.data =data;
        this.context=ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.custom_list_layout,parent,false);

        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //bind the data
        holder.name.setText("Name:"+data.get(position).getName());
       Picasso.get().load(data.get(position).getUrl()).into(holder.imageView);
      holder.view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
            Intent intent = new Intent(context,MainActivity2.class);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("id",data.get(position).getId());
            intent.putExtra("name",data.get(position).getName());
            intent.putExtra("temprament",data.get(position).getTemprament());
            intent.putExtra("url",data.get(position).getUrl());
            intent.putExtra("origin",data.get(position).getOrigin());
            intent.putExtra("Weight",data.get(position).getWeight());
            intent.putExtra("Height",data.get(position).getHeight());
            intent.putExtra("Bred_for",data.get(position).getBred_for());
            intent.putExtra("Image_id",data.get(position).getImage_id());
            context.startActivity(intent);
           }
       });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
       ImageView imageView;
       View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView);
         imageView = itemView.findViewById(R.id.imageView);
         this.view = itemView;
        }
    }
 }
