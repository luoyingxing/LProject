package com.luo.project.retrofit;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * author:  luoyingxing
 * date: 2017/10/26.
 */
public interface ApiService {
//    "http://api.dagoogle.cn/news/get-news?tableNum=1&page=1&pagesize=10";

    @GET("index")
    Call<Focus> getResult(@Query("type") String type,
                          @Query("key") String page);
}