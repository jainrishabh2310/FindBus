package com.example.findbus.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findbus.ModelClasses.BusData;
import com.example.findbus.R;
import com.example.findbus.TicketBook;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class busadapter extends RecyclerView.Adapter<busadapter.Viewholder> {
    Context context;
    ArrayList<String> arrayList= new ArrayList<>();
    public busadapter(Context context, ArrayList<String> arrayList){
        this.context=context;
        this.arrayList=arrayList;

    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.singlebus,parent,false);
        RecyclerView.ViewHolder viewholder= new Viewholder(v);
        return (Viewholder) viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {

        String busid=arrayList.get(position);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Buses").child(busid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    BusData busData=  snapshot.getValue(BusData.class);
                    holder.busname.setText(busData.getBusname().toString());
                    holder.busfrom.setText(busData.getFrom().toString());
                    holder.busto.setText(busData.getTo().toString());
                    holder.seatavailable.setText(""+busData.getBusseat());
                    holder.price.setText("₹"+busData.getBusprice());


                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent= new Intent(context, TicketBook.class);
                            intent.putExtra("Busid",arrayList.get(position).toString());
                            context.startActivity(intent);

                        }
                    });


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

    public class Viewholder extends RecyclerView.ViewHolder{
        TextView busname;
        TextView busfrom;
        TextView busto;
        TextView seatavailable;
        TextView price;



        public Viewholder(@NonNull View itemView) {
            super(itemView);

            busname=itemView.findViewById(R.id.singlebusname);
            busfrom=itemView.findViewById(R.id.singlebusfrom);
            busto=itemView.findViewById(R.id.singletobus);
            seatavailable=itemView.findViewById(R.id.seatavailablle);
            price=itemView.findViewById(R.id.busprice);
        }
    }
}
