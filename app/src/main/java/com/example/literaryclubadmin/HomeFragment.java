package com.example.literaryclubadmin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
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

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private FloatingActionButton fab,Close,signOut;

    FirebaseAuth mAuth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;

    private ArrayList<Task<byte[]>> TaskArray=new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private ArrayList<DataHome> Data=new ArrayList<DataHome>();
    private View.OnClickListener onClickList;

    private TextView tv,tvs,tvs2;
    private ImageView Iview,Iview2;

    private ConstraintLayout Con;

    Uri s2;
    String s1;

    int i;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        tv=root.findViewById(R.id.User);
        Iview =root.findViewById(R.id.imageView2);
        tvs=root.findViewById(R.id.HomeName);
        tvs2=root.findViewById(R.id.HomeDesc);
        Iview2=root.findViewById(R.id.HomeImage);
        Close=root.findViewById(R.id.close);
        Con=root.findViewById(R.id.home_constrain2);
        signOut=root.findViewById(R.id.Signout);

        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("isUser");
                editor.commit();
                Intent intent = new Intent(getActivity(), Sign.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        recyclerView = root.findViewById(R.id.RecView);
        fab=root.findViewById(R.id.addHome);

        mAuth=FirebaseAuth.getInstance();

        mFirebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=mFirebaseDatabase.getReference("Home");
        user=mAuth.getCurrentUser();

        onClickList=new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                for (int i = 0; i < Data.size(); i++) {
                    fab.setVisibility(View.INVISIBLE);
                    if (((TextView) v.findViewById(R.id.EventTitle)).getText().toString().equals(Data.get(i).Ename)) {
                        tvs.setText( Data.get(i).Ename);
                        tvs2.setText( Data.get(i).Edesc);
                        Uri z = Uri.parse(Data.get(i).Eimage);
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



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AddHome.class));
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter= new AdapterHome(Data);
        recyclerView.setAdapter(adapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                i=0;
                Data.clear();
                TaskArray.clear();

                for(DataSnapshot Child:dataSnapshot.getChildren()){
                    try {
                        if((Child != null) && Child.child("Event").getValue() != null && Child.child("Description").getValue() != null && Child.child("ImageUrl").getValue()!=null && Child.child("Like").getValue()!=null){
                            Data.add(new DataHome(Child.child("Event").getValue().toString(),Child.child("Description").getValue().toString(),Child.child("ImageUrl").getValue().toString(),Child.child("Like").getValue().toString(),onClickList));

                        }
                    }
                    catch (Exception e){
                        Toast.makeText(getContext(),"gatCaught",Toast.LENGTH_SHORT).show();
                    }
                }
                adapter= new AdapterHome(Data);
                recyclerView.setAdapter(adapter);
                for (DataSnapshot Child :dataSnapshot.getChildren()){
                    try {
                        if ((Child != null) && Child.child("Event").getValue() != null && Child.child("Description").getValue() != null && Child.child("ImageUrl").getValue()!=null && Child.child("Like").getValue()!=null) {
                            for (int i = 0; i < TaskArray.toArray().length; i++) {
                                if (TaskArray.get(i).isSuccessful()) {
                                    Data.set(i, new DataHome(Data.get(i).Ename, Data.get(i).Edesc,Data.get(i).Eimage,Data.get(i).Like,onClickList));

                                }
                            }
                            adapter = new AdapterHome(Data);
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