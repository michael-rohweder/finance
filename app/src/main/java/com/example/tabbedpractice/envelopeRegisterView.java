package com.example.tabbedpractice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.support.v4.app.Fragment;

//import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class envelopeRegisterView extends Fragment {
    private static final String TAG = MainActivity.class.getSimpleName();
    public List<Envelope> envelopes = new ArrayList<Envelope>();
    public RecyclerView rv;
    public FirebaseFirestore db;
    public String ID;
    public RVadapterEnvelopeView adapter;
    public FirebaseAuth mAuth;
    public static String userName, envelopeDBLocation;



    public envelopeRegisterView() {
    }

    public void loadUI(){

        db.collection(envelopeDBLocation)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){

                                //String name = document.getData().get("name").toString();
                                String amount = document.getData().get("amount").toString();
                                String ID = document.getId();
                                adapter = new RVadapterEnvelopeView(envelopes);
                                rv.setAdapter(adapter);

                                Log.d(TAG, document.getId() + " =>" + document.getData());
                            }
                        }else {
                            Log.w(TAG, "ERROR GETTING DOCUMENTS.", task.getException());
                        }
                    }
                });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_all_envelopes, container, false);

        rv = (RecyclerView)view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        mAuth = FirebaseAuth.getInstance();
        userName = mAuth.getUid();

        envelopeDBLocation = "users/" + userName + "/envelopes/" + passedEnvelope + "/register";

        adapter = new RVadapterEnvelopeView(envelopes);
        rv.setLayoutManager(llm);


        db = FirebaseFirestore.getInstance();
        db.collection(envelopeDBLocation)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        envelopes.clear();
                        for (QueryDocumentSnapshot doc:queryDocumentSnapshots){
                            if (doc.get("amount") != null){
//                                String name = doc.get("name").toString();

                                String name = doc.getId();

                                String amount = doc.get("amount").toString();
                                String ID = doc.getId();

                                envelopes.add(new Envelope(ID,name,amount));

                            }
                        }
                        adapter.notifyDataSetChanged();

                    }
                });
        loadUI();
        RVadapterEnvelopeView adapter = new RVadapterEnvelopeView(envelopes);
        rv.setAdapter(adapter);

        final FloatingActionMenu FABMenu = view.findViewById(R.id.FABMenu);

        FloatingActionButton newEnvelope = view.findViewById(R.id.addEnvelopeMenuButton);
        FloatingActionButton profile = view.findViewById(R.id.ProfileMenuButton);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), Profile.class);
                intent.putExtra("sending_class", "all_envelopes");
                startActivity(intent);
            }
        });

        rv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        newEnvelope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FABMenu.close(true);

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("");
                final View alertLayout = getLayoutInflater().inflate(R.layout.add_envelope_layout,null);
                builder.setView(alertLayout);
                final EditText nameET = alertLayout.findViewById(R.id.newEnvelopeName);
                final EditText amountET = alertLayout.findViewById(R.id.newEnvelopeAmount);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = nameET.getText().toString();
                        String amount = amountET.getText().toString();
                        Map<String, Object> envelope = new HashMap<>();
                        //envelope.put("name", name);
                        envelope.put("amount", amount);
                        db.collection(envelopeDBLocation).document(name)
                                .set(envelope)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //ID = documentReference.getId();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });

                        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        Date date = new Date();

                        String currentDate = dateFormat.format(date);

                        Map<String, Object> register = new HashMap<>();
                        register.put("date", currentDate);
                        register.put("payee", "STARTING BALANCE");
                        register.put("amount", amount);
                        register.put("balance", amount);

                        db.collection(envelopeDBLocation + "/" + name + "/register").document("RegisterEntry")
                                .set(register);

                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog = builder.create();
                amountET.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (TextUtils.isEmpty(amountET.getText().toString()) || TextUtils.isEmpty(nameET.getText().toString())){
                            ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        }else {
                            ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        }
                    }
                });
                nameET.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (TextUtils.isEmpty(nameET.getText().toString()) || TextUtils.isEmpty(amountET.getText().toString())){
                            ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        } else {
                            ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        }
                    }
                });

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                       ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    }
                });
                dialog.show();
            }
        });

        return view;
    }
}
