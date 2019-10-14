package tech.andrav.loftmoney;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class BudgetFragment extends Fragment {

    ItemsAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private final int LOFT_REQUEST_CODE = 100;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_budget, null);

        Button addItemButton = view.findViewById(R.id.add_item_button);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), AddItemActivity.class), LOFT_REQUEST_CODE);
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.budget_item_list);

        mAdapter = new ItemsAdapter(getArguments()); // передаем bundle в recyclerViewAdapter
        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));


        mAdapter.addItem(new Item("Молоко", 50));
        mAdapter.addItem(new Item("Сыр", 150));
        mAdapter.addItem(new Item("Ветчина", 150));
        mAdapter.addItem(new Item(
                "Что-то с оооооочень большим названием и большой ценой",
                50000000));

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int price;
        try {
            price = Integer.parseInt(data.getStringExtra("price"));
        } catch (NumberFormatException e) {
            price = 0;
        }

        if (requestCode == LOFT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mAdapter.addItem(new Item(data.getStringExtra("name"), price));
        }
    }

    public static BudgetFragment newInstance(Bundle args) {
        BudgetFragment fragment = new BudgetFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
