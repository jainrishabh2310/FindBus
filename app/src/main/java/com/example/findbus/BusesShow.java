package com.example.findbus;



import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.findbus.Adapters.busadapter;

import java.util.ArrayList;

public class BusesShow extends AppCompatActivity {
RecyclerView recyclerView;
busadapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buses_show);
        Intent intent=getIntent();
        ArrayList<String> list=intent.getStringArrayListExtra("routebus");
        recyclerView=findViewById(R.id.busrecview);


        LinearLayoutManager layoutManager = new LinearLayoutManager(BusesShow.this);
        recyclerView.setLayoutManager(layoutManager);
        adapter= new busadapter(this,list);
        recyclerView.setAdapter(adapter);



    }
}