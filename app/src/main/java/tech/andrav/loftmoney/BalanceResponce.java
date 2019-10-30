package tech.andrav.loftmoney;

import com.google.gson.annotations.SerializedName;

public class BalanceResponce {

    private String status;

    @SerializedName("total_expenses")
    private float totalExpences;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getTotalExpences() {
        return totalExpences;
    }

    public void setTotalExpences(float totalExpences) {
        this.totalExpences = totalExpences;
    }

    public float getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(float totalIncome) {
        this.totalIncome = totalIncome;
    }

    @SerializedName("total_income")
    private float totalIncome;
}
