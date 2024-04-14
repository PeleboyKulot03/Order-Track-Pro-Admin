package com.example.ordertrackproadmin.ui.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.ordertrackproadmin.R;
import com.example.ordertrackproadmin.ui.controller.IAddAccount;
import com.example.ordertrackproadmin.ui.customViews.AlertNotice;
import com.example.ordertrackproadmin.utils.AddNewAccountModel;

import java.util.Objects;
import java.util.regex.Pattern;

public class AddNewActivity extends AppCompatActivity implements IAddAccount {
    private Uri finalUri = null;
    private EditText nameET, usernameET, passwordET;
    private ProgressBar progressBar;
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private AddNewAccountModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        model = new AddNewAccountModel();
        progressBar = findViewById(R.id.progressBar);
        ImageView profile = findViewById(R.id.image);
        passwordET = findViewById(R.id.passwordET);
        usernameET = findViewById(R.id.usernameET);
        nameET = findViewById(R.id.nameET);
        Button signUp = findViewById(R.id.signUp);

        ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                Glide.with(AddNewActivity.this).load(uri).centerCrop().circleCrop().into(profile);
                finalUri = uri;
                int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                AddNewActivity.this.getContentResolver().takePersistableUriPermission(finalUri, flag);
            }
            else {
                Toast.makeText(this, "No image is selected.", Toast.LENGTH_SHORT).show();
            }
        });
        profile.setOnClickListener(view -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));


        signUp.setOnClickListener(v -> {
            if (validate()) {
                progressBar.setVisibility(View.VISIBLE);
                AddNewAccountModel addNewAccountModel = new AddNewAccountModel(nameET.getText().toString(), "",  usernameET.getText().toString());
                model.addNewUser(usernameET.getText().toString(), passwordET.getText().toString(), addNewAccountModel, AddNewActivity.this, finalUri);
            }
        });
    }

    private boolean validate() {
        if (finalUri == null) {
            AlertNotice alertNotice = new AlertNotice(AddNewActivity.this, "INVALID IMAGE INPUT", "Sorry but image is a required field, Please select an image to continue!");
            Objects.requireNonNull(alertNotice.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertNotice.show();
            return false;
        }
        else if (nameET.getText().toString().isEmpty()) {
            AlertNotice alertNotice = new AlertNotice(AddNewActivity.this, "INVALID NAME INPUT", "Sorry but name is a required field, Please input a name to continue!");
            Objects.requireNonNull(alertNotice.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertNotice.show();
            nameET.requestFocus();
            return false;
        }
        else if (nameET.getText().toString().length() < 2) {
            AlertNotice alertNotice = new AlertNotice(AddNewActivity.this, "INVALID NAME INPUT", "Sorry but name needs to be at least 3 character long!");
            Objects.requireNonNull(alertNotice.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertNotice.show();
            nameET.requestFocus();
            return false;
        }
        else if (usernameET.getText().toString().isEmpty()) {
            AlertNotice alertNotice = new AlertNotice(AddNewActivity.this, "INVALID EMAIL INPUT", "Sorry but email is a required field, Please input a food name to continue!");
            Objects.requireNonNull(alertNotice.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertNotice.show();
            usernameET.requestFocus();
            return false;
        }
        else if (usernameET.getText().toString().length() < 2) {
            AlertNotice alertNotice = new AlertNotice(AddNewActivity.this, "INVALID EMAIL INPUT", "Sorry but email needs to be at least 3 character long!");
            Objects.requireNonNull(alertNotice.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertNotice.show();
            usernameET.requestFocus();
            return false;
        }

        else if (!VALID_EMAIL_ADDRESS_REGEX.matcher(usernameET.getText().toString()).matches()) {
            AlertNotice alertNotice = new AlertNotice(AddNewActivity.this, "INVALID EMAIL INPUT", "Sorry but please input valid email address!");
            Objects.requireNonNull(alertNotice.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertNotice.show();
            usernameET.requestFocus();
            return false;
        }
        else if (passwordET.getText().toString().isEmpty()) {
            AlertNotice alertNotice = new AlertNotice(AddNewActivity.this, "INVALID PASSWORD INPUT", "Sorry but password is a required field, Please input a food name to continue!");
            Objects.requireNonNull(alertNotice.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertNotice.show();
            passwordET.requestFocus();
            return false;
        }
        else if (passwordET.getText().toString().length() < 6) {
            AlertNotice alertNotice = new AlertNotice(AddNewActivity.this, "INVALID PASSWORD INPUT", "Sorry but password needs to be at least 6 character long!");
            Objects.requireNonNull(alertNotice.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertNotice.show();
            passwordET.requestFocus();
            return false;
        }


        return true;
    }

    @Override
    public void onAddAccount(boolean verdict, String message) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}