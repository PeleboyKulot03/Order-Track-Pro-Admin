package com.example.ordertrackproadmin.ui.view;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.ordertrackproadmin.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    public static final int EDIT = R.id.edit;
    public static final int ACCOUNT = R.id.account;
    private AccountFragment accountFragment;
    private EditFragment editFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setSelectedItemId(EDIT);
        accountFragment = new AccountFragment();
        editFragment = new EditFragment();

        switchFragment(editFragment);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == EDIT) {
                switchFragment(editFragment);
            }
            if (item.getItemId() == ACCOUNT) {
                switchFragment(accountFragment);
            }
            return true;
        });

    }

    public void switchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.rootLayout, fragment).commit();
    }
}