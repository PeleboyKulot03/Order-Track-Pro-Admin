package com.example.ordertrackproadmin.utils;

import androidx.annotation.NonNull;

import com.example.ordertrackproadmin.ui.controller.IEditFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditModel {
    private String name, imageUrl, id;
    private double price;
    private int qty;
    private FirebaseDatabase database;
    private DatabaseReference reference;


    public EditModel(String imageUrl, String name, double price, int qty, String id) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
        this.qty = qty;
        this.id = id;
    }
    public EditModel() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    public String getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQty() {
        return qty;
    }

    public void getProducts(IEditFragment iEditFragment) {
        ArrayList<ArrayList<EditModel>> models = new ArrayList<>();
        reference.child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot meal: snapshot.getChildren()) {
                    ArrayList<EditModel> tempModel = new ArrayList<>();
                    for (DataSnapshot product: meal.getChildren()) {
                        EditModel model = product.getValue(EditModel.class);
                        tempModel.add(model);
                    }
                    models.add(tempModel);
                }
                iEditFragment.getProducts(models);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                iEditFragment.getProducts(null);
            }
        });
    }
}
