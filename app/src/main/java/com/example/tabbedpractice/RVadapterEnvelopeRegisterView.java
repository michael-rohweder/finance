package com.example.tabbedpractice;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class RVadapterEnvelopeRegisterView extends RecyclerView.Adapter<RVadapterEnvelopeRegisterView.EnvelopeViewHolder> {
    List<Envelope> envelopes;
    public FirebaseFirestore db;
    public static CardView cv;

    public static class EnvelopeViewHolder extends RecyclerView.ViewHolder {

        TextView envelopeName;
        TextView envelopeAmount;
        public View view;

        EnvelopeViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            cv = (CardView)itemView.findViewById(R.id.cv);
            envelopeName = (TextView)itemView.findViewById(R.id.envelopeName);
            envelopeAmount = (TextView)itemView.findViewById(R.id.envelopeAmount);
        }
    }

    RVadapterEnvelopeRegisterView(List<Envelope> envelope){
        this.envelopes = envelope;
    }

    @Override
    public int getItemCount(){
        return envelopes.size();
    }

    @Override
    public EnvelopeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.register_view_layout, viewGroup, false);
        EnvelopeViewHolder evh = new EnvelopeViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(final EnvelopeViewHolder envelopeViewHolder, final int i){
        envelopeViewHolder.envelopeName.setText(envelopes.get(i).name);
        String balance = "BALANCE: " + String.format("$%.2f", Float.parseFloat(envelopes.get(i).amount));
        if (Float.parseFloat(envelopes.get(i).amount.toString()) < 0){
            envelopeViewHolder.envelopeAmount.setTextColor(Color.RED);
        }
        envelopeViewHolder.envelopeAmount.setText(balance);
        envelopeViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = FirebaseFirestore.getInstance();


                db.collection("envelope_data").document(envelopes.get(i).ID)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                envelopes.remove(i);

            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }
}
