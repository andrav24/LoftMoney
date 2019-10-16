package tech.andrav.loftmoney;

import android.app.Application;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


// singleton-object
// необходимо добавить в Манифест в тег android:name
public class LoftApp extends Application {

    private Api mApi;

    @Override
    public void onCreate() {
        super.onCreate();

        // создание http клиента
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://loftschool.com/android-api/basic/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        mApi = retrofit.create(Api.class);
    }

    public Api getApi() {
        return mApi;
    }

}


