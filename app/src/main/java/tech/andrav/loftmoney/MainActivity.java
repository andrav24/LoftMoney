package tech.andrav.loftmoney;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    ItemsAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private final int LOFT_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addItemButton = findViewById(R.id.add_item_button);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(
                        MainActivity.this, AddItemActivity.class), LOFT_REQUEST_CODE);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.budget_item_list);

        mAdapter = new ItemsAdapter();
        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));


        mAdapter.addItem(new Item("Молоко", 50));
        mAdapter.addItem(new Item("Сыр", 150));
        mAdapter.addItem(new Item("Ветчина", 150));
        mAdapter.addItem(new Item(
                "Что-то с оооооочень большим названием и большой ценой",
                50000000));

    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int price;
        try {
            price = Integer.parseInt(data.getStringExtra("price"));
        } catch (NumberFormatException e) {
            price = 0;
        }

        if (requestCode == LOFT_REQUEST_CODE && resultCode == RESULT_OK) {
            mAdapter.addItem(new Item(data.getStringExtra("name"), price));
        }
    }
}
