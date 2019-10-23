package tech.andrav.loftmoney;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class AddItemActivity extends AppCompatActivity {

    private TextView mNameEditText;
    private TextView mPriceEditText;
    private Button mAddButton;
    private String mName;
    private String mPrice;
    private int mColorId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mColorId = getIntent().getIntExtra(MainActivity.COLOR_ID, R.color.design_default_color_on_secondary);

        mNameEditText = findViewById(R.id.name_edittext);
        mNameEditText.setTextColor(getResources().getColor(mColorId));
        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(
                    final CharSequence s, final int start, final int before, final int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                mName = s.toString();
                checkEditTextHasText();
            }
        });

        mPriceEditText = findViewById(R.id.price_edittext);
        mPriceEditText.setTextColor(getResources().getColor(mColorId));
        mPriceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(
                    final CharSequence s, final int start, final int before, final int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                mPrice = s.toString();
                checkEditTextHasText();
            }
        });

        mAddButton = findViewById(R.id.add_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(mName) && !TextUtils.isEmpty(mPrice)) {
                    setResult(
                            RESULT_OK,
                            new Intent()
                                    .putExtra("name", mName)
                                    .putExtra("price", mPrice));
                    finish();
                }
            }
        });
    }

    public void checkEditTextHasText() {
        mAddButton.setEnabled(!TextUtils.isEmpty(mName) && !TextUtils.isEmpty(mPrice));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
    }
}
