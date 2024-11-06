package com.example.findbus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OTP extends AppCompatActivity {

    TextInputEditText enterotp;

    AppCompatButton verify;
    TextView numbershow;
    FirebaseAuth mAuth;
    String number;
    String otpid;
    ProgressBar progressbaroffillotp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        enterotp = findViewById(R.id.enterotp);
        verify=findViewById(R.id.verifyotp);
        numbershow=findViewById(R.id.numbershow);
        number = getIntent().getStringExtra("number");
        mAuth = FirebaseAuth.getInstance();
        otpid=getIntent().getStringExtra("otp");
        numbershow.setText(number.toString());
        progressbaroffillotp= findViewById(R.id.progressbaroffillotp);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(enterotp.getText().toString().length()!=6) {
                    Toast.makeText(OTP.this, "invalid OTP", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressbaroffillotp.setVisibility(View.VISIBLE);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid, enterotp.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });








    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressbaroffillotp.setVisibility(View.INVISIBLE);
                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users")
                            .child(mAuth.getCurrentUser().getUid()).child("name");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                startActivity(new Intent(OTP.this,MainActivity.class));



                            }
                            else{
                              Intent intent=  new Intent(OTP.this,Registration.class);
                                Toast.makeText(OTP.this, "OTP Verification Success", Toast.LENGTH_SHORT).show();

                                intent.putExtra("uid", mAuth.getUid());
                                intent.putExtra("phone", mAuth.getCurrentUser().getPhoneNumber());
                                startActivity(intent);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });




                }
                else{
                    Toast.makeText(OTP.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }
}