package com.example.ordertrackproadmin.ui.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ordertrackproadmin.R;
import com.example.ordertrackproadmin.ui.controller.IAccount;
import com.example.ordertrackproadmin.utils.AccountModel;

import java.util.ArrayList;


public class AccountFragment extends Fragment implements IAccount {

    private RecyclerView recyclerView;
    private AccountModel model;
    private ImageView add;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        model = new AccountModel();
        model.getUsers(this);
        add = view.findViewById(R.id.add);

        add.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), AddNewActivity.class));
        });
        return view;
    }

    @Override
    public void onGetUsers(ArrayList<AccountModel> models) {
        if (!models.isEmpty()) {
            Log.i("TAGELE", "onGetUsers: ");
            AccountFragmentAdapter adapter = new AccountFragmentAdapter(models, getContext());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }
}