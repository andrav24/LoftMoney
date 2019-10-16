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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BudgetFragment extends Fragment {

    private ItemsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Api mApi;

    private static final int LOFT_REQUEST_CODE = 100;


    public static BudgetFragment newInstance(Bundle args) {
        BudgetFragment fragment = new BudgetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // получаем ссылку на объект Api
        mApi = ((LoftApp) getActivity().getApplication()).getApi();
        loadItems();
    }

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

        /*  добавление данных из кода
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
        Call<List<Item>> items = mApi.getItems(getArguments().getString(MainActivity.BudgetPagerAdapter.TYPE), token);
        items.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                List<Item> items = response.body();
                for (Item item : items) {
                    mAdapter.addItem(item);
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {

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

        if (requestCode == LOFT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            final String name = data.getStringExtra("name");

            final String token = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(MainActivity.TOKEN, "");

            // вызов этого метода не отправляет данные на сервер
            Call<Status> call = mApi.addItem(new AddItemRequest(
                    price,
                    name,
                    getArguments().getString(MainActivity.BudgetPagerAdapter.TYPE)), token);

            // в этом методе данные ставятся в очередь и отправляются на сервер
            // Callback<Status> - для чтения ответа от сервера
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
