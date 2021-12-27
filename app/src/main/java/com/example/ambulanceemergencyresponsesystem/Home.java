package com.example.ambulanceemergencyresponsesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    private Button logout, profile, map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logout = (Button) findViewById(R.id.btnlogout);
        profile = (Button) findViewById(R.id.btn_profile);
        map = (Button) findViewById(R.id.btn_maps);

        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Home.this, GettingStarted.class));
        });

        profile.setOnClickListener(v -> startActivity(new Intent(Home.this, UserProfile.class)));

        map.setOnClickListener(v -> startActivity(new Intent(Home.this, UserMaps.class)));
    }
}