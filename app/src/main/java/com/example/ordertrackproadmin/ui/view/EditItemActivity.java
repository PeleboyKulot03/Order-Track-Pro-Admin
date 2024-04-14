package com.example.ordertrackproadmin.ui.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.example.ordertrackproadmin.ui.controller.IEditItem;
import com.example.ordertrackproadmin.utils.EditItemModel;

public class EditItemActivity extends AppCompatActivity implements IEditItem {
    private String name = "";
    private String id = "";
    private double price = 0.0;
    private int qty = 0;
    private String image = "";
    private String classification = "";
    private Uri finalUri = null;
    private String newName = "";
    private int newQty = 0;
    private double newPrice = 0.0;
    private EditText foodNameET;
    private EditText qtyET;
    private EditText priceET;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        EditItemModel model = new EditItemModel();
        progressBar = findViewById(R.id.progressBar);
        Intent intent = getIntent();

        if (intent.hasExtra("name")) {
            name = intent.getStringExtra("name");
            price = intent.getDoubleExtra("price", 0.0);
            qty = intent.getIntExtra("qty", 0);
            image = intent.getStringExtra("image");
            classification = intent.getStringExtra("classification");
            id = intent.getStringExtra("id");
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView foodImage = findViewById(R.id.foodImage);
        foodNameET = findViewById(R.id.foodName);
        qtyET = findViewById(R.id.qty);
        priceET = findViewById(R.id.price);
        Button updateItem = findViewById(R.id.updateItem);
        Button deleteItem = findViewById(R.id.deleteItem);

        priceET.setText(String.valueOf(price));
        qtyET.setText(String.valueOf(qty));
        foodNameET.setText(name);
        Glide.with(getApplicationContext()).load(image).into(foodImage);
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                Glide.with(EditItemActivity.this).load(uri).into(foodImage);
                finalUri = uri;
                int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                EditItemActivity.this.getContentResolver().takePersistableUriPermission(finalUri, flag);
            }
            else {
                Toast.makeText(this, "No image is selected.", Toast.LENGTH_SHORT).show();
            }
        });
        foodImage.setOnClickListener(view -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build()));

        updateItem.setOnClickListener(v -> {
            getValues();
            if (hasChanged()) {
                progressBar.setVisibility(View.VISIBLE);
                EditItemModel editItemModel = new EditItemModel(image, newName, newPrice, newQty, id);
                model.updateItem(editItemModel, EditItemActivity.this, classification, finalUri);
                return;
            }
            Toast.makeText(this, "Please update at least one field to continue!", Toast.LENGTH_SHORT).show();
            
        });

        deleteItem.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditItemActivity.this);
            alertDialogBuilder.setTitle("Delete Caution");
            alertDialogBuilder.setMessage("Clicking yes means you agree to delete this item permanently, Are you sure that you want to delete this item?");
            alertDialogBuilder.setPositiveButton("Yes", (dialog, which) -> {
                model.deleteItem(model.getName(), classification, id, EditItemActivity.this);
                dialog.dismiss();
            });
            alertDialogBuilder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            alertDialogBuilder.create().show();
        });
    }

    private void getValues() {
        newName = foodNameET.getText().toString();
        newPrice = Double.parseDouble(priceET.getText().toString());
        newQty = Integer.parseInt(qtyET.getText().toString());
    }
    private boolean hasChanged() {
        if (finalUri == null && newName.equals(name) && newQty == qty && newPrice == price) {
            return false;
        }
        return true;
    }

    @Override
    public void onUpdateItem(boolean verdict, String message) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteItem(boolean verdict, String message) {
        progressBar.setVisibility(View.GONE);
        if (!verdict) {
            Toast.makeText(this, "Sorry but " + message, Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}