package com.example.ambulanceemergencyresponsesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ambulanceemergencyresponsesystem.databinding.ActivityUserProfileBinding;
import com.example.ambulanceemergencyresponsesystem.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class UserProfile extends AppCompatActivity {

    private ActivityUserProfileBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        mAuth = FirebaseAuth.getInstance();


        binding.updateButton.setOnClickListener(v -> {
            binding.progressbar.progressbarContainer.setVisibility(View.VISIBLE);
            String name = binding.editTextName.getText().toString();
            String email = binding.editTextEmail.getText().toString();
            String phone = binding.editTextPhone.getText().toString();
            String address = binding.editTextAddress.getText().toString();

            User user = new User(name, email, phone, address);

            FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    binding.progressbar.progressbarContainer.setVisibility(View.GONE);
                    binding.fullName.setText(user.username);

                }
            });

        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        binding.progressbar.progressbarContainer.setVisibility(View.VISIBLE);
        mAuth.addAuthStateListener(firebaseAuth -> {
            user = firebaseAuth.getCurrentUser();
            if (user != null) {
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        User userData = task.getResult().getValue(User.class);
                        if (userData != null) {

                            binding.fullName.setText(userData.username);
                            binding.editTextName.setText(userData.username, TextView.BufferType.EDITABLE);
                            binding.editTextEmail.setText(userData.email, TextView.BufferType.EDITABLE);
                            binding.editTextPhone.setText(userData.phoneNumber, TextView.BufferType.EDITABLE);
                            binding.editTextAddress.setText(userData.address, TextView.BufferType.EDITABLE);
                            binding.progressbar.progressbarContainer.setVisibility(View.GONE);
                        }


                    }
                });
            } else {
                Log.e("UserProfile", "onStart: user is null");
            }
        });


    }
}