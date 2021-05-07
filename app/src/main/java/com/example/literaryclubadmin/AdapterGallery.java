package com.example.literaryclubadmin;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterGallery extends RecyclerView.Adapter<AdapterGallery.ViewHolder> {

    public ArrayList<DataGallery> Data2;
    HashMap<Integer,ViewHolder> Card=new HashMap<>();
    // public Context context;
    //  public List<UploadTask> uploadTasks;
    Activity thisActivity;

    public AdapterGallery(ArrayList<DataGallery> data) {
        Data2 = data;
        //   this.context = context;
        //   this.uploadTasks = uploadTasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_card,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.T1.setText(Data2.get(position).Ecom);
        holder.cardView.setOnClickListener(this.Data2.get(position).act);
        Uri u=Uri.parse(this.Data2.get(position).Eimage);
        Picasso.get().load(u).fit().into(holder.Img);
    }

    public ViewHolder getView(int pos){return Card.get(pos);}

    @Override
    public int getItemCount() {
        return Data2.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView T1;
        ImageView Img;
        CardView cardView;

        public ViewHolder(@NonNull android.view.View itemView) {
            super(itemView);
            T1=itemView.findViewById(R.id.GallTitle);
            Img=itemView.findViewById(R.id.BackgroundImage);
            cardView=itemView.findViewById(R.id.notify_card);
        }
    }
}