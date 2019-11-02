package tech.andrav.loftmoney;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {

    public static final int LOFT_REQUEST_CODE = 100;
    public static final String COLOR_ID = "colorId";
    public static final String TYPE = "fragmentType";
    public static final String EXPENSE = "expense";
    public static final String INCOME = "income";
    public static final String TOKEN = "token";
    public static final String BACKGROUND_ID = "backgroundId";

    private TabLayout mTabLayout;
    private Toolbar mToolbar;
    private FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getSupportActionBar().hide();

        mToolbar = findViewById(R.id.toolbar);
        mTabLayout = findViewById(R.id.tabs);
        final ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new BudgetPagerAdapter(
                getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));

        /*
            метод вызывается при смене страниц View Pager
         */
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                /*
                switch (position) {
                    case 0:
                    case 1:
                        mFloatingActionButton.show();
                        BudgetFragment activeFragment = (BudgetFragment) getSupportFragmentManager().getFragments().get(position);
                        ActionMode actionMode = ((BudgetFragment) getSupportFragmentManager().getFragments().get(position)).getActionMode();
                        if (actionMode != null) {
                            actionMode.finish();
                        }
                        break;
                    case 2:
                        mFloatingActionButton.hide();
                        break;
                }

                 */
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    mFloatingActionButton.hide();
                } else {
                    mFloatingActionButton.show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                ActionMode actionMode;
                final int position = viewPager.getCurrentItem();
                Fragment fragment = getSupportFragmentManager().getFragments().get(position);
                if (fragment instanceof BudgetFragment) {
                    actionMode = ((BudgetFragment)fragment).getActionMode();
                } else {
                    actionMode = null;
                }
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                    case ViewPager.SCROLL_STATE_SETTLING:
                        if (null != actionMode) actionMode.finish();
                        break;
                }
            }
        });

        mTabLayout.setupWithViewPager(viewPager);
        mTabLayout.getTabAt(0).setText(R.string.expences);
        mTabLayout.getTabAt(1).setText(R.string.incomes);
        mTabLayout.getTabAt(2).setText(R.string.balance);

        mFloatingActionButton = findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int activeFragmentIndex = viewPager.getCurrentItem();
                Fragment activeFragment = getSupportFragmentManager().getFragments().get(activeFragmentIndex);
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                switch (activeFragmentIndex) {
                    case 0:
                        intent.putExtra(COLOR_ID, R.color.dark_sky_blue);
                        break;
                    case 1:
                        intent.putExtra(COLOR_ID, R.color.apple_green);
                        break;
                }

                activeFragment.startActivityForResult(intent, LOFT_REQUEST_CODE);
                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
            }
        });

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof BudgetFragment) {
                ((BudgetFragment)fragment).loadItems();
            }
        }
    }

    public void loadBalance() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof BalanceFragment) {
                ((BalanceFragment)fragment).loadBalance();
            }
        }
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {
        super.onActionModeStarted(mode);
        mTabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_grey_blue));
        mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_grey_blue));
        mFloatingActionButton.hide();
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);
        mTabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mFloatingActionButton.show();
    }

    static class BudgetPagerAdapter extends FragmentPagerAdapter {

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
                    bundle.putInt(BACKGROUND_ID, R.drawable.expense_tab_background);
                    return BudgetFragment.newInstance(bundle);
                case 1:
                    bundle.putInt(COLOR_ID, R.color.apple_green);
                    bundle.putString(TYPE, INCOME);
                    bundle.putInt(BACKGROUND_ID, R.drawable.income_tab_background);
                    return BudgetFragment.newInstance(bundle);
                case 2:
                    return BalanceFragment.newInstance();
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return 3;   // return number of tabs
        }
    }
}
