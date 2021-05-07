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

public class Sign extends AppCompatActivity {
    private EditText ed1,ed2;
    private Button bt1,bt2;
    private TextView tv;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        ed1=findViewById(R.id.editText7);
        ed2=findViewById(R.id.editText8);
        bt1=findViewById(R.id.button3);
        bt2=findViewById(R.id.button4);
        tv=findViewById(R.id.textView2);
        mAuth=FirebaseAuth.getInstance();
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1=ed1.getText().toString().trim();
                String s2=ed2.getText().toString().trim();
                mAuth.signInWithEmailAndPassword(s1,s2).addOnCompleteListener(Sign.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("isUser", true);
                            editor.commit();
                            startActivity(new Intent(getApplicationContext(),Bottom.class));

                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Sign in not Successful",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Log.class));
            }
        });
    }
}
