package com.example.ambulanceemergencyresponsesystem;

import static com.example.ambulanceemergencyresponsesystem.common.Constants.KEY_ROLE;
import static com.example.ambulanceemergencyresponsesystem.common.Constants.ROLE_ADMIN;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.ambulanceemergencyresponsesystem.dashboard.DashboardActivity;
import com.example.ambulanceemergencyresponsesystem.databinding.ActivityLoginBinding;
import com.example.ambulanceemergencyresponsesystem.entities.Admin;
import com.example.ambulanceemergencyresponsesystem.usermaps.UserMapActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth mAuth;
    private ActivityLoginBinding binding;
    private String role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        init();
        setListeners();

        mAuth = FirebaseAuth.getInstance();
    }

    private void init() {
        role = getIntent().getStringExtra(KEY_ROLE);

        if (role.equals(ROLE_ADMIN)) {
            binding.fgpass.setVisibility(View.GONE);
            binding.signUpContainer.setVisibility(View.GONE);
        }
    }

    private void setListeners() {
        binding.btnlogin.setOnClickListener(this);
        binding.fgpass.setOnClickListener(this);
        binding.txtSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.txtSignUp) {

            startActivity(new Intent(this, Registration.class));
        } else if (id == R.id.btnlogin) {

            userLogin();
        } else if (id == R.id.fgpass) {

            startActivity(new Intent(this, ForgotPassword.class));
        }

    }

    private void userLogin() {

        String email = binding.usrEmail.getText().toString().trim();
        String password = binding.usrPass.getText().toString().trim();

        if (email.isEmpty()) {
            binding.usrEmail.setError("Email is required");
            binding.usrEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.usrEmail.setError("Please provide valid email");
            binding.usrEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            binding.usrPass.setError("Password is required");
            binding.usrPass.requestFocus();
            return;
        }
        if (password.length() < 6) {
            binding.usrPass.setError("Password should not be less than 6 characters");
            binding.usrPass.requestFocus();
            return;
        }

        binding.progress.container.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                        if (role.equals(ROLE_ADMIN)) {

                            FirebaseFirestore.getInstance()
                                    .collection("admins")
                                    .document(user.getUid())
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Admin admin = task1.getResult().toObject(Admin.class);
                                            if (admin != null) {


                                                startActivity(new Intent(this, DashboardActivity.class));
                                                finishAffinity();
                                            } else {
                                                Toast.makeText(Login.this, "You are not authorized to login as admin", Toast.LENGTH_LONG).show();
                                            }


                                        }else {
                                            Toast.makeText(Login.this, "Failed to Login! Something went wrong!", Toast.LENGTH_LONG).show();
                                        }

                                        binding.progress.container.setVisibility(View.GONE);
                                    });

                        } else {
                            if (user.isEmailVerified()) {
                                startActivity(new Intent(Login.this, UserMapActivity.class));
                                finishAffinity();
                            } else {


                                user.sendEmailVerification();
                                Toast.makeText(Login.this, "Please check your email to verify your account!", Toast.LENGTH_LONG).show();

                            }
                            binding.progress.container.setVisibility(View.GONE);
                        }


                    } else {
                        binding.progress.container.setVisibility(View.GONE);
                        Toast.makeText(Login.this, "Failed to Login! Please check your credentials!", Toast.LENGTH_LONG).show();
                    }
                });


    }

}