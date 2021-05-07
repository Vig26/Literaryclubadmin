package com.example.literaryclubadmin;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterEvents extends RecyclerView.Adapter<AdapterEvents.ViewHolder> {

    public ArrayList<DataEvents> Data1;
    HashMap<Integer,ViewHolder> Card=new HashMap<>();
    // public Context context;
    //  public List<UploadTask> uploadTasks;
    Activity thisActivity;

    public AdapterEvents(ArrayList<DataEvents> data) {
        Data1 = data;
        //   this.context = context;
        //   this.uploadTasks = uploadTasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_card,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.T1.setText(Data1.get(position).Ename);
        holder.cardView.setOnClickListener(this.Data1.get(position).act);
        Uri u=Uri.parse(this.Data1.get(position).Eimage);
        Picasso.get().load(u).fit().into(holder.Img);
    }

    public ViewHolder getView(int pos){return Card.get(pos);}

    @Override
    public int getItemCount() {
        return Data1.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView T1;
        ImageView Img;
        CardView cardView;

        public ViewHolder(@NonNull android.view.View itemView) {
            super(itemView);
            T1=itemView.findViewById(R.id.EventTitle);
            Img=itemView.findViewById(R.id.BackgroundImage);
            cardView=itemView.findViewById(R.id.dashboard_card);
        }
    }
}
