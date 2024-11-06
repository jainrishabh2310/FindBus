package com.example.findbus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.findbus.Adapters.bookingadapter;
import com.example.findbus.Adapters.busadapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.example.findbus.Adapters.busadapter;

public class Booking_History extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<String> list;
    bookingadapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);
        recyclerView=findViewById(R.id.busbooking);
        String user= FirebaseAuth.getInstance().getCurrentUser().getUid();
        list= new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(Booking_History.this);
        recyclerView.setLayoutManager(layoutManager);

        adapter= new bookingadapter(Booking_History.this,list);
        recyclerView.setAdapter(adapter);

        DatabaseReference reference= FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(user)
                .child("booking");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    list.clear();
                    for(DataSnapshot bussnap : snapshot.getChildren()) {
                        list.add(bussnap.getKey());
                        System.out.println(list);


                    }
                    Collections.reverse(list);
                    adapter.notifyDataSetChanged();













                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        System.out.println(list);


    }
}