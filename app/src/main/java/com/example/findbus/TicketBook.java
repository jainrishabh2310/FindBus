package com.example.findbus;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findbus.ModelClasses.BusData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class TicketBook extends AppCompatActivity {
    GridLayout gridLayout;
    Long totalseat;
    private List<Integer> bookedSeats = new ArrayList<>();
    private List<Integer> onbook = new ArrayList<>();
    AppCompatButton bookingcomplete;
    ProgressBar progressBar;
    TextView busfromticket;
    TextView bustoticket;


    int no=3;
    int no1=2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_book);
        gridLayout=findViewById(R.id.gridLayoutSeats);
        String busid=getIntent().getStringExtra("Busid");
        progressBar=findViewById(R.id.ticketprogess);
        busfromticket=findViewById(R.id.routefromticket);
        bustoticket=findViewById(R.id.routetoticket);;

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Buses")
                .child(busid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    BusData busData= snapshot.getValue(BusData.class);
                    busfromticket.setText(busData.getFrom().toString());
                    bustoticket.setText(busData.getTo().toString());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Seats")
                .child(busid).child("bookedseat");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot child : snapshot.getChildren()){
                        Long seatValue = (Long) child.getValue();
                        System.out.println(seatValue);

                        // Convert Long to Integer
                        bookedSeats.add(seatValue != null ? seatValue.intValue() : 0);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        bookingcomplete= findViewById(R.id.confrim);

        DatabaseReference reference3= FirebaseDatabase.getInstance().getReference("Seats")
                .child(busid).child("busseat");
        reference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    totalseat= (Long) snapshot.getValue();
                    createSeatButtons(totalseat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        bookingcomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                HashMap<String, Object> map=new HashMap<>();
                bookedSeats.addAll(onbook);
                map.put("bookedseat",bookedSeats);
                FirebaseDatabase.getInstance().getReference("Seats")
                        .child(busid).updateChildren(map);

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
                String dateTime = dateFormat.format(new Date());
                String userid= FirebaseAuth.getInstance().getCurrentUser().getUid();


                DatabaseReference reference2=FirebaseDatabase.getInstance().getReference("Booking")
                        .child(busid).child(userid);
                String bookingid= reference2.push().getKey();


                Map<String, Object> bookingData = new HashMap<>();
                bookingData.put("date_time", dateTime);
                bookingData.put("bookedseat",onbook);
                reference2.child(bookingid).setValue(bookingData);

                bookingData.put("Busid",busid);

                FirebaseDatabase.getInstance().getReference("Users")
                        .child(userid).child("booking").child(bookingid).setValue(bookingData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(TicketBook.this,MainActivity.class));
                                Toast.makeText(TicketBook.this,"Ticket book Successfully",Toast.LENGTH_LONG).show();


                            }
                        });





            }
        });



    }

    private void createSeatButtons(Long totalSeats) {

        gridLayout.removeAllViews(); // Clear existing views

        int rows;
        int columns;

        // Determine the layout based on the total number of seats
        if (totalSeats == 40) {
            rows = 10;
            columns = 4;
            gridLayout.setRowCount(10);
            gridLayout.setColumnCount(4);
        } else if (totalSeats == 50) {
            rows = 10;
            columns = 5;
            gridLayout.setRowCount(10);
            gridLayout.setColumnCount(5);
        } else {
            // Handle other cases or provide a default layout
            rows = 10;
            columns = 4 ;
        }

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int seatNumber = row * columns + col + 1;
                createSeatButton(seatNumber,totalSeats);
            }
        }
    }

    private void createSeatButton(int seatNumber,Long totalseat) {

        CardView seatCard = new CardView(this);
        seatCard.setId(View.generateViewId());
        seatCard.setContentPadding(16, 16, 16, 16); // Adjust padding as needed
        seatCard.setCardElevation(8); // Adjust elevation as needed
        seatCard.setRadius(16); // Adjust corner radius as needed
        // Set border color for the CardView
        seatCard.setBackgroundResource(R.drawable.card_border);

        seatCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSeatButtonClick(seatNumber,seatCard);
            }
        });
        TextView seatNumberTextView = new TextView(this);
        seatNumberTextView.setText(String.valueOf(seatNumber));
        seatNumberTextView.setTextColor(Color.BLACK); // Change to your desired text color
        seatNumberTextView.setTextSize(16); // Change to your desired text size

        // Set layout parameters for the TextView
        CardView.LayoutParams textParams = new CardView.LayoutParams(
                CardView.LayoutParams.WRAP_CONTENT,
                CardView.LayoutParams.WRAP_CONTENT
        );
        textParams.gravity = Gravity.CENTER; // Adjust as needed
        seatNumberTextView.setLayoutParams(textParams);

        // Add the TextView to the CardView
        seatCard.addView(seatNumberTextView);

        // Set layout parameters for the seat card
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 5;
        params.height = 100;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);

        if(totalseat==50){
            if(seatNumber==no){

                params.setMargins(10,10,50,10);
                no=no+5;


            }
            else {


                params.setMargins(10, 10, 10, 10);
            }

        }
        else{

            if(seatNumber==no1){

                params.setMargins(10,10,50,10);
                no1=no1+4;


            }
            else {


                params.setMargins(10,10,10,10);
            }

        }

        seatCard.setLayoutParams(params);
        seatCard.setBackgroundResource(R.drawable.card_border);



        // Check if the seat is booked and set background color accordingly
        if (bookedSeats.contains(seatNumber)) {
            markSeatAsBooked(seatCard);
        } else {
            markSeatAsAvailable(seatCard);
        }

        // Add the seat button to the GridLayout
        gridLayout.addView(seatCard);

    }
    private void onSeatButtonClick(int seatNumber, CardView seatButton) {
        if (!onbook.contains(seatNumber)) {
            // Seat is available, mark it as booked
            onbook.add(seatNumber);
            int color = ContextCompat.getColor(this, R.color.main); // Replace with your color resource

            seatButton.setBackgroundColor(color);



            // TODO: Implement logic to save booking information to the database
            // You may want to store the booked seat information here
        } else {
            // Seat is already booked
            onbook.remove(Integer.valueOf(seatNumber));
            markSeatAsAvailable(seatButton);
        }
    }

    private void markSeatAsBooked(CardView seatButton) {
        seatButton.setBackgroundColor(Color.GRAY); // Change to your desired color for booked seats
        seatButton.setEnabled(false); // Disable the button for booked seats
    }

    private void markSeatAsAvailable(CardView seatButton) {
        seatButton.setBackgroundColor(Color.WHITE); // Change to your desired color for available seats
        seatButton.setEnabled(true); // Enable the button for available seats
    }
}