package tech.andrav.loftmoney;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    /*
      метод для авторизации на сервере

     */
    @GET("auth")
    Call<Status> auth(@Query("social_user_id") String userId);

    /*
      метод получения всех items,
      слово в скобках взято из API сервера, куда будем отправлять запрос,
      тип запроса @GET или @POST - надо смотреть API сервера,
      параметр в @GET - из API сервера,
      параметр в @Query берется из API сервера,
     */
    @GET("items")
    Call<List<Item>> getItems(@Query("type") String type, @Query("auth-token") String token);


    /*
        Запрос на добавление новых item,
        Параметров нет. Есть только тело запроса.
     */
    @POST("items/add")
    Call<Status> addItem(@Body AddItemRequest request, @Query("auth-token") String token);


    @POST("items/remove")
    Call<Status> removeItem(@Query("id") String id, @Query("auth-token") String token);
}
