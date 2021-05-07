package com.example.literaryclubadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Log extends AppCompatActivity {

    private EditText ed1,ed2,ed3,ed4;
    private Button bt1,bt2;
    private TextView tv;
    FirebaseAuth mAuth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        ed1=findViewById(R.id.editText3);
        ed2=findViewById(R.id.editText4);
        ed3=findViewById(R.id.editText5);
        ed4=findViewById(R.id.editText6);
        bt1=findViewById(R.id.button);
        bt2=findViewById(R.id.button2);
        mAuth=FirebaseAuth.getInstance();
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=mFirebaseDatabase.getReference().child("Users");

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1=ed2.getText().toString().trim();
                String s2=ed3.getText().toString().trim();

                if(s1.equals(s2)){
                    String s3=ed1.getText().toString().trim();
                    mAuth.createUserWithEmailAndPassword(s3,s1).addOnCompleteListener(Log.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                user=mAuth.getCurrentUser();
                                String s4=user.getUid();
                                String s5=ed4.getText().toString().trim();
                                databaseReference=mFirebaseDatabase.getReference("Users/");
                                databaseReference.child(s4);
                                databaseReference=mFirebaseDatabase.getReference("Users/"+s4+"/");
                                databaseReference.child("UserName").setValue(s5);
                                SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("isUser", true);
                                startActivity(new Intent(getApplicationContext(),Bottom.class));
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Sign in not Successful",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Sign.class));
            }
        });

    }
}
