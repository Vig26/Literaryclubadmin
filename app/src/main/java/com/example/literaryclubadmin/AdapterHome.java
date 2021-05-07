package com.example.literaryclubadmin;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdapterHome extends RecyclerView.Adapter<AdapterHome.ViewHolder> {

    public ArrayList<DataHome> Data;
    HashMap<Integer,ViewHolder> Card=new HashMap<>();
   // public Context context;
  //  public List<UploadTask> uploadTasks;
    Activity thisActivity;

    public AdapterHome(ArrayList<DataHome> data) {
        Data = data;
     //   this.context = context;
     //   this.uploadTasks = uploadTasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_card,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.T1.setText(Data.get(position).Ename);
        holder.Bt1.setText(Data.get(position).Like);
        holder.cardView.setOnClickListener(this.Data.get(position).act);
        Uri u=Uri.parse(this.Data.get(position).Eimage);
        Picasso.get().load(u).fit().into(holder.Img);
    }

    public ViewHolder getView(int pos){return Card.get(pos);}

    @Override
    public int getItemCount() {
        return Data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView T1;
        ImageView Img;
        Button Bt1;
        CardView cardView;

        public ViewHolder(@NonNull android.view.View itemView) {
            super(itemView);
             T1=itemView.findViewById(R.id.EventTitle);
             Img=itemView.findViewById(R.id.BackgroundImage);
             Bt1=itemView.findViewById(R.id.like);
             cardView=itemView.findViewById(R.id.home_card);
        }
    }
}
