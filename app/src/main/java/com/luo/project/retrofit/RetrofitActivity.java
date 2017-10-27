package com.luo.project.retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.luo.project.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitActivity extends AppCompatActivity {
    public static final String URL = "http://v.juhe.cn/toutiao/";

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serializable);

        textView = (TextView) findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        query();

        new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())//解析方法
                .baseUrl(URL)//主机地址
                .build().create(ApiService.class)
                .getResult("头条", "1e055d822e828e1f0d78ef05cde6f5f2")
                .enqueue(new Callback<Focus>() {
                    @Override
                    public void onResponse(Call<Focus> call, Response<Focus> response) {

                    }

                    @Override
                    public void onFailure(Call<Focus> call, Throwable t) {

                    }
                });
    }

    private void query() {
        //1.创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())//解析方法
                .baseUrl(URL)//主机地址
                .build();

        //2.创建访问API的请求
        ApiService service = retrofit.create(ApiService.class);
        Call<Focus> call = service.getResult("头条", "1e055d822e828e1f0d78ef05cde6f5f2");

        //3.发送请求
        call.enqueue(new Callback<Focus>() {

            @Override
            public void onResponse(Call<Focus> call, Response<Focus> response) {
                //4.处理结果
                Log.i(getClass().getName(), response.toString());
                Log.i(getClass().getName(), response.message());

                if (response.isSuccessful()) {
                    Focus result = response.body();
                    if (result != null) {
                        textView.setText(result.toString());
                        Log.i(getClass().getName(), result.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<Focus> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}