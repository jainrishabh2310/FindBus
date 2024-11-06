package com.example.findbus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class Number extends AppCompatActivity {
    TextInputEditText number;
    String phonenumber;
    AppCompatButton sendotp;
    private FirebaseAuth firebaseAuth;
    private String verificationId;
    CountryCodePicker ccp;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;
    PhoneAuthProvider.ForceResendingToken forceResendingToke;
    ProgressBar progressBar;
    String codeSent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);
        number = findViewById(R.id.phonenumber);
        sendotp = findViewById(R.id.sendotpbtn);
        ccp = findViewById(R.id.countryCodePicker);
        ccp.registerCarrierNumberEditText(number);
        progressBar=findViewById(R.id.progressbarofmain);

        firebaseAuth = FirebaseAuth.getInstance();


        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = number.getText().toString().trim();
                if (phoneNumber.isEmpty()) {
                    Toast.makeText(Number.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                } else if (phoneNumber.replace(" ", "").length() != 10) {
                    Toast.makeText(getApplicationContext(), "Please Enter Correct Number", Toast.LENGTH_SHORT).show();

                } else {
                    progressBar.setVisibility(View.VISIBLE);

                    phonenumber = ccp.getFullNumberWithPlus().replace(" ", "");
                    PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(firebaseAuth)
                            .setPhoneNumber(phonenumber)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(Number.this)
                            .setCallbacks(mcallbacks);

                    PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(forceResendingToke).build());


                }
            }
        });
//        firebaseAuth.setLanguageCode("in");


        mcallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                // Automatically Fetch OTP CODE HERE


            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                progressBar.setVisibility(View.INVISIBLE);

                Toast.makeText(getApplicationContext(), "OTP Sent Successfully.", Toast.LENGTH_SHORT).show();
                codeSent = s;
                forceResendingToke=forceResendingToken;
                Intent intent = new Intent(Number.this, OTP.class);
                intent.putExtra("number",phonenumber);
                intent.putExtra("otp", codeSent);
                startActivity(intent);
            }
        };

    }
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            DatabaseReference getDataRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("name");
            getDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Intent intent = new Intent(Number.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

}