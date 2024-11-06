package com.example.findbus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.findbus.ModelClasses.UserData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class Registration extends AppCompatActivity {
    CircleImageView image;
    TextInputEditText name;
    TextInputEditText gender;
    TextInputEditText dob;
    TextInputEditText address;
    AppCompatButton complete;
    Uri uri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    String uid;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        image=findViewById(R.id.profile_pic);
        name=findViewById(R.id.username);
        gender=findViewById(R.id.gender);
        dob=findViewById(R.id.dob);
        address=findViewById(R.id.address);
        uid=getIntent().getStringExtra("uid");

        complete=findViewById(R.id.complete);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Select Image"),1);
//

            }
        });
            complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    storageReference=FirebaseStorage.getInstance().getReference().child("users/"+"image1"+new Random().nextInt(100)+".jpg");
                    storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    databaseReference=FirebaseDatabase.getInstance().getReference("Users");

                                    String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    String Username=name.getText().toString().trim();
                                    String Gender=gender.getText().toString().trim();
                                    String Dob=dob.getText().toString().trim();
                                    String Address=address.getText().toString().trim();
                                    UserData userData= new UserData(Username,uri.toString(),Gender,Dob,Address);

                                    if(Username.isEmpty() || Gender.isEmpty() || Dob.isEmpty() || Address.isEmpty()){
                                        Toast.makeText(getApplicationContext(),"Please fill all Details",Toast.LENGTH_LONG).show();
                                    }
                                    else {

                                        databaseReference.child(uid).setValue(userData);
                                        startActivity(new Intent(Registration.this, MainActivity.class));
                                        finish();
                                    }


                                }
                            });

                        }
                    })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                                }
                            })

    ;



                }
            });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK){
            uri=data.getData();
            try {
                Glide.with(getApplicationContext()).load(uri).into(image);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}