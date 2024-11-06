package com.example.findbus.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findbus.ModelClasses.BookingHistory;
import com.example.findbus.ModelClasses.BusData;
import com.example.findbus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class bookingadapter extends  RecyclerView.Adapter<bookingadapter.viewholder>{
    Context context;
    ArrayList<String> arrayList;
    public bookingadapter(Context context, ArrayList<String> arrayList){
        this.context=context;
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.singlebus,parent,false);
        viewholder viewholder1= new viewholder(v);
        return viewholder1;
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        String key=arrayList.get(position);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("booking")
                ;
        reference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    BookingHistory bookingHistory= snapshot.getValue(BookingHistory.class);
                    if(bookingHistory!=null) {


                        String busid = bookingHistory.getBusid();
                        if (busid != null) {
                            FirebaseDatabase.getInstance().getReference("Buses")
                                    .child(busid).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                BusData busData = snapshot.getValue(BusData.class);
                                                holder.busname.setText(busData.getBusname().toString());
                                                holder.busfrom.setText(busData.getFrom().toString());
                                                holder.busto.setText(busData.getTo().toString());
                                                holder.price.setText("₹" + busData.getBusprice());
                                                reference.child(key)
                                                        .child("bookedseat").addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                holder.seatavailable.setText(snapshot.getChildrenCount() + "");

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });


                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    class viewholder extends RecyclerView.ViewHolder{
        TextView busname;
        TextView busfrom;
        TextView busto;
        TextView seatavailable;
        TextView price;



        public viewholder(@NonNull View itemView) {
            super(itemView);


            busname=itemView.findViewById(R.id.singlebusname);
            busfrom=itemView.findViewById(R.id.singlebusfrom);
            busto=itemView.findViewById(R.id.singletobus);
            seatavailable=itemView.findViewById(R.id.seatavailablle);
            price=itemView.findViewById(R.id.busprice);
        }
    }
}

