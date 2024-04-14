package com.example.ordertrackproadmin.utils;

import androidx.annotation.NonNull;

import com.example.ordertrackproadmin.ui.controller.IAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AccountModel {
    private String name, imageUrl, email;
    private DatabaseReference reference;

    public AccountModel(String name, String imageUrl, String email) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public AccountModel() {
        reference = FirebaseDatabase.getInstance().getReference("Employees");
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void getUsers(IAccount iAccount) {
        ArrayList<AccountModel> models = new ArrayList<>();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot account: snapshot.getChildren()) {
                    String name = account.child("name").getValue(String.class);
                    String imageUrl = account.child("imageUrl").getValue(String.class);
                    String email = account.child("email").getValue(String.class);
                    AccountModel model = new AccountModel(name, imageUrl, email);
                    models.add(model);
                }
                iAccount.onGetUsers(models);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                iAccount.onGetUsers(null);
            }
        });
    }
}
