package com.example.literaryclubadmin;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    private FloatingActionButton fab,Close;

    FirebaseAuth mAuth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;

    private ArrayList<Task<byte[]>> TaskArray=new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private ArrayList<DataEvents> Data1=new ArrayList<DataEvents>();
    private View.OnClickListener onClickList;

    private GifImageView Gif;
    private TextView tv,tvs,tvs2;
    private ImageView Iview2;

    private ConstraintLayout Con;
    int i;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        tv=root.findViewById(R.id.Google);
        tvs=root.findViewById(R.id.EventsName);
        tvs2=root.findViewById(R.id.EventsDesc);
        Iview2=root.findViewById(R.id.EventsImage);
        Close=root.findViewById(R.id.close1);
        Con=root.findViewById(R.id.dashboard_constrain2);


        recyclerView = root.findViewById(R.id.RecView1);
        fab=root.findViewById(R.id.addEvents);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AddEvents.class));
            }
        });

        mAuth=FirebaseAuth.getInstance();

        mFirebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=mFirebaseDatabase.getReference("Events");
        user=mAuth.getCurrentUser();

        onClickList=new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                for (int i = 0; i < Data1.size(); i++) {
                    fab.setVisibility(View.INVISIBLE);
                    if (((TextView) v.findViewById(R.id.EventTitle)).getText().toString().equals(Data1.get(i).Ename)) {
                        tv.setText(Data1.get(i).Gform);
                        tvs.setText(Data1.get(i).Ename);
                        tvs2.setText(Data1.get(i).Edesc);
                        Uri z = Uri.parse(Data1.get(i).Eimage);
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter=new AdapterEvents(Data1);
        recyclerView.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                i = 0;
                Data1.clear();
                TaskArray.clear();


                for(DataSnapshot Child:dataSnapshot.getChildren()){
                    try {
                        if((Child != null) && Child.child("Event").getValue() != null && Child.child("Description").getValue() != null && Child.child("ImageUrl").getValue()!=null && Child.child("GoogleForm").getValue()!=null){
                            Data1.add(new DataEvents(Child.child("Event").getValue().toString(),Child.child("Description").getValue().toString(),Child.child("ImageUrl").getValue().toString(),Child.child("GoogleForm").getValue().toString(),onClickList));

                        }
                    }
                    catch (Exception e){
                        Toast.makeText(getContext(),"gatCaught",Toast.LENGTH_SHORT).show();
                    }
                }
                adapter= new AdapterEvents(Data1);
                recyclerView.setAdapter(adapter);
                for (DataSnapshot Child :dataSnapshot.getChildren()){
                    try {
                        if ((Child != null) && Child.child("Event").getValue() != null && Child.child("Description").getValue() != null && Child.child("ImageUrl").getValue()!=null && Child.child("GoogleForm").getValue()!=null) {
                            for (int i = 0; i < TaskArray.toArray().length; i++) {
                                if (TaskArray.get(i).isSuccessful()) {
                                    Data1.set(i, new DataEvents(Data1.get(i).Ename, Data1.get(i).Edesc,Data1.get(i).Eimage,Data1.get(i).Gform,onClickList));

                                }
                            }
                            adapter = new AdapterEvents(Data1);
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