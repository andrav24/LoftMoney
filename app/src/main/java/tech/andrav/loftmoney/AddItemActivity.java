package tech.andrav.loftmoney;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class AddItemActivity extends AppCompatActivity {

    private TextView mNameEditText;
    private TextView mPriceEditText;
    private Button mAddButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mNameEditText = findViewById(R.id.name_edittext);
        mPriceEditText = findViewById(R.id.price_edittext);
        mAddButton = findViewById(R.id.add_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNameEditText.getText().toString();
                String price = mPriceEditText.getText().toString();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(price)) {
                    setResult(
                            RESULT_OK,
                            new Intent().putExtra("name", name)
                                        .putExtra("price", price));
                    finish();
                }

            }
        });
    }
}
