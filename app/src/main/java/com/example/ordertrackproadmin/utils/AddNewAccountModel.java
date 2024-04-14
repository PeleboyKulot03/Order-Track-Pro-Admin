package com.example.ordertrackproadmin.utils;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.ordertrackproadmin.ui.controller.IAddAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class AddNewAccountModel {
    private String name, imageUrl, email;

    private DatabaseReference reference;
    private FirebaseAuth auth;
    private StorageReference storageReference;

    public AddNewAccountModel() {
        reference = FirebaseDatabase.getInstance().getReference("Employees");
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
    }
    public AddNewAccountModel(String name, String imageUrl, String email) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void addNewUser(String email, String password, AddNewAccountModel model, IAddAccount iAddAccount, Uri uri) {
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            storageReference = storageReference.child("Employees/").child(authResult.getUser().getUid());
            UploadTask uploadTask = storageReference.putFile(uri);
            Task<Uri> urlTask = uploadTask.continueWithTask(task1 -> {
                if (!task1.isSuccessful()) {
                    throw Objects.requireNonNull(task1.getException());
                }

                return storageReference.getDownloadUrl();
            }).addOnCompleteListener(task12 -> {
                if (task12.isSuccessful()) {
                    Uri downloadUri = task12.getResult();
                    UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
                    builder.setPhotoUri(downloadUri);
                    builder.setDisplayName(model.getName());
                    auth.getCurrentUser().updateProfile(builder.build()).addOnSuccessListener(unused -> {
                        model.setImageUrl(downloadUri.toString());
                        reference.child(authResult.getUser().getUid()).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                iAddAccount.onAddAccount(true, "Adding new employee complete!");
                            }
                        }).addOnFailureListener(e -> iAddAccount.onAddAccount(false, e.getLocalizedMessage()));
                    }).addOnFailureListener(e -> iAddAccount.onAddAccount(false, e.getLocalizedMessage()));
                } else {
                    iAddAccount.onAddAccount(false, "An error occurred, please try again later");
                }
            });

        }).addOnFailureListener(e -> iAddAccount.onAddAccount(false, e.getLocalizedMessage()));
    }
}
