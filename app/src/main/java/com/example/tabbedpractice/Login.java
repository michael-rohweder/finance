package com.example.tabbedpractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText emailET = findViewById(R.id.email);
        final EditText passwordET = findViewById(R.id.password);
        final Button login = findViewById(R.id.login_button);
        final TextView newUser = findViewById(R.id.new_user);
        final CheckBox saveLogin = findViewById(R.id.savelogin);


        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String logout = intent.getStringExtra("logout");
        SharedPreferences sp1 = this.getSharedPreferences("Login", MODE_PRIVATE);
        if (logout != null) {
            if (logout.contains("true")) {
                sp1.edit().clear().commit();
                emailET.setText("");
                passwordET.setText("");
                saveLogin.setChecked(false);
            }
        }


        login.setEnabled(false);

        email = sp1.getString("User", null);
        password = sp1.getString("Password", null);

        if (email != null && password != null) {
            if (!email.isEmpty() && !password.isEmpty()) {
                Toast.makeText(Login.this, "Loggin In...", Toast.LENGTH_LONG).show();
                emailET.setText(email);
                passwordET.setText(password);
                saveLogin.setChecked(true);
                login(email, password);
            }
        }

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, NewUser.class));
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
                if (TextUtils.isEmpty(emailET.getText().toString()) || TextUtils.isEmpty(passwordET.getText().toString())){
                    login.setEnabled(false);
                } else {
                    login.setEnabled(true);
                }
            }
        });
        emailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(emailET.getText().toString()) || TextUtils.isEmpty(passwordET.getText().toString())){
                    login.setEnabled(false);
                } else {
                    login.setEnabled(true);
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailET.getText().toString();
                password = passwordET.getText().toString();
                login(email, password, saveLogin);
            }
        });
    }

    private void login(final String email, final String password, final CheckBox saveLoginCheck){
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            currentUser = mAuth.getCurrentUser();
                            Toast.makeText(Login.this, "LOG IN SUCCESSFUL", Toast.LENGTH_LONG).show();
                            if (saveLoginCheck.isChecked()){
                                SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
                                SharedPreferences.Editor Ed = sp.edit();
                                Ed.putString("User", email);
                                Ed.putString("Password", password);
                                Ed.commit();
                            }
                            startActivity(new Intent(Login.this, MainActivity.class));
                        } else {
                            Toast.makeText(Login.this, "ERROR LOGGING IN", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void login(final String email, final String password){
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            currentUser = mAuth.getCurrentUser();
                            Toast.makeText(Login.this, "LOG IN SUCCESSFUL", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Login.this, MainActivity.class));
                        } else {
                            Toast.makeText(Login.this, "ERROR LOGGING IN", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
