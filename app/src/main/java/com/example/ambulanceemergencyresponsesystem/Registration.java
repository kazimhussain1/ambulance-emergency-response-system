package com.example.ambulanceemergencyresponsesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity implements View.OnClickListener {

    private TextView btnRegister;
    private EditText txtUsername, txtEmail, txtPassword, txtConPass;

    private FirebaseAuth mAuth;
    private Object user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        txtUsername = (EditText) findViewById(R.id.inputUsername);
        txtEmail = (EditText) findViewById(R.id.inputEmail);
        txtPassword= (EditText) findViewById(R.id.inputPassword);
        txtConPass = (EditText) findViewById(R.id.inputConPass);




        TextView btn=findViewById(R.id.txtLogin);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this,Login.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:
                btnRegister();
                break;
        }

    }

    private void btnRegister() {

        String username= txtUsername.getText().toString().trim();
        String email= txtEmail.getText().toString().trim();
        String password= txtPassword.getText().toString().trim();
        String conpass= txtConPass.getText().toString().trim();

        if (username.isEmpty()){
            txtUsername.setError("UserName is required");
            txtUsername.requestFocus();
            return;
        }
        if (email.isEmpty()){
            txtEmail.setError("Email is required");
            txtEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtEmail.setError("Please provide valid email");
            txtEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            txtPassword.setError("Password is required");
            txtPassword.requestFocus();
            return;
        }
        if (password.length() < 6){
            txtPassword.setError("Password should not be less than 6 characters");
            txtPassword.requestFocus();
            return;
        }



        if (conpass.isEmpty()){
            txtConPass.setError("Confirm Password is required");
            txtConPass.requestFocus();
            return;
        }
        if (conpass.length() < 6){
            txtConPass.setError("Confirm Password should not be less than 6 characters");
            txtConPass.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            user  us = new user(username, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(us).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){

                                        Toast.makeText(Registration.this,"user has been registered successfully!", Toast.LENGTH_LONG).show();

                                    }else{
                                        Toast.makeText(Registration.this,"Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                    }else{
                            Log.d("onComplete: ", task.getException().toString());
                            Toast.makeText(Registration.this,"Failed to register! Try again!", Toast.LENGTH_LONG).show();
                        }
                }


        });

    }
}