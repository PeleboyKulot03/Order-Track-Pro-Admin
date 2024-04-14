package com.example.ordertrackproadmin.ui.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.ordertrackproadmin.R;
import com.example.ordertrackproadmin.ui.controller.IAddItem;
import com.example.ordertrackproadmin.ui.customViews.AlertNotice;
import com.example.ordertrackproadmin.utils.AddItemModel;

import java.util.Objects;
import java.util.Random;

public class AddItemActivity extends AppCompatActivity implements IAddItem {
    private ImageView foodImage;
    private EditText foodNameET, priceET, qtyET;
    private Spinner spinner;
    private Uri finalUri = null;
    private String finalCategory = "";
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        AddItemModel addItemModel = new AddItemModel();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        progressBar = findViewById(R.id.progressBar);
        foodImage = findViewById(R.id.foodImage);
        foodNameET = findViewById(R.id.foodName);
        priceET = findViewById(R.id.price);
        qtyET = findViewById(R.id.qty);
        Button add = findViewById(R.id.add);
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                finalCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(AddItemActivity.this, "Please select a category to continue!", Toast.LENGTH_SHORT).show();
            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.categories,
                R.layout.spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                Glide.with(AddItemActivity.this).load(uri).into(foodImage);
                finalUri = uri;
                int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                AddItemActivity.this.getContentResolver().takePersistableUriPermission(finalUri, flag);
            }
            else {
                Toast.makeText(this, "No image is selected.", Toast.LENGTH_SHORT).show();
            }
        });
        foodImage.setOnClickListener(view -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));

        add.setOnClickListener(v -> {
            if (validator()) {
                progressBar.setVisibility(View.VISIBLE);
                String id = getRandomId();
                AddItemModel model = new AddItemModel("",
                        foodNameET.getText().toString(),
                        Double.parseDouble(priceET.getText().toString()),
                        Integer.parseInt(qtyET.getText().toString()),
                        id);

                addItemModel.addNewItem(model, finalUri, finalCategory, AddItemActivity.this);
            }
        });

    }

    private String getRandomId() {
        Random r = new Random();

        String alphabet = "123abcdefghijklmnopqrstuvwxyz";
        StringBuilder finalId = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            finalId.append(alphabet.charAt(r.nextInt(alphabet.length())));
        }
        return finalId.toString();
    }

    private boolean validator() {
        if (finalUri == null) {
            AlertNotice alertNotice = new AlertNotice(AddItemActivity.this, "INVALID IMAGE INPUT", "Sorry but image is a required field, Please select an image to continue!");
            Objects.requireNonNull(alertNotice.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertNotice.show();
            return false;
        }
        else if (foodNameET.getText().toString().isEmpty()) {
            AlertNotice alertNotice = new AlertNotice(AddItemActivity.this, "INVALID FOOD NAME INPUT", "Sorry but food's name is a required field, Please input a food name to continue!");
            Objects.requireNonNull(alertNotice.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertNotice.show();
            foodNameET.requestFocus();
            return false;
        }
        else if (foodNameET.getText().toString().length() < 2) {
            AlertNotice alertNotice = new AlertNotice(AddItemActivity.this, "INVALID FOOD NAME INPUT", "Sorry but food's name needs to be at least 3 character long!");
            Objects.requireNonNull(alertNotice.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertNotice.show();
            foodNameET.requestFocus();
            return false;
        }

        else if (priceET.getText().toString().isEmpty()) {
            AlertNotice alertNotice = new AlertNotice(AddItemActivity.this, "INVALID PRICE INPUT", "Sorry but price is a required field, Please input a price to continue!");
            Objects.requireNonNull(alertNotice.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertNotice.show();
            priceET.requestFocus();
            return false;
        }
        else if (Integer.parseInt(priceET.getText().toString()) <= 0) {
            AlertNotice alertNotice = new AlertNotice(AddItemActivity.this, "INVALID PRICE INPUT", "Please enter a valid price to continue!");
            Objects.requireNonNull(alertNotice.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertNotice.show();
            priceET.requestFocus();
            return false;
        }

        else if (qtyET.getText().toString().isEmpty()) {
            AlertNotice alertNotice = new AlertNotice(AddItemActivity.this, "INVALID QUANTITY INPUT", "Sorry but quantity is a required field, Please input a quantity to continue!");
            Objects.requireNonNull(alertNotice.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertNotice.show();
            qtyET.requestFocus();
            return false;
        }
        else if (Integer.parseInt(qtyET.getText().toString()) <= 0) {
            AlertNotice alertNotice = new AlertNotice(AddItemActivity.this, "INVALID QUANTITY INPUT", "Please enter a valid quantity to continue!");
            Objects.requireNonNull(alertNotice.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertNotice.show();
            priceET.requestFocus();
            return false;
        }

        else if (finalCategory.equals("None")) {
            AlertNotice alertNotice = new AlertNotice(AddItemActivity.this, "INVALID CATEGORY INPUT", "Sorry but category is a required field, Please input a category to continue!");
            Objects.requireNonNull(alertNotice.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertNotice.show();
            spinner.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onAddItem(boolean verdict, String message) {
        progressBar.setVisibility(View.GONE);
        if (!verdict) {
            AlertNotice alertNotice = new AlertNotice(AddItemActivity.this, "WARNING NOTICE", "Sorry but " + message);
            Objects.requireNonNull(alertNotice.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertNotice.show();
            foodNameET.requestFocus();
            return;
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}