package com.example.findbus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.findbus.ModelClasses.BusData;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;

    Spinner PlaceFrom;
    Spinner PlaceTo;
    AppCompatButton findbus;

    String Route;

   ArrayList<String> list;

    HashSet<String> routeto;
    CircleImageView exchange_btn;
    ArrayAdapter<String> from;
    ArrayAdapter<String> to;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView=findViewById(R.id.NavigationView);
        drawerLayout=findViewById(R.id.drawerlayout);


         HashSet<String> routefrom = new HashSet<>(); // Declaring as an array of size one



        toolbar=findViewById(R.id.toolbar);
        routeto= new HashSet<>();
        PlaceTo=findViewById(R.id.spinner_routeto);
        PlaceFrom=findViewById(R.id.spinner_routefrom);

        findbus=findViewById(R.id.findbus);
        list= new ArrayList<>();
        ProgressBar progressBar = findViewById(R.id.progressbar);




//        DatabaseReference db= FirebaseDatabase.getInstance().getReference("Buses");
//        db.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    for(DataSnapshot bussnap : snapshot.getChildren()){
//
//                        String key=bussnap.getKey();
//                        System.out.println(key);
//                        FirebaseDatabase.getInstance().getReference("Buses").child(key)
//                                .addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        System.out.println(snapshot.getChildren());
//                                        System.out.println(snapshot.getValue());
//                                        BusData busData= snapshot.getValue(BusData.class);
//                                        if(busData!=null){
//                                            System.out.println(busData.getFrom());
//                                            routefrom.add(busData.getFrom().toUpperCase());
//                                           from = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item,new ArrayList<>(routefrom));
//                                            from.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                                            PlaceFrom.setAdapter(from);
//                                        }
//
//
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });
//
//
//
//
//
////                                    if (bus != null && bus.getRoute().equalsIgnoreCase(Route)) {
////                                        list.add(bus);
////                                    }
//                    }
//
//
//
//
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//
//        });
//
//
//
        DatabaseReference db= FirebaseDatabase.getInstance().getReference("Buses");
//        todb.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                routeto.clear();
//                if(snapshot.exists()){
//                    for(DataSnapshot bussnap : snapshot.getChildren()){
//
//                        String key=bussnap.getKey();
//                        FirebaseDatabase.getInstance().getReference("Buses").child(key)
//                                .addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        BusData busData= snapshot.getValue(BusData.class);
//
//                                        if(busData!=null){
//                                            routeto.add(busData.getTo().toUpperCase());
//                                           to= new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item,new ArrayList<>(routeto));
//                                            to.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                                            PlaceTo.setAdapter(to);
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });
//
//
//
//
//
////                                    if (bus != null && bus.getRoute().equalsIgnoreCase(Route)) {
////                                        list.add(bus);
////                                    }
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });





        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    routefrom.clear(); // Clear previous data to avoid duplicates
                    routeto.clear();

                    for (DataSnapshot bussnap : snapshot.getChildren()) {
                        BusData busData = bussnap.getValue(BusData.class);
                        if (busData != null) {
                            routefrom.add(busData.getFrom().toUpperCase());
                            routeto.add(busData.getTo().toUpperCase());
                        }
                    }

                    // Update the adapters with the new data
                    from = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, new ArrayList<>(routefrom));
                    from.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    PlaceFrom.setAdapter(from);

                    to = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, new ArrayList<>(routeto));
                    to.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    PlaceTo.setAdapter(to);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Database error: " + error.getMessage());
            }
        });






        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.headerbkg); // Set your custom icon here
        }

        ActionBarDrawerToggle ad=new ActionBarDrawerToggle(MainActivity.this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(ad);
        ad.syncState();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);






        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemid=item.getItemId();

                if(itemid==R.id.nav_item_booking){


                    Intent intent= new Intent(MainActivity.this, Booking_History.class);
                    startActivity(intent);
                    return true;

                }

                return true;

            }
        });

        findbus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                FirebaseDatabase.getInstance().getReference("Buses")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                list.clear();
                                Route=PlaceFrom.getSelectedItem().toString().toLowerCase().trim()
                                        +"to"+PlaceTo.getSelectedItem().toString().toLowerCase().trim();


                                for(DataSnapshot bussnap : snapshot.getChildren()){

                                    String key=bussnap.getKey();
                                    FirebaseDatabase.getInstance().getReference("Buses").child(key)
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            BusData busData= snapshot.getValue(BusData.class);

                                                            if (busData != null && busData.getBusroute().equals(Route)) {
                                                                    list.add(key);



                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });







//                                    if (bus != null && bus.getRoute().equalsIgnoreCase(Route)) {
//                                        list.add(bus);
//                                    }
                                }





                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                if(list.size()==0){

                    Toast.makeText(MainActivity.this, "There are no bus for this Route", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                }

                         else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);

                            Intent intent = new Intent(MainActivity.this, BusesShow.class);
                            intent.putStringArrayListExtra("routebus", list);
                            startActivity(intent);
                        }
                    }, 2000);
                }


            }
        });



    }
    private int getSpinnerIndex(Spinner spinner, String item) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(item)) {
                return i;
            }
        }
        return 0; // Default to the first item if not found
    }
}