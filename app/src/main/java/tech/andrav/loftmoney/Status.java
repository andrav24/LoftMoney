package tech.andrav.loftmoney;

import com.google.gson.annotations.SerializedName;

public class Status {
    private String status;
    private int id;

    // эта аннатация говорит, что поле auth-token должно парситься в поле token объекта
    @SerializedName("auth_token")
    private String token;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
