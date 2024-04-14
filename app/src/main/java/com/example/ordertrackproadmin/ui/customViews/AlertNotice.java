package com.example.ordertrackproadmin.ui.customViews;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ordertrackproadmin.R;

public class AlertNotice extends Dialog {
    private final String title;
    private final String description;

    public AlertNotice(@NonNull Context context, String title, String description) {
        super(context);
        this.title = title;
        this.description = description;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_notice);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Button okay = findViewById(R.id.okay);
        TextView titleTV = findViewById(R.id.title);
        TextView warningTV = findViewById(R.id.warning);

        titleTV.setText(title);
        warningTV.setText(description);
        okay.setOnClickListener(view -> dismiss());


    }
}