package com.example.ordertrackproadmin.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ordertrackproadmin.R;
import com.example.ordertrackproadmin.ui.controller.IEditFragment;
import com.example.ordertrackproadmin.utils.EditModel;

import java.util.ArrayList;

public class EditRecyclerViewAdapter extends RecyclerView.Adapter<EditRecyclerViewAdapter.ViewHolder> {
    private final ArrayList<EditModel> models;
    private final Context context;
    private final Activity activity;
    private final IEditFragment iEditFragment;
    private final String classification;

    public EditRecyclerViewAdapter(ArrayList<EditModel> models, Context context, Activity activity, IEditFragment iEditFragment, String classification) {
        this.models = models;
        this.context = context;
        this.activity = activity;
        this.iEditFragment = iEditFragment;
        this.classification = classification;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.edit_item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EditModel model = models.get(holder.getAdapterPosition());
        holder.foodName.setText(model.getName());
        String qtyString = "x" + model.getQty();
        String priceString = "₱ " + model.getPrice();
        holder.qty.setText(qtyString);
        holder.price.setText(priceString);
        Glide.with(context).load(model.getImageUrl()).into(holder.foodImage);
        holder.item.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditItemActivity.class);
            intent.putExtra("name", model.getName());
            intent.putExtra("qty", model.getQty());
            intent.putExtra("price", model.getPrice());
            intent.putExtra("image", model.getImageUrl());
            intent.putExtra("classification", classification);
            intent.putExtra("id", model.getId());
            activity.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView foodName;
        private final TextView price;
        private final TextView qty;
        private final ImageView foodImage;
        private final RelativeLayout item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            price = itemView.findViewById(R.id.price);
            qty = itemView.findViewById(R.id.qty);
            foodImage = itemView.findViewById(R.id.foodImage);
            item = itemView.findViewById(R.id.item);
        }
    }
}
