package com.example.ordertrackproadmin.ui.view;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ordertrackproadmin.R;
import com.example.ordertrackproadmin.utils.AccountModel;

import java.util.ArrayList;

public class AccountFragmentAdapter extends RecyclerView.Adapter<AccountFragmentAdapter.ViewHolder> {
    private final ArrayList<AccountModel> models;
    private final Context context;

    public AccountFragmentAdapter(ArrayList<AccountModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @NonNull
    @Override
    public AccountFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.person_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountFragmentAdapter.ViewHolder holder, int position) {
        AccountModel model = models.get(position);
        holder.name.setText(model.getName());
        holder.email.setText(model.getEmail());
        Uri uri = Uri.parse(model.getImageUrl());
        Glide.with(context).load(uri).centerCrop().circleCrop().into(holder.profile);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView profile;
        private final TextView name;
        private final TextView email;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
        }
    }
}
