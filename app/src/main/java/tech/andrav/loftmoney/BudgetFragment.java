package tech.andrav.loftmoney;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BudgetFragment extends Fragment {

    private ItemsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Api mApi;


    public static BudgetFragment newInstance(Bundle args) {
        BudgetFragment fragment = new BudgetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get a reference to the Api object
        mApi = ((LoftApp) getActivity().getApplication()).getApi();
        mAdapter = new ItemsAdapter(getArguments()); // pass bundle to recyclerViewAdapter
        mLayoutManager = new LinearLayoutManager(getActivity());
        loadItems();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_budget, null);

        RecyclerView recyclerView = view.findViewById(R.id.budget_item_list);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadItems();
            }
        });

        /*  adding data from code
        mAdapter.addItem(new Item("Молоко", 50));
        mAdapter.addItem(new Item("Сыр", 150));
        mAdapter.addItem(new Item("Ветчина", 150));
        mAdapter.addItem(new Item(
                "Что-то с оооооочень большим названием и большой ценой",
                50000000));
        */

        return view;
    }

    private void loadItems(){
        final String token = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(MainActivity.TOKEN, "");
        Call<List<Item>> items = mApi.getItems(getArguments().getString(MainActivity.TYPE), token);
        items.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                mAdapter.clearItems();
                mSwipeRefreshLayout.setRefreshing(false);
                List<Item> items = response.body();
                for (Item item : items) {
                    mAdapter.addItem(item);
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
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

        final int realPrice = price;

        if (requestCode == MainActivity.LOFT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            final String name = data.getStringExtra("name");

            final String token = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(MainActivity.TOKEN, "");

            // calling this method does not send data to the server
            Call<Status> call = mApi.addItem(new AddItemRequest(
                    price,
                    name,
                    getArguments().getString(MainActivity.TYPE)), token);

            // the data is queued and sent to the server
            // Callback<Status> - to read the response from the server
            call.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response) {
                    if (response.body().getStatus().equals("success")) {
                        mAdapter.addItem(new Item(name, realPrice));
                    }

                }

                @Override
                public void onFailure(Call<Status> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }
}
