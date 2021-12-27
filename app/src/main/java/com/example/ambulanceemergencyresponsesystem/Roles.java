package com.example.ambulanceemergencyresponsesystem;

import static com.example.ambulanceemergencyresponsesystem.common.Constants.KEY_ROLE;
import static com.example.ambulanceemergencyresponsesystem.common.Constants.ROLE_ADMIN;
import static com.example.ambulanceemergencyresponsesystem.common.Constants.ROLE_USER;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.ambulanceemergencyresponsesystem.databinding.ActivityRolesBinding;
import com.example.ambulanceemergencyresponsesystem.entities.Admin;
import com.example.ambulanceemergencyresponsesystem.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.function.Consumer;

public class Roles extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Roles";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRolesBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_roles);

        binding.btnUser.setOnClickListener(this);
        binding.btnAdmin.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        listener = firebaseAuth -> {
            FirebaseUser user = mAuth.getCurrentUser();

            if (user != null) {
                FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(user.getUid())
                        .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        User user1 = task.getResult().toObject(User.class);
                        if (user1 != null) {
                            Log.d(TAG, "onStart: user success");
                            startActivity(new Intent(this, Home.class));
                            finishAffinity();
                        }

                    }
                });

                FirebaseFirestore.getInstance()
                        .collection("admins")
                        .document(user.getUid())
                        .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Admin admin = task.getResult().toObject(Admin.class);
                        if (admin != null) {
                            Log.d(TAG, "onStart: admin success");
                            startActivity(new Intent(this, DashboardActivity.class));
                            finishAffinity();
                        }
                    }
                });

            } else {
                Log.e("Home", "user not logged in");
            }
        };
        mAuth.addAuthStateListener(listener);

    }

    @Override
    protected void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(listener);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_user) {
            Intent intent = new Intent(this, Login.class);
            intent.putExtra(KEY_ROLE, ROLE_USER);
            startActivity(intent);
        } else if (id == R.id.btn_admin) {
            Intent intent = new Intent(this, Login.class);
            intent.putExtra(KEY_ROLE, ROLE_ADMIN);
            startActivity(intent);
        }
    }
}