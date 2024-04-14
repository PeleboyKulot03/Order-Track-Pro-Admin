package com.example.ordertrackproadmin.utils;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.ordertrackproadmin.ui.controller.IAddItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class AddItemModel {
    private String name, id, imageUrl;
    private double price;
    private int qty;
    private DatabaseReference reference;
    private StorageReference storageReference;

    public AddItemModel(String imageUrl, String name, double price, int qty, String id) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
        this.qty = qty;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public int getQty() {
        return qty;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public AddItemModel() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("Products");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    public void addNewItem(AddItemModel model, Uri uri, String classification, IAddItem iAddItem) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot classification: snapshot.getChildren()) {
                    for (DataSnapshot meal: classification.getChildren()) {
                        if (meal.child("name").getValue(String.class).equals(model.getName())) {
                            iAddItem.onAddItem(false, "Food's name is already on the menu");
                            return;
                        }
                    }
                }
                storageReference = storageReference.child("product_images/" + classification).child(model.getId() + ".jpg");
                UploadTask uploadTask = storageReference.putFile(uri);
                Task<Uri> urlTask = uploadTask.continueWithTask(task1 -> {
                    if (!task1.isSuccessful()) {
                        throw Objects.requireNonNull(task1.getException());
                    }

                    return storageReference.getDownloadUrl();
                }).addOnCompleteListener(task12 -> {
                    if (task12.isSuccessful()) {
                        Uri downloadUri = task12.getResult();
                        model.setImageUrl(downloadUri.toString());
                        reference.child(classification).child(model.getId()).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                iAddItem.onAddItem(true, "Adding new item Complete!");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                iAddItem.onAddItem(false, e.getLocalizedMessage());
                            }
                        });
                    } else {
                        iAddItem.onAddItem(false, "An error occurred, please try again later");
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                iAddItem.onAddItem(false, error.getMessage());
            }
        });
    }
}
