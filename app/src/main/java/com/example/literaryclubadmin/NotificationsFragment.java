package com.example.literaryclubadmin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    private FloatingActionButton fab,Close;

    FirebaseAuth mAuth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;

    private ArrayList<Task<byte[]>> TaskArray=new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private ArrayList<DataGallery> Data2=new ArrayList<DataGallery>();
    private View.OnClickListener onClickList;

    private TextView tv;
    private ImageView Iview2;
    PhotoViewAttacher photoViewAttacher;
    GifImageView Gif;

    private ConstraintLayout Con;
    int i;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);


        notificationsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }

        });

        tv=root.findViewById(R.id.GallName);
        Iview2=root.findViewById(R.id.img3);
        Close=root.findViewById(R.id.close2);
        Con=root.findViewById(R.id.notify_constrain2);
        recyclerView = root.findViewById(R.id.RecView2);
        fab=root.findViewById(R.id.addGallery);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AddGallery.class));
            }
        });

        mAuth=FirebaseAuth.getInstance();

        mFirebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=mFirebaseDatabase.getReference("Gallery");
        user=mAuth.getCurrentUser();

        onClickList=new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                for (int i = 0; i < Data2.size(); i++) {
                    fab.setVisibility(View.INVISIBLE);
                    if (((TextView) v.findViewById(R.id.GallTitle)).getText().toString().equals(Data2.get(i).Ecom)) {
                        tv.setText(Data2.get(i).Ecom);
                        Uri z = Uri.parse(Data2.get(i).Eimage);
                        Picasso.get().load(z).into(Iview2);


                    }

                }
                Con.setVisibility(View.VISIBLE);
            }
        };

        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Con.setVisibility(View.INVISIBLE);

            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));

        adapter=new AdapterGallery(Data2);
        recyclerView.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                i = 0;
                Data2.clear();
                TaskArray.clear();


                for(DataSnapshot Child:dataSnapshot.getChildren()){
                    try {
                        if((Child != null) && Child.child("Comments").getValue() != null &&  Child.child("ImageUrl").getValue()!=null ){
                            Data2.add(new DataGallery(Child.child("Comments").getValue().toString(),Child.child("ImageUrl").getValue().toString(),onClickList));

                        }
                    }
                    catch (Exception e){
                        Toast.makeText(getContext(),"gatCaught",Toast.LENGTH_SHORT).show();
                    }
                }
                adapter= new AdapterGallery(Data2);
                recyclerView.setAdapter(adapter);
                for (DataSnapshot Child :dataSnapshot.getChildren()){
                    try {
                        if ((Child != null) && Child.child("Comments").getValue() != null && Child.child("ImageUrl").getValue()!=null ) {
                            for (int i = 0; i < TaskArray.toArray().length; i++) {
                                if (TaskArray.get(i).isSuccessful()) {
                                    Data2.set(i, new DataGallery(Data2.get(i).Ecom, Data2.get(i).Eimage,onClickList));

                                }
                            }
                            adapter = new AdapterGallery(Data2);
                            recyclerView.setAdapter(adapter);
                            i++;
                        }



                    }
                    catch (Exception e){
                        Toast.makeText( getContext() , "TaskCaught", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return root;
    }
}