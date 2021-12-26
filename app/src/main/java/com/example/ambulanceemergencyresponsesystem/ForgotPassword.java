package com.example.ambulanceemergencyresponsesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText txtemail;
    private Button btnreset;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        txtemail = (EditText) findViewById(R.id.email);
        btnreset = (Button) findViewById(R.id.reset);

        auth = FirebaseAuth.getInstance();

        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetpassword();

            }
        });






    }

    private void resetpassword() {

        String email = txtemail.getText().toString().trim();

        if (email.isEmpty()){

            txtemail.setError("Email is required!");
            txtemail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            txtemail.setError("Please Provide valid Email!");
            txtemail.requestFocus();
            return;
        }

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    Toast.makeText(ForgotPassword.this,"Check your email to reset your password!", Toast.LENGTH_LONG).show();
                }else {

                    Toast.makeText(ForgotPassword.this," Try Again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}