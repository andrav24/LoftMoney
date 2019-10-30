package tech.andrav.loftmoney;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BalanceFragment extends Fragment {

    private Api mApi;
    private TextView mTotalFinance;
    private TextView mMyExpences;
    private TextView mMyIncome;
    private DiagramView mDiagramView;


    public static Fragment newInstance() {
        return new BalanceFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApi = ((LoftApp) getActivity().getApplication()).getApi();
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_balance, null);
        mTotalFinance = view.findViewById(R.id.total_finance);
        mMyExpences = view.findViewById(R.id.my_expences);
        mMyIncome = view.findViewById(R.id.my_income);
        mDiagramView = view.findViewById(R.id.diagram_view);
        loadBalance();
        return view ;
    }

    public void loadBalance() {
        String token = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(MainActivity.TOKEN, "");
        Call<BalanceResponce> responceCall = mApi.getBalance(token);
        responceCall.enqueue(new Callback<BalanceResponce>() {
            @Override
            public void onResponse(Call<BalanceResponce> call, Response<BalanceResponce> response) {

                float expences = response.body().getTotalExpences();
                float income = response.body().getTotalIncome();

                mMyExpences.setText(String.format(Locale.ROOT,"%.2f", expences));
                mMyIncome.setText(String.format(Locale.ROOT,"%.2f", income));
                mTotalFinance.setText(String.format(Locale.ROOT,"%.2f", income - expences));
                mDiagramView.update(expences, income);
            }

            @Override
            public void onFailure(Call<BalanceResponce> call, Throwable t) {

            }
        });
    }
}
