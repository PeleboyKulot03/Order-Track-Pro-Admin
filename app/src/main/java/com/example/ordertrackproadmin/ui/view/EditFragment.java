package com.example.ordertrackproadmin.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ordertrackproadmin.R;
import com.example.ordertrackproadmin.ui.controller.IEditFragment;
import com.example.ordertrackproadmin.utils.EditModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Objects;

public class EditFragment extends Fragment implements IEditFragment {
    private ViewPager2 pager;
    private TabLayout tabLayout;
    private final int[] tabIcons = {
            R.drawable.lunch_dining_fill0_wght400_grad0_opsz24,
            R.drawable.fastfood_fill0_wght400_grad0_opsz24,
            R.drawable.water_full_fill0_wght400_grad0_opsz24
    };
    private final String[] tabTexts = {
            "Ala Carts",
            "Rice Meal",
            "Drinks and Desert"
    };
    private EditModel editModel;
    private LinearLayout secondPhase;
    private ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        secondPhase = view.findViewById(R.id.items);
        progressBar = view.findViewById(R.id.progressBar);
        ImageView add = view.findViewById(R.id.add);

        editModel = new EditModel();
        editModel.getProducts(this);

        pager = view.findViewById(R.id.pager);
        tabLayout = view.findViewById(R.id.tabLayout);

        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            // This method is triggered when there is any scrolling activity for the current page
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        add.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), AddItemActivity.class));
        });
        return view;
    }

    @Override
    public void getProducts(ArrayList<ArrayList<EditModel>> models) {
        progressBar.setVisibility(View.GONE);

        ArrayList<EditRecyclerViewAdapter> adapters = getEditRecyclerViewAdapters(models);
        EditFragmentAdapter adapter = new EditFragmentAdapter(adapters, getContext());
        pager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, pager,
                (tab, position) -> tab.setText(tabTexts[position])).attach();

        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(tabIcons[0]);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(tabIcons[1]);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(tabIcons[2]);

        secondPhase.setVisibility(View.VISIBLE);
    }

    @NonNull
    private ArrayList<EditRecyclerViewAdapter> getEditRecyclerViewAdapters(ArrayList<ArrayList<EditModel>> models) {
        EditRecyclerViewAdapter alaCartAdapter = new EditRecyclerViewAdapter(models.get(0), getContext(), getActivity(), EditFragment.this, "Ala Carts");
        EditRecyclerViewAdapter riceMealAdapter = new EditRecyclerViewAdapter(models.get(2), getContext(), getActivity(), EditFragment.this, "Rice Meal");
        EditRecyclerViewAdapter drinksAndDessertAdapter = new EditRecyclerViewAdapter(models.get(1), getContext(), getActivity(), EditFragment.this, "Drinks and Desert");

        ArrayList<EditRecyclerViewAdapter> adapters = new ArrayList<>();
        adapters.add(alaCartAdapter);
        adapters.add(riceMealAdapter);
        adapters.add(drinksAndDessertAdapter);
        return adapters;
    }
}