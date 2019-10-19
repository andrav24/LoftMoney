package tech.andrav.loftmoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private Api mApi;
    public static final int LOFT_REQUEST_CODE = 100;
    public static final String TOKEN = "token";
    private static final String USER_ID = "andrav";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        TabLayout tabLayout = findViewById(R.id.tabs);
        final ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new BudgetPagerAdapter(
                getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText(R.string.expences);
        tabLayout.getTabAt(1).setText(R.string.incomes);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int activeFragmentIndex = viewPager.getCurrentItem();
                Fragment activeFragment = getSupportFragmentManager().getFragments().get(activeFragmentIndex);
                activeFragment.startActivityForResult(new Intent(
                    MainActivity.this, AddItemActivity.class), LOFT_REQUEST_CODE);
            }
        });

        mApi = ((LoftApp) getApplication()).getApi();

        final String token = PreferenceManager.getDefaultSharedPreferences(this).getString(TOKEN, "");
        if (TextUtils.isEmpty(token)) {

            Call<Status> auth = mApi.auth(USER_ID);
            auth.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response) {
                    SharedPreferences.Editor editor =
                            PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();

                    editor.putString(TOKEN, response.body().getToken());
                    editor.apply();

                }

                @Override
                public void onFailure(Call<Status> call, Throwable t) {

                }
            });
        }
    }

    static class BudgetPagerAdapter extends FragmentPagerAdapter {

        static final String COLOR_ID = "colorId";
        static final String TYPE = "fragmentType";
        static final String EXPENSE = "expense";
        static final String INCOME = "income";

        public BudgetPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            switch (position){
                case 0:
                    bundle.putInt(COLOR_ID, R.color.dark_sky_blue);
                    bundle.putString(TYPE, EXPENSE);
                    break;
                case 1:
                    bundle.putInt(COLOR_ID, R.color.apple_green);
                    bundle.putString(TYPE, INCOME);
                    break;
                default:
                    return null;
            }
            return BudgetFragment.newInstance(bundle);
        }

        @Override
        public int getCount() {
            return 2;   // return number of tabs
        }
    }
}
