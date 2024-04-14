package com.example.ordertrackproadmin.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ordertrackproadmin.R;

import java.util.ArrayList;

public class EditFragmentAdapter extends RecyclerView.Adapter<EditFragmentAdapter.ViewHolder> {
    private final ArrayList<EditRecyclerViewAdapter> adapters;
    private final Context context;

    public EditFragmentAdapter(ArrayList<EditRecyclerViewAdapter> adapters, Context context) {
        this.adapters = adapters;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EditRecyclerViewAdapter adapter = adapters.get(holder.getAdapterPosition());
        holder.recyclerView.setAdapter(adapter);
        holder.recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
    }

    @Override
    public int getItemCount() {
        return adapters.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
        }
    }
}
