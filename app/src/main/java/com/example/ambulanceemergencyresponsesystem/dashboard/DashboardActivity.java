package com.example.ambulanceemergencyresponsesystem.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.ambulanceemergencyresponsesystem.R;
import com.example.ambulanceemergencyresponsesystem.RolesActivity;
import com.example.ambulanceemergencyresponsesystem.databinding.ActivityDashboardBinding;
import com.example.ambulanceemergencyresponsesystem.entities.Admin;
import com.example.ambulanceemergencyresponsesystem.users.UserListActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {


    private ActivityDashboardBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        setupDrawerAndToolbar();
        setListeners();

        mAuth = FirebaseAuth.getInstance();
    }

    private void setListeners() {

        binding.contentDashboard.btnUsers.setOnClickListener(this);
        binding.btnLogout.setOnClickListener(this);
    }

    private void setupDrawerAndToolbar() {

        binding.toolbar.setTitle("Admin Panel");
        setSupportActionBar(binding.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

    }

    @Override
    protected void onStart() {
        super.onStart();

        listener = firebaseAuth -> {
            FirebaseUser user = mAuth.getCurrentUser();

            if (user != null) {


                FirebaseFirestore.getInstance()
                        .collection("admins")
                        .document(user.getUid())
                        .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Admin admin = task.getResult().toObject(Admin.class);
                        if (admin != null) {
                            binding.navHeader.name.setText(admin.username);
                            binding.navHeader.email.setText(user.getEmail());
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
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_users) {
            startActivity(new Intent(this, UserListActivity.class));
        } else if(id == R.id.btn_logout){

            mAuth.signOut();
            Intent intent = new Intent(this, RolesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            finishAffinity();
        }
    }
}