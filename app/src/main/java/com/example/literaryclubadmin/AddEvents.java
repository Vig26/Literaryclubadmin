package com.example.literaryclubadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddEvents extends AppCompatActivity {

    private static final int Pick=1;

    private ProgressBar Pro;
    private Button b1,b2;
    private EditText ed1,ed2,ed3;
    private ImageView img1;
    private Uri mUri;

    private DatabaseReference mData;
    private FirebaseDatabase mfDatabase;
    private FirebaseStorage mfStorage;
    private StorageReference storageReferencee;
    String s1,s2,s3,s4;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_events);

        Pro=findViewById(R.id.progress_bar);
        b1=findViewById(R.id.AddButEvent);
        b2=findViewById(R.id.SubmitEvent);
        ed1=findViewById(R.id.EventName);
        ed2=findViewById(R.id.EventDesc);
        ed3=findViewById(R.id.GoogleForms);
        img1=findViewById(R.id.testImage);

        mfDatabase=FirebaseDatabase.getInstance();
        mData=mfDatabase.getReference().child("Events");

        mfStorage=FirebaseStorage.getInstance();
        storageReferencee=mfStorage.getReference().child("Events");

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if((!ed1.getText().toString().trim().equals("")) && (!ed2.getText().toString().trim().equals("")) &&(!ed3.getText().toString().trim().equals("")) && mUri!=null){
                    s1=getFileExtension(mUri);
                    s2=ed1.getText().toString();
                    s3=ed2.getText().toString();
                    s4=ed3.getText().toString();
                    mData=mfDatabase.getReference("Events").child(s2);
                    storageReferencee=mfStorage.getReference("Events").child(s2);
                    storageReferencee.child(System.currentTimeMillis()+"."+s1);
                    UploadTask uploadTask= (UploadTask) storageReferencee
                            .putFile(mUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Pro.setProgress(0);
                                        }
                                    },1000);
                                    Toast.makeText(getApplicationContext(),"Upload Successful",Toast.LENGTH_SHORT).show();

                                    mData=mfDatabase.getReference("Events/"+s2+"/");
                                    mData.child("Description").setValue(s3);
                                    mData.child("Event").setValue(s2);
                                    mData.child("GoogleForm").setValue(s4);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),e.getMessage()+"***Failed",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double Progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                                    Pro.setProgress((int)Progress);
                                }
                            });
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return storageReferencee.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                mData=mfDatabase.getReference("Events/"+s2+"/");
                                String murl=downloadUri.toString();
                                mData.child("ImageUrl").setValue(murl);
                            } else {
                                // Handle failures
                                // ...
                            }

                            Fragment fragment = new DashboardFragment();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(),"All Fields are Required",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void openFileChooser(){
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,Pick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Pick && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            mUri=data.getData();
            Picasso.get().load(mUri).fit().into(img1);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}
