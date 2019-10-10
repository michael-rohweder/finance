package com.example.tabbedpractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class NewUser extends AppCompatActivity {

    private FirebaseAuth mAuth;
    boolean validEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        mAuth = FirebaseAuth.getInstance();

        final EditText emailET = findViewById(R.id.newEmail);
        final EditText passwordET = findViewById(R.id.newPassword);
        final EditText confirmPasswordET = findViewById(R.id.confirmPassword);

        final Button createUser = findViewById(R.id.create_user);

        createUser.setEnabled(false);

        emailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String email = emailET.getText().toString();

                validEmail = false;

                if (email.contains("@")){
                    if (email.contains(".com") || email.contains(".net") || email.contains(".org")) {
                        validEmail = true;
                    }
                }

                if (!validEmail){
                    emailET.setTextColor(Color.RED);
                    createUser.setEnabled(false);
                }else {
                    emailET.setTextColor(Color.BLACK);
                    if (!passwordET.getText().toString().isEmpty() && !confirmPasswordET.getText().toString().isEmpty()) {
                        createUser.setEnabled(true);
                    }
                }
            }
        });

        confirmPasswordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = passwordET.getText().toString();
                String confirm = confirmPasswordET.getText().toString();
                if (!password.equals(confirm)){
                    passwordET.setTextColor(Color.RED);
                    confirmPasswordET.setTextColor(Color.RED);
                    createUser.setEnabled(false);
                }else {
                    if (!confirm.isEmpty()) {
                        passwordET.setTextColor(Color.BLACK);
                        confirmPasswordET.setTextColor(Color.BLACK);
                        createUser.setEnabled(true);
                    }
                }
            }
        });

        passwordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = passwordET.getText().toString();
                String confirm = confirmPasswordET.getText().toString();
                if (!password.equals(confirm)){
                    passwordET.setTextColor(Color.RED);
                    confirmPasswordET.setTextColor(Color.RED);
                    createUser.setEnabled(false);
                }else {
                    if (!password.isEmpty()) {
                        passwordET.setTextColor(Color.BLACK);
                        confirmPasswordET.setTextColor(Color.BLACK);
                        createUser.setEnabled(true);
                    }
                }
            }
        });

        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                String confirmPassword = confirmPasswordET.getText().toString();

                if (password.equals(confirmPassword)){
                    if (!validEmail){
                        emailET.setTextColor(Color.RED);
                        Toast.makeText(NewUser.this, "Email address invalid", Toast.LENGTH_LONG).show();
                    }
                    else {
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(NewUser.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        try {
                                            //check if successful
                                            if (task.isSuccessful()) {
                                                //User is successfully registered and logged in
                                                //start Profile Activity here
                                                Toast.makeText(NewUser.this, "registration successful", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), Login.class));
                                            }else{
                                                Toast.makeText(NewUser.this, "ERROR ON CREATION", Toast.LENGTH_LONG).show();
                                            }
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    }
                } else {
                    passwordET.setTextColor(Color.RED);
                    confirmPasswordET.setTextColor(Color.RED);
                    Toast.makeText(NewUser.this, "Passwords must match", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
